/*
 * Copyright 2017-2020 Viktor Lövgren
 *
 * SPDX-License-Identifier: MIT
 */

package ciris

import cats.effect.kernel.{Async, Resource}

private[ciris] trait ConfigValueScalaVersionSpecific {
  extension[F[x] >: Effect[x], A, B](value: ConfigValue[F, A]) {
    final def evalMap(f: Async[F] ?=> A => F[B]): ConfigValue[F, B] =
      ConfigValue.create {
        new ConfigValue.Create[F, B] {
          override final def to[G[x] >: F[x]](implicit G: Async[G]): Resource[G, ConfigEntry[B]] =
            value.to[G].evalMap(_.traverse[G, B](f(using (G.asInstanceOf[Async[F]]))))
        }
      }
  }

  def eval[F[x] >: Effect[x], A](f: Async[F] ?=> F[ConfigValue[F, A]]): ConfigValue[F, A] =
    ConfigValue.create {
      new ConfigValue.Create[F, A] {
        override final def to[G[x] >: F[x]](implicit G: Async[G]): Resource[G, ConfigEntry[A]] =
          Resource.liftF(f(using (G.asInstanceOf[Async[F]]))).flatMap(_.to[G])
      }
    }

  def resource[F[x] >: Effect[x], A](f: Async[F] ?=> Resource[F, ConfigValue[F, A]]): ConfigValue[F, A] =
    ConfigValue.create {
      new ConfigValue.Create[F, A] {
        override final def to[G[x] >: F[x]](implicit G: Async[G]): Resource[G, ConfigEntry[A]] =
          f(using (G.asInstanceOf[Async[F]])).flatMap(_.to[G])
      }
    }
}
