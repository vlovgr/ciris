package ciris.refined.decoders

import ciris.PropertySpec
import ciris.refined._
import eu.timepit.refined.types.numeric.PosInt
import org.scalacheck.Gen

final class RefinedConfigDecodersSpec extends PropertySpec {
  "RefinedConfigDecoders" when {
    "reading a RefType" should {
      "successfully read RefType values" in {
        forAll(Gen.posNum[Int]) { posInt =>
          readValue[PosInt](posInt.toString).right.map(_.value) shouldBe Right(posInt)
        }
      }

      "successfully read optional RefType values" in {
        forAll(Gen.posNum[Int]) { posInt =>
          readValue[Option[PosInt]](posInt.toString).right.map(_.map(_.value)) shouldBe Right(Some(posInt))
        }
      }

      "return a failure for wrong type" in {
        forAll(Gen.choose(Int.MinValue, 0)) { nonPosInt =>
          readValue[PosInt](nonPosInt.toString) shouldBe a[Left[_, _]]
        }
      }

      "return a failure for missing key" in {
        readNonExistingValue[PosInt] shouldBe a[Left[_, _]]
      }
    }
  }
}
