package ciris

/**
  * Describes whether a [[ConfigKey]] is either required
  * or optional. If the key is optional, a default value
  * can optionally be specified.
  */
sealed abstract class ConfigField {

  /**
    * The key described by the field.
    */
  val key: ConfigKey

  /**
    * Returns whether the key is optional or not.
    *
    * An optional key with a default value is also
    * considered optional.
    */
  final val optional: Boolean =
    this match {
      case _: ConfigField.Required => false
      case _: ConfigField.Optional => true
    }

  /**
    * Returns whether the key is required or not.
    */
  final val required: Boolean =
    !optional

  /**
    * Returns the default value of an optional field.
    *
    * If the field is required, or if the field is optional
    * but no default value is specified, `None` is returned.
    */
  val defaultValue: Option[String]

  /**
    * If the field is required, returns an optional field
    * without a default value; or if the field is already
    * optional, returns it without changes.
    */
  final def option: ConfigField =
    this match {
      case ConfigField.Required(key)  => ConfigField.Optional(key, None)
      case ConfigField.Optional(_, _) => this
    }
}

object ConfigField {

  /**
    * Field where the key is optional and where
    * there is an optional default value.
    */
  private final case class Optional(
    override val key: ConfigKey,
    override val defaultValue: Option[String]
  ) extends ConfigField

  /**
    * Field where the key is required.
    */
  private final case class Required(
    override val key: ConfigKey
  ) extends ConfigField {
    override val defaultValue: Option[String] =
      None
  }

  /**
    * Returns an [[optional]] field if the specified default
    * value is available; otherwise a [[required]] field.
    */
  final def fromOption(key: ConfigKey, defaultValue: Option[String]): ConfigField =
    defaultValue match {
      case Some(_) => optional(key, defaultValue)
      case None    => required(key)
    }

  /**
    * Returns an optional field with the specified default value.
    */
  final def optional(key: ConfigKey, defaultValue: Option[String]): ConfigField =
    ConfigField.Optional(key, defaultValue)

  /**
    * Returns a required field for the specified key.
    */
  final def required(key: ConfigKey): ConfigField =
    ConfigField.Required(key)
}
