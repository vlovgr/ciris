package ciris

import org.scalacheck.Gen
import org.scalatest.prop.PropertyChecks
import org.scalatest.{EitherValues, Matchers, WordSpec}

import scala.util.Try

class PropertySpec extends WordSpec with Matchers with PropertyChecks with EitherValues {
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

  def readConfigValue[A](value: String)(implicit decoder: ConfigDecoder[String, A]): ConfigValue[A] =
    ConfigValue("key")(sourceWith("key" -> value), decoder)

  def readValue[A](value: String)(implicit decoder: ConfigDecoder[String, A]): Either[ConfigError, A] =
    readConfigValue[A](value).value

  def readNonExistingConfigValue[A](implicit decoder: ConfigDecoder[String, A]): ConfigValue[A] =
    ConfigValue("key")(emptySource, decoder)

  def readNonExistingValue[A](implicit decoder: ConfigDecoder[String, A]): Either[ConfigError, A] =
    decoder.decode(emptySource.read("key"))

  def existingEntry(value: String): ConfigEntry[String, String, String] =
    sourceWith("key" -> value).read("key")

  val emptySource: ConfigSource[String] =
    sourceWith()

  val nonExistingEntry: ConfigEntry[String, String, String] =
    emptySource.read("key")

  def sourceWith(entries: (String, String)*): ConfigSource[String] =
    ConfigSource.fromMap(ConfigKeyType[String]("test key"))(entries.toMap)
}
