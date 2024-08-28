package ciris

/**
  * A single configuration field.
  */
sealed trait ConfigField {

  /**
    * This field's key.
    */
  def key: ConfigKey

  /**
    * Whether this field is optional or not. A field with a default value is considered optional.
    */
  val optional: Boolean = this match {
    case _: ConfigField.Required => false
    case _: ConfigField.Optional => true
  }

  /**
    * Whether or not this field is required aka not optional.
    */
  val required: Boolean = !optional

  /**
    * The default value of this field if any.
    */
  val defaultValue: Option[String] = this match {
    case ConfigField.Required(_)          => None
    case ConfigField.Optional(_, default) => default
  }

  def option: ConfigField = this match {
    case ConfigField.Required(key)  => ConfigField.Optional(key, None)
    case ConfigField.Optional(_, _) => this
  }
}

object ConfigField {

  case class Required(key: ConfigKey) extends ConfigField
  case class Optional(key: ConfigKey, default: Option[String]) extends ConfigField

  def apply(key: ConfigKey, defaultValue: Option[String] = None): ConfigField =
    defaultValue.fold[ConfigField](Required(key))(_ => Optional(key, defaultValue))
}
