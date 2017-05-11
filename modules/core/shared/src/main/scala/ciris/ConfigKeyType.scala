package ciris

sealed class ConfigKeyType(val value: String)

object ConfigKeyType {
  def apply(value: String): ConfigKeyType =
    new ConfigKeyType(value) {
      override def toString: String =
        s"ConfigKeyType($value)"
    }

  case object Environment extends ConfigKeyType("environment variable")

  case object Properties extends ConfigKeyType("system property")
}
