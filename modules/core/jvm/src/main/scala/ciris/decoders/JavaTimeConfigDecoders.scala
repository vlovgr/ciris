package ciris.decoders

import java.time.format.DateTimeFormatter

import ciris.ConfigDecoder

trait JavaTimeConfigDecoders {
  import java.time._

  implicit val dayOfWeekConfigDecoder: ConfigDecoder[String, DayOfWeek] =
    ConfigDecoder.fromOption("DayOfWeek") { value =>
      DayOfWeek.values.find(_.name equalsIgnoreCase value)
    }

  implicit val javaTimeDurationConfigDecoder: ConfigDecoder[String, Duration] =
    ConfigDecoder.catchNonFatal("Duration")(Duration.parse)

  implicit val instantConfigDecoder: ConfigDecoder[String, Instant] =
    ConfigDecoder.catchNonFatal("Instant")(Instant.parse)

  implicit def localDateConfigDecoder(
    implicit format: DateTimeFormatter = null
  ): ConfigDecoder[String, LocalDate] = format match {
    case null => ConfigDecoder.catchNonFatal("LocalDate")(LocalDate.parse)
    case _    => ConfigDecoder.catchNonFatal("LocalDate")(LocalDate.parse(_, format))
  }

  implicit def localDateTimeConfigDecoder(
    implicit format: DateTimeFormatter = null
  ): ConfigDecoder[String, LocalDateTime] = format match {
    case null => ConfigDecoder.catchNonFatal("LocalDateTime")(LocalDateTime.parse)
    case _    => ConfigDecoder.catchNonFatal("LocalDateTime")(LocalDateTime.parse(_, format))
  }

  implicit def localTimeConfigDecoder(
    implicit format: DateTimeFormatter = null
  ): ConfigDecoder[String, LocalTime] = format match {
    case null => ConfigDecoder.catchNonFatal("LocalTime")(LocalTime.parse)
    case _    => ConfigDecoder.catchNonFatal("LocalTime")(LocalTime.parse(_, format))
  }

  implicit val monthConfigDecoder: ConfigDecoder[String, Month] =
    ConfigDecoder.fromOption("Month") { value =>
      Month.values.find(_.name equalsIgnoreCase value)
    }

  implicit def monthDayConfigDecoder(
    implicit format: DateTimeFormatter = null
  ): ConfigDecoder[String, MonthDay] = format match {
    case null => ConfigDecoder.catchNonFatal("MonthDay")(MonthDay.parse)
    case _    => ConfigDecoder.catchNonFatal("MonthDay")(MonthDay.parse(_, format))
  }

  implicit def offsetDateTimeConfigDecoder(
    implicit format: DateTimeFormatter = null
  ): ConfigDecoder[String, OffsetDateTime] = format match {
    case null => ConfigDecoder.catchNonFatal("OffsetDateTime")(OffsetDateTime.parse)
    case _    => ConfigDecoder.catchNonFatal("OffsetDateTime")(OffsetDateTime.parse(_, format))
  }

  implicit def offsetTimeConfigDecoder(
    implicit format: DateTimeFormatter = null
  ): ConfigDecoder[String, OffsetTime] = format match {
    case null => ConfigDecoder.catchNonFatal("OffsetTime")(OffsetTime.parse)
    case _    => ConfigDecoder.catchNonFatal("OffsetTime")(OffsetTime.parse(_, format))
  }

  implicit val periodConfigDecoder: ConfigDecoder[String, Period] =
    ConfigDecoder.catchNonFatal("Period")(Period.parse)

  implicit def yearConfigDecoder(
    implicit format: DateTimeFormatter = null
  ): ConfigDecoder[String, Year] = format match {
    case null => ConfigDecoder.catchNonFatal("Year")(Year.parse)
    case _    => ConfigDecoder.catchNonFatal("Year")(Year.parse(_, format))
  }

  implicit def yearMonthConfigDecoder(
    implicit format: DateTimeFormatter = null
  ): ConfigDecoder[String, YearMonth] = format match {
    case null => ConfigDecoder.catchNonFatal("YearMonth")(YearMonth.parse)
    case _    => ConfigDecoder.catchNonFatal("YearMonth")(YearMonth.parse(_, format))
  }

  implicit def zonedDateTimeConfigDecoder(
    implicit format: DateTimeFormatter = null
  ): ConfigDecoder[String, ZonedDateTime] = format match {
    case null => ConfigDecoder.catchNonFatal("ZonedDateTime")(ZonedDateTime.parse)
    case _    => ConfigDecoder.catchNonFatal("ZonedDateTime")(ZonedDateTime.parse(_, format))
  }

  implicit val zoneIdConfigDecoder: ConfigDecoder[String, ZoneId] =
    ConfigDecoder.catchNonFatal("ZoneId")(ZoneId.of)

  implicit val zoneOffsetConfigDecoder: ConfigDecoder[String, ZoneOffset] =
    ConfigDecoder.catchNonFatal("ZoneOffset")(ZoneOffset.of)

  import java.time.chrono._

  implicit val chronologyConfigDecoder: ConfigDecoder[String, Chronology] =
    ConfigDecoder.catchNonFatal("Chronology")(Chronology.of)

  implicit val hijrahEraConfigDecoder: ConfigDecoder[String, HijrahEra] =
    ConfigDecoder.fromOption("HijrahEra") { value =>
      HijrahEra.values.find(_.name equalsIgnoreCase value)
    }

  implicit val isoEraConfigDecoder: ConfigDecoder[String, IsoEra] =
    ConfigDecoder.fromOption("IsoEra") { value =>
      IsoEra.values.find(_.name equalsIgnoreCase value)
    }

  implicit val japaneseEraConfigDecoder: ConfigDecoder[String, JapaneseEra] =
    ConfigDecoder.fromOption("JapaneseEra") { value =>
      JapaneseEra.values.find(_.toString equalsIgnoreCase value)
    }

  implicit val minguoEraConfigDecoder: ConfigDecoder[String, MinguoEra] =
    ConfigDecoder.fromOption("MinguoEra") { value =>
      MinguoEra.values.find(_.name equalsIgnoreCase value)
    }

  implicit val thaiBuddhistEraConfigDecoder: ConfigDecoder[String, ThaiBuddhistEra] =
    ConfigDecoder.fromOption("ThaiBuddhistEra") { value =>
      ThaiBuddhistEra.values.find(_.name equalsIgnoreCase value)
    }

  import java.time.format._

  implicit val dateTimeFormatterConfigDecoder: ConfigDecoder[String, DateTimeFormatter] =
    ConfigDecoder.catchNonFatal("DateTimeFormatter")(DateTimeFormatter.ofPattern)

  implicit val formatStyleConfigDecoder: ConfigDecoder[String, FormatStyle] =
    ConfigDecoder.fromOption("FormatStyle") { value =>
      FormatStyle.values.find(_.name equalsIgnoreCase value)
    }

  implicit val resolverStyleConfigDecoder: ConfigDecoder[String, ResolverStyle] =
    ConfigDecoder.fromOption("ResolverStyle") { value =>
      ResolverStyle.values.find(_.name equalsIgnoreCase value)
    }

  implicit val signStyleConfigDecoder: ConfigDecoder[String, SignStyle] =
    ConfigDecoder.fromOption("SignStyle") { value =>
      SignStyle.values.find(_.name equalsIgnoreCase value)
    }

  implicit val textStyleConfigDecoder: ConfigDecoder[String, TextStyle] =
    ConfigDecoder.fromOption("TextStyle") { value =>
      TextStyle.values.find(_.name equalsIgnoreCase value)
    }
}
