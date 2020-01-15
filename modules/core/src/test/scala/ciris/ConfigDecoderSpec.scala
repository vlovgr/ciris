package ciris

import cats.implicits._
import cats.laws.discipline.MonadErrorTests
import org.scalacheck.Gen
import scala.concurrent.duration.{Duration, FiniteDuration}
import scala.util.Try

final class ConfigDecoderSpec extends BaseSpec {
  test("ConfigDecoder.as") {
    forAll { (key: Option[ConfigKey], s: String) =>
      val expected = ConfigDecoder[String, Int].decode(key, s)
      val actual = ConfigDecoder[String].as[Int].decode(key, s)
      assert(actual === expected)
    }
  }

  test("ConfigDecoder.bigDecimal.success") {
    forAll { bigDecimal: BigDecimal =>
      assert(
        ConfigDecoder[String, BigDecimal].decode(None, bigDecimal.toString) === bigDecimal.asRight
      )
    }
  }

  test("ConfigDecoder.bigDecimal.failure") {
    forAll { s: String =>
      whenever(Try(BigDecimal(s)).isFailure) {
        assert(ConfigDecoder[String, BigDecimal].decode(None, s).isLeft)
      }
    }
  }

  test("ConfigDecoder.bigInt.success") {
    forAll { bigInt: BigInt =>
      assert(ConfigDecoder[String, BigInt].decode(None, bigInt.toString) === bigInt.asRight)
    }
  }

  test("ConfigDecoder.bigInt.failure") {
    forAll { s: String =>
      whenever(Try(BigInt(s)).isFailure) {
        assert(ConfigDecoder[String, BigInt].decode(None, s).isLeft)
      }
    }
  }

  test("ConfigDecoder.boolean.success") {
    val genBoolean =
      Gen.oneOf(
        "true",
        "yes",
        "on",
        "false",
        "no",
        "off"
      )

    forAll(genBoolean) { boolean =>
      assert(ConfigDecoder[String, Boolean].decode(None, boolean.toString).isRight)
    }
  }

  test("ConfigDecoder.boolean.failure") {
    def isBoolean(s: String) =
      s.toLowerCase() match {
        case "true" | "yes" | "on"  => true
        case "false" | "no" | "off" => true
        case _                      => false
      }

    forAll { s: String =>
      whenever(!isBoolean(s)) {
        assert(ConfigDecoder[String, Boolean].decode(None, s).isLeft)
      }
    }
  }

  test("ConfigDecoder.byte.success") {
    forAll { byte: Byte =>
      assert(ConfigDecoder[String, Byte].decode(None, byte.toString) === byte.asRight)
    }
  }

  test("ConfigDecoder.byte.failure") {
    forAll { s: String =>
      whenever(Try(s.toByte).isFailure) {
        assert(ConfigDecoder[String, Byte].decode(None, s).isLeft)
      }
    }
  }

  test("ConfigDecoder.char.success") {
    forAll { char: Char =>
      assert(ConfigDecoder[String, Char].decode(None, char.toString) === char.asRight)
    }
  }

  test("ConfigDecoder.char.failure") {
    forAll { s: String =>
      whenever(s.length !== 1) {
        assert(ConfigDecoder[String, Char].decode(None, s).isLeft)
      }
    }
  }

  test("ConfigDecoder.double.success") {
    forAll { double: Double =>
      assert(ConfigDecoder[String, Double].decode(None, double.toString) === double.asRight)
    }
  }

  test("ConfigDecoder.double.success percent") {
    forAll { double: Double =>
      assert(
        ConfigDecoder[String, Double]
          .decode(None, double.toString ++ "%") === (double / 100f).asRight
      )
    }
  }

  test("ConfigDecoder.double.failure") {
    forAll { s: String =>
      whenever(Try(s.toDouble).isFailure) {
        assert(ConfigDecoder[String, Double].decode(None, s).isLeft)
      }
    }
  }

  test("ConfigDecoder.duration.success") {
    forAll { duration: Duration =>
      val expected = Try(Duration(duration.toString))
      whenever(expected.isSuccess) {
        assert(ConfigDecoder[String, Duration].decode(None, duration.toString) == expected.toEither)
      }
    }
  }

  test("ConfigDecoder.duration.failure") {
    forAll { s: String =>
      whenever(Try(Duration(s)).isFailure) {
        assert(ConfigDecoder[String, Duration].decode(None, s).isLeft)
      }
    }
  }

  test("ConfigDecoder.finiteDuration.success") {
    forAll { finiteDuration: FiniteDuration =>
      val expected = Try(Duration(finiteDuration.toString))
      whenever(expected.isSuccess) {
        assert {
          ConfigDecoder[String, FiniteDuration]
            .decode(None, finiteDuration.toString) == expected.toEither
        }
      }
    }
  }

  test("ConfigDecoder.finiteDuration.failure") {
    forAll { s: String =>
      whenever(Try(Duration(s)).isFailure) {
        assert(ConfigDecoder[String, FiniteDuration].decode(None, s).isLeft)
      }
    }
  }

  test("ConfigDecoder.float.success") {
    forAll { float: Float =>
      assert(ConfigDecoder[String, Float].decode(None, float.toString) === float.asRight)
    }
  }

  test("ConfigDecoder.float.success percent") {
    forAll { float: Float =>
      assert(
        ConfigDecoder[String, Float].decode(None, float.toString ++ "%") === (float / 100f).asRight
      )
    }
  }

  test("ConfigDecoder.float.failure") {
    forAll { s: String =>
      whenever(Try(s.toFloat).isFailure) {
        assert(ConfigDecoder[String, Float].decode(None, s).isLeft)
      }
    }
  }

  test("ConfigDecoder.int.success") {
    forAll { int: Int =>
      assert(ConfigDecoder[String, Int].decode(None, int.toString) === int.asRight)
    }
  }

  test("ConfigDecoder.int.failure") {
    forAll { s: String =>
      whenever(Try(s.toInt).isFailure) {
        assert(ConfigDecoder[String, Int].decode(None, s).isLeft)
      }
    }
  }

  test("ConfigDecoder.long.success") {
    forAll { long: Long =>
      assert(ConfigDecoder[String, Long].decode(None, long.toString) === long.asRight)
    }
  }

  test("ConfigDecoder.long.failure") {
    forAll { s: String =>
      whenever(Try(s.toLong).isFailure) {
        assert(ConfigDecoder[String, Long].decode(None, s).isLeft)
      }
    }
  }

  checkAll(
    "ConfigDecoder",
    MonadErrorTests[ConfigDecoder[String, ?], ConfigError].monadError[String, String, String]
  )

  test("ConfigDecoder.short.success") {
    forAll { short: Short =>
      assert(ConfigDecoder[String, Short].decode(None, short.toString) === short.asRight)
    }
  }

  test("ConfigDecoder.short.failure") {
    forAll { s: String =>
      whenever(Try(s.toShort).isFailure) {
        assert(ConfigDecoder[String, Short].decode(None, s).isLeft)
      }
    }
  }

  test("ConfigDecoder.toString") {
    forAll { decoder: ConfigDecoder[String, String] =>
      assert(decoder.toString.startsWith("ConfigDecoder$"))
    }
  }
}
