// format: off

/**
  * Generated using sbt source generators.
  * You should not modify this file directly.
  */

package ciris

import ciris.api._
import ciris.api.syntax._
import ciris.ConfigErrors.{left, right}

private[ciris] class LoadConfigs {

  /**
    * Wraps the specified value in an `F[Either[ConfigErrors, Z]]`. Useful
    * when you want to use a static configuration inside a `withValues`
    * block, requiring you to wrap it with this method.
    *
    * @param z the value to wrap
    * @tparam F the [[ConfigValue]] context
    * @tparam Z the type of the value to wrap
    * @return the value wrapped in an `F[Either[ConfigErrors, Z]]`
    */
  def loadConfig[F[_]: Applicative, Z](z: Z): F[Either[ConfigErrors, Z]] =
    right(z).pure[F]

  /**
    * Loads a configuration using the specified [[ConfigValue]].
    *
    * @param a1 the configuration value
    * @param f the function to create the configuration
    * @tparam F the [[ConfigValue]] context
    * @tparam A1 the type of the configuration value
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[F[_]: Functor, A1, Z](a1: ConfigValue[F, A1])(f: A1 => Z): F[Either[ConfigErrors, Z]] =
    a1.value.map(_.fold(error => Left(ConfigErrors(error)), a1 => Right(f(a1))))

  /**
    * Defines a requirement on a single [[ConfigValue]] in order to be
    * able to load a configuration. The method wraps `loadConfig`
    * methods, requiring the provided [[ConfigValue]] to be
    * available in order to use the `loadConfig` methods.
    *
    * @param a1 the configuration value
    * @param f the function to create the configuration
    * @tparam F the [[ConfigValue]] context
    * @tparam A1 the type of the configuration value
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValue[F[_]: Monad, A1, Z](a1: ConfigValue[F, A1])(f: A1 => F[Either[ConfigErrors, Z]]): F[Either[ConfigErrors, Z]] =
    withValues(a1)(f)

  /**
    * Defines a requirement on a single [[ConfigValue]] in order to be
    * able to load a configuration. The method wraps any `loadConfig`
    * methods, requiring the provided [[ConfigValue]] to be
    * available in order to use the `loadConfig` methods.
    *
    * @param a1 the configuration value
    * @param f the function to create the configuration
    * @tparam F the [[ConfigValue]] context
    * @tparam A1 the type of the configuration value
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[F[_]: Monad, A1, Z](a1: ConfigValue[F, A1])(f: A1 => F[Either[ConfigErrors, Z]]): F[Either[ConfigErrors, Z]] =
    a1.value.flatMap {
      case Left(error) => left[Z](ConfigErrors(error)).pure[F]
      case Right(value) => f(value)
    }

  /**
    * Loads a configuration using the 2 specified [[ConfigValue]]s.
    * Deals with error accumulation if there are any errors in the
    * provided [[ConfigValue]]s.
    *
    * @param a1 configuration value 1
    * @param a2 configuration value 2
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration value 1
    * @tparam A2 the type for configuration value 2
    * @tparam F the [[ConfigValue]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[F[_]: Functor, A1, A2, Z](a1: ConfigValue[F, A1], a2: ConfigValue[F, A2])(f: (A1, A2) => Z): F[Either[ConfigErrors, Z]] =
    (a1 append a2).value.map(_.right.map(f.tupled))

  /**
    * Defines a requirement on 2 [[ConfigValue]]s in order to be
    * able to load a configuration. The method wraps `loadConfig`
    * methods, requiring the provided [[ConfigValue]]s to be
    * available in order to use the `loadConfig` methods.
    *
    * Deals with error accumulation if there are any errors in
    * the provided [[ConfigValue]]s.
    *
    * @param a1 configuration value 1
    * @param a2 configuration value 2
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration value 1
    * @tparam A2 the type for configuration value 2
    * @tparam F the [[ConfigValue]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[F[_]: Monad, A1, A2, Z](a1: ConfigValue[F, A1], a2: ConfigValue[F, A2])(f: (A1, A2) => F[Either[ConfigErrors, Z]]): F[Either[ConfigErrors, Z]] =
    (a1 append a2).value.flatMap {
       case Left(errors) => left[Z](errors).pure[F]
       case Right(values) => f.tupled.apply(values)
     }

  /**
    * Loads a configuration using the 3 specified [[ConfigValue]]s.
    * Deals with error accumulation if there are any errors in the
    * provided [[ConfigValue]]s.
    *
    * @param a1 configuration value 1
    * @param a2 configuration value 2
    * @param a3 configuration value 3
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration value 1
    * @tparam A2 the type for configuration value 2
    * @tparam A3 the type for configuration value 3
    * @tparam F the [[ConfigValue]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[F[_]: Functor, A1, A2, A3, Z](a1: ConfigValue[F, A1], a2: ConfigValue[F, A2], a3: ConfigValue[F, A3])(f: (A1, A2, A3) => Z): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3).value.map(_.right.map(f.tupled))

  /**
    * Defines a requirement on 3 [[ConfigValue]]s in order to be
    * able to load a configuration. The method wraps `loadConfig`
    * methods, requiring the provided [[ConfigValue]]s to be
    * available in order to use the `loadConfig` methods.
    *
    * Deals with error accumulation if there are any errors in
    * the provided [[ConfigValue]]s.
    *
    * @param a1 configuration value 1
    * @param a2 configuration value 2
    * @param a3 configuration value 3
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration value 1
    * @tparam A2 the type for configuration value 2
    * @tparam A3 the type for configuration value 3
    * @tparam F the [[ConfigValue]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[F[_]: Monad, A1, A2, A3, Z](a1: ConfigValue[F, A1], a2: ConfigValue[F, A2], a3: ConfigValue[F, A3])(f: (A1, A2, A3) => F[Either[ConfigErrors, Z]]): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3).value.flatMap {
       case Left(errors) => left[Z](errors).pure[F]
       case Right(values) => f.tupled.apply(values)
     }

  /**
    * Loads a configuration using the 4 specified [[ConfigValue]]s.
    * Deals with error accumulation if there are any errors in the
    * provided [[ConfigValue]]s.
    *
    * @param a1 configuration value 1
    * @param a2 configuration value 2
    * @param a3 configuration value 3
    * @param a4 configuration value 4
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration value 1
    * @tparam A2 the type for configuration value 2
    * @tparam A3 the type for configuration value 3
    * @tparam A4 the type for configuration value 4
    * @tparam F the [[ConfigValue]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[F[_]: Functor, A1, A2, A3, A4, Z](a1: ConfigValue[F, A1], a2: ConfigValue[F, A2], a3: ConfigValue[F, A3], a4: ConfigValue[F, A4])(f: (A1, A2, A3, A4) => Z): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4).value.map(_.right.map(f.tupled))

  /**
    * Defines a requirement on 4 [[ConfigValue]]s in order to be
    * able to load a configuration. The method wraps `loadConfig`
    * methods, requiring the provided [[ConfigValue]]s to be
    * available in order to use the `loadConfig` methods.
    *
    * Deals with error accumulation if there are any errors in
    * the provided [[ConfigValue]]s.
    *
    * @param a1 configuration value 1
    * @param a2 configuration value 2
    * @param a3 configuration value 3
    * @param a4 configuration value 4
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration value 1
    * @tparam A2 the type for configuration value 2
    * @tparam A3 the type for configuration value 3
    * @tparam A4 the type for configuration value 4
    * @tparam F the [[ConfigValue]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[F[_]: Monad, A1, A2, A3, A4, Z](a1: ConfigValue[F, A1], a2: ConfigValue[F, A2], a3: ConfigValue[F, A3], a4: ConfigValue[F, A4])(f: (A1, A2, A3, A4) => F[Either[ConfigErrors, Z]]): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4).value.flatMap {
       case Left(errors) => left[Z](errors).pure[F]
       case Right(values) => f.tupled.apply(values)
     }

  /**
    * Loads a configuration using the 5 specified [[ConfigValue]]s.
    * Deals with error accumulation if there are any errors in the
    * provided [[ConfigValue]]s.
    *
    * @param a1 configuration value 1
    * @param a2 configuration value 2
    * @param a3 configuration value 3
    * @param a4 configuration value 4
    * @param a5 configuration value 5
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration value 1
    * @tparam A2 the type for configuration value 2
    * @tparam A3 the type for configuration value 3
    * @tparam A4 the type for configuration value 4
    * @tparam A5 the type for configuration value 5
    * @tparam F the [[ConfigValue]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[F[_]: Functor, A1, A2, A3, A4, A5, Z](a1: ConfigValue[F, A1], a2: ConfigValue[F, A2], a3: ConfigValue[F, A3], a4: ConfigValue[F, A4], a5: ConfigValue[F, A5])(f: (A1, A2, A3, A4, A5) => Z): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4 append a5).value.map(_.right.map(f.tupled))

  /**
    * Defines a requirement on 5 [[ConfigValue]]s in order to be
    * able to load a configuration. The method wraps `loadConfig`
    * methods, requiring the provided [[ConfigValue]]s to be
    * available in order to use the `loadConfig` methods.
    *
    * Deals with error accumulation if there are any errors in
    * the provided [[ConfigValue]]s.
    *
    * @param a1 configuration value 1
    * @param a2 configuration value 2
    * @param a3 configuration value 3
    * @param a4 configuration value 4
    * @param a5 configuration value 5
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration value 1
    * @tparam A2 the type for configuration value 2
    * @tparam A3 the type for configuration value 3
    * @tparam A4 the type for configuration value 4
    * @tparam A5 the type for configuration value 5
    * @tparam F the [[ConfigValue]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[F[_]: Monad, A1, A2, A3, A4, A5, Z](a1: ConfigValue[F, A1], a2: ConfigValue[F, A2], a3: ConfigValue[F, A3], a4: ConfigValue[F, A4], a5: ConfigValue[F, A5])(f: (A1, A2, A3, A4, A5) => F[Either[ConfigErrors, Z]]): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4 append a5).value.flatMap {
       case Left(errors) => left[Z](errors).pure[F]
       case Right(values) => f.tupled.apply(values)
     }

  /**
    * Loads a configuration using the 6 specified [[ConfigValue]]s.
    * Deals with error accumulation if there are any errors in the
    * provided [[ConfigValue]]s.
    *
    * @param a1 configuration value 1
    * @param a2 configuration value 2
    * @param a3 configuration value 3
    * @param a4 configuration value 4
    * @param a5 configuration value 5
    * @param a6 configuration value 6
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration value 1
    * @tparam A2 the type for configuration value 2
    * @tparam A3 the type for configuration value 3
    * @tparam A4 the type for configuration value 4
    * @tparam A5 the type for configuration value 5
    * @tparam A6 the type for configuration value 6
    * @tparam F the [[ConfigValue]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[F[_]: Functor, A1, A2, A3, A4, A5, A6, Z](a1: ConfigValue[F, A1], a2: ConfigValue[F, A2], a3: ConfigValue[F, A3], a4: ConfigValue[F, A4], a5: ConfigValue[F, A5], a6: ConfigValue[F, A6])(f: (A1, A2, A3, A4, A5, A6) => Z): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4 append a5 append a6).value.map(_.right.map(f.tupled))

  /**
    * Defines a requirement on 6 [[ConfigValue]]s in order to be
    * able to load a configuration. The method wraps `loadConfig`
    * methods, requiring the provided [[ConfigValue]]s to be
    * available in order to use the `loadConfig` methods.
    *
    * Deals with error accumulation if there are any errors in
    * the provided [[ConfigValue]]s.
    *
    * @param a1 configuration value 1
    * @param a2 configuration value 2
    * @param a3 configuration value 3
    * @param a4 configuration value 4
    * @param a5 configuration value 5
    * @param a6 configuration value 6
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration value 1
    * @tparam A2 the type for configuration value 2
    * @tparam A3 the type for configuration value 3
    * @tparam A4 the type for configuration value 4
    * @tparam A5 the type for configuration value 5
    * @tparam A6 the type for configuration value 6
    * @tparam F the [[ConfigValue]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[F[_]: Monad, A1, A2, A3, A4, A5, A6, Z](a1: ConfigValue[F, A1], a2: ConfigValue[F, A2], a3: ConfigValue[F, A3], a4: ConfigValue[F, A4], a5: ConfigValue[F, A5], a6: ConfigValue[F, A6])(f: (A1, A2, A3, A4, A5, A6) => F[Either[ConfigErrors, Z]]): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4 append a5 append a6).value.flatMap {
       case Left(errors) => left[Z](errors).pure[F]
       case Right(values) => f.tupled.apply(values)
     }

  /**
    * Loads a configuration using the 7 specified [[ConfigValue]]s.
    * Deals with error accumulation if there are any errors in the
    * provided [[ConfigValue]]s.
    *
    * @param a1 configuration value 1
    * @param a2 configuration value 2
    * @param a3 configuration value 3
    * @param a4 configuration value 4
    * @param a5 configuration value 5
    * @param a6 configuration value 6
    * @param a7 configuration value 7
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration value 1
    * @tparam A2 the type for configuration value 2
    * @tparam A3 the type for configuration value 3
    * @tparam A4 the type for configuration value 4
    * @tparam A5 the type for configuration value 5
    * @tparam A6 the type for configuration value 6
    * @tparam A7 the type for configuration value 7
    * @tparam F the [[ConfigValue]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[F[_]: Functor, A1, A2, A3, A4, A5, A6, A7, Z](a1: ConfigValue[F, A1], a2: ConfigValue[F, A2], a3: ConfigValue[F, A3], a4: ConfigValue[F, A4], a5: ConfigValue[F, A5], a6: ConfigValue[F, A6], a7: ConfigValue[F, A7])(f: (A1, A2, A3, A4, A5, A6, A7) => Z): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7).value.map(_.right.map(f.tupled))

  /**
    * Defines a requirement on 7 [[ConfigValue]]s in order to be
    * able to load a configuration. The method wraps `loadConfig`
    * methods, requiring the provided [[ConfigValue]]s to be
    * available in order to use the `loadConfig` methods.
    *
    * Deals with error accumulation if there are any errors in
    * the provided [[ConfigValue]]s.
    *
    * @param a1 configuration value 1
    * @param a2 configuration value 2
    * @param a3 configuration value 3
    * @param a4 configuration value 4
    * @param a5 configuration value 5
    * @param a6 configuration value 6
    * @param a7 configuration value 7
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration value 1
    * @tparam A2 the type for configuration value 2
    * @tparam A3 the type for configuration value 3
    * @tparam A4 the type for configuration value 4
    * @tparam A5 the type for configuration value 5
    * @tparam A6 the type for configuration value 6
    * @tparam A7 the type for configuration value 7
    * @tparam F the [[ConfigValue]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[F[_]: Monad, A1, A2, A3, A4, A5, A6, A7, Z](a1: ConfigValue[F, A1], a2: ConfigValue[F, A2], a3: ConfigValue[F, A3], a4: ConfigValue[F, A4], a5: ConfigValue[F, A5], a6: ConfigValue[F, A6], a7: ConfigValue[F, A7])(f: (A1, A2, A3, A4, A5, A6, A7) => F[Either[ConfigErrors, Z]]): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7).value.flatMap {
       case Left(errors) => left[Z](errors).pure[F]
       case Right(values) => f.tupled.apply(values)
     }

  /**
    * Loads a configuration using the 8 specified [[ConfigValue]]s.
    * Deals with error accumulation if there are any errors in the
    * provided [[ConfigValue]]s.
    *
    * @param a1 configuration value 1
    * @param a2 configuration value 2
    * @param a3 configuration value 3
    * @param a4 configuration value 4
    * @param a5 configuration value 5
    * @param a6 configuration value 6
    * @param a7 configuration value 7
    * @param a8 configuration value 8
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration value 1
    * @tparam A2 the type for configuration value 2
    * @tparam A3 the type for configuration value 3
    * @tparam A4 the type for configuration value 4
    * @tparam A5 the type for configuration value 5
    * @tparam A6 the type for configuration value 6
    * @tparam A7 the type for configuration value 7
    * @tparam A8 the type for configuration value 8
    * @tparam F the [[ConfigValue]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[F[_]: Functor, A1, A2, A3, A4, A5, A6, A7, A8, Z](a1: ConfigValue[F, A1], a2: ConfigValue[F, A2], a3: ConfigValue[F, A3], a4: ConfigValue[F, A4], a5: ConfigValue[F, A5], a6: ConfigValue[F, A6], a7: ConfigValue[F, A7], a8: ConfigValue[F, A8])(f: (A1, A2, A3, A4, A5, A6, A7, A8) => Z): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8).value.map(_.right.map(f.tupled))

  /**
    * Defines a requirement on 8 [[ConfigValue]]s in order to be
    * able to load a configuration. The method wraps `loadConfig`
    * methods, requiring the provided [[ConfigValue]]s to be
    * available in order to use the `loadConfig` methods.
    *
    * Deals with error accumulation if there are any errors in
    * the provided [[ConfigValue]]s.
    *
    * @param a1 configuration value 1
    * @param a2 configuration value 2
    * @param a3 configuration value 3
    * @param a4 configuration value 4
    * @param a5 configuration value 5
    * @param a6 configuration value 6
    * @param a7 configuration value 7
    * @param a8 configuration value 8
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration value 1
    * @tparam A2 the type for configuration value 2
    * @tparam A3 the type for configuration value 3
    * @tparam A4 the type for configuration value 4
    * @tparam A5 the type for configuration value 5
    * @tparam A6 the type for configuration value 6
    * @tparam A7 the type for configuration value 7
    * @tparam A8 the type for configuration value 8
    * @tparam F the [[ConfigValue]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[F[_]: Monad, A1, A2, A3, A4, A5, A6, A7, A8, Z](a1: ConfigValue[F, A1], a2: ConfigValue[F, A2], a3: ConfigValue[F, A3], a4: ConfigValue[F, A4], a5: ConfigValue[F, A5], a6: ConfigValue[F, A6], a7: ConfigValue[F, A7], a8: ConfigValue[F, A8])(f: (A1, A2, A3, A4, A5, A6, A7, A8) => F[Either[ConfigErrors, Z]]): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8).value.flatMap {
       case Left(errors) => left[Z](errors).pure[F]
       case Right(values) => f.tupled.apply(values)
     }

  /**
    * Loads a configuration using the 9 specified [[ConfigValue]]s.
    * Deals with error accumulation if there are any errors in the
    * provided [[ConfigValue]]s.
    *
    * @param a1 configuration value 1
    * @param a2 configuration value 2
    * @param a3 configuration value 3
    * @param a4 configuration value 4
    * @param a5 configuration value 5
    * @param a6 configuration value 6
    * @param a7 configuration value 7
    * @param a8 configuration value 8
    * @param a9 configuration value 9
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration value 1
    * @tparam A2 the type for configuration value 2
    * @tparam A3 the type for configuration value 3
    * @tparam A4 the type for configuration value 4
    * @tparam A5 the type for configuration value 5
    * @tparam A6 the type for configuration value 6
    * @tparam A7 the type for configuration value 7
    * @tparam A8 the type for configuration value 8
    * @tparam A9 the type for configuration value 9
    * @tparam F the [[ConfigValue]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[F[_]: Functor, A1, A2, A3, A4, A5, A6, A7, A8, A9, Z](a1: ConfigValue[F, A1], a2: ConfigValue[F, A2], a3: ConfigValue[F, A3], a4: ConfigValue[F, A4], a5: ConfigValue[F, A5], a6: ConfigValue[F, A6], a7: ConfigValue[F, A7], a8: ConfigValue[F, A8], a9: ConfigValue[F, A9])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9) => Z): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9).value.map(_.right.map(f.tupled))

  /**
    * Defines a requirement on 9 [[ConfigValue]]s in order to be
    * able to load a configuration. The method wraps `loadConfig`
    * methods, requiring the provided [[ConfigValue]]s to be
    * available in order to use the `loadConfig` methods.
    *
    * Deals with error accumulation if there are any errors in
    * the provided [[ConfigValue]]s.
    *
    * @param a1 configuration value 1
    * @param a2 configuration value 2
    * @param a3 configuration value 3
    * @param a4 configuration value 4
    * @param a5 configuration value 5
    * @param a6 configuration value 6
    * @param a7 configuration value 7
    * @param a8 configuration value 8
    * @param a9 configuration value 9
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration value 1
    * @tparam A2 the type for configuration value 2
    * @tparam A3 the type for configuration value 3
    * @tparam A4 the type for configuration value 4
    * @tparam A5 the type for configuration value 5
    * @tparam A6 the type for configuration value 6
    * @tparam A7 the type for configuration value 7
    * @tparam A8 the type for configuration value 8
    * @tparam A9 the type for configuration value 9
    * @tparam F the [[ConfigValue]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[F[_]: Monad, A1, A2, A3, A4, A5, A6, A7, A8, A9, Z](a1: ConfigValue[F, A1], a2: ConfigValue[F, A2], a3: ConfigValue[F, A3], a4: ConfigValue[F, A4], a5: ConfigValue[F, A5], a6: ConfigValue[F, A6], a7: ConfigValue[F, A7], a8: ConfigValue[F, A8], a9: ConfigValue[F, A9])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9) => F[Either[ConfigErrors, Z]]): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9).value.flatMap {
       case Left(errors) => left[Z](errors).pure[F]
       case Right(values) => f.tupled.apply(values)
     }

  /**
    * Loads a configuration using the 10 specified [[ConfigValue]]s.
    * Deals with error accumulation if there are any errors in the
    * provided [[ConfigValue]]s.
    *
    * @param a1 configuration value 1
    * @param a2 configuration value 2
    * @param a3 configuration value 3
    * @param a4 configuration value 4
    * @param a5 configuration value 5
    * @param a6 configuration value 6
    * @param a7 configuration value 7
    * @param a8 configuration value 8
    * @param a9 configuration value 9
    * @param a10 configuration value 10
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration value 1
    * @tparam A2 the type for configuration value 2
    * @tparam A3 the type for configuration value 3
    * @tparam A4 the type for configuration value 4
    * @tparam A5 the type for configuration value 5
    * @tparam A6 the type for configuration value 6
    * @tparam A7 the type for configuration value 7
    * @tparam A8 the type for configuration value 8
    * @tparam A9 the type for configuration value 9
    * @tparam A10 the type for configuration value 10
    * @tparam F the [[ConfigValue]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[F[_]: Functor, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, Z](a1: ConfigValue[F, A1], a2: ConfigValue[F, A2], a3: ConfigValue[F, A3], a4: ConfigValue[F, A4], a5: ConfigValue[F, A5], a6: ConfigValue[F, A6], a7: ConfigValue[F, A7], a8: ConfigValue[F, A8], a9: ConfigValue[F, A9], a10: ConfigValue[F, A10])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10) => Z): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10).value.map(_.right.map(f.tupled))

  /**
    * Defines a requirement on 10 [[ConfigValue]]s in order to be
    * able to load a configuration. The method wraps `loadConfig`
    * methods, requiring the provided [[ConfigValue]]s to be
    * available in order to use the `loadConfig` methods.
    *
    * Deals with error accumulation if there are any errors in
    * the provided [[ConfigValue]]s.
    *
    * @param a1 configuration value 1
    * @param a2 configuration value 2
    * @param a3 configuration value 3
    * @param a4 configuration value 4
    * @param a5 configuration value 5
    * @param a6 configuration value 6
    * @param a7 configuration value 7
    * @param a8 configuration value 8
    * @param a9 configuration value 9
    * @param a10 configuration value 10
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration value 1
    * @tparam A2 the type for configuration value 2
    * @tparam A3 the type for configuration value 3
    * @tparam A4 the type for configuration value 4
    * @tparam A5 the type for configuration value 5
    * @tparam A6 the type for configuration value 6
    * @tparam A7 the type for configuration value 7
    * @tparam A8 the type for configuration value 8
    * @tparam A9 the type for configuration value 9
    * @tparam A10 the type for configuration value 10
    * @tparam F the [[ConfigValue]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[F[_]: Monad, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, Z](a1: ConfigValue[F, A1], a2: ConfigValue[F, A2], a3: ConfigValue[F, A3], a4: ConfigValue[F, A4], a5: ConfigValue[F, A5], a6: ConfigValue[F, A6], a7: ConfigValue[F, A7], a8: ConfigValue[F, A8], a9: ConfigValue[F, A9], a10: ConfigValue[F, A10])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10) => F[Either[ConfigErrors, Z]]): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10).value.flatMap {
       case Left(errors) => left[Z](errors).pure[F]
       case Right(values) => f.tupled.apply(values)
     }

  /**
    * Loads a configuration using the 11 specified [[ConfigValue]]s.
    * Deals with error accumulation if there are any errors in the
    * provided [[ConfigValue]]s.
    *
    * @param a1 configuration value 1
    * @param a2 configuration value 2
    * @param a3 configuration value 3
    * @param a4 configuration value 4
    * @param a5 configuration value 5
    * @param a6 configuration value 6
    * @param a7 configuration value 7
    * @param a8 configuration value 8
    * @param a9 configuration value 9
    * @param a10 configuration value 10
    * @param a11 configuration value 11
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration value 1
    * @tparam A2 the type for configuration value 2
    * @tparam A3 the type for configuration value 3
    * @tparam A4 the type for configuration value 4
    * @tparam A5 the type for configuration value 5
    * @tparam A6 the type for configuration value 6
    * @tparam A7 the type for configuration value 7
    * @tparam A8 the type for configuration value 8
    * @tparam A9 the type for configuration value 9
    * @tparam A10 the type for configuration value 10
    * @tparam A11 the type for configuration value 11
    * @tparam F the [[ConfigValue]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[F[_]: Functor, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, Z](a1: ConfigValue[F, A1], a2: ConfigValue[F, A2], a3: ConfigValue[F, A3], a4: ConfigValue[F, A4], a5: ConfigValue[F, A5], a6: ConfigValue[F, A6], a7: ConfigValue[F, A7], a8: ConfigValue[F, A8], a9: ConfigValue[F, A9], a10: ConfigValue[F, A10], a11: ConfigValue[F, A11])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11) => Z): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11).value.map(_.right.map(f.tupled))

  /**
    * Defines a requirement on 11 [[ConfigValue]]s in order to be
    * able to load a configuration. The method wraps `loadConfig`
    * methods, requiring the provided [[ConfigValue]]s to be
    * available in order to use the `loadConfig` methods.
    *
    * Deals with error accumulation if there are any errors in
    * the provided [[ConfigValue]]s.
    *
    * @param a1 configuration value 1
    * @param a2 configuration value 2
    * @param a3 configuration value 3
    * @param a4 configuration value 4
    * @param a5 configuration value 5
    * @param a6 configuration value 6
    * @param a7 configuration value 7
    * @param a8 configuration value 8
    * @param a9 configuration value 9
    * @param a10 configuration value 10
    * @param a11 configuration value 11
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration value 1
    * @tparam A2 the type for configuration value 2
    * @tparam A3 the type for configuration value 3
    * @tparam A4 the type for configuration value 4
    * @tparam A5 the type for configuration value 5
    * @tparam A6 the type for configuration value 6
    * @tparam A7 the type for configuration value 7
    * @tparam A8 the type for configuration value 8
    * @tparam A9 the type for configuration value 9
    * @tparam A10 the type for configuration value 10
    * @tparam A11 the type for configuration value 11
    * @tparam F the [[ConfigValue]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[F[_]: Monad, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, Z](a1: ConfigValue[F, A1], a2: ConfigValue[F, A2], a3: ConfigValue[F, A3], a4: ConfigValue[F, A4], a5: ConfigValue[F, A5], a6: ConfigValue[F, A6], a7: ConfigValue[F, A7], a8: ConfigValue[F, A8], a9: ConfigValue[F, A9], a10: ConfigValue[F, A10], a11: ConfigValue[F, A11])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11) => F[Either[ConfigErrors, Z]]): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11).value.flatMap {
       case Left(errors) => left[Z](errors).pure[F]
       case Right(values) => f.tupled.apply(values)
     }

  /**
    * Loads a configuration using the 12 specified [[ConfigValue]]s.
    * Deals with error accumulation if there are any errors in the
    * provided [[ConfigValue]]s.
    *
    * @param a1 configuration value 1
    * @param a2 configuration value 2
    * @param a3 configuration value 3
    * @param a4 configuration value 4
    * @param a5 configuration value 5
    * @param a6 configuration value 6
    * @param a7 configuration value 7
    * @param a8 configuration value 8
    * @param a9 configuration value 9
    * @param a10 configuration value 10
    * @param a11 configuration value 11
    * @param a12 configuration value 12
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration value 1
    * @tparam A2 the type for configuration value 2
    * @tparam A3 the type for configuration value 3
    * @tparam A4 the type for configuration value 4
    * @tparam A5 the type for configuration value 5
    * @tparam A6 the type for configuration value 6
    * @tparam A7 the type for configuration value 7
    * @tparam A8 the type for configuration value 8
    * @tparam A9 the type for configuration value 9
    * @tparam A10 the type for configuration value 10
    * @tparam A11 the type for configuration value 11
    * @tparam A12 the type for configuration value 12
    * @tparam F the [[ConfigValue]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[F[_]: Functor, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, Z](a1: ConfigValue[F, A1], a2: ConfigValue[F, A2], a3: ConfigValue[F, A3], a4: ConfigValue[F, A4], a5: ConfigValue[F, A5], a6: ConfigValue[F, A6], a7: ConfigValue[F, A7], a8: ConfigValue[F, A8], a9: ConfigValue[F, A9], a10: ConfigValue[F, A10], a11: ConfigValue[F, A11], a12: ConfigValue[F, A12])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12) => Z): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12).value.map(_.right.map(f.tupled))

  /**
    * Defines a requirement on 12 [[ConfigValue]]s in order to be
    * able to load a configuration. The method wraps `loadConfig`
    * methods, requiring the provided [[ConfigValue]]s to be
    * available in order to use the `loadConfig` methods.
    *
    * Deals with error accumulation if there are any errors in
    * the provided [[ConfigValue]]s.
    *
    * @param a1 configuration value 1
    * @param a2 configuration value 2
    * @param a3 configuration value 3
    * @param a4 configuration value 4
    * @param a5 configuration value 5
    * @param a6 configuration value 6
    * @param a7 configuration value 7
    * @param a8 configuration value 8
    * @param a9 configuration value 9
    * @param a10 configuration value 10
    * @param a11 configuration value 11
    * @param a12 configuration value 12
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration value 1
    * @tparam A2 the type for configuration value 2
    * @tparam A3 the type for configuration value 3
    * @tparam A4 the type for configuration value 4
    * @tparam A5 the type for configuration value 5
    * @tparam A6 the type for configuration value 6
    * @tparam A7 the type for configuration value 7
    * @tparam A8 the type for configuration value 8
    * @tparam A9 the type for configuration value 9
    * @tparam A10 the type for configuration value 10
    * @tparam A11 the type for configuration value 11
    * @tparam A12 the type for configuration value 12
    * @tparam F the [[ConfigValue]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[F[_]: Monad, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, Z](a1: ConfigValue[F, A1], a2: ConfigValue[F, A2], a3: ConfigValue[F, A3], a4: ConfigValue[F, A4], a5: ConfigValue[F, A5], a6: ConfigValue[F, A6], a7: ConfigValue[F, A7], a8: ConfigValue[F, A8], a9: ConfigValue[F, A9], a10: ConfigValue[F, A10], a11: ConfigValue[F, A11], a12: ConfigValue[F, A12])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12) => F[Either[ConfigErrors, Z]]): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12).value.flatMap {
       case Left(errors) => left[Z](errors).pure[F]
       case Right(values) => f.tupled.apply(values)
     }

  /**
    * Loads a configuration using the 13 specified [[ConfigValue]]s.
    * Deals with error accumulation if there are any errors in the
    * provided [[ConfigValue]]s.
    *
    * @param a1 configuration value 1
    * @param a2 configuration value 2
    * @param a3 configuration value 3
    * @param a4 configuration value 4
    * @param a5 configuration value 5
    * @param a6 configuration value 6
    * @param a7 configuration value 7
    * @param a8 configuration value 8
    * @param a9 configuration value 9
    * @param a10 configuration value 10
    * @param a11 configuration value 11
    * @param a12 configuration value 12
    * @param a13 configuration value 13
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration value 1
    * @tparam A2 the type for configuration value 2
    * @tparam A3 the type for configuration value 3
    * @tparam A4 the type for configuration value 4
    * @tparam A5 the type for configuration value 5
    * @tparam A6 the type for configuration value 6
    * @tparam A7 the type for configuration value 7
    * @tparam A8 the type for configuration value 8
    * @tparam A9 the type for configuration value 9
    * @tparam A10 the type for configuration value 10
    * @tparam A11 the type for configuration value 11
    * @tparam A12 the type for configuration value 12
    * @tparam A13 the type for configuration value 13
    * @tparam F the [[ConfigValue]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[F[_]: Functor, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, Z](a1: ConfigValue[F, A1], a2: ConfigValue[F, A2], a3: ConfigValue[F, A3], a4: ConfigValue[F, A4], a5: ConfigValue[F, A5], a6: ConfigValue[F, A6], a7: ConfigValue[F, A7], a8: ConfigValue[F, A8], a9: ConfigValue[F, A9], a10: ConfigValue[F, A10], a11: ConfigValue[F, A11], a12: ConfigValue[F, A12], a13: ConfigValue[F, A13])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13) => Z): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13).value.map(_.right.map(f.tupled))

  /**
    * Defines a requirement on 13 [[ConfigValue]]s in order to be
    * able to load a configuration. The method wraps `loadConfig`
    * methods, requiring the provided [[ConfigValue]]s to be
    * available in order to use the `loadConfig` methods.
    *
    * Deals with error accumulation if there are any errors in
    * the provided [[ConfigValue]]s.
    *
    * @param a1 configuration value 1
    * @param a2 configuration value 2
    * @param a3 configuration value 3
    * @param a4 configuration value 4
    * @param a5 configuration value 5
    * @param a6 configuration value 6
    * @param a7 configuration value 7
    * @param a8 configuration value 8
    * @param a9 configuration value 9
    * @param a10 configuration value 10
    * @param a11 configuration value 11
    * @param a12 configuration value 12
    * @param a13 configuration value 13
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration value 1
    * @tparam A2 the type for configuration value 2
    * @tparam A3 the type for configuration value 3
    * @tparam A4 the type for configuration value 4
    * @tparam A5 the type for configuration value 5
    * @tparam A6 the type for configuration value 6
    * @tparam A7 the type for configuration value 7
    * @tparam A8 the type for configuration value 8
    * @tparam A9 the type for configuration value 9
    * @tparam A10 the type for configuration value 10
    * @tparam A11 the type for configuration value 11
    * @tparam A12 the type for configuration value 12
    * @tparam A13 the type for configuration value 13
    * @tparam F the [[ConfigValue]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[F[_]: Monad, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, Z](a1: ConfigValue[F, A1], a2: ConfigValue[F, A2], a3: ConfigValue[F, A3], a4: ConfigValue[F, A4], a5: ConfigValue[F, A5], a6: ConfigValue[F, A6], a7: ConfigValue[F, A7], a8: ConfigValue[F, A8], a9: ConfigValue[F, A9], a10: ConfigValue[F, A10], a11: ConfigValue[F, A11], a12: ConfigValue[F, A12], a13: ConfigValue[F, A13])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13) => F[Either[ConfigErrors, Z]]): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13).value.flatMap {
       case Left(errors) => left[Z](errors).pure[F]
       case Right(values) => f.tupled.apply(values)
     }

  /**
    * Loads a configuration using the 14 specified [[ConfigValue]]s.
    * Deals with error accumulation if there are any errors in the
    * provided [[ConfigValue]]s.
    *
    * @param a1 configuration value 1
    * @param a2 configuration value 2
    * @param a3 configuration value 3
    * @param a4 configuration value 4
    * @param a5 configuration value 5
    * @param a6 configuration value 6
    * @param a7 configuration value 7
    * @param a8 configuration value 8
    * @param a9 configuration value 9
    * @param a10 configuration value 10
    * @param a11 configuration value 11
    * @param a12 configuration value 12
    * @param a13 configuration value 13
    * @param a14 configuration value 14
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration value 1
    * @tparam A2 the type for configuration value 2
    * @tparam A3 the type for configuration value 3
    * @tparam A4 the type for configuration value 4
    * @tparam A5 the type for configuration value 5
    * @tparam A6 the type for configuration value 6
    * @tparam A7 the type for configuration value 7
    * @tparam A8 the type for configuration value 8
    * @tparam A9 the type for configuration value 9
    * @tparam A10 the type for configuration value 10
    * @tparam A11 the type for configuration value 11
    * @tparam A12 the type for configuration value 12
    * @tparam A13 the type for configuration value 13
    * @tparam A14 the type for configuration value 14
    * @tparam F the [[ConfigValue]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[F[_]: Functor, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, Z](a1: ConfigValue[F, A1], a2: ConfigValue[F, A2], a3: ConfigValue[F, A3], a4: ConfigValue[F, A4], a5: ConfigValue[F, A5], a6: ConfigValue[F, A6], a7: ConfigValue[F, A7], a8: ConfigValue[F, A8], a9: ConfigValue[F, A9], a10: ConfigValue[F, A10], a11: ConfigValue[F, A11], a12: ConfigValue[F, A12], a13: ConfigValue[F, A13], a14: ConfigValue[F, A14])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14) => Z): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14).value.map(_.right.map(f.tupled))

  /**
    * Defines a requirement on 14 [[ConfigValue]]s in order to be
    * able to load a configuration. The method wraps `loadConfig`
    * methods, requiring the provided [[ConfigValue]]s to be
    * available in order to use the `loadConfig` methods.
    *
    * Deals with error accumulation if there are any errors in
    * the provided [[ConfigValue]]s.
    *
    * @param a1 configuration value 1
    * @param a2 configuration value 2
    * @param a3 configuration value 3
    * @param a4 configuration value 4
    * @param a5 configuration value 5
    * @param a6 configuration value 6
    * @param a7 configuration value 7
    * @param a8 configuration value 8
    * @param a9 configuration value 9
    * @param a10 configuration value 10
    * @param a11 configuration value 11
    * @param a12 configuration value 12
    * @param a13 configuration value 13
    * @param a14 configuration value 14
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration value 1
    * @tparam A2 the type for configuration value 2
    * @tparam A3 the type for configuration value 3
    * @tparam A4 the type for configuration value 4
    * @tparam A5 the type for configuration value 5
    * @tparam A6 the type for configuration value 6
    * @tparam A7 the type for configuration value 7
    * @tparam A8 the type for configuration value 8
    * @tparam A9 the type for configuration value 9
    * @tparam A10 the type for configuration value 10
    * @tparam A11 the type for configuration value 11
    * @tparam A12 the type for configuration value 12
    * @tparam A13 the type for configuration value 13
    * @tparam A14 the type for configuration value 14
    * @tparam F the [[ConfigValue]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[F[_]: Monad, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, Z](a1: ConfigValue[F, A1], a2: ConfigValue[F, A2], a3: ConfigValue[F, A3], a4: ConfigValue[F, A4], a5: ConfigValue[F, A5], a6: ConfigValue[F, A6], a7: ConfigValue[F, A7], a8: ConfigValue[F, A8], a9: ConfigValue[F, A9], a10: ConfigValue[F, A10], a11: ConfigValue[F, A11], a12: ConfigValue[F, A12], a13: ConfigValue[F, A13], a14: ConfigValue[F, A14])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14) => F[Either[ConfigErrors, Z]]): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14).value.flatMap {
       case Left(errors) => left[Z](errors).pure[F]
       case Right(values) => f.tupled.apply(values)
     }

  /**
    * Loads a configuration using the 15 specified [[ConfigValue]]s.
    * Deals with error accumulation if there are any errors in the
    * provided [[ConfigValue]]s.
    *
    * @param a1 configuration value 1
    * @param a2 configuration value 2
    * @param a3 configuration value 3
    * @param a4 configuration value 4
    * @param a5 configuration value 5
    * @param a6 configuration value 6
    * @param a7 configuration value 7
    * @param a8 configuration value 8
    * @param a9 configuration value 9
    * @param a10 configuration value 10
    * @param a11 configuration value 11
    * @param a12 configuration value 12
    * @param a13 configuration value 13
    * @param a14 configuration value 14
    * @param a15 configuration value 15
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration value 1
    * @tparam A2 the type for configuration value 2
    * @tparam A3 the type for configuration value 3
    * @tparam A4 the type for configuration value 4
    * @tparam A5 the type for configuration value 5
    * @tparam A6 the type for configuration value 6
    * @tparam A7 the type for configuration value 7
    * @tparam A8 the type for configuration value 8
    * @tparam A9 the type for configuration value 9
    * @tparam A10 the type for configuration value 10
    * @tparam A11 the type for configuration value 11
    * @tparam A12 the type for configuration value 12
    * @tparam A13 the type for configuration value 13
    * @tparam A14 the type for configuration value 14
    * @tparam A15 the type for configuration value 15
    * @tparam F the [[ConfigValue]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[F[_]: Functor, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, Z](a1: ConfigValue[F, A1], a2: ConfigValue[F, A2], a3: ConfigValue[F, A3], a4: ConfigValue[F, A4], a5: ConfigValue[F, A5], a6: ConfigValue[F, A6], a7: ConfigValue[F, A7], a8: ConfigValue[F, A8], a9: ConfigValue[F, A9], a10: ConfigValue[F, A10], a11: ConfigValue[F, A11], a12: ConfigValue[F, A12], a13: ConfigValue[F, A13], a14: ConfigValue[F, A14], a15: ConfigValue[F, A15])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15) => Z): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14 append a15).value.map(_.right.map(f.tupled))

  /**
    * Defines a requirement on 15 [[ConfigValue]]s in order to be
    * able to load a configuration. The method wraps `loadConfig`
    * methods, requiring the provided [[ConfigValue]]s to be
    * available in order to use the `loadConfig` methods.
    *
    * Deals with error accumulation if there are any errors in
    * the provided [[ConfigValue]]s.
    *
    * @param a1 configuration value 1
    * @param a2 configuration value 2
    * @param a3 configuration value 3
    * @param a4 configuration value 4
    * @param a5 configuration value 5
    * @param a6 configuration value 6
    * @param a7 configuration value 7
    * @param a8 configuration value 8
    * @param a9 configuration value 9
    * @param a10 configuration value 10
    * @param a11 configuration value 11
    * @param a12 configuration value 12
    * @param a13 configuration value 13
    * @param a14 configuration value 14
    * @param a15 configuration value 15
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration value 1
    * @tparam A2 the type for configuration value 2
    * @tparam A3 the type for configuration value 3
    * @tparam A4 the type for configuration value 4
    * @tparam A5 the type for configuration value 5
    * @tparam A6 the type for configuration value 6
    * @tparam A7 the type for configuration value 7
    * @tparam A8 the type for configuration value 8
    * @tparam A9 the type for configuration value 9
    * @tparam A10 the type for configuration value 10
    * @tparam A11 the type for configuration value 11
    * @tparam A12 the type for configuration value 12
    * @tparam A13 the type for configuration value 13
    * @tparam A14 the type for configuration value 14
    * @tparam A15 the type for configuration value 15
    * @tparam F the [[ConfigValue]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[F[_]: Monad, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, Z](a1: ConfigValue[F, A1], a2: ConfigValue[F, A2], a3: ConfigValue[F, A3], a4: ConfigValue[F, A4], a5: ConfigValue[F, A5], a6: ConfigValue[F, A6], a7: ConfigValue[F, A7], a8: ConfigValue[F, A8], a9: ConfigValue[F, A9], a10: ConfigValue[F, A10], a11: ConfigValue[F, A11], a12: ConfigValue[F, A12], a13: ConfigValue[F, A13], a14: ConfigValue[F, A14], a15: ConfigValue[F, A15])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15) => F[Either[ConfigErrors, Z]]): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14 append a15).value.flatMap {
       case Left(errors) => left[Z](errors).pure[F]
       case Right(values) => f.tupled.apply(values)
     }

  /**
    * Loads a configuration using the 16 specified [[ConfigValue]]s.
    * Deals with error accumulation if there are any errors in the
    * provided [[ConfigValue]]s.
    *
    * @param a1 configuration value 1
    * @param a2 configuration value 2
    * @param a3 configuration value 3
    * @param a4 configuration value 4
    * @param a5 configuration value 5
    * @param a6 configuration value 6
    * @param a7 configuration value 7
    * @param a8 configuration value 8
    * @param a9 configuration value 9
    * @param a10 configuration value 10
    * @param a11 configuration value 11
    * @param a12 configuration value 12
    * @param a13 configuration value 13
    * @param a14 configuration value 14
    * @param a15 configuration value 15
    * @param a16 configuration value 16
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration value 1
    * @tparam A2 the type for configuration value 2
    * @tparam A3 the type for configuration value 3
    * @tparam A4 the type for configuration value 4
    * @tparam A5 the type for configuration value 5
    * @tparam A6 the type for configuration value 6
    * @tparam A7 the type for configuration value 7
    * @tparam A8 the type for configuration value 8
    * @tparam A9 the type for configuration value 9
    * @tparam A10 the type for configuration value 10
    * @tparam A11 the type for configuration value 11
    * @tparam A12 the type for configuration value 12
    * @tparam A13 the type for configuration value 13
    * @tparam A14 the type for configuration value 14
    * @tparam A15 the type for configuration value 15
    * @tparam A16 the type for configuration value 16
    * @tparam F the [[ConfigValue]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[F[_]: Functor, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, Z](a1: ConfigValue[F, A1], a2: ConfigValue[F, A2], a3: ConfigValue[F, A3], a4: ConfigValue[F, A4], a5: ConfigValue[F, A5], a6: ConfigValue[F, A6], a7: ConfigValue[F, A7], a8: ConfigValue[F, A8], a9: ConfigValue[F, A9], a10: ConfigValue[F, A10], a11: ConfigValue[F, A11], a12: ConfigValue[F, A12], a13: ConfigValue[F, A13], a14: ConfigValue[F, A14], a15: ConfigValue[F, A15], a16: ConfigValue[F, A16])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16) => Z): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14 append a15 append a16).value.map(_.right.map(f.tupled))

  /**
    * Defines a requirement on 16 [[ConfigValue]]s in order to be
    * able to load a configuration. The method wraps `loadConfig`
    * methods, requiring the provided [[ConfigValue]]s to be
    * available in order to use the `loadConfig` methods.
    *
    * Deals with error accumulation if there are any errors in
    * the provided [[ConfigValue]]s.
    *
    * @param a1 configuration value 1
    * @param a2 configuration value 2
    * @param a3 configuration value 3
    * @param a4 configuration value 4
    * @param a5 configuration value 5
    * @param a6 configuration value 6
    * @param a7 configuration value 7
    * @param a8 configuration value 8
    * @param a9 configuration value 9
    * @param a10 configuration value 10
    * @param a11 configuration value 11
    * @param a12 configuration value 12
    * @param a13 configuration value 13
    * @param a14 configuration value 14
    * @param a15 configuration value 15
    * @param a16 configuration value 16
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration value 1
    * @tparam A2 the type for configuration value 2
    * @tparam A3 the type for configuration value 3
    * @tparam A4 the type for configuration value 4
    * @tparam A5 the type for configuration value 5
    * @tparam A6 the type for configuration value 6
    * @tparam A7 the type for configuration value 7
    * @tparam A8 the type for configuration value 8
    * @tparam A9 the type for configuration value 9
    * @tparam A10 the type for configuration value 10
    * @tparam A11 the type for configuration value 11
    * @tparam A12 the type for configuration value 12
    * @tparam A13 the type for configuration value 13
    * @tparam A14 the type for configuration value 14
    * @tparam A15 the type for configuration value 15
    * @tparam A16 the type for configuration value 16
    * @tparam F the [[ConfigValue]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[F[_]: Monad, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, Z](a1: ConfigValue[F, A1], a2: ConfigValue[F, A2], a3: ConfigValue[F, A3], a4: ConfigValue[F, A4], a5: ConfigValue[F, A5], a6: ConfigValue[F, A6], a7: ConfigValue[F, A7], a8: ConfigValue[F, A8], a9: ConfigValue[F, A9], a10: ConfigValue[F, A10], a11: ConfigValue[F, A11], a12: ConfigValue[F, A12], a13: ConfigValue[F, A13], a14: ConfigValue[F, A14], a15: ConfigValue[F, A15], a16: ConfigValue[F, A16])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16) => F[Either[ConfigErrors, Z]]): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14 append a15 append a16).value.flatMap {
       case Left(errors) => left[Z](errors).pure[F]
       case Right(values) => f.tupled.apply(values)
     }

  /**
    * Loads a configuration using the 17 specified [[ConfigValue]]s.
    * Deals with error accumulation if there are any errors in the
    * provided [[ConfigValue]]s.
    *
    * @param a1 configuration value 1
    * @param a2 configuration value 2
    * @param a3 configuration value 3
    * @param a4 configuration value 4
    * @param a5 configuration value 5
    * @param a6 configuration value 6
    * @param a7 configuration value 7
    * @param a8 configuration value 8
    * @param a9 configuration value 9
    * @param a10 configuration value 10
    * @param a11 configuration value 11
    * @param a12 configuration value 12
    * @param a13 configuration value 13
    * @param a14 configuration value 14
    * @param a15 configuration value 15
    * @param a16 configuration value 16
    * @param a17 configuration value 17
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration value 1
    * @tparam A2 the type for configuration value 2
    * @tparam A3 the type for configuration value 3
    * @tparam A4 the type for configuration value 4
    * @tparam A5 the type for configuration value 5
    * @tparam A6 the type for configuration value 6
    * @tparam A7 the type for configuration value 7
    * @tparam A8 the type for configuration value 8
    * @tparam A9 the type for configuration value 9
    * @tparam A10 the type for configuration value 10
    * @tparam A11 the type for configuration value 11
    * @tparam A12 the type for configuration value 12
    * @tparam A13 the type for configuration value 13
    * @tparam A14 the type for configuration value 14
    * @tparam A15 the type for configuration value 15
    * @tparam A16 the type for configuration value 16
    * @tparam A17 the type for configuration value 17
    * @tparam F the [[ConfigValue]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[F[_]: Functor, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, Z](a1: ConfigValue[F, A1], a2: ConfigValue[F, A2], a3: ConfigValue[F, A3], a4: ConfigValue[F, A4], a5: ConfigValue[F, A5], a6: ConfigValue[F, A6], a7: ConfigValue[F, A7], a8: ConfigValue[F, A8], a9: ConfigValue[F, A9], a10: ConfigValue[F, A10], a11: ConfigValue[F, A11], a12: ConfigValue[F, A12], a13: ConfigValue[F, A13], a14: ConfigValue[F, A14], a15: ConfigValue[F, A15], a16: ConfigValue[F, A16], a17: ConfigValue[F, A17])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17) => Z): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14 append a15 append a16 append a17).value.map(_.right.map(f.tupled))

  /**
    * Defines a requirement on 17 [[ConfigValue]]s in order to be
    * able to load a configuration. The method wraps `loadConfig`
    * methods, requiring the provided [[ConfigValue]]s to be
    * available in order to use the `loadConfig` methods.
    *
    * Deals with error accumulation if there are any errors in
    * the provided [[ConfigValue]]s.
    *
    * @param a1 configuration value 1
    * @param a2 configuration value 2
    * @param a3 configuration value 3
    * @param a4 configuration value 4
    * @param a5 configuration value 5
    * @param a6 configuration value 6
    * @param a7 configuration value 7
    * @param a8 configuration value 8
    * @param a9 configuration value 9
    * @param a10 configuration value 10
    * @param a11 configuration value 11
    * @param a12 configuration value 12
    * @param a13 configuration value 13
    * @param a14 configuration value 14
    * @param a15 configuration value 15
    * @param a16 configuration value 16
    * @param a17 configuration value 17
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration value 1
    * @tparam A2 the type for configuration value 2
    * @tparam A3 the type for configuration value 3
    * @tparam A4 the type for configuration value 4
    * @tparam A5 the type for configuration value 5
    * @tparam A6 the type for configuration value 6
    * @tparam A7 the type for configuration value 7
    * @tparam A8 the type for configuration value 8
    * @tparam A9 the type for configuration value 9
    * @tparam A10 the type for configuration value 10
    * @tparam A11 the type for configuration value 11
    * @tparam A12 the type for configuration value 12
    * @tparam A13 the type for configuration value 13
    * @tparam A14 the type for configuration value 14
    * @tparam A15 the type for configuration value 15
    * @tparam A16 the type for configuration value 16
    * @tparam A17 the type for configuration value 17
    * @tparam F the [[ConfigValue]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[F[_]: Monad, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, Z](a1: ConfigValue[F, A1], a2: ConfigValue[F, A2], a3: ConfigValue[F, A3], a4: ConfigValue[F, A4], a5: ConfigValue[F, A5], a6: ConfigValue[F, A6], a7: ConfigValue[F, A7], a8: ConfigValue[F, A8], a9: ConfigValue[F, A9], a10: ConfigValue[F, A10], a11: ConfigValue[F, A11], a12: ConfigValue[F, A12], a13: ConfigValue[F, A13], a14: ConfigValue[F, A14], a15: ConfigValue[F, A15], a16: ConfigValue[F, A16], a17: ConfigValue[F, A17])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17) => F[Either[ConfigErrors, Z]]): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14 append a15 append a16 append a17).value.flatMap {
       case Left(errors) => left[Z](errors).pure[F]
       case Right(values) => f.tupled.apply(values)
     }

  /**
    * Loads a configuration using the 18 specified [[ConfigValue]]s.
    * Deals with error accumulation if there are any errors in the
    * provided [[ConfigValue]]s.
    *
    * @param a1 configuration value 1
    * @param a2 configuration value 2
    * @param a3 configuration value 3
    * @param a4 configuration value 4
    * @param a5 configuration value 5
    * @param a6 configuration value 6
    * @param a7 configuration value 7
    * @param a8 configuration value 8
    * @param a9 configuration value 9
    * @param a10 configuration value 10
    * @param a11 configuration value 11
    * @param a12 configuration value 12
    * @param a13 configuration value 13
    * @param a14 configuration value 14
    * @param a15 configuration value 15
    * @param a16 configuration value 16
    * @param a17 configuration value 17
    * @param a18 configuration value 18
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration value 1
    * @tparam A2 the type for configuration value 2
    * @tparam A3 the type for configuration value 3
    * @tparam A4 the type for configuration value 4
    * @tparam A5 the type for configuration value 5
    * @tparam A6 the type for configuration value 6
    * @tparam A7 the type for configuration value 7
    * @tparam A8 the type for configuration value 8
    * @tparam A9 the type for configuration value 9
    * @tparam A10 the type for configuration value 10
    * @tparam A11 the type for configuration value 11
    * @tparam A12 the type for configuration value 12
    * @tparam A13 the type for configuration value 13
    * @tparam A14 the type for configuration value 14
    * @tparam A15 the type for configuration value 15
    * @tparam A16 the type for configuration value 16
    * @tparam A17 the type for configuration value 17
    * @tparam A18 the type for configuration value 18
    * @tparam F the [[ConfigValue]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[F[_]: Functor, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, Z](a1: ConfigValue[F, A1], a2: ConfigValue[F, A2], a3: ConfigValue[F, A3], a4: ConfigValue[F, A4], a5: ConfigValue[F, A5], a6: ConfigValue[F, A6], a7: ConfigValue[F, A7], a8: ConfigValue[F, A8], a9: ConfigValue[F, A9], a10: ConfigValue[F, A10], a11: ConfigValue[F, A11], a12: ConfigValue[F, A12], a13: ConfigValue[F, A13], a14: ConfigValue[F, A14], a15: ConfigValue[F, A15], a16: ConfigValue[F, A16], a17: ConfigValue[F, A17], a18: ConfigValue[F, A18])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18) => Z): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14 append a15 append a16 append a17 append a18).value.map(_.right.map(f.tupled))

  /**
    * Defines a requirement on 18 [[ConfigValue]]s in order to be
    * able to load a configuration. The method wraps `loadConfig`
    * methods, requiring the provided [[ConfigValue]]s to be
    * available in order to use the `loadConfig` methods.
    *
    * Deals with error accumulation if there are any errors in
    * the provided [[ConfigValue]]s.
    *
    * @param a1 configuration value 1
    * @param a2 configuration value 2
    * @param a3 configuration value 3
    * @param a4 configuration value 4
    * @param a5 configuration value 5
    * @param a6 configuration value 6
    * @param a7 configuration value 7
    * @param a8 configuration value 8
    * @param a9 configuration value 9
    * @param a10 configuration value 10
    * @param a11 configuration value 11
    * @param a12 configuration value 12
    * @param a13 configuration value 13
    * @param a14 configuration value 14
    * @param a15 configuration value 15
    * @param a16 configuration value 16
    * @param a17 configuration value 17
    * @param a18 configuration value 18
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration value 1
    * @tparam A2 the type for configuration value 2
    * @tparam A3 the type for configuration value 3
    * @tparam A4 the type for configuration value 4
    * @tparam A5 the type for configuration value 5
    * @tparam A6 the type for configuration value 6
    * @tparam A7 the type for configuration value 7
    * @tparam A8 the type for configuration value 8
    * @tparam A9 the type for configuration value 9
    * @tparam A10 the type for configuration value 10
    * @tparam A11 the type for configuration value 11
    * @tparam A12 the type for configuration value 12
    * @tparam A13 the type for configuration value 13
    * @tparam A14 the type for configuration value 14
    * @tparam A15 the type for configuration value 15
    * @tparam A16 the type for configuration value 16
    * @tparam A17 the type for configuration value 17
    * @tparam A18 the type for configuration value 18
    * @tparam F the [[ConfigValue]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[F[_]: Monad, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, Z](a1: ConfigValue[F, A1], a2: ConfigValue[F, A2], a3: ConfigValue[F, A3], a4: ConfigValue[F, A4], a5: ConfigValue[F, A5], a6: ConfigValue[F, A6], a7: ConfigValue[F, A7], a8: ConfigValue[F, A8], a9: ConfigValue[F, A9], a10: ConfigValue[F, A10], a11: ConfigValue[F, A11], a12: ConfigValue[F, A12], a13: ConfigValue[F, A13], a14: ConfigValue[F, A14], a15: ConfigValue[F, A15], a16: ConfigValue[F, A16], a17: ConfigValue[F, A17], a18: ConfigValue[F, A18])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18) => F[Either[ConfigErrors, Z]]): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14 append a15 append a16 append a17 append a18).value.flatMap {
       case Left(errors) => left[Z](errors).pure[F]
       case Right(values) => f.tupled.apply(values)
     }

  /**
    * Loads a configuration using the 19 specified [[ConfigValue]]s.
    * Deals with error accumulation if there are any errors in the
    * provided [[ConfigValue]]s.
    *
    * @param a1 configuration value 1
    * @param a2 configuration value 2
    * @param a3 configuration value 3
    * @param a4 configuration value 4
    * @param a5 configuration value 5
    * @param a6 configuration value 6
    * @param a7 configuration value 7
    * @param a8 configuration value 8
    * @param a9 configuration value 9
    * @param a10 configuration value 10
    * @param a11 configuration value 11
    * @param a12 configuration value 12
    * @param a13 configuration value 13
    * @param a14 configuration value 14
    * @param a15 configuration value 15
    * @param a16 configuration value 16
    * @param a17 configuration value 17
    * @param a18 configuration value 18
    * @param a19 configuration value 19
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration value 1
    * @tparam A2 the type for configuration value 2
    * @tparam A3 the type for configuration value 3
    * @tparam A4 the type for configuration value 4
    * @tparam A5 the type for configuration value 5
    * @tparam A6 the type for configuration value 6
    * @tparam A7 the type for configuration value 7
    * @tparam A8 the type for configuration value 8
    * @tparam A9 the type for configuration value 9
    * @tparam A10 the type for configuration value 10
    * @tparam A11 the type for configuration value 11
    * @tparam A12 the type for configuration value 12
    * @tparam A13 the type for configuration value 13
    * @tparam A14 the type for configuration value 14
    * @tparam A15 the type for configuration value 15
    * @tparam A16 the type for configuration value 16
    * @tparam A17 the type for configuration value 17
    * @tparam A18 the type for configuration value 18
    * @tparam A19 the type for configuration value 19
    * @tparam F the [[ConfigValue]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[F[_]: Functor, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, Z](a1: ConfigValue[F, A1], a2: ConfigValue[F, A2], a3: ConfigValue[F, A3], a4: ConfigValue[F, A4], a5: ConfigValue[F, A5], a6: ConfigValue[F, A6], a7: ConfigValue[F, A7], a8: ConfigValue[F, A8], a9: ConfigValue[F, A9], a10: ConfigValue[F, A10], a11: ConfigValue[F, A11], a12: ConfigValue[F, A12], a13: ConfigValue[F, A13], a14: ConfigValue[F, A14], a15: ConfigValue[F, A15], a16: ConfigValue[F, A16], a17: ConfigValue[F, A17], a18: ConfigValue[F, A18], a19: ConfigValue[F, A19])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19) => Z): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14 append a15 append a16 append a17 append a18 append a19).value.map(_.right.map(f.tupled))

  /**
    * Defines a requirement on 19 [[ConfigValue]]s in order to be
    * able to load a configuration. The method wraps `loadConfig`
    * methods, requiring the provided [[ConfigValue]]s to be
    * available in order to use the `loadConfig` methods.
    *
    * Deals with error accumulation if there are any errors in
    * the provided [[ConfigValue]]s.
    *
    * @param a1 configuration value 1
    * @param a2 configuration value 2
    * @param a3 configuration value 3
    * @param a4 configuration value 4
    * @param a5 configuration value 5
    * @param a6 configuration value 6
    * @param a7 configuration value 7
    * @param a8 configuration value 8
    * @param a9 configuration value 9
    * @param a10 configuration value 10
    * @param a11 configuration value 11
    * @param a12 configuration value 12
    * @param a13 configuration value 13
    * @param a14 configuration value 14
    * @param a15 configuration value 15
    * @param a16 configuration value 16
    * @param a17 configuration value 17
    * @param a18 configuration value 18
    * @param a19 configuration value 19
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration value 1
    * @tparam A2 the type for configuration value 2
    * @tparam A3 the type for configuration value 3
    * @tparam A4 the type for configuration value 4
    * @tparam A5 the type for configuration value 5
    * @tparam A6 the type for configuration value 6
    * @tparam A7 the type for configuration value 7
    * @tparam A8 the type for configuration value 8
    * @tparam A9 the type for configuration value 9
    * @tparam A10 the type for configuration value 10
    * @tparam A11 the type for configuration value 11
    * @tparam A12 the type for configuration value 12
    * @tparam A13 the type for configuration value 13
    * @tparam A14 the type for configuration value 14
    * @tparam A15 the type for configuration value 15
    * @tparam A16 the type for configuration value 16
    * @tparam A17 the type for configuration value 17
    * @tparam A18 the type for configuration value 18
    * @tparam A19 the type for configuration value 19
    * @tparam F the [[ConfigValue]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[F[_]: Monad, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, Z](a1: ConfigValue[F, A1], a2: ConfigValue[F, A2], a3: ConfigValue[F, A3], a4: ConfigValue[F, A4], a5: ConfigValue[F, A5], a6: ConfigValue[F, A6], a7: ConfigValue[F, A7], a8: ConfigValue[F, A8], a9: ConfigValue[F, A9], a10: ConfigValue[F, A10], a11: ConfigValue[F, A11], a12: ConfigValue[F, A12], a13: ConfigValue[F, A13], a14: ConfigValue[F, A14], a15: ConfigValue[F, A15], a16: ConfigValue[F, A16], a17: ConfigValue[F, A17], a18: ConfigValue[F, A18], a19: ConfigValue[F, A19])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19) => F[Either[ConfigErrors, Z]]): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14 append a15 append a16 append a17 append a18 append a19).value.flatMap {
       case Left(errors) => left[Z](errors).pure[F]
       case Right(values) => f.tupled.apply(values)
     }

  /**
    * Loads a configuration using the 20 specified [[ConfigValue]]s.
    * Deals with error accumulation if there are any errors in the
    * provided [[ConfigValue]]s.
    *
    * @param a1 configuration value 1
    * @param a2 configuration value 2
    * @param a3 configuration value 3
    * @param a4 configuration value 4
    * @param a5 configuration value 5
    * @param a6 configuration value 6
    * @param a7 configuration value 7
    * @param a8 configuration value 8
    * @param a9 configuration value 9
    * @param a10 configuration value 10
    * @param a11 configuration value 11
    * @param a12 configuration value 12
    * @param a13 configuration value 13
    * @param a14 configuration value 14
    * @param a15 configuration value 15
    * @param a16 configuration value 16
    * @param a17 configuration value 17
    * @param a18 configuration value 18
    * @param a19 configuration value 19
    * @param a20 configuration value 20
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration value 1
    * @tparam A2 the type for configuration value 2
    * @tparam A3 the type for configuration value 3
    * @tparam A4 the type for configuration value 4
    * @tparam A5 the type for configuration value 5
    * @tparam A6 the type for configuration value 6
    * @tparam A7 the type for configuration value 7
    * @tparam A8 the type for configuration value 8
    * @tparam A9 the type for configuration value 9
    * @tparam A10 the type for configuration value 10
    * @tparam A11 the type for configuration value 11
    * @tparam A12 the type for configuration value 12
    * @tparam A13 the type for configuration value 13
    * @tparam A14 the type for configuration value 14
    * @tparam A15 the type for configuration value 15
    * @tparam A16 the type for configuration value 16
    * @tparam A17 the type for configuration value 17
    * @tparam A18 the type for configuration value 18
    * @tparam A19 the type for configuration value 19
    * @tparam A20 the type for configuration value 20
    * @tparam F the [[ConfigValue]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[F[_]: Functor, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, Z](a1: ConfigValue[F, A1], a2: ConfigValue[F, A2], a3: ConfigValue[F, A3], a4: ConfigValue[F, A4], a5: ConfigValue[F, A5], a6: ConfigValue[F, A6], a7: ConfigValue[F, A7], a8: ConfigValue[F, A8], a9: ConfigValue[F, A9], a10: ConfigValue[F, A10], a11: ConfigValue[F, A11], a12: ConfigValue[F, A12], a13: ConfigValue[F, A13], a14: ConfigValue[F, A14], a15: ConfigValue[F, A15], a16: ConfigValue[F, A16], a17: ConfigValue[F, A17], a18: ConfigValue[F, A18], a19: ConfigValue[F, A19], a20: ConfigValue[F, A20])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20) => Z): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14 append a15 append a16 append a17 append a18 append a19 append a20).value.map(_.right.map(f.tupled))

  /**
    * Defines a requirement on 20 [[ConfigValue]]s in order to be
    * able to load a configuration. The method wraps `loadConfig`
    * methods, requiring the provided [[ConfigValue]]s to be
    * available in order to use the `loadConfig` methods.
    *
    * Deals with error accumulation if there are any errors in
    * the provided [[ConfigValue]]s.
    *
    * @param a1 configuration value 1
    * @param a2 configuration value 2
    * @param a3 configuration value 3
    * @param a4 configuration value 4
    * @param a5 configuration value 5
    * @param a6 configuration value 6
    * @param a7 configuration value 7
    * @param a8 configuration value 8
    * @param a9 configuration value 9
    * @param a10 configuration value 10
    * @param a11 configuration value 11
    * @param a12 configuration value 12
    * @param a13 configuration value 13
    * @param a14 configuration value 14
    * @param a15 configuration value 15
    * @param a16 configuration value 16
    * @param a17 configuration value 17
    * @param a18 configuration value 18
    * @param a19 configuration value 19
    * @param a20 configuration value 20
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration value 1
    * @tparam A2 the type for configuration value 2
    * @tparam A3 the type for configuration value 3
    * @tparam A4 the type for configuration value 4
    * @tparam A5 the type for configuration value 5
    * @tparam A6 the type for configuration value 6
    * @tparam A7 the type for configuration value 7
    * @tparam A8 the type for configuration value 8
    * @tparam A9 the type for configuration value 9
    * @tparam A10 the type for configuration value 10
    * @tparam A11 the type for configuration value 11
    * @tparam A12 the type for configuration value 12
    * @tparam A13 the type for configuration value 13
    * @tparam A14 the type for configuration value 14
    * @tparam A15 the type for configuration value 15
    * @tparam A16 the type for configuration value 16
    * @tparam A17 the type for configuration value 17
    * @tparam A18 the type for configuration value 18
    * @tparam A19 the type for configuration value 19
    * @tparam A20 the type for configuration value 20
    * @tparam F the [[ConfigValue]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[F[_]: Monad, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, Z](a1: ConfigValue[F, A1], a2: ConfigValue[F, A2], a3: ConfigValue[F, A3], a4: ConfigValue[F, A4], a5: ConfigValue[F, A5], a6: ConfigValue[F, A6], a7: ConfigValue[F, A7], a8: ConfigValue[F, A8], a9: ConfigValue[F, A9], a10: ConfigValue[F, A10], a11: ConfigValue[F, A11], a12: ConfigValue[F, A12], a13: ConfigValue[F, A13], a14: ConfigValue[F, A14], a15: ConfigValue[F, A15], a16: ConfigValue[F, A16], a17: ConfigValue[F, A17], a18: ConfigValue[F, A18], a19: ConfigValue[F, A19], a20: ConfigValue[F, A20])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20) => F[Either[ConfigErrors, Z]]): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14 append a15 append a16 append a17 append a18 append a19 append a20).value.flatMap {
       case Left(errors) => left[Z](errors).pure[F]
       case Right(values) => f.tupled.apply(values)
     }

  /**
    * Loads a configuration using the 21 specified [[ConfigValue]]s.
    * Deals with error accumulation if there are any errors in the
    * provided [[ConfigValue]]s.
    *
    * @param a1 configuration value 1
    * @param a2 configuration value 2
    * @param a3 configuration value 3
    * @param a4 configuration value 4
    * @param a5 configuration value 5
    * @param a6 configuration value 6
    * @param a7 configuration value 7
    * @param a8 configuration value 8
    * @param a9 configuration value 9
    * @param a10 configuration value 10
    * @param a11 configuration value 11
    * @param a12 configuration value 12
    * @param a13 configuration value 13
    * @param a14 configuration value 14
    * @param a15 configuration value 15
    * @param a16 configuration value 16
    * @param a17 configuration value 17
    * @param a18 configuration value 18
    * @param a19 configuration value 19
    * @param a20 configuration value 20
    * @param a21 configuration value 21
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration value 1
    * @tparam A2 the type for configuration value 2
    * @tparam A3 the type for configuration value 3
    * @tparam A4 the type for configuration value 4
    * @tparam A5 the type for configuration value 5
    * @tparam A6 the type for configuration value 6
    * @tparam A7 the type for configuration value 7
    * @tparam A8 the type for configuration value 8
    * @tparam A9 the type for configuration value 9
    * @tparam A10 the type for configuration value 10
    * @tparam A11 the type for configuration value 11
    * @tparam A12 the type for configuration value 12
    * @tparam A13 the type for configuration value 13
    * @tparam A14 the type for configuration value 14
    * @tparam A15 the type for configuration value 15
    * @tparam A16 the type for configuration value 16
    * @tparam A17 the type for configuration value 17
    * @tparam A18 the type for configuration value 18
    * @tparam A19 the type for configuration value 19
    * @tparam A20 the type for configuration value 20
    * @tparam A21 the type for configuration value 21
    * @tparam F the [[ConfigValue]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[F[_]: Functor, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, Z](a1: ConfigValue[F, A1], a2: ConfigValue[F, A2], a3: ConfigValue[F, A3], a4: ConfigValue[F, A4], a5: ConfigValue[F, A5], a6: ConfigValue[F, A6], a7: ConfigValue[F, A7], a8: ConfigValue[F, A8], a9: ConfigValue[F, A9], a10: ConfigValue[F, A10], a11: ConfigValue[F, A11], a12: ConfigValue[F, A12], a13: ConfigValue[F, A13], a14: ConfigValue[F, A14], a15: ConfigValue[F, A15], a16: ConfigValue[F, A16], a17: ConfigValue[F, A17], a18: ConfigValue[F, A18], a19: ConfigValue[F, A19], a20: ConfigValue[F, A20], a21: ConfigValue[F, A21])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21) => Z): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14 append a15 append a16 append a17 append a18 append a19 append a20 append a21).value.map(_.right.map(f.tupled))

  /**
    * Defines a requirement on 21 [[ConfigValue]]s in order to be
    * able to load a configuration. The method wraps `loadConfig`
    * methods, requiring the provided [[ConfigValue]]s to be
    * available in order to use the `loadConfig` methods.
    *
    * Deals with error accumulation if there are any errors in
    * the provided [[ConfigValue]]s.
    *
    * @param a1 configuration value 1
    * @param a2 configuration value 2
    * @param a3 configuration value 3
    * @param a4 configuration value 4
    * @param a5 configuration value 5
    * @param a6 configuration value 6
    * @param a7 configuration value 7
    * @param a8 configuration value 8
    * @param a9 configuration value 9
    * @param a10 configuration value 10
    * @param a11 configuration value 11
    * @param a12 configuration value 12
    * @param a13 configuration value 13
    * @param a14 configuration value 14
    * @param a15 configuration value 15
    * @param a16 configuration value 16
    * @param a17 configuration value 17
    * @param a18 configuration value 18
    * @param a19 configuration value 19
    * @param a20 configuration value 20
    * @param a21 configuration value 21
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration value 1
    * @tparam A2 the type for configuration value 2
    * @tparam A3 the type for configuration value 3
    * @tparam A4 the type for configuration value 4
    * @tparam A5 the type for configuration value 5
    * @tparam A6 the type for configuration value 6
    * @tparam A7 the type for configuration value 7
    * @tparam A8 the type for configuration value 8
    * @tparam A9 the type for configuration value 9
    * @tparam A10 the type for configuration value 10
    * @tparam A11 the type for configuration value 11
    * @tparam A12 the type for configuration value 12
    * @tparam A13 the type for configuration value 13
    * @tparam A14 the type for configuration value 14
    * @tparam A15 the type for configuration value 15
    * @tparam A16 the type for configuration value 16
    * @tparam A17 the type for configuration value 17
    * @tparam A18 the type for configuration value 18
    * @tparam A19 the type for configuration value 19
    * @tparam A20 the type for configuration value 20
    * @tparam A21 the type for configuration value 21
    * @tparam F the [[ConfigValue]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[F[_]: Monad, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, Z](a1: ConfigValue[F, A1], a2: ConfigValue[F, A2], a3: ConfigValue[F, A3], a4: ConfigValue[F, A4], a5: ConfigValue[F, A5], a6: ConfigValue[F, A6], a7: ConfigValue[F, A7], a8: ConfigValue[F, A8], a9: ConfigValue[F, A9], a10: ConfigValue[F, A10], a11: ConfigValue[F, A11], a12: ConfigValue[F, A12], a13: ConfigValue[F, A13], a14: ConfigValue[F, A14], a15: ConfigValue[F, A15], a16: ConfigValue[F, A16], a17: ConfigValue[F, A17], a18: ConfigValue[F, A18], a19: ConfigValue[F, A19], a20: ConfigValue[F, A20], a21: ConfigValue[F, A21])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21) => F[Either[ConfigErrors, Z]]): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14 append a15 append a16 append a17 append a18 append a19 append a20 append a21).value.flatMap {
       case Left(errors) => left[Z](errors).pure[F]
       case Right(values) => f.tupled.apply(values)
     }
}
