package ciris.readers

import ciris.ConfigReader
import ciris.ConfigReader.{catchNonFatal, fromOption}

trait JavaTimeConfigReaders {
  import java.time._

  implicit val dayOfWeekConfigReader: ConfigReader[DayOfWeek] =
    fromOption("DayOfWeek")(value ⇒ DayOfWeek.values.find(_.name equalsIgnoreCase value))

  implicit val javaTimeDurationConfigReader: ConfigReader[Duration] =
    catchNonFatal("Duration")(Duration.parse)

  implicit val instantConfigReader: ConfigReader[Instant] =
    catchNonFatal("Instant")(Instant.parse)

  implicit val localDateConfigReader: ConfigReader[LocalDate] =
    catchNonFatal("LocalDate")(LocalDate.parse)

  implicit val localDateTimeConfigReader: ConfigReader[LocalDateTime] =
    catchNonFatal("LocalDateTime")(LocalDateTime.parse)

  implicit val localTimeConfigReader: ConfigReader[LocalTime] =
    catchNonFatal("LocalTime")(LocalTime.parse)

  implicit val monthConfigReader: ConfigReader[Month] =
    fromOption("Month")(value ⇒ Month.values.find(_.name equalsIgnoreCase value))

  implicit val monthDayConfigReader: ConfigReader[MonthDay] =
    catchNonFatal("MonthDay")(MonthDay.parse)

  implicit val offsetDateTimeConfigReader: ConfigReader[OffsetDateTime] =
    catchNonFatal("OffsetDateTime")(OffsetDateTime.parse)

  implicit val offsetTimeConfigReader: ConfigReader[OffsetTime] =
    catchNonFatal("OffsetTime")(OffsetTime.parse)

  implicit val periodConfigReader: ConfigReader[Period] =
    catchNonFatal("Period")(Period.parse)

  implicit val yearConfigReader: ConfigReader[Year] =
    catchNonFatal("Year")(Year.parse)

  implicit val yearMonthConfigReader: ConfigReader[YearMonth] =
    catchNonFatal("YearMonth")(YearMonth.parse)

  implicit val zonedDateTimeConfigReader: ConfigReader[ZonedDateTime] =
    catchNonFatal("ZonedDateTime")(ZonedDateTime.parse)

  implicit val zoneIdConfigReader: ConfigReader[ZoneId] =
    catchNonFatal("ZoneId")(ZoneId.of)

  implicit val zoneOffsetConfigReader: ConfigReader[ZoneOffset] =
    catchNonFatal("ZoneOffset")(ZoneOffset.of)

  import java.time.chrono._

  implicit val chronologyConfigReader: ConfigReader[Chronology] =
    catchNonFatal("Chronology")(Chronology.of)

  implicit val hijrahEraConfigReader: ConfigReader[HijrahEra] =
    fromOption("HijrahEra")(value ⇒ HijrahEra.values.find(_.name equalsIgnoreCase value))

  implicit val isoEraConfigReader: ConfigReader[IsoEra] =
    fromOption("IsoEra")(value ⇒ IsoEra.values.find(_.name equalsIgnoreCase value))

  implicit val japaneseEraConfigReader: ConfigReader[JapaneseEra] =
    fromOption("JapaneseEra")(value ⇒ JapaneseEra.values.find(_.toString equalsIgnoreCase value))

  implicit val minguoEraConfigReader: ConfigReader[MinguoEra] =
    fromOption("MinguoEra")(value ⇒ MinguoEra.values.find(_.name equalsIgnoreCase value))

  implicit val thaiBuddhistEraConfigReader: ConfigReader[ThaiBuddhistEra] =
    fromOption("ThaiBuddhistEra")(value ⇒
      ThaiBuddhistEra.values.find(_.name equalsIgnoreCase value))

  import java.time.format._

  implicit val dateTimeFormatterConfigReader: ConfigReader[DateTimeFormatter] =
    catchNonFatal("DateTimeFormatter")(DateTimeFormatter.ofPattern)

  implicit val formatStyleConfigReader: ConfigReader[FormatStyle] =
    fromOption("FormatStyle")(value ⇒ FormatStyle.values.find(_.name equalsIgnoreCase value))

  implicit val resolverStyleConfigReader: ConfigReader[ResolverStyle] =
    fromOption("ResolverStyle")(value ⇒ ResolverStyle.values.find(_.name equalsIgnoreCase value))

  implicit val signStyleConfigReader: ConfigReader[SignStyle] =
    fromOption("SignStyle")(value ⇒ SignStyle.values.find(_.name equalsIgnoreCase value))

  implicit val textStyleConfigReader: ConfigReader[TextStyle] =
    fromOption("TextStyle")(value ⇒ TextStyle.values.find(_.name equalsIgnoreCase value))
}
