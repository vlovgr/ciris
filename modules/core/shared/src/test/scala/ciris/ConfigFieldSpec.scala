package ciris

import cats.effect.IO
import cats.syntax.all._
import munit.CatsEffectSuite
import munit.ScalaCheckEffectSuite

final class ConfigFieldSpec extends CatsEffectSuite with ScalaCheckEffectSuite with Generators {
  def check[F[_], A](config: ConfigValue[F, A], expected: List[ConfigField]): Unit =
    assertEquals(config.fields, expected)

  test("ConfigValue.pure.fields") {
    check(ConfigValue.pure(ConfigEntry.loaded(None, "test")), Nil)
  }

  test("ConfigValue.environment.fields") {
    check(env("ENV_VARIABLE"), List(ConfigField.required(ConfigKey.env("ENV_VARIABLE"))))
  }

  test("ConfigValue.prop.fields") {
    check(prop("user.dir"), List(ConfigField.required(ConfigKey.prop("user.dir"))))
  }

  test("ConfigValue.prop.default.fields") {
    check(
      prop("user.dir").default("~"),
      List(ConfigField.optional(ConfigKey.prop("user.dir"), Some("~")))
    )
  }

  test("ConfigValue.prop.default.default.fields") {
    check(
      prop("user.dir").default("/").default("~"),
      List(ConfigField.optional(ConfigKey.prop("user.dir"), Some("~")))
    )
  }

  test("ConfigValue.option.fields") {
    check(
      prop("user.dir").option,
      List(ConfigField.optional(ConfigKey.prop("user.dir"), None))
    )
  }

  test("ConfigValue.option.option.fields") {
    check(
      prop("user.dir").option.option,
      List(ConfigField.optional(ConfigKey.prop("user.dir"), None))
    )
  }

  test("ConfigValue.option.default.fields") {
    check(
      prop("user.dir").option.default(Some("discarded since option always returns a value")),
      List(ConfigField.optional(ConfigKey.prop("user.dir"), None))
    )
  }

  test("ConfigValue.default.option.fields") {
    check(
      prop("user.dir").default("~").option,
      List(ConfigField.optional(ConfigKey.prop("user.dir"), Some("~")))
    )
  }

  test("ConfigValue.or.default.fields") {
    check(
      prop("user.dir").or(env("USER_DIR")).default("~"),
      List(
        ConfigField.optional(ConfigKey.prop("user.dir"), Some("~")),
        ConfigField.optional(ConfigKey.env("USER_DIR"), Some("~"))
      )
    )
  }

  test("ConfigValue.secret.fields") {
    check(
      prop("user.dir").secret,
      List(ConfigField.required(ConfigKey.prop("user.dir")))
    )
  }

  test("ConfigValue.useOnceSecret.fields") {
    check(
      prop("user.dir").imap(_.toCharArray)(_.mkString).useOnceSecret,
      List(ConfigField.required(ConfigKey.prop("user.dir")))
    )
  }

  test("ConfigValue.product.default.fields") {
    check(
      prop("user.dir").product(prop("user.name")).default(("~", "default_user")),
      List(
        ConfigField.optional(ConfigKey.prop("user.dir"), Some("~")),
        ConfigField.optional(ConfigKey.prop("user.name"), Some("default_user"))
      )
    )
  }

  test("ConfigValue.imap.default.fields") {
    check(
      prop("user.id").imap(_.toInt)(_.toString).default(0),
      List(ConfigField.optional(ConfigKey.prop("user.id"), Some("0")))
    )
  }

  test("ConfigValue.ievalMap.default.fields") {
    check(
      prop("user.id").ievalMap(x => IO(x.toInt))(_.toString).default(0),
      List(ConfigField.optional(ConfigKey.prop("user.id"), Some("0")))
    )
  }

  test("ConfigValue.imapN.default.fields") {
    case class ProgramEnvironment(pwd: String, username: String)

    check(
      clue(
        (
          prop("user.dir"),
          prop("user.name")
        )
          .imapN(ProgramEnvironment.apply)(env => (env.pwd, env.username))
          .default(ProgramEnvironment("~", "default_user"))
      ),
      List(
        ConfigField.optional(ConfigKey.prop("user.dir"), Some("~")),
        ConfigField.optional(ConfigKey.prop("user.name"), Some("default_user"))
      )
    )
  }
}
