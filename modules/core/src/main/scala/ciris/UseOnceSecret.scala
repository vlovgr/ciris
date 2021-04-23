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

trait UseOnceSecret {
  def resource[F[_]](implicit F: Sync[F]): Resource[F, Array[Char]]

  final def useOnce[F[_], A](f: Array[Char] => F[A])(implicit F: Sync[F]): F[A] =
    resource[F].use(f)
}

object UseOnceSecret {
  final def apply[F[_]](secret: Array[Char])(implicit F: Sync[F]): F[UseOnceSecret] =
    F.delay(new AtomicReference(secret.some)).map { ref =>
      new UseOnceSecret {
        override final def resource[G[_]](implicit G: Sync[G]): Resource[G, Array[Char]] = {
          val acquire: G[Array[Char]] =
            G.delay(ref.getAndSet(None)).flatMap {
              case Some(secret) =>
                G.pure(secret)
              case None =>
                G.raiseError(new IllegalStateException("Secret has already been used once"))
            }

          val release: G[Unit] =
            G.delay(Arrays.fill(secret, ' '))

          Resource.make(acquire)(_ => release)
        }
      }
    }
}
