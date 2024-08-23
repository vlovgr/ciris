/*
 * Copyright 2017-2024 Viktor Rudebeck
 *
 * SPDX-License-Identifier: MIT
 */

package object ciris extends CirisRuntimePlatform {

  /**
    * Indicates a [[ConfigValue]] can be used with any effect type.
    */
  type Effect[A] <: Nothing

  /**
  * Returns a new [[ConfigValue]] for the specified environment variable.
  */
  final def env(name: String): ConfigValue[Effect, String] =
    ConfigValue.Environment(name)

  /**
    * Returns a new [[ConfigValue]] with the specified default value.
    */
  final def default[A](value: => A): ConfigValue[Effect, A] =
    ConfigValue.default(value)

  /**
    * Returns a new [[ConfigValue]] for the specified system property.
    */
  final def prop(name: String): ConfigValue[Effect, String] =
    ConfigValue.Property(name)
}
