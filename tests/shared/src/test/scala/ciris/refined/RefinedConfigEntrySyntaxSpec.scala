package ciris.refined

import ciris._
import ciris.api._
import ciris.refined.syntax._
import eu.timepit.refined.numeric.Positive
import org.scalacheck.{Gen, Shrink}

final class RefinedConfigEntrySyntaxSpec extends PropertySpec {
  implicit def noShrink[T]: Shrink[T] = Shrink(_ => Stream.empty)

  "RefinedConfigEntrySyntax" when {
    "using refine" should {
      "successfully refine values conforming to predicate" in {
        forAll(Gen.chooseNum(1, Int.MaxValue)) { n: Int =>
          ConfigEntry[Id, String, Int]("key", ConfigKeyType.Property, Right(n))
            .refineValue[Positive].value shouldBe a[Right[_, _]]
        }
      }

      "return an error when refining values not confirming to predicate" in {
        forAll(Gen.chooseNum(Int.MinValue, 0)) { n: Int =>
          ConfigEntry[Id, String, Int]("key", ConfigKeyType.Property, Right(n))
            .refineValue[Positive].value shouldBe a[Left[_, _]]
        }
      }

      "return an error when refining unavailable values" in {
        ConfigEntry[Id, String, Int]("key", ConfigKeyType.Property, Left(ConfigError("error")))
          .refineValue[Positive].value shouldBe a[Left[_, _]]
      }
    }

    "using mapRefine" should {
      "successfully refine value conforming to predicate" in {
        forAll(Gen.chooseNum(1, Int.MaxValue)) { n: Int =>
          ConfigEntry[Id, String, Int]("key", ConfigKeyType.Property, Right(n))
            .mapRefineValue[Positive](identity).value shouldBe a[Right[_, _]]
        }
      }

      "return an error when refining value not confirming to predicate" in {
        forAll(Gen.chooseNum(Int.MinValue, 0)) { n: Int =>
          ConfigEntry[Id, String, Int]("key", ConfigKeyType.Property, Right(n))
            .mapRefineValue[Positive](identity).value shouldBe a[Left[_, _]]
        }
      }

      "return an error when refining unavailable values" in {
        ConfigEntry[Id, String, Int]("key", ConfigKeyType.Property, Left(ConfigError("error")))
          .mapRefineValue[Positive](identity)
          .value shouldBe a[Left[_, _]]
      }
    }
  }
}
