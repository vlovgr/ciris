package ciris

final class ConfigValuePartiallyApplied[Value] {
  def apply[Key](key: Key)(
    implicit source: ConfigSource[Key],
    reader: ConfigReader[Value]
  ): ConfigValue[Value] =
    ConfigValue[Key, Value](key)
}
