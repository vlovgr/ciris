package ciris

final class ConfigErrors private (val toVector: Vector[ConfigError]) extends AnyVal {
  def append(error: ConfigError): ConfigErrors =
    new ConfigErrors(toVector :+ error)

  def messages: Vector[String] =
    toVector.map(_.message)

  def size: Int =
    toVector.size

  override def toString: String =
    s"ConfigErrors(${toVector.mkString(",")})"
}

object ConfigErrors {
  def apply(error: ConfigError): ConfigErrors =
    new ConfigErrors(Vector(error))
}
