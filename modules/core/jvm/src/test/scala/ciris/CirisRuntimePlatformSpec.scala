/*
 * Copyright 2017-2022 Viktor Rudebeck
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

final class CirisRuntimePlatformSpec
    extends CatsEffectSuite
    with ScalaCheckEffectSuite
    with Generators {

  def existingFile(charset: Charset, content: String): Path = {
    val path = Files.createTempFile("test-", ".tmp")
    path.toFile.deleteOnExit()
    Files.write(path, content.getBytes(charset))
    path
  }

  def nonExistingFile: Path = {
    val path = Files.createTempFile("test-", ".tmp")
    Files.deleteIfExists(path)
    path
  }

  test("env") {
    val envGen: Gen[String] =
      Gen.oneOf(
        Gen.oneOf(sys.env.keys.toList),
        arbitrary[String]
      )

    PropF.forAllF(envGen) { name =>
      val description = ConfigKey.env(name).description
      env(name)
        .to[IO]
        .use {
          case ConfigEntry.Loaded(ConfigError.Loaded, Some(ConfigKey(`description`)), value) =>
            IO(System.getenv(name) == value)

          case ConfigEntry.Failed(ConfigError.Missing(ConfigKey(`description`))) =>
            IO(System.getenv(name) == null)

          case _ =>
            IO.pure(false)
        }
        .assert
    }
  }

  test("file.path") {
    val pathContentGen: Gen[(Path, String)] =
      for {
        content <- Gen.alphaNumStr.filter(_.nonEmpty)
        path <- Gen.oneOf(
          Gen.const(existingFile(StandardCharsets.UTF_8, content)),
          Gen.const(nonExistingFile)
        )
      } yield (path, content)

    PropF
      .forAllF(pathContentGen) { case (path, content) =>
        val description = ConfigKey.file(path, StandardCharsets.UTF_8).description

        file(path)
          .to[IO]
          .use {
            case ConfigEntry.Loaded(ConfigError.Loaded, Some(ConfigKey(`description`)), value) =>
              IO.pure(value === content)

            case ConfigEntry.Failed(ConfigError.Missing(ConfigKey(`description`))) =>
              IO(!path.toFile.exists())

            case _ =>
              IO.pure(false)
          }
          .assert
      }
  }

  test("file.path.charset") {
    val pathContentCharsetGen: Gen[(Path, String, Charset)] =
      for {
        charset <- charsetGen
        content <- Gen.alphaNumStr.filter(_.nonEmpty)
        path <- Gen.oneOf(
          Gen.const(existingFile(charset, content)),
          Gen.const(nonExistingFile)
        )
      } yield (path, content, charset)

    PropF.forAllF(pathContentCharsetGen) { case (path, content, charset) =>
      val description = ConfigKey.file(path, charset).description
      file(path, charset)
        .to[IO]
        .use {
          case ConfigEntry.Loaded(ConfigError.Loaded, Some(ConfigKey(`description`)), value) =>
            IO.pure(value === content)

          case ConfigEntry.Failed(ConfigError.Missing(ConfigKey(`description`))) =>
            IO(!path.toFile.exists())

          case _ =>
            IO.pure(false)
        }
        .assert
    }
  }
}
