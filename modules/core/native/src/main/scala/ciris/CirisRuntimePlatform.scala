/*
 * Copyright 2017-2024 Viktor Rudebeck
 *
 * SPDX-License-Identifier: MIT
 */

package ciris

import java.nio.charset.{Charset, StandardCharsets}
import java.nio.file.{Files, Path}

private[ciris] trait CirisRuntimePlatform {

  private[ciris] final def getEnv(name: String): ConfigEntry[String] = {
    val key = ConfigKey.env(name)
    val value = System.getenv(name)

    if (value != null) {
      ConfigEntry.loaded(Some(key), value)
    } else {
      ConfigEntry.missing(key)
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

      if (path.toFile.isFile) {
        val bytes = Files.readAllBytes(path)
        val value = new String(bytes, charset)
        ConfigValue.loaded(key, value)
      } else {
        ConfigValue.missing(key)
      }
    }
}
