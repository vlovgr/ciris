/*
 * Copyright 2017-2022 Viktor Rudebeck
 *
 * SPDX-License-Identifier: MIT
 */

package object ciris extends CirisRuntimePlatform {

  /**
    * Indicates a [[ConfigValue]] can be used with any effect type.
    */
  type Effect[A] <: Nothing

  /**
    * Returns a new [[ConfigValue]] with the specified default value.
    */
  final def default[A](value: => A): ConfigValue[Effect, A] =
    ConfigValue.default(value)

  /**
    * Returns a new [[ConfigValue]] for the specified system property.
    */
  final def prop(name: String): ConfigValue[Effect, String] =
    ConfigValue.suspend {
      val key = ConfigKey.prop(name)

      if (name.nonEmpty) {
        val value = System.getProperty(name)
        if (value != null) {
          ConfigValue.loaded(key, value)
        } else {
          ConfigValue.missing(key)
        }
      } else {
        ConfigValue.missing(key)
      }
    }
}
