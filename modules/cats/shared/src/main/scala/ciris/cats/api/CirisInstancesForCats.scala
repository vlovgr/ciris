package ciris.cats.api

import cats.Show
import ciris._

trait CirisInstancesForCats {
  implicit def showConfigEntry[F[_], K, S, V](
    implicit showK: Show[K],
    showS: Show[F[Either[ConfigError, S]]],
    showV: Show[F[Either[ConfigError, V]]]
  ): Show[ConfigEntry[F, K, S, V]] = Show.show { entry =>
    val key = showK.show(entry.key)
    val keyType = showConfigKeyType[K].show(entry.keyType)
    val sourceValue = showS.show(entry.sourceValue)
    val value = showV.show(entry.value)

    if (sourceValue == value) s"ConfigEntry($key, $keyType, $value)"
    else s"ConfigEntry($key, $keyType, $sourceValue, $value)"
  }

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
}
