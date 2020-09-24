package ciris

import java.security.KeyStore.PasswordProtection

import cats.effect.{Bracket, Resource, Sync}

/**
 * A single use password, after use it will be destroyed from the memory.
 *
 */
class SingleUsePassword[F[_]] private (private val password: Resource[F, PasswordProtection]) {

  def useAndDestroy[G[x] >: F[x], B](f: Array[Char] => G[B])(implicit F: Bracket[G[*], Throwable]): G[B] =
    password.use { p =>
      f(p.getPassword)
    }

}

object SingleUsePassword {
  def apply[F[_]: Sync](pass: Array[Char]) = new SingleUsePassword[F](
    Resource.fromDestroyable(
      Sync[F].delay(new PasswordProtection(pass))
    )
  )
}