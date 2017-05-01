package ciris

import ciris.ConfigError.MissingKey
import org.scalacheck.Gen
import org.scalatest.prop.PropertyChecks
import org.scalatest.{Matchers, WordSpec}

import scala.util.Try

class PropertySpec extends WordSpec with Matchers with PropertyChecks {
  def mixedCase(string: String): Gen[String] = {
    (for {
      lowers ← Gen.listOfN(string.length, Gen.oneOf(true, false))
      (lower, char) ← lowers zip string
    } yield if (lower) char.toLower else char.toUpper).map(_.mkString)
  }

  def fails[A](f: ⇒ A): Boolean = Try(f).isFailure

  def read[A](value: String)(implicit reader: ConfigReader[A]): Either[ConfigError, A] =
    reader.read("key")(sourceWith("key", value))

  def sourceWith(key: String, value: String): ConfigSource =
    new ConfigSource {
      override def read(key: String): Either[ConfigError, String] =
        Map(key → value).get(key).map(Right(_)).getOrElse(Left(MissingKey(key, this)))

      override def keyType: String = "test source"
    }
}
