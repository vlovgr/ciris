/*
 * Copyright 2017-2020 Viktor Lövgren
 *
 * SPDX-License-Identifier: MIT
 */

package ciris

import cats.{Applicative, Apply, Eq, Eval, Show, Traverse}
import cats.implicits._
import ciris.ConfigEntry._

private[ciris] sealed abstract class ConfigEntry[+A] {
  def error: ConfigError

  final def map[B](f: A => B): ConfigEntry[B] =
    this match {
      case Default(error, a)     => Default(error, () => f(a()))
      case failed @ Failed(_)    => failed
      case Loaded(error, key, a) => Loaded(error, key, f(a))
    }

  final def mapError(f: ConfigError => ConfigError): ConfigEntry[A] =
    this match {
      case Default(error, a)     => Default(f(error), a)
      case Failed(error)         => Failed(f(error))
      case Loaded(error, key, a) => Loaded(f(error), key, a)
    }

  final def traverse[F[_], B](f: A => F[B])(implicit F: Applicative[F]): F[ConfigEntry[B]] =
    this match {
      case Default(error, a)     => f(a()).map(b => Default(error, () => b))
      case failed @ Failed(_)    => F.pure(failed)
      case Loaded(error, key, a) => f(a).map(Loaded(error, key, _))
    }
}

private[ciris] final object ConfigEntry {
  final def default[A](value: => A): ConfigEntry[A] =
    Default(ConfigError.Empty, () => value)

  final def failed[A](error: ConfigError): ConfigEntry[A] =
    Failed(error)

  final def loaded[A](key: Option[ConfigKey], value: A): ConfigEntry[A] =
    Loaded(ConfigError.Loaded, key, value)

  final case class Default[A](error: ConfigError, value: () => A) extends ConfigEntry[A] {
    override final def hashCode: Int =
      (error, value()).hashCode

    override final def equals(that: Any): Boolean =
      that match {
        case Default(thatError, thatValue) =>
          error == thatError && value() == thatValue()
        case _ =>
          false
      }

    override final def toString: String =
      s"Default($error, ${value()})"
  }

  final case class Failed(error: ConfigError) extends ConfigEntry[Nothing] {
    override final def toString: String =
      s"Failed($error)"
  }

  final case class Loaded[A](error: ConfigError, key: Option[ConfigKey], value: A)
      extends ConfigEntry[A] {

    override final def toString: String =
      s"Loaded($error, $key, $value)"
  }

  implicit final def configEntryEq[A](implicit eq: Eq[A]): Eq[ConfigEntry[A]] =
    Eq.instance {
      case (Default(e1, v1), Default(e2, v2))       => e1 === e2 && v1() === v2()
      case (Default(_, _), _)                       => false
      case (Failed(e1), Failed(e2))                 => e1 === e2
      case (Failed(_), _)                           => false
      case (Loaded(e1, k1, v1), Loaded(e2, k2, v2)) => e1 === e2 && k1 === k2 && v1 === v2
      case (Loaded(_, _, _), _)                     => false
    }

  implicit final def configEntryShow[A](implicit show: Show[A]): Show[ConfigEntry[A]] =
    Show.show {
      case Default(error, value)     => show"Default($error, ${value()})"
      case Failed(error)             => show"Failed($error)"
      case Loaded(error, key, value) => show"Loaded($error, $key, $value)"
    }

  implicit final val configEntryTraverseApply: Traverse[ConfigEntry] with Apply[ConfigEntry] =
    new Traverse[ConfigEntry] with Apply[ConfigEntry] {
      override final def foldLeft[A, B](entry: ConfigEntry[A], b: B)(f: (B, A) => B): B =
        entry match {
          case Default(_, a)   => f(b, a())
          case Failed(_)       => b
          case Loaded(_, _, a) => f(b, a)
        }

      override final def foldRight[A, B](entry: ConfigEntry[A], eb: Eval[B])(
        f: (A, Eval[B]) => Eval[B]
      ): Eval[B] =
        entry match {
          case Default(_, a)   => f(a(), eb)
          case Failed(_)       => eb
          case Loaded(_, _, a) => f(a, eb)
        }

      override final def map[A, B](entry: ConfigEntry[A])(f: A => B): ConfigEntry[B] =
        entry.map(f)

      override final def traverse[G[_]: Applicative, A, B](
        entry: ConfigEntry[A]
      )(f: A => G[B]): G[ConfigEntry[B]] =
        entry.traverse(f)

      override def ap[A, B](ff: ConfigEntry[A => B])(fa: ConfigEntry[A]): ConfigEntry[B] =
        ff match {
          case Default(_, ab) =>
            fa match {
              case Default(_, a)      => ConfigEntry.default(ab().apply(a()))
              case failed @ Failed(_) => failed
              case Loaded(_, _, a)    => ConfigEntry.loaded(None, ab().apply(a))
            }
          case failed @ Failed(e1) =>
            fa match {
              case Default(_, _)   => failed
              case Failed(e2)      => ConfigEntry.failed(e1.and(e2))
              case Loaded(_, _, _) => ConfigEntry.failed(ConfigError.Loaded.and(e1))
            }
          case Loaded(_, _, ab) =>
            fa match {
              case Default(_, a)   => ConfigEntry.loaded(None, ab(a()))
              case Failed(e2)      => ConfigEntry.failed(ConfigError.Loaded.and(e2))
              case Loaded(_, _, a) => ConfigEntry.loaded(None, ab(a))
            }
        }

    }
}
