package ciris.decoders

import ciris.ConfigDecoder

trait PrimitiveConfigDecoders {
  implicit val booleanConfigDecoder: ConfigDecoder[String, Boolean] =
    ConfigDecoder.catchNonFatal[String]("Boolean")(_.toBoolean)

  implicit val byteConfigDecoder: ConfigDecoder[String, Byte] =
    ConfigDecoder.catchNonFatal[String]("Byte")(_.toByte)

  implicit val charConfigDecoder: ConfigDecoder[String, Char] =
    ConfigDecoder.fromOption[String]("Char") { value =>
      if (value.length == 1) Some(value.head) else None
    }

  implicit val doubleConfigDecoder: ConfigDecoder[String, Double] =
    ConfigDecoder.catchNonFatal[String]("Double") {
      case s if s.lastOption.exists(_ == '%') => s.init.toDouble / 100d
      case s => s.toDouble
    }

  implicit val floatConfigDecoder: ConfigDecoder[String, Float] =
    ConfigDecoder.catchNonFatal[String]("Float") {
      case s if s.lastOption.exists(_ == '%') => s.init.toFloat / 100f
      case s => s.toFloat
    }

  implicit val intConfigDecoder: ConfigDecoder[String, Int] =
    ConfigDecoder.catchNonFatal[String]("Int")(_.toInt)

  implicit val longConfigDecoder: ConfigDecoder[String, Long] =
    ConfigDecoder.catchNonFatal[String]("Long")(_.toLong)

  implicit val shortConfigDecoder: ConfigDecoder[String, Short] =
    ConfigDecoder.catchNonFatal[String]("Short")(_.toShort)

  implicit def identityConfigDecoder[A]: ConfigDecoder[A, A] =
    ConfigDecoder.identity
}
