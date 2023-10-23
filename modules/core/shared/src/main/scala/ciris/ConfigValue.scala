/*
 * Copyright 2017-2023 Viktor Rudebeck
 *
 * SPDX-License-Identifier: MIT
 */

package ciris

import cats.{Apply, FlatMap, NonEmptyParallel, Show}
import cats.arrow.FunctionK
import cats.effect.kernel.{Async, Resource}
import cats.syntax.all._
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
  * loads the value using a specified effect type. If a [[ConfigValue]]
  * contains `Resource`s for loading the configuration, there is also
  * the option to return a `Resource` with [[ConfigValue#resource]].
  *
  * @example {{{
  * scala> import cats.syntax.all._
  * import cats.syntax.all._
  *
  * scala> case class Config(maxRetries: Int, apiKey: Option[Secret[String]])
  * class Config
  *
  * scala> val maxRetries = env("MAX_RETRIES").or(prop("max.retries")).as[Int].default(5)
  * val maxRetries: ConfigValue[[x]Effect[x],Int] = ConfigValue$$88354410
  *
  * scala> val apiKey = env("API_KEY").or(prop("api.key")).secret.option
  * val apiKey: ConfigValue[[x]Effect[x],Option[Secret[String]]] = ConfigValue$$2109306667
  *
  * scala> val config = (maxRetries, apiKey).parMapN(Config(_, _))
  * val config: ConfigValue[[x]Effect[x],Config] = ConfigValue$$1463229407
  * }}}
  */
sealed abstract class ConfigValue[+F[_], A] {
  private[this] final val self: ConfigValue[F, A] = this

  /**
    * Returns a new [[ConfigValue]] which attempts to decode the
    * value to the specified type.
    */
  final def as[B](implicit decoder: ConfigDecoder[A, B]): ConfigValue[F, B] =
    transform {
      case Default(error, a) =>
        decoder.decode(None, a) match {
          case Right(b)          => Default(error, b)
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
    *
    * Note that if the [[ConfigValue]] contains any resources,
    * from using [[ConfigValue.resource]], these will be used
    * (acquired and released) as part of the returned effect.
    * If this behaviour is not desired, we can instead use
    * [[ConfigValue#resource]] to return a `Resource`.
    */
  final def attempt[G[x] >: F[x]](implicit G: Async[G]): G[Either[ConfigError, A]] =
    to[G].use { result =>
      G.pure(result match {
        case Default(_, a)   => Right(a)
        case Failed(error)   => Left(error)
        case Loaded(_, _, a) => Right(a)
      })
    }

  /**
    * Returns the same [[ConfigValue]] but lifted to the
    * specified effect type.
    */
  final def covary[G[x] >: F[x]]: ConfigValue[G, A] =
    self

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
  final def default(value: => A): ConfigValue[F, A] =
    transform {
      case Default(error, _)                => Default(error, value)
      case Failed(error) if error.isMissing => Default(error, value)
      case failed @ Failed(_)               => failed
      case loaded @ Loaded(_, _, _)         => loaded
    }

  /**
    * Alias for `evalMap(f).flatten`.
    */
  final def evalFlatMap[G[x] >: F[x], B](f: A => G[ConfigValue[G, B]]): ConfigValue[G, B] =
    evalMap(f).flatten

  /**
    * Returns a new [[ConfigValue]] which applies the
    * specified effectful function on the value.
    */
  final def evalMap[G[x] >: F[x], B](f: A => G[B]): ConfigValue[G, B] =
    new ConfigValue[G, B] {
      override final def to[H[x] >: G[x]](implicit H: Async[H]): Resource[H, ConfigEntry[B]] =
        self.to[H].evalMap(_.traverse[H, B](f))

      override final def toString: String =
        "ConfigValue$" + System.identityHashCode(this)
    }

  /**
    * Returns a new [[ConfigValue]] which loads the specified
    * configuration using the value.
    */
  final def flatMap[G[x] >: F[x], B](f: A => ConfigValue[G, B]): ConfigValue[G, B] =
    new ConfigValue[G, B] {
      override final def to[H[x] >: G[x]](implicit H: Async[H]): Resource[H, ConfigEntry[B]] =
        self.to[H].flatMap {
          case Default(_, a) =>
            f(a).to[H].map {
              case Default(_, b)      => ConfigEntry.default(b)
              case failed @ Failed(_) => failed
              case Loaded(_, _, b)    => ConfigEntry.loaded(None, b)
            }

          case failed @ Failed(_) =>
            Resource.eval(H.pure(failed))

          case Loaded(_, _, a) =>
            f(a).to[H].map {
              case Default(_, b)   => ConfigEntry.loaded(None, b)
              case Failed(e2)      => ConfigEntry.failed(ConfigError.Loaded.and(e2))
              case Loaded(_, _, b) => ConfigEntry.loaded(None, b)
            }
        }

      override final def toString: String =
        "ConfigValue$" + System.identityHashCode(this)
    }

  /**
    * Returns an effect of the specified type which loads
    * the configuration value.
    *
    * Note that if the [[ConfigValue]] contains any resources,
    * from using [[ConfigValue.resource]], these will be used
    * (acquired and released) as part of the returned effect.
    * If this behaviour is not desired, we can instead use
    * [[ConfigValue#resource]] to return a `Resource`.
    */
  final def load[G[x] >: F[x]](implicit G: Async[G]): G[A] =
    to[G].use {
      case Default(_, a)   => G.pure(a)
      case Failed(error)   => G.raiseError(error.throwable)
      case Loaded(_, _, a) => G.pure(a)
    }

  /**
    * Returns a new [[ConfigValue]] which applies the
    * specified function on the value.
    */
  final def map[B](f: A => B): ConfigValue[F, B] =
    transform(_.map(f))

  /**
    * Returns a new [[ConfigValue]] which uses `None` as the
    * default if the value is missing.
    *
    * Using `.option` is equivalent to using `.map(_.some).default(None)`.
    */
  final def option: ConfigValue[F, Option[A]] =
    transform {
      case Default(error, _)                => Default(error, None)
      case Failed(error) if error.isMissing => Default(error, None)
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
  final def or[G[x] >: F[x]](value: => ConfigValue[G, A]): ConfigValue[G, A] =
    new ConfigValue[G, A] {
      override final def to[H[x] >: G[x]](implicit H: Async[H]): Resource[H, ConfigEntry[A]] =
        self.to[H].flatMap {
          case Default(error, a) =>
            value.to[H].map {
              case Failed(nextError) if nextError.isMissing => Default(error.or(nextError), a)
              case Failed(nextError)                        => Failed(error.or(nextError))
              case Default(nextError, b)                    => Default(error.or(nextError), b)
              case Loaded(nextError, key, b)                => Loaded(error.or(nextError), key, b)
            }

          case Failed(error) if error.isMissing =>
            value.to[H].map {
              case Failed(nextError)         => Failed(error.or(nextError))
              case Default(nextError, b)     => Default(error.or(nextError), b)
              case Loaded(nextError, key, b) => Loaded(error.or(nextError), key, b)
            }

          case failed @ Failed(_) =>
            Resource.eval(H.pure(failed))

          case loaded @ Loaded(_, _, _) =>
            Resource.eval(H.pure(loaded))
        }

      override final def toString: String =
        "ConfigValue$" + System.identityHashCode(this)
    }

  /**
    * Returns a new [[ConfigValue]] with sensitive
    * details redacted from error messages.
    *
    * Using `.redacted` is equivalent to using
    * `.secret.map(_.value)`, except without
    * requiring a `Show` instance.
    */
  final def redacted: ConfigValue[F, A] =
    transform(_.mapError(_.redacted))

  /**
    * Returns a `Resource` with the specified effect
    * type which loads the configuration value.
    */
  final def resource[G[x] >: F[x]](implicit G: Async[G]): Resource[G, A] =
    to[G].evalMap[A] {
      case Default(_, a)   => G.pure(a)
      case Failed(error)   => G.raiseError(error.throwable)
      case Loaded(_, _, a) => G.pure(a)
    }

  /**
    * Returns a new [[ConfigValue]] which treats the value
    * like it might contain sensitive details.
    *
    * Sensitive details are redacted from error messages.
    * The value is wrapped in [[Secret]], which prevents
    * the value from being shown.
    *
    * Using `.secret` is equivalent to using
    * `.redacted.map(Secret(_))`.
    */
  final def secret(implicit show: Show[A]): ConfigValue[F, Secret[A]] =
    transform {
      case Default(error, a)     => Default(error.redacted, Secret(a)(show))
      case Failed(error)         => Failed(error.redacted)
      case Loaded(error, key, a) => Loaded(error.redacted, key, Secret(a)(show))
    }

  private[ciris] def to[G[x] >: F[x]](implicit G: Async[G]): Resource[G, ConfigEntry[A]]

  private[ciris] final def transform[B](
    f: ConfigEntry[A] => ConfigEntry[B]
  ): ConfigValue[F, B] =
    new ConfigValue[F, B] {
      override final def to[G[x] >: F[x]](implicit G: Async[G]): Resource[G, ConfigEntry[B]] =
        self.to[G].map(f)

      override final def toString: String =
        "ConfigValue$" + System.identityHashCode(this)
    }

  /**
    * Returns a new [[ConfigValue]] which treats the value
    * as a secret which can only be used once.
    *
    * Sensitive details are redacted from error messages. The
    * value is wrapped in [[UseOnceSecret]] which prevents
    * multiple accesses to the secret and which nullifies
    * the secret once it has been used.
    */
  final def useOnceSecret(implicit ev: A <:< Array[Char]): ConfigValue[F, UseOnceSecret] =
    new ConfigValue[F, UseOnceSecret] {
      override final def to[G[x] >: F[x]](
        implicit G: Async[G]
      ): Resource[G, ConfigEntry[UseOnceSecret]] =
        self.redacted.to[G].evalMap(_.traverse(UseOnceSecret[G](_)))

      override final def toString: String =
        "ConfigValue$" + System.identityHashCode(this)
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
object ConfigValue {

  /**
    * Returns a new [[ConfigValue]] which loads a configuration
    * value using a callback.
    *
    * @group Create
    */
  final def async[F[_], A](
    k: (Either[Throwable, ConfigValue[F, A]] => Unit) => F[Option[F[Unit]]]
  ): ConfigValue[F, A] =
    new ConfigValue[F, A] {
      override final def to[G[x] >: F[x]](implicit G: Async[G]): Resource[G, ConfigEntry[A]] =
        Resource
          .eval(G.async[ConfigValue[F, A]](k(_).asInstanceOf[G[Option[G[Unit]]]]))
          .flatMap(_.to[G])

      override final def toString: String =
        "ConfigValue$" + System.identityHashCode(this)
    }

  /**
    * Returns a new [[ConfigValue]] which loads a configuration
    * value using a callback.
    *
    * @group Create
    */
  final def async_[F[_], A](
    k: (Either[Throwable, ConfigValue[F, A]] => Unit) => Unit
  ): ConfigValue[F, A] =
    new ConfigValue[F, A] {
      override final def to[G[x] >: F[x]](implicit G: Async[G]): Resource[G, ConfigEntry[A]] =
        Resource.eval(G.async_(k)).flatMap(_.to[G])

      override final def toString: String =
        "ConfigValue$" + System.identityHashCode(this)
    }

  /**
    * Returns a new [[ConfigValue]] which loads the specified
    * blocking value.
    *
    * @group Create
    */
  final def blocking[F[_], A](value: => ConfigValue[F, A]): ConfigValue[F, A] =
    new ConfigValue[F, A] {
      override final def to[G[x] >: F[x]](implicit G: Async[G]): Resource[G, ConfigEntry[A]] =
        Resource.eval(G.blocking(value)).flatMap(_.to[G])

      override final def toString: String =
        "ConfigValue$" + System.identityHashCode(this)
    }

  /**
    * Returns a new [[ConfigValue]] with the specified
    * default value.
    *
    * @group Create
    */
  final def default[A](value: A): ConfigValue[Effect, A] =
    ConfigValue.pure(ConfigEntry.default(value))

  /**
    * Returns a new [[ConfigValue]] which evaluates the
    * effect for the specified value.
    *
    * @group Create
    */
  final def eval[F[_], A](value: F[ConfigValue[F, A]]): ConfigValue[F, A] =
    new ConfigValue[F, A] {
      override final def to[G[x] >: F[x]](implicit G: Async[G]): Resource[G, ConfigEntry[A]] =
        Resource.eval(value).asInstanceOf[Resource[G, ConfigValue[F, A]]].flatMap(_.to[G])

      override final def toString: String =
        "ConfigValue$" + System.identityHashCode(this)
    }

  /**
    * Returns a new [[ConfigValue]] which failed with
    * the specified error.
    *
    * @group Create
    */
  final def failed[A](error: ConfigError): ConfigValue[Effect, A] =
    ConfigValue.pure(ConfigEntry.failed(error))

  /**
    * Returns a new [[ConfigValue]] which loaded
    * successfully with the specified value.
    *
    * @group Create
    */
  final def loaded[A](key: ConfigKey, value: A): ConfigValue[Effect, A] =
    ConfigValue.pure(ConfigEntry.loaded(Some(key), value))

  /**
    * Returns a new [[ConfigValue]] which failed because
    * the value was missing.
    *
    * @group Create
    */
  final def missing[A](key: ConfigKey): ConfigValue[Effect, A] =
    ConfigValue.failed(ConfigError.Missing(key))

  /**
    * Alias for `ConfigValue.missing(ConfigKey(description))`.
    */
  final def missing[A](description: => String): ConfigValue[Effect, A] =
    ConfigValue.missing(ConfigKey(description))

  private[ciris] final def pure[F[_], A](entry: ConfigEntry[A]): ConfigValue[F, A] =
    new ConfigValue[F, A] {
      override final def to[G[x] >: F[x]](implicit G: Async[G]): Resource[G, ConfigEntry[A]] =
        Resource.pure(entry)

      override final def toString: String =
        "ConfigValue$" + System.identityHashCode(this)
    }

  /**
    * Returns a new [[ConfigValue]] for the specified resource.
    *
    * @group Create
    */
  final def resource[F[_], A](resource: Resource[F, ConfigValue[F, A]]): ConfigValue[F, A] = {
    val _resource = resource
    new ConfigValue[F, A] {
      override final def to[G[x] >: F[x]](implicit G: Async[G]): Resource[G, ConfigEntry[A]] =
        _resource.asInstanceOf[Resource[G, ConfigValue[F, A]]].flatMap(_.to[G])

      override final def toString: String =
        "ConfigValue$" + System.identityHashCode(this)
    }
  }

  /**
    * Returns a new [[ConfigValue]] which suspends loading
    * of the specified value.
    *
    * @group Create
    */
  final def suspend[F[_], A](value: => ConfigValue[F, A]): ConfigValue[F, A] =
    new ConfigValue[F, A] {
      override final def to[G[x] >: F[x]](implicit G: Async[G]): Resource[G, ConfigEntry[A]] =
        Resource.suspend(G.delay(value.to[G]))

      override final def toString: String =
        "ConfigValue$" + System.identityHashCode(this)
    }

  /**
    * @group Instances
    */
  implicit final def configValueFlatMap[F[_]]: FlatMap[ConfigValue[F, *]] =
    new FlatMap[ConfigValue[F, *]] {
      override final def flatMap[A, B](
        value: ConfigValue[F, A]
      )(f: A => ConfigValue[F, B]): ConfigValue[F, B] =
        value.flatMap(f)

      override final def map[A, B](
        value: ConfigValue[F, A]
      )(f: A => B): ConfigValue[F, B] =
        value.map(f)

      /**
        * Note: this is intentionally not stack safe, as the `flatMap`
        * on `ConfigValue` cannot be expressed in a tail-recursive way.
        */
      override final def tailRecM[A, B](
        a: A
      )(f: A => ConfigValue[F, Either[A, B]]): ConfigValue[F, B] =
        f(a).flatMap {
          case Left(a)  => tailRecM(a)(f)
          case Right(b) => default(b)
        }
    }

  /**
    * @group Instances
    */
  implicit final def configValueParApply[F[_]]: Apply[Par[F, *]] =
    new Apply[Par[F, *]] {
      override final def ap[A, B](pab: Par[F, A => B])(pa: Par[F, A]): Par[F, B] =
        Par {
          new ConfigValue[F, B] {
            override final def to[G[x] >: F[x]](
              implicit G: Async[G]
            ): Resource[G, ConfigEntry[B]] =
              (pab.unwrap.to[G], pa.unwrap.to[G]).parMapN {
                case (Default(_, ab), Default(_, a)) =>
                  ConfigEntry.default(ab.apply(a))
                case (Default(_, _), failed @ Failed(_)) =>
                  failed
                case (Default(_, ab), Loaded(_, _, a)) =>
                  ConfigEntry.loaded(None, ab.apply(a))

                case (failed @ Failed(_), Default(_, _)) =>
                  failed
                case (Failed(e1), Failed(e2)) =>
                  ConfigEntry.failed(e1.and(e2))
                case (Failed(e1), Loaded(_, _, _)) =>
                  ConfigEntry.failed(ConfigError.Loaded.and(e1))

                case (Loaded(_, _, ab), Default(_, a)) =>
                  ConfigEntry.loaded(None, ab(a))
                case (Loaded(_, _, _), Failed(e2)) =>
                  ConfigEntry.failed(ConfigError.Loaded.and(e2))
                case (Loaded(_, _, ab), Loaded(_, _, a)) =>
                  ConfigEntry.loaded(None, ab(a))
              }

            override final def toString: String =
              "ConfigValue$" + System.identityHashCode(this)
          }
        }

      override final def map[A, B](pa: Par[F, A])(f: A => B): Par[F, B] =
        Par(pa.unwrap.map(f))
    }

  /**
    * @group Instances
    */
  implicit final def configValueNonEmptyParallel[G[_]]
    : NonEmptyParallel.Aux[ConfigValue[G, *], Par[G, *]] =
    new NonEmptyParallel[ConfigValue[G, *]] {
      override final type F[A] = Par[G, A]

      override final val apply: Apply[Par[G, *]] =
        configValueParApply

      override final val flatMap: FlatMap[ConfigValue[G, *]] =
        configValueFlatMap

      override final val parallel: FunctionK[ConfigValue[G, *], Par[G, *]] =
        new FunctionK[ConfigValue[G, *], Par[G, *]] {
          override final def apply[A](value: ConfigValue[G, A]): Par[G, A] =
            Par(value)
        }

      override final val sequential: FunctionK[Par[G, *], ConfigValue[G, *]] =
        new FunctionK[Par[G, *], ConfigValue[G, *]] {
          override final def apply[A](par: Par[G, A]): ConfigValue[G, A] =
            par.unwrap
        }
    }

  /**
    * Newtype for parallel composition of configuration values.
    *
    * @group Newtypes
    */
  final type Par[F[_], A] = Par.Type[F, A]

  /**
    * @group Newtypes
    */
  object Par {
    type Base

    sealed trait Tag extends Any

    type Type[F[_], A] <: Base with Tag

    /**
      * Returns a [[Par]] instance for the specified [[ConfigValue]].
      */
    final def apply[F[_], A](value: ConfigValue[F, A]): Par[F, A] =
      value.asInstanceOf[Par[F, A]]

    implicit final class Ops[F[_], A] private[ciris] (
      private val par: Par[F, A]
    ) extends AnyVal {

      /**
        * Returns the underlying [[ConfigValue]] for the instance.
        */
      final def unwrap: ConfigValue[F, A] =
        par.asInstanceOf[ConfigValue[F, A]]
    }
  }
}
