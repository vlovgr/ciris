package ciris

sealed class ConfigKeyType[Key](val name: String)

object ConfigKeyType {
  def apply[Key](name: String): ConfigKeyType[Key] =
    new ConfigKeyType[Key](name) {
      override def toString: String =
        s"ConfigKeyType($name)"
    }

  case object Environment extends ConfigKeyType[String]("environment variable")

  case object Properties extends ConfigKeyType[String]("system property")
}
