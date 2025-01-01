/*
 * Copyright 2017-2025 Viktor Rudebeck
 *
 * SPDX-License-Identifier: MIT
 */

package ciris.refined.internal

final case class TypeName[A](value: String)

object TypeName extends TypeNameRuntimePlatform
