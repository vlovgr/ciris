package ciris

import scala.annotation.tailrec

/**
  * [[Secret]] is used to denote that a configuration value is secret, and
  * that it should not be included in any log output. By wrapping a value
  * with `Secret`, the value will not be printed, but a `Secret(hash)`
  * placeholder will take its place, where `hash` is the SHA1 short
  * hash of the value as a `String` in UTF-8. The short hash can
  * be retrieved with [[valueShortHash]] and the full hash with
  * [[valueHash]]. When decoding types wrapped with [[Secret]],
  * sensitive details will automatically be redacted from
  * error messages.<br>
  * <br>
  * To create a new `Secret`, use the apply method in the companion object.
  * {{{
  * scala> Secret(123)
  * res0: Secret[Int] = Secret(40bd001)
  * }}}
  *
  * The `equals`, `hashCode`, `copy`, and `unapply` methods are implemented.
  * {{{
  * scala> Secret(123) == Secret(123)
  * res1: Boolean = true
  *
  * scala> Secret(123).copy("abc")
  * res2: Secret[String] = Secret(a9993e3)
  *
  * scala> Secret(123) match { case Secret(value) => Secret(value + 1) }
  * res3: Secret[Int] = Secret(f38cfe2)
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

  /**
    * Generates the SHA1 hash of the value as a `String` in UTF-8 encoding.
    * The hash is returned in hex encoding and a short version, available
    * from [[valueShortHash]] is used in [[toString]].
    *
    * @return the SHA1 hex hash of `value.toString` in UTF-8
    */
  def valueHash: String =
    Secret.sha1Hex(value.toString)

  /**
    * Short version of [[valueHash]]. Only the first 7 characters are
    * included. This short version of the hash is used in [[toString]].
    *
    * @return the first 7 characters of [[valueHash]]
    */
  def valueShortHash: String =
    valueHash.take(7)

  /**
    * Generates a `String` representation which includes the short SHA1
    * hash of the value as a `String`. By logging this hash, we can see
    * whether it matches a pre-generated hash of the secret, and know
    * that we're using the expected secret -- without logging the
    * actual secret.
    *
    * @return a `String` representation containing [[valueShortHash]]
    * @see [[valueShortHash]]
    * @see [[valueHash]]
    */
  override def toString: String =
    s"Secret($valueShortHash)"

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

  private[this] val hexChars: Array[Char] =
    Array('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')

  private def sha1Hex(s: String): String = {
    def sha1(bytes: Array[Byte]): Array[Byte] = {
      val digest = new internal.digest.SHA1Digest()
      val hash = new Array[Byte](digest.digestSize)
      digest.update(bytes, 0, bytes.length)
      digest.doFinal(hash, 0)
      hash
    }

    def hex(in: Array[Byte]): Array[Char] = {
      val length = in.length

      @tailrec def encode(out: Array[Char], i: Int, j: Int): Array[Char] = {
        if (i < length) {
          out(j) = hexChars((0xf0 & in(i)) >>> 4)
          out(j + 1) = hexChars(0x0f & in(i))
          encode(out, i + 1, j + 2)
        } else out
      }

      encode(new Array(length << 1), 0, 0)
    }

    new String(hex(sha1(s.getBytes("UTF-8"))))
  }
}
