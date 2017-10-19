package ciris.generators

import java.time.{Month, Year, ZoneId, ZonedDateTime}

import org.scalacheck.Gen

trait LimitedRangeTimeGenerators extends JavaTimeGenerators {
  override val genZonedDateTime: Gen[ZonedDateTime] =
    for {
      year <- Gen.choose(2000, 2099)
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
}
