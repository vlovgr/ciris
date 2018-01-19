package ciris.decoders

import ciris.ConfigDecoder
import ciris.ConfigDecoder.{catchNonFatal, fromOption}

trait PrimitiveConfigDecoders {
  implicit val booleanConfigDecoder: ConfigDecoder[Boolean] =
    catchNonFatal("Boolean")(_.toBoolean)

  implicit val byteConfigDecoder: ConfigDecoder[Byte] =
    catchNonFatal("Byte")(_.toByte)

  implicit val charConfigDecoder: ConfigDecoder[Char] =
    fromOption("Char") { value =>
      if (value.length == 1) Some(value.head) else None
    }

  implicit val doubleConfigDecoder: ConfigDecoder[Double] =
    catchNonFatal("Double") {
      case s if s.lastOption.exists(_ == '%') => s.init.toDouble / 100d
      case s => s.toDouble
    }

  implicit val floatConfigDecoder: ConfigDecoder[Float] =
    catchNonFatal("Float") {
      case s if s.lastOption.exists(_ == '%') => s.init.toFloat / 100f
      case s => s.toFloat
    }

  implicit val intConfigDecoder: ConfigDecoder[Int] =
    catchNonFatal("Int")(_.toInt)

  implicit val longConfigDecoder: ConfigDecoder[Long] =
    catchNonFatal("Long")(_.toLong)

  implicit val shortConfigDecoder: ConfigDecoder[Short] =
    catchNonFatal("Short")(_.toShort)

  implicit val stringConfigDecoder: ConfigDecoder[String] =
    ConfigDecoder.identity
}
