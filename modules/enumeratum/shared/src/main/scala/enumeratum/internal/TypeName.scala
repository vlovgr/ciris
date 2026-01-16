/*
 * Copyright 2017-2026 Viktor Rudebeck
 *
 * SPDX-License-Identifier: MIT
 */

package enumeratum.internal

final case class TypeName[A](value: String)

object TypeName extends TypeNameRuntimePlatform
