/*
 * Copyright 2017-2019 Viktor Lövgren
 *
 * SPDX-License-Identifier: MIT
 */

package ciris

import cats.{Apply, FlatMap, NonEmptyParallel, Show}
import cats.arrow.FunctionK
import cats.effect.{Async, Blocker, ContextShift, Effect, IO}
import cats.implicits._
import ciris.ConfigEntry.{Default, Failed, Loaded}

/**
  * Represents a configuration value or a composition of multiple values.
  *
  * If a configuration value is missing, we can use [[ConfigValue#or]] to
  * try and load the value from elsewhere. [[ConfigValue#default]] can be
  * used to set a default value if all other values are missing. If the
  * value is optional, [[ConfigValue#option]] can be used to default
  * to `None` if all values are missing.
  *
  * Values can be converted to a different type using [[ConfigValue#as]].
  * If the value might contain sensitive details, [[ConfigValue#secret]]
  * can be used to redact sensitive details from error messages while
  * also wrapping the value in [[Secret]], preventing the value from
  * being shown.
  *
  * Sometimes, we first need to load a configuration value to determine
  * how to continue loading the remaining values. In such cases, it's
  * suitable to use [[ConfigValue#flatMap]]. When loading values in
  * sequence using `flatMap`, errors are not accumulated, and so
  * only the first error will be available.
  *
  * Parallel composition lets us achieve error accumulation. Functions
  * like `parMapN` and `parTupled` on tuples of [[ConfigValue]]s loads
  * several values while also accumulating errors. It is often helpful
  * to see all errors when loading configurations, so prefer options
  * which accumulate errors.
  *
  * Configuration values can be loaded using [[ConfigValue#load]], which
  * loads the value using a specified effect type. If there are errors
  * and we want to deal with them explicitly, [[ConfigValue#attempt]]
  * can be used.
  *
  * @example {{{
  * scala> import cats.implicits._
  * import cats.implicits._
  *
  * scala> case class Config(maxRetries: Int, apiKey: Option[Secret[String]])
  * defined class Config
  *
  * scala> val maxRetries = env("MAX_RETRIES").or(prop("max.retries")).as[Int].default(5)
  * maxRetries: ConfigValue[Int] = ciris.ConfigValue$$$$anon$$3@5821f472
  *
  * scala> val apiKey = env("API_KEY").or(prop("api.key")).secret.option
  * apiKey: ConfigValue[Option[Secret[String]]] = ciris.ConfigValue$$$$anon$$3@186ab4b8
  *
  * scala> val config = (maxRetries, apiKey).parMapN(Config)
  * config: ciris.ConfigValue[Config] = ciris.ConfigValue$$$$anon$$3@565e528c
  * }}}
  */
sealed abstract class ConfigValue[A] {
  private[this] final val self: ConfigValue[A] = this

  /**
    * Returns a new [[ConfigValue]] which attempts to decode the
    * value to the specified type.
    */
  final def as[B](implicit decoder: ConfigDecoder[A, B]): ConfigValue[B] =
    transform {
      case Default(error, a) =>
        decoder.decode(None, a()) match {
          case Right(b)          => Default(error, () => b)
          case Left(decodeError) => Failed(error.or(decodeError))
        }

      case failed @ Failed(_) =>
        failed

      case Loaded(error, key, a) =>
        decoder.decode(key, a) match {
          case Right(b)          => Loaded(error, key, b)
          case Left(decodeError) => Failed(error.or(decodeError))
        }
    }

  /**
    * Returns an effect of the specified type which attempts
    * to load the configuration value.
    */
  final def attempt[F[_]](
    implicit F: Async[F],
    context: ContextShift[F]
  ): F[Either[ConfigError, A]] =
    to[F].map {
      case Default(_, a)   => Right(a())
      case Failed(error)   => Left(error)
      case Loaded(_, _, a) => Right(a)
    }

  /**
    * Returns a new [[ConfigValue]] which uses the specified default
    * if the value is missing.
    *
    * If a previous default value has been specified, a later default
    * will override the earlier. If a default value is specified for
    * a composition of values, the default will be used in case all
    * values are either missing or are default values themselves.
    *
    * Using `.default(a)` is equivalent to using `.or(default(a))`.
    */
  final def default(value: => A): ConfigValue[A] =
    transform {
      case Default(error, _)                => Default(error, () => value)
      case Failed(error) if error.isMissing => Default(error, () => value)
      case failed @ Failed(_)               => failed
      case loaded @ Loaded(_, _, _)         => loaded
    }

  /**
    * Returns a new [[ConfigValue]] which applies the
    * specified effectful function on the value.
    */
  final def evalMap[F[_], B](f: A => F[B])(implicit F: Effect[F]): ConfigValue[B] =
    new ConfigValue[B] {
      override final def to[G[_]](
        implicit G: Async[G],
        context: ContextShift[G]
      ): G[ConfigEntry[B]] =
        self.to[G].flatMap { entry =>
          G.async[ConfigEntry[B]] { cb =>
            F.runAsync(entry.traverse(f))(e => IO(cb(e))).unsafeRunSync
          }
        }
    }

  /**
    * Returns a new [[ConfigValue]] which loads the specified
    * configuration using the value.
    */
  final def flatMap[B](f: A => ConfigValue[B]): ConfigValue[B] =
    new ConfigValue[B] {
      override final def to[F[_]](
        implicit F: Async[F],
        context: ContextShift[F]
      ): F[ConfigEntry[B]] =
        self.to[F].flatMap {
          case Default(_, a)      => f(a()).to[F]
          case failed @ Failed(_) => F.pure(failed)
          case Loaded(_, _, a)    => f(a).to[F]
        }
    }

  /**
    * Returns an effect of the specified type which loads
    * the configuration value.
    */
  final def load[F[_]](
    implicit F: Async[F],
    context: ContextShift[F]
  ): F[A] =
    to[F].flatMap {
      case Default(_, a)   => F.pure(a())
      case Failed(error)   => F.raiseError(error.throwable)
      case Loaded(_, _, a) => F.pure(a)
    }

  /**
    * Returns a new [[ConfigValue]] which applies the
    * specified function on the value.
    */
  final def map[B](f: A => B): ConfigValue[B] =
    transform(_.map(f))

  /**
    * Returns a new [[ConfigValue]] which uses `None` as the
    * default if the value is missing.
    *
    * Using `.option` is equivalent to using `.map(_.some).default(None)`.
    */
  final def option: ConfigValue[Option[A]] =
    transform {
      case Default(error, _)                => Default(error, () => None)
      case Failed(error) if error.isMissing => Default(error, () => None)
      case failed @ Failed(_)               => failed
      case Loaded(error, key, a)            => Loaded(error, key, Some(a))
    }

  /**
    * Returns a new [[ConfigValue]] which uses the specified
    * configuration if the value is missing.
    *
    * If the value is a default value, an attempt is made to
    * use the specified configuration, and if that is also
    * missing, the default value remains. Defaults in the
    * specified configuration will override any previous
    * defaults. Errors from both the value and the
    * specified configuration are accumulated.
    */
  final def or(value: => ConfigValue[A]): ConfigValue[A] =
    new ConfigValue[A] {
      override final def to[F[_]](
        implicit F: Async[F],
        context: ContextShift[F]
      ): F[ConfigEntry[A]] =
        self.to[F].flatMap {
          case Default(error, a) =>
            value.to[F].map {
              case Failed(nextError) if nextError.isMissing => Default(error.or(nextError), a)
              case Failed(nextError)                        => Failed(error.or(nextError))
              case Default(nextError, b)                    => Default(error.or(nextError), b)
              case Loaded(nextError, key, b)                => Loaded(error.or(nextError), key, b)
            }

          case Failed(error) if error.isMissing =>
            value.to[F].map {
              case Failed(nextError)         => Failed(error.or(nextError))
              case Default(nextError, b)     => Default(error.or(nextError), b)
              case Loaded(nextError, key, b) => Loaded(error.or(nextError), key, b)
            }

          case failed @ Failed(_) =>
            F.pure(failed)

          case loaded @ Loaded(_, _, _) =>
            F.pure(loaded)
        }
    }

  /**
    * Returns a new [[ConfigValue]] which treats the value
    * like it might contain sensitive details.
    *
    * Sensitive details are redacted from error messages.
    * The value is wrapped in [[Secret]], which prevents
    * the value from being shown.
    */
  final def secret(implicit show: Show[A]): ConfigValue[Secret[A]] =
    transform {
      case Default(error, a)     => Default(error.redacted, () => Secret(a()))
      case Failed(error)         => Failed(error.redacted)
      case Loaded(error, key, a) => Loaded(error.redacted, key, Secret(a))
    }

  private[ciris] def to[F[_]](
    implicit F: Async[F],
    context: ContextShift[F]
  ): F[ConfigEntry[A]]

  private[ciris] final def transform[B](
    f: ConfigEntry[A] => ConfigEntry[B]
  ): ConfigValue[B] =
    new ConfigValue[B] {
      override final def to[F[_]](
        implicit F: Async[F],
        context: ContextShift[F]
      ): F[ConfigEntry[B]] =
        self.to[F].map(f)
    }
}

/**
  * @groupname Create Creating Instances
  * @groupprio Create 0
  *
  * @groupname Instances Type Class Instances
  * @groupprio Instances 1
  *
  * @groupname Newtypes Newtypes
  * @groupprio Newtypes 2
  */
final object ConfigValue {

  /**
    * Returns a new [[ConfigValue]] which loads a configuration
    * value using a callback.
    *
    * @group Create
    */
  final def async[A](k: (Either[Throwable, ConfigValue[A]] => Unit) => Unit): ConfigValue[A] =
    new ConfigValue[A] {
      override final def to[F[_]](
        implicit F: Async[F],
        context: ContextShift[F]
      ): F[ConfigEntry[A]] =
        F.async(k).flatMap(_.to[F])
    }

  /**
    * Returns a new [[ConfigValue]] which loads the specified
    * blocking value.
    *
    * @group Create
    */
  final def blockOn[A](blocker: Blocker)(value: ConfigValue[A]): ConfigValue[A] =
    new ConfigValue[A] {
      override final def to[F[_]](
        implicit F: Async[F],
        context: ContextShift[F]
      ): F[ConfigEntry[A]] =
        context.blockOn(blocker)(value.to[F])
    }

  /**
    * Returns a new [[ConfigValue]] with the specified
    * default value.
    *
    * @group Create
    */
  final def default[A](value: => A): ConfigValue[A] =
    ConfigValue.pure(ConfigEntry.default(value))

  /**
    * Returns a new [[ConfigValue]] which evaluates the
    * effect for the specified value.
    *
    * @group Create
    */
  final def eval[F[_], A](value: F[ConfigValue[A]])(implicit F: Effect[F]): ConfigValue[A] =
    new ConfigValue[A] {
      override final def to[G[_]](
        implicit G: Async[G],
        context: ContextShift[G]
      ): G[ConfigEntry[A]] =
        G.async[ConfigValue[A]] { cb =>
            F.runAsync(value)(e => IO(cb(e))).unsafeRunSync
          }
          .flatMap(_.to[G])
    }

  /**
    * Returns a new [[ConfigValue]] which failed with
    * the specified error.
    *
    * @group Create
    */
  final def failed[A](error: ConfigError): ConfigValue[A] =
    ConfigValue.pure(ConfigEntry.failed(error))

  /**
    * Returns a new [[ConfigValue]] which loaded
    * successfully with the specified value.
    *
    * @group Create
    */
  final def loaded[A](key: ConfigKey, value: A): ConfigValue[A] =
    ConfigValue.pure(ConfigEntry.loaded(Some(key), value))

  /**
    * Returns a new [[ConfigValue]] which failed because
    * the value was missing.
    *
    * @group Create
    */
  final def missing[A](key: ConfigKey): ConfigValue[A] =
    ConfigValue.failed(ConfigError.Missing(key))

  private[ciris] final def pure[A](entry: ConfigEntry[A]): ConfigValue[A] =
    new ConfigValue[A] {
      override final def to[F[_]](
        implicit F: Async[F],
        context: ContextShift[F]
      ): F[ConfigEntry[A]] =
        F.pure(entry)
    }

  /**
    * Returns a new [[ConfigValue]] which suspends loading
    * of the specified value.
    *
    * @group Create
    */
  final def suspend[A](value: => ConfigValue[A]): ConfigValue[A] =
    new ConfigValue[A] {
      override final def to[F[_]](
        implicit F: Async[F],
        context: ContextShift[F]
      ): F[ConfigEntry[A]] =
        F.suspend(value.to[F])
    }

  /**
    * @group Instances
    */
  implicit final val configValueFlatMap: FlatMap[ConfigValue] =
    new FlatMap[ConfigValue] {
      override final def flatMap[A, B](
        value: ConfigValue[A]
      )(f: A => ConfigValue[B]): ConfigValue[B] =
        value.flatMap(f)

      override final def map[A, B](
        value: ConfigValue[A]
      )(f: A => B): ConfigValue[B] =
        value.map(f)

      override final def tailRecM[A, B](a: A)(f: A => ConfigValue[Either[A, B]]): ConfigValue[B] =
        new ConfigValue[B] {
          override final def to[F[_]](
            implicit F: Async[F],
            context: ContextShift[F]
          ): F[ConfigEntry[B]] =
            F.tailRecM(a) { a =>
              f(a).to[F].map {
                case Default(error, either) =>
                  either() match {
                    case left @ Left(_) => left.asInstanceOf[Either[A, ConfigEntry[B]]]
                    case Right(b)       => Right(Default(error, () => b))
                  }
                case failed @ Failed(_)           => Right(failed)
                case Loaded(_, _, left @ Left(_)) => left.asInstanceOf[Either[A, ConfigEntry[B]]]
                case Loaded(error, key, Right(b)) => Right(Loaded(error, key, b))
              }
            }
        }
    }

  /**
    * @group Instances
    */
  implicit final val configValueParApply: Apply[Par] =
    new Apply[Par] {
      override final def ap[A, B](pab: Par[A => B])(pa: Par[A]): Par[B] =
        Par {
          new ConfigValue[B] {
            override final def to[F[_]](
              implicit F: Async[F],
              context: ContextShift[F]
            ): F[ConfigEntry[B]] =
              pab.unwrap.to[F].flatMap {
                case Default(_, ab) =>
                  pa.unwrap.to[F].map {
                    case Default(_, a)      => ConfigEntry.default(ab().apply(a()))
                    case failed @ Failed(_) => failed
                    case Loaded(_, _, a)    => ConfigEntry.loaded(None, ab().apply(a))
                  }

                case failed @ Failed(e1) =>
                  pa.unwrap.to[F].map {
                    case Default(_, _)   => failed
                    case Failed(e2)      => ConfigEntry.failed(e1.and(e2))
                    case Loaded(_, _, _) => ConfigEntry.failed(ConfigError.Loaded.and(e1))
                  }

                case Loaded(_, _, ab) =>
                  pa.unwrap.to[F].map {
                    case Default(_, a)   => ConfigEntry.loaded(None, ab(a()))
                    case Failed(e2)      => ConfigEntry.failed(ConfigError.Loaded.and(e2))
                    case Loaded(_, _, a) => ConfigEntry.loaded(None, ab(a))
                  }
              }
          }
        }

      override final def map[A, B](pa: Par[A])(f: A => B): Par[B] =
        Par(pa.unwrap.map(f))
    }

  /**
    * @group Instances
    */
  implicit final val configValueNonEmptyParallel: NonEmptyParallel.Aux[ConfigValue, Par] =
    new NonEmptyParallel[ConfigValue] {
      override final type F[A] = Par[A]

      override final val apply: Apply[Par] =
        configValueParApply

      override final val flatMap: FlatMap[ConfigValue] =
        configValueFlatMap

      override final val parallel: FunctionK[ConfigValue, Par] =
        new FunctionK[ConfigValue, Par] {
          override final def apply[A](value: ConfigValue[A]): Par[A] =
            Par(value)
        }

      override final val sequential: FunctionK[Par, ConfigValue] =
        new FunctionK[Par, ConfigValue] {
          override final def apply[A](par: Par[A]): ConfigValue[A] =
            par.unwrap
        }
    }

  /**
    * Newtype for parallel composition of configuration values.
    *
    * @group Newtypes
    */
  final type Par[A] = Par.Type[A]

  /**
    * @group Newtypes
    */
  final object Par {
    type Base

    sealed trait Tag extends Any

    type Type[A] <: Base with Tag

    /**
      * Returns a [[Par]] instance for the specified [[ConfigValue]].
      */
    final def apply[A](value: ConfigValue[A]): Par[A] =
      value.asInstanceOf[Par[A]]

    implicit final class Ops[A] private[ciris] (
      private val par: Par[A]
    ) extends AnyVal {

      /**
        * Returns the underlying [[ConfigValue]] for the instance.
        */
      final def unwrap: ConfigValue[A] =
        par.asInstanceOf[ConfigValue[A]]
    }
  }
}
