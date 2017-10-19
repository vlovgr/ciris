package ciris.readers

import java.time._
import java.time.format.DateTimeFormatter

import ciris.PropertySpec
import ciris.generators.LimitedRangeTimeGenerators

class JavaFormattedTimeConfigReadersSpec extends PropertySpec with LimitedRangeTimeGenerators {

  "JavaFormattedTimeConfigReaders" when {

    "reading a Year" should {
      implicit val dtf = DateTimeFormatter.ofPattern("yy")

      "successfully read Year values" in {
        forAll{year: Year =>
          readValue[Year](year.format(dtf)) shouldBe Right(year)
        }
      }
    }

    "reading a LocalDate" should {
      implicit val dtf = DateTimeFormatter.ofPattern("dd/MM/yy")

      "successfully read LocalDate values" in {
        forAll { localDate: LocalDate =>
          readValue[LocalDate](localDate.format(dtf)) shouldBe Right(localDate)
        }
      }
    }


    "reading a LocalTime" should {
      implicit val dtf = DateTimeFormatter.ofPattern("HH:mm")

      "successfully read LocalTime values" in {
        forAll { localTime: LocalTime =>
          val expected = LocalTime.of(localTime.getHour, localTime.getMinute)
          readValue[LocalTime](localTime.format(dtf)) shouldBe Right(expected)
        }
      }
    }


    "reading a LocalDateTime" should {
      implicit val dtf = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm:ss")

      "successfully read LocalDateTime values" in {
        forAll { ldt: LocalDateTime =>
          val expected = LocalDateTime.of(ldt.getYear, ldt.getMonth, ldt.getDayOfMonth, ldt.getHour, ldt.getMinute, ldt.getSecond)
          readValue[LocalDateTime](ldt.format(dtf)) shouldBe Right(expected)
        }
      }
    }

    "reading a ZonedDateTime" should {
      implicit val dtf = DateTimeFormatter.ISO_ZONED_DATE_TIME

      "successfully read ZonedDateTime values" in {
        forAll { zdt: ZonedDateTime =>
          readValue[ZonedDateTime](zdt.format(dtf)) shouldBe Right(zdt)
        }
      }
    }

    "reading YearMonth" should {
      implicit val dtf = DateTimeFormatter.ofPattern("yy-MMM")

      "successfully read YearMonth values" in {
        forAll { ym: YearMonth =>
          readValue[YearMonth](ym.format(dtf)) shouldBe Right(ym)
        }
      }
    }

    "reading OffsetTime" should {
      implicit val dtf = DateTimeFormatter.ISO_OFFSET_TIME

      "successfully read OffsetTime values" in {
        forAll { ot: OffsetTime =>
          readValue[OffsetTime](ot.format(dtf)) shouldBe Right(ot)
        }
      }
    }

    "reading OffsetDateTime" should {
      implicit val dtf = DateTimeFormatter.ISO_OFFSET_DATE_TIME

      "successfully read OffsetDateTime values" in {
        forAll { odt: OffsetDateTime =>
          readValue[OffsetDateTime](odt.format(dtf)) shouldBe Right(odt)
        }
      }
    }

    "reading MonthDay" should {
      implicit val dtf = DateTimeFormatter.ofPattern("MMM-dd")

      "successfully read YearMonth values" in {
        forAll { md: MonthDay =>
          readValue[MonthDay](md.format(dtf)) shouldBe Right(md)
        }
      }
    }
  }
}
