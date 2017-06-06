package object ciris extends LoadConfigs {
  def env[Value: ConfigReader](key: String): ConfigValue[Value] =
    ConfigValue(key)(ConfigSource.Environment, ConfigReader[Value])

  def prop[Value: ConfigReader](key: String): ConfigValue[Value] =
    ConfigValue(key)(ConfigSource.Properties, ConfigReader[Value])

  def read[Value]: ConfigValuePartiallyApplied[Value] =
    new ConfigValuePartiallyApplied[Value]
}
