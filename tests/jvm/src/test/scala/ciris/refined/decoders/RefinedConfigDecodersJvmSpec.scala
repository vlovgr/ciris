package ciris.refined.decoders

import ciris.PropertySpec
import ciris.refined._
import eu.timepit.refined.types.numeric.PosInt

final class RefinedConfigDecodersJvmSpec extends PropertySpec {
  "RefinedConfigDecoders" when {
    "the refinement predicate fails" should {
      "use a type tag for the refinement type" in {
        val error = readValue[PosInt]("0").left.value
        error.message shouldBe "Test key [key] with value [0] cannot be converted to type [eu.timepit.refined.api.Refined[Int,eu.timepit.refined.numeric.Positive]]: Predicate failed: (0 > 0)."
      }
    }
  }
}
