package ciris.generators

import java.time._
import java.time.chrono._

import org.scalacheck.{Arbitrary, Gen}

import scala.collection.JavaConverters._

trait JavaTimeGenerators {
  val genDuration: Gen[Duration] =
    Gen.chooseNum(0, Long.MaxValue).map(Duration.ofNanos)

  implicit val arbDuration: Arbitrary[Duration] =
    Arbitrary(genDuration)

  val genZonedDateTime: Gen[ZonedDateTime] =
    for {
      year <- Gen.choose(-292278994, 292278994)
      month <- Gen.choose(1, 12)
      maxDaysInMonth = Month.of(month).length(Year.of(year).isLeap)
      dayOfMonth <- Gen.choose(1, maxDaysInMonth)
      hour <- Gen.choose(0, 23)
      minute <- Gen.choose(0, 59)
      second <- Gen.choose(0, 59)
      nanoOfSecond <- Gen.choose(0, 999999999)
      zoneId = ZoneId.of("UTC")
    } yield
      ZonedDateTime.of(
        year,
        month,
        dayOfMonth,
        hour,
        minute,
        second,
        nanoOfSecond,
        zoneId
      )

  implicit val arbZonedDateTime: Arbitrary[ZonedDateTime] =
    Arbitrary(genZonedDateTime)

  implicit val arbInstant: Arbitrary[Instant] =
    Arbitrary(genZonedDateTime.map(_.toInstant))

  implicit val arbLocalDate: Arbitrary[LocalDate] =
    Arbitrary(genZonedDateTime.map(_.toLocalDate))

  implicit val arbLocalDateTime: Arbitrary[LocalDateTime] =
    Arbitrary(genZonedDateTime.map(_.toLocalDateTime))

  implicit val arbLocalTime: Arbitrary[LocalTime] =
    Arbitrary(genZonedDateTime.map(_.toLocalTime))

  implicit val arbMonthDay: Arbitrary[MonthDay] =
    Arbitrary(genZonedDateTime.map(date ⇒ MonthDay.of(date.getMonth, date.getDayOfMonth)))

  implicit val arbOffsetDateTime: Arbitrary[OffsetDateTime] =
    Arbitrary(genZonedDateTime.map(_.toOffsetDateTime))

  implicit val arbOffsetTime: Arbitrary[OffsetTime] =
    Arbitrary(genZonedDateTime.map(_.toOffsetDateTime.toOffsetTime))

  implicit val arbPeriod: Arbitrary[Period] =
    Arbitrary(Gen.choose(0, Int.MaxValue).map(Period.ofDays))

  implicit val arbYear: Arbitrary[Year] =
    Arbitrary(genZonedDateTime.map(date ⇒ Year.of(date.getYear)))

  implicit val arbYearMonth: Arbitrary[YearMonth] =
    Arbitrary(genZonedDateTime.map(date ⇒ YearMonth.of(date.getYear, date.getMonth)))

  implicit val arbZoneId: Arbitrary[ZoneId] =
    Arbitrary(Gen.oneOf(ZoneId.getAvailableZoneIds.asScala.toList).map(ZoneId.of))

  implicit val arbZoneOffset: Arbitrary[ZoneOffset] =
    Arbitrary(genZonedDateTime.map(_.getOffset))

  implicit val arbChronology: Arbitrary[Chronology] =
    Arbitrary(Gen.oneOf(Chronology.getAvailableChronologies.asScala.toList))
}
