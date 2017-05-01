package ciris.readers

import java.util.UUID

import ciris.PropertySpec
import org.scalacheck.Gen

final class JavaUtilConfigReadersSpec extends PropertySpec {
  "JavaUtilConfigReaders" when {
    "reading an UUID" should {
      "successfully read UUID values" in {
        forAll(Gen.uuid) { uuid ⇒
          read[UUID](uuid.toString) shouldBe Right(uuid)
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(fails(UUID.fromString(string))) {
            read[UUID](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }
  }
}
