/*
 * Copyright 2017-2022 Viktor Lövgren
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
  final def default[A](value: => A): ConfigValue[Effect, A] =
    ConfigValue.default(value)

  /**
    * Returns a new [[ConfigValue]] for the specified environment variable.
    */
  final def env(name: String): ConfigValue[Effect, String] =
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
  final def file(path: Path): ConfigValue[Effect, String] =
    file(path, StandardCharsets.UTF_8)

  /**
    * Returns a new [[ConfigValue]] for the file at the
    * specified path.
    *
    * The file contents are read synchronously using
    * the specified charset.
    */
  final def file(path: Path, charset: Charset): ConfigValue[Effect, String] =
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
  final def prop(name: String): ConfigValue[Effect, String] =
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
