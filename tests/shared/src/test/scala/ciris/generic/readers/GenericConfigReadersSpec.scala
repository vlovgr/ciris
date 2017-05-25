package ciris.generic.readers

import ciris.PropertySpec
import ciris.generic._
import shapeless._

final case class Port(value: Int) extends AnyVal

sealed trait DoubleOrBoolean
final case class DoubleValue(value: Double) extends DoubleOrBoolean
final case class BooleanValue(value: Boolean) extends DoubleOrBoolean

final case class IntValue(value: Int)

final class GenericConfigReadersSpec extends PropertySpec {
  "ShapelessConfigReaders" when {
    "reading value classes" should {
      "successfully read value classes" in {
        forAll { value: Int =>
          readValue[Port](value.toString) shouldBe Right(Port(value))
        }
      }

      "return a failure for the wrong type" in {
        forAll { value: String =>
          whenever(fails(value.toInt)) {
            readValue[Port](value) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading coproducts" should {
      type DoubleOrBooleanCoproduct = DoubleValue :+: BooleanValue :+: CNil

      "successfully read coproduct values" in {
        forAll { double: Double =>
          readValue[DoubleOrBooleanCoproduct](double.toString) shouldBe
            Right(Coproduct[DoubleOrBooleanCoproduct](DoubleValue(double)))
        }

        forAll { boolean: Boolean =>
          readValue[DoubleOrBooleanCoproduct](boolean.toString) shouldBe
            Right(Coproduct[DoubleOrBooleanCoproduct](BooleanValue(boolean)))
        }
      }

      "return a failure for wrong coproduct values" in {
        forAll { string: String =>
          whenever(fails(string.toDouble)) {
            whenever(fails(string.toBoolean)) {
              readValue[DoubleOrBooleanCoproduct](string) shouldBe a[Left[_, _]]
            }
          }
        }
      }

      "successfully read generic coproduct values" in {
        forAll { double: Double =>
          readValue[DoubleOrBoolean](double.toString) shouldBe
            Right(DoubleValue(double))
        }

        forAll { boolean: Boolean =>
          readValue[DoubleOrBoolean](boolean.toString) shouldBe
            Right(BooleanValue(boolean))
        }
      }

      "return a failure for wrong generic coproduct values" in {
        forAll { string: String =>
          whenever(fails(string.toDouble)) {
            whenever(fails(string.toBoolean)) {
              readValue[DoubleOrBoolean](string) shouldBe a[Left[_, _]]
            }
          }
        }
      }
    }

    "reading products with arity one" should {
      "successfully read product values" in {
        forAll { int: Int =>
          readValue[IntValue](int.toString) shouldBe Right(IntValue(int))
        }
      }

      "return a failure for wrong product values" in {
        forAll { string: String =>
          whenever(fails(string.toInt)) {
            readValue[IntValue](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }
  }
}
