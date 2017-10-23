package ciris.readers

import java.time.format.DateTimeFormatter

import ciris.ConfigReader
import ciris.ConfigReader.{catchNonFatal, fromOption}

trait JavaTimeConfigReaders {
  import java.time._

  implicit val dayOfWeekConfigReader: ConfigReader[DayOfWeek] =
    fromOption("DayOfWeek")(value => DayOfWeek.values.find(_.name equalsIgnoreCase value))

  implicit val javaTimeDurationConfigReader: ConfigReader[Duration] =
    catchNonFatal("Duration")(Duration.parse)

  implicit val instantConfigReader: ConfigReader[Instant] =
    catchNonFatal("Instant")(Instant.parse)

  implicit def localDateConfigReader(
    implicit format: DateTimeFormatter = null
  ): ConfigReader[LocalDate] = format match {
    case null => catchNonFatal("LocalDate")(LocalDate.parse)
    case _    => catchNonFatal("LocalDate")(LocalDate.parse(_, format))
  }

  implicit def localDateTimeConfigReader(
    implicit format: DateTimeFormatter = null
  ): ConfigReader[LocalDateTime] = format match {
    case null => catchNonFatal("LocalDateTime")(LocalDateTime.parse)
    case _    => catchNonFatal("LocalDateTime")(LocalDateTime.parse(_, format))
  }

  implicit def localTimeConfigReader(
    implicit format: DateTimeFormatter = null
  ): ConfigReader[LocalTime] = format match {
    case null => catchNonFatal("LocalTime")(LocalTime.parse)
    case _    => catchNonFatal("LocalTime")(LocalTime.parse(_, format))
  }

  implicit val monthConfigReader: ConfigReader[Month] =
    fromOption("Month")(value => Month.values.find(_.name equalsIgnoreCase value))

  implicit def monthDayConfigReader(
    implicit format: DateTimeFormatter = null
  ): ConfigReader[MonthDay] = format match {
    case null => catchNonFatal("MonthDay")(MonthDay.parse)
    case _    => catchNonFatal("MonthDay")(MonthDay.parse(_, format))
  }

  implicit def offsetDateTimeConfigReader(
    implicit format: DateTimeFormatter = null
  ): ConfigReader[OffsetDateTime] = format match {
    case null => catchNonFatal("OffsetDateTime")(OffsetDateTime.parse)
    case _    => catchNonFatal("OffsetDateTime")(OffsetDateTime.parse(_, format))
  }

  implicit def offsetTimeConfigReader(
    implicit format: DateTimeFormatter = null
  ): ConfigReader[OffsetTime] = format match {
    case null => catchNonFatal("OffsetTime")(OffsetTime.parse)
    case _    => catchNonFatal("OffsetTime")(OffsetTime.parse(_, format))
  }

  implicit val periodConfigReader: ConfigReader[Period] =
    catchNonFatal("Period")(Period.parse)

  implicit def yearConfigReader(
    implicit format: DateTimeFormatter = null
  ): ConfigReader[Year] = format match {
    case null => catchNonFatal("Year")(Year.parse)
    case _    => catchNonFatal("Year")(Year.parse(_, format))
  }

  implicit def yearMonthConfigReader(
    implicit format: DateTimeFormatter = null
  ): ConfigReader[YearMonth] = format match {
    case null => catchNonFatal("YearMonth")(YearMonth.parse)
    case _    => catchNonFatal("YearMonth")(YearMonth.parse(_, format))
  }

  implicit def zonedDateTimeConfigReader(
    implicit format: DateTimeFormatter = null
  ): ConfigReader[ZonedDateTime] = format match {
    case null => catchNonFatal("ZonedDateTime")(ZonedDateTime.parse)
    case _    => catchNonFatal("ZonedDateTime")(ZonedDateTime.parse(_, format))
  }

  implicit val zoneIdConfigReader: ConfigReader[ZoneId] =
    catchNonFatal("ZoneId")(ZoneId.of)

  implicit val zoneOffsetConfigReader: ConfigReader[ZoneOffset] =
    catchNonFatal("ZoneOffset")(ZoneOffset.of)

  import java.time.chrono._

  implicit val chronologyConfigReader: ConfigReader[Chronology] =
    catchNonFatal("Chronology")(Chronology.of)

  implicit val hijrahEraConfigReader: ConfigReader[HijrahEra] =
    fromOption("HijrahEra")(value => HijrahEra.values.find(_.name equalsIgnoreCase value))

  implicit val isoEraConfigReader: ConfigReader[IsoEra] =
    fromOption("IsoEra")(value => IsoEra.values.find(_.name equalsIgnoreCase value))

  implicit val japaneseEraConfigReader: ConfigReader[JapaneseEra] =
    fromOption("JapaneseEra")(value => JapaneseEra.values.find(_.toString equalsIgnoreCase value))

  implicit val minguoEraConfigReader: ConfigReader[MinguoEra] =
    fromOption("MinguoEra")(value => MinguoEra.values.find(_.name equalsIgnoreCase value))

  implicit val thaiBuddhistEraConfigReader: ConfigReader[ThaiBuddhistEra] =
    fromOption("ThaiBuddhistEra")(value => ThaiBuddhistEra.values.find(_.name equalsIgnoreCase value))

  import java.time.format._

  implicit val dateTimeFormatterConfigReader: ConfigReader[DateTimeFormatter] =
    catchNonFatal("DateTimeFormatter")(DateTimeFormatter.ofPattern)

  implicit val formatStyleConfigReader: ConfigReader[FormatStyle] =
    fromOption("FormatStyle")(value => FormatStyle.values.find(_.name equalsIgnoreCase value))

  implicit val resolverStyleConfigReader: ConfigReader[ResolverStyle] =
    fromOption("ResolverStyle")(value => ResolverStyle.values.find(_.name equalsIgnoreCase value))

  implicit val signStyleConfigReader: ConfigReader[SignStyle] =
    fromOption("SignStyle")(value => SignStyle.values.find(_.name equalsIgnoreCase value))

  implicit val textStyleConfigReader: ConfigReader[TextStyle] =
    fromOption("TextStyle")(value => TextStyle.values.find(_.name equalsIgnoreCase value))
}
