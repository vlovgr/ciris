package ciris

final class CirisJsSpec extends PropertySpec {
  "Ciris" when {
    "being used in a JavaScript environment" should {
      "successfully be able to load a configuration" in {
        withValue(env[Option[String]]("APP_ENV")) {
          case None ⇒
            loadConfig(prop[Option[Int]]("http.port")) { port ⇒
              ("changeme", port getOrElse 4000, "local")
            }

          case Some(appEnv) ⇒
            loadConfig(
              env[String]("API_KEY"),
              prop[Int]("http.port")
            ) { (apiKey, httpPort) ⇒
              (apiKey, httpPort, appEnv)
            }
        } shouldBe a[Right[_, _]]
      }
    }
  }
}
