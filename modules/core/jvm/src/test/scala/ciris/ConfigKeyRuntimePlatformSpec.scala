/*
 * Copyright 2017-2022 Viktor Lövgren
 *
 * SPDX-License-Identifier: MIT
 */

package ciris

import cats.tests.CatsSuite
import java.nio.charset.Charset
import java.nio.file.Path

final class ConfigKeyRuntimePlatformSpec extends CatsSuite with Generators {
  test("ConfigKey.file") {
    forAll { (path: Path, charset: Charset) =>
      val description = ConfigKey.file(path, charset).description
      assert(description === s"file at $path with charset $charset")
    }
  }
}
