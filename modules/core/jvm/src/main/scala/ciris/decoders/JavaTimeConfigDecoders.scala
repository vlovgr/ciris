package ciris.decoders

import java.time.format.DateTimeFormatter

import ciris.ConfigDecoder

trait JavaTimeConfigDecoders {
  import java.time._

  implicit val dayOfWeekConfigDecoder: ConfigDecoder[String, DayOfWeek] =
    ConfigDecoder.fromOption[String]("DayOfWeek") { value =>
      DayOfWeek.values.find(_.name equalsIgnoreCase value)
    }

  implicit val javaTimeDurationConfigDecoder: ConfigDecoder[String, Duration] =
    ConfigDecoder.catchNonFatal[String]("Duration")(Duration.parse)

  implicit val instantConfigDecoder: ConfigDecoder[String, Instant] =
    ConfigDecoder.catchNonFatal[String]("Instant")(Instant.parse)

  implicit def localDateConfigDecoder(
    implicit format: DateTimeFormatter = null
  ): ConfigDecoder[String, LocalDate] = format match {
    case null => ConfigDecoder.catchNonFatal[String]("LocalDate")(LocalDate.parse)
    case _    => ConfigDecoder.catchNonFatal[String]("LocalDate")(LocalDate.parse(_, format))
  }

  implicit def localDateTimeConfigDecoder(
    implicit format: DateTimeFormatter = null
  ): ConfigDecoder[String, LocalDateTime] = format match {
    case null => ConfigDecoder.catchNonFatal[String]("LocalDateTime")(LocalDateTime.parse)
    case _    => ConfigDecoder.catchNonFatal[String]("LocalDateTime")(LocalDateTime.parse(_, format))
  }

  implicit def localTimeConfigDecoder(
    implicit format: DateTimeFormatter = null
  ): ConfigDecoder[String, LocalTime] = format match {
    case null => ConfigDecoder.catchNonFatal[String]("LocalTime")(LocalTime.parse)
    case _    => ConfigDecoder.catchNonFatal[String]("LocalTime")(LocalTime.parse(_, format))
  }

  implicit val monthConfigDecoder: ConfigDecoder[String, Month] =
    ConfigDecoder.fromOption[String]("Month") { value =>
      Month.values.find(_.name equalsIgnoreCase value)
    }

  implicit def monthDayConfigDecoder(
    implicit format: DateTimeFormatter = null
  ): ConfigDecoder[String, MonthDay] = format match {
    case null => ConfigDecoder.catchNonFatal[String]("MonthDay")(MonthDay.parse)
    case _    => ConfigDecoder.catchNonFatal[String]("MonthDay")(MonthDay.parse(_, format))
  }

  implicit def offsetDateTimeConfigDecoder(
    implicit format: DateTimeFormatter = null
  ): ConfigDecoder[String, OffsetDateTime] = format match {
    case null => ConfigDecoder.catchNonFatal[String]("OffsetDateTime")(OffsetDateTime.parse)
    case _    => ConfigDecoder.catchNonFatal[String]("OffsetDateTime")(OffsetDateTime.parse(_, format))
  }

  implicit def offsetTimeConfigDecoder(
    implicit format: DateTimeFormatter = null
  ): ConfigDecoder[String, OffsetTime] = format match {
    case null => ConfigDecoder.catchNonFatal[String]("OffsetTime")(OffsetTime.parse)
    case _    => ConfigDecoder.catchNonFatal[String]("OffsetTime")(OffsetTime.parse(_, format))
  }

  implicit val periodConfigDecoder: ConfigDecoder[String, Period] =
    ConfigDecoder.catchNonFatal[String]("Period")(Period.parse)

  implicit def yearConfigDecoder(
    implicit format: DateTimeFormatter = null
  ): ConfigDecoder[String, Year] = format match {
    case null => ConfigDecoder.catchNonFatal[String]("Year")(Year.parse)
    case _    => ConfigDecoder.catchNonFatal[String]("Year")(Year.parse(_, format))
  }

  implicit def yearMonthConfigDecoder(
    implicit format: DateTimeFormatter = null
  ): ConfigDecoder[String, YearMonth] = format match {
    case null => ConfigDecoder.catchNonFatal[String]("YearMonth")(YearMonth.parse)
    case _    => ConfigDecoder.catchNonFatal[String]("YearMonth")(YearMonth.parse(_, format))
  }

  implicit def zonedDateTimeConfigDecoder(
    implicit format: DateTimeFormatter = null
  ): ConfigDecoder[String, ZonedDateTime] = format match {
    case null => ConfigDecoder.catchNonFatal[String]("ZonedDateTime")(ZonedDateTime.parse)
    case _    => ConfigDecoder.catchNonFatal[String]("ZonedDateTime")(ZonedDateTime.parse(_, format))
  }

  implicit val zoneIdConfigDecoder: ConfigDecoder[String, ZoneId] =
    ConfigDecoder.catchNonFatal[String]("ZoneId")(ZoneId.of)

  implicit val zoneOffsetConfigDecoder: ConfigDecoder[String, ZoneOffset] =
    ConfigDecoder.catchNonFatal[String]("ZoneOffset")(ZoneOffset.of)

  import java.time.chrono._

  implicit val chronologyConfigDecoder: ConfigDecoder[String, Chronology] =
    ConfigDecoder.catchNonFatal[String]("Chronology")(Chronology.of)

  implicit val hijrahEraConfigDecoder: ConfigDecoder[String, HijrahEra] =
    ConfigDecoder.fromOption[String]("HijrahEra") { value =>
      HijrahEra.values.find(_.name equalsIgnoreCase value)
    }

  implicit val isoEraConfigDecoder: ConfigDecoder[String, IsoEra] =
    ConfigDecoder.fromOption[String]("IsoEra") { value =>
      IsoEra.values.find(_.name equalsIgnoreCase value)
    }

  implicit val japaneseEraConfigDecoder: ConfigDecoder[String, JapaneseEra] =
    ConfigDecoder.fromOption[String]("JapaneseEra") { value =>
      JapaneseEra.values.find(_.toString equalsIgnoreCase value)
    }

  implicit val minguoEraConfigDecoder: ConfigDecoder[String, MinguoEra] =
    ConfigDecoder.fromOption[String]("MinguoEra") { value =>
      MinguoEra.values.find(_.name equalsIgnoreCase value)
    }

  implicit val thaiBuddhistEraConfigDecoder: ConfigDecoder[String, ThaiBuddhistEra] =
    ConfigDecoder.fromOption[String]("ThaiBuddhistEra") { value =>
      ThaiBuddhistEra.values.find(_.name equalsIgnoreCase value)
    }

  import java.time.format._

  implicit val dateTimeFormatterConfigDecoder: ConfigDecoder[String, DateTimeFormatter] =
    ConfigDecoder.catchNonFatal[String]("DateTimeFormatter")(DateTimeFormatter.ofPattern)

  implicit val formatStyleConfigDecoder: ConfigDecoder[String, FormatStyle] =
    ConfigDecoder.fromOption[String]("FormatStyle") { value =>
      FormatStyle.values.find(_.name equalsIgnoreCase value)
    }

  implicit val resolverStyleConfigDecoder: ConfigDecoder[String, ResolverStyle] =
    ConfigDecoder.fromOption[String]("ResolverStyle") { value =>
      ResolverStyle.values.find(_.name equalsIgnoreCase value)
    }

  implicit val signStyleConfigDecoder: ConfigDecoder[String, SignStyle] =
    ConfigDecoder.fromOption[String]("SignStyle") { value =>
      SignStyle.values.find(_.name equalsIgnoreCase value)
    }

  implicit val textStyleConfigDecoder: ConfigDecoder[String, TextStyle] =
    ConfigDecoder.fromOption[String]("TextStyle") { value =>
      TextStyle.values.find(_.name equalsIgnoreCase value)
    }
}
