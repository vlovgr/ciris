package ciris

import org.apache.commons.codec.digest.DigestUtils.sha1Hex

final class SecretJvmSpec extends JvmPropertySpec {
  "Secret" should {
    "calculate the expected hash" in {
      forAll { s: String =>
        Secret(s).valueHash shouldBe sha1Hex(s)
      }
    }
  }
}
