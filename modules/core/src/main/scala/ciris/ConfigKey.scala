/*
 * Copyright 2017-2019 Viktor Lövgren
 *
 * SPDX-License-Identifier: MIT
 */

package ciris

import cats.{Eq, Show}
import cats.implicits._
import java.nio.charset.Charset
import java.nio.file.Path

/**
  * Provides a description of a key used for loading configuration values.
  *
  * @example {{{
  * scala> val apiKey = ConfigKey.env("API_KEY")
  * apiKey: ConfigKey = ConfigKey(environment variable API_KEY)
  *
  * scala> apiKey.description
  * res0: String = environment variable API_KEY
  * }}}
  */
sealed abstract class ConfigKey {

  /**
    * Returns a description of a key used for loading
    * configuration values.
    */
  def description: String
}

/**
  * @groupname Create Creating Instances
  * @groupprio Create 0
  *
  * @groupname Instances Type Class Instances
  * @groupprio Instances 1
  */
final object ConfigKey {

  /**
    * Returns a new [[ConfigKey]] with the specified description.
    *
    * @group Create
    */
  final def apply(description: => String): ConfigKey = {
    def _description = description
    new ConfigKey {
      override final def description: String =
        _description

      override final def hashCode: Int =
        description.hashCode

      override final def equals(that: Any): Boolean =
        that match {
          case key: ConfigKey => configKeyEq.eqv(this, key)
          case _              => false
        }

      override final def toString: String =
        s"ConfigKey($description)"
    }
  }

  /**
    * Returns a new [[ConfigKey]] for the specified environment variable.
    *
    * @example {{{
    * scala> val apiKey = ConfigKey.env("API_KEY")
    * apiKey: ConfigKey = ConfigKey(environment variable API_KEY)
    *
    * scala> apiKey.description
    * res0: String = environment variable API_KEY
    * }}}
    *
    * @group Create
    */
  final def env(name: String): ConfigKey =
    ConfigKey(s"environment variable $name")

  /**
    * Returns a new [[ConfigKey]] for the specified path and charset.
    *
    * @group Create
    */
  final def file(path: Path, charset: Charset): ConfigKey =
    ConfigKey(s"file at $path with charset $charset")

  /**
    * Returns a new [[ConfigKey]] for the specified system property.
    *
    * @example {{{
    * scala> val apiKey = ConfigKey.prop("api.key")
    * apiKey: ConfigKey = ConfigKey(system property api.key)
    *
    * scala> apiKey.description
    * res0: String = system property api.key
    * }}}
    *
    * @group Create
    */
  final def prop(name: String): ConfigKey =
    ConfigKey(s"system property $name")

  /**
    * Returns the description for the specified [[ConfigKey]].
    *
    * This function enables pattern matching on [[ConfigKey]]s.
    *
    * @example {{{
    * scala> val apiKey = ConfigKey.env("API_KEY")
    * apiKey: ConfigKey = ConfigKey(environment variable API_KEY)
    *
    * scala> apiKey match { case ConfigKey(description) => description }
    * res0: String = environment variable API_KEY
    * }}}
    *
    * @group Create
    */
  final def unapply(key: ConfigKey): Some[String] =
    Some(key.description)

  /**
    * @group Instances
    */
  implicit final val configKeyEq: Eq[ConfigKey] =
    Eq.by(_.description)

  /**
    * @group Instances
    */
  implicit final val configKeyShow: Show[ConfigKey] =
    Show.fromToString
}
