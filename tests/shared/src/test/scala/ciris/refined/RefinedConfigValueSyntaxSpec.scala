package ciris.refined

import ciris.refined.syntax._
import ciris._
import eu.timepit.refined.numeric.Positive
import org.scalacheck.{Gen, Shrink}

final class RefinedConfigValueSyntaxSpec extends PropertySpec {
  implicit def noShrink[T] = Shrink[T](_ => Stream.empty)

  "RefinedConfigValueSyntax" when {
    "using refine" should {
      "successfully refine values conforming to predicate" in {
        forAll(Gen.chooseNum(1, Int.MaxValue)) { n: Int =>
          ConfigValue(Right(n)).refine[Positive].value shouldBe a[Right[_, _]]
        }
      }

      "return an error when refining values not confirming to predicate" in {
        forAll(Gen.chooseNum(Int.MinValue, 0)) { n: Int =>
          ConfigValue(Right(n)).refine[Positive].value shouldBe a[Left[_, _]]
        }
      }

      "return an error when refining unavailable values" in {
        ConfigValue[Int](Left(ConfigError("error"))).refine[Positive].value shouldBe a[Left[_, _]]
      }
    }

    "using mapRefine" should {
      "successfully refine value conforming to predicate" in {
        forAll(Gen.chooseNum(1, Int.MaxValue)) { n: Int =>
          ConfigValue(Right(n)).mapRefine[Positive](identity).value shouldBe a[Right[_, _]]
        }
      }

      "return an error when refining value not confirming to predicate" in {
        forAll(Gen.chooseNum(Int.MinValue, 0)) { n: Int =>
          ConfigValue(Right(n)).mapRefine[Positive](identity).value shouldBe a[Left[_, _]]
        }
      }

      "return an error when refining unavailable values" in {
        ConfigValue[Int](Left(ConfigError("error")))
          .mapRefine[Positive](identity)
          .value shouldBe a[Left[_, _]]
      }
    }
  }
}
