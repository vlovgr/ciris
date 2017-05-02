package object ciris extends LoadConfigs {
  def env[A: ConfigReader](key: String): ConfigValue[A] =
    ConfigValue(key)(ConfigSource.Environment, ConfigReader[A])

  def prop[A: ConfigReader](key: String): ConfigValue[A] =
    ConfigValue(key)(ConfigSource.Properties, ConfigReader[A])

  def read[A: ConfigReader](key: String)(implicit source: ConfigSource): ConfigValue[A] =
    ConfigValue(key)(source, ConfigReader[A])
}
