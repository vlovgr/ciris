package ciris.cats.api

import cats.{Semigroup, Show}
import ciris._

trait CirisInstancesForCats {
  implicit def showConfigEntry[F[_], K, S, V](
    implicit showKey: Show[K],
    showKeyType: Show[ConfigKeyType[K]]
  ): Show[ConfigEntry[F, K, S, V]] = Show.show { entry =>
    val key = showKey.show(entry.key)
    val keyType = showKeyType.show(entry.keyType)
    s"ConfigEntry($key, $keyType)"
  }

  implicit def showConfigValue[F[_], V]: Show[ConfigValue[F, V]] =
    Show.fromToString

  implicit val showConfigError: Show[ConfigError] =
    Show.fromToString

  implicit val showConfigErrors: Show[ConfigErrors] =
    Show.fromToString

  implicit val showConfigException: Show[ConfigException] =
    Show.fromToString

  implicit def showConfigKeyType[K]: Show[ConfigKeyType[K]] =
    Show.fromToString

  implicit def showSecret[A]: Show[Secret[A]] =
    Show.fromToString

  implicit val semigroupConfigErrors: Semigroup[ConfigErrors] =
    Semigroup.instance(_ combine _)
}
