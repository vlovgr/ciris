/*
 * Copyright 2017-2025 Viktor Rudebeck
 *
 * SPDX-License-Identifier: MIT
 */

package ciris

import cats.Eq
import cats.effect.IO
import cats.effect.kernel.Resource
import cats.effect.kernel.Sync
import cats.laws.discipline.ApplyTests
import cats.laws.discipline.FlatMapTests
import cats.laws.discipline.NonEmptyParallelTests
import cats.syntax.all._
import munit.CatsEffectSuite
import munit.ScalaCheckEffectSuite
import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalacheck.effect.PropF
import java.util.Base64
import scala.util.Try

final class ConfigValueSpec extends CatsEffectSuite with ScalaCheckEffectSuite with Generators {
  val defaultValue: String = "defaultValue"
  val defaultError: ConfigError = ConfigError.Empty
  val default: ConfigValue[Effect, String] = ConfigValue.default(defaultValue)

  val defaultValue2: String = "defaultValue2"
  val defaultKey2: ConfigKey = ConfigKey("defaultKey2")
  val defaultError2: ConfigError = ConfigError.Missing(defaultKey2)
  val default2: ConfigValue[Effect, String] =
    ConfigValue.missing(defaultKey2).default(defaultValue2)

  val failedErrorMessage: String = "failedErrorMessage"
  val failedError: ConfigError = ConfigError(failedErrorMessage)
  val failed: ConfigValue[Effect, String] = ConfigValue.failed(failedError)

  val failedErrorMessage2: String = "failedErrorMessage2"
  val failedError2: ConfigError = ConfigError(failedErrorMessage2)
  val failed2: ConfigValue[Effect, String] = ConfigValue.failed(failedError2)

  val loadedValue: String = "loadedValue"
  val loadedKey: ConfigKey = ConfigKey("loadedKey")
  val loadedError: ConfigError = ConfigError.Loaded
  val loaded: ConfigValue[Effect, String] = ConfigValue.loaded(loadedKey, loadedValue)

  val loadedValue2: String = "loadedValue2"
  val loadedKey2: ConfigKey = ConfigKey("loadedKey2")
  val loadedError2: ConfigError = ConfigError.Loaded
  val loaded2: ConfigValue[Effect, String] = ConfigValue.loaded(loadedKey2, loadedValue2)

  val missingKey: ConfigKey = ConfigKey("missingKey")
  val missingError: ConfigError = ConfigError.Missing(missingKey)
  val missing: ConfigValue[Effect, String] = ConfigValue.missing(missingKey)

  val missingKey2: ConfigKey = ConfigKey("missingKey2")
  val missingError2: ConfigError = ConfigError.Missing(missingKey2)
  val missing2: ConfigValue[Effect, String] = ConfigValue.missing(missingKey2)

  def failedWith[F[_], A](error: ConfigError): ConfigValue[F, A] =
    ConfigValue.failed(error)

  def loadedWith[F[_], A](error: ConfigError, key: Option[ConfigKey], value: A): ConfigValue[F, A] =
    ConfigValue.pure(ConfigEntry.Loaded(error, key, value))

  def defaultWith[F[_], A](error: ConfigError, value: A): ConfigValue[F, A] =
    ConfigValue.pure(ConfigEntry.Default(error, value))

  def check[A](
    actual: ConfigValue[IO, A],
    expected: ConfigValue[IO, A]
  )(implicit eq: Eq[A]): IO[Unit] = {
    val eqEntry = Eq[ConfigEntry[A]]
    val actualEntry = actual.to[IO].use(IO.pure)
    val expectedEntry = expected.to[IO].use(IO.pure)

    (actualEntry, expectedEntry).mapN { case (actualEntry, expectedEntry) =>
      eqEntry.eqv(actualEntry, expectedEntry)
    }.assert
  }

  def checkError[A](actual: ConfigValue[IO, A], expected: ConfigError): IO[Unit] = {
    val eqError = Eq[ConfigError]
    val actualError = actual.to[IO].use(IO.pure).map(_.error)
    actualError.map(eqError.eqv(_, expected)).assert
  }

  def checkAttempt[A](actual: ConfigValue[IO, A], expected: Either[ConfigError, A])(
    implicit eq: Eq[A]
  ): IO[Unit] = {
    val actualAttempted = actual.attempt
    val attemptEq = Eq[Either[ConfigError, A]]
    actualAttempted.map(attemptEq.eqv(_, expected)).assert
  }

  def checkLoad[A](actual: ConfigValue[IO, A], expected: A)(
    implicit eq: Eq[A]
  ): IO[Unit] =
    actual.load.map(eq.eqv(_, expected)).assert

  def checkLoadFail[A](actual: ConfigValue[IO, A]): IO[Unit] =
    actual.load.attempt.map(_.isLeft).assert

  test("ConfigValue.as.default success") {
    check(default.as[String], default)
  }

  test("ConfigValue.as.default error") {
    check(
      default.as[Int],
      failedWith[IO, Int](defaultError.or(ConfigError.decode("Int", None, defaultValue)))
    )
  }

  test("ConfigValue.as.failed success") {
    check(failed.as[String], failed)
  }

  test("ConfigValue.as.failed error") {
    check(failed.as[Int], failed.asInstanceOf[ConfigValue[IO, Int]])
  }

  test("ConfigValue.as.loaded success") {
    check(loaded.as[String], loaded)
  }

  test("ConfigValue.as.loaded error") {
    check(
      loaded.as[Int],
      failedWith[IO, Int](loadedError.or(ConfigError.decode("Int", Some(loadedKey), loadedValue)))
    )
  }

  test("ConfigValue.as.missing success") {
    check(missing.as[String], missing)
  }

  test("ConfigValue.as.missing error") {
    check(missing.as[Int], missing.asInstanceOf[ConfigValue[IO, Int]])
  }

  test("ConfigValue.async.default") {
    check(
      ConfigValue.async[IO, String] { cb => IO(cb(Right(default))).as(None) },
      default
    )
  }

  test("ConfigValue.async.error") {
    checkLoadFail {
      ConfigValue.async[IO, String] { cb => IO(cb(Left(new RuntimeException))).as(None) }
    }
  }

  test("ConfigValue.async.failed") {
    check(
      ConfigValue.async[IO, String] { cb => IO(cb(Right(failed))).as(None) },
      failed
    )
  }

  test("ConfigValue.async.loaded") {
    check(
      ConfigValue.async[IO, String] { cb => IO(cb(Right(loaded))).as(None) },
      loaded
    )
  }

  test("ConfigValue.async.missing") {
    check(
      ConfigValue.async[IO, String] { cb => IO(cb(Right(missing))).as(None) },
      missing
    )
  }

  test("ConfigValue.async_.default") {
    check(
      ConfigValue.async_[IO, String] { cb => cb(Right(default)) },
      default
    )
  }

  test("ConfigValue.async_.error") {
    checkLoadFail {
      ConfigValue.async_[IO, String] { cb => cb(Left(new RuntimeException)) }
    }
  }

  test("ConfigValue.async_.failed") {
    check(
      ConfigValue.async_[IO, String] { cb => cb(Right(failed)) },
      failed
    )
  }

  test("ConfigValue.async_.loaded") {
    check(
      ConfigValue.async_[IO, String] { cb => cb(Right(loaded)) },
      loaded
    )
  }

  test("ConfigValue.async_.missing") {
    check(
      ConfigValue.async_[IO, String] { cb => cb(Right(missing)) },
      missing
    )
  }

  test("ConfigValue.attempt.default") {
    checkAttempt(default, Right(defaultValue))
  }

  test("ConfigValue.attempt.failed") {
    checkAttempt(failed, Left(failedError))
  }

  test("ConfigValue.attempt.loaded") {
    checkAttempt(loaded, Right(loadedValue))
  }

  test("ConfigValue.attempt.missing") {
    checkAttempt(missing, Left(missingError))
  }

  test("ConfigValue.default.default") {
    check(
      default.default(defaultValue2),
      defaultWith(defaultError, defaultValue2)
    )
  }

  test("ConfigValue.default.failed") {
    check(failed.default(defaultValue), failed)
  }

  test("ConfigValue.default.loaded") {
    check(loaded.default(defaultValue), loaded)
  }

  test("ConfigValue.default.missing") {
    check(
      missing.default(defaultValue),
      defaultWith(missingError, defaultValue)
    )
  }

  test("ConfigValue.default.eqv or(default)") {
    PropF.forAllF { (value: ConfigValue[IO, String], a: String) =>
      val default = value.default(a).to[IO].attempt
      val or = value.or(ciris.default(a)).to[IO].attempt

      (default, or).tupled.use {
        case (Left(_), Left(_))   => IO.pure(true)
        case (Right(a), Right(b)) => IO.pure(a === b)
        case (_, _)               => IO.pure(false)
      }.assert
    }
  }

  test("ConfigValue.eval.default") {
    check(
      ConfigValue.eval(IO(default)),
      default
    )
  }

  test("ConfigValue.eval.error") {
    checkLoadFail {
      ConfigValue.eval(IO.raiseError[ConfigValue[IO, String]](ConfigError("").throwable))
    }
  }

  test("ConfigValue.eval.failed") {
    check(
      ConfigValue.eval(IO(failed)),
      failed
    )
  }

  test("ConfigValue.eval.loaded") {
    check(
      ConfigValue.eval(IO(loaded)),
      loaded
    )
  }

  test("ConfigValue.eval.missing") {
    check(
      ConfigValue.eval(IO(missing)),
      missing
    )
  }

  test("ConfigValue.evalMap.default") {
    check(
      default.evalMap(s => IO(s ++ s)),
      defaultWith(defaultError, defaultValue ++ defaultValue)
    )
  }

  test("ConfigValue.evalMap.default error") {
    checkLoadFail {
      default.evalMap(_ => IO.raiseError[String](ConfigError("").throwable))
    }
  }

  test("ConfigValue.evalMap.failed") {
    check(
      failed.evalMap(s => IO(s ++ s)),
      failed
    )
  }

  test("ConfigValue.evalMap.failed error") {
    checkLoadFail {
      failed.evalMap(_ => IO.raiseError[String](ConfigError("").throwable))
    }
  }

  test("ConfigValue.evalMap.loaded") {
    check(
      loaded.evalMap(s => IO(s ++ s)),
      loadedWith(loadedError, Some(loadedKey), loadedValue ++ loadedValue)
    )
  }

  test("ConfigValue.evalMap.loaded error") {
    checkLoadFail {
      loaded.evalMap(_ => IO.raiseError[String](ConfigError("").throwable))
    }
  }

  test("ConfigValue.evalMap.missing") {
    check(
      missing.evalMap(s => IO(s ++ s)),
      missing
    )
  }

  test("ConfigValue.evalMap.missing error") {
    checkLoadFail {
      missing.evalMap(_ => IO.raiseError[String](ConfigError("").throwable))
    }
  }

  test("ConfigValue.flatMap.default >> default") {
    check(
      default >> default2,
      defaultWith(ConfigError.Empty, defaultValue2)
    )
  }

  test("ConfigValue.flatMap.default >> failed") {
    check(default >> failed, failed)
  }

  test("ConfigValue.flatMap.default >> loaded") {
    check(
      default >> loaded,
      loadedWith(ConfigError.Loaded, None, loadedValue)
    )
  }

  test("ConfigValue.flatMap.default >> missing") {
    check(default >> missing, missing)
  }

  test("ConfigValue.flatMap.failed >> default") {
    check(failed >> default, failed)
  }

  test("ConfigValue.flatMap.failed >> failed") {
    check(failed >> failed2, failed)
  }

  test("ConfigValue.flatMap.failed >> loaded") {
    check(failed >> loaded, failed)
  }

  test("ConfigValue.flatMap.failed >> missing") {
    check(failed >> missing, failed)
  }

  test("ConfigValue.flatMap.loaded >> default") {
    check(
      loaded >> default,
      loadedWith(ConfigError.Loaded, None, defaultValue)
    )
  }

  test("ConfigValue.flatMap.loaded >> failed") {
    check(
      loaded >> failed,
      failedWith[IO, String](ConfigError.Loaded.and(failedError))
    )
  }

  test("ConfigValue.flatMap.loaded >> loaded") {
    check(
      loaded >> loaded2,
      loadedWith(ConfigError.Loaded, None, loadedValue2)
    )
  }

  test("ConfigValue.flatMap.loaded >> missing") {
    check(
      loaded >> missing,
      failedWith[IO, String](ConfigError.Loaded.and(missingError))
    )
  }

  test("ConfigValue.flatMap.missing >> default") {
    check(missing >> default, missing)
  }

  test("ConfigValue.flatMap.missing >> failed") {
    check(missing >> failed, missing)
  }

  test("ConfigValue.flatMap.missing >> loaded") {
    check(missing >> loaded, missing)
  }

  test("ConfigValue.flatMap.missing >> missing") {
    check(missing >> missing2, missing)
  }

  test("ConfigValue.load.default") {
    checkLoad(default, defaultValue)
  }

  test("ConfigValue.load.failed") {
    checkLoadFail(failed)
  }

  test("ConfigValue.load.loaded") {
    checkLoad(loaded, loadedValue)
  }

  test("ConfigValue.load.missing") {
    checkLoadFail(missing)
  }

  test("ConfigValue.option.default") {
    check(default.option, defaultWith(defaultError, none[String]))
  }

  test("ConfigValue.option.failed") {
    check(failed.option, failed.asInstanceOf[ConfigValue[IO, Option[String]]])
  }

  test("ConfigValue.option.loaded") {
    check(loaded.option, loadedWith(loadedError, Some(loadedKey), loadedValue.some))
  }

  test("ConfigValue.option.missing") {
    check(missing.option, defaultWith(missingError, none[String]))
  }

  test("ConfigValue.option.eqv map(_.some).default(None)") {
    PropF.forAllF { (value: ConfigValue[IO, String]) =>
      val option = value.option.to[IO].attempt
      val default = value.map(_.some).default(None).to[IO].attempt

      (option, default).tupled.use {
        case (Left(_), Left(_))   => IO.pure(true)
        case (Right(a), Right(b)) => IO.pure(a === b)
        case (_, _)               => IO.pure(false)
      }.assert
    }
  }

  test("ConfigValue.or.failed or failed") {
    check(failed.or(failed2), failed)
  }

  test("ConfigValue.or.failed or loaded") {
    check(failed.or(loaded), failed)
  }

  test("ConfigValue.or.failed or missing") {
    check(failed.or(missing), failed)
  }

  test("ConfigValue.or.failed or default") {
    check(failed.or(default), failed)
  }

  test("ConfigValue.or.loaded or failed") {
    check(loaded.or(failed), loaded)
  }

  test("ConfigValue.or.loaded or loaded") {
    check(loaded.or(loaded2), loaded)
  }

  test("ConfigValue.or.loaded or missing") {
    check(loaded.or(missing), loaded)
  }

  test("ConfigValue.or.loaded or default") {
    check(loaded.or(default), loaded)
  }

  test("ConfigValue.or.missing or failed") {
    check(
      missing.or(failed),
      failedWith[IO, String](missingError.or(failedError))
    )
  }

  test("ConfigValue.or.missing or loaded") {
    check(
      missing.or(loaded),
      loadedWith(missingError.or(ConfigError.Loaded), Some(loadedKey), loadedValue)
    )
  }

  test("ConfigValue.or.missing or missing") {
    check(
      missing.or(missing2),
      failedWith[IO, String](missingError.or(missingError2))
    )
  }

  test("ConfigValue.or.missing or default") {
    check(
      missing.or(default),
      defaultWith(missingError.or(defaultError), defaultValue)
    )
  }

  test("ConfigValue.or.default or failed") {
    check(
      default.or(failed),
      failedWith[IO, String](defaultError.or(failedError))
    )
  }

  test("ConfigValue.or.default or loaded") {
    check(
      default.or(loaded),
      loadedWith(defaultError.or(ConfigError.Loaded), Some(loadedKey), loadedValue)
    )
  }

  test("ConfigValue.or.default or missing") {
    check(
      default.or(missing),
      defaultWith(defaultError.or(missingError), defaultValue)
    )
  }

  test("ConfigValue.or.default or default") {
    check(
      default.or(default2),
      defaultWith(defaultError.or(defaultError2), defaultValue2)
    )
  }

  test("ConfigValue.firstValid.loaded or loaded") {
    checkLoad(loaded.findValid(loaded2), loadedValue)
  }

  test("ConfigValue.firstValid.loaded or failed") {
    checkLoad(loaded.findValid(failed), loadedValue)
  }

  test("ConfigValue.firstValid.loaded or missing") {
    checkLoad(loaded.findValid(missing), loadedValue)
  }

  test("ConfigValue.firstValid.loaded or default") {
    checkLoad(loaded.findValid(default), loadedValue)
  }

  test("ConfigValue.firstValid.failed or loaded") {
    checkError(failed.findValid(loaded), failedError)
  }

  test("ConfigValue.firstValid.failed or failed") {
    checkError(failed.findValid(failed2), failedError)
  }

  test("ConfigValue.firstValid.failed or missing") {
    checkError(failed.findValid(missing), failedError)
  }

  test("ConfigValue.firstValid.failed or default") {
    checkError(failed.findValid(default), failedError)
  }

  test("ConfigValue.firstValid.missing or loaded") {
    checkLoad(missing.findValid(loaded), loadedValue)
  }

  test("ConfigValue.firstValid.missing or failed") {
    checkError(missing.findValid(failed), missingError.or(failedError))
  }

  test("ConfigValue.firstValid.missing or missing") {
    checkError(missing.findValid(missing2), missingError.or(missingError2))
  }

  test("ConfigValue.firstValid.missing or default") {
    checkLoad(missing.findValid(default), defaultValue)
  }

  test("ConfigValue.firstValid.default or loaded") {
    checkLoad(default.findValid(loaded), loadedValue)
  }

  test("ConfigValue.firstValid.default or failed") {
    checkError(default.findValid(failed), defaultError.or(failedError))
  }

  test("ConfigValue.firstValid.default or missing") {
    checkLoad(default.findValid(missing), defaultValue)
  }

  test("ConfigValue.firstValid.default or default") {
    checkLoad(default.findValid(default2), defaultValue2)
  }

  test("ConfigValue.parallel.(default, default).parTupled") {
    check(
      (default, default2).parTupled,
      defaultWith(ConfigError.Empty, (defaultValue, defaultValue2))
    )
  }

  test("ConfigValue.parallel.(default, failed).parTupled") {
    check(
      (default, failed).parTupled,
      failed.asInstanceOf[ConfigValue[IO, (String, String)]]
    )
  }

  test("ConfigValue.parallel.(default, loaded).parTupled") {
    check(
      (default, loaded).parTupled,
      loadedWith(ConfigError.Loaded, None, (defaultValue, loadedValue))
    )
  }

  test("ConfigValue.parallel.(default, missing).parTupled") {
    check(
      (default, missing).parTupled,
      failedWith[IO, (String, String)](missingError)
    )
  }

  test("ConfigValue.parallel.(failed, default).parTupled") {
    check(
      (failed, default).parTupled,
      failed.asInstanceOf[ConfigValue[IO, (String, String)]]
    )
  }

  test("ConfigValue.parallel.(failed, failed).parTupled") {
    check(
      (failed, failed2).parTupled,
      failedWith[IO, (String, String)](failedError.and(failedError2))
    )
  }

  test("ConfigValue.parallel.(failed, loaded).parTupled") {
    check(
      (failed, loaded).parTupled,
      failedWith[IO, (String, String)](failedError.and(loadedError))
    )
  }

  test("ConfigValue.parallel.(failed, missing).parTupled") {
    check(
      (failed, missing).parTupled,
      failedWith[IO, (String, String)](failedError.and(missingError))
    )
  }

  test("ConfigValue.parallel.(loaded, default).parTupled") {
    check(
      (loaded, default).parTupled,
      loadedWith(ConfigError.Loaded, None, (loadedValue, defaultValue))
    )
  }

  test("ConfigValue.parallel.(loaded, failed).parTupled") {
    check(
      (loaded, failed).parTupled,
      failedWith[IO, (String, String)](loadedError.and(failedError))
    )
  }

  test("ConfigValue.parallel.(loaded, loaded).parTupled") {
    check(
      (loaded, loaded2).parTupled,
      loadedWith(ConfigError.Loaded, None, (loadedValue, loadedValue2))
    )
  }

  test("ConfigValue.parallel.(loaded, missing).parTupled") {
    check(
      (loaded, missing).parTupled,
      failedWith[IO, (String, String)](loadedError.and(missingError))
    )
  }

  test("ConfigValue.parallel.(missing, default).parTupled") {
    check(
      (missing, default).parTupled,
      failedWith[IO, (String, String)](missingError)
    )
  }

  test("ConfigValue.parallel.(missing, failed).parTupled") {
    check(
      (missing, failed).parTupled,
      failedWith[IO, (String, String)](missingError.and(failedError))
    )
  }

  test("ConfigValue.parallel.(missing, loaded).parTupled") {
    check(
      (missing, loaded).parTupled,
      failedWith[IO, (String, String)](missingError.and(loadedError))
    )
  }

  test("ConfigValue.parallel.(missing, missing).parTupled") {
    check(
      (missing, missing2).parTupled,
      failedWith[IO, (String, String)](missingError.and(missingError2))
    )
  }

  test("ConfigValue.redacted.default") {
    val defaultSensitive =
      ConfigValue.pure(
        ConfigEntry.Default(
          ConfigError.sensitive("message", "redactedMessage"),
          () => defaultValue
        )
      )

    checkError(defaultSensitive.redacted, ConfigError("redactedMessage"))
  }

  test("ConfigValue.redacted.failed") {
    val failedSensitive =
      ConfigValue.failed[String](
        ConfigError.sensitive("message", "redactedMessage")
      )

    checkError(failedSensitive.redacted, ConfigError("redactedMessage"))
  }

  test("ConfigValue.redacted.loaded") {
    val loadedSensitive =
      ConfigValue.pure(
        ConfigEntry.Loaded(
          ConfigError.sensitive("message", "redactedMessage"),
          Some(loadedKey),
          loadedValue
        )
      )

    checkError(loadedSensitive.redacted, ConfigError("redactedMessage"))
  }

  test("ConfigValue.resource") {
    var acquired = false
    var released = false

    val acquire =
      IO {
        acquired = true
        ConfigValue.default("value")
      }

    val release =
      (_: ConfigValue[IO, String]) =>
        IO {
          released = true
          ()
        }

    val input =
      Resource.make(acquire)(release)

    val output =
      ConfigValue.resource(input).resource[IO]

    assert(!acquired && !released)

    output
      .use(_ => IO(assert(acquired)))
      .flatMap(_ => IO(assert(released)))
  }

  test("ConfigValue.secret.default") {
    check(default.secret, defaultWith(defaultError, Secret(defaultValue)))
  }

  test("ConfigValue.secret.default sensitive") {
    val defaultSensitive =
      ConfigValue.pure(
        ConfigEntry.Default(
          ConfigError.sensitive("message", "redactedMessage"),
          defaultValue
        )
      )

    checkError(defaultSensitive.secret, ConfigError("redactedMessage"))
  }

  test("ConfigValue.secret.failed") {
    check(failed.secret, failed.asInstanceOf[ConfigValue[IO, Secret[String]]])
  }

  test("ConfigValue.secret.failed sensitive") {
    val failedSensitive =
      ConfigValue.failed[String](
        ConfigError.sensitive("message", "redactedMessage")
      )

    checkError(failedSensitive.secret, ConfigError("redactedMessage"))
  }

  test("ConfigValue.secret.loaded") {
    check(loaded.secret, loadedWith(loadedError, Some(loadedKey), Secret(loadedValue)))
  }

  test("ConfigValue.secret.loaded sensitive") {
    val loadedSensitive =
      ConfigValue.pure(
        ConfigEntry.Loaded(
          ConfigError.sensitive("message", "redactedMessage"),
          Some(loadedKey),
          loadedValue
        )
      )

    checkError(loadedSensitive.secret, ConfigError("redactedMessage"))
  }

  test("ConfigValue.secret.missing") {
    check(missing.secret, missing.asInstanceOf[ConfigValue[IO, Secret[String]]])
  }

  def base64(s: String): String =
    new String(Base64.getEncoder.encode(s.getBytes("UTF-8")), "UTF-8")

  def decodeBase64(s: String): Try[String] =
    Try(new String(Base64.getDecoder.decode(s.getBytes("UTF-8")), "UTF-8"))

  test("ConfigValue.base64") {
    PropF.forAllF { (s: String) =>
      check(
        ConfigValue.default(base64(s)).base64,
        ConfigValue.default(s)
      )
    }
  }

  test("ConfigValue.secret.base64") {
    PropF.forAllF { (s: String) =>
      check(
        ConfigValue.default(base64(s)).secret.base64,
        ConfigValue.default(s).secret
      )
    }
  }

  test("ConfigValue.base64.error") {
    val gen = arbitrary[String].suchThat(decodeBase64(_).isFailure)
    PropF.forAllF(gen) { s =>
      checkError(
        ConfigValue.default(s).base64,
        ConfigError("Unable to base64 decode")
      )
    }
  }
}
