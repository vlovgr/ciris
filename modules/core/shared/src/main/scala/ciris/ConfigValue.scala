/*
 * Copyright 2017-2024 Viktor Rudebeck
 *
 * SPDX-License-Identifier: MIT
 */

package ciris

import cats.Show
import cats.effect.kernel.{Async, Resource}
import cats.syntax.all._
import ciris.ConfigEntry.{Default, Failed, Loaded}
import cats.data.NonEmptyList
import cats.Monad
import scala.annotation.nowarn
import cats.NonEmptyParallel
import cats.arrow.FunctionK

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

  final def asIso[B](implicit codec: ConfigCodec[A, B]): ConfigValue[F, B] =
    itransform {
      case Default(error, a) =>
        codec.decode(None, a) match {
          case Right(b)          => Default(error, b)
          case Left(decodeError) => Failed(error.or(decodeError))
        }

      case failed @ Failed(_) =>
        failed

      case Loaded(error, key, a) =>
        codec.decode(key, a) match {
          case Right(b)          => Loaded(error, key, b)
          case Left(decodeError) => Failed(error.or(decodeError))
        }
    }(codec.encode)

  /**
    * Returns a new [[ConfigValue]] which attempts to decode the
    * value to the specified type.
    */
  @deprecated("Use asIso instead", "3.7.0")
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
    * Using `.default(a)` is roughly equivalent to using `.or(default(a))`.
    */
  def default(value: => A): ConfigValue[F, A] = ConfigValue.DefaultValue(this, value)

  /**
    * Alias for `evalMap(f).flatten`.
    */
  @deprecated("Use ievalMap or flatMap instead", "3.7.0")
  final def evalFlatMap[G[x] >: F[x], B](f: A => G[ConfigValue[G, B]]): ConfigValue[G, B] =
    evalMap(f).flatten

  /**
    * Returns a new [[ConfigValue]] which applies the
    * specified effectful function on the value.
    */
  @deprecated("Use ievalMap instead", "3.7.0")
  final def evalMap[G[x] >: F[x], B](f: A => G[B]): ConfigValue[G, B] =
    ConfigValue.EvalMap[F, G, A, B](this, f)

  /**
    * Returns a new [[ConfigValue]] which loads the specified
    * configuration using the value.
    */
  final def flatMap[G[x] >: F[x], B](f: A => ConfigValue[G, B]): ConfigValue[G, B] =
    ConfigValue.FlatMap[F, G, A, B](this, f)

  /**
    * Isomophic-ish version of [[evalMap]].
    */
  final def ievalMap[G[x] >: F[x], B](f: A => G[B])(g: B => A): ConfigValue[G, B] =
    ConfigValue.IEvalMap[F, G, A, B](this, f, g)

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
  @deprecated("Use imap instead", "3.7.0")
  final def map[B](f: A => B): ConfigValue[F, B] =
    transform(_.map(f))

  /**
    * Isomorphic [[map]].
    */
  final def imap[B](f: A => B)(g: B => A): ConfigValue[F, B] =
    itransform(_.map(f))(g)

  /**
    * Returns a new [[ConfigValue]] which uses `None` as the
    * default if the value is missing.
    */
  final def option: ConfigValue[F, Option[A]] = ConfigValue.Optional(this)

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
  final def or[G[x] >: F[x]](value: => ConfigValue[G, A]): ConfigValue[G, A] = this match {
    case ConfigValue.Or(alternatives) => ConfigValue.Or(alternatives :+ value)
    case _                            => ConfigValue.Or(NonEmptyList.of(this, value))
  }

  final def product[G[x] >: F[x], B](right: ConfigValue[G, B]): ConfigValue[G, (A, B)] =
    ConfigValue.Product(this, right)

  /**
    * Returns a new [[ConfigValue]] with sensitive
    * details redacted from error messages.
    *
    * Using `.redacted` is equivalent to using
    * `.secret.map(_.value)`, except without
    * requiring a `Show` instance.
    */
  final def redacted: ConfigValue[F, A] =
    itransform(_.mapError(_.redacted))(identity)

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
    itransform({
      case Default(error, a)     => Default(error.redacted, Secret(a)(show))
      case Failed(error)         => Failed(error.redacted)
      case Loaded(error, key, a) => Loaded(error.redacted, key, Secret(a)(show))
    })(_.value)

  final def fields: List[ConfigField] =
    fieldsRec(None)

  private[ciris] def fieldsRec(defaultValue: Option[A]): List[ConfigField]

  private[ciris] def to[G[x] >: F[x]](implicit G: Async[G]): Resource[G, ConfigEntry[A]]

  private[ciris] final def transform[B](
    f: ConfigEntry[A] => ConfigEntry[B]
  ): ConfigValue[F, B] =
    ConfigValue.Transform(this, f)

  private[ciris] final def itransform[B](f: ConfigEntry[A] => ConfigEntry[B])(
    g: B => A
  ): ConfigValue[F, B] =
    ConfigValue.ITransform(this, f, g)

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
    ConfigValue.ToUseOnceSecret(this)

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
    * A misceallenous effectful configuration that cannot be introspected in a pure environment.
    */
  private sealed abstract class Effectful[F[_], A] extends ConfigValue[F, A] {
    override def fieldsRec(defaultValue: Option[A]): List[ConfigField] =
      Nil
  }

  private final case class Apply[F[_], A, B](ff: ConfigValue[F, A => B], fa: ConfigValue[F, A])
      extends ConfigValue[F, B] {

    override def fieldsRec(defaultValue: Option[B]): List[ConfigField] =
      ff.fieldsRec(None) ++ fa.fieldsRec(None)

    override final def to[G[x] >: F[x]](
      implicit G: Async[G]
    ): Resource[G, ConfigEntry[B]] =
      (ff.to[G], fa.to[G]).parMapN {
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
  }

  private final case class DefaultValue[F[_], A](configValue: ConfigValue[F, A], defaultValue: A)
      extends ConfigValue[F, A] {
    override def default(value: => A): ConfigValue[F, A] =
      DefaultValue(configValue, value)

    override def fieldsRec(existingDefaultValue: Option[A]): List[ConfigField] =
      configValue.fieldsRec(existingDefaultValue.orElse(Some(defaultValue))).map(_.option)

    override final def to[G[x] >: F[x]](implicit G: Async[G]): Resource[G, ConfigEntry[A]] = {
      configValue.to[G].map {
        case Default(error, _)                => Default(error, defaultValue)
        case Failed(error) if error.isMissing => Default(error, defaultValue)
        case failed @ Failed(_)               => failed
        case loaded @ Loaded(_, _, _)         => loaded
      }
    }
  }

  private[ciris] final case class Environment(name: String) extends ConfigValue[Effect, String] {
    override def fieldsRec(defaultValue: Option[String]): List[ConfigField] =
      List(ConfigField.fromOption(ConfigKey.env(name), defaultValue))

    override final def to[G[x]](implicit G: Async[G]): Resource[G, ConfigEntry[String]] =
      Resource.eval(G.delay(getEnv(name)))
  }

  private final case class EvalMap[F[_], G[x] >: F[x], A, B](input: ConfigValue[F, A], f: A => G[B])
      extends ConfigValue[G, B] {
    override def fieldsRec(defaultValue: Option[B]): List[ConfigField] =
      input.fieldsRec(None)

    override final def to[H[x] >: G[x]](implicit H: Async[H]): Resource[H, ConfigEntry[B]] =
      input.to[H].evalMap(_.traverse[H, B](f))
  }

  private final case class FlatMap[F[_], G[x] >: F[x], A, B](
    input: ConfigValue[F, A],
    f: A => ConfigValue[G, B]
  ) extends ConfigValue[G, B] {
    override def fieldsRec(defaultValue: Option[B]): List[ConfigField] =
      input.fieldsRec(None)

    override final def to[H[x] >: G[x]](implicit H: Async[H]): Resource[H, ConfigEntry[B]] =
      input.to[H].flatMap {
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
  }

  private final case class IEvalMap[F[_], G[x] >: F[x], A, B](
    input: ConfigValue[F, A],
    f: A => G[B],
    g: B => A
  ) extends ConfigValue[G, B] {
    override def fieldsRec(defaultValue: Option[B]): List[ConfigField] =
      input.fieldsRec(defaultValue.map(g))

    override final def to[H[x] >: G[x]](implicit H: Async[H]): Resource[H, ConfigEntry[B]] =
      input.to[H].evalMap(_.traverse[H, B](f))
  }

  private final case class ITransform[F[_], A, B](
    input: ConfigValue[F, A],
    f: ConfigEntry[A] => ConfigEntry[B],
    g: B => A
  ) extends ConfigValue[F, B] {
    override def fieldsRec(defaultValue: Option[B]): List[ConfigField] =
      input.fieldsRec(defaultValue.map(g))

    override private[ciris] def to[G[x] >: F[x]](
      implicit G: Async[G]
    ): Resource[G, ConfigEntry[B]] =
      input.to[G].map(f)
  }

  private final case class Optional[F[_], A](input: ConfigValue[F, A])
      extends ConfigValue[F, Option[A]] {

    override def fieldsRec(defaultValue: Option[Option[A]]): List[ConfigField] =
      input.fieldsRec(defaultValue.flatten).map(_.option)

    override private[ciris] def to[G[x] >: F[x]](
      implicit G: Async[G]
    ): Resource[G, ConfigEntry[Option[A]]] =
      input.to[G].map {
        case Default(error, _)                => Default(error, None)
        case Failed(error) if error.isMissing => Default(error, None)
        case failed @ Failed(_)               => failed
        case Loaded(error, key, a)            => Loaded(error, key, Some(a))
      }
  }

  private final case class Or[F[_], A](alternatives: NonEmptyList[ConfigValue[F, A]])
      extends ConfigValue[F, A] {
    override def fieldsRec(defaultValue: Option[A]): List[ConfigField] =
      alternatives.toList.flatMap(_.fieldsRec(defaultValue))

    override final def to[G[x] >: F[x]](implicit G: Async[G]): Resource[G, ConfigEntry[A]] =
      alternatives.reduceLeftTo(_.to[G])((res, value) =>
        res.flatMap {
          case Default(error, a) =>
            value.to[G].map {
              case Failed(nextError) if nextError.isMissing => Default(error.or(nextError), a)
              case Failed(nextError)                        => Failed(error.or(nextError))
              case Default(nextError, b)                    => Default(error.or(nextError), b)
              case Loaded(nextError, key, b)                => Loaded(error.or(nextError), key, b)
            }

          case Failed(error) if error.isMissing =>
            value.to[G].map {
              case Failed(nextError)         => Failed(error.or(nextError))
              case Default(nextError, b)     => Default(error.or(nextError), b)
              case Loaded(nextError, key, b) => Loaded(error.or(nextError), key, b)
            }

          case failed @ Failed(_) =>
            Resource.eval(G.pure(failed))

          case loaded @ Loaded(_, _, _) =>
            Resource.eval(G.pure(loaded))
        }
      )
  }

  private final case class Product[F[_], A, B](a: ConfigValue[F, A], b: ConfigValue[F, B])
      extends ConfigValue[F, (A, B)] {
    override def fieldsRec(defaultValue: Option[(A, B)]): List[ConfigField] =
      a.fieldsRec(defaultValue.map(_._1)) ++ b.fieldsRec(defaultValue.map(_._2))

    override private[ciris] def to[G[x] >: F[x]](
      implicit G: Async[G]
    ): Resource[G, ConfigEntry[(A, B)]] =
      a.to[G].map2(b.to[G]) {
        case (Default(errorA, valueA), Default(errorB, valueB)) =>
          Default(errorA.and(errorB), (valueA, valueB))
        case (Default(errorA, valueA), Loaded(errorB, _, valueB)) =>
          Default(errorA.and(errorB), (valueA, valueB))
        case (Loaded(errorA, _, valueA), Default(errorB, valueB)) =>
          Default(errorA.and(errorB), (valueA, valueB))
        case (Loaded(errorA, keyA, valueA), Loaded(errorB, keyB, valueB)) =>
          Loaded(
            errorA.and(errorB),
            for {
              kA <- keyA
              kB <- keyB
            } yield kA.and(kB),
            (valueA, valueB)
          )
        case (Failed(errorA), entryB) => Failed(errorA.and(entryB.error))
        case (entryA, Failed(errorB)) => Failed(entryA.error.and(errorB))
      }
  }

  private[ciris] final case class Property(name: String) extends ConfigValue[Effect, String] {
    override def fieldsRec(defaultValue: Option[String]): List[ConfigField] =
      List(ConfigField.fromOption(ConfigKey.prop(name), defaultValue))

    override final def to[G[x]](implicit G: Async[G]): Resource[G, ConfigEntry[String]] =
      Resource.eval(G.delay {
        val key = ConfigKey.prop(name)

        if (name.nonEmpty) {
          val value = System.getProperty(name)
          if (value != null) {
            ConfigEntry.loaded(Some(key), value)
          } else {
            ConfigEntry.missing(key)
          }
        } else {
          ConfigEntry.missing(key)
        }
      })
  }

  private final case class Pure[A](entry: ConfigEntry[A]) extends ConfigValue[Effect, A] {
    override def fieldsRec(defaultValue: Option[A]): List[ConfigField] = Nil

    override final def to[G[x]](implicit G: Async[G]): Resource[G, ConfigEntry[A]] =
      Resource.pure(entry)
  }

  private final case class Transform[F[_], A, B](
    input: ConfigValue[F, A],
    f: ConfigEntry[A] => ConfigEntry[B]
  ) extends ConfigValue[F, B] {
    override def fieldsRec(defaultValue: Option[B]): List[ConfigField] =
      input.fieldsRec(None)

    override final def to[G[x] >: F[x]](implicit G: Async[G]): Resource[G, ConfigEntry[B]] =
      input.to[G].map(f)
  }

  private final case class ToUseOnceSecret[F[_], A](input: ConfigValue[F, A])(
    implicit ev: A <:< Array[Char]
  ) extends ConfigValue[F, UseOnceSecret] {
    override def fieldsRec(defaultValue: Option[UseOnceSecret]): List[ConfigField] =
      input.fieldsRec(None)

    override final def to[G[x] >: F[x]](
      implicit G: Async[G]
    ): Resource[G, ConfigEntry[UseOnceSecret]] =
      input.redacted.to[G].evalMap(_.traverse(UseOnceSecret[G](_)))
  }

  /**
    * Returns a new [[ConfigValue]] which loads a configuration
    * value using a callback.
    *
    * @group Create
    */
  final def async[F[_], A](
    k: (Either[Throwable, ConfigValue[F, A]] => Unit) => F[Option[F[Unit]]]
  ): ConfigValue[F, A] =
    new Effectful[F, A] {

      override private[ciris] def to[G[x] >: F[x]](
        implicit G: Async[G]
      ): Resource[G, ConfigEntry[A]] =
        Resource
          .eval(G.async[ConfigValue[F, A]](k(_).asInstanceOf[G[Option[G[Unit]]]]))
          .flatMap(_.to[G])
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
    new Effectful[F, A] {
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
    new Effectful[F, A] {
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
    new Effectful[F, A] {
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
    Pure(entry)

  /**
    * Returns a new [[ConfigValue]] for the specified resource.
    *
    * @group Create
    */
  final def resource[F[_], A](resource: Resource[F, ConfigValue[F, A]]): ConfigValue[F, A] = {
    val _resource = resource
    new Effectful[F, A] {
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
    new Effectful[F, A] {
      override final def to[G[x] >: F[x]](implicit G: Async[G]): Resource[G, ConfigEntry[A]] =
        Resource.suspend(G.delay(value.to[G]))

      override final def toString: String =
        "ConfigValue$" + System.identityHashCode(this)
    }

  @deprecated("Use configValueMonad instead", "3.7.0")
  final def configValueFlatMap[F[_]]: cats.FlatMap[ConfigValue[F, *]] = configValueMonad[F]

  /**
    * @group Instances
    */
  implicit final def configValueMonad[F[_]]: Monad[ConfigValue[F, *]] =
    new Monad[ConfigValue[F, *]] {

      override def ap[A, B](ff: ConfigValue[F, A => B])(fa: ConfigValue[F, A]): ConfigValue[F, B] =
        Apply(ff, fa)

      override def pure[A](x: A): ConfigValue[F, A] = default(x)

      override def flatMap[A, B](fa: ConfigValue[F, A])(
        f: A => ConfigValue[F, B]
      ): ConfigValue[F, B] =
        fa.flatMap(f)

      @nowarn("cat=deprecation")
      override def map[A, B](fa: ConfigValue[F, A])(f: A => B): ConfigValue[F, B] =
        fa.map(f)

      override def imap[A, B](fa: ConfigValue[F, A])(f: A => B)(g: B => A): ConfigValue[F, B] =
        fa.imap(f)(g)

      override def product[A, B](
        fa: ConfigValue[F, A],
        fb: ConfigValue[F, B]
      ): ConfigValue[F, (A, B)] =
        fa.product(fb)

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
  implicit final def configValueParApply[F[_]]: cats.Apply[Par[F, *]] =
    new cats.Apply[Par[F, *]] {
      override final def ap[A, B](pab: Par[F, A => B])(pa: Par[F, A]): Par[F, B] =
        Par {
          new ConfigValue[F, B] {
            override def fieldsRec(defaultValue: Option[B]): List[ConfigField] =
              pab.unwrap.fieldsRec(None) ++ pa.unwrap.fieldsRec(None)

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

      @deprecated("Use imap instead", "3.7.0")
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

      override final val apply: cats.Apply[Par[G, *]] =
        configValueParApply

      override final val flatMap: cats.FlatMap[ConfigValue[G, *]] =
        configValueMonad

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
