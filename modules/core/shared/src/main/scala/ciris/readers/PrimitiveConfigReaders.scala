package ciris.readers

import ciris.ConfigReader
import ciris.ConfigReader.{catchNonFatal, fromOption, withValue}

trait PrimitiveConfigReaders {
  implicit val booleanConfigReader: ConfigReader[Boolean] =
    catchNonFatal("Boolean")(_.toBoolean)

  implicit val byteConfigReader: ConfigReader[Byte] =
    catchNonFatal("Byte")(_.toByte)

  implicit val charConfigReader: ConfigReader[Char] =
    fromOption("Char") { value ⇒
      if (value.length == 1) Some(value.head) else None
    }

  implicit val doubleConfigReader: ConfigReader[Double] =
    catchNonFatal("Double") {
      case s if s.lastOption.exists(_ == '%') ⇒ s.dropRight(1).toDouble / 100d
      case s ⇒ s.toDouble
    }

  implicit val floatConfigReader: ConfigReader[Float] =
    catchNonFatal("Float") {
      case s if s.lastOption.exists(_ == '%') ⇒ s.dropRight(1).toFloat / 100f
      case s ⇒ s.toFloat
    }

  implicit val intConfigReader: ConfigReader[Int] =
    catchNonFatal("Int")(_.toInt)

  implicit val longConfigReader: ConfigReader[Long] =
    catchNonFatal("Long")(_.toLong)

  implicit val shortConfigReader: ConfigReader[Short] =
    catchNonFatal("Short")(_.toShort)

  implicit val stringConfigReader: ConfigReader[String] =
    withValue((_, value, _) ⇒ Right(value))
}
