/*
 * Copyright 2017-2022 Viktor Lövgren
 *
 * SPDX-License-Identifier: MIT
 */

package ciris.http4s

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import ciris._
import org.http4s.syntax.literals._
import org.http4s.Uri
import org.scalatest.funsuite.AnyFunSuite

final class Http4sSpec extends AnyFunSuite {
  test("uriConfigDecoder.success") {
    assert {
      val actual = default("https://github.com/vlovgr/ciris").as[Uri].attempt[IO].unsafeRunSync()
      val expected = Right(uri"https://github.com/vlovgr/ciris")
      actual == expected
    }
  }

  test("uriConfigDecoder.error") {
    assert {
      default("invalid uri").as[Uri].attempt[IO].unsafeRunSync().isLeft
    }
  }
}
