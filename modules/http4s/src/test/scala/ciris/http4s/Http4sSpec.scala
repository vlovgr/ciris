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
import com.comcast.ip4s._

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

  test("hostConfigDecoder.success") {
    assert {
      val actual = default("localhost").as[Host].attempt[IO].unsafeRunSync()
      val expected = Right(host"localhost")
      actual === expected
    }

    assert {
      val actual = default("0.0.0.0").as[Host].attempt[IO].unsafeRunSync()
      val expected = Right(ip"0.0.0.0")
      actual === expected
    }

    assert {
      val actual = default("::1").as[Host].attempt[IO].unsafeRunSync()
      val expected = Right(ip"::1")
      actual === expected
    }

  }

  test("hostConfigDecoder.error") {
    default("🍋.🍊.🍉.🍐").as[Host].attempt[IO].unsafeRunSync().isLeft
  }

  test("portConfigDecoder.success") {

    assert {
      val actual = default("12345").as[Port].attempt[IO].unsafeRunSync()
      val expected = Right(port"12345")
      actual === expected
    }

  }

  test("portConfigDecoder.error") {

    assert {
      default("🚨").as[Port].attempt[IO].unsafeRunSync().isLeft
    }

  }
}
