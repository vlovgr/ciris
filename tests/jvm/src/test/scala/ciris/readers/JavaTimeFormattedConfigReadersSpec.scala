package ciris.readers

import java.time._
import java.time.format.DateTimeFormatter

import ciris.PropertySpec
import ciris.generators.LimitedJavaTimeGenerators

final class JavaTimeFormattedConfigReadersSpec
    extends PropertySpec
    with LimitedJavaTimeGenerators {

  "JavaTimeFormattedConfigReaders" when {
    "reading a LocalDate" should {
      "successfully read LocalDate values" in {
        implicit val formatter: DateTimeFormatter =
          DateTimeFormatter.ofPattern("dd/MM/yy")

        forAll { localDate: LocalDate =>
          readValue[LocalDate](localDate.format(formatter)) shouldBe Right(localDate)
        }
      }
    }

    "reading a LocalDateTime" should {
      "successfully read LocalDateTime values" in {
        implicit val formatter: DateTimeFormatter =
          DateTimeFormatter.ofPattern("dd/MM/yy HH:mm:ss")

        forAll { localDateTime: LocalDateTime =>
          val expected = LocalDateTime.of(
            localDateTime.getYear,
            localDateTime.getMonth,
            localDateTime.getDayOfMonth,
            localDateTime.getHour,
            localDateTime.getMinute,
            localDateTime.getSecond
          )

          readValue[LocalDateTime](localDateTime.format(formatter)) shouldBe Right(expected)
        }
      }
    }

    "reading a LocalTime" should {
      "successfully read LocalTime values" in {
        implicit val formatter: DateTimeFormatter =
          DateTimeFormatter.ofPattern("HH:mm")

        forAll { localTime: LocalTime =>
          val expected = LocalTime.of(localTime.getHour, localTime.getMinute)
          readValue[LocalTime](localTime.format(formatter)) shouldBe Right(expected)
        }
      }
    }

    "reading MonthDay" should {
      "successfully read MonthDay values" in {
        implicit val formatter: DateTimeFormatter =
          DateTimeFormatter.ofPattern("MMM-dd")

        forAll { md: MonthDay =>
          readValue[MonthDay](md.format(formatter)) shouldBe Right(md)
        }
      }
    }

    "reading OffsetDateTime" should {
      "successfully read OffsetDateTime values" in {
        implicit val formatter: DateTimeFormatter =
          DateTimeFormatter.ISO_OFFSET_DATE_TIME

        forAll { offsetDateTime: OffsetDateTime =>
          readValue[OffsetDateTime](offsetDateTime.format(formatter)) shouldBe Right(offsetDateTime)
        }
      }
    }

    "reading OffsetTime" should {
      "successfully read OffsetTime values" in {
        implicit val formatter: DateTimeFormatter =
          DateTimeFormatter.ISO_OFFSET_TIME

        forAll { offsetTime: OffsetTime =>
          readValue[OffsetTime](offsetTime.format(formatter)) shouldBe Right(offsetTime)
        }
      }
    }

    "reading a Year" should {
      "successfully read Year values" in {
        implicit val formatter: DateTimeFormatter =
          DateTimeFormatter.ofPattern("yyyy")

        forAll { year: Year =>
          readValue[Year](year.format(formatter)) shouldBe Right(year)
        }
      }
    }

    "reading YearMonth" should {
      "successfully read YearMonth values" in {
        implicit val formatter: DateTimeFormatter =
          DateTimeFormatter.ofPattern("yy-MMM")

        forAll { yearMonth: YearMonth =>
          readValue[YearMonth](yearMonth.format(formatter)) shouldBe Right(yearMonth)
        }
      }
    }

    "reading a ZonedDateTime" should {
      "successfully read ZonedDateTime values" in {
        implicit val formatter: DateTimeFormatter =
          DateTimeFormatter.ISO_ZONED_DATE_TIME

        forAll { zonedDateTime: ZonedDateTime =>
          readValue[ZonedDateTime](zonedDateTime.format(formatter)) shouldBe Right(zonedDateTime)
        }
      }
    }
  }
}
