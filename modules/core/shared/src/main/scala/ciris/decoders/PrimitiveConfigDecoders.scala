package ciris.decoders

import ciris.ConfigDecoder

private[ciris] trait PrimitiveConfigDecoders {
  implicit val booleanConfigDecoder: ConfigDecoder[String, Boolean] =
    ConfigDecoder.catchNonFatal("Boolean")(_.toBoolean)

  implicit val byteConfigDecoder: ConfigDecoder[String, Byte] =
    ConfigDecoder.catchNonFatal("Byte")(_.toByte)

  implicit val charConfigDecoder: ConfigDecoder[String, Char] =
    ConfigDecoder.fromOption("Char") { value =>
      if (value.length == 1) Some(value.head) else None
    }

  implicit val doubleConfigDecoder: ConfigDecoder[String, Double] =
    ConfigDecoder.catchNonFatal("Double") {
      case s if s.lastOption.exists(_ == '%') => s.init.toDouble / 100d
      case s => s.toDouble
    }

  implicit val floatConfigDecoder: ConfigDecoder[String, Float] =
    ConfigDecoder.catchNonFatal("Float") {
      case s if s.lastOption.exists(_ == '%') => s.init.toFloat / 100f
      case s => s.toFloat
    }

  implicit val intConfigDecoder: ConfigDecoder[String, Int] =
    ConfigDecoder.catchNonFatal("Int")(_.toInt)

  implicit val longConfigDecoder: ConfigDecoder[String, Long] =
    ConfigDecoder.catchNonFatal("Long")(_.toLong)

  implicit val shortConfigDecoder: ConfigDecoder[String, Short] =
    ConfigDecoder.catchNonFatal("Short")(_.toShort)

  implicit def identityConfigDecoder[A]: ConfigDecoder[A, A] =
    ConfigDecoder.identity
}
