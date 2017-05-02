package object ciris extends LoadConfigs {
  def env[A: ConfigReader](key: String): ConfigValue[A] =
    ConfigValue(key)(ConfigSource.Environment, ConfigReader[A])

  def prop[A: ConfigReader](key: String): ConfigValue[A] =
    ConfigValue(key)(ConfigSource.Properties, ConfigReader[A])

  def read[A: ConfigReader](key: String)(implicit source: ConfigSource): ConfigValue[A] =
    ConfigValue(key)(source, ConfigReader[A])

  def loadConfig[A, Z](a: ConfigValue[A])(f: A ⇒ Z): Either[ConfigError, Z] =
    a.value match {
      case Right(a) ⇒ Right(f(a))
      case Left(error) ⇒ Left(error)
    }
}
