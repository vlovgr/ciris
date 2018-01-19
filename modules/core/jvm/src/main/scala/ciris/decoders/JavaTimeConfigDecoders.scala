package ciris.decoders

import java.time.format.DateTimeFormatter

import ciris.ConfigDecoder
import ciris.ConfigDecoder.{catchNonFatal, fromOption}

trait JavaTimeConfigDecoders {
  import java.time._

  implicit val dayOfWeekConfigDecoder: ConfigDecoder[DayOfWeek] =
    fromOption("DayOfWeek")(value => DayOfWeek.values.find(_.name equalsIgnoreCase value))

  implicit val javaTimeDurationConfigDecoder: ConfigDecoder[Duration] =
    catchNonFatal("Duration")(Duration.parse)

  implicit val instantConfigDecoder: ConfigDecoder[Instant] =
    catchNonFatal("Instant")(Instant.parse)

  implicit def localDateConfigDecoder(
    implicit format: DateTimeFormatter = null
  ): ConfigDecoder[LocalDate] = format match {
    case null => catchNonFatal("LocalDate")(LocalDate.parse)
    case _    => catchNonFatal("LocalDate")(LocalDate.parse(_, format))
  }

  implicit def localDateTimeConfigDecoder(
    implicit format: DateTimeFormatter = null
  ): ConfigDecoder[LocalDateTime] = format match {
    case null => catchNonFatal("LocalDateTime")(LocalDateTime.parse)
    case _    => catchNonFatal("LocalDateTime")(LocalDateTime.parse(_, format))
  }

  implicit def localTimeConfigDecoder(
    implicit format: DateTimeFormatter = null
  ): ConfigDecoder[LocalTime] = format match {
    case null => catchNonFatal("LocalTime")(LocalTime.parse)
    case _    => catchNonFatal("LocalTime")(LocalTime.parse(_, format))
  }

  implicit val monthConfigDecoder: ConfigDecoder[Month] =
    fromOption("Month")(value => Month.values.find(_.name equalsIgnoreCase value))

  implicit def monthDayConfigDecoder(
    implicit format: DateTimeFormatter = null
  ): ConfigDecoder[MonthDay] = format match {
    case null => catchNonFatal("MonthDay")(MonthDay.parse)
    case _    => catchNonFatal("MonthDay")(MonthDay.parse(_, format))
  }

  implicit def offsetDateTimeConfigDecoder(
    implicit format: DateTimeFormatter = null
  ): ConfigDecoder[OffsetDateTime] = format match {
    case null => catchNonFatal("OffsetDateTime")(OffsetDateTime.parse)
    case _    => catchNonFatal("OffsetDateTime")(OffsetDateTime.parse(_, format))
  }

  implicit def offsetTimeConfigDecoder(
    implicit format: DateTimeFormatter = null
  ): ConfigDecoder[OffsetTime] = format match {
    case null => catchNonFatal("OffsetTime")(OffsetTime.parse)
    case _    => catchNonFatal("OffsetTime")(OffsetTime.parse(_, format))
  }

  implicit val periodConfigDecoder: ConfigDecoder[Period] =
    catchNonFatal("Period")(Period.parse)

  implicit def yearConfigDecoder(
    implicit format: DateTimeFormatter = null
  ): ConfigDecoder[Year] = format match {
    case null => catchNonFatal("Year")(Year.parse)
    case _    => catchNonFatal("Year")(Year.parse(_, format))
  }

  implicit def yearMonthConfigDecoder(
    implicit format: DateTimeFormatter = null
  ): ConfigDecoder[YearMonth] = format match {
    case null => catchNonFatal("YearMonth")(YearMonth.parse)
    case _    => catchNonFatal("YearMonth")(YearMonth.parse(_, format))
  }

  implicit def zonedDateTimeConfigDecoder(
    implicit format: DateTimeFormatter = null
  ): ConfigDecoder[ZonedDateTime] = format match {
    case null => catchNonFatal("ZonedDateTime")(ZonedDateTime.parse)
    case _    => catchNonFatal("ZonedDateTime")(ZonedDateTime.parse(_, format))
  }

  implicit val zoneIdConfigDecoder: ConfigDecoder[ZoneId] =
    catchNonFatal("ZoneId")(ZoneId.of)

  implicit val zoneOffsetConfigDecoder: ConfigDecoder[ZoneOffset] =
    catchNonFatal("ZoneOffset")(ZoneOffset.of)

  import java.time.chrono._

  implicit val chronologyConfigDecoder: ConfigDecoder[Chronology] =
    catchNonFatal("Chronology")(Chronology.of)

  implicit val hijrahEraConfigDecoder: ConfigDecoder[HijrahEra] =
    fromOption("HijrahEra")(value => HijrahEra.values.find(_.name equalsIgnoreCase value))

  implicit val isoEraConfigDecoder: ConfigDecoder[IsoEra] =
    fromOption("IsoEra")(value => IsoEra.values.find(_.name equalsIgnoreCase value))

  implicit val japaneseEraConfigDecoder: ConfigDecoder[JapaneseEra] =
    fromOption("JapaneseEra")(value => JapaneseEra.values.find(_.toString equalsIgnoreCase value))

  implicit val minguoEraConfigDecoder: ConfigDecoder[MinguoEra] =
    fromOption("MinguoEra")(value => MinguoEra.values.find(_.name equalsIgnoreCase value))

  implicit val thaiBuddhistEraConfigDecoder: ConfigDecoder[ThaiBuddhistEra] =
    fromOption("ThaiBuddhistEra")(value => ThaiBuddhistEra.values.find(_.name equalsIgnoreCase value))

  import java.time.format._

  implicit val dateTimeFormatterConfigDecoder: ConfigDecoder[DateTimeFormatter] =
    catchNonFatal("DateTimeFormatter")(DateTimeFormatter.ofPattern)

  implicit val formatStyleConfigDecoder: ConfigDecoder[FormatStyle] =
    fromOption("FormatStyle")(value => FormatStyle.values.find(_.name equalsIgnoreCase value))

  implicit val resolverStyleConfigDecoder: ConfigDecoder[ResolverStyle] =
    fromOption("ResolverStyle")(value => ResolverStyle.values.find(_.name equalsIgnoreCase value))

  implicit val signStyleConfigDecoder: ConfigDecoder[SignStyle] =
    fromOption("SignStyle")(value => SignStyle.values.find(_.name equalsIgnoreCase value))

  implicit val textStyleConfigDecoder: ConfigDecoder[TextStyle] =
    fromOption("TextStyle")(value => TextStyle.values.find(_.name equalsIgnoreCase value))
}
