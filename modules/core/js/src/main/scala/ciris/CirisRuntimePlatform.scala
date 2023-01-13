/*
 * Copyright 2017-2023 Viktor Rudebeck
 *
 * SPDX-License-Identifier: MIT
 */

package ciris

import scala.scalajs.js
import scala.util.control.NonFatal

private[ciris] trait CirisRuntimePlatform {

  /**
    * Returns a new [[ConfigValue]] for the specified environment variable.
    */
  final def env(name: String): ConfigValue[Effect, String] =
    ConfigValue.suspend {
      val key = ConfigKey.env(name)

      val value =
        try {
          val env = js.Dynamic.global.process.env.asInstanceOf[js.Dictionary[Any]]
          env.get(name).collect { case value: String => value }
        } catch {
          case e if NonFatal(e) =>
            None
        }

      value match {
        case Some(value) => ConfigValue.loaded(key, value)
        case None        => ConfigValue.missing(key)
      }
    }
}
