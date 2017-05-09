package ciris

sealed abstract class ConfigValue[A] {
  def value: Either[ConfigError, A]

  private[ciris] def append[B](next: ConfigValue[B]): ConfigValue2[A, B] = {
    (value, next.value) match {
      case (Right(a), Right(b)) ⇒ new ConfigValue2(Right((a, b)))
      case (Left(error1), Right(_)) ⇒ new ConfigValue2(Left(ConfigErrors(error1)))
      case (Right(_), Left(error2)) ⇒ new ConfigValue2(Left(ConfigErrors(error2)))
      case (Left(error1), Left(error2)) ⇒ new ConfigValue2(Left(error1 append error2))
    }
  }
}

object ConfigValue {
  def apply[A](key: String)(
    implicit source: ConfigSource,
    reader: ConfigReader[A]
  ): ConfigValue[A] = {
    new ConfigValue[A] {
      override def value: Either[ConfigError, A] =
        reader.read(key)(source)
    }
  }
}
