/*
 * Copyright 2017-2022 Viktor Lövgren
 *
 * SPDX-License-Identifier: MIT
 */

package ciris

import cats.effect.IO
import cats.syntax.all._
import java.nio.charset.{Charset, StandardCharsets}
import java.nio.file.{Files, Path, StandardOpenOption}
import munit.{CatsEffectSuite, ScalaCheckEffectSuite}
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.effect.PropF
import org.scalacheck.Gen
import scala.scalajs.js

final class CirisRuntimePlatformSpec extends CatsEffectSuite with ScalaCheckEffectSuite {
  test("env") {
    val processEnv: js.Dictionary[Any] =
      js.Dynamic.global.process.env.asInstanceOf[js.Dictionary[Any]]

    def getenv(name: String): Option[String] =
      processEnv.get(name).collect { case value: String => value }

    val envGen: Gen[String] =
      Gen.oneOf(
        Gen.oneOf(processEnv.keySet),
        arbitrary[String]
      )

    PropF.forAllF(envGen) { (name: String) =>
      val description = ConfigKey.env(name).description
      env(name)
        .to[IO]
        .use {
          case ConfigEntry.Loaded(ConfigError.Loaded, Some(ConfigKey(`description`)), value) =>
            IO(getenv(name).contains(value))

          case ConfigEntry.Failed(ConfigError.Missing(ConfigKey(`description`))) =>
            IO(getenv(name).isEmpty)

          case _ =>
            IO.pure(false)
        }
        .assert
    }
  }
}
