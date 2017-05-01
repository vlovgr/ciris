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
            read[DayOfWeek](string) shouldBe Right(dayOfWeek)
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(!DayOfWeek.values.exists(_.name equalsIgnoreCase string)) {
            read[DayOfWeek](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading a Duration" should {
      "successfully read Duration values" in {
        forAll { duration: Duration ⇒
          read[Duration](duration.toString) shouldBe Right(duration)
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(fails(Duration.parse(string))) {
            read[Duration](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading a Instant" should {
      "successfully read Instant values" in {
        forAll { instant: Instant ⇒
          read[Instant](instant.toString) shouldBe Right(instant)
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(fails(Instant.parse(string))) {
            read[Instant](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading a LocalDate" should {
      "successfully read LocalDate values" in {
        forAll { localDate: LocalDate ⇒
          read[LocalDate](localDate.toString) shouldBe Right(localDate)
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(fails(LocalDate.parse(string))) {
            read[LocalDate](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading a LocalDateTime" should {
      "successfully read LocalDateTime values" in {
        forAll { localDateTime: LocalDateTime ⇒
          read[LocalDateTime](localDateTime.toString) shouldBe Right(localDateTime)
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(fails(LocalDateTime.parse(string))) {
            read[LocalDateTime](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading a LocalTime" should {
      "successfully read LocalTime values" in {
        forAll { localTime: LocalTime ⇒
          read[LocalTime](localTime.toString) shouldBe Right(localTime)
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(fails(LocalTime.parse(string))) {
            read[LocalTime](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading a Month" should {
      "successfully read Month values" in {
        forAll(mixedCaseEnum(Month.values)(_.name)) {
          case (month, string) ⇒
            read[Month](string) shouldBe Right(month)
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(!Month.values.exists(_.name equalsIgnoreCase string)) {
            read[Month](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading a MonthDay" should {
      "successfully read MonthDay values" in {
        forAll { monthDay: MonthDay ⇒
          read[MonthDay](monthDay.toString) shouldBe Right(monthDay)
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(fails(MonthDay.parse(string))) {
            read[MonthDay](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading a OffsetDateTime" should {
      "successfully read OffsetDateTime values" in {
        forAll { offsetDateTime: OffsetDateTime ⇒
          read[OffsetDateTime](offsetDateTime.toString) shouldBe Right(offsetDateTime)
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(fails(OffsetDateTime.parse(string))) {
            read[OffsetDateTime](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading a OffsetTime" should {
      "successfully read OffsetTime values" in {
        forAll { offsetTime: OffsetTime ⇒
          read[OffsetTime](offsetTime.toString) shouldBe Right(offsetTime)
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(fails(OffsetTime.parse(string))) {
            read[OffsetTime](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading a Period" should {
      "successfully read Period values" in {
        forAll { period: Period ⇒
          read[Period](period.toString) shouldBe Right(period)
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(fails(Period.parse(string))) {
            read[Period](string) shouldBe a[Left[_, _]]
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

          read[Year](yearString) shouldBe Right(year)
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(fails(Year.parse(string))) {
            read[Year](string) shouldBe a[Left[_, _]]
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

          read[YearMonth](yearMonthString) shouldBe Right(yearMonth)
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(fails(YearMonth.parse(string))) {
            read[YearMonth](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading a ZonedDateTime" should {
      "successfully read ZonedDateTime values" in {
        forAll { zonedDateTime: ZonedDateTime ⇒
          read[ZonedDateTime](zonedDateTime.toString) shouldBe Right(zonedDateTime)
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(fails(ZonedDateTime.parse(string))) {
            read[ZonedDateTime](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading a ZoneId" should {
      "successfully read ZoneId values" in {
        forAll { zoneId: ZoneId ⇒
          read[ZoneId](zoneId.toString) shouldBe Right(zoneId)
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(fails(ZoneId.of(string))) {
            read[ZoneId](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading a ZoneOffset" should {
      "successfully read ZoneOffset values" in {
        forAll { zoneOffset: ZoneOffset ⇒
          read[ZoneOffset](zoneOffset.toString) shouldBe Right(zoneOffset)
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(fails(ZoneOffset.of(string))) {
            read[ZoneOffset](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading a Chronology" should {
      "successfully read Chronology values" in {
        forAll { chronology: Chronology ⇒
          read[Chronology](chronology.toString) shouldBe Right(chronology)
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(fails(Chronology.of(string))) {
            read[Chronology](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading a HijrahEra" should {
      "successfully read HijrahEra values" in {
        forAll(mixedCaseEnum(HijrahEra.values)(_.name)) {
          case (hijrahEra, string) ⇒
            read[HijrahEra](string) shouldBe Right(hijrahEra)
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(!HijrahEra.values.exists(_.name equalsIgnoreCase string)) {
            read[HijrahEra](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading a IsoEra" should {
      "successfully read IsoEra values" in {
        forAll(mixedCaseEnum(IsoEra.values)(_.name)) {
          case (isoEra, string) ⇒
            read[IsoEra](string) shouldBe Right(isoEra)
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(!IsoEra.values.exists(_.name equalsIgnoreCase string)) {
            read[IsoEra](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading a JapaneseEra" should {
      "successfully read JapaneseEra values" in {
        forAll(mixedCaseEnum(JapaneseEra.values)(_.toString)) {
          case (japaneseEra, string) ⇒
            read[JapaneseEra](string) shouldBe Right(japaneseEra)
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(!JapaneseEra.values.exists(_.toString equalsIgnoreCase string)) {
            read[JapaneseEra](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading a MinguoEra" should {
      "successfully read MinguoEra values" in {
        forAll(mixedCaseEnum(MinguoEra.values)(_.name)) {
          case (minguoEra, string) ⇒
            read[MinguoEra](string) shouldBe Right(minguoEra)
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(!MinguoEra.values.exists(_.name equalsIgnoreCase string)) {
            read[MinguoEra](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading a ThaiBuddhistEra" should {
      "successfully read ThaiBuddhistEra values" in {
        forAll(mixedCaseEnum(ThaiBuddhistEra.values)(_.name)) {
          case (thaiBuddhistEra, string) ⇒
            read[ThaiBuddhistEra](string) shouldBe Right(thaiBuddhistEra)
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(!ThaiBuddhistEra.values.exists(_.name equalsIgnoreCase string)) {
            read[ThaiBuddhistEra](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading a DateTimeFormatter" should {
      "successfully read DateTimeFormatter patterns" in {
        val examplePatterns = Gen.oneOf("uuuu-MMM-dd", "yyyy MM dd")
        forAll(examplePatterns) { examplePattern: String ⇒
          read[DateTimeFormatter](examplePattern) shouldBe a[Right[_, _]]
        }
      }

      "return a failure for other values" in {
        val badExamplePatterns = Gen.oneOf("mmm", "ddd")
        forAll(badExamplePatterns) { badExamplePattern: String ⇒
          read[DateTimeFormatter](badExamplePattern) shouldBe a[Left[_, _]]
        }
      }
    }

    "reading a FormatStyle" should {
      "successfully read FormatStyle values" in {
        forAll(mixedCaseEnum(FormatStyle.values)(_.name)) {
          case (formatStyle, string) ⇒
            read[FormatStyle](string) shouldBe Right(formatStyle)
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(!FormatStyle.values.exists(_.name equalsIgnoreCase string)) {
            read[FormatStyle](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading a ResolverStyle" should {
      "successfully read ResolverStyle values" in {
        forAll(mixedCaseEnum(ResolverStyle.values)(_.name)) {
          case (resolverStyle, string) ⇒
            read[ResolverStyle](string) shouldBe Right(resolverStyle)
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(!ResolverStyle.values.exists(_.name equalsIgnoreCase string)) {
            read[ResolverStyle](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading a SignStyle" should {
      "successfully read SignStyle values" in {
        forAll(mixedCaseEnum(SignStyle.values)(_.name)) {
          case (signStyle, string) ⇒
            read[SignStyle](string) shouldBe Right(signStyle)
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(!SignStyle.values.exists(_.name equalsIgnoreCase string)) {
            read[SignStyle](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading a TextStyle" should {
      "successfully read TextStyle values" in {
        forAll(mixedCaseEnum(TextStyle.values)(_.name)) {
          case (textStyle, string) ⇒
            read[TextStyle](string) shouldBe Right(textStyle)
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(!TextStyle.values.exists(_.name equalsIgnoreCase string)) {
            read[TextStyle](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }
  }
}
