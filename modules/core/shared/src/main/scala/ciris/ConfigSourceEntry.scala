package ciris

final case class ConfigSourceEntry[Key](
  key: Key,
  keyType: ConfigKeyType[Key],
  value: Either[ConfigError, String]
)
