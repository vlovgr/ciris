/*
 * Copyright 2017-2021 Viktor Lövgren
 *
 * SPDX-License-Identifier: MIT
 */

package ciris

import cats.effect.kernel.{Resource, Sync}
import cats.implicits._
import java.util.Arrays
import java.util.concurrent.atomic.AtomicReference

/**
  * Secret configuration value which can only be used once
  * before being nullified.
  *
  * [[UseOnceSecret.apply]] wraps an `Array[Char]` ensuring
  * the array is only accessed once and that the array is
  * nullified once used. The array can be accessed with
  * [[UseOnceSecret#useOnce]] or alternatively, through
  * `Resource` using [[UseOnceSecret#resource]].
  *
  * [[ConfigValue#useOnceSecret]] can be used to wrap a
  * value in [[UseOnceSecret]], while also redacting
  * sentitive details from errors.
  */
sealed abstract class UseOnceSecret {

  /**
    * Returns a `Resource` which accesses the underlying
    * `Array[Char]` and nullifies it after use.
    *
    * In case the secret has already been used once, an
    * `IllegalStateException` will instead be raised.
    */
  def resource[F[_]](implicit F: Sync[F]): Resource[F, Array[Char]]

  /**
    * Returns an effect running the specified function
    * on the underlying `Array[Char]` and nullifies it
    * afterwards.
    *
    * In case the secret has already been used once, an
    * `IllegalStateException` will instead be raised.
    */
  def useOnce[F[_], A](f: Array[Char] => F[A])(implicit F: Sync[F]): F[A]
}

object UseOnceSecret {

  /**
    * Returns an effect which creates a new [[UseOnceSecret]]
    * for the specified secret configuration value.
    */
  final def apply[F[_]](secret: Array[Char])(implicit F: Sync[F]): F[UseOnceSecret] =
    F.delay(new AtomicReference(secret.some)).map { ref =>
      new UseOnceSecret {
        override final def resource[G[_]](implicit G: Sync[G]): Resource[G, Array[Char]] = {
          val acquire: G[Array[Char]] =
            G.delay(ref.getAndSet(None)).flatMap {
              case Some(secret) =>
                G.pure(secret)
              case None =>
                G.raiseError(new IllegalStateException("secret has already been used once"))
            }

          val release: G[Unit] =
            G.delay(Arrays.fill(secret, ' '))

          Resource.make(acquire)(_ => release)
        }

        override final def useOnce[G[_], A](f: Array[Char] => G[A])(implicit G: Sync[G]): G[A] =
          resource[G].use(f)

        override final def toString: String =
          "UseOnceSecret$" + System.identityHashCode(this)
      }
    }
}
