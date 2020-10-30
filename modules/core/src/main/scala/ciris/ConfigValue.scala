/*
 * Copyright 2017-2020 Viktor Lövgren
 *
 * SPDX-License-Identifier: MIT
 */

package ciris

import cats.{Apply, FlatMap, NonEmptyParallel, Show}
import cats.implicits._
import cats.arrow.FunctionK
import cats.effect.{IO, Async, Blocker, ContextShift, Effect, Resource}
import cats.effect.implicits._
import ciris.ConfigEntry.{Default, Failed, Loaded}
import cats.Parallel
import cats.effect.LiftIO

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
    *
    * Note that if the [[ConfigValue]] contains any resources,
    * from using [[ConfigValue.resource]], these will be used
    * (acquired and released) as part of the returned effect.
    * If this behaviour is not desired, we can instead use
    * [[ConfigValue#resource]] to return a `Resource`.
    */
  final def attempt[F[_]](
    implicit F: Async[F],
    context: ContextShift[F]
  ): F[Either[ConfigError, A]] = resourceAttempt[F].use(F.pure)

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
    this.flatMapCE(ce => ConfigValue.suspendCE(ce.traverse(f).toIO))

  /**
    * Returns a new [[ConfigValue]] which loads the specified
    * configuration using the value.
    */
  final def flatMap[B](f: A => ConfigValue[B]): ConfigValue[B] =
    flatMapCE[B] {
      case ConfigEntry.Default(_, a) =>
        f(a()).transform {
          case Default(_, b)      => ConfigEntry.default(b())
          case failed @ Failed(_) => failed
          case Loaded(_, _, b)    => ConfigEntry.loaded(None, b)
        }
      case ce: ConfigEntry.Failed => ConfigValue.pure(ce)
      case ConfigEntry.Loaded(_, _, v) =>
        f(v).transform {
          case Default(_, b)   => ConfigEntry.loaded(None, b())
          case Failed(e2)      => ConfigEntry.failed(ConfigError.Loaded.and(e2))
          case Loaded(_, _, b) => ConfigEntry.loaded(None, b)
        }
    }

  private final def flatMapCE[B](f: ConfigEntry[A] => ConfigValue[B]): ConfigValue[B] =
    ConfigValue.FlatMapped(f, this)

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
  final def load[F[_]](
    implicit F: Async[F],
    context: ContextShift[F]
  ): F[A] = resource[F].use(F.pure)

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
    this.flatMapCE {
      case Default(error, a) =>
        value.transform {
          case Failed(nextError) if nextError.isMissing => Default(error.or(nextError), a)
          case Failed(nextError)                        => Failed(error.or(nextError))
          case Default(nextError, b)                    => Default(error.or(nextError), b)
          case Loaded(nextError, key, b)                => Loaded(error.or(nextError), key, b)
        }

      case Failed(error) if error.isMissing =>
        value.transform {
          case Failed(nextError)         => Failed(error.or(nextError))
          case Default(nextError, b)     => Default(error.or(nextError), b)
          case Loaded(nextError, key, b) => Loaded(error.or(nextError), key, b)
        }

      case failed @ Failed(_) =>
        ConfigValue.pure(failed)

      case loaded @ Loaded(_, _, _) =>
        ConfigValue.pure(loaded)
    }

  /**
    * Returns a new [[ConfigValue]] which loads the specified
    * configuration using the value.
    */
  @deprecated("Use flatMap instead", "1.2.1")
  final def parFlatMap[B](f: A => ConfigValue[B]): ConfigValue[B] =
    flatMap(f)

  /**
    * Returns a new [[ConfigValue]] with sensitive
    * details redacted from error messages.
    *
    * Using `.redacted` is equivalent to using
    * `.secret.map(_.value)`, except without
    * requiring a `Show` instance.
    */
  final def redacted: ConfigValue[A] =
    transform(_.mapError(_.redacted))

  /**
    * Returns a `Resource` with the specified effect
    * type which loads the configuration value.
    */
  final def resource[F[_]](
    implicit F: Async[F],
    context: ContextShift[F]
  ): Resource[F, A] = resourceAttempt[F].evalMap[F, A] {
    case Right(a)    => F.pure(a)
    case Left(error) => F.raiseError(error.throwable)
  }

  /**
    * Returns a `Resource` with the specified effect
    * type which attempts to loads the configuration value.
    */
  final def resourceAttempt[F[_]](
    implicit F: Async[F],
    context: ContextShift[F]
  ): Resource[F, Either[ConfigError, A]] = this.to[F].evalMap {
    case Default(_, a)   => F.delay(a().asRight[ConfigError])
    case Loaded(_, _, a) => F.pure(a.asRight[ConfigError])
    case Failed(error)   => F.pure(error.asLeft[A])
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
  final def secret(implicit show: Show[A]): ConfigValue[Secret[A]] =
    transform {
      case Default(error, a)     => Default(error.redacted, () => Secret(a()))
      case Failed(error)         => Failed(error.redacted)
      case Loaded(error, key, a) => Loaded(error.redacted, key, Secret(a))
    }

  private[ciris] def to[F[_]](
    implicit F: Async[F],
    context: ContextShift[F]
  ): Resource[F, ConfigEntry[A]] = {
    this.toPar[F](
      F,
      context,
      Parallel.identity[F]
    )
  }
  private[ciris] def toPar[F[_]](
    implicit F: Async[F],
    context: ContextShift[F],
    par: Parallel[F]
  ): Resource[F, ConfigEntry[A]]

  private[ciris] final def transform[B](
    f: ConfigEntry[A] => ConfigEntry[B]
  ): ConfigValue[B] =
    this.flatMapCE { ce => ConfigValue.pure(f(ce)) }
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

  private case class Pure[A](entry: ConfigEntry[A]) extends ConfigValue[A] {
    override private[ciris] def toPar[F[_]](
      implicit F: Async[F],
      context: ContextShift[F],
      par: Parallel[F]
    ): Resource[F, ConfigEntry[A]] = Resource.pure[F, ConfigEntry[A]](entry)
  }
  private case class Suspend[A](resource: Resource[IO, ConfigValue[A]]) extends ConfigValue[A] {
    override private[ciris] def toPar[F[_]](
      implicit F: Async[F],
      context: ContextShift[F],
      par: Parallel[F]
    ): Resource[F, ConfigEntry[A]] = {
      resource.mapK(LiftIO.liftK[F]).flatMap(_.toPar[F])
    }
  }
  private case class Blocked[A](blocker: Blocker, value: ConfigValue[A]) extends ConfigValue[A] {
    override private[ciris] def toPar[F[_]](
      implicit F: Async[F],
      context: ContextShift[F],
      par: Parallel[F]
    ): Resource[F, ConfigEntry[A]] = {
      Resource(
        blocker.blockOn(
          value.toPar[F].allocated
        )
      )
    }
  }
  private case class Ap[A, B](f: ConfigValue[B => A], value: ConfigValue[B])
      extends ConfigValue[A] {
    override private[ciris] def toPar[F[_]](
      implicit F: Async[F],
      context: ContextShift[F],
      par: Parallel[F]
    ): Resource[F, ConfigEntry[A]] =
      (f.toPar, value.toPar)
        .parMapN { (f, value) =>
          f.ap(value)
        }
  }
  private case class FlatMapped[A, B](f: ConfigEntry[B] => ConfigValue[A], value: ConfigValue[B])
      extends ConfigValue[A] {
    override private[ciris] def toPar[F[_]](
      implicit F: Async[F],
      context: ContextShift[F],
      par: Parallel[F]
    ): Resource[F, ConfigEntry[A]] =
      value.toPar[F].flatMap { e =>
        f(e).toPar[F]
      }
  }

  /**
    * Returns a new [[ConfigValue]] which loads a configuration
    * value using a callback.
    *
    * @group Create
    */
  final def async[A](k: (Either[Throwable, ConfigValue[A]] => Unit) => Unit): ConfigValue[A] =
    Suspend(Resource.liftF(IO.async(k)))

  /**
    * Returns a new [[ConfigValue]] which loads the specified
    * Blockeding value.
    *
    * Note that if the [[ConfigValue]] contains any resources,
    * from using [[ConfigValue.resource]], these will be used
    * (acquired and released) using the specified `Blocker`.
    *
    * @group Create
    */
  final def blockOn[A](blocker: Blocker)(value: ConfigValue[A]): ConfigValue[A] =
    Blocked(blocker, value)

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
    resource[F, A](Resource.liftF(value))

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
    Pure(entry)

  private[ciris] final def suspendCE[F[_]: Effect, A](entry: F[ConfigEntry[A]]): ConfigValue[A] =
    Suspend(Resource.liftF(entry.toIO).map(ConfigValue.pure))

  /**
    * Returns a new [[ConfigValue]] for the specified resource.
    *
    * @group Create
    */
  final def resource[F[_], V](
    resource: Resource[F, ConfigValue[V]]
  )(implicit F: Effect[F]): ConfigValue[V] =
    Suspend[V](resource.mapK(FunctionK.lift[F, IO](F.toIO)))

  /**
    * Returns a new [[ConfigValue]] which suspends loading
    * of the specified value.
    *
    * @group Create
    */
  final def suspend[A](value: => ConfigValue[A]): ConfigValue[A] =
    Suspend(Resource.liftF(IO.delay(value)))

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

      /**
        * Note: this is intentionally not stack safe, as the `flatMap`
        * on `ConfigValue` cannot be expressed in a tail-recursive way.
        */
      override final def tailRecM[A, B](a: A)(f: A => ConfigValue[Either[A, B]]): ConfigValue[B] =
        f(a).flatMap {
          case Left(a)  => tailRecM(a)(f)
          case Right(b) => default(b)
        }
    }

  /**
    * @group Instances
    */
  implicit final val configValueParApply: Apply[Par] =
    new Apply[Par] {
      override final def ap[A, B](pab: Par[A => B])(pa: Par[A]): Par[B] =
        Par(Ap(pab.unwrap, pa.unwrap))

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
