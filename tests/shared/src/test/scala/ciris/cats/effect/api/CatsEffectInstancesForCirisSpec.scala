package ciris.cats.effect.api

import ciris.PropertySpec
import ciris.api.Sync

final class CatsEffectInstancesForCirisSpec extends PropertySpec {
  "CatsEffectInstancesForCiris" when {
    "providing instances for IO" should {
      "be able to provide all required instances" in {
        import _root_.cats.effect.IO
        import ciris.cats.effect._

        Sync[IO]
      }
    }
  }
}
