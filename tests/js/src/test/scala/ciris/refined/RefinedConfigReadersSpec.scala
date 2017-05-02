package ciris.refined

import ciris.PropertySpec
import eu.timepit.refined.types.numeric.PosInt

final class RefinedConfigReadersSpec extends PropertySpec {
  "RefinedConfigReaders" when {
    "reading a RefType" should {
      "successfully read a RefType" in {
        readValue[PosInt]("1").right.map(_.value) shouldBe Right(1)
      }

      "return a failure for wrong type" in {
        readValue[PosInt]("0") shouldBe a[Left[_, _]]
      }

      "return a failure for missing key" in {
        readNonExistingValue[PosInt] shouldBe a[Left[_, _]]
      }
    }
  }
}
