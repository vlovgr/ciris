/*
 * Copyright 2017-2020 Viktor Lövgren
 *
 * SPDX-License-Identifier: MIT
 */

import java.nio.charset.{Charset, StandardCharsets}
import java.nio.file.{Files, NoSuchFileException, Path}

package object ciris {

  /**
    * Indicates a [[ConfigValue]] can be used with any effect type.
    */
  type Effect[A] <: Nothing

  /**
    * Returns a new [[ConfigValue]] with the specified default value.
    */
  final def default[F[x] >: Effect[x], A](value: => A): ConfigValue[F, A] =
    ConfigValue.default(value)

  /**
    * Returns a new [[ConfigValue]] for the specified environment variable.
    */
  final def env[F[x] >: Effect[x]](name: String): ConfigValue[F, String] =
    ConfigValue.suspend {
      val key = ConfigKey.env(name)
      val value = System.getenv(name)

      if (value != null) {
        ConfigValue.loaded(key, value)
      } else {
        ConfigValue.missing(key)
      }
    }

  /**
    * Returns a new [[ConfigValue]] for the file at the
    * specified path.
    *
    * The file contents are read synchronously using
    * the `UTF-8` charset.
    */
  final def file[F[x] >: Effect[x]](path: Path): ConfigValue[F, String] =
    file(path, StandardCharsets.UTF_8)

  /**
    * Returns a new [[ConfigValue]] for the file at the
    * specified path.
    *
    * The file contents are read synchronously using
    * the specified charset.
    */
  final def file[F[x] >: Effect[x]](path: Path, charset: Charset): ConfigValue[F, String] =
    ConfigValue.blocking {
      val key = ConfigKey.file(path, charset)

      try {
        val bytes = Files.readAllBytes(path)
        val value = new String(bytes, charset)
        ConfigValue.loaded(key, value)
      } catch {
        case _: NoSuchFileException =>
          ConfigValue.missing(key)
      }
    }

  /**
    * Returns a new [[ConfigValue]] for the specified system property.
    */
  final def prop[F[x] >: Effect[x]](name: String): ConfigValue[F, String] =
    ConfigValue.suspend {
      val key = ConfigKey.prop(name)

      if (name.nonEmpty) {
        val value = System.getProperty(name)
        if (value != null) {
          ConfigValue.loaded(key, value)
        } else {
          ConfigValue.missing(key)
        }
      } else {
        ConfigValue.missing(key)
      }
    }
}
