package ciris

import cats.syntax.all._
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

  test("ConfigValue.product.default.fields") {
    check(
      prop("user.dir").product(prop("user.name")).default(("~", "default_user")),
      List(
        ConfigField(ConfigKey.prop("user.dir"), Some("~")),
        ConfigField(ConfigKey.prop("user.name"), Some("default_user"))
      )
    )
  }

  test("ConfigValue.map.default.fields") {
    check(
      prop("user.id").map(_.toInt).default(0),
      List(ConfigField(ConfigKey.prop("user.id"), None))
    )
  }

  test("ConfigValue.evalMap.default.fields") {
    check(
      prop("user.id").evalMap(x => IO(x.toInt)).default(0),
      List(ConfigField(ConfigKey.prop("user.id"), None))
    )
  }

  test("ConfigValue.imap.default.fields") {
    check(
      prop("user.id").imap(_.toInt)(_.toString).default(0),
      List(ConfigField(ConfigKey.prop("user.id"), Some("0")))
    )
  }

  test("ConfigValue.ievalMap.default.fields") {
    check(
      prop("user.id").ievalMap(x => IO(x.toInt))(_.toString).default(0),
      List(ConfigField(ConfigKey.prop("user.id"), Some("0")))
    )
  }

  test("ConfigValue.imapN.default.fields") {
    case class ProgramEnvironment(pwd: String, username: String)

    check(clue(
      (
        prop("user.dir"),
        prop("user.name")
      )
      .imapN(ProgramEnvironment.apply)(env => (env.pwd, env.username))
      .default(ProgramEnvironment("~", "default_user"))),
      List(
        ConfigField(ConfigKey.prop("user.dir"), Some("~")),
        ConfigField(ConfigKey.prop("user.name"), Some("default_user")),
      )
    )
  }
}