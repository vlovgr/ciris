/*
 * Copyright 2017-2022 Viktor Rudebeck
 *
 * SPDX-License-Identifier: MIT
 */

package enumeratum.internal

final case class TypeName[A](value: String)

object TypeName extends TypeNameRuntimePlatform
