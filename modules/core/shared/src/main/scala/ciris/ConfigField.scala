package ciris

/**
  * A single configuration field.
  *
  * @param key the field/entry key
  * @param defaultValue the value to use if this field is absent
  */
final case class ConfigField(key: ConfigKey, defaultValue: Option[String])