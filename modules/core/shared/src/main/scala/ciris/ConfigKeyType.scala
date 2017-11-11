package ciris

/**
  * Represents the name and type of keys that can be read from a [[ConfigSource]].
  * Names should be in singular form and in all lowercase. The [[ConfigKeyType]]s
  * supported by Ciris in the core module include the following:<br>
  * <br>
  * - [[ConfigKeyType#Environment]]: "environment variable" of type `String`,<br>
  * - [[ConfigKeyType#Property]]: "system property" of type `String`, and<br>
  * - [[ConfigKeyType#Argument]]: "command-line argument" of type `Int`.<br>
  * <br>
  * To create a new [[ConfigKeyType]] use the apply method in the companion object,
  * specifying the name of the key and its type. Note that there are no checks for
  * whether the name is in singular form or in all lowercase, since the only real
  * implication is for error messages.
  *
  * {{{
  * scala> ConfigKeyType[String]("custom key")
  * res0: ConfigKeyType[String] = ConfigKeyType(custom key)
  * }}}
  *
  * @param name the name of the key, for example, "environment variable";
  *             should be in singular form and in all lowercase letters
  * @tparam Key the type of the key
  */
sealed class ConfigKeyType[Key](val name: String)

object ConfigKeyType extends ConfigKeyTypePlatformSpecific {

  /**
    * Creates a new [[ConfigKeyType]] for the specified key name and type.
    * Note that the type cannot be inferred automatically, so you will have
    * to specify it explicitly like in the following example.
    *
    * @param name the name of the key, for example, "environment variable";
    *             should be in singular form and in all lowercase letters
    * @tparam Key the type of the key
    * @return a new [[ConfigKeyType]] for the specified key name and type
    * @example {{{
    * scala> ConfigKeyType[String]("custom key")
    * res0: ConfigKeyType[String] = ConfigKeyType(custom key)
    * }}}
    */
  def apply[Key](name: String): ConfigKeyType[Key] =
    new ConfigKeyType[Key](name) {
      override def toString: String =
        s"ConfigKeyType($name)"
    }

  /**
    * [[ConfigKeyType]] for an environment variable of type `String`.
    */
  case object Environment extends ConfigKeyType[String]("environment variable")

  /**
    * [[ConfigKeyType]] for a system property of type `String`.
    */
  case object Property extends ConfigKeyType[String]("system property")

  /**
    * [[ConfigKeyType]] for a command-line argument of type `Int`.
    */
  case object Argument extends ConfigKeyType[Int]("command-line argument")
}
