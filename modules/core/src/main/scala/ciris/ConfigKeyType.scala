package ciris

import java.io.{File => JFile}
import java.nio.charset.Charset

/**
  * [[ConfigKeyType]] represents the name and type of keys that can be
  * read from a [[ConfigSource]]. Names should be in singular form and
  * in all lowercase. The [[ConfigKeyType]]s supported by Ciris in the
  * core module include the following:<br>
  * <br>
  * - [[ConfigKeyType#Environment]]: "environment variable" of type `String`,<br>
  * - [[ConfigKeyType#Property]]: "system property" of type `String`,<br>
  * - [[ConfigKeyType#Argument]]: "command-line argument" of type `Int`, and<br>
  * - `ConfigKeyType.File`: "file" of type `(File, Charset)`.<br>
  * <br>
  * To create a new [[ConfigKeyType]], use the [[ConfigKeyType#apply]] method,
  * specifying the name of the key and its type. Note that there is no check
  * whether the name is in singular form or in all lowercase, since the only
  * real implication is for error messages.
  *
  * @param name the name of the key, for example "environment variable";
  *             should be in singular form and in all lowercase letters
  * @tparam K the type of the key
  * @example {{{
  * scala> ConfigKeyType[String]("custom key")
  * res0: ConfigKeyType[String] = ConfigKeyType(custom key)
  * }}}
  */
sealed class ConfigKeyType[K](val name: String)

object ConfigKeyType {

  /**
    * Creates a new [[ConfigKeyType]] with the specified key name and type.
    * Note that the type cannot be inferred automatically, so you will have
    * to specify it explicitly, like in the following example.
    *
    * @param name the name of the key, for example "environment variable";
    *             should be in singular form and in all lowercase letters
    * @tparam K the type of the key
    * @return a new [[ConfigKeyType]] with the specified key name and type
    * @example {{{
    * scala> ConfigKeyType[String]("custom key")
    * res0: ConfigKeyType[String] = ConfigKeyType(custom key)
    * }}}
    */
  def apply[K](name: String): ConfigKeyType[K] =
    new ConfigKeyType[K](name) {
      override def toString: String =
        s"ConfigKeyType($name)"
    }

  /**
    * [[ConfigKeyType]] for an environment variable of type `String`.
    */
  object Environment extends ConfigKeyType[String]("environment variable") {
    override def toString: String = "Environment"
  }

  /**
    * [[ConfigKeyType]] for a system property of type `String`.
    */
  object Property extends ConfigKeyType[String]("system property") {
    override def toString: String = "Property"
  }

  /**
    * [[ConfigKeyType]] for a command-line argument of type `Int`.
    */
  object Argument extends ConfigKeyType[Int]("command-line argument") {
    override def toString: String = "Argument"
  }

  /**
    * [[ConfigKeyType]] for a file of type `File` with charset `Charset`.
    */
  object File extends ConfigKeyType[(JFile, Charset)]("file") {
    override def toString: String = "File"
  }
}
