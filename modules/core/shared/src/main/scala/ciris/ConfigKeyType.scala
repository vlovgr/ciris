package ciris

sealed class ConfigKeyType(val name: String)

object ConfigKeyType {
  def apply(name: String): ConfigKeyType =
    new ConfigKeyType(name) {
      override def toString: String =
        s"ConfigKeyType($name)"
    }

  case object Environment extends ConfigKeyType("environment variable")

  case object Properties extends ConfigKeyType("system property")
}
