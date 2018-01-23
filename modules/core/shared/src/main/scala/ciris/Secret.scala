package ciris

/**
  * [[Secret]] is used to denote that a configuration value is secret, and
  * that it should not be included in any log output. By wrapping a value
  * with `Secret`, the value will not be printed, but a `Secret(***)`
  * placeholder will take its place.<br>
  * <br>
  * To create a new `Secret`, use the apply method in the companion object.
  * {{{
  * scala> Secret(123)
  * res0: Secret[Int] = Secret(***)
  * }}}
  *
  * The `equals`, `hashCode`, `copy`, and `unapply` methods are implemented.
  * {{{
  * scala> Secret(123) == Secret(123)
  * res1: Boolean = true
  *
  * scala> Secret(123).copy("abc")
  * res2: Secret[String] = Secret(***)
  *
  * scala> Secret(123) match { case Secret(value) => Secret(value + 1) }
  * res3: Secret[Int] = Secret(***)
  * }}}
  *
  * The original value can be retrieved; be careful to not include it in logs.
  * {{{
  * scala> Secret(123).value
  * res4: Int = 123
  * }}}
  *
  * @param value the original value which the `Secret` is wrapping
  * @tparam A the type of the original value being wrapped
  */
final class Secret[A] private (val value: A) {
  override def toString: String =
    "Secret(***)"

  override def equals(that: Any): Boolean =
    that match {
      case Secret(a) => value == a
      case _         => false
    }

  override def hashCode: Int =
    value.hashCode

  def copy[B](value: B = value): Secret[B] =
    new Secret(value = value)
}

object Secret {
  def apply[A](value: A): Secret[A] =
    new Secret(value)

  def unapply[A](secret: Secret[A]): Option[A] =
    Some(secret.value)
}
