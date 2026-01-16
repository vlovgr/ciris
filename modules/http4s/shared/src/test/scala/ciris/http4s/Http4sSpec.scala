/*
 * Copyright 2017-2026 Viktor Rudebeck
 *
 * SPDX-License-Identifier: MIT
 */

package ciris.http4s

import cats.data.NonEmptyList
import cats.effect.IO
import cats.syntax.all._
import ciris._
import com.comcast.ip4s._
import munit.CatsEffectSuite
import org.http4s.syntax.literals._
import org.http4s.Uri
import org.http4s.headers.Origin

final class Http4sSpec extends CatsEffectSuite {
  test("hostConfigDecoder.error") {
    default("").as[Host].attempt[IO].map(_.isLeft).assert
  }

  test("hostConfigDecoder.success") {
    val first =
      default("localhost").as[Host].attempt[IO].assertEquals(Right(host"localhost"))

    val second =
      default("0.0.0.0").as[Host].attempt[IO].assertEquals(Right(ip"0.0.0.0"))

    val third =
      default("::1").as[Host].attempt[IO].assertEquals(Right(ip"::1"))

    (first, second, third).tupled.void
  }

  test("portConfigDecoder.error") {
    default("").as[Port].attempt[IO].map(_.isLeft).assert
  }

  test("portConfigDecoder.success") {
    default("12345").as[Port].attempt[IO].assertEquals(Right(port"12345"))
  }

  test("uriConfigDecoder.error") {
    default("invalid uri").as[Uri].attempt[IO].map(_.isLeft).assert
  }

  test("uriConfigDecoder.success") {
    default("https://github.com/vlovgr/ciris")
      .as[Uri]
      .attempt[IO]
      .assertEquals(Right(uri"https://github.com/vlovgr/ciris"))
  }

  test("originConfigDecoder.error") {
    default("invalid origin").as[Origin].attempt[IO].map(_.isLeft).assert
  }

  test("originConfigDecoder.success") {
    default("https://cir.is:443 http://github.com")
      .as[Origin]
      .attempt[IO]
      .assertEquals(
        Right(
          Origin
            .HostList(
              NonEmptyList
                .of(
                  Origin.Host(Uri.Scheme.https, Uri.Host.fromIp4sHost(host"cir.is"), Some(443)),
                  Origin.Host(
                    Uri.Scheme.http,
                    Uri.Host.fromIp4sHost(host"github.com"),
                    None
                  )
                )
            )
        )
      )
  }
}
