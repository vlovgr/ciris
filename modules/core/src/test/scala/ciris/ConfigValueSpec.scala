package ciris

import cats.effect.{Blocker, ContextShift, IO, Sync}
import cats.effect.laws.util.TestContext
import cats.Eq
import cats.implicits._
import cats.laws.discipline.{ApplyTests, FlatMapTests, NonEmptyParallelTests}
import org.scalacheck.{Arbitrary, Gen}
import org.scalacheck.Arbitrary.arbitrary
import org.scalatest.Assertion
import org.scalatest.funspec.AnyFunSpec
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

final class ConfigValueSpec extends BaseSpec {
  implicit val contextShift: ContextShift[IO] =
    IO.contextShift(concurrent.ExecutionContext.global)

  val defaultValue: String = "defaultValue"
  val defaultError: ConfigError = ConfigError.Empty
  val default: ConfigValue[String] = ConfigValue.default(defaultValue)

  val defaultValue2: String = "defaultValue2"
  val defaultKey2: ConfigKey = ConfigKey("defaultKey2")
  val defaultError2: ConfigError = ConfigError.Missing(defaultKey2)
  val default2: ConfigValue[String] = ConfigValue.missing(defaultKey2).default(defaultValue2)

  val failedErrorMessage: String = "failedErrorMessage"
  val failedError: ConfigError = ConfigError(failedErrorMessage)
  val failed: ConfigValue[String] = ConfigValue.failed(failedError)

  val failedErrorMessage2: String = "failedErrorMessage2"
  val failedError2: ConfigError = ConfigError(failedErrorMessage2)
  val failed2: ConfigValue[String] = ConfigValue.failed(failedError2)

  val loadedValue: String = "loadedValue"
  val loadedKey: ConfigKey = ConfigKey("loadedKey")
  val loadedError: ConfigError = ConfigError.Loaded
  val loaded: ConfigValue[String] = ConfigValue.loaded(loadedKey, loadedValue)

  val loadedValue2: String = "loadedValue2"
  val loadedKey2: ConfigKey = ConfigKey("loadedKey2")
  val loadedError2: ConfigError = ConfigError.Loaded
  val loaded2: ConfigValue[String] = ConfigValue.loaded(loadedKey2, loadedValue2)

  val missingKey: ConfigKey = ConfigKey("missingKey")
  val missingError: ConfigError = ConfigError.Missing(missingKey)
  val missing: ConfigValue[String] = ConfigValue.missing(missingKey)

  val missingKey2: ConfigKey = ConfigKey("missingKey2")
  val missingError2: ConfigError = ConfigError.Missing(missingKey2)
  val missing2: ConfigValue[String] = ConfigValue.missing(missingKey2)

  def failedWith[A](error: ConfigError): ConfigValue[A] =
    ConfigValue.failed(error)

  def loadedWith[A](error: ConfigError, key: Option[ConfigKey], value: A): ConfigValue[A] =
    ConfigValue.pure(ConfigEntry.Loaded(error, key, value))

  def defaultWith[A](error: ConfigError, value: A): ConfigValue[A] =
    ConfigValue.pure(ConfigEntry.Default(error, () => value))

  def check[A](actual: ConfigValue[A], expected: ConfigValue[A])(implicit eq: Eq[A]): Assertion = {
    val eqEntry = Eq[ConfigEntry[A]]
    val actualEntry = actual.to[IO].unsafeRunSync
    val expectedEntry = expected.to[IO].unsafeRunSync

    withClue(s"actual: $actualEntry") {
      withClue(s"expected: $expectedEntry") {
        assert(eqEntry.eqv(actualEntry, expectedEntry))
      }
    }
  }

  def checkError[A](actual: ConfigValue[A], expected: ConfigError): Assertion = {
    val eqError = Eq[ConfigError]
    val actualError = actual.to[IO].unsafeRunSync.error

    withClue(s"actual: $actualError") {
      withClue(s"expected: $expected") {
        assert(eqError.eqv(actualError, expected))
      }
    }
  }

  def checkAttempt[A](actual: ConfigValue[A], expected: Either[ConfigError, A])(
    implicit eq: Eq[A]
  ): Assertion = {
    val actualAttempted = actual.attempt[IO].unsafeRunSync
    val attemptEq = Eq[Either[ConfigError, A]]

    withClue(s"actual: $actualAttempted") {
      withClue(s"expected: $expected") {
        assert(attemptEq.eqv(actualAttempted, expected))
      }
    }
  }

  def checkLoad[A](actual: ConfigValue[A], expected: A)(
    implicit eq: Eq[A]
  ): Assertion = {
    val actualLoaded = actual.load[IO].unsafeRunSync
    withClue(s"actual: $actualLoaded") {
      withClue(s"expected: $expected") {
        assert(eq.eqv(actualLoaded, expected))
      }
    }
  }

  def checkLoadFail[A](actual: ConfigValue[A]): Assertion =
    assert(actual.load[IO].attempt.unsafeRunSync.isLeft)

  implicit def configValueEq[A](
    implicit eq: Eq[A]
  ): Eq[ConfigValue[A]] = {
    Eq.instance { (v1, v2) =>
      val a1 = v1.to[IO].attempt.unsafeRunSync
      val a2 = v2.to[IO].attempt.unsafeRunSync

      (a1, a2) match {
        case (Left(e1: ConfigException), Left(e2: ConfigException)) => e1 === e2
        case (Right(e1), Right(e2))                                 => e1 === e2
        case (_, _)                                                 => false
      }
    }
  }

  implicit def configValueParEq[A](
    implicit eq: Eq[A]
  ): Eq[ConfigValue.Par[A]] = {
    Eq.instance { (v1, v2) =>
      val a1 = v1.unwrap.to[IO].attempt.unsafeRunSync
      val a2 = v2.unwrap.to[IO].attempt.unsafeRunSync

      (a1, a2) match {
        case (Left(e1: ConfigException), Left(e2: ConfigException)) => e1 === e2
        case (Right(e1), Right(e2))                                 => e1 === e2
        case (_, _)                                                 => false
      }
    }
  }

  test("ConfigValue.as.default success") {
    check(default.as[String], default)
  }

  test("ConfigValue.as.default error") {
    check(
      default.as[Int],
      failedWith[Int](defaultError.or(ConfigError.decode("Int", None, defaultValue)))
    )
  }

  test("ConfigValue.as.failed success") {
    check(failed.as[String], failed)
  }

  test("ConfigValue.as.failed error") {
    check(failed.as[Int], failed.asInstanceOf[ConfigValue[Int]])
  }

  test("ConfigValue.as.loaded success") {
    check(loaded.as[String], loaded)
  }

  test("ConfigValue.as.loaded error") {
    check(
      loaded.as[Int],
      failedWith[Int](loadedError.or(ConfigError.decode("Int", Some(loadedKey), loadedValue)))
    )
  }

  test("ConfigValue.as.missing success") {
    check(missing.as[String], missing)
  }

  test("ConfigValue.as.missing error") {
    check(missing.as[Int], missing.asInstanceOf[ConfigValue[Int]])
  }

  test("ConfigValue.async.default") {
    check({
      ConfigValue.async[String] { cb => cb(Right(default)) }
    }, default)
  }

  test("ConfigValue.async.error") {
    checkLoadFail {
      ConfigValue.async[String] { cb => cb(Left(new RuntimeException)) }
    }
  }

  test("ConfigValue.async.failed") {
    check({
      ConfigValue.async[String] { cb => cb(Right(failed)) }
    }, failed)
  }

  test("ConfigValue.async.loaded") {
    check({
      ConfigValue.async[String] { cb => cb(Right(loaded)) }
    }, loaded)
  }

  test("ConfigValue.async.missing") {
    check({
      ConfigValue.async[String] { cb => cb(Right(missing)) }
    }, missing)
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

  test("ConfigValue.blockOn.default") {
    Blocker[IO].use { blocker =>
      val value = ConfigValue.blockOn(blocker)(default)
      IO(check(value, default))
    }.unsafeRunSync
  }

  test("ConfigValue.blockOn.failed") {
    Blocker[IO].use { blocker =>
      val value = ConfigValue.blockOn(blocker)(failed)
      IO(check(value, failed))
    }.unsafeRunSync
  }

  test("ConfigValue.blockOn.loaded") {
    Blocker[IO].use { blocker =>
      val value = ConfigValue.blockOn(blocker)(loaded)
      IO(check(value, loaded))
    }.unsafeRunSync
  }

  test("ConfigValue.blockOn.missing") {
    Blocker[IO].use { blocker =>
      val value = ConfigValue.blockOn(blocker)(missing)
      IO(check(value, missing))
    }.unsafeRunSync
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
    forAll { (value: ConfigValue[String], a: String) =>
      assert(value.default(a) === value.or(ciris.default(a)))
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
      ConfigValue.eval(IO.raiseError[ConfigValue[String]](ConfigError("").throwable))
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

  checkAll("ConfigValue", {
    implicit val testContext: TestContext = TestContext()
    FlatMapTests[ConfigValue].flatMap[String, String, String]
  })

  test("ConfigValue.flatMap.default >> default") {
    check(default >> default2, default2)
  }

  test("ConfigValue.flatMap.default >> failed") {
    check(default >> failed, failed)
  }

  test("ConfigValue.flatMap.default >> loaded") {
    check(default >> loaded, loaded)
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
    check(loaded >> default, default)
  }

  test("ConfigValue.flatMap.loaded >> failed") {
    check(loaded >> failed, failed)
  }

  test("ConfigValue.flatMap.loaded >> loaded") {
    check(loaded >> loaded2, loaded2)
  }

  test("ConfigValue.flatMap.loaded >> missing") {
    check(loaded >> missing, missing)
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
    check(failed.option, failed.asInstanceOf[ConfigValue[Option[String]]])
  }

  test("ConfigValue.option.loaded") {
    check(loaded.option, loadedWith(loadedError, Some(loadedKey), loadedValue.some))
  }

  test("ConfigValue.option.missing") {
    check(missing.option, defaultWith(missingError, none[String]))
  }

  test("ConfigValue.option.eqv map(_.some).default(None)") {
    forAll { value: ConfigValue[String] =>
      assert(value.option === value.map(_.some).default(None))
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
      failedWith[String](missingError.or(failedError))
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
      failedWith[String](missingError.or(missingError2))
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
      failedWith[String](defaultError.or(failedError))
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

  checkAll("ConfigValue.parallel", {
    implicit val testContext: TestContext = TestContext()
    ApplyTests[ConfigValue.Par].apply[String, String, String]
  })

  checkAll("ConfigValue", {
    implicit val testContext: TestContext = TestContext()
    NonEmptyParallelTests[ConfigValue].nonEmptyParallel[String, String]
  })

  test("ConfigValue.parallel.(default, default).parTupled") {
    check(
      (default, default2).parTupled,
      defaultWith(ConfigError.Empty, (defaultValue, defaultValue2))
    )
  }

  test("ConfigValue.parallel.(default, failed).parTupled") {
    check(
      (default, failed).parTupled,
      failed.asInstanceOf[ConfigValue[(String, String)]]
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
      failedWith[(String, String)](missingError)
    )
  }

  test("ConfigValue.parallel.(failed, default).parTupled") {
    check(
      (failed, default).parTupled,
      failed.asInstanceOf[ConfigValue[(String, String)]]
    )
  }

  test("ConfigValue.parallel.(failed, failed).parTupled") {
    check(
      (failed, failed2).parTupled,
      failedWith[(String, String)](failedError.and(failedError2))
    )
  }

  test("ConfigValue.parallel.(failed, loaded).parTupled") {
    check(
      (failed, loaded).parTupled,
      failedWith[(String, String)](failedError.and(loadedError))
    )
  }

  test("ConfigValue.parallel.(failed, missing).parTupled") {
    check(
      (failed, missing).parTupled,
      failedWith[(String, String)](failedError.and(missingError))
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
      failedWith[(String, String)](loadedError.and(failedError))
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
      failedWith[(String, String)](loadedError.and(missingError))
    )
  }

  test("ConfigValue.parallel.(missing, default).parTupled") {
    check(
      (missing, default).parTupled,
      failedWith[(String, String)](missingError)
    )
  }

  test("ConfigValue.parallel.(missing, failed).parTupled") {
    check(
      (missing, failed).parTupled,
      failedWith[(String, String)](missingError.and(failedError))
    )
  }

  test("ConfigValue.parallel.(missing, loaded).parTupled") {
    check(
      (missing, loaded).parTupled,
      failedWith[(String, String)](missingError.and(loadedError))
    )
  }

  test("ConfigValue.parallel.(missing, missing).parTupled") {
    check(
      (missing, missing2).parTupled,
      failedWith[(String, String)](missingError.and(missingError2))
    )
  }

  test("ConfigValue.parFlatMap.default.parFlatMap(_ => default)") {
    check(
      default.parFlatMap(_ => default2),
      defaultWith(ConfigError.Empty, defaultValue2)
    )
  }

  test("ConfigValue.parFlatMap.default.parFlatMap(_ => failed)") {
    check(default.parFlatMap(_ => failed), failed)
  }

  test("ConfigValue.parFlatMap.default.parFlatMap(_ => loaded)") {
    check(
      default.parFlatMap(_ => loaded),
      loadedWith(ConfigError.Loaded, None, loadedValue)
    )
  }

  test("ConfigValue.parFlatMap.default.parFlatMap(_ => missing)") {
    check(default.parFlatMap(_ => missing), missing)
  }

  test("ConfigValue.parFlatMap.failed.parFlatMap(_ => default)") {
    check(failed.parFlatMap(_ => default), failed)
  }

  test("ConfigValue.parFlatMap.failed.parFlatMap(_ => failed)") {
    check(failed.parFlatMap(_ => failed2), failed)
  }

  test("ConfigValue.parFlatMap.failed.parFlatMap(_ => loaded)") {
    check(failed.parFlatMap(_ => loaded), failed)
  }

  test("ConfigValue.parFlatMap.failed.parFlatMap(_ => missing)") {
    check(failed.parFlatMap(_ => missing), failed)
  }

  test("ConfigValue.parFlatMap.loaded.parFlatMap(_ => default)") {
    check(
      loaded.parFlatMap(_ => default),
      loadedWith(ConfigError.Loaded, None, defaultValue)
    )
  }

  test("ConfigValue.parFlatMap.loaded.parFlatMap(_ => failed)") {
    check(
      loaded.parFlatMap(_ => failed),
      failedWith[String](ConfigError.Loaded.and(failedError))
    )
  }

  test("ConfigValue.parFlatMap.loaded.parFlatMap(_ => loaded)") {
    check(
      loaded.parFlatMap(_ => loaded2),
      loadedWith(ConfigError.Loaded, None, loadedValue2)
    )
  }

  test("ConfigValue.parFlatMap.loaded.parFlatMap(_ => missing)") {
    check(
      loaded.parFlatMap(_ => missing),
      failedWith[String](ConfigError.Loaded.and(missingError))
    )
  }

  test("ConfigValue.parFlatMap.missing.parFlatMap(_ => default)") {
    check(missing.parFlatMap(_ => default), missing)
  }

  test("ConfigValue.parFlatMap.missing.parFlatMap(_ => failed)") {
    check(missing.parFlatMap(_ => failed), missing)
  }

  test("ConfigValue.parFlatMap.missing.parFlatMap(_ => loaded)") {
    check(missing.parFlatMap(_ => loaded), missing)
  }

  test("ConfigValue.parFlatMap.missing.parFlatMap(_ => missing)") {
    check(missing.parFlatMap(_ => missing2), missing)
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

  test("ConfigValue.secret.default") {
    check(default.secret, defaultWith(defaultError, Secret(defaultValue)))
  }

  test("ConfigValue.secret.default sensitive") {
    val defaultSensitive =
      ConfigValue.pure(
        ConfigEntry.Default(
          ConfigError.sensitive("message", "redactedMessage"),
          () => defaultValue
        )
      )

    checkError(defaultSensitive.secret, ConfigError("redactedMessage"))
  }

  test("ConfigValue.secret.failed") {
    check(failed.secret, failed.asInstanceOf[ConfigValue[Secret[String]]])
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
    check(missing.secret, missing.asInstanceOf[ConfigValue[Secret[String]]])
  }
}
