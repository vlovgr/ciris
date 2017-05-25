package ciris.squants.generators

import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import squants.market._
import squants.{Dimension, Quantity}

trait SquantsGenerators {
  def genQuantity[A <: Quantity[A]](dimension: Dimension[A]): Gen[A] =
    for {
      value <- arbitrary[Double]
      unit <- Gen.oneOf(dimension.units.toList)
    } yield unit(value)

  val genCurrency: Gen[Currency] =
    Gen.oneOf(
      USD,
      ARS,
      AUD,
      BRL,
      CAD,
      CHF,
      CLP,
      CNY,
      CZK,
      DKK,
      EUR,
      GBP,
      HKD,
      INR,
      JPY,
      KRW,
      MXN,
      MYR,
      NOK,
      NZD,
      RUB,
      SEK,
      XAG,
      XAU
      //BTC 0E-15 BTC fails to parse
    )

  val genMoney: Gen[Money] =
    for {
      value <- arbitrary[Double]
      currency <- genCurrency
    } yield {
      val moneyValue =
        BigDecimal(value)
          .setScale(
            currency.formatDecimals,
            BigDecimal.RoundingMode.HALF_EVEN
          )

      Money(moneyValue, currency)
    }
}
