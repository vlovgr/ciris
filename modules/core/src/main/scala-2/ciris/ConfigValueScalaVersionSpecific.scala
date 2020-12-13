/*
 * Copyright 2017-2020 Viktor Lövgren
 *
 * SPDX-License-Identifier: MIT
 */

package ciris

import cats.effect.kernel.{Async, Resource}

private[ciris] trait ConfigValueScalaVersionSpecific {
  implicit final class ConfigValueExtension[F[_], A] private[ciris] (
    private val value: ConfigValue[F, A]
  ) {
    final def evalMap[G[x] >: F[x], B](f: A => G[B]): ConfigValue[G, B] =
      ConfigValue.create {
        new ConfigValue.Create[G, B] {
          override final def to[H[x] >: G[x]](implicit H: Async[H]): Resource[H, ConfigEntry[B]] =
            value.to[H].evalMap(_.traverse[H, B](f))
        }
      }

    final def evalMapK[G[x] >: F[x], B](f: Async[G] => A => G[B]): ConfigValue[G, B] =
      ConfigValue.create {
        new ConfigValue.Create[G, B] {
          override final def to[H[x] >: G[x]](implicit H: Async[H]): Resource[H, ConfigEntry[B]] =
            value.to[H].evalMap(_.traverse[H, B](f(H.asInstanceOf[Async[G]])))
        }
      }
  }

  final def eval[F[_], A](value: F[ConfigValue[F, A]]): ConfigValue[F, A] =
    ConfigValue.create {
      new ConfigValue.Create[F, A] {
        override final def to[G[x] >: F[x]](implicit G: Async[G]): Resource[G, ConfigEntry[A]] =
          Resource.liftF(value).flatMap(_.to[G])
      }
    }

  final def evalK[F[x] >: Effect[x], A](
    f: Async[F] => F[ConfigValue[F, A]]
  ): ConfigValue[F, A] =
    ConfigValue.create {
      new ConfigValue.Create[F, A] {
        override final def to[G[x] >: F[x]](implicit G: Async[G]): Resource[G, ConfigEntry[A]] =
          Resource.liftF(f(G.asInstanceOf[Async[F]])).flatMap(_.to[G])
      }
    }

  final def resource[F[_], A](resource: Resource[F, ConfigValue[F, A]]): ConfigValue[F, A] = {
    val _resource = resource
    ConfigValue.create {
      new ConfigValue.Create[F, A] {
        override final def to[G[x] >: F[x]](implicit G: Async[G]): Resource[G, ConfigEntry[A]] =
          _resource.flatMap(_.to[G])
      }
    }
  }

  final def resourceK[F[x] >: Effect[x], A](
    f: Async[F] => Resource[F, ConfigValue[F, A]]
  ): ConfigValue[F, A] = {
    val _f = f
    ConfigValue.create {
      new ConfigValue.Create[F, A] {
        override final def to[G[x] >: F[x]](implicit G: Async[G]): Resource[G, ConfigEntry[A]] =
          _f(G.asInstanceOf[Async[F]]).flatMap(_.to[G])
      }
    }
  }
}
