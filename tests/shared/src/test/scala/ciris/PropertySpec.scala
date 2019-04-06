package ciris

import ciris.api._
import org.scalacheck.Gen
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import org.scalatest.{EitherValues, Matchers, WordSpec}

import scala.util.Try

class PropertySpec extends WordSpec with Matchers with ScalaCheckPropertyChecks with EitherValues {
  override implicit val generatorDrivenConfig: PropertyCheckConfiguration =
    PropertyCheckConfiguration(minSuccessful = 1000)

  def sized[A](gen: Gen[A]): Gen[A] =
    Gen.resize(20, gen)

  def mixedCase(string: String): Gen[String] = {
    (for {
      lowers <- Gen.listOfN(string.length, Gen.oneOf(true, false))
      (lower, char) <- lowers zip string
    } yield if (lower) char.toLower else char.toUpper).map(_.mkString)
  }

  def mixedCaseEnum[T](values: Array[T])(f: T => String): Gen[(T, String)] =
    for {
      value <- Gen.oneOf(values)
      valueString <- mixedCase(f(value))
    } yield (value, valueString)

  def fails[A](f: => A): Boolean = Try(f).isFailure

  def readConfigEntry[A](value: String)(implicit decoder: ConfigDecoder[String, A]): ConfigEntry[Id, String, String, A] =
    sourceWith("key" -> value).read("key").decodeValue[A]

  def readValue[A](value: String)(implicit decoder: ConfigDecoder[String, A]): Either[ConfigError, A] =
    readConfigEntry[A](value).value

  def readNonExistingConfigEntry[A](implicit decoder: ConfigDecoder[String, A]): ConfigEntry[Id, String, String, A] =
    emptySource.read("key").decodeValue[A]

  def readNonExistingValue[A](implicit decoder: ConfigDecoder[String, A]): Either[ConfigError, A] =
    decoder.decode(emptySource.read("key"))

  def existingEntry(value: String): ConfigEntry[Id, String, String, String] =
    sourceWith("key" -> value).read("key")

  val emptySource: ConfigSource[Id, String, String] =
    sourceWith()

  val nonExistingEntry: ConfigEntry[Id, String, String, String] =
    emptySource.read("key")

  def sourceWith(entries: (String, String)*): ConfigSource[Id, String, String] =
    ConfigSource.fromMap(ConfigKeyType[String]("test key"))(entries.toMap)
}
