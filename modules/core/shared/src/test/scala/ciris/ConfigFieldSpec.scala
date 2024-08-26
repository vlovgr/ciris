package ciris

import cats.syntax.eq._
import munit.CatsEffectSuite
import munit.ScalaCheckEffectSuite
import ciris.env
import ciris.prop
import ciris.prop
import cats.effect.IO
import ciris.Effect

final class ConfigFieldSpec extends CatsEffectSuite with ScalaCheckEffectSuite with Generators {

  def check[F[_], A](config: ConfigValue[F, A], expected: List[ConfigField]): Unit =
    assertEquals(config.fields, expected)

  test("ConfigValue.pure.fields") {
    check(ConfigValue.pure(ConfigEntry.loaded(None, "test")), Nil)
  }

  test("ConfigValue.environment.fields") {
    check(env("ENV_VARIABLE"), List(ConfigField(ConfigKey.env("ENV_VARIABLE"), None)))
  }

  test("ConfigValue.prop.fields") {
    check(prop("user.dir"), List(ConfigField(ConfigKey.prop("user.dir"), None)))
  }

  test("ConfigValue.prop.default.fields") {
    check(
      prop("user.dir").default("~"),
      List(ConfigField(ConfigKey.prop("user.dir"), Some("~")))
    )
  }

  test("ConfigValue.prop.default.default.fields") {
    check(
      prop("user.dir").default("/").default("~"),
      List(ConfigField(ConfigKey.prop("user.dir"), Some("~")))
    )
  }

  test("ConfigValue.or.default.fields") {
    check(
      prop("user.dir").or(env("USER_DIR")).default("~"),
      List(
        ConfigField(ConfigKey.prop("user.dir"), Some("~")),
        ConfigField(ConfigKey.env("USER_DIR"), Some("~"))
      )
    )
  }

  test("ConfigValue.secret.fields") {
    check(
      prop("user.dir").secret,
      List(ConfigField(ConfigKey.prop("user.dir"), None))
    )
  }

  test("ConfigValue.useOnceSecret.fields") {
    check(
      prop("user.dir").map(_.toCharArray).useOnceSecret,
      List(ConfigField(ConfigKey.prop("user.dir"), None))
    )
  }
}