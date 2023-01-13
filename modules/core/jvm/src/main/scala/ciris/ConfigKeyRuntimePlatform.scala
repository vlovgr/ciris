/*
 * Copyright 2017-2023 Viktor Rudebeck
 *
 * SPDX-License-Identifier: MIT
 */

package ciris

import java.nio.charset.Charset
import java.nio.file.Path

private[ciris] trait ConfigKeyRuntimePlatform {

  /**
    * Returns a new [[ConfigKey]] for the specified path and charset.
    *
    * @group Create
    */
  final def file(path: Path, charset: Charset): ConfigKey =
    ConfigKey(s"file at $path with charset $charset")
}
