package ciris.readers

import java.time._
import java.time.chrono._
import java.time.format._

import ciris.PropertySpec
import ciris.generators.JavaTimeGenerators
import org.scalacheck.Gen

final class JavaTimeConfigReadersSpec extends PropertySpec with JavaTimeGenerators {
  "JavaTimeConfigReaders" when {
    "reading a DayOfWeek" should {
      "successfully read DayOfWeek values" in {
        forAll(mixedCaseEnum(DayOfWeek.values)(_.name)) {
          case (dayOfWeek, string) ⇒
            readValue[DayOfWeek](string) shouldBe Right(dayOfWeek)
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(!DayOfWeek.values.exists(_.name equalsIgnoreCase string)) {
            readValue[DayOfWeek](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading a Duration" should {
      "successfully read Duration values" in {
        forAll { duration: Duration ⇒
          readValue[Duration](duration.toString) shouldBe Right(duration)
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(fails(Duration.parse(string))) {
            readValue[Duration](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading a Instant" should {
      "successfully read Instant values" in {
        forAll { instant: Instant ⇒
          readValue[Instant](instant.toString) shouldBe Right(instant)
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(fails(Instant.parse(string))) {
            readValue[Instant](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading a LocalDate" should {
      "successfully read LocalDate values" in {
        forAll { localDate: LocalDate ⇒
          readValue[LocalDate](localDate.toString) shouldBe Right(localDate)
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(fails(LocalDate.parse(string))) {
            readValue[LocalDate](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading a LocalDateTime" should {
      "successfully read LocalDateTime values" in {
        forAll { localDateTime: LocalDateTime ⇒
          readValue[LocalDateTime](localDateTime.toString) shouldBe Right(localDateTime)
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(fails(LocalDateTime.parse(string))) {
            readValue[LocalDateTime](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading a LocalTime" should {
      "successfully read LocalTime values" in {
        forAll { localTime: LocalTime ⇒
          readValue[LocalTime](localTime.toString) shouldBe Right(localTime)
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(fails(LocalTime.parse(string))) {
            readValue[LocalTime](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading a Month" should {
      "successfully read Month values" in {
        forAll(mixedCaseEnum(Month.values)(_.name)) {
          case (month, string) ⇒
            readValue[Month](string) shouldBe Right(month)
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(!Month.values.exists(_.name equalsIgnoreCase string)) {
            readValue[Month](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading a MonthDay" should {
      "successfully read MonthDay values" in {
        forAll { monthDay: MonthDay ⇒
          readValue[MonthDay](monthDay.toString) shouldBe Right(monthDay)
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(fails(MonthDay.parse(string))) {
            readValue[MonthDay](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading a OffsetDateTime" should {
      "successfully read OffsetDateTime values" in {
        forAll { offsetDateTime: OffsetDateTime ⇒
          readValue[OffsetDateTime](offsetDateTime.toString) shouldBe Right(offsetDateTime)
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(fails(OffsetDateTime.parse(string))) {
            readValue[OffsetDateTime](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading a OffsetTime" should {
      "successfully read OffsetTime values" in {
        forAll { offsetTime: OffsetTime ⇒
          readValue[OffsetTime](offsetTime.toString) shouldBe Right(offsetTime)
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(fails(OffsetTime.parse(string))) {
            readValue[OffsetTime](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading a Period" should {
      "successfully read Period values" in {
        forAll { period: Period ⇒
          readValue[Period](period.toString) shouldBe Right(period)
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(fails(Period.parse(string))) {
            readValue[Period](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading a Year" should {
      "successfully read Year values" in {
        forAll { year: Year ⇒
          val yearString =
            if (year.getValue > 9999) s"+${year.toString}"
            else year.toString

          readValue[Year](yearString) shouldBe Right(year)
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(fails(Year.parse(string))) {
            readValue[Year](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading a YearMonth" should {
      "successfully read YearMonth values" in {
        forAll { yearMonth: YearMonth ⇒
          val yearMonthString =
            if (yearMonth.getYear > 9999) s"+${yearMonth.toString}"
            else yearMonth.toString

          readValue[YearMonth](yearMonthString) shouldBe Right(yearMonth)
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(fails(YearMonth.parse(string))) {
            readValue[YearMonth](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading a ZonedDateTime" should {
      "successfully read ZonedDateTime values" in {
        forAll { zonedDateTime: ZonedDateTime ⇒
          readValue[ZonedDateTime](zonedDateTime.toString) shouldBe Right(zonedDateTime)
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(fails(ZonedDateTime.parse(string))) {
            readValue[ZonedDateTime](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading a ZoneId" should {
      "successfully read ZoneId values" in {
        forAll { zoneId: ZoneId ⇒
          readValue[ZoneId](zoneId.toString) shouldBe Right(zoneId)
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(fails(ZoneId.of(string))) {
            readValue[ZoneId](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading a ZoneOffset" should {
      "successfully read ZoneOffset values" in {
        forAll { zoneOffset: ZoneOffset ⇒
          readValue[ZoneOffset](zoneOffset.toString) shouldBe Right(zoneOffset)
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(fails(ZoneOffset.of(string))) {
            readValue[ZoneOffset](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading a Chronology" should {
      "successfully read Chronology values" in {
        forAll { chronology: Chronology ⇒
          readValue[Chronology](chronology.toString) shouldBe Right(chronology)
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(fails(Chronology.of(string))) {
            readValue[Chronology](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading a HijrahEra" should {
      "successfully read HijrahEra values" in {
        forAll(mixedCaseEnum(HijrahEra.values)(_.name)) {
          case (hijrahEra, string) ⇒
            readValue[HijrahEra](string) shouldBe Right(hijrahEra)
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(!HijrahEra.values.exists(_.name equalsIgnoreCase string)) {
            readValue[HijrahEra](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading a IsoEra" should {
      "successfully read IsoEra values" in {
        forAll(mixedCaseEnum(IsoEra.values)(_.name)) {
          case (isoEra, string) ⇒
            readValue[IsoEra](string) shouldBe Right(isoEra)
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(!IsoEra.values.exists(_.name equalsIgnoreCase string)) {
            readValue[IsoEra](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading a JapaneseEra" should {
      "successfully read JapaneseEra values" in {
        forAll(mixedCaseEnum(JapaneseEra.values)(_.toString)) {
          case (japaneseEra, string) ⇒
            readValue[JapaneseEra](string) shouldBe Right(japaneseEra)
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(!JapaneseEra.values.exists(_.toString equalsIgnoreCase string)) {
            readValue[JapaneseEra](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading a MinguoEra" should {
      "successfully read MinguoEra values" in {
        forAll(mixedCaseEnum(MinguoEra.values)(_.name)) {
          case (minguoEra, string) ⇒
            readValue[MinguoEra](string) shouldBe Right(minguoEra)
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(!MinguoEra.values.exists(_.name equalsIgnoreCase string)) {
            readValue[MinguoEra](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading a ThaiBuddhistEra" should {
      "successfully read ThaiBuddhistEra values" in {
        forAll(mixedCaseEnum(ThaiBuddhistEra.values)(_.name)) {
          case (thaiBuddhistEra, string) ⇒
            readValue[ThaiBuddhistEra](string) shouldBe Right(thaiBuddhistEra)
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(!ThaiBuddhistEra.values.exists(_.name equalsIgnoreCase string)) {
            readValue[ThaiBuddhistEra](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading a DateTimeFormatter" should {
      "successfully read DateTimeFormatter patterns" in {
        val examplePatterns = Gen.oneOf("uuuu-MMM-dd", "yyyy MM dd")
        forAll(examplePatterns) { examplePattern: String ⇒
          readValue[DateTimeFormatter](examplePattern) shouldBe a[Right[_, _]]
        }
      }

      "return a failure for other values" in {
        val badExamplePatterns = Gen.oneOf("mmm", "ddd")
        forAll(badExamplePatterns) { badExamplePattern: String ⇒
          readValue[DateTimeFormatter](badExamplePattern) shouldBe a[Left[_, _]]
        }
      }
    }

    "reading a FormatStyle" should {
      "successfully read FormatStyle values" in {
        forAll(mixedCaseEnum(FormatStyle.values)(_.name)) {
          case (formatStyle, string) ⇒
            readValue[FormatStyle](string) shouldBe Right(formatStyle)
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(!FormatStyle.values.exists(_.name equalsIgnoreCase string)) {
            readValue[FormatStyle](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading a ResolverStyle" should {
      "successfully read ResolverStyle values" in {
        forAll(mixedCaseEnum(ResolverStyle.values)(_.name)) {
          case (resolverStyle, string) ⇒
            readValue[ResolverStyle](string) shouldBe Right(resolverStyle)
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(!ResolverStyle.values.exists(_.name equalsIgnoreCase string)) {
            readValue[ResolverStyle](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading a SignStyle" should {
      "successfully read SignStyle values" in {
        forAll(mixedCaseEnum(SignStyle.values)(_.name)) {
          case (signStyle, string) ⇒
            readValue[SignStyle](string) shouldBe Right(signStyle)
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(!SignStyle.values.exists(_.name equalsIgnoreCase string)) {
            readValue[SignStyle](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading a TextStyle" should {
      "successfully read TextStyle values" in {
        forAll(mixedCaseEnum(TextStyle.values)(_.name)) {
          case (textStyle, string) ⇒
            readValue[TextStyle](string) shouldBe Right(textStyle)
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(!TextStyle.values.exists(_.name equalsIgnoreCase string)) {
            readValue[TextStyle](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }
  }
}
