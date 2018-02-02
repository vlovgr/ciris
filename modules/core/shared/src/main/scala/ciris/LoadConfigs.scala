// format: off

/**
  * Generated using sbt source generators.
  * You should not modify this file directly.
  */

package ciris

import ciris.api._
import ciris.api.syntax._

private[ciris] class LoadConfigs {

  /**
    * Wraps the specified value in an `F[Either[ConfigErrors, Z]]`. Useful
    * when you want to use a static configuration inside a `withValues`
    * block, requiring you to wrap it with this method.
    *
    * @param z the value to wrap
    * @tparam F the [[ConfigEntry]] context
    * @tparam Z the type of the value to wrap
    * @return the value wrapped in an `F[Either[ConfigErrors, Z]]`
    */
  def loadConfig[F[_]: Applicative, Z](z: Z): F[Either[ConfigErrors, Z]] =
    (Right(z) : Either[ConfigErrors, Z]).pure[F]

  /**
    * Loads a configuration using the specified [[ConfigEntry]].
    *
    * @param a1 the configuration entry
    * @param f the function to create the configuration
    * @tparam F the [[ConfigEntry]] context
    * @tparam A1 the type of the configuration entry
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[F[_]: Functor, A1, Z](a1: ConfigEntry[F, _, _, A1])(f: A1 => Z): F[Either[ConfigErrors, Z]] =
    a1.value.map(_.fold(error => Left(ConfigErrors(error)), a1 => Right(f(a1))))

  /**
    * Defines a requirement on a single [[ConfigEntry]] in order to be
    * able to load a configuration. The method wraps `loadConfig`
    * methods, requiring the provided [[ConfigEntry]] to be
    * available in order to use the `loadConfig` methods.
    *
    * @param a1 the configuration entry
    * @param f the function to create the configuration
    * @tparam F the [[ConfigEntry]] context
    * @tparam A1 the type of the configuration entry
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValue[F[_]: Monad, A1, Z](a1: ConfigEntry[F, _, _, A1])(f: A1 => F[Either[ConfigErrors, Z]]): F[Either[ConfigErrors, Z]] =
    withValues(a1)(f)

  /**
    * Defines a requirement on a single [[ConfigEntry]] in order to be
    * able to load a configuration. The method wraps any `loadConfig`
    * methods, requiring the provided [[ConfigEntry]] to be
    * available in order to use the `loadConfig` methods.
    *
    * @param a1 the configuration entry
    * @param f the function to create the configuration
    * @tparam F the [[ConfigEntry]] context
    * @tparam A1 the type of the configuration entry
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[F[_]: Monad, A1, Z](a1: ConfigEntry[F, _, _, A1])(f: A1 => F[Either[ConfigErrors, Z]]): F[Either[ConfigErrors, Z]] =
    a1.value.flatMap {
      case Left(error) => (Left(ConfigErrors(error)): Either[ConfigErrors, Z]).pure[F]
      case Right(value) => f(value)
    }

  /**
    * Loads a configuration using the 2 specified [[ConfigEntry]]s.
    * Deals with error accumulation if there are any errors in the
    * provided [[ConfigEntry]]s.
    *
    * @param a1 configuration entry 1
    * @param a2 configuration entry 2
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration value 1
    * @tparam A2 the type for configuration value 2
    * @tparam F the [[ConfigEntry]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[F[_]: Functor, A1, A2, Z](a1: ConfigEntry[F, _, _, A1], a2: ConfigEntry[F, _, _, A2])(f: (A1, A2) => Z): F[Either[ConfigErrors, Z]] =
    (a1 append a2).value.map(_.right.map(f.tupled))

  /**
    * Defines a requirement on 2 [[ConfigEntry]]s in order to be
    * able to load a configuration. The method wraps `loadConfig`
    * methods, requiring the provided [[ConfigEntry]]s to be
    * available in order to use the `loadConfig` methods.
    *
    * Deals with error accumulation if there are any errors in
    * the provided [[ConfigEntry]]s.
    *
    * @param a1 configuration entry 1
    * @param a2 configuration entry 2
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration value 1
    * @tparam A2 the type for configuration value 2
    * @tparam F the [[ConfigEntry]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[F[_]: Monad, A1, A2, Z](a1: ConfigEntry[F, _, _, A1], a2: ConfigEntry[F, _, _, A2])(f: (A1, A2) => F[Either[ConfigErrors, Z]]): F[Either[ConfigErrors, Z]] =
    (a1 append a2).value.flatMap {
       case Left(errors) => (Left(errors): Either[ConfigErrors, Z]).pure[F]
       case Right(values) => f.tupled.apply(values)
     }

  /**
    * Loads a configuration using the 3 specified [[ConfigEntry]]s.
    * Deals with error accumulation if there are any errors in the
    * provided [[ConfigEntry]]s.
    *
    * @param a1 configuration entry 1
    * @param a2 configuration entry 2
    * @param a3 configuration entry 3
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration value 1
    * @tparam A2 the type for configuration value 2
    * @tparam A3 the type for configuration value 3
    * @tparam F the [[ConfigEntry]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[F[_]: Functor, A1, A2, A3, Z](a1: ConfigEntry[F, _, _, A1], a2: ConfigEntry[F, _, _, A2], a3: ConfigEntry[F, _, _, A3])(f: (A1, A2, A3) => Z): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3).value.map(_.right.map(f.tupled))

  /**
    * Defines a requirement on 3 [[ConfigEntry]]s in order to be
    * able to load a configuration. The method wraps `loadConfig`
    * methods, requiring the provided [[ConfigEntry]]s to be
    * available in order to use the `loadConfig` methods.
    *
    * Deals with error accumulation if there are any errors in
    * the provided [[ConfigEntry]]s.
    *
    * @param a1 configuration entry 1
    * @param a2 configuration entry 2
    * @param a3 configuration entry 3
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration value 1
    * @tparam A2 the type for configuration value 2
    * @tparam A3 the type for configuration value 3
    * @tparam F the [[ConfigEntry]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[F[_]: Monad, A1, A2, A3, Z](a1: ConfigEntry[F, _, _, A1], a2: ConfigEntry[F, _, _, A2], a3: ConfigEntry[F, _, _, A3])(f: (A1, A2, A3) => F[Either[ConfigErrors, Z]]): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3).value.flatMap {
       case Left(errors) => (Left(errors): Either[ConfigErrors, Z]).pure[F]
       case Right(values) => f.tupled.apply(values)
     }

  /**
    * Loads a configuration using the 4 specified [[ConfigEntry]]s.
    * Deals with error accumulation if there are any errors in the
    * provided [[ConfigEntry]]s.
    *
    * @param a1 configuration entry 1
    * @param a2 configuration entry 2
    * @param a3 configuration entry 3
    * @param a4 configuration entry 4
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration value 1
    * @tparam A2 the type for configuration value 2
    * @tparam A3 the type for configuration value 3
    * @tparam A4 the type for configuration value 4
    * @tparam F the [[ConfigEntry]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[F[_]: Functor, A1, A2, A3, A4, Z](a1: ConfigEntry[F, _, _, A1], a2: ConfigEntry[F, _, _, A2], a3: ConfigEntry[F, _, _, A3], a4: ConfigEntry[F, _, _, A4])(f: (A1, A2, A3, A4) => Z): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4).value.map(_.right.map(f.tupled))

  /**
    * Defines a requirement on 4 [[ConfigEntry]]s in order to be
    * able to load a configuration. The method wraps `loadConfig`
    * methods, requiring the provided [[ConfigEntry]]s to be
    * available in order to use the `loadConfig` methods.
    *
    * Deals with error accumulation if there are any errors in
    * the provided [[ConfigEntry]]s.
    *
    * @param a1 configuration entry 1
    * @param a2 configuration entry 2
    * @param a3 configuration entry 3
    * @param a4 configuration entry 4
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration value 1
    * @tparam A2 the type for configuration value 2
    * @tparam A3 the type for configuration value 3
    * @tparam A4 the type for configuration value 4
    * @tparam F the [[ConfigEntry]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[F[_]: Monad, A1, A2, A3, A4, Z](a1: ConfigEntry[F, _, _, A1], a2: ConfigEntry[F, _, _, A2], a3: ConfigEntry[F, _, _, A3], a4: ConfigEntry[F, _, _, A4])(f: (A1, A2, A3, A4) => F[Either[ConfigErrors, Z]]): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4).value.flatMap {
       case Left(errors) => (Left(errors): Either[ConfigErrors, Z]).pure[F]
       case Right(values) => f.tupled.apply(values)
     }

  /**
    * Loads a configuration using the 5 specified [[ConfigEntry]]s.
    * Deals with error accumulation if there are any errors in the
    * provided [[ConfigEntry]]s.
    *
    * @param a1 configuration entry 1
    * @param a2 configuration entry 2
    * @param a3 configuration entry 3
    * @param a4 configuration entry 4
    * @param a5 configuration entry 5
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration value 1
    * @tparam A2 the type for configuration value 2
    * @tparam A3 the type for configuration value 3
    * @tparam A4 the type for configuration value 4
    * @tparam A5 the type for configuration value 5
    * @tparam F the [[ConfigEntry]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[F[_]: Functor, A1, A2, A3, A4, A5, Z](a1: ConfigEntry[F, _, _, A1], a2: ConfigEntry[F, _, _, A2], a3: ConfigEntry[F, _, _, A3], a4: ConfigEntry[F, _, _, A4], a5: ConfigEntry[F, _, _, A5])(f: (A1, A2, A3, A4, A5) => Z): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4 append a5).value.map(_.right.map(f.tupled))

  /**
    * Defines a requirement on 5 [[ConfigEntry]]s in order to be
    * able to load a configuration. The method wraps `loadConfig`
    * methods, requiring the provided [[ConfigEntry]]s to be
    * available in order to use the `loadConfig` methods.
    *
    * Deals with error accumulation if there are any errors in
    * the provided [[ConfigEntry]]s.
    *
    * @param a1 configuration entry 1
    * @param a2 configuration entry 2
    * @param a3 configuration entry 3
    * @param a4 configuration entry 4
    * @param a5 configuration entry 5
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration value 1
    * @tparam A2 the type for configuration value 2
    * @tparam A3 the type for configuration value 3
    * @tparam A4 the type for configuration value 4
    * @tparam A5 the type for configuration value 5
    * @tparam F the [[ConfigEntry]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[F[_]: Monad, A1, A2, A3, A4, A5, Z](a1: ConfigEntry[F, _, _, A1], a2: ConfigEntry[F, _, _, A2], a3: ConfigEntry[F, _, _, A3], a4: ConfigEntry[F, _, _, A4], a5: ConfigEntry[F, _, _, A5])(f: (A1, A2, A3, A4, A5) => F[Either[ConfigErrors, Z]]): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4 append a5).value.flatMap {
       case Left(errors) => (Left(errors): Either[ConfigErrors, Z]).pure[F]
       case Right(values) => f.tupled.apply(values)
     }

  /**
    * Loads a configuration using the 6 specified [[ConfigEntry]]s.
    * Deals with error accumulation if there are any errors in the
    * provided [[ConfigEntry]]s.
    *
    * @param a1 configuration entry 1
    * @param a2 configuration entry 2
    * @param a3 configuration entry 3
    * @param a4 configuration entry 4
    * @param a5 configuration entry 5
    * @param a6 configuration entry 6
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration value 1
    * @tparam A2 the type for configuration value 2
    * @tparam A3 the type for configuration value 3
    * @tparam A4 the type for configuration value 4
    * @tparam A5 the type for configuration value 5
    * @tparam A6 the type for configuration value 6
    * @tparam F the [[ConfigEntry]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[F[_]: Functor, A1, A2, A3, A4, A5, A6, Z](a1: ConfigEntry[F, _, _, A1], a2: ConfigEntry[F, _, _, A2], a3: ConfigEntry[F, _, _, A3], a4: ConfigEntry[F, _, _, A4], a5: ConfigEntry[F, _, _, A5], a6: ConfigEntry[F, _, _, A6])(f: (A1, A2, A3, A4, A5, A6) => Z): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4 append a5 append a6).value.map(_.right.map(f.tupled))

  /**
    * Defines a requirement on 6 [[ConfigEntry]]s in order to be
    * able to load a configuration. The method wraps `loadConfig`
    * methods, requiring the provided [[ConfigEntry]]s to be
    * available in order to use the `loadConfig` methods.
    *
    * Deals with error accumulation if there are any errors in
    * the provided [[ConfigEntry]]s.
    *
    * @param a1 configuration entry 1
    * @param a2 configuration entry 2
    * @param a3 configuration entry 3
    * @param a4 configuration entry 4
    * @param a5 configuration entry 5
    * @param a6 configuration entry 6
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration value 1
    * @tparam A2 the type for configuration value 2
    * @tparam A3 the type for configuration value 3
    * @tparam A4 the type for configuration value 4
    * @tparam A5 the type for configuration value 5
    * @tparam A6 the type for configuration value 6
    * @tparam F the [[ConfigEntry]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[F[_]: Monad, A1, A2, A3, A4, A5, A6, Z](a1: ConfigEntry[F, _, _, A1], a2: ConfigEntry[F, _, _, A2], a3: ConfigEntry[F, _, _, A3], a4: ConfigEntry[F, _, _, A4], a5: ConfigEntry[F, _, _, A5], a6: ConfigEntry[F, _, _, A6])(f: (A1, A2, A3, A4, A5, A6) => F[Either[ConfigErrors, Z]]): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4 append a5 append a6).value.flatMap {
       case Left(errors) => (Left(errors): Either[ConfigErrors, Z]).pure[F]
       case Right(values) => f.tupled.apply(values)
     }

  /**
    * Loads a configuration using the 7 specified [[ConfigEntry]]s.
    * Deals with error accumulation if there are any errors in the
    * provided [[ConfigEntry]]s.
    *
    * @param a1 configuration entry 1
    * @param a2 configuration entry 2
    * @param a3 configuration entry 3
    * @param a4 configuration entry 4
    * @param a5 configuration entry 5
    * @param a6 configuration entry 6
    * @param a7 configuration entry 7
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration value 1
    * @tparam A2 the type for configuration value 2
    * @tparam A3 the type for configuration value 3
    * @tparam A4 the type for configuration value 4
    * @tparam A5 the type for configuration value 5
    * @tparam A6 the type for configuration value 6
    * @tparam A7 the type for configuration value 7
    * @tparam F the [[ConfigEntry]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[F[_]: Functor, A1, A2, A3, A4, A5, A6, A7, Z](a1: ConfigEntry[F, _, _, A1], a2: ConfigEntry[F, _, _, A2], a3: ConfigEntry[F, _, _, A3], a4: ConfigEntry[F, _, _, A4], a5: ConfigEntry[F, _, _, A5], a6: ConfigEntry[F, _, _, A6], a7: ConfigEntry[F, _, _, A7])(f: (A1, A2, A3, A4, A5, A6, A7) => Z): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7).value.map(_.right.map(f.tupled))

  /**
    * Defines a requirement on 7 [[ConfigEntry]]s in order to be
    * able to load a configuration. The method wraps `loadConfig`
    * methods, requiring the provided [[ConfigEntry]]s to be
    * available in order to use the `loadConfig` methods.
    *
    * Deals with error accumulation if there are any errors in
    * the provided [[ConfigEntry]]s.
    *
    * @param a1 configuration entry 1
    * @param a2 configuration entry 2
    * @param a3 configuration entry 3
    * @param a4 configuration entry 4
    * @param a5 configuration entry 5
    * @param a6 configuration entry 6
    * @param a7 configuration entry 7
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration value 1
    * @tparam A2 the type for configuration value 2
    * @tparam A3 the type for configuration value 3
    * @tparam A4 the type for configuration value 4
    * @tparam A5 the type for configuration value 5
    * @tparam A6 the type for configuration value 6
    * @tparam A7 the type for configuration value 7
    * @tparam F the [[ConfigEntry]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[F[_]: Monad, A1, A2, A3, A4, A5, A6, A7, Z](a1: ConfigEntry[F, _, _, A1], a2: ConfigEntry[F, _, _, A2], a3: ConfigEntry[F, _, _, A3], a4: ConfigEntry[F, _, _, A4], a5: ConfigEntry[F, _, _, A5], a6: ConfigEntry[F, _, _, A6], a7: ConfigEntry[F, _, _, A7])(f: (A1, A2, A3, A4, A5, A6, A7) => F[Either[ConfigErrors, Z]]): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7).value.flatMap {
       case Left(errors) => (Left(errors): Either[ConfigErrors, Z]).pure[F]
       case Right(values) => f.tupled.apply(values)
     }

  /**
    * Loads a configuration using the 8 specified [[ConfigEntry]]s.
    * Deals with error accumulation if there are any errors in the
    * provided [[ConfigEntry]]s.
    *
    * @param a1 configuration entry 1
    * @param a2 configuration entry 2
    * @param a3 configuration entry 3
    * @param a4 configuration entry 4
    * @param a5 configuration entry 5
    * @param a6 configuration entry 6
    * @param a7 configuration entry 7
    * @param a8 configuration entry 8
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration value 1
    * @tparam A2 the type for configuration value 2
    * @tparam A3 the type for configuration value 3
    * @tparam A4 the type for configuration value 4
    * @tparam A5 the type for configuration value 5
    * @tparam A6 the type for configuration value 6
    * @tparam A7 the type for configuration value 7
    * @tparam A8 the type for configuration value 8
    * @tparam F the [[ConfigEntry]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[F[_]: Functor, A1, A2, A3, A4, A5, A6, A7, A8, Z](a1: ConfigEntry[F, _, _, A1], a2: ConfigEntry[F, _, _, A2], a3: ConfigEntry[F, _, _, A3], a4: ConfigEntry[F, _, _, A4], a5: ConfigEntry[F, _, _, A5], a6: ConfigEntry[F, _, _, A6], a7: ConfigEntry[F, _, _, A7], a8: ConfigEntry[F, _, _, A8])(f: (A1, A2, A3, A4, A5, A6, A7, A8) => Z): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8).value.map(_.right.map(f.tupled))

  /**
    * Defines a requirement on 8 [[ConfigEntry]]s in order to be
    * able to load a configuration. The method wraps `loadConfig`
    * methods, requiring the provided [[ConfigEntry]]s to be
    * available in order to use the `loadConfig` methods.
    *
    * Deals with error accumulation if there are any errors in
    * the provided [[ConfigEntry]]s.
    *
    * @param a1 configuration entry 1
    * @param a2 configuration entry 2
    * @param a3 configuration entry 3
    * @param a4 configuration entry 4
    * @param a5 configuration entry 5
    * @param a6 configuration entry 6
    * @param a7 configuration entry 7
    * @param a8 configuration entry 8
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration value 1
    * @tparam A2 the type for configuration value 2
    * @tparam A3 the type for configuration value 3
    * @tparam A4 the type for configuration value 4
    * @tparam A5 the type for configuration value 5
    * @tparam A6 the type for configuration value 6
    * @tparam A7 the type for configuration value 7
    * @tparam A8 the type for configuration value 8
    * @tparam F the [[ConfigEntry]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[F[_]: Monad, A1, A2, A3, A4, A5, A6, A7, A8, Z](a1: ConfigEntry[F, _, _, A1], a2: ConfigEntry[F, _, _, A2], a3: ConfigEntry[F, _, _, A3], a4: ConfigEntry[F, _, _, A4], a5: ConfigEntry[F, _, _, A5], a6: ConfigEntry[F, _, _, A6], a7: ConfigEntry[F, _, _, A7], a8: ConfigEntry[F, _, _, A8])(f: (A1, A2, A3, A4, A5, A6, A7, A8) => F[Either[ConfigErrors, Z]]): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8).value.flatMap {
       case Left(errors) => (Left(errors): Either[ConfigErrors, Z]).pure[F]
       case Right(values) => f.tupled.apply(values)
     }

  /**
    * Loads a configuration using the 9 specified [[ConfigEntry]]s.
    * Deals with error accumulation if there are any errors in the
    * provided [[ConfigEntry]]s.
    *
    * @param a1 configuration entry 1
    * @param a2 configuration entry 2
    * @param a3 configuration entry 3
    * @param a4 configuration entry 4
    * @param a5 configuration entry 5
    * @param a6 configuration entry 6
    * @param a7 configuration entry 7
    * @param a8 configuration entry 8
    * @param a9 configuration entry 9
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
    * @tparam F the [[ConfigEntry]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[F[_]: Functor, A1, A2, A3, A4, A5, A6, A7, A8, A9, Z](a1: ConfigEntry[F, _, _, A1], a2: ConfigEntry[F, _, _, A2], a3: ConfigEntry[F, _, _, A3], a4: ConfigEntry[F, _, _, A4], a5: ConfigEntry[F, _, _, A5], a6: ConfigEntry[F, _, _, A6], a7: ConfigEntry[F, _, _, A7], a8: ConfigEntry[F, _, _, A8], a9: ConfigEntry[F, _, _, A9])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9) => Z): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9).value.map(_.right.map(f.tupled))

  /**
    * Defines a requirement on 9 [[ConfigEntry]]s in order to be
    * able to load a configuration. The method wraps `loadConfig`
    * methods, requiring the provided [[ConfigEntry]]s to be
    * available in order to use the `loadConfig` methods.
    *
    * Deals with error accumulation if there are any errors in
    * the provided [[ConfigEntry]]s.
    *
    * @param a1 configuration entry 1
    * @param a2 configuration entry 2
    * @param a3 configuration entry 3
    * @param a4 configuration entry 4
    * @param a5 configuration entry 5
    * @param a6 configuration entry 6
    * @param a7 configuration entry 7
    * @param a8 configuration entry 8
    * @param a9 configuration entry 9
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
    * @tparam F the [[ConfigEntry]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[F[_]: Monad, A1, A2, A3, A4, A5, A6, A7, A8, A9, Z](a1: ConfigEntry[F, _, _, A1], a2: ConfigEntry[F, _, _, A2], a3: ConfigEntry[F, _, _, A3], a4: ConfigEntry[F, _, _, A4], a5: ConfigEntry[F, _, _, A5], a6: ConfigEntry[F, _, _, A6], a7: ConfigEntry[F, _, _, A7], a8: ConfigEntry[F, _, _, A8], a9: ConfigEntry[F, _, _, A9])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9) => F[Either[ConfigErrors, Z]]): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9).value.flatMap {
       case Left(errors) => (Left(errors): Either[ConfigErrors, Z]).pure[F]
       case Right(values) => f.tupled.apply(values)
     }

  /**
    * Loads a configuration using the 10 specified [[ConfigEntry]]s.
    * Deals with error accumulation if there are any errors in the
    * provided [[ConfigEntry]]s.
    *
    * @param a1 configuration entry 1
    * @param a2 configuration entry 2
    * @param a3 configuration entry 3
    * @param a4 configuration entry 4
    * @param a5 configuration entry 5
    * @param a6 configuration entry 6
    * @param a7 configuration entry 7
    * @param a8 configuration entry 8
    * @param a9 configuration entry 9
    * @param a10 configuration entry 10
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
    * @tparam F the [[ConfigEntry]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[F[_]: Functor, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, Z](a1: ConfigEntry[F, _, _, A1], a2: ConfigEntry[F, _, _, A2], a3: ConfigEntry[F, _, _, A3], a4: ConfigEntry[F, _, _, A4], a5: ConfigEntry[F, _, _, A5], a6: ConfigEntry[F, _, _, A6], a7: ConfigEntry[F, _, _, A7], a8: ConfigEntry[F, _, _, A8], a9: ConfigEntry[F, _, _, A9], a10: ConfigEntry[F, _, _, A10])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10) => Z): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10).value.map(_.right.map(f.tupled))

  /**
    * Defines a requirement on 10 [[ConfigEntry]]s in order to be
    * able to load a configuration. The method wraps `loadConfig`
    * methods, requiring the provided [[ConfigEntry]]s to be
    * available in order to use the `loadConfig` methods.
    *
    * Deals with error accumulation if there are any errors in
    * the provided [[ConfigEntry]]s.
    *
    * @param a1 configuration entry 1
    * @param a2 configuration entry 2
    * @param a3 configuration entry 3
    * @param a4 configuration entry 4
    * @param a5 configuration entry 5
    * @param a6 configuration entry 6
    * @param a7 configuration entry 7
    * @param a8 configuration entry 8
    * @param a9 configuration entry 9
    * @param a10 configuration entry 10
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
    * @tparam F the [[ConfigEntry]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[F[_]: Monad, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, Z](a1: ConfigEntry[F, _, _, A1], a2: ConfigEntry[F, _, _, A2], a3: ConfigEntry[F, _, _, A3], a4: ConfigEntry[F, _, _, A4], a5: ConfigEntry[F, _, _, A5], a6: ConfigEntry[F, _, _, A6], a7: ConfigEntry[F, _, _, A7], a8: ConfigEntry[F, _, _, A8], a9: ConfigEntry[F, _, _, A9], a10: ConfigEntry[F, _, _, A10])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10) => F[Either[ConfigErrors, Z]]): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10).value.flatMap {
       case Left(errors) => (Left(errors): Either[ConfigErrors, Z]).pure[F]
       case Right(values) => f.tupled.apply(values)
     }

  /**
    * Loads a configuration using the 11 specified [[ConfigEntry]]s.
    * Deals with error accumulation if there are any errors in the
    * provided [[ConfigEntry]]s.
    *
    * @param a1 configuration entry 1
    * @param a2 configuration entry 2
    * @param a3 configuration entry 3
    * @param a4 configuration entry 4
    * @param a5 configuration entry 5
    * @param a6 configuration entry 6
    * @param a7 configuration entry 7
    * @param a8 configuration entry 8
    * @param a9 configuration entry 9
    * @param a10 configuration entry 10
    * @param a11 configuration entry 11
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
    * @tparam F the [[ConfigEntry]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[F[_]: Functor, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, Z](a1: ConfigEntry[F, _, _, A1], a2: ConfigEntry[F, _, _, A2], a3: ConfigEntry[F, _, _, A3], a4: ConfigEntry[F, _, _, A4], a5: ConfigEntry[F, _, _, A5], a6: ConfigEntry[F, _, _, A6], a7: ConfigEntry[F, _, _, A7], a8: ConfigEntry[F, _, _, A8], a9: ConfigEntry[F, _, _, A9], a10: ConfigEntry[F, _, _, A10], a11: ConfigEntry[F, _, _, A11])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11) => Z): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11).value.map(_.right.map(f.tupled))

  /**
    * Defines a requirement on 11 [[ConfigEntry]]s in order to be
    * able to load a configuration. The method wraps `loadConfig`
    * methods, requiring the provided [[ConfigEntry]]s to be
    * available in order to use the `loadConfig` methods.
    *
    * Deals with error accumulation if there are any errors in
    * the provided [[ConfigEntry]]s.
    *
    * @param a1 configuration entry 1
    * @param a2 configuration entry 2
    * @param a3 configuration entry 3
    * @param a4 configuration entry 4
    * @param a5 configuration entry 5
    * @param a6 configuration entry 6
    * @param a7 configuration entry 7
    * @param a8 configuration entry 8
    * @param a9 configuration entry 9
    * @param a10 configuration entry 10
    * @param a11 configuration entry 11
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
    * @tparam F the [[ConfigEntry]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[F[_]: Monad, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, Z](a1: ConfigEntry[F, _, _, A1], a2: ConfigEntry[F, _, _, A2], a3: ConfigEntry[F, _, _, A3], a4: ConfigEntry[F, _, _, A4], a5: ConfigEntry[F, _, _, A5], a6: ConfigEntry[F, _, _, A6], a7: ConfigEntry[F, _, _, A7], a8: ConfigEntry[F, _, _, A8], a9: ConfigEntry[F, _, _, A9], a10: ConfigEntry[F, _, _, A10], a11: ConfigEntry[F, _, _, A11])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11) => F[Either[ConfigErrors, Z]]): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11).value.flatMap {
       case Left(errors) => (Left(errors): Either[ConfigErrors, Z]).pure[F]
       case Right(values) => f.tupled.apply(values)
     }

  /**
    * Loads a configuration using the 12 specified [[ConfigEntry]]s.
    * Deals with error accumulation if there are any errors in the
    * provided [[ConfigEntry]]s.
    *
    * @param a1 configuration entry 1
    * @param a2 configuration entry 2
    * @param a3 configuration entry 3
    * @param a4 configuration entry 4
    * @param a5 configuration entry 5
    * @param a6 configuration entry 6
    * @param a7 configuration entry 7
    * @param a8 configuration entry 8
    * @param a9 configuration entry 9
    * @param a10 configuration entry 10
    * @param a11 configuration entry 11
    * @param a12 configuration entry 12
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
    * @tparam F the [[ConfigEntry]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[F[_]: Functor, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, Z](a1: ConfigEntry[F, _, _, A1], a2: ConfigEntry[F, _, _, A2], a3: ConfigEntry[F, _, _, A3], a4: ConfigEntry[F, _, _, A4], a5: ConfigEntry[F, _, _, A5], a6: ConfigEntry[F, _, _, A6], a7: ConfigEntry[F, _, _, A7], a8: ConfigEntry[F, _, _, A8], a9: ConfigEntry[F, _, _, A9], a10: ConfigEntry[F, _, _, A10], a11: ConfigEntry[F, _, _, A11], a12: ConfigEntry[F, _, _, A12])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12) => Z): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12).value.map(_.right.map(f.tupled))

  /**
    * Defines a requirement on 12 [[ConfigEntry]]s in order to be
    * able to load a configuration. The method wraps `loadConfig`
    * methods, requiring the provided [[ConfigEntry]]s to be
    * available in order to use the `loadConfig` methods.
    *
    * Deals with error accumulation if there are any errors in
    * the provided [[ConfigEntry]]s.
    *
    * @param a1 configuration entry 1
    * @param a2 configuration entry 2
    * @param a3 configuration entry 3
    * @param a4 configuration entry 4
    * @param a5 configuration entry 5
    * @param a6 configuration entry 6
    * @param a7 configuration entry 7
    * @param a8 configuration entry 8
    * @param a9 configuration entry 9
    * @param a10 configuration entry 10
    * @param a11 configuration entry 11
    * @param a12 configuration entry 12
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
    * @tparam F the [[ConfigEntry]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[F[_]: Monad, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, Z](a1: ConfigEntry[F, _, _, A1], a2: ConfigEntry[F, _, _, A2], a3: ConfigEntry[F, _, _, A3], a4: ConfigEntry[F, _, _, A4], a5: ConfigEntry[F, _, _, A5], a6: ConfigEntry[F, _, _, A6], a7: ConfigEntry[F, _, _, A7], a8: ConfigEntry[F, _, _, A8], a9: ConfigEntry[F, _, _, A9], a10: ConfigEntry[F, _, _, A10], a11: ConfigEntry[F, _, _, A11], a12: ConfigEntry[F, _, _, A12])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12) => F[Either[ConfigErrors, Z]]): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12).value.flatMap {
       case Left(errors) => (Left(errors): Either[ConfigErrors, Z]).pure[F]
       case Right(values) => f.tupled.apply(values)
     }

  /**
    * Loads a configuration using the 13 specified [[ConfigEntry]]s.
    * Deals with error accumulation if there are any errors in the
    * provided [[ConfigEntry]]s.
    *
    * @param a1 configuration entry 1
    * @param a2 configuration entry 2
    * @param a3 configuration entry 3
    * @param a4 configuration entry 4
    * @param a5 configuration entry 5
    * @param a6 configuration entry 6
    * @param a7 configuration entry 7
    * @param a8 configuration entry 8
    * @param a9 configuration entry 9
    * @param a10 configuration entry 10
    * @param a11 configuration entry 11
    * @param a12 configuration entry 12
    * @param a13 configuration entry 13
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
    * @tparam F the [[ConfigEntry]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[F[_]: Functor, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, Z](a1: ConfigEntry[F, _, _, A1], a2: ConfigEntry[F, _, _, A2], a3: ConfigEntry[F, _, _, A3], a4: ConfigEntry[F, _, _, A4], a5: ConfigEntry[F, _, _, A5], a6: ConfigEntry[F, _, _, A6], a7: ConfigEntry[F, _, _, A7], a8: ConfigEntry[F, _, _, A8], a9: ConfigEntry[F, _, _, A9], a10: ConfigEntry[F, _, _, A10], a11: ConfigEntry[F, _, _, A11], a12: ConfigEntry[F, _, _, A12], a13: ConfigEntry[F, _, _, A13])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13) => Z): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13).value.map(_.right.map(f.tupled))

  /**
    * Defines a requirement on 13 [[ConfigEntry]]s in order to be
    * able to load a configuration. The method wraps `loadConfig`
    * methods, requiring the provided [[ConfigEntry]]s to be
    * available in order to use the `loadConfig` methods.
    *
    * Deals with error accumulation if there are any errors in
    * the provided [[ConfigEntry]]s.
    *
    * @param a1 configuration entry 1
    * @param a2 configuration entry 2
    * @param a3 configuration entry 3
    * @param a4 configuration entry 4
    * @param a5 configuration entry 5
    * @param a6 configuration entry 6
    * @param a7 configuration entry 7
    * @param a8 configuration entry 8
    * @param a9 configuration entry 9
    * @param a10 configuration entry 10
    * @param a11 configuration entry 11
    * @param a12 configuration entry 12
    * @param a13 configuration entry 13
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
    * @tparam F the [[ConfigEntry]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[F[_]: Monad, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, Z](a1: ConfigEntry[F, _, _, A1], a2: ConfigEntry[F, _, _, A2], a3: ConfigEntry[F, _, _, A3], a4: ConfigEntry[F, _, _, A4], a5: ConfigEntry[F, _, _, A5], a6: ConfigEntry[F, _, _, A6], a7: ConfigEntry[F, _, _, A7], a8: ConfigEntry[F, _, _, A8], a9: ConfigEntry[F, _, _, A9], a10: ConfigEntry[F, _, _, A10], a11: ConfigEntry[F, _, _, A11], a12: ConfigEntry[F, _, _, A12], a13: ConfigEntry[F, _, _, A13])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13) => F[Either[ConfigErrors, Z]]): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13).value.flatMap {
       case Left(errors) => (Left(errors): Either[ConfigErrors, Z]).pure[F]
       case Right(values) => f.tupled.apply(values)
     }

  /**
    * Loads a configuration using the 14 specified [[ConfigEntry]]s.
    * Deals with error accumulation if there are any errors in the
    * provided [[ConfigEntry]]s.
    *
    * @param a1 configuration entry 1
    * @param a2 configuration entry 2
    * @param a3 configuration entry 3
    * @param a4 configuration entry 4
    * @param a5 configuration entry 5
    * @param a6 configuration entry 6
    * @param a7 configuration entry 7
    * @param a8 configuration entry 8
    * @param a9 configuration entry 9
    * @param a10 configuration entry 10
    * @param a11 configuration entry 11
    * @param a12 configuration entry 12
    * @param a13 configuration entry 13
    * @param a14 configuration entry 14
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
    * @tparam F the [[ConfigEntry]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[F[_]: Functor, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, Z](a1: ConfigEntry[F, _, _, A1], a2: ConfigEntry[F, _, _, A2], a3: ConfigEntry[F, _, _, A3], a4: ConfigEntry[F, _, _, A4], a5: ConfigEntry[F, _, _, A5], a6: ConfigEntry[F, _, _, A6], a7: ConfigEntry[F, _, _, A7], a8: ConfigEntry[F, _, _, A8], a9: ConfigEntry[F, _, _, A9], a10: ConfigEntry[F, _, _, A10], a11: ConfigEntry[F, _, _, A11], a12: ConfigEntry[F, _, _, A12], a13: ConfigEntry[F, _, _, A13], a14: ConfigEntry[F, _, _, A14])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14) => Z): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14).value.map(_.right.map(f.tupled))

  /**
    * Defines a requirement on 14 [[ConfigEntry]]s in order to be
    * able to load a configuration. The method wraps `loadConfig`
    * methods, requiring the provided [[ConfigEntry]]s to be
    * available in order to use the `loadConfig` methods.
    *
    * Deals with error accumulation if there are any errors in
    * the provided [[ConfigEntry]]s.
    *
    * @param a1 configuration entry 1
    * @param a2 configuration entry 2
    * @param a3 configuration entry 3
    * @param a4 configuration entry 4
    * @param a5 configuration entry 5
    * @param a6 configuration entry 6
    * @param a7 configuration entry 7
    * @param a8 configuration entry 8
    * @param a9 configuration entry 9
    * @param a10 configuration entry 10
    * @param a11 configuration entry 11
    * @param a12 configuration entry 12
    * @param a13 configuration entry 13
    * @param a14 configuration entry 14
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
    * @tparam F the [[ConfigEntry]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[F[_]: Monad, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, Z](a1: ConfigEntry[F, _, _, A1], a2: ConfigEntry[F, _, _, A2], a3: ConfigEntry[F, _, _, A3], a4: ConfigEntry[F, _, _, A4], a5: ConfigEntry[F, _, _, A5], a6: ConfigEntry[F, _, _, A6], a7: ConfigEntry[F, _, _, A7], a8: ConfigEntry[F, _, _, A8], a9: ConfigEntry[F, _, _, A9], a10: ConfigEntry[F, _, _, A10], a11: ConfigEntry[F, _, _, A11], a12: ConfigEntry[F, _, _, A12], a13: ConfigEntry[F, _, _, A13], a14: ConfigEntry[F, _, _, A14])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14) => F[Either[ConfigErrors, Z]]): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14).value.flatMap {
       case Left(errors) => (Left(errors): Either[ConfigErrors, Z]).pure[F]
       case Right(values) => f.tupled.apply(values)
     }

  /**
    * Loads a configuration using the 15 specified [[ConfigEntry]]s.
    * Deals with error accumulation if there are any errors in the
    * provided [[ConfigEntry]]s.
    *
    * @param a1 configuration entry 1
    * @param a2 configuration entry 2
    * @param a3 configuration entry 3
    * @param a4 configuration entry 4
    * @param a5 configuration entry 5
    * @param a6 configuration entry 6
    * @param a7 configuration entry 7
    * @param a8 configuration entry 8
    * @param a9 configuration entry 9
    * @param a10 configuration entry 10
    * @param a11 configuration entry 11
    * @param a12 configuration entry 12
    * @param a13 configuration entry 13
    * @param a14 configuration entry 14
    * @param a15 configuration entry 15
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
    * @tparam F the [[ConfigEntry]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[F[_]: Functor, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, Z](a1: ConfigEntry[F, _, _, A1], a2: ConfigEntry[F, _, _, A2], a3: ConfigEntry[F, _, _, A3], a4: ConfigEntry[F, _, _, A4], a5: ConfigEntry[F, _, _, A5], a6: ConfigEntry[F, _, _, A6], a7: ConfigEntry[F, _, _, A7], a8: ConfigEntry[F, _, _, A8], a9: ConfigEntry[F, _, _, A9], a10: ConfigEntry[F, _, _, A10], a11: ConfigEntry[F, _, _, A11], a12: ConfigEntry[F, _, _, A12], a13: ConfigEntry[F, _, _, A13], a14: ConfigEntry[F, _, _, A14], a15: ConfigEntry[F, _, _, A15])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15) => Z): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14 append a15).value.map(_.right.map(f.tupled))

  /**
    * Defines a requirement on 15 [[ConfigEntry]]s in order to be
    * able to load a configuration. The method wraps `loadConfig`
    * methods, requiring the provided [[ConfigEntry]]s to be
    * available in order to use the `loadConfig` methods.
    *
    * Deals with error accumulation if there are any errors in
    * the provided [[ConfigEntry]]s.
    *
    * @param a1 configuration entry 1
    * @param a2 configuration entry 2
    * @param a3 configuration entry 3
    * @param a4 configuration entry 4
    * @param a5 configuration entry 5
    * @param a6 configuration entry 6
    * @param a7 configuration entry 7
    * @param a8 configuration entry 8
    * @param a9 configuration entry 9
    * @param a10 configuration entry 10
    * @param a11 configuration entry 11
    * @param a12 configuration entry 12
    * @param a13 configuration entry 13
    * @param a14 configuration entry 14
    * @param a15 configuration entry 15
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
    * @tparam F the [[ConfigEntry]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[F[_]: Monad, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, Z](a1: ConfigEntry[F, _, _, A1], a2: ConfigEntry[F, _, _, A2], a3: ConfigEntry[F, _, _, A3], a4: ConfigEntry[F, _, _, A4], a5: ConfigEntry[F, _, _, A5], a6: ConfigEntry[F, _, _, A6], a7: ConfigEntry[F, _, _, A7], a8: ConfigEntry[F, _, _, A8], a9: ConfigEntry[F, _, _, A9], a10: ConfigEntry[F, _, _, A10], a11: ConfigEntry[F, _, _, A11], a12: ConfigEntry[F, _, _, A12], a13: ConfigEntry[F, _, _, A13], a14: ConfigEntry[F, _, _, A14], a15: ConfigEntry[F, _, _, A15])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15) => F[Either[ConfigErrors, Z]]): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14 append a15).value.flatMap {
       case Left(errors) => (Left(errors): Either[ConfigErrors, Z]).pure[F]
       case Right(values) => f.tupled.apply(values)
     }

  /**
    * Loads a configuration using the 16 specified [[ConfigEntry]]s.
    * Deals with error accumulation if there are any errors in the
    * provided [[ConfigEntry]]s.
    *
    * @param a1 configuration entry 1
    * @param a2 configuration entry 2
    * @param a3 configuration entry 3
    * @param a4 configuration entry 4
    * @param a5 configuration entry 5
    * @param a6 configuration entry 6
    * @param a7 configuration entry 7
    * @param a8 configuration entry 8
    * @param a9 configuration entry 9
    * @param a10 configuration entry 10
    * @param a11 configuration entry 11
    * @param a12 configuration entry 12
    * @param a13 configuration entry 13
    * @param a14 configuration entry 14
    * @param a15 configuration entry 15
    * @param a16 configuration entry 16
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
    * @tparam F the [[ConfigEntry]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[F[_]: Functor, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, Z](a1: ConfigEntry[F, _, _, A1], a2: ConfigEntry[F, _, _, A2], a3: ConfigEntry[F, _, _, A3], a4: ConfigEntry[F, _, _, A4], a5: ConfigEntry[F, _, _, A5], a6: ConfigEntry[F, _, _, A6], a7: ConfigEntry[F, _, _, A7], a8: ConfigEntry[F, _, _, A8], a9: ConfigEntry[F, _, _, A9], a10: ConfigEntry[F, _, _, A10], a11: ConfigEntry[F, _, _, A11], a12: ConfigEntry[F, _, _, A12], a13: ConfigEntry[F, _, _, A13], a14: ConfigEntry[F, _, _, A14], a15: ConfigEntry[F, _, _, A15], a16: ConfigEntry[F, _, _, A16])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16) => Z): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14 append a15 append a16).value.map(_.right.map(f.tupled))

  /**
    * Defines a requirement on 16 [[ConfigEntry]]s in order to be
    * able to load a configuration. The method wraps `loadConfig`
    * methods, requiring the provided [[ConfigEntry]]s to be
    * available in order to use the `loadConfig` methods.
    *
    * Deals with error accumulation if there are any errors in
    * the provided [[ConfigEntry]]s.
    *
    * @param a1 configuration entry 1
    * @param a2 configuration entry 2
    * @param a3 configuration entry 3
    * @param a4 configuration entry 4
    * @param a5 configuration entry 5
    * @param a6 configuration entry 6
    * @param a7 configuration entry 7
    * @param a8 configuration entry 8
    * @param a9 configuration entry 9
    * @param a10 configuration entry 10
    * @param a11 configuration entry 11
    * @param a12 configuration entry 12
    * @param a13 configuration entry 13
    * @param a14 configuration entry 14
    * @param a15 configuration entry 15
    * @param a16 configuration entry 16
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
    * @tparam F the [[ConfigEntry]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[F[_]: Monad, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, Z](a1: ConfigEntry[F, _, _, A1], a2: ConfigEntry[F, _, _, A2], a3: ConfigEntry[F, _, _, A3], a4: ConfigEntry[F, _, _, A4], a5: ConfigEntry[F, _, _, A5], a6: ConfigEntry[F, _, _, A6], a7: ConfigEntry[F, _, _, A7], a8: ConfigEntry[F, _, _, A8], a9: ConfigEntry[F, _, _, A9], a10: ConfigEntry[F, _, _, A10], a11: ConfigEntry[F, _, _, A11], a12: ConfigEntry[F, _, _, A12], a13: ConfigEntry[F, _, _, A13], a14: ConfigEntry[F, _, _, A14], a15: ConfigEntry[F, _, _, A15], a16: ConfigEntry[F, _, _, A16])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16) => F[Either[ConfigErrors, Z]]): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14 append a15 append a16).value.flatMap {
       case Left(errors) => (Left(errors): Either[ConfigErrors, Z]).pure[F]
       case Right(values) => f.tupled.apply(values)
     }

  /**
    * Loads a configuration using the 17 specified [[ConfigEntry]]s.
    * Deals with error accumulation if there are any errors in the
    * provided [[ConfigEntry]]s.
    *
    * @param a1 configuration entry 1
    * @param a2 configuration entry 2
    * @param a3 configuration entry 3
    * @param a4 configuration entry 4
    * @param a5 configuration entry 5
    * @param a6 configuration entry 6
    * @param a7 configuration entry 7
    * @param a8 configuration entry 8
    * @param a9 configuration entry 9
    * @param a10 configuration entry 10
    * @param a11 configuration entry 11
    * @param a12 configuration entry 12
    * @param a13 configuration entry 13
    * @param a14 configuration entry 14
    * @param a15 configuration entry 15
    * @param a16 configuration entry 16
    * @param a17 configuration entry 17
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
    * @tparam F the [[ConfigEntry]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[F[_]: Functor, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, Z](a1: ConfigEntry[F, _, _, A1], a2: ConfigEntry[F, _, _, A2], a3: ConfigEntry[F, _, _, A3], a4: ConfigEntry[F, _, _, A4], a5: ConfigEntry[F, _, _, A5], a6: ConfigEntry[F, _, _, A6], a7: ConfigEntry[F, _, _, A7], a8: ConfigEntry[F, _, _, A8], a9: ConfigEntry[F, _, _, A9], a10: ConfigEntry[F, _, _, A10], a11: ConfigEntry[F, _, _, A11], a12: ConfigEntry[F, _, _, A12], a13: ConfigEntry[F, _, _, A13], a14: ConfigEntry[F, _, _, A14], a15: ConfigEntry[F, _, _, A15], a16: ConfigEntry[F, _, _, A16], a17: ConfigEntry[F, _, _, A17])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17) => Z): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14 append a15 append a16 append a17).value.map(_.right.map(f.tupled))

  /**
    * Defines a requirement on 17 [[ConfigEntry]]s in order to be
    * able to load a configuration. The method wraps `loadConfig`
    * methods, requiring the provided [[ConfigEntry]]s to be
    * available in order to use the `loadConfig` methods.
    *
    * Deals with error accumulation if there are any errors in
    * the provided [[ConfigEntry]]s.
    *
    * @param a1 configuration entry 1
    * @param a2 configuration entry 2
    * @param a3 configuration entry 3
    * @param a4 configuration entry 4
    * @param a5 configuration entry 5
    * @param a6 configuration entry 6
    * @param a7 configuration entry 7
    * @param a8 configuration entry 8
    * @param a9 configuration entry 9
    * @param a10 configuration entry 10
    * @param a11 configuration entry 11
    * @param a12 configuration entry 12
    * @param a13 configuration entry 13
    * @param a14 configuration entry 14
    * @param a15 configuration entry 15
    * @param a16 configuration entry 16
    * @param a17 configuration entry 17
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
    * @tparam F the [[ConfigEntry]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[F[_]: Monad, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, Z](a1: ConfigEntry[F, _, _, A1], a2: ConfigEntry[F, _, _, A2], a3: ConfigEntry[F, _, _, A3], a4: ConfigEntry[F, _, _, A4], a5: ConfigEntry[F, _, _, A5], a6: ConfigEntry[F, _, _, A6], a7: ConfigEntry[F, _, _, A7], a8: ConfigEntry[F, _, _, A8], a9: ConfigEntry[F, _, _, A9], a10: ConfigEntry[F, _, _, A10], a11: ConfigEntry[F, _, _, A11], a12: ConfigEntry[F, _, _, A12], a13: ConfigEntry[F, _, _, A13], a14: ConfigEntry[F, _, _, A14], a15: ConfigEntry[F, _, _, A15], a16: ConfigEntry[F, _, _, A16], a17: ConfigEntry[F, _, _, A17])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17) => F[Either[ConfigErrors, Z]]): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14 append a15 append a16 append a17).value.flatMap {
       case Left(errors) => (Left(errors): Either[ConfigErrors, Z]).pure[F]
       case Right(values) => f.tupled.apply(values)
     }

  /**
    * Loads a configuration using the 18 specified [[ConfigEntry]]s.
    * Deals with error accumulation if there are any errors in the
    * provided [[ConfigEntry]]s.
    *
    * @param a1 configuration entry 1
    * @param a2 configuration entry 2
    * @param a3 configuration entry 3
    * @param a4 configuration entry 4
    * @param a5 configuration entry 5
    * @param a6 configuration entry 6
    * @param a7 configuration entry 7
    * @param a8 configuration entry 8
    * @param a9 configuration entry 9
    * @param a10 configuration entry 10
    * @param a11 configuration entry 11
    * @param a12 configuration entry 12
    * @param a13 configuration entry 13
    * @param a14 configuration entry 14
    * @param a15 configuration entry 15
    * @param a16 configuration entry 16
    * @param a17 configuration entry 17
    * @param a18 configuration entry 18
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
    * @tparam F the [[ConfigEntry]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[F[_]: Functor, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, Z](a1: ConfigEntry[F, _, _, A1], a2: ConfigEntry[F, _, _, A2], a3: ConfigEntry[F, _, _, A3], a4: ConfigEntry[F, _, _, A4], a5: ConfigEntry[F, _, _, A5], a6: ConfigEntry[F, _, _, A6], a7: ConfigEntry[F, _, _, A7], a8: ConfigEntry[F, _, _, A8], a9: ConfigEntry[F, _, _, A9], a10: ConfigEntry[F, _, _, A10], a11: ConfigEntry[F, _, _, A11], a12: ConfigEntry[F, _, _, A12], a13: ConfigEntry[F, _, _, A13], a14: ConfigEntry[F, _, _, A14], a15: ConfigEntry[F, _, _, A15], a16: ConfigEntry[F, _, _, A16], a17: ConfigEntry[F, _, _, A17], a18: ConfigEntry[F, _, _, A18])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18) => Z): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14 append a15 append a16 append a17 append a18).value.map(_.right.map(f.tupled))

  /**
    * Defines a requirement on 18 [[ConfigEntry]]s in order to be
    * able to load a configuration. The method wraps `loadConfig`
    * methods, requiring the provided [[ConfigEntry]]s to be
    * available in order to use the `loadConfig` methods.
    *
    * Deals with error accumulation if there are any errors in
    * the provided [[ConfigEntry]]s.
    *
    * @param a1 configuration entry 1
    * @param a2 configuration entry 2
    * @param a3 configuration entry 3
    * @param a4 configuration entry 4
    * @param a5 configuration entry 5
    * @param a6 configuration entry 6
    * @param a7 configuration entry 7
    * @param a8 configuration entry 8
    * @param a9 configuration entry 9
    * @param a10 configuration entry 10
    * @param a11 configuration entry 11
    * @param a12 configuration entry 12
    * @param a13 configuration entry 13
    * @param a14 configuration entry 14
    * @param a15 configuration entry 15
    * @param a16 configuration entry 16
    * @param a17 configuration entry 17
    * @param a18 configuration entry 18
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
    * @tparam F the [[ConfigEntry]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[F[_]: Monad, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, Z](a1: ConfigEntry[F, _, _, A1], a2: ConfigEntry[F, _, _, A2], a3: ConfigEntry[F, _, _, A3], a4: ConfigEntry[F, _, _, A4], a5: ConfigEntry[F, _, _, A5], a6: ConfigEntry[F, _, _, A6], a7: ConfigEntry[F, _, _, A7], a8: ConfigEntry[F, _, _, A8], a9: ConfigEntry[F, _, _, A9], a10: ConfigEntry[F, _, _, A10], a11: ConfigEntry[F, _, _, A11], a12: ConfigEntry[F, _, _, A12], a13: ConfigEntry[F, _, _, A13], a14: ConfigEntry[F, _, _, A14], a15: ConfigEntry[F, _, _, A15], a16: ConfigEntry[F, _, _, A16], a17: ConfigEntry[F, _, _, A17], a18: ConfigEntry[F, _, _, A18])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18) => F[Either[ConfigErrors, Z]]): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14 append a15 append a16 append a17 append a18).value.flatMap {
       case Left(errors) => (Left(errors): Either[ConfigErrors, Z]).pure[F]
       case Right(values) => f.tupled.apply(values)
     }

  /**
    * Loads a configuration using the 19 specified [[ConfigEntry]]s.
    * Deals with error accumulation if there are any errors in the
    * provided [[ConfigEntry]]s.
    *
    * @param a1 configuration entry 1
    * @param a2 configuration entry 2
    * @param a3 configuration entry 3
    * @param a4 configuration entry 4
    * @param a5 configuration entry 5
    * @param a6 configuration entry 6
    * @param a7 configuration entry 7
    * @param a8 configuration entry 8
    * @param a9 configuration entry 9
    * @param a10 configuration entry 10
    * @param a11 configuration entry 11
    * @param a12 configuration entry 12
    * @param a13 configuration entry 13
    * @param a14 configuration entry 14
    * @param a15 configuration entry 15
    * @param a16 configuration entry 16
    * @param a17 configuration entry 17
    * @param a18 configuration entry 18
    * @param a19 configuration entry 19
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
    * @tparam F the [[ConfigEntry]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[F[_]: Functor, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, Z](a1: ConfigEntry[F, _, _, A1], a2: ConfigEntry[F, _, _, A2], a3: ConfigEntry[F, _, _, A3], a4: ConfigEntry[F, _, _, A4], a5: ConfigEntry[F, _, _, A5], a6: ConfigEntry[F, _, _, A6], a7: ConfigEntry[F, _, _, A7], a8: ConfigEntry[F, _, _, A8], a9: ConfigEntry[F, _, _, A9], a10: ConfigEntry[F, _, _, A10], a11: ConfigEntry[F, _, _, A11], a12: ConfigEntry[F, _, _, A12], a13: ConfigEntry[F, _, _, A13], a14: ConfigEntry[F, _, _, A14], a15: ConfigEntry[F, _, _, A15], a16: ConfigEntry[F, _, _, A16], a17: ConfigEntry[F, _, _, A17], a18: ConfigEntry[F, _, _, A18], a19: ConfigEntry[F, _, _, A19])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19) => Z): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14 append a15 append a16 append a17 append a18 append a19).value.map(_.right.map(f.tupled))

  /**
    * Defines a requirement on 19 [[ConfigEntry]]s in order to be
    * able to load a configuration. The method wraps `loadConfig`
    * methods, requiring the provided [[ConfigEntry]]s to be
    * available in order to use the `loadConfig` methods.
    *
    * Deals with error accumulation if there are any errors in
    * the provided [[ConfigEntry]]s.
    *
    * @param a1 configuration entry 1
    * @param a2 configuration entry 2
    * @param a3 configuration entry 3
    * @param a4 configuration entry 4
    * @param a5 configuration entry 5
    * @param a6 configuration entry 6
    * @param a7 configuration entry 7
    * @param a8 configuration entry 8
    * @param a9 configuration entry 9
    * @param a10 configuration entry 10
    * @param a11 configuration entry 11
    * @param a12 configuration entry 12
    * @param a13 configuration entry 13
    * @param a14 configuration entry 14
    * @param a15 configuration entry 15
    * @param a16 configuration entry 16
    * @param a17 configuration entry 17
    * @param a18 configuration entry 18
    * @param a19 configuration entry 19
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
    * @tparam F the [[ConfigEntry]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[F[_]: Monad, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, Z](a1: ConfigEntry[F, _, _, A1], a2: ConfigEntry[F, _, _, A2], a3: ConfigEntry[F, _, _, A3], a4: ConfigEntry[F, _, _, A4], a5: ConfigEntry[F, _, _, A5], a6: ConfigEntry[F, _, _, A6], a7: ConfigEntry[F, _, _, A7], a8: ConfigEntry[F, _, _, A8], a9: ConfigEntry[F, _, _, A9], a10: ConfigEntry[F, _, _, A10], a11: ConfigEntry[F, _, _, A11], a12: ConfigEntry[F, _, _, A12], a13: ConfigEntry[F, _, _, A13], a14: ConfigEntry[F, _, _, A14], a15: ConfigEntry[F, _, _, A15], a16: ConfigEntry[F, _, _, A16], a17: ConfigEntry[F, _, _, A17], a18: ConfigEntry[F, _, _, A18], a19: ConfigEntry[F, _, _, A19])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19) => F[Either[ConfigErrors, Z]]): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14 append a15 append a16 append a17 append a18 append a19).value.flatMap {
       case Left(errors) => (Left(errors): Either[ConfigErrors, Z]).pure[F]
       case Right(values) => f.tupled.apply(values)
     }

  /**
    * Loads a configuration using the 20 specified [[ConfigEntry]]s.
    * Deals with error accumulation if there are any errors in the
    * provided [[ConfigEntry]]s.
    *
    * @param a1 configuration entry 1
    * @param a2 configuration entry 2
    * @param a3 configuration entry 3
    * @param a4 configuration entry 4
    * @param a5 configuration entry 5
    * @param a6 configuration entry 6
    * @param a7 configuration entry 7
    * @param a8 configuration entry 8
    * @param a9 configuration entry 9
    * @param a10 configuration entry 10
    * @param a11 configuration entry 11
    * @param a12 configuration entry 12
    * @param a13 configuration entry 13
    * @param a14 configuration entry 14
    * @param a15 configuration entry 15
    * @param a16 configuration entry 16
    * @param a17 configuration entry 17
    * @param a18 configuration entry 18
    * @param a19 configuration entry 19
    * @param a20 configuration entry 20
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
    * @tparam F the [[ConfigEntry]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[F[_]: Functor, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, Z](a1: ConfigEntry[F, _, _, A1], a2: ConfigEntry[F, _, _, A2], a3: ConfigEntry[F, _, _, A3], a4: ConfigEntry[F, _, _, A4], a5: ConfigEntry[F, _, _, A5], a6: ConfigEntry[F, _, _, A6], a7: ConfigEntry[F, _, _, A7], a8: ConfigEntry[F, _, _, A8], a9: ConfigEntry[F, _, _, A9], a10: ConfigEntry[F, _, _, A10], a11: ConfigEntry[F, _, _, A11], a12: ConfigEntry[F, _, _, A12], a13: ConfigEntry[F, _, _, A13], a14: ConfigEntry[F, _, _, A14], a15: ConfigEntry[F, _, _, A15], a16: ConfigEntry[F, _, _, A16], a17: ConfigEntry[F, _, _, A17], a18: ConfigEntry[F, _, _, A18], a19: ConfigEntry[F, _, _, A19], a20: ConfigEntry[F, _, _, A20])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20) => Z): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14 append a15 append a16 append a17 append a18 append a19 append a20).value.map(_.right.map(f.tupled))

  /**
    * Defines a requirement on 20 [[ConfigEntry]]s in order to be
    * able to load a configuration. The method wraps `loadConfig`
    * methods, requiring the provided [[ConfigEntry]]s to be
    * available in order to use the `loadConfig` methods.
    *
    * Deals with error accumulation if there are any errors in
    * the provided [[ConfigEntry]]s.
    *
    * @param a1 configuration entry 1
    * @param a2 configuration entry 2
    * @param a3 configuration entry 3
    * @param a4 configuration entry 4
    * @param a5 configuration entry 5
    * @param a6 configuration entry 6
    * @param a7 configuration entry 7
    * @param a8 configuration entry 8
    * @param a9 configuration entry 9
    * @param a10 configuration entry 10
    * @param a11 configuration entry 11
    * @param a12 configuration entry 12
    * @param a13 configuration entry 13
    * @param a14 configuration entry 14
    * @param a15 configuration entry 15
    * @param a16 configuration entry 16
    * @param a17 configuration entry 17
    * @param a18 configuration entry 18
    * @param a19 configuration entry 19
    * @param a20 configuration entry 20
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
    * @tparam F the [[ConfigEntry]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[F[_]: Monad, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, Z](a1: ConfigEntry[F, _, _, A1], a2: ConfigEntry[F, _, _, A2], a3: ConfigEntry[F, _, _, A3], a4: ConfigEntry[F, _, _, A4], a5: ConfigEntry[F, _, _, A5], a6: ConfigEntry[F, _, _, A6], a7: ConfigEntry[F, _, _, A7], a8: ConfigEntry[F, _, _, A8], a9: ConfigEntry[F, _, _, A9], a10: ConfigEntry[F, _, _, A10], a11: ConfigEntry[F, _, _, A11], a12: ConfigEntry[F, _, _, A12], a13: ConfigEntry[F, _, _, A13], a14: ConfigEntry[F, _, _, A14], a15: ConfigEntry[F, _, _, A15], a16: ConfigEntry[F, _, _, A16], a17: ConfigEntry[F, _, _, A17], a18: ConfigEntry[F, _, _, A18], a19: ConfigEntry[F, _, _, A19], a20: ConfigEntry[F, _, _, A20])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20) => F[Either[ConfigErrors, Z]]): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14 append a15 append a16 append a17 append a18 append a19 append a20).value.flatMap {
       case Left(errors) => (Left(errors): Either[ConfigErrors, Z]).pure[F]
       case Right(values) => f.tupled.apply(values)
     }

  /**
    * Loads a configuration using the 21 specified [[ConfigEntry]]s.
    * Deals with error accumulation if there are any errors in the
    * provided [[ConfigEntry]]s.
    *
    * @param a1 configuration entry 1
    * @param a2 configuration entry 2
    * @param a3 configuration entry 3
    * @param a4 configuration entry 4
    * @param a5 configuration entry 5
    * @param a6 configuration entry 6
    * @param a7 configuration entry 7
    * @param a8 configuration entry 8
    * @param a9 configuration entry 9
    * @param a10 configuration entry 10
    * @param a11 configuration entry 11
    * @param a12 configuration entry 12
    * @param a13 configuration entry 13
    * @param a14 configuration entry 14
    * @param a15 configuration entry 15
    * @param a16 configuration entry 16
    * @param a17 configuration entry 17
    * @param a18 configuration entry 18
    * @param a19 configuration entry 19
    * @param a20 configuration entry 20
    * @param a21 configuration entry 21
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
    * @tparam F the [[ConfigEntry]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[F[_]: Functor, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, Z](a1: ConfigEntry[F, _, _, A1], a2: ConfigEntry[F, _, _, A2], a3: ConfigEntry[F, _, _, A3], a4: ConfigEntry[F, _, _, A4], a5: ConfigEntry[F, _, _, A5], a6: ConfigEntry[F, _, _, A6], a7: ConfigEntry[F, _, _, A7], a8: ConfigEntry[F, _, _, A8], a9: ConfigEntry[F, _, _, A9], a10: ConfigEntry[F, _, _, A10], a11: ConfigEntry[F, _, _, A11], a12: ConfigEntry[F, _, _, A12], a13: ConfigEntry[F, _, _, A13], a14: ConfigEntry[F, _, _, A14], a15: ConfigEntry[F, _, _, A15], a16: ConfigEntry[F, _, _, A16], a17: ConfigEntry[F, _, _, A17], a18: ConfigEntry[F, _, _, A18], a19: ConfigEntry[F, _, _, A19], a20: ConfigEntry[F, _, _, A20], a21: ConfigEntry[F, _, _, A21])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21) => Z): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14 append a15 append a16 append a17 append a18 append a19 append a20 append a21).value.map(_.right.map(f.tupled))

  /**
    * Defines a requirement on 21 [[ConfigEntry]]s in order to be
    * able to load a configuration. The method wraps `loadConfig`
    * methods, requiring the provided [[ConfigEntry]]s to be
    * available in order to use the `loadConfig` methods.
    *
    * Deals with error accumulation if there are any errors in
    * the provided [[ConfigEntry]]s.
    *
    * @param a1 configuration entry 1
    * @param a2 configuration entry 2
    * @param a3 configuration entry 3
    * @param a4 configuration entry 4
    * @param a5 configuration entry 5
    * @param a6 configuration entry 6
    * @param a7 configuration entry 7
    * @param a8 configuration entry 8
    * @param a9 configuration entry 9
    * @param a10 configuration entry 10
    * @param a11 configuration entry 11
    * @param a12 configuration entry 12
    * @param a13 configuration entry 13
    * @param a14 configuration entry 14
    * @param a15 configuration entry 15
    * @param a16 configuration entry 16
    * @param a17 configuration entry 17
    * @param a18 configuration entry 18
    * @param a19 configuration entry 19
    * @param a20 configuration entry 20
    * @param a21 configuration entry 21
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
    * @tparam F the [[ConfigEntry]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[F[_]: Monad, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, Z](a1: ConfigEntry[F, _, _, A1], a2: ConfigEntry[F, _, _, A2], a3: ConfigEntry[F, _, _, A3], a4: ConfigEntry[F, _, _, A4], a5: ConfigEntry[F, _, _, A5], a6: ConfigEntry[F, _, _, A6], a7: ConfigEntry[F, _, _, A7], a8: ConfigEntry[F, _, _, A8], a9: ConfigEntry[F, _, _, A9], a10: ConfigEntry[F, _, _, A10], a11: ConfigEntry[F, _, _, A11], a12: ConfigEntry[F, _, _, A12], a13: ConfigEntry[F, _, _, A13], a14: ConfigEntry[F, _, _, A14], a15: ConfigEntry[F, _, _, A15], a16: ConfigEntry[F, _, _, A16], a17: ConfigEntry[F, _, _, A17], a18: ConfigEntry[F, _, _, A18], a19: ConfigEntry[F, _, _, A19], a20: ConfigEntry[F, _, _, A20], a21: ConfigEntry[F, _, _, A21])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21) => F[Either[ConfigErrors, Z]]): F[Either[ConfigErrors, Z]] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14 append a15 append a16 append a17 append a18 append a19 append a20 append a21).value.flatMap {
       case Left(errors) => (Left(errors): Either[ConfigErrors, Z]).pure[F]
       case Right(values) => f.tupled.apply(values)
     }
}
