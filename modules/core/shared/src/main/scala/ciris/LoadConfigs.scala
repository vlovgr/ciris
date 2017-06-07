// format: off

/**
  * Generated using sbt source generators.
  * You should not modify this file directly.
  */

package ciris

private [ciris] trait LoadConfigs {

  /**
    * Wraps the specified value in an `Either[ConfigErrors, Z]`. Useful
    * when you want to use a static configuration inside a `withValues`
    * block, requiring you to wrap it with this method.
    *
    * @param z the value to wrap
    * @tparam Z the type of the value to wrap
    * @return the value wrapped in an `Either[ConfigErrors, Z]`
    */
  def loadConfig[Z](z: Z): Either[ConfigErrors, Z] =
    Right(z)

  /**
    * Loads a configuration using the specified [[ConfigValue]].
    *
    * @param a1 the configuration value
    * @param f the function to create the configuration
    * @tparam A1 the type of the configuration value
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[A1, Z](a1: ConfigValue[A1])(f: A1 => Z): Either[ConfigErrors, Z] =
    a1.value.fold(error => Left(ConfigErrors(error)), a1 => Right(f(a1)))

  /**
    * Defines a requirement on a single [[ConfigValue]] in order to be
    * able to load a configuration. The method wraps `loadConfig`
    * methods, requiring the provided [[ConfigValue]] to be
    * available in order to use the `loadConfig` methods.
    *
    * @param a1 the configuration value
    * @param f the function to create the configuration
    * @tparam A1 the type of the configuration value
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValue[A1, Z](a1: ConfigValue[A1])(f: A1 => Either[ConfigErrors, Z]): Either[ConfigErrors, Z] =
   withValues(a1)(f)

  /**
    * Defines a requirement on a single [[ConfigValue]] in order to be
    * able to load a configuration. The method wraps any `loadConfig`
    * methods, requiring the provided [[ConfigValue]] to be
    * available in order to use the `loadConfig` methods.
    *
    * @param a1 the configuration value
    * @param f the function to create the configuration
    * @tparam A1 the type of the configuration value
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[A1, Z](a1: ConfigValue[A1])(f: A1 => Either[ConfigErrors, Z]): Either[ConfigErrors, Z] =
    a1.value.fold(error => Left(ConfigErrors(error)), f)

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
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[A1, A2, Z](a1: ConfigValue[A1], a2: ConfigValue[A2])(f: (A1, A2) => Z): Either[ConfigErrors, Z] =
    (a1 append a2).value.right.map(f.tupled)

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
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[A1, A2, Z](a1: ConfigValue[A1], a2: ConfigValue[A2])(f: (A1, A2) => Either[ConfigErrors, Z]): Either[ConfigErrors, Z] =
    (a1 append a2).value.right.flatMap(f.tupled)

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
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[A1, A2, A3, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3])(f: (A1, A2, A3) => Z): Either[ConfigErrors, Z] =
    (a1 append a2 append a3).value.right.map(f.tupled)

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
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[A1, A2, A3, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3])(f: (A1, A2, A3) => Either[ConfigErrors, Z]): Either[ConfigErrors, Z] =
    (a1 append a2 append a3).value.right.flatMap(f.tupled)

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
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[A1, A2, A3, A4, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4])(f: (A1, A2, A3, A4) => Z): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4).value.right.map(f.tupled)

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
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[A1, A2, A3, A4, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4])(f: (A1, A2, A3, A4) => Either[ConfigErrors, Z]): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4).value.right.flatMap(f.tupled)

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
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[A1, A2, A3, A4, A5, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4], a5: ConfigValue[A5])(f: (A1, A2, A3, A4, A5) => Z): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4 append a5).value.right.map(f.tupled)

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
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[A1, A2, A3, A4, A5, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4], a5: ConfigValue[A5])(f: (A1, A2, A3, A4, A5) => Either[ConfigErrors, Z]): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4 append a5).value.right.flatMap(f.tupled)

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
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[A1, A2, A3, A4, A5, A6, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4], a5: ConfigValue[A5], a6: ConfigValue[A6])(f: (A1, A2, A3, A4, A5, A6) => Z): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4 append a5 append a6).value.right.map(f.tupled)

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
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[A1, A2, A3, A4, A5, A6, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4], a5: ConfigValue[A5], a6: ConfigValue[A6])(f: (A1, A2, A3, A4, A5, A6) => Either[ConfigErrors, Z]): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4 append a5 append a6).value.right.flatMap(f.tupled)

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
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[A1, A2, A3, A4, A5, A6, A7, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4], a5: ConfigValue[A5], a6: ConfigValue[A6], a7: ConfigValue[A7])(f: (A1, A2, A3, A4, A5, A6, A7) => Z): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7).value.right.map(f.tupled)

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
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[A1, A2, A3, A4, A5, A6, A7, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4], a5: ConfigValue[A5], a6: ConfigValue[A6], a7: ConfigValue[A7])(f: (A1, A2, A3, A4, A5, A6, A7) => Either[ConfigErrors, Z]): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7).value.right.flatMap(f.tupled)

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
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[A1, A2, A3, A4, A5, A6, A7, A8, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4], a5: ConfigValue[A5], a6: ConfigValue[A6], a7: ConfigValue[A7], a8: ConfigValue[A8])(f: (A1, A2, A3, A4, A5, A6, A7, A8) => Z): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8).value.right.map(f.tupled)

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
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[A1, A2, A3, A4, A5, A6, A7, A8, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4], a5: ConfigValue[A5], a6: ConfigValue[A6], a7: ConfigValue[A7], a8: ConfigValue[A8])(f: (A1, A2, A3, A4, A5, A6, A7, A8) => Either[ConfigErrors, Z]): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8).value.right.flatMap(f.tupled)

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
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[A1, A2, A3, A4, A5, A6, A7, A8, A9, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4], a5: ConfigValue[A5], a6: ConfigValue[A6], a7: ConfigValue[A7], a8: ConfigValue[A8], a9: ConfigValue[A9])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9) => Z): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9).value.right.map(f.tupled)

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
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[A1, A2, A3, A4, A5, A6, A7, A8, A9, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4], a5: ConfigValue[A5], a6: ConfigValue[A6], a7: ConfigValue[A7], a8: ConfigValue[A8], a9: ConfigValue[A9])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9) => Either[ConfigErrors, Z]): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9).value.right.flatMap(f.tupled)

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
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4], a5: ConfigValue[A5], a6: ConfigValue[A6], a7: ConfigValue[A7], a8: ConfigValue[A8], a9: ConfigValue[A9], a10: ConfigValue[A10])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10) => Z): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10).value.right.map(f.tupled)

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
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4], a5: ConfigValue[A5], a6: ConfigValue[A6], a7: ConfigValue[A7], a8: ConfigValue[A8], a9: ConfigValue[A9], a10: ConfigValue[A10])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10) => Either[ConfigErrors, Z]): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10).value.right.flatMap(f.tupled)

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
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4], a5: ConfigValue[A5], a6: ConfigValue[A6], a7: ConfigValue[A7], a8: ConfigValue[A8], a9: ConfigValue[A9], a10: ConfigValue[A10], a11: ConfigValue[A11])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11) => Z): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11).value.right.map(f.tupled)

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
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4], a5: ConfigValue[A5], a6: ConfigValue[A6], a7: ConfigValue[A7], a8: ConfigValue[A8], a9: ConfigValue[A9], a10: ConfigValue[A10], a11: ConfigValue[A11])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11) => Either[ConfigErrors, Z]): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11).value.right.flatMap(f.tupled)

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
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4], a5: ConfigValue[A5], a6: ConfigValue[A6], a7: ConfigValue[A7], a8: ConfigValue[A8], a9: ConfigValue[A9], a10: ConfigValue[A10], a11: ConfigValue[A11], a12: ConfigValue[A12])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12) => Z): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12).value.right.map(f.tupled)

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
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4], a5: ConfigValue[A5], a6: ConfigValue[A6], a7: ConfigValue[A7], a8: ConfigValue[A8], a9: ConfigValue[A9], a10: ConfigValue[A10], a11: ConfigValue[A11], a12: ConfigValue[A12])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12) => Either[ConfigErrors, Z]): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12).value.right.flatMap(f.tupled)

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
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4], a5: ConfigValue[A5], a6: ConfigValue[A6], a7: ConfigValue[A7], a8: ConfigValue[A8], a9: ConfigValue[A9], a10: ConfigValue[A10], a11: ConfigValue[A11], a12: ConfigValue[A12], a13: ConfigValue[A13])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13) => Z): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13).value.right.map(f.tupled)

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
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4], a5: ConfigValue[A5], a6: ConfigValue[A6], a7: ConfigValue[A7], a8: ConfigValue[A8], a9: ConfigValue[A9], a10: ConfigValue[A10], a11: ConfigValue[A11], a12: ConfigValue[A12], a13: ConfigValue[A13])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13) => Either[ConfigErrors, Z]): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13).value.right.flatMap(f.tupled)

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
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4], a5: ConfigValue[A5], a6: ConfigValue[A6], a7: ConfigValue[A7], a8: ConfigValue[A8], a9: ConfigValue[A9], a10: ConfigValue[A10], a11: ConfigValue[A11], a12: ConfigValue[A12], a13: ConfigValue[A13], a14: ConfigValue[A14])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14) => Z): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14).value.right.map(f.tupled)

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
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4], a5: ConfigValue[A5], a6: ConfigValue[A6], a7: ConfigValue[A7], a8: ConfigValue[A8], a9: ConfigValue[A9], a10: ConfigValue[A10], a11: ConfigValue[A11], a12: ConfigValue[A12], a13: ConfigValue[A13], a14: ConfigValue[A14])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14) => Either[ConfigErrors, Z]): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14).value.right.flatMap(f.tupled)

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
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4], a5: ConfigValue[A5], a6: ConfigValue[A6], a7: ConfigValue[A7], a8: ConfigValue[A8], a9: ConfigValue[A9], a10: ConfigValue[A10], a11: ConfigValue[A11], a12: ConfigValue[A12], a13: ConfigValue[A13], a14: ConfigValue[A14], a15: ConfigValue[A15])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15) => Z): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14 append a15).value.right.map(f.tupled)

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
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4], a5: ConfigValue[A5], a6: ConfigValue[A6], a7: ConfigValue[A7], a8: ConfigValue[A8], a9: ConfigValue[A9], a10: ConfigValue[A10], a11: ConfigValue[A11], a12: ConfigValue[A12], a13: ConfigValue[A13], a14: ConfigValue[A14], a15: ConfigValue[A15])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15) => Either[ConfigErrors, Z]): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14 append a15).value.right.flatMap(f.tupled)

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
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4], a5: ConfigValue[A5], a6: ConfigValue[A6], a7: ConfigValue[A7], a8: ConfigValue[A8], a9: ConfigValue[A9], a10: ConfigValue[A10], a11: ConfigValue[A11], a12: ConfigValue[A12], a13: ConfigValue[A13], a14: ConfigValue[A14], a15: ConfigValue[A15], a16: ConfigValue[A16])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16) => Z): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14 append a15 append a16).value.right.map(f.tupled)

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
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4], a5: ConfigValue[A5], a6: ConfigValue[A6], a7: ConfigValue[A7], a8: ConfigValue[A8], a9: ConfigValue[A9], a10: ConfigValue[A10], a11: ConfigValue[A11], a12: ConfigValue[A12], a13: ConfigValue[A13], a14: ConfigValue[A14], a15: ConfigValue[A15], a16: ConfigValue[A16])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16) => Either[ConfigErrors, Z]): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14 append a15 append a16).value.right.flatMap(f.tupled)

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
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4], a5: ConfigValue[A5], a6: ConfigValue[A6], a7: ConfigValue[A7], a8: ConfigValue[A8], a9: ConfigValue[A9], a10: ConfigValue[A10], a11: ConfigValue[A11], a12: ConfigValue[A12], a13: ConfigValue[A13], a14: ConfigValue[A14], a15: ConfigValue[A15], a16: ConfigValue[A16], a17: ConfigValue[A17])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17) => Z): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14 append a15 append a16 append a17).value.right.map(f.tupled)

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
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4], a5: ConfigValue[A5], a6: ConfigValue[A6], a7: ConfigValue[A7], a8: ConfigValue[A8], a9: ConfigValue[A9], a10: ConfigValue[A10], a11: ConfigValue[A11], a12: ConfigValue[A12], a13: ConfigValue[A13], a14: ConfigValue[A14], a15: ConfigValue[A15], a16: ConfigValue[A16], a17: ConfigValue[A17])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17) => Either[ConfigErrors, Z]): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14 append a15 append a16 append a17).value.right.flatMap(f.tupled)

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
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4], a5: ConfigValue[A5], a6: ConfigValue[A6], a7: ConfigValue[A7], a8: ConfigValue[A8], a9: ConfigValue[A9], a10: ConfigValue[A10], a11: ConfigValue[A11], a12: ConfigValue[A12], a13: ConfigValue[A13], a14: ConfigValue[A14], a15: ConfigValue[A15], a16: ConfigValue[A16], a17: ConfigValue[A17], a18: ConfigValue[A18])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18) => Z): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14 append a15 append a16 append a17 append a18).value.right.map(f.tupled)

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
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4], a5: ConfigValue[A5], a6: ConfigValue[A6], a7: ConfigValue[A7], a8: ConfigValue[A8], a9: ConfigValue[A9], a10: ConfigValue[A10], a11: ConfigValue[A11], a12: ConfigValue[A12], a13: ConfigValue[A13], a14: ConfigValue[A14], a15: ConfigValue[A15], a16: ConfigValue[A16], a17: ConfigValue[A17], a18: ConfigValue[A18])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18) => Either[ConfigErrors, Z]): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14 append a15 append a16 append a17 append a18).value.right.flatMap(f.tupled)

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
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4], a5: ConfigValue[A5], a6: ConfigValue[A6], a7: ConfigValue[A7], a8: ConfigValue[A8], a9: ConfigValue[A9], a10: ConfigValue[A10], a11: ConfigValue[A11], a12: ConfigValue[A12], a13: ConfigValue[A13], a14: ConfigValue[A14], a15: ConfigValue[A15], a16: ConfigValue[A16], a17: ConfigValue[A17], a18: ConfigValue[A18], a19: ConfigValue[A19])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19) => Z): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14 append a15 append a16 append a17 append a18 append a19).value.right.map(f.tupled)

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
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4], a5: ConfigValue[A5], a6: ConfigValue[A6], a7: ConfigValue[A7], a8: ConfigValue[A8], a9: ConfigValue[A9], a10: ConfigValue[A10], a11: ConfigValue[A11], a12: ConfigValue[A12], a13: ConfigValue[A13], a14: ConfigValue[A14], a15: ConfigValue[A15], a16: ConfigValue[A16], a17: ConfigValue[A17], a18: ConfigValue[A18], a19: ConfigValue[A19])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19) => Either[ConfigErrors, Z]): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14 append a15 append a16 append a17 append a18 append a19).value.right.flatMap(f.tupled)

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
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4], a5: ConfigValue[A5], a6: ConfigValue[A6], a7: ConfigValue[A7], a8: ConfigValue[A8], a9: ConfigValue[A9], a10: ConfigValue[A10], a11: ConfigValue[A11], a12: ConfigValue[A12], a13: ConfigValue[A13], a14: ConfigValue[A14], a15: ConfigValue[A15], a16: ConfigValue[A16], a17: ConfigValue[A17], a18: ConfigValue[A18], a19: ConfigValue[A19], a20: ConfigValue[A20])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20) => Z): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14 append a15 append a16 append a17 append a18 append a19 append a20).value.right.map(f.tupled)

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
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4], a5: ConfigValue[A5], a6: ConfigValue[A6], a7: ConfigValue[A7], a8: ConfigValue[A8], a9: ConfigValue[A9], a10: ConfigValue[A10], a11: ConfigValue[A11], a12: ConfigValue[A12], a13: ConfigValue[A13], a14: ConfigValue[A14], a15: ConfigValue[A15], a16: ConfigValue[A16], a17: ConfigValue[A17], a18: ConfigValue[A18], a19: ConfigValue[A19], a20: ConfigValue[A20])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20) => Either[ConfigErrors, Z]): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14 append a15 append a16 append a17 append a18 append a19 append a20).value.right.flatMap(f.tupled)

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
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def loadConfig[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4], a5: ConfigValue[A5], a6: ConfigValue[A6], a7: ConfigValue[A7], a8: ConfigValue[A8], a9: ConfigValue[A9], a10: ConfigValue[A10], a11: ConfigValue[A11], a12: ConfigValue[A12], a13: ConfigValue[A13], a14: ConfigValue[A14], a15: ConfigValue[A15], a16: ConfigValue[A16], a17: ConfigValue[A17], a18: ConfigValue[A18], a19: ConfigValue[A19], a20: ConfigValue[A20], a21: ConfigValue[A21])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21) => Z): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14 append a15 append a16 append a17 append a18 append a19 append a20 append a21).value.right.map(f.tupled)

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
    * @tparam Z the type of the configuration
    * @return the configuration or errors
    */
  def withValues[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4], a5: ConfigValue[A5], a6: ConfigValue[A6], a7: ConfigValue[A7], a8: ConfigValue[A8], a9: ConfigValue[A9], a10: ConfigValue[A10], a11: ConfigValue[A11], a12: ConfigValue[A12], a13: ConfigValue[A13], a14: ConfigValue[A14], a15: ConfigValue[A15], a16: ConfigValue[A16], a17: ConfigValue[A17], a18: ConfigValue[A18], a19: ConfigValue[A19], a20: ConfigValue[A20], a21: ConfigValue[A21])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21) => Either[ConfigErrors, Z]): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14 append a15 append a16 append a17 append a18 append a19 append a20 append a21).value.right.flatMap(f.tupled)
}
