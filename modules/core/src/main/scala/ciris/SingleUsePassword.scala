/*
 * Copyright 2017-2020 Viktor Lövgren
 *
 * SPDX-License-Identifier: MIT
 */

package ciris

import java.security.KeyStore.PasswordProtection

import cats.effect.{Bracket, Resource, Sync}

/**
  * A single use password, after use it will be destroyed from the memory.
  */
class SingleUsePassword[F[_]] private (private val password: PasswordProtection) {

  def useAndDestroy[G[x] >: F[x], B](
    f: Array[Char] => G[B]
  )(implicit S: Sync[F], F: Bracket[G[?], Throwable]): G[B] =
    Resource.fromDestroyable(S.delay(password)).map(_.getPassword).use(f)
}

object SingleUsePassword {
  def apply[F[_]: Sync](pass: Array[Char]) =
    new SingleUsePassword[F](new PasswordProtection(pass))
}
