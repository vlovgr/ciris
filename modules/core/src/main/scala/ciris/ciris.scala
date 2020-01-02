/*
 * Copyright 2017-2020 Viktor Lövgren
 *
 * SPDX-License-Identifier: MIT
 */

import cats.effect.Blocker
import java.nio.charset.{Charset, StandardCharsets}
import java.nio.file.{Files, NoSuchFileException, Path}

package object ciris {

  /**
    * Returns a new [[ConfigValue]] with the specified default value.
    */
  final def default[A](value: => A): ConfigValue[A] =
    ConfigValue.default(value)

  /**
    * Returns a new [[ConfigValue]] for the specified environment variable.
    */
  final def env(name: String): ConfigValue[String] =
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
    * the `UTF-8` charset and `Blocker` instance.
    */
  final def file(path: Path, blocker: Blocker): ConfigValue[String] =
    file(path, blocker, StandardCharsets.UTF_8)

  /**
    * Returns a new [[ConfigValue]] for the file at the
    * specified path.
    *
    * The file contents are read synchronously using
    * the specified charset and `Blocker` instance.
    */
  final def file(path: Path, blocker: Blocker, charset: Charset): ConfigValue[String] =
    ConfigValue.blockOn(blocker) {
      ConfigValue.suspend {
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
    }

  /**
    * Returns a new [[ConfigValue]] for the specified system property.
    */
  final def prop(name: String): ConfigValue[String] =
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
