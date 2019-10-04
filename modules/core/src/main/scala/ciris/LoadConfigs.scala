// format: off

/**
  * Generated using sbt source generators.
  * You should not modify this file directly.
  */

package ciris

import cats.{Applicative, Apply, Monad}
import cats.implicits._
import ciris.ConfigErrors.{left, right}

private[ciris] class LoadConfigs {

  /**
    * Wraps the specified value in a `ConfigResult[F, Z]`. Useful when
    * you want to use a static configuration inside a `withValues`
    * block, requiring you to wrap it with this function.
    *
    * @param z the value to wrap
    * @tparam F the [[ConfigResult]] context
    * @tparam Z the type of the value to wrap
    * @return the value wrapped in a `ConfigResult[F, Z]`
    */
  def loadConfig[F[_]: Applicative, Z](z: Z): ConfigResult[F, Z] =
    ConfigResult(right(z).pure[F])

  /**
    * Loads a configuration using the specified [[ConfigResult]].
    *
    * @param a1 the configuration result
    * @param f the function to create the configuration
    * @tparam F the [[ConfigResult]] context
    * @tparam A1 the type of the configuration result
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[F[_]: Apply, A1, Z](a1: ConfigResult[F, A1])(f: A1 => Z): ConfigResult[F, Z] =
    ConfigResult(a1.result.map(_.map(f)))

  /**
    * Defines a requirement on a single [[ConfigResult]] in order to be
    * able to load a configuration. The method wraps `loadConfig`
    * functions, requiring the provided [[ConfigResult]] to be
    * available in order to use the `loadConfig` functions.
    *
    * @param a1 the configuration result
    * @param f the function to create the configuration
    * @tparam F the [[ConfigResult]] context
    * @tparam A1 the type of the configuration result
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValue[F[_]: Monad, A1, Z](a1: ConfigResult[F, A1])(f: A1 => ConfigResult[F, Z]): ConfigResult[F, Z] =
    withValues(a1)(f)

  /**
    * Defines a requirement on a single [[ConfigResult]] in order to be
    * able to load a configuration. The method wraps any `loadConfig`
    * functions, requiring the provided [[ConfigResult]] to be
    * available in order to use the `loadConfig` functions.
    *
    * @param a1 the configuration result
    * @param f the function to create the configuration
    * @tparam F the [[ConfigResult]] context
    * @tparam A1 the type of the configuration result
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[F[_]: Monad, A1, Z](a1: ConfigResult[F, A1])(f: A1 => ConfigResult[F, Z]): ConfigResult[F, Z] =
    ConfigResult(a1.result.flatMap {
      case Left(errors) => left[Z](errors).pure[F]
      case Right(value) => f(value).result
    })

  /**
    * Loads a configuration using the 2 specified [[ConfigResult]]s.
    * Deals with error accumulation if there are any errors in the
    * provided [[ConfigResult]]s.
    *
    * @param a1 configuration result 1
    * @param a2 configuration result 2
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration result 1
    * @tparam A2 the type for configuration result 2
    * @tparam F the [[ConfigResult]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[F[_]: Apply, A1, A2, Z](a1: ConfigResult[F, A1], a2: ConfigResult[F, A2])(f: (A1, A2) => Z): ConfigResult[F, Z] =
    ConfigResult((a1 append a2).result.map(_.map(f.tupled)))

  /**
    * Defines a requirement on 2 [[ConfigResult]]s in order to be
    * able to load a configuration. The method wraps `loadConfig`
    * methods, requiring the provided [[ConfigResult]]s to be
    * available in order to use the `loadConfig` methods.
    *
    * Deals with error accumulation if there are any errors in
    * the provided [[ConfigResult]]s.
    *
    * @param a1 configuration result 1
    * @param a2 configuration result 2
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration result 1
    * @tparam A2 the type for configuration result 2
    * @tparam F the [[ConfigResult]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[F[_]: Monad, A1, A2, Z](a1: ConfigResult[F, A1], a2: ConfigResult[F, A2])(f: (A1, A2) => ConfigResult[F, Z]): ConfigResult[F, Z] =
    ConfigResult((a1 append a2).result.flatMap {
       case Left(errors) => left[Z](errors).pure[F]
       case Right(values) => f.tupled.apply(values).result
     })

  /**
    * Loads a configuration using the 3 specified [[ConfigResult]]s.
    * Deals with error accumulation if there are any errors in the
    * provided [[ConfigResult]]s.
    *
    * @param a1 configuration result 1
    * @param a2 configuration result 2
    * @param a3 configuration result 3
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration result 1
    * @tparam A2 the type for configuration result 2
    * @tparam A3 the type for configuration result 3
    * @tparam F the [[ConfigResult]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[F[_]: Apply, A1, A2, A3, Z](a1: ConfigResult[F, A1], a2: ConfigResult[F, A2], a3: ConfigResult[F, A3])(f: (A1, A2, A3) => Z): ConfigResult[F, Z] =
    ConfigResult((a1 append a2 append a3).result.map(_.map(f.tupled)))

  /**
    * Defines a requirement on 3 [[ConfigResult]]s in order to be
    * able to load a configuration. The method wraps `loadConfig`
    * methods, requiring the provided [[ConfigResult]]s to be
    * available in order to use the `loadConfig` methods.
    *
    * Deals with error accumulation if there are any errors in
    * the provided [[ConfigResult]]s.
    *
    * @param a1 configuration result 1
    * @param a2 configuration result 2
    * @param a3 configuration result 3
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration result 1
    * @tparam A2 the type for configuration result 2
    * @tparam A3 the type for configuration result 3
    * @tparam F the [[ConfigResult]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[F[_]: Monad, A1, A2, A3, Z](a1: ConfigResult[F, A1], a2: ConfigResult[F, A2], a3: ConfigResult[F, A3])(f: (A1, A2, A3) => ConfigResult[F, Z]): ConfigResult[F, Z] =
    ConfigResult((a1 append a2 append a3).result.flatMap {
       case Left(errors) => left[Z](errors).pure[F]
       case Right(values) => f.tupled.apply(values).result
     })

  /**
    * Loads a configuration using the 4 specified [[ConfigResult]]s.
    * Deals with error accumulation if there are any errors in the
    * provided [[ConfigResult]]s.
    *
    * @param a1 configuration result 1
    * @param a2 configuration result 2
    * @param a3 configuration result 3
    * @param a4 configuration result 4
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration result 1
    * @tparam A2 the type for configuration result 2
    * @tparam A3 the type for configuration result 3
    * @tparam A4 the type for configuration result 4
    * @tparam F the [[ConfigResult]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[F[_]: Apply, A1, A2, A3, A4, Z](a1: ConfigResult[F, A1], a2: ConfigResult[F, A2], a3: ConfigResult[F, A3], a4: ConfigResult[F, A4])(f: (A1, A2, A3, A4) => Z): ConfigResult[F, Z] =
    ConfigResult((a1 append a2 append a3 append a4).result.map(_.map(f.tupled)))

  /**
    * Defines a requirement on 4 [[ConfigResult]]s in order to be
    * able to load a configuration. The method wraps `loadConfig`
    * methods, requiring the provided [[ConfigResult]]s to be
    * available in order to use the `loadConfig` methods.
    *
    * Deals with error accumulation if there are any errors in
    * the provided [[ConfigResult]]s.
    *
    * @param a1 configuration result 1
    * @param a2 configuration result 2
    * @param a3 configuration result 3
    * @param a4 configuration result 4
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration result 1
    * @tparam A2 the type for configuration result 2
    * @tparam A3 the type for configuration result 3
    * @tparam A4 the type for configuration result 4
    * @tparam F the [[ConfigResult]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[F[_]: Monad, A1, A2, A3, A4, Z](a1: ConfigResult[F, A1], a2: ConfigResult[F, A2], a3: ConfigResult[F, A3], a4: ConfigResult[F, A4])(f: (A1, A2, A3, A4) => ConfigResult[F, Z]): ConfigResult[F, Z] =
    ConfigResult((a1 append a2 append a3 append a4).result.flatMap {
       case Left(errors) => left[Z](errors).pure[F]
       case Right(values) => f.tupled.apply(values).result
     })

  /**
    * Loads a configuration using the 5 specified [[ConfigResult]]s.
    * Deals with error accumulation if there are any errors in the
    * provided [[ConfigResult]]s.
    *
    * @param a1 configuration result 1
    * @param a2 configuration result 2
    * @param a3 configuration result 3
    * @param a4 configuration result 4
    * @param a5 configuration result 5
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration result 1
    * @tparam A2 the type for configuration result 2
    * @tparam A3 the type for configuration result 3
    * @tparam A4 the type for configuration result 4
    * @tparam A5 the type for configuration result 5
    * @tparam F the [[ConfigResult]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[F[_]: Apply, A1, A2, A3, A4, A5, Z](a1: ConfigResult[F, A1], a2: ConfigResult[F, A2], a3: ConfigResult[F, A3], a4: ConfigResult[F, A4], a5: ConfigResult[F, A5])(f: (A1, A2, A3, A4, A5) => Z): ConfigResult[F, Z] =
    ConfigResult((a1 append a2 append a3 append a4 append a5).result.map(_.map(f.tupled)))

  /**
    * Defines a requirement on 5 [[ConfigResult]]s in order to be
    * able to load a configuration. The method wraps `loadConfig`
    * methods, requiring the provided [[ConfigResult]]s to be
    * available in order to use the `loadConfig` methods.
    *
    * Deals with error accumulation if there are any errors in
    * the provided [[ConfigResult]]s.
    *
    * @param a1 configuration result 1
    * @param a2 configuration result 2
    * @param a3 configuration result 3
    * @param a4 configuration result 4
    * @param a5 configuration result 5
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration result 1
    * @tparam A2 the type for configuration result 2
    * @tparam A3 the type for configuration result 3
    * @tparam A4 the type for configuration result 4
    * @tparam A5 the type for configuration result 5
    * @tparam F the [[ConfigResult]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[F[_]: Monad, A1, A2, A3, A4, A5, Z](a1: ConfigResult[F, A1], a2: ConfigResult[F, A2], a3: ConfigResult[F, A3], a4: ConfigResult[F, A4], a5: ConfigResult[F, A5])(f: (A1, A2, A3, A4, A5) => ConfigResult[F, Z]): ConfigResult[F, Z] =
    ConfigResult((a1 append a2 append a3 append a4 append a5).result.flatMap {
       case Left(errors) => left[Z](errors).pure[F]
       case Right(values) => f.tupled.apply(values).result
     })

  /**
    * Loads a configuration using the 6 specified [[ConfigResult]]s.
    * Deals with error accumulation if there are any errors in the
    * provided [[ConfigResult]]s.
    *
    * @param a1 configuration result 1
    * @param a2 configuration result 2
    * @param a3 configuration result 3
    * @param a4 configuration result 4
    * @param a5 configuration result 5
    * @param a6 configuration result 6
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration result 1
    * @tparam A2 the type for configuration result 2
    * @tparam A3 the type for configuration result 3
    * @tparam A4 the type for configuration result 4
    * @tparam A5 the type for configuration result 5
    * @tparam A6 the type for configuration result 6
    * @tparam F the [[ConfigResult]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[F[_]: Apply, A1, A2, A3, A4, A5, A6, Z](a1: ConfigResult[F, A1], a2: ConfigResult[F, A2], a3: ConfigResult[F, A3], a4: ConfigResult[F, A4], a5: ConfigResult[F, A5], a6: ConfigResult[F, A6])(f: (A1, A2, A3, A4, A5, A6) => Z): ConfigResult[F, Z] =
    ConfigResult((a1 append a2 append a3 append a4 append a5 append a6).result.map(_.map(f.tupled)))

  /**
    * Defines a requirement on 6 [[ConfigResult]]s in order to be
    * able to load a configuration. The method wraps `loadConfig`
    * methods, requiring the provided [[ConfigResult]]s to be
    * available in order to use the `loadConfig` methods.
    *
    * Deals with error accumulation if there are any errors in
    * the provided [[ConfigResult]]s.
    *
    * @param a1 configuration result 1
    * @param a2 configuration result 2
    * @param a3 configuration result 3
    * @param a4 configuration result 4
    * @param a5 configuration result 5
    * @param a6 configuration result 6
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration result 1
    * @tparam A2 the type for configuration result 2
    * @tparam A3 the type for configuration result 3
    * @tparam A4 the type for configuration result 4
    * @tparam A5 the type for configuration result 5
    * @tparam A6 the type for configuration result 6
    * @tparam F the [[ConfigResult]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[F[_]: Monad, A1, A2, A3, A4, A5, A6, Z](a1: ConfigResult[F, A1], a2: ConfigResult[F, A2], a3: ConfigResult[F, A3], a4: ConfigResult[F, A4], a5: ConfigResult[F, A5], a6: ConfigResult[F, A6])(f: (A1, A2, A3, A4, A5, A6) => ConfigResult[F, Z]): ConfigResult[F, Z] =
    ConfigResult((a1 append a2 append a3 append a4 append a5 append a6).result.flatMap {
       case Left(errors) => left[Z](errors).pure[F]
       case Right(values) => f.tupled.apply(values).result
     })

  /**
    * Loads a configuration using the 7 specified [[ConfigResult]]s.
    * Deals with error accumulation if there are any errors in the
    * provided [[ConfigResult]]s.
    *
    * @param a1 configuration result 1
    * @param a2 configuration result 2
    * @param a3 configuration result 3
    * @param a4 configuration result 4
    * @param a5 configuration result 5
    * @param a6 configuration result 6
    * @param a7 configuration result 7
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration result 1
    * @tparam A2 the type for configuration result 2
    * @tparam A3 the type for configuration result 3
    * @tparam A4 the type for configuration result 4
    * @tparam A5 the type for configuration result 5
    * @tparam A6 the type for configuration result 6
    * @tparam A7 the type for configuration result 7
    * @tparam F the [[ConfigResult]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[F[_]: Apply, A1, A2, A3, A4, A5, A6, A7, Z](a1: ConfigResult[F, A1], a2: ConfigResult[F, A2], a3: ConfigResult[F, A3], a4: ConfigResult[F, A4], a5: ConfigResult[F, A5], a6: ConfigResult[F, A6], a7: ConfigResult[F, A7])(f: (A1, A2, A3, A4, A5, A6, A7) => Z): ConfigResult[F, Z] =
    ConfigResult((a1 append a2 append a3 append a4 append a5 append a6 append a7).result.map(_.map(f.tupled)))

  /**
    * Defines a requirement on 7 [[ConfigResult]]s in order to be
    * able to load a configuration. The method wraps `loadConfig`
    * methods, requiring the provided [[ConfigResult]]s to be
    * available in order to use the `loadConfig` methods.
    *
    * Deals with error accumulation if there are any errors in
    * the provided [[ConfigResult]]s.
    *
    * @param a1 configuration result 1
    * @param a2 configuration result 2
    * @param a3 configuration result 3
    * @param a4 configuration result 4
    * @param a5 configuration result 5
    * @param a6 configuration result 6
    * @param a7 configuration result 7
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration result 1
    * @tparam A2 the type for configuration result 2
    * @tparam A3 the type for configuration result 3
    * @tparam A4 the type for configuration result 4
    * @tparam A5 the type for configuration result 5
    * @tparam A6 the type for configuration result 6
    * @tparam A7 the type for configuration result 7
    * @tparam F the [[ConfigResult]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[F[_]: Monad, A1, A2, A3, A4, A5, A6, A7, Z](a1: ConfigResult[F, A1], a2: ConfigResult[F, A2], a3: ConfigResult[F, A3], a4: ConfigResult[F, A4], a5: ConfigResult[F, A5], a6: ConfigResult[F, A6], a7: ConfigResult[F, A7])(f: (A1, A2, A3, A4, A5, A6, A7) => ConfigResult[F, Z]): ConfigResult[F, Z] =
    ConfigResult((a1 append a2 append a3 append a4 append a5 append a6 append a7).result.flatMap {
       case Left(errors) => left[Z](errors).pure[F]
       case Right(values) => f.tupled.apply(values).result
     })

  /**
    * Loads a configuration using the 8 specified [[ConfigResult]]s.
    * Deals with error accumulation if there are any errors in the
    * provided [[ConfigResult]]s.
    *
    * @param a1 configuration result 1
    * @param a2 configuration result 2
    * @param a3 configuration result 3
    * @param a4 configuration result 4
    * @param a5 configuration result 5
    * @param a6 configuration result 6
    * @param a7 configuration result 7
    * @param a8 configuration result 8
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration result 1
    * @tparam A2 the type for configuration result 2
    * @tparam A3 the type for configuration result 3
    * @tparam A4 the type for configuration result 4
    * @tparam A5 the type for configuration result 5
    * @tparam A6 the type for configuration result 6
    * @tparam A7 the type for configuration result 7
    * @tparam A8 the type for configuration result 8
    * @tparam F the [[ConfigResult]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[F[_]: Apply, A1, A2, A3, A4, A5, A6, A7, A8, Z](a1: ConfigResult[F, A1], a2: ConfigResult[F, A2], a3: ConfigResult[F, A3], a4: ConfigResult[F, A4], a5: ConfigResult[F, A5], a6: ConfigResult[F, A6], a7: ConfigResult[F, A7], a8: ConfigResult[F, A8])(f: (A1, A2, A3, A4, A5, A6, A7, A8) => Z): ConfigResult[F, Z] =
    ConfigResult((a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8).result.map(_.map(f.tupled)))

  /**
    * Defines a requirement on 8 [[ConfigResult]]s in order to be
    * able to load a configuration. The method wraps `loadConfig`
    * methods, requiring the provided [[ConfigResult]]s to be
    * available in order to use the `loadConfig` methods.
    *
    * Deals with error accumulation if there are any errors in
    * the provided [[ConfigResult]]s.
    *
    * @param a1 configuration result 1
    * @param a2 configuration result 2
    * @param a3 configuration result 3
    * @param a4 configuration result 4
    * @param a5 configuration result 5
    * @param a6 configuration result 6
    * @param a7 configuration result 7
    * @param a8 configuration result 8
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration result 1
    * @tparam A2 the type for configuration result 2
    * @tparam A3 the type for configuration result 3
    * @tparam A4 the type for configuration result 4
    * @tparam A5 the type for configuration result 5
    * @tparam A6 the type for configuration result 6
    * @tparam A7 the type for configuration result 7
    * @tparam A8 the type for configuration result 8
    * @tparam F the [[ConfigResult]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[F[_]: Monad, A1, A2, A3, A4, A5, A6, A7, A8, Z](a1: ConfigResult[F, A1], a2: ConfigResult[F, A2], a3: ConfigResult[F, A3], a4: ConfigResult[F, A4], a5: ConfigResult[F, A5], a6: ConfigResult[F, A6], a7: ConfigResult[F, A7], a8: ConfigResult[F, A8])(f: (A1, A2, A3, A4, A5, A6, A7, A8) => ConfigResult[F, Z]): ConfigResult[F, Z] =
    ConfigResult((a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8).result.flatMap {
       case Left(errors) => left[Z](errors).pure[F]
       case Right(values) => f.tupled.apply(values).result
     })

  /**
    * Loads a configuration using the 9 specified [[ConfigResult]]s.
    * Deals with error accumulation if there are any errors in the
    * provided [[ConfigResult]]s.
    *
    * @param a1 configuration result 1
    * @param a2 configuration result 2
    * @param a3 configuration result 3
    * @param a4 configuration result 4
    * @param a5 configuration result 5
    * @param a6 configuration result 6
    * @param a7 configuration result 7
    * @param a8 configuration result 8
    * @param a9 configuration result 9
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration result 1
    * @tparam A2 the type for configuration result 2
    * @tparam A3 the type for configuration result 3
    * @tparam A4 the type for configuration result 4
    * @tparam A5 the type for configuration result 5
    * @tparam A6 the type for configuration result 6
    * @tparam A7 the type for configuration result 7
    * @tparam A8 the type for configuration result 8
    * @tparam A9 the type for configuration result 9
    * @tparam F the [[ConfigResult]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[F[_]: Apply, A1, A2, A3, A4, A5, A6, A7, A8, A9, Z](a1: ConfigResult[F, A1], a2: ConfigResult[F, A2], a3: ConfigResult[F, A3], a4: ConfigResult[F, A4], a5: ConfigResult[F, A5], a6: ConfigResult[F, A6], a7: ConfigResult[F, A7], a8: ConfigResult[F, A8], a9: ConfigResult[F, A9])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9) => Z): ConfigResult[F, Z] =
    ConfigResult((a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9).result.map(_.map(f.tupled)))

  /**
    * Defines a requirement on 9 [[ConfigResult]]s in order to be
    * able to load a configuration. The method wraps `loadConfig`
    * methods, requiring the provided [[ConfigResult]]s to be
    * available in order to use the `loadConfig` methods.
    *
    * Deals with error accumulation if there are any errors in
    * the provided [[ConfigResult]]s.
    *
    * @param a1 configuration result 1
    * @param a2 configuration result 2
    * @param a3 configuration result 3
    * @param a4 configuration result 4
    * @param a5 configuration result 5
    * @param a6 configuration result 6
    * @param a7 configuration result 7
    * @param a8 configuration result 8
    * @param a9 configuration result 9
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration result 1
    * @tparam A2 the type for configuration result 2
    * @tparam A3 the type for configuration result 3
    * @tparam A4 the type for configuration result 4
    * @tparam A5 the type for configuration result 5
    * @tparam A6 the type for configuration result 6
    * @tparam A7 the type for configuration result 7
    * @tparam A8 the type for configuration result 8
    * @tparam A9 the type for configuration result 9
    * @tparam F the [[ConfigResult]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[F[_]: Monad, A1, A2, A3, A4, A5, A6, A7, A8, A9, Z](a1: ConfigResult[F, A1], a2: ConfigResult[F, A2], a3: ConfigResult[F, A3], a4: ConfigResult[F, A4], a5: ConfigResult[F, A5], a6: ConfigResult[F, A6], a7: ConfigResult[F, A7], a8: ConfigResult[F, A8], a9: ConfigResult[F, A9])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9) => ConfigResult[F, Z]): ConfigResult[F, Z] =
    ConfigResult((a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9).result.flatMap {
       case Left(errors) => left[Z](errors).pure[F]
       case Right(values) => f.tupled.apply(values).result
     })

  /**
    * Loads a configuration using the 10 specified [[ConfigResult]]s.
    * Deals with error accumulation if there are any errors in the
    * provided [[ConfigResult]]s.
    *
    * @param a1 configuration result 1
    * @param a2 configuration result 2
    * @param a3 configuration result 3
    * @param a4 configuration result 4
    * @param a5 configuration result 5
    * @param a6 configuration result 6
    * @param a7 configuration result 7
    * @param a8 configuration result 8
    * @param a9 configuration result 9
    * @param a10 configuration result 10
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration result 1
    * @tparam A2 the type for configuration result 2
    * @tparam A3 the type for configuration result 3
    * @tparam A4 the type for configuration result 4
    * @tparam A5 the type for configuration result 5
    * @tparam A6 the type for configuration result 6
    * @tparam A7 the type for configuration result 7
    * @tparam A8 the type for configuration result 8
    * @tparam A9 the type for configuration result 9
    * @tparam A10 the type for configuration result 10
    * @tparam F the [[ConfigResult]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[F[_]: Apply, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, Z](a1: ConfigResult[F, A1], a2: ConfigResult[F, A2], a3: ConfigResult[F, A3], a4: ConfigResult[F, A4], a5: ConfigResult[F, A5], a6: ConfigResult[F, A6], a7: ConfigResult[F, A7], a8: ConfigResult[F, A8], a9: ConfigResult[F, A9], a10: ConfigResult[F, A10])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10) => Z): ConfigResult[F, Z] =
    ConfigResult((a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10).result.map(_.map(f.tupled)))

  /**
    * Defines a requirement on 10 [[ConfigResult]]s in order to be
    * able to load a configuration. The method wraps `loadConfig`
    * methods, requiring the provided [[ConfigResult]]s to be
    * available in order to use the `loadConfig` methods.
    *
    * Deals with error accumulation if there are any errors in
    * the provided [[ConfigResult]]s.
    *
    * @param a1 configuration result 1
    * @param a2 configuration result 2
    * @param a3 configuration result 3
    * @param a4 configuration result 4
    * @param a5 configuration result 5
    * @param a6 configuration result 6
    * @param a7 configuration result 7
    * @param a8 configuration result 8
    * @param a9 configuration result 9
    * @param a10 configuration result 10
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration result 1
    * @tparam A2 the type for configuration result 2
    * @tparam A3 the type for configuration result 3
    * @tparam A4 the type for configuration result 4
    * @tparam A5 the type for configuration result 5
    * @tparam A6 the type for configuration result 6
    * @tparam A7 the type for configuration result 7
    * @tparam A8 the type for configuration result 8
    * @tparam A9 the type for configuration result 9
    * @tparam A10 the type for configuration result 10
    * @tparam F the [[ConfigResult]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[F[_]: Monad, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, Z](a1: ConfigResult[F, A1], a2: ConfigResult[F, A2], a3: ConfigResult[F, A3], a4: ConfigResult[F, A4], a5: ConfigResult[F, A5], a6: ConfigResult[F, A6], a7: ConfigResult[F, A7], a8: ConfigResult[F, A8], a9: ConfigResult[F, A9], a10: ConfigResult[F, A10])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10) => ConfigResult[F, Z]): ConfigResult[F, Z] =
    ConfigResult((a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10).result.flatMap {
       case Left(errors) => left[Z](errors).pure[F]
       case Right(values) => f.tupled.apply(values).result
     })

  /**
    * Loads a configuration using the 11 specified [[ConfigResult]]s.
    * Deals with error accumulation if there are any errors in the
    * provided [[ConfigResult]]s.
    *
    * @param a1 configuration result 1
    * @param a2 configuration result 2
    * @param a3 configuration result 3
    * @param a4 configuration result 4
    * @param a5 configuration result 5
    * @param a6 configuration result 6
    * @param a7 configuration result 7
    * @param a8 configuration result 8
    * @param a9 configuration result 9
    * @param a10 configuration result 10
    * @param a11 configuration result 11
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration result 1
    * @tparam A2 the type for configuration result 2
    * @tparam A3 the type for configuration result 3
    * @tparam A4 the type for configuration result 4
    * @tparam A5 the type for configuration result 5
    * @tparam A6 the type for configuration result 6
    * @tparam A7 the type for configuration result 7
    * @tparam A8 the type for configuration result 8
    * @tparam A9 the type for configuration result 9
    * @tparam A10 the type for configuration result 10
    * @tparam A11 the type for configuration result 11
    * @tparam F the [[ConfigResult]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[F[_]: Apply, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, Z](a1: ConfigResult[F, A1], a2: ConfigResult[F, A2], a3: ConfigResult[F, A3], a4: ConfigResult[F, A4], a5: ConfigResult[F, A5], a6: ConfigResult[F, A6], a7: ConfigResult[F, A7], a8: ConfigResult[F, A8], a9: ConfigResult[F, A9], a10: ConfigResult[F, A10], a11: ConfigResult[F, A11])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11) => Z): ConfigResult[F, Z] =
    ConfigResult((a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11).result.map(_.map(f.tupled)))

  /**
    * Defines a requirement on 11 [[ConfigResult]]s in order to be
    * able to load a configuration. The method wraps `loadConfig`
    * methods, requiring the provided [[ConfigResult]]s to be
    * available in order to use the `loadConfig` methods.
    *
    * Deals with error accumulation if there are any errors in
    * the provided [[ConfigResult]]s.
    *
    * @param a1 configuration result 1
    * @param a2 configuration result 2
    * @param a3 configuration result 3
    * @param a4 configuration result 4
    * @param a5 configuration result 5
    * @param a6 configuration result 6
    * @param a7 configuration result 7
    * @param a8 configuration result 8
    * @param a9 configuration result 9
    * @param a10 configuration result 10
    * @param a11 configuration result 11
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration result 1
    * @tparam A2 the type for configuration result 2
    * @tparam A3 the type for configuration result 3
    * @tparam A4 the type for configuration result 4
    * @tparam A5 the type for configuration result 5
    * @tparam A6 the type for configuration result 6
    * @tparam A7 the type for configuration result 7
    * @tparam A8 the type for configuration result 8
    * @tparam A9 the type for configuration result 9
    * @tparam A10 the type for configuration result 10
    * @tparam A11 the type for configuration result 11
    * @tparam F the [[ConfigResult]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[F[_]: Monad, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, Z](a1: ConfigResult[F, A1], a2: ConfigResult[F, A2], a3: ConfigResult[F, A3], a4: ConfigResult[F, A4], a5: ConfigResult[F, A5], a6: ConfigResult[F, A6], a7: ConfigResult[F, A7], a8: ConfigResult[F, A8], a9: ConfigResult[F, A9], a10: ConfigResult[F, A10], a11: ConfigResult[F, A11])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11) => ConfigResult[F, Z]): ConfigResult[F, Z] =
    ConfigResult((a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11).result.flatMap {
       case Left(errors) => left[Z](errors).pure[F]
       case Right(values) => f.tupled.apply(values).result
     })

  /**
    * Loads a configuration using the 12 specified [[ConfigResult]]s.
    * Deals with error accumulation if there are any errors in the
    * provided [[ConfigResult]]s.
    *
    * @param a1 configuration result 1
    * @param a2 configuration result 2
    * @param a3 configuration result 3
    * @param a4 configuration result 4
    * @param a5 configuration result 5
    * @param a6 configuration result 6
    * @param a7 configuration result 7
    * @param a8 configuration result 8
    * @param a9 configuration result 9
    * @param a10 configuration result 10
    * @param a11 configuration result 11
    * @param a12 configuration result 12
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration result 1
    * @tparam A2 the type for configuration result 2
    * @tparam A3 the type for configuration result 3
    * @tparam A4 the type for configuration result 4
    * @tparam A5 the type for configuration result 5
    * @tparam A6 the type for configuration result 6
    * @tparam A7 the type for configuration result 7
    * @tparam A8 the type for configuration result 8
    * @tparam A9 the type for configuration result 9
    * @tparam A10 the type for configuration result 10
    * @tparam A11 the type for configuration result 11
    * @tparam A12 the type for configuration result 12
    * @tparam F the [[ConfigResult]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[F[_]: Apply, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, Z](a1: ConfigResult[F, A1], a2: ConfigResult[F, A2], a3: ConfigResult[F, A3], a4: ConfigResult[F, A4], a5: ConfigResult[F, A5], a6: ConfigResult[F, A6], a7: ConfigResult[F, A7], a8: ConfigResult[F, A8], a9: ConfigResult[F, A9], a10: ConfigResult[F, A10], a11: ConfigResult[F, A11], a12: ConfigResult[F, A12])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12) => Z): ConfigResult[F, Z] =
    ConfigResult((a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12).result.map(_.map(f.tupled)))

  /**
    * Defines a requirement on 12 [[ConfigResult]]s in order to be
    * able to load a configuration. The method wraps `loadConfig`
    * methods, requiring the provided [[ConfigResult]]s to be
    * available in order to use the `loadConfig` methods.
    *
    * Deals with error accumulation if there are any errors in
    * the provided [[ConfigResult]]s.
    *
    * @param a1 configuration result 1
    * @param a2 configuration result 2
    * @param a3 configuration result 3
    * @param a4 configuration result 4
    * @param a5 configuration result 5
    * @param a6 configuration result 6
    * @param a7 configuration result 7
    * @param a8 configuration result 8
    * @param a9 configuration result 9
    * @param a10 configuration result 10
    * @param a11 configuration result 11
    * @param a12 configuration result 12
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration result 1
    * @tparam A2 the type for configuration result 2
    * @tparam A3 the type for configuration result 3
    * @tparam A4 the type for configuration result 4
    * @tparam A5 the type for configuration result 5
    * @tparam A6 the type for configuration result 6
    * @tparam A7 the type for configuration result 7
    * @tparam A8 the type for configuration result 8
    * @tparam A9 the type for configuration result 9
    * @tparam A10 the type for configuration result 10
    * @tparam A11 the type for configuration result 11
    * @tparam A12 the type for configuration result 12
    * @tparam F the [[ConfigResult]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[F[_]: Monad, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, Z](a1: ConfigResult[F, A1], a2: ConfigResult[F, A2], a3: ConfigResult[F, A3], a4: ConfigResult[F, A4], a5: ConfigResult[F, A5], a6: ConfigResult[F, A6], a7: ConfigResult[F, A7], a8: ConfigResult[F, A8], a9: ConfigResult[F, A9], a10: ConfigResult[F, A10], a11: ConfigResult[F, A11], a12: ConfigResult[F, A12])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12) => ConfigResult[F, Z]): ConfigResult[F, Z] =
    ConfigResult((a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12).result.flatMap {
       case Left(errors) => left[Z](errors).pure[F]
       case Right(values) => f.tupled.apply(values).result
     })

  /**
    * Loads a configuration using the 13 specified [[ConfigResult]]s.
    * Deals with error accumulation if there are any errors in the
    * provided [[ConfigResult]]s.
    *
    * @param a1 configuration result 1
    * @param a2 configuration result 2
    * @param a3 configuration result 3
    * @param a4 configuration result 4
    * @param a5 configuration result 5
    * @param a6 configuration result 6
    * @param a7 configuration result 7
    * @param a8 configuration result 8
    * @param a9 configuration result 9
    * @param a10 configuration result 10
    * @param a11 configuration result 11
    * @param a12 configuration result 12
    * @param a13 configuration result 13
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration result 1
    * @tparam A2 the type for configuration result 2
    * @tparam A3 the type for configuration result 3
    * @tparam A4 the type for configuration result 4
    * @tparam A5 the type for configuration result 5
    * @tparam A6 the type for configuration result 6
    * @tparam A7 the type for configuration result 7
    * @tparam A8 the type for configuration result 8
    * @tparam A9 the type for configuration result 9
    * @tparam A10 the type for configuration result 10
    * @tparam A11 the type for configuration result 11
    * @tparam A12 the type for configuration result 12
    * @tparam A13 the type for configuration result 13
    * @tparam F the [[ConfigResult]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[F[_]: Apply, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, Z](a1: ConfigResult[F, A1], a2: ConfigResult[F, A2], a3: ConfigResult[F, A3], a4: ConfigResult[F, A4], a5: ConfigResult[F, A5], a6: ConfigResult[F, A6], a7: ConfigResult[F, A7], a8: ConfigResult[F, A8], a9: ConfigResult[F, A9], a10: ConfigResult[F, A10], a11: ConfigResult[F, A11], a12: ConfigResult[F, A12], a13: ConfigResult[F, A13])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13) => Z): ConfigResult[F, Z] =
    ConfigResult((a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13).result.map(_.map(f.tupled)))

  /**
    * Defines a requirement on 13 [[ConfigResult]]s in order to be
    * able to load a configuration. The method wraps `loadConfig`
    * methods, requiring the provided [[ConfigResult]]s to be
    * available in order to use the `loadConfig` methods.
    *
    * Deals with error accumulation if there are any errors in
    * the provided [[ConfigResult]]s.
    *
    * @param a1 configuration result 1
    * @param a2 configuration result 2
    * @param a3 configuration result 3
    * @param a4 configuration result 4
    * @param a5 configuration result 5
    * @param a6 configuration result 6
    * @param a7 configuration result 7
    * @param a8 configuration result 8
    * @param a9 configuration result 9
    * @param a10 configuration result 10
    * @param a11 configuration result 11
    * @param a12 configuration result 12
    * @param a13 configuration result 13
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration result 1
    * @tparam A2 the type for configuration result 2
    * @tparam A3 the type for configuration result 3
    * @tparam A4 the type for configuration result 4
    * @tparam A5 the type for configuration result 5
    * @tparam A6 the type for configuration result 6
    * @tparam A7 the type for configuration result 7
    * @tparam A8 the type for configuration result 8
    * @tparam A9 the type for configuration result 9
    * @tparam A10 the type for configuration result 10
    * @tparam A11 the type for configuration result 11
    * @tparam A12 the type for configuration result 12
    * @tparam A13 the type for configuration result 13
    * @tparam F the [[ConfigResult]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[F[_]: Monad, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, Z](a1: ConfigResult[F, A1], a2: ConfigResult[F, A2], a3: ConfigResult[F, A3], a4: ConfigResult[F, A4], a5: ConfigResult[F, A5], a6: ConfigResult[F, A6], a7: ConfigResult[F, A7], a8: ConfigResult[F, A8], a9: ConfigResult[F, A9], a10: ConfigResult[F, A10], a11: ConfigResult[F, A11], a12: ConfigResult[F, A12], a13: ConfigResult[F, A13])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13) => ConfigResult[F, Z]): ConfigResult[F, Z] =
    ConfigResult((a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13).result.flatMap {
       case Left(errors) => left[Z](errors).pure[F]
       case Right(values) => f.tupled.apply(values).result
     })

  /**
    * Loads a configuration using the 14 specified [[ConfigResult]]s.
    * Deals with error accumulation if there are any errors in the
    * provided [[ConfigResult]]s.
    *
    * @param a1 configuration result 1
    * @param a2 configuration result 2
    * @param a3 configuration result 3
    * @param a4 configuration result 4
    * @param a5 configuration result 5
    * @param a6 configuration result 6
    * @param a7 configuration result 7
    * @param a8 configuration result 8
    * @param a9 configuration result 9
    * @param a10 configuration result 10
    * @param a11 configuration result 11
    * @param a12 configuration result 12
    * @param a13 configuration result 13
    * @param a14 configuration result 14
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration result 1
    * @tparam A2 the type for configuration result 2
    * @tparam A3 the type for configuration result 3
    * @tparam A4 the type for configuration result 4
    * @tparam A5 the type for configuration result 5
    * @tparam A6 the type for configuration result 6
    * @tparam A7 the type for configuration result 7
    * @tparam A8 the type for configuration result 8
    * @tparam A9 the type for configuration result 9
    * @tparam A10 the type for configuration result 10
    * @tparam A11 the type for configuration result 11
    * @tparam A12 the type for configuration result 12
    * @tparam A13 the type for configuration result 13
    * @tparam A14 the type for configuration result 14
    * @tparam F the [[ConfigResult]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[F[_]: Apply, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, Z](a1: ConfigResult[F, A1], a2: ConfigResult[F, A2], a3: ConfigResult[F, A3], a4: ConfigResult[F, A4], a5: ConfigResult[F, A5], a6: ConfigResult[F, A6], a7: ConfigResult[F, A7], a8: ConfigResult[F, A8], a9: ConfigResult[F, A9], a10: ConfigResult[F, A10], a11: ConfigResult[F, A11], a12: ConfigResult[F, A12], a13: ConfigResult[F, A13], a14: ConfigResult[F, A14])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14) => Z): ConfigResult[F, Z] =
    ConfigResult((a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14).result.map(_.map(f.tupled)))

  /**
    * Defines a requirement on 14 [[ConfigResult]]s in order to be
    * able to load a configuration. The method wraps `loadConfig`
    * methods, requiring the provided [[ConfigResult]]s to be
    * available in order to use the `loadConfig` methods.
    *
    * Deals with error accumulation if there are any errors in
    * the provided [[ConfigResult]]s.
    *
    * @param a1 configuration result 1
    * @param a2 configuration result 2
    * @param a3 configuration result 3
    * @param a4 configuration result 4
    * @param a5 configuration result 5
    * @param a6 configuration result 6
    * @param a7 configuration result 7
    * @param a8 configuration result 8
    * @param a9 configuration result 9
    * @param a10 configuration result 10
    * @param a11 configuration result 11
    * @param a12 configuration result 12
    * @param a13 configuration result 13
    * @param a14 configuration result 14
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration result 1
    * @tparam A2 the type for configuration result 2
    * @tparam A3 the type for configuration result 3
    * @tparam A4 the type for configuration result 4
    * @tparam A5 the type for configuration result 5
    * @tparam A6 the type for configuration result 6
    * @tparam A7 the type for configuration result 7
    * @tparam A8 the type for configuration result 8
    * @tparam A9 the type for configuration result 9
    * @tparam A10 the type for configuration result 10
    * @tparam A11 the type for configuration result 11
    * @tparam A12 the type for configuration result 12
    * @tparam A13 the type for configuration result 13
    * @tparam A14 the type for configuration result 14
    * @tparam F the [[ConfigResult]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[F[_]: Monad, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, Z](a1: ConfigResult[F, A1], a2: ConfigResult[F, A2], a3: ConfigResult[F, A3], a4: ConfigResult[F, A4], a5: ConfigResult[F, A5], a6: ConfigResult[F, A6], a7: ConfigResult[F, A7], a8: ConfigResult[F, A8], a9: ConfigResult[F, A9], a10: ConfigResult[F, A10], a11: ConfigResult[F, A11], a12: ConfigResult[F, A12], a13: ConfigResult[F, A13], a14: ConfigResult[F, A14])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14) => ConfigResult[F, Z]): ConfigResult[F, Z] =
    ConfigResult((a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14).result.flatMap {
       case Left(errors) => left[Z](errors).pure[F]
       case Right(values) => f.tupled.apply(values).result
     })

  /**
    * Loads a configuration using the 15 specified [[ConfigResult]]s.
    * Deals with error accumulation if there are any errors in the
    * provided [[ConfigResult]]s.
    *
    * @param a1 configuration result 1
    * @param a2 configuration result 2
    * @param a3 configuration result 3
    * @param a4 configuration result 4
    * @param a5 configuration result 5
    * @param a6 configuration result 6
    * @param a7 configuration result 7
    * @param a8 configuration result 8
    * @param a9 configuration result 9
    * @param a10 configuration result 10
    * @param a11 configuration result 11
    * @param a12 configuration result 12
    * @param a13 configuration result 13
    * @param a14 configuration result 14
    * @param a15 configuration result 15
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration result 1
    * @tparam A2 the type for configuration result 2
    * @tparam A3 the type for configuration result 3
    * @tparam A4 the type for configuration result 4
    * @tparam A5 the type for configuration result 5
    * @tparam A6 the type for configuration result 6
    * @tparam A7 the type for configuration result 7
    * @tparam A8 the type for configuration result 8
    * @tparam A9 the type for configuration result 9
    * @tparam A10 the type for configuration result 10
    * @tparam A11 the type for configuration result 11
    * @tparam A12 the type for configuration result 12
    * @tparam A13 the type for configuration result 13
    * @tparam A14 the type for configuration result 14
    * @tparam A15 the type for configuration result 15
    * @tparam F the [[ConfigResult]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[F[_]: Apply, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, Z](a1: ConfigResult[F, A1], a2: ConfigResult[F, A2], a3: ConfigResult[F, A3], a4: ConfigResult[F, A4], a5: ConfigResult[F, A5], a6: ConfigResult[F, A6], a7: ConfigResult[F, A7], a8: ConfigResult[F, A8], a9: ConfigResult[F, A9], a10: ConfigResult[F, A10], a11: ConfigResult[F, A11], a12: ConfigResult[F, A12], a13: ConfigResult[F, A13], a14: ConfigResult[F, A14], a15: ConfigResult[F, A15])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15) => Z): ConfigResult[F, Z] =
    ConfigResult((a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14 append a15).result.map(_.map(f.tupled)))

  /**
    * Defines a requirement on 15 [[ConfigResult]]s in order to be
    * able to load a configuration. The method wraps `loadConfig`
    * methods, requiring the provided [[ConfigResult]]s to be
    * available in order to use the `loadConfig` methods.
    *
    * Deals with error accumulation if there are any errors in
    * the provided [[ConfigResult]]s.
    *
    * @param a1 configuration result 1
    * @param a2 configuration result 2
    * @param a3 configuration result 3
    * @param a4 configuration result 4
    * @param a5 configuration result 5
    * @param a6 configuration result 6
    * @param a7 configuration result 7
    * @param a8 configuration result 8
    * @param a9 configuration result 9
    * @param a10 configuration result 10
    * @param a11 configuration result 11
    * @param a12 configuration result 12
    * @param a13 configuration result 13
    * @param a14 configuration result 14
    * @param a15 configuration result 15
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration result 1
    * @tparam A2 the type for configuration result 2
    * @tparam A3 the type for configuration result 3
    * @tparam A4 the type for configuration result 4
    * @tparam A5 the type for configuration result 5
    * @tparam A6 the type for configuration result 6
    * @tparam A7 the type for configuration result 7
    * @tparam A8 the type for configuration result 8
    * @tparam A9 the type for configuration result 9
    * @tparam A10 the type for configuration result 10
    * @tparam A11 the type for configuration result 11
    * @tparam A12 the type for configuration result 12
    * @tparam A13 the type for configuration result 13
    * @tparam A14 the type for configuration result 14
    * @tparam A15 the type for configuration result 15
    * @tparam F the [[ConfigResult]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[F[_]: Monad, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, Z](a1: ConfigResult[F, A1], a2: ConfigResult[F, A2], a3: ConfigResult[F, A3], a4: ConfigResult[F, A4], a5: ConfigResult[F, A5], a6: ConfigResult[F, A6], a7: ConfigResult[F, A7], a8: ConfigResult[F, A8], a9: ConfigResult[F, A9], a10: ConfigResult[F, A10], a11: ConfigResult[F, A11], a12: ConfigResult[F, A12], a13: ConfigResult[F, A13], a14: ConfigResult[F, A14], a15: ConfigResult[F, A15])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15) => ConfigResult[F, Z]): ConfigResult[F, Z] =
    ConfigResult((a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14 append a15).result.flatMap {
       case Left(errors) => left[Z](errors).pure[F]
       case Right(values) => f.tupled.apply(values).result
     })

  /**
    * Loads a configuration using the 16 specified [[ConfigResult]]s.
    * Deals with error accumulation if there are any errors in the
    * provided [[ConfigResult]]s.
    *
    * @param a1 configuration result 1
    * @param a2 configuration result 2
    * @param a3 configuration result 3
    * @param a4 configuration result 4
    * @param a5 configuration result 5
    * @param a6 configuration result 6
    * @param a7 configuration result 7
    * @param a8 configuration result 8
    * @param a9 configuration result 9
    * @param a10 configuration result 10
    * @param a11 configuration result 11
    * @param a12 configuration result 12
    * @param a13 configuration result 13
    * @param a14 configuration result 14
    * @param a15 configuration result 15
    * @param a16 configuration result 16
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration result 1
    * @tparam A2 the type for configuration result 2
    * @tparam A3 the type for configuration result 3
    * @tparam A4 the type for configuration result 4
    * @tparam A5 the type for configuration result 5
    * @tparam A6 the type for configuration result 6
    * @tparam A7 the type for configuration result 7
    * @tparam A8 the type for configuration result 8
    * @tparam A9 the type for configuration result 9
    * @tparam A10 the type for configuration result 10
    * @tparam A11 the type for configuration result 11
    * @tparam A12 the type for configuration result 12
    * @tparam A13 the type for configuration result 13
    * @tparam A14 the type for configuration result 14
    * @tparam A15 the type for configuration result 15
    * @tparam A16 the type for configuration result 16
    * @tparam F the [[ConfigResult]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[F[_]: Apply, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, Z](a1: ConfigResult[F, A1], a2: ConfigResult[F, A2], a3: ConfigResult[F, A3], a4: ConfigResult[F, A4], a5: ConfigResult[F, A5], a6: ConfigResult[F, A6], a7: ConfigResult[F, A7], a8: ConfigResult[F, A8], a9: ConfigResult[F, A9], a10: ConfigResult[F, A10], a11: ConfigResult[F, A11], a12: ConfigResult[F, A12], a13: ConfigResult[F, A13], a14: ConfigResult[F, A14], a15: ConfigResult[F, A15], a16: ConfigResult[F, A16])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16) => Z): ConfigResult[F, Z] =
    ConfigResult((a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14 append a15 append a16).result.map(_.map(f.tupled)))

  /**
    * Defines a requirement on 16 [[ConfigResult]]s in order to be
    * able to load a configuration. The method wraps `loadConfig`
    * methods, requiring the provided [[ConfigResult]]s to be
    * available in order to use the `loadConfig` methods.
    *
    * Deals with error accumulation if there are any errors in
    * the provided [[ConfigResult]]s.
    *
    * @param a1 configuration result 1
    * @param a2 configuration result 2
    * @param a3 configuration result 3
    * @param a4 configuration result 4
    * @param a5 configuration result 5
    * @param a6 configuration result 6
    * @param a7 configuration result 7
    * @param a8 configuration result 8
    * @param a9 configuration result 9
    * @param a10 configuration result 10
    * @param a11 configuration result 11
    * @param a12 configuration result 12
    * @param a13 configuration result 13
    * @param a14 configuration result 14
    * @param a15 configuration result 15
    * @param a16 configuration result 16
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration result 1
    * @tparam A2 the type for configuration result 2
    * @tparam A3 the type for configuration result 3
    * @tparam A4 the type for configuration result 4
    * @tparam A5 the type for configuration result 5
    * @tparam A6 the type for configuration result 6
    * @tparam A7 the type for configuration result 7
    * @tparam A8 the type for configuration result 8
    * @tparam A9 the type for configuration result 9
    * @tparam A10 the type for configuration result 10
    * @tparam A11 the type for configuration result 11
    * @tparam A12 the type for configuration result 12
    * @tparam A13 the type for configuration result 13
    * @tparam A14 the type for configuration result 14
    * @tparam A15 the type for configuration result 15
    * @tparam A16 the type for configuration result 16
    * @tparam F the [[ConfigResult]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[F[_]: Monad, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, Z](a1: ConfigResult[F, A1], a2: ConfigResult[F, A2], a3: ConfigResult[F, A3], a4: ConfigResult[F, A4], a5: ConfigResult[F, A5], a6: ConfigResult[F, A6], a7: ConfigResult[F, A7], a8: ConfigResult[F, A8], a9: ConfigResult[F, A9], a10: ConfigResult[F, A10], a11: ConfigResult[F, A11], a12: ConfigResult[F, A12], a13: ConfigResult[F, A13], a14: ConfigResult[F, A14], a15: ConfigResult[F, A15], a16: ConfigResult[F, A16])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16) => ConfigResult[F, Z]): ConfigResult[F, Z] =
    ConfigResult((a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14 append a15 append a16).result.flatMap {
       case Left(errors) => left[Z](errors).pure[F]
       case Right(values) => f.tupled.apply(values).result
     })

  /**
    * Loads a configuration using the 17 specified [[ConfigResult]]s.
    * Deals with error accumulation if there are any errors in the
    * provided [[ConfigResult]]s.
    *
    * @param a1 configuration result 1
    * @param a2 configuration result 2
    * @param a3 configuration result 3
    * @param a4 configuration result 4
    * @param a5 configuration result 5
    * @param a6 configuration result 6
    * @param a7 configuration result 7
    * @param a8 configuration result 8
    * @param a9 configuration result 9
    * @param a10 configuration result 10
    * @param a11 configuration result 11
    * @param a12 configuration result 12
    * @param a13 configuration result 13
    * @param a14 configuration result 14
    * @param a15 configuration result 15
    * @param a16 configuration result 16
    * @param a17 configuration result 17
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration result 1
    * @tparam A2 the type for configuration result 2
    * @tparam A3 the type for configuration result 3
    * @tparam A4 the type for configuration result 4
    * @tparam A5 the type for configuration result 5
    * @tparam A6 the type for configuration result 6
    * @tparam A7 the type for configuration result 7
    * @tparam A8 the type for configuration result 8
    * @tparam A9 the type for configuration result 9
    * @tparam A10 the type for configuration result 10
    * @tparam A11 the type for configuration result 11
    * @tparam A12 the type for configuration result 12
    * @tparam A13 the type for configuration result 13
    * @tparam A14 the type for configuration result 14
    * @tparam A15 the type for configuration result 15
    * @tparam A16 the type for configuration result 16
    * @tparam A17 the type for configuration result 17
    * @tparam F the [[ConfigResult]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[F[_]: Apply, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, Z](a1: ConfigResult[F, A1], a2: ConfigResult[F, A2], a3: ConfigResult[F, A3], a4: ConfigResult[F, A4], a5: ConfigResult[F, A5], a6: ConfigResult[F, A6], a7: ConfigResult[F, A7], a8: ConfigResult[F, A8], a9: ConfigResult[F, A9], a10: ConfigResult[F, A10], a11: ConfigResult[F, A11], a12: ConfigResult[F, A12], a13: ConfigResult[F, A13], a14: ConfigResult[F, A14], a15: ConfigResult[F, A15], a16: ConfigResult[F, A16], a17: ConfigResult[F, A17])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17) => Z): ConfigResult[F, Z] =
    ConfigResult((a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14 append a15 append a16 append a17).result.map(_.map(f.tupled)))

  /**
    * Defines a requirement on 17 [[ConfigResult]]s in order to be
    * able to load a configuration. The method wraps `loadConfig`
    * methods, requiring the provided [[ConfigResult]]s to be
    * available in order to use the `loadConfig` methods.
    *
    * Deals with error accumulation if there are any errors in
    * the provided [[ConfigResult]]s.
    *
    * @param a1 configuration result 1
    * @param a2 configuration result 2
    * @param a3 configuration result 3
    * @param a4 configuration result 4
    * @param a5 configuration result 5
    * @param a6 configuration result 6
    * @param a7 configuration result 7
    * @param a8 configuration result 8
    * @param a9 configuration result 9
    * @param a10 configuration result 10
    * @param a11 configuration result 11
    * @param a12 configuration result 12
    * @param a13 configuration result 13
    * @param a14 configuration result 14
    * @param a15 configuration result 15
    * @param a16 configuration result 16
    * @param a17 configuration result 17
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration result 1
    * @tparam A2 the type for configuration result 2
    * @tparam A3 the type for configuration result 3
    * @tparam A4 the type for configuration result 4
    * @tparam A5 the type for configuration result 5
    * @tparam A6 the type for configuration result 6
    * @tparam A7 the type for configuration result 7
    * @tparam A8 the type for configuration result 8
    * @tparam A9 the type for configuration result 9
    * @tparam A10 the type for configuration result 10
    * @tparam A11 the type for configuration result 11
    * @tparam A12 the type for configuration result 12
    * @tparam A13 the type for configuration result 13
    * @tparam A14 the type for configuration result 14
    * @tparam A15 the type for configuration result 15
    * @tparam A16 the type for configuration result 16
    * @tparam A17 the type for configuration result 17
    * @tparam F the [[ConfigResult]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[F[_]: Monad, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, Z](a1: ConfigResult[F, A1], a2: ConfigResult[F, A2], a3: ConfigResult[F, A3], a4: ConfigResult[F, A4], a5: ConfigResult[F, A5], a6: ConfigResult[F, A6], a7: ConfigResult[F, A7], a8: ConfigResult[F, A8], a9: ConfigResult[F, A9], a10: ConfigResult[F, A10], a11: ConfigResult[F, A11], a12: ConfigResult[F, A12], a13: ConfigResult[F, A13], a14: ConfigResult[F, A14], a15: ConfigResult[F, A15], a16: ConfigResult[F, A16], a17: ConfigResult[F, A17])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17) => ConfigResult[F, Z]): ConfigResult[F, Z] =
    ConfigResult((a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14 append a15 append a16 append a17).result.flatMap {
       case Left(errors) => left[Z](errors).pure[F]
       case Right(values) => f.tupled.apply(values).result
     })

  /**
    * Loads a configuration using the 18 specified [[ConfigResult]]s.
    * Deals with error accumulation if there are any errors in the
    * provided [[ConfigResult]]s.
    *
    * @param a1 configuration result 1
    * @param a2 configuration result 2
    * @param a3 configuration result 3
    * @param a4 configuration result 4
    * @param a5 configuration result 5
    * @param a6 configuration result 6
    * @param a7 configuration result 7
    * @param a8 configuration result 8
    * @param a9 configuration result 9
    * @param a10 configuration result 10
    * @param a11 configuration result 11
    * @param a12 configuration result 12
    * @param a13 configuration result 13
    * @param a14 configuration result 14
    * @param a15 configuration result 15
    * @param a16 configuration result 16
    * @param a17 configuration result 17
    * @param a18 configuration result 18
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration result 1
    * @tparam A2 the type for configuration result 2
    * @tparam A3 the type for configuration result 3
    * @tparam A4 the type for configuration result 4
    * @tparam A5 the type for configuration result 5
    * @tparam A6 the type for configuration result 6
    * @tparam A7 the type for configuration result 7
    * @tparam A8 the type for configuration result 8
    * @tparam A9 the type for configuration result 9
    * @tparam A10 the type for configuration result 10
    * @tparam A11 the type for configuration result 11
    * @tparam A12 the type for configuration result 12
    * @tparam A13 the type for configuration result 13
    * @tparam A14 the type for configuration result 14
    * @tparam A15 the type for configuration result 15
    * @tparam A16 the type for configuration result 16
    * @tparam A17 the type for configuration result 17
    * @tparam A18 the type for configuration result 18
    * @tparam F the [[ConfigResult]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[F[_]: Apply, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, Z](a1: ConfigResult[F, A1], a2: ConfigResult[F, A2], a3: ConfigResult[F, A3], a4: ConfigResult[F, A4], a5: ConfigResult[F, A5], a6: ConfigResult[F, A6], a7: ConfigResult[F, A7], a8: ConfigResult[F, A8], a9: ConfigResult[F, A9], a10: ConfigResult[F, A10], a11: ConfigResult[F, A11], a12: ConfigResult[F, A12], a13: ConfigResult[F, A13], a14: ConfigResult[F, A14], a15: ConfigResult[F, A15], a16: ConfigResult[F, A16], a17: ConfigResult[F, A17], a18: ConfigResult[F, A18])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18) => Z): ConfigResult[F, Z] =
    ConfigResult((a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14 append a15 append a16 append a17 append a18).result.map(_.map(f.tupled)))

  /**
    * Defines a requirement on 18 [[ConfigResult]]s in order to be
    * able to load a configuration. The method wraps `loadConfig`
    * methods, requiring the provided [[ConfigResult]]s to be
    * available in order to use the `loadConfig` methods.
    *
    * Deals with error accumulation if there are any errors in
    * the provided [[ConfigResult]]s.
    *
    * @param a1 configuration result 1
    * @param a2 configuration result 2
    * @param a3 configuration result 3
    * @param a4 configuration result 4
    * @param a5 configuration result 5
    * @param a6 configuration result 6
    * @param a7 configuration result 7
    * @param a8 configuration result 8
    * @param a9 configuration result 9
    * @param a10 configuration result 10
    * @param a11 configuration result 11
    * @param a12 configuration result 12
    * @param a13 configuration result 13
    * @param a14 configuration result 14
    * @param a15 configuration result 15
    * @param a16 configuration result 16
    * @param a17 configuration result 17
    * @param a18 configuration result 18
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration result 1
    * @tparam A2 the type for configuration result 2
    * @tparam A3 the type for configuration result 3
    * @tparam A4 the type for configuration result 4
    * @tparam A5 the type for configuration result 5
    * @tparam A6 the type for configuration result 6
    * @tparam A7 the type for configuration result 7
    * @tparam A8 the type for configuration result 8
    * @tparam A9 the type for configuration result 9
    * @tparam A10 the type for configuration result 10
    * @tparam A11 the type for configuration result 11
    * @tparam A12 the type for configuration result 12
    * @tparam A13 the type for configuration result 13
    * @tparam A14 the type for configuration result 14
    * @tparam A15 the type for configuration result 15
    * @tparam A16 the type for configuration result 16
    * @tparam A17 the type for configuration result 17
    * @tparam A18 the type for configuration result 18
    * @tparam F the [[ConfigResult]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[F[_]: Monad, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, Z](a1: ConfigResult[F, A1], a2: ConfigResult[F, A2], a3: ConfigResult[F, A3], a4: ConfigResult[F, A4], a5: ConfigResult[F, A5], a6: ConfigResult[F, A6], a7: ConfigResult[F, A7], a8: ConfigResult[F, A8], a9: ConfigResult[F, A9], a10: ConfigResult[F, A10], a11: ConfigResult[F, A11], a12: ConfigResult[F, A12], a13: ConfigResult[F, A13], a14: ConfigResult[F, A14], a15: ConfigResult[F, A15], a16: ConfigResult[F, A16], a17: ConfigResult[F, A17], a18: ConfigResult[F, A18])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18) => ConfigResult[F, Z]): ConfigResult[F, Z] =
    ConfigResult((a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14 append a15 append a16 append a17 append a18).result.flatMap {
       case Left(errors) => left[Z](errors).pure[F]
       case Right(values) => f.tupled.apply(values).result
     })

  /**
    * Loads a configuration using the 19 specified [[ConfigResult]]s.
    * Deals with error accumulation if there are any errors in the
    * provided [[ConfigResult]]s.
    *
    * @param a1 configuration result 1
    * @param a2 configuration result 2
    * @param a3 configuration result 3
    * @param a4 configuration result 4
    * @param a5 configuration result 5
    * @param a6 configuration result 6
    * @param a7 configuration result 7
    * @param a8 configuration result 8
    * @param a9 configuration result 9
    * @param a10 configuration result 10
    * @param a11 configuration result 11
    * @param a12 configuration result 12
    * @param a13 configuration result 13
    * @param a14 configuration result 14
    * @param a15 configuration result 15
    * @param a16 configuration result 16
    * @param a17 configuration result 17
    * @param a18 configuration result 18
    * @param a19 configuration result 19
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration result 1
    * @tparam A2 the type for configuration result 2
    * @tparam A3 the type for configuration result 3
    * @tparam A4 the type for configuration result 4
    * @tparam A5 the type for configuration result 5
    * @tparam A6 the type for configuration result 6
    * @tparam A7 the type for configuration result 7
    * @tparam A8 the type for configuration result 8
    * @tparam A9 the type for configuration result 9
    * @tparam A10 the type for configuration result 10
    * @tparam A11 the type for configuration result 11
    * @tparam A12 the type for configuration result 12
    * @tparam A13 the type for configuration result 13
    * @tparam A14 the type for configuration result 14
    * @tparam A15 the type for configuration result 15
    * @tparam A16 the type for configuration result 16
    * @tparam A17 the type for configuration result 17
    * @tparam A18 the type for configuration result 18
    * @tparam A19 the type for configuration result 19
    * @tparam F the [[ConfigResult]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[F[_]: Apply, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, Z](a1: ConfigResult[F, A1], a2: ConfigResult[F, A2], a3: ConfigResult[F, A3], a4: ConfigResult[F, A4], a5: ConfigResult[F, A5], a6: ConfigResult[F, A6], a7: ConfigResult[F, A7], a8: ConfigResult[F, A8], a9: ConfigResult[F, A9], a10: ConfigResult[F, A10], a11: ConfigResult[F, A11], a12: ConfigResult[F, A12], a13: ConfigResult[F, A13], a14: ConfigResult[F, A14], a15: ConfigResult[F, A15], a16: ConfigResult[F, A16], a17: ConfigResult[F, A17], a18: ConfigResult[F, A18], a19: ConfigResult[F, A19])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19) => Z): ConfigResult[F, Z] =
    ConfigResult((a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14 append a15 append a16 append a17 append a18 append a19).result.map(_.map(f.tupled)))

  /**
    * Defines a requirement on 19 [[ConfigResult]]s in order to be
    * able to load a configuration. The method wraps `loadConfig`
    * methods, requiring the provided [[ConfigResult]]s to be
    * available in order to use the `loadConfig` methods.
    *
    * Deals with error accumulation if there are any errors in
    * the provided [[ConfigResult]]s.
    *
    * @param a1 configuration result 1
    * @param a2 configuration result 2
    * @param a3 configuration result 3
    * @param a4 configuration result 4
    * @param a5 configuration result 5
    * @param a6 configuration result 6
    * @param a7 configuration result 7
    * @param a8 configuration result 8
    * @param a9 configuration result 9
    * @param a10 configuration result 10
    * @param a11 configuration result 11
    * @param a12 configuration result 12
    * @param a13 configuration result 13
    * @param a14 configuration result 14
    * @param a15 configuration result 15
    * @param a16 configuration result 16
    * @param a17 configuration result 17
    * @param a18 configuration result 18
    * @param a19 configuration result 19
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration result 1
    * @tparam A2 the type for configuration result 2
    * @tparam A3 the type for configuration result 3
    * @tparam A4 the type for configuration result 4
    * @tparam A5 the type for configuration result 5
    * @tparam A6 the type for configuration result 6
    * @tparam A7 the type for configuration result 7
    * @tparam A8 the type for configuration result 8
    * @tparam A9 the type for configuration result 9
    * @tparam A10 the type for configuration result 10
    * @tparam A11 the type for configuration result 11
    * @tparam A12 the type for configuration result 12
    * @tparam A13 the type for configuration result 13
    * @tparam A14 the type for configuration result 14
    * @tparam A15 the type for configuration result 15
    * @tparam A16 the type for configuration result 16
    * @tparam A17 the type for configuration result 17
    * @tparam A18 the type for configuration result 18
    * @tparam A19 the type for configuration result 19
    * @tparam F the [[ConfigResult]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[F[_]: Monad, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, Z](a1: ConfigResult[F, A1], a2: ConfigResult[F, A2], a3: ConfigResult[F, A3], a4: ConfigResult[F, A4], a5: ConfigResult[F, A5], a6: ConfigResult[F, A6], a7: ConfigResult[F, A7], a8: ConfigResult[F, A8], a9: ConfigResult[F, A9], a10: ConfigResult[F, A10], a11: ConfigResult[F, A11], a12: ConfigResult[F, A12], a13: ConfigResult[F, A13], a14: ConfigResult[F, A14], a15: ConfigResult[F, A15], a16: ConfigResult[F, A16], a17: ConfigResult[F, A17], a18: ConfigResult[F, A18], a19: ConfigResult[F, A19])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19) => ConfigResult[F, Z]): ConfigResult[F, Z] =
    ConfigResult((a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14 append a15 append a16 append a17 append a18 append a19).result.flatMap {
       case Left(errors) => left[Z](errors).pure[F]
       case Right(values) => f.tupled.apply(values).result
     })

  /**
    * Loads a configuration using the 20 specified [[ConfigResult]]s.
    * Deals with error accumulation if there are any errors in the
    * provided [[ConfigResult]]s.
    *
    * @param a1 configuration result 1
    * @param a2 configuration result 2
    * @param a3 configuration result 3
    * @param a4 configuration result 4
    * @param a5 configuration result 5
    * @param a6 configuration result 6
    * @param a7 configuration result 7
    * @param a8 configuration result 8
    * @param a9 configuration result 9
    * @param a10 configuration result 10
    * @param a11 configuration result 11
    * @param a12 configuration result 12
    * @param a13 configuration result 13
    * @param a14 configuration result 14
    * @param a15 configuration result 15
    * @param a16 configuration result 16
    * @param a17 configuration result 17
    * @param a18 configuration result 18
    * @param a19 configuration result 19
    * @param a20 configuration result 20
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration result 1
    * @tparam A2 the type for configuration result 2
    * @tparam A3 the type for configuration result 3
    * @tparam A4 the type for configuration result 4
    * @tparam A5 the type for configuration result 5
    * @tparam A6 the type for configuration result 6
    * @tparam A7 the type for configuration result 7
    * @tparam A8 the type for configuration result 8
    * @tparam A9 the type for configuration result 9
    * @tparam A10 the type for configuration result 10
    * @tparam A11 the type for configuration result 11
    * @tparam A12 the type for configuration result 12
    * @tparam A13 the type for configuration result 13
    * @tparam A14 the type for configuration result 14
    * @tparam A15 the type for configuration result 15
    * @tparam A16 the type for configuration result 16
    * @tparam A17 the type for configuration result 17
    * @tparam A18 the type for configuration result 18
    * @tparam A19 the type for configuration result 19
    * @tparam A20 the type for configuration result 20
    * @tparam F the [[ConfigResult]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[F[_]: Apply, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, Z](a1: ConfigResult[F, A1], a2: ConfigResult[F, A2], a3: ConfigResult[F, A3], a4: ConfigResult[F, A4], a5: ConfigResult[F, A5], a6: ConfigResult[F, A6], a7: ConfigResult[F, A7], a8: ConfigResult[F, A8], a9: ConfigResult[F, A9], a10: ConfigResult[F, A10], a11: ConfigResult[F, A11], a12: ConfigResult[F, A12], a13: ConfigResult[F, A13], a14: ConfigResult[F, A14], a15: ConfigResult[F, A15], a16: ConfigResult[F, A16], a17: ConfigResult[F, A17], a18: ConfigResult[F, A18], a19: ConfigResult[F, A19], a20: ConfigResult[F, A20])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20) => Z): ConfigResult[F, Z] =
    ConfigResult((a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14 append a15 append a16 append a17 append a18 append a19 append a20).result.map(_.map(f.tupled)))

  /**
    * Defines a requirement on 20 [[ConfigResult]]s in order to be
    * able to load a configuration. The method wraps `loadConfig`
    * methods, requiring the provided [[ConfigResult]]s to be
    * available in order to use the `loadConfig` methods.
    *
    * Deals with error accumulation if there are any errors in
    * the provided [[ConfigResult]]s.
    *
    * @param a1 configuration result 1
    * @param a2 configuration result 2
    * @param a3 configuration result 3
    * @param a4 configuration result 4
    * @param a5 configuration result 5
    * @param a6 configuration result 6
    * @param a7 configuration result 7
    * @param a8 configuration result 8
    * @param a9 configuration result 9
    * @param a10 configuration result 10
    * @param a11 configuration result 11
    * @param a12 configuration result 12
    * @param a13 configuration result 13
    * @param a14 configuration result 14
    * @param a15 configuration result 15
    * @param a16 configuration result 16
    * @param a17 configuration result 17
    * @param a18 configuration result 18
    * @param a19 configuration result 19
    * @param a20 configuration result 20
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration result 1
    * @tparam A2 the type for configuration result 2
    * @tparam A3 the type for configuration result 3
    * @tparam A4 the type for configuration result 4
    * @tparam A5 the type for configuration result 5
    * @tparam A6 the type for configuration result 6
    * @tparam A7 the type for configuration result 7
    * @tparam A8 the type for configuration result 8
    * @tparam A9 the type for configuration result 9
    * @tparam A10 the type for configuration result 10
    * @tparam A11 the type for configuration result 11
    * @tparam A12 the type for configuration result 12
    * @tparam A13 the type for configuration result 13
    * @tparam A14 the type for configuration result 14
    * @tparam A15 the type for configuration result 15
    * @tparam A16 the type for configuration result 16
    * @tparam A17 the type for configuration result 17
    * @tparam A18 the type for configuration result 18
    * @tparam A19 the type for configuration result 19
    * @tparam A20 the type for configuration result 20
    * @tparam F the [[ConfigResult]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[F[_]: Monad, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, Z](a1: ConfigResult[F, A1], a2: ConfigResult[F, A2], a3: ConfigResult[F, A3], a4: ConfigResult[F, A4], a5: ConfigResult[F, A5], a6: ConfigResult[F, A6], a7: ConfigResult[F, A7], a8: ConfigResult[F, A8], a9: ConfigResult[F, A9], a10: ConfigResult[F, A10], a11: ConfigResult[F, A11], a12: ConfigResult[F, A12], a13: ConfigResult[F, A13], a14: ConfigResult[F, A14], a15: ConfigResult[F, A15], a16: ConfigResult[F, A16], a17: ConfigResult[F, A17], a18: ConfigResult[F, A18], a19: ConfigResult[F, A19], a20: ConfigResult[F, A20])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20) => ConfigResult[F, Z]): ConfigResult[F, Z] =
    ConfigResult((a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14 append a15 append a16 append a17 append a18 append a19 append a20).result.flatMap {
       case Left(errors) => left[Z](errors).pure[F]
       case Right(values) => f.tupled.apply(values).result
     })

  /**
    * Loads a configuration using the 21 specified [[ConfigResult]]s.
    * Deals with error accumulation if there are any errors in the
    * provided [[ConfigResult]]s.
    *
    * @param a1 configuration result 1
    * @param a2 configuration result 2
    * @param a3 configuration result 3
    * @param a4 configuration result 4
    * @param a5 configuration result 5
    * @param a6 configuration result 6
    * @param a7 configuration result 7
    * @param a8 configuration result 8
    * @param a9 configuration result 9
    * @param a10 configuration result 10
    * @param a11 configuration result 11
    * @param a12 configuration result 12
    * @param a13 configuration result 13
    * @param a14 configuration result 14
    * @param a15 configuration result 15
    * @param a16 configuration result 16
    * @param a17 configuration result 17
    * @param a18 configuration result 18
    * @param a19 configuration result 19
    * @param a20 configuration result 20
    * @param a21 configuration result 21
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration result 1
    * @tparam A2 the type for configuration result 2
    * @tparam A3 the type for configuration result 3
    * @tparam A4 the type for configuration result 4
    * @tparam A5 the type for configuration result 5
    * @tparam A6 the type for configuration result 6
    * @tparam A7 the type for configuration result 7
    * @tparam A8 the type for configuration result 8
    * @tparam A9 the type for configuration result 9
    * @tparam A10 the type for configuration result 10
    * @tparam A11 the type for configuration result 11
    * @tparam A12 the type for configuration result 12
    * @tparam A13 the type for configuration result 13
    * @tparam A14 the type for configuration result 14
    * @tparam A15 the type for configuration result 15
    * @tparam A16 the type for configuration result 16
    * @tparam A17 the type for configuration result 17
    * @tparam A18 the type for configuration result 18
    * @tparam A19 the type for configuration result 19
    * @tparam A20 the type for configuration result 20
    * @tparam A21 the type for configuration result 21
    * @tparam F the [[ConfigResult]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[F[_]: Apply, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, Z](a1: ConfigResult[F, A1], a2: ConfigResult[F, A2], a3: ConfigResult[F, A3], a4: ConfigResult[F, A4], a5: ConfigResult[F, A5], a6: ConfigResult[F, A6], a7: ConfigResult[F, A7], a8: ConfigResult[F, A8], a9: ConfigResult[F, A9], a10: ConfigResult[F, A10], a11: ConfigResult[F, A11], a12: ConfigResult[F, A12], a13: ConfigResult[F, A13], a14: ConfigResult[F, A14], a15: ConfigResult[F, A15], a16: ConfigResult[F, A16], a17: ConfigResult[F, A17], a18: ConfigResult[F, A18], a19: ConfigResult[F, A19], a20: ConfigResult[F, A20], a21: ConfigResult[F, A21])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21) => Z): ConfigResult[F, Z] =
    ConfigResult((a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14 append a15 append a16 append a17 append a18 append a19 append a20 append a21).result.map(_.map(f.tupled)))

  /**
    * Defines a requirement on 21 [[ConfigResult]]s in order to be
    * able to load a configuration. The method wraps `loadConfig`
    * methods, requiring the provided [[ConfigResult]]s to be
    * available in order to use the `loadConfig` methods.
    *
    * Deals with error accumulation if there are any errors in
    * the provided [[ConfigResult]]s.
    *
    * @param a1 configuration result 1
    * @param a2 configuration result 2
    * @param a3 configuration result 3
    * @param a4 configuration result 4
    * @param a5 configuration result 5
    * @param a6 configuration result 6
    * @param a7 configuration result 7
    * @param a8 configuration result 8
    * @param a9 configuration result 9
    * @param a10 configuration result 10
    * @param a11 configuration result 11
    * @param a12 configuration result 12
    * @param a13 configuration result 13
    * @param a14 configuration result 14
    * @param a15 configuration result 15
    * @param a16 configuration result 16
    * @param a17 configuration result 17
    * @param a18 configuration result 18
    * @param a19 configuration result 19
    * @param a20 configuration result 20
    * @param a21 configuration result 21
    * @param f the function to create the configuration
    * @tparam A1 the type for configuration result 1
    * @tparam A2 the type for configuration result 2
    * @tparam A3 the type for configuration result 3
    * @tparam A4 the type for configuration result 4
    * @tparam A5 the type for configuration result 5
    * @tparam A6 the type for configuration result 6
    * @tparam A7 the type for configuration result 7
    * @tparam A8 the type for configuration result 8
    * @tparam A9 the type for configuration result 9
    * @tparam A10 the type for configuration result 10
    * @tparam A11 the type for configuration result 11
    * @tparam A12 the type for configuration result 12
    * @tparam A13 the type for configuration result 13
    * @tparam A14 the type for configuration result 14
    * @tparam A15 the type for configuration result 15
    * @tparam A16 the type for configuration result 16
    * @tparam A17 the type for configuration result 17
    * @tparam A18 the type for configuration result 18
    * @tparam A19 the type for configuration result 19
    * @tparam A20 the type for configuration result 20
    * @tparam A21 the type for configuration result 21
    * @tparam F the [[ConfigResult]] context
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[F[_]: Monad, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, Z](a1: ConfigResult[F, A1], a2: ConfigResult[F, A2], a3: ConfigResult[F, A3], a4: ConfigResult[F, A4], a5: ConfigResult[F, A5], a6: ConfigResult[F, A6], a7: ConfigResult[F, A7], a8: ConfigResult[F, A8], a9: ConfigResult[F, A9], a10: ConfigResult[F, A10], a11: ConfigResult[F, A11], a12: ConfigResult[F, A12], a13: ConfigResult[F, A13], a14: ConfigResult[F, A14], a15: ConfigResult[F, A15], a16: ConfigResult[F, A16], a17: ConfigResult[F, A17], a18: ConfigResult[F, A18], a19: ConfigResult[F, A19], a20: ConfigResult[F, A20], a21: ConfigResult[F, A21])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21) => ConfigResult[F, Z]): ConfigResult[F, Z] =
    ConfigResult((a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14 append a15 append a16 append a17 append a18 append a19 append a20 append a21).result.flatMap {
       case Left(errors) => left[Z](errors).pure[F]
       case Right(values) => f.tupled.apply(values).result
     })
}
