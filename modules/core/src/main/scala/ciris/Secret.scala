/*
 * Copyright 2017-2019 Viktor Lövgren
 *
 * SPDX-License-Identifier: MIT
 */

package ciris

import cats.{Eq, Show}
import cats.implicits._
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import scala.annotation.tailrec

/**
  * Secret configuration value which might contain sensitive details.
  *
  * When a secret configuration value is shown, the value is replaced
  * by the first 7 characters of the SHA-1 hash for the value. This
  * short SHA-1 hash is available as [[Secret#valueShortHash]], and
  * the full SHA-1 hash is available as [[Secret#valueHash]]. The
  * underlying configuration value is available as [[Secret#value]].
  *
  * [[ConfigValue#secret]] can be used to wrap a value in [[Secret]],
  * while also redacting sentitive details from errors.
  *
  * @example {{{
  * scala> import cats.implicits._
  * import cats.implicits._
  *
  * scala> val secret = Secret(123)
  * secret: Secret[Int] = Secret(40bd001)
  *
  * scala> secret.valueShortHash
  * res0: String = 40bd001
  *
  * scala> secret.valueHash
  * res1: String = 40bd001563085fc35165329ea1ff5c5ecbdbbeef
  *
  * scala> secret.value
  * res2: Int = 123
  * }}}
  */
sealed abstract class Secret[+A] {

  /**
    * Returns the underlying configuration value.
    */
  def value: A

  /**
    * Returns the SHA-1 hash for the configuration value.
    *
    * Hashing is done in `UTF-8` and the hash is
    * returned in hexadecimal format.
    */
  def valueHash: String

  /**
    * Returns the first 7 characters of the SHA-1 hash
    * for the configuration value.
    *
    * @see [[Secret#valueHash]]
    */
  def valueShortHash: String
}

/**
  * @groupname Create Creating Instances
  * @groupprio Create 0
  *
  * @groupname Instances Type Class Instances
  * @groupprio Instances 1
  */
final object Secret {

  /**
    * Returns a new [[Secret]] for the specified configuration value.
    *
    * The [[Secret#valueHash]] will be calculated using the shown value.
    *
    * @example {{{
    * scala> import cats.implicits._
    * import cats.implicits._
    *
    * scala> Secret(12.5)
    * res0: Secret[Double] = Secret(90db4c0)
    * }}}
    *
    * @group Create
    */
  final def apply[A](value: A)(implicit show: Show[A]): Secret[A] = {
    val _value = value
    new Secret[A] {
      override final val value: A =
        _value

      override final def valueHash: String =
        sha1Hex(value.show)

      override final def valueShortHash: String =
        valueHash.take(7)

      override final def hashCode: Int =
        value.hashCode

      override final def equals(that: Any): Boolean =
        that match {
          case Secret(thatValue) => value == thatValue
          case _                 => false
        }

      override final def toString: String =
        s"Secret($valueShortHash)"
    }
  }

  /**
    * Returns the configuration value for the specified [[Secret]].
    *
    * This function enables pattern matching on [[Secret]]s.
    *
    * @example {{{
    * scala> import cats.implicits._
    * import cats.implicits._
    *
    * scala> val secret = Secret(12.5)
    * secret: Secret[Double] = Secret(90db4c0)
    *
    * scala> secret match { case Secret(value) => value }
    * res0: Double = 12.5
    * }}}
    *
    * @group Create
    */
  final def unapply[A](secret: Secret[A]): Some[A] =
    Some(secret.value)

  /**
    * @group Instances
    */
  implicit final def secretEq[A](implicit eq: Eq[A]): Eq[Secret[A]] =
    Eq.by(_.value)

  /**
    * @group Instances
    */
  implicit final def secretShow[A]: Show[Secret[A]] =
    Show.fromToString

  private[this] final val hexChars: Array[Char] =
    Array('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')

  private[this] final def hex(in: Array[Byte]): Array[Char] = {
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

  private[this] final def sha1(bytes: Array[Byte]): Array[Byte] =
    MessageDigest.getInstance("SHA-1").digest(bytes)

  private[this] final def sha1Hex(s: String): String =
    new String(hex(sha1(s.getBytes(StandardCharsets.UTF_8))))
}
