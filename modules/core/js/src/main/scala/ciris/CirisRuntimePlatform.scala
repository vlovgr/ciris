/*
 * Copyright 2017-2024 Viktor Rudebeck
 *
 * SPDX-License-Identifier: MIT
 */

package ciris

import scala.scalajs.js
import scala.util.control.NonFatal

private[ciris] trait CirisRuntimePlatform {

  private[ciris] final def getEnv(name: String): ConfigEntry[String] = {
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
        case Some(value) => ConfigEntry.loaded(Some(key), value)
        case None        => ConfigEntry.missing(key)
      }
  }
}
