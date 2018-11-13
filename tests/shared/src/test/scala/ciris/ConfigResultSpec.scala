package ciris

import ciris.api.{Id, MonadError}

final class ConfigResultSpec extends PropertySpec {
  "ConfigResult" when {
    "created with apply" should {
      "include the result in toStringWithResult" in {
        ConfigResult[Id, Int](Right(123)).toStringWithResult shouldBe "ConfigResult(Right(123))"
      }

      "not include the result in toString" in {
        ConfigResult[Id, Int](Right(123)).toString shouldNot be ("ConfigResult(Right(123))")
      }
    }

    "using orThrow" should {
      "return the configuration if loaded successfully" in {
        val config = loadConfig(
          readConfigEntry[String]("key1"),
          readConfigEntry[String]("key2")
        )(_ + _)

        noException shouldBe thrownBy {
          config.orThrow()
        }
      }

      "throw an exception if loading failed" in {
        val config = loadConfig(
          readConfigEntry[String]("key1"),
          readNonExistingConfigEntry[String]
        )(_ + _)

        a[ConfigException] shouldBe thrownBy {
          config.orThrow()
        }
      }
    }

    import _root_.cats.effect.IO
    import ciris.cats.effect._

    "using orRaiseErrors" should {
      type ErrorsOr[A] = Either[ConfigErrors, A]

      implicit val M: MonadError[ErrorsOr, ConfigErrors] =
        new MonadError[ErrorsOr, ConfigErrors] {
          override def raiseError[A](e: ConfigErrors): ErrorsOr[A] = Left(e)

          override def handleErrorWith[A](fa: ErrorsOr[A])(
            f: ConfigErrors => ErrorsOr[A]
          ): ErrorsOr[A] = fa.left.flatMap(f)

          override def pure[A](x: A): ErrorsOr[A] =
            Right(x)

          override def flatMap[A, B](fa: ErrorsOr[A])(f: A => ErrorsOr[B]): ErrorsOr[B] =
            fa.right.flatMap(f)

          override def product[A, B](fa: ErrorsOr[A], fb: ErrorsOr[B]): ErrorsOr[(A, B)] =
            fa.right.flatMap(a => fb.right.map(b => (a, b)))

          override def map[A, B](fa: ErrorsOr[A])(f: A => B): ErrorsOr[B] =
            fa.right.map(f)
        }

      "return the configuration if loaded successfully" in {
        val config = loadConfig(
          readConfigEntry[String]("key1").transformF[ErrorsOr],
          readConfigEntry[String]("key2").transformF[ErrorsOr]
        )(_ + _)

        config.orRaiseErrors shouldBe a[Right[_, _]]
      }

      "raise an error if loading failed" in {
        val config = loadConfig(
          readConfigEntry[String]("key1").transformF[ErrorsOr],
          readNonExistingConfigEntry[String].transformF[ErrorsOr]
        )(_ + _)

        config.orRaiseErrors shouldBe a[Left[_, _]]
      }
    }

    "using orRaiseThrowable" should {
      "return the configuration if loaded successfully" in {
        val config = loadConfig(
          readConfigEntry[String]("key1").transformF[IO],
          readConfigEntry[String]("key2").transformF[IO]
        )(_ + _)

        noException shouldBe thrownBy {
          config.orRaiseThrowable.unsafeRunSync()
        }
      }

      "raise an error if loading failed" in {
        val config = loadConfig(
          readConfigEntry[String]("key1").transformF[IO],
          readNonExistingConfigEntry[String].transformF[IO]
        )(_ + _)

        a[ConfigException] shouldBe thrownBy {
          config.orRaiseThrowable.unsafeRunSync()
        }
      }
    }
  }
}
