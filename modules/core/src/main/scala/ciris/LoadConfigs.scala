// format: off

/**
  * Generated using sbt source generators.
  * You should not modify this file directly.
  */

package ciris

private [ciris] trait LoadConfigs {
  def loadConfig[Z](z: Z): Either[ConfigErrors, Z] =
    Right(z)

  def loadConfig[A1, Z](a1: ConfigValue[A1])(f: A1 ⇒ Z): Either[ConfigErrors, Z] =
    a1.value.fold(error ⇒ Left(ConfigErrors(error)), a1 ⇒ Right(f(a1)))

  def withConfig[A1, Z](a1: ConfigValue[A1])(f: A1 => Either[ConfigErrors, Z]): Either[ConfigErrors, Z] =
    a1.value.fold(error ⇒ Left(ConfigErrors(error)), f)

  def loadConfig[A1, A2, Z](a1: ConfigValue[A1], a2: ConfigValue[A2])(f: (A1, A2) => Z): Either[ConfigErrors, Z] =
    (a1 append a2).value.right.map(f.tupled)
  
  def withConfig[A1, A2, Z](a1: ConfigValue[A1], a2: ConfigValue[A2])(f: (A1, A2) => Either[ConfigErrors, Z]): Either[ConfigErrors, Z] =
    (a1 append a2).value.right.flatMap(f.tupled)

  def loadConfig[A1, A2, A3, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3])(f: (A1, A2, A3) => Z): Either[ConfigErrors, Z] =
    (a1 append a2 append a3).value.right.map(f.tupled)
  
  def withConfig[A1, A2, A3, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3])(f: (A1, A2, A3) => Either[ConfigErrors, Z]): Either[ConfigErrors, Z] =
    (a1 append a2 append a3).value.right.flatMap(f.tupled)

  def loadConfig[A1, A2, A3, A4, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4])(f: (A1, A2, A3, A4) => Z): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4).value.right.map(f.tupled)
  
  def withConfig[A1, A2, A3, A4, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4])(f: (A1, A2, A3, A4) => Either[ConfigErrors, Z]): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4).value.right.flatMap(f.tupled)

  def loadConfig[A1, A2, A3, A4, A5, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4], a5: ConfigValue[A5])(f: (A1, A2, A3, A4, A5) => Z): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4 append a5).value.right.map(f.tupled)
  
  def withConfig[A1, A2, A3, A4, A5, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4], a5: ConfigValue[A5])(f: (A1, A2, A3, A4, A5) => Either[ConfigErrors, Z]): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4 append a5).value.right.flatMap(f.tupled)

  def loadConfig[A1, A2, A3, A4, A5, A6, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4], a5: ConfigValue[A5], a6: ConfigValue[A6])(f: (A1, A2, A3, A4, A5, A6) => Z): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4 append a5 append a6).value.right.map(f.tupled)
  
  def withConfig[A1, A2, A3, A4, A5, A6, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4], a5: ConfigValue[A5], a6: ConfigValue[A6])(f: (A1, A2, A3, A4, A5, A6) => Either[ConfigErrors, Z]): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4 append a5 append a6).value.right.flatMap(f.tupled)

  def loadConfig[A1, A2, A3, A4, A5, A6, A7, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4], a5: ConfigValue[A5], a6: ConfigValue[A6], a7: ConfigValue[A7])(f: (A1, A2, A3, A4, A5, A6, A7) => Z): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7).value.right.map(f.tupled)
  
  def withConfig[A1, A2, A3, A4, A5, A6, A7, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4], a5: ConfigValue[A5], a6: ConfigValue[A6], a7: ConfigValue[A7])(f: (A1, A2, A3, A4, A5, A6, A7) => Either[ConfigErrors, Z]): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7).value.right.flatMap(f.tupled)

  def loadConfig[A1, A2, A3, A4, A5, A6, A7, A8, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4], a5: ConfigValue[A5], a6: ConfigValue[A6], a7: ConfigValue[A7], a8: ConfigValue[A8])(f: (A1, A2, A3, A4, A5, A6, A7, A8) => Z): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8).value.right.map(f.tupled)
  
  def withConfig[A1, A2, A3, A4, A5, A6, A7, A8, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4], a5: ConfigValue[A5], a6: ConfigValue[A6], a7: ConfigValue[A7], a8: ConfigValue[A8])(f: (A1, A2, A3, A4, A5, A6, A7, A8) => Either[ConfigErrors, Z]): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8).value.right.flatMap(f.tupled)

  def loadConfig[A1, A2, A3, A4, A5, A6, A7, A8, A9, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4], a5: ConfigValue[A5], a6: ConfigValue[A6], a7: ConfigValue[A7], a8: ConfigValue[A8], a9: ConfigValue[A9])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9) => Z): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9).value.right.map(f.tupled)
  
  def withConfig[A1, A2, A3, A4, A5, A6, A7, A8, A9, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4], a5: ConfigValue[A5], a6: ConfigValue[A6], a7: ConfigValue[A7], a8: ConfigValue[A8], a9: ConfigValue[A9])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9) => Either[ConfigErrors, Z]): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9).value.right.flatMap(f.tupled)

  def loadConfig[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4], a5: ConfigValue[A5], a6: ConfigValue[A6], a7: ConfigValue[A7], a8: ConfigValue[A8], a9: ConfigValue[A9], a10: ConfigValue[A10])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10) => Z): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10).value.right.map(f.tupled)
  
  def withConfig[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4], a5: ConfigValue[A5], a6: ConfigValue[A6], a7: ConfigValue[A7], a8: ConfigValue[A8], a9: ConfigValue[A9], a10: ConfigValue[A10])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10) => Either[ConfigErrors, Z]): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10).value.right.flatMap(f.tupled)

  def loadConfig[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4], a5: ConfigValue[A5], a6: ConfigValue[A6], a7: ConfigValue[A7], a8: ConfigValue[A8], a9: ConfigValue[A9], a10: ConfigValue[A10], a11: ConfigValue[A11])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11) => Z): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11).value.right.map(f.tupled)
  
  def withConfig[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4], a5: ConfigValue[A5], a6: ConfigValue[A6], a7: ConfigValue[A7], a8: ConfigValue[A8], a9: ConfigValue[A9], a10: ConfigValue[A10], a11: ConfigValue[A11])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11) => Either[ConfigErrors, Z]): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11).value.right.flatMap(f.tupled)

  def loadConfig[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4], a5: ConfigValue[A5], a6: ConfigValue[A6], a7: ConfigValue[A7], a8: ConfigValue[A8], a9: ConfigValue[A9], a10: ConfigValue[A10], a11: ConfigValue[A11], a12: ConfigValue[A12])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12) => Z): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12).value.right.map(f.tupled)
  
  def withConfig[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4], a5: ConfigValue[A5], a6: ConfigValue[A6], a7: ConfigValue[A7], a8: ConfigValue[A8], a9: ConfigValue[A9], a10: ConfigValue[A10], a11: ConfigValue[A11], a12: ConfigValue[A12])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12) => Either[ConfigErrors, Z]): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12).value.right.flatMap(f.tupled)

  def loadConfig[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4], a5: ConfigValue[A5], a6: ConfigValue[A6], a7: ConfigValue[A7], a8: ConfigValue[A8], a9: ConfigValue[A9], a10: ConfigValue[A10], a11: ConfigValue[A11], a12: ConfigValue[A12], a13: ConfigValue[A13])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13) => Z): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13).value.right.map(f.tupled)
  
  def withConfig[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4], a5: ConfigValue[A5], a6: ConfigValue[A6], a7: ConfigValue[A7], a8: ConfigValue[A8], a9: ConfigValue[A9], a10: ConfigValue[A10], a11: ConfigValue[A11], a12: ConfigValue[A12], a13: ConfigValue[A13])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13) => Either[ConfigErrors, Z]): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13).value.right.flatMap(f.tupled)

  def loadConfig[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4], a5: ConfigValue[A5], a6: ConfigValue[A6], a7: ConfigValue[A7], a8: ConfigValue[A8], a9: ConfigValue[A9], a10: ConfigValue[A10], a11: ConfigValue[A11], a12: ConfigValue[A12], a13: ConfigValue[A13], a14: ConfigValue[A14])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14) => Z): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14).value.right.map(f.tupled)
  
  def withConfig[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4], a5: ConfigValue[A5], a6: ConfigValue[A6], a7: ConfigValue[A7], a8: ConfigValue[A8], a9: ConfigValue[A9], a10: ConfigValue[A10], a11: ConfigValue[A11], a12: ConfigValue[A12], a13: ConfigValue[A13], a14: ConfigValue[A14])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14) => Either[ConfigErrors, Z]): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14).value.right.flatMap(f.tupled)

  def loadConfig[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4], a5: ConfigValue[A5], a6: ConfigValue[A6], a7: ConfigValue[A7], a8: ConfigValue[A8], a9: ConfigValue[A9], a10: ConfigValue[A10], a11: ConfigValue[A11], a12: ConfigValue[A12], a13: ConfigValue[A13], a14: ConfigValue[A14], a15: ConfigValue[A15])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15) => Z): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14 append a15).value.right.map(f.tupled)
  
  def withConfig[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4], a5: ConfigValue[A5], a6: ConfigValue[A6], a7: ConfigValue[A7], a8: ConfigValue[A8], a9: ConfigValue[A9], a10: ConfigValue[A10], a11: ConfigValue[A11], a12: ConfigValue[A12], a13: ConfigValue[A13], a14: ConfigValue[A14], a15: ConfigValue[A15])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15) => Either[ConfigErrors, Z]): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14 append a15).value.right.flatMap(f.tupled)

  def loadConfig[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4], a5: ConfigValue[A5], a6: ConfigValue[A6], a7: ConfigValue[A7], a8: ConfigValue[A8], a9: ConfigValue[A9], a10: ConfigValue[A10], a11: ConfigValue[A11], a12: ConfigValue[A12], a13: ConfigValue[A13], a14: ConfigValue[A14], a15: ConfigValue[A15], a16: ConfigValue[A16])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16) => Z): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14 append a15 append a16).value.right.map(f.tupled)
  
  def withConfig[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4], a5: ConfigValue[A5], a6: ConfigValue[A6], a7: ConfigValue[A7], a8: ConfigValue[A8], a9: ConfigValue[A9], a10: ConfigValue[A10], a11: ConfigValue[A11], a12: ConfigValue[A12], a13: ConfigValue[A13], a14: ConfigValue[A14], a15: ConfigValue[A15], a16: ConfigValue[A16])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16) => Either[ConfigErrors, Z]): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14 append a15 append a16).value.right.flatMap(f.tupled)

  def loadConfig[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4], a5: ConfigValue[A5], a6: ConfigValue[A6], a7: ConfigValue[A7], a8: ConfigValue[A8], a9: ConfigValue[A9], a10: ConfigValue[A10], a11: ConfigValue[A11], a12: ConfigValue[A12], a13: ConfigValue[A13], a14: ConfigValue[A14], a15: ConfigValue[A15], a16: ConfigValue[A16], a17: ConfigValue[A17])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17) => Z): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14 append a15 append a16 append a17).value.right.map(f.tupled)
  
  def withConfig[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4], a5: ConfigValue[A5], a6: ConfigValue[A6], a7: ConfigValue[A7], a8: ConfigValue[A8], a9: ConfigValue[A9], a10: ConfigValue[A10], a11: ConfigValue[A11], a12: ConfigValue[A12], a13: ConfigValue[A13], a14: ConfigValue[A14], a15: ConfigValue[A15], a16: ConfigValue[A16], a17: ConfigValue[A17])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17) => Either[ConfigErrors, Z]): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14 append a15 append a16 append a17).value.right.flatMap(f.tupled)

  def loadConfig[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4], a5: ConfigValue[A5], a6: ConfigValue[A6], a7: ConfigValue[A7], a8: ConfigValue[A8], a9: ConfigValue[A9], a10: ConfigValue[A10], a11: ConfigValue[A11], a12: ConfigValue[A12], a13: ConfigValue[A13], a14: ConfigValue[A14], a15: ConfigValue[A15], a16: ConfigValue[A16], a17: ConfigValue[A17], a18: ConfigValue[A18])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18) => Z): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14 append a15 append a16 append a17 append a18).value.right.map(f.tupled)
  
  def withConfig[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4], a5: ConfigValue[A5], a6: ConfigValue[A6], a7: ConfigValue[A7], a8: ConfigValue[A8], a9: ConfigValue[A9], a10: ConfigValue[A10], a11: ConfigValue[A11], a12: ConfigValue[A12], a13: ConfigValue[A13], a14: ConfigValue[A14], a15: ConfigValue[A15], a16: ConfigValue[A16], a17: ConfigValue[A17], a18: ConfigValue[A18])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18) => Either[ConfigErrors, Z]): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14 append a15 append a16 append a17 append a18).value.right.flatMap(f.tupled)

  def loadConfig[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4], a5: ConfigValue[A5], a6: ConfigValue[A6], a7: ConfigValue[A7], a8: ConfigValue[A8], a9: ConfigValue[A9], a10: ConfigValue[A10], a11: ConfigValue[A11], a12: ConfigValue[A12], a13: ConfigValue[A13], a14: ConfigValue[A14], a15: ConfigValue[A15], a16: ConfigValue[A16], a17: ConfigValue[A17], a18: ConfigValue[A18], a19: ConfigValue[A19])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19) => Z): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14 append a15 append a16 append a17 append a18 append a19).value.right.map(f.tupled)
  
  def withConfig[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4], a5: ConfigValue[A5], a6: ConfigValue[A6], a7: ConfigValue[A7], a8: ConfigValue[A8], a9: ConfigValue[A9], a10: ConfigValue[A10], a11: ConfigValue[A11], a12: ConfigValue[A12], a13: ConfigValue[A13], a14: ConfigValue[A14], a15: ConfigValue[A15], a16: ConfigValue[A16], a17: ConfigValue[A17], a18: ConfigValue[A18], a19: ConfigValue[A19])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19) => Either[ConfigErrors, Z]): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14 append a15 append a16 append a17 append a18 append a19).value.right.flatMap(f.tupled)

  def loadConfig[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4], a5: ConfigValue[A5], a6: ConfigValue[A6], a7: ConfigValue[A7], a8: ConfigValue[A8], a9: ConfigValue[A9], a10: ConfigValue[A10], a11: ConfigValue[A11], a12: ConfigValue[A12], a13: ConfigValue[A13], a14: ConfigValue[A14], a15: ConfigValue[A15], a16: ConfigValue[A16], a17: ConfigValue[A17], a18: ConfigValue[A18], a19: ConfigValue[A19], a20: ConfigValue[A20])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20) => Z): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14 append a15 append a16 append a17 append a18 append a19 append a20).value.right.map(f.tupled)
  
  def withConfig[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4], a5: ConfigValue[A5], a6: ConfigValue[A6], a7: ConfigValue[A7], a8: ConfigValue[A8], a9: ConfigValue[A9], a10: ConfigValue[A10], a11: ConfigValue[A11], a12: ConfigValue[A12], a13: ConfigValue[A13], a14: ConfigValue[A14], a15: ConfigValue[A15], a16: ConfigValue[A16], a17: ConfigValue[A17], a18: ConfigValue[A18], a19: ConfigValue[A19], a20: ConfigValue[A20])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20) => Either[ConfigErrors, Z]): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14 append a15 append a16 append a17 append a18 append a19 append a20).value.right.flatMap(f.tupled)

  def loadConfig[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4], a5: ConfigValue[A5], a6: ConfigValue[A6], a7: ConfigValue[A7], a8: ConfigValue[A8], a9: ConfigValue[A9], a10: ConfigValue[A10], a11: ConfigValue[A11], a12: ConfigValue[A12], a13: ConfigValue[A13], a14: ConfigValue[A14], a15: ConfigValue[A15], a16: ConfigValue[A16], a17: ConfigValue[A17], a18: ConfigValue[A18], a19: ConfigValue[A19], a20: ConfigValue[A20], a21: ConfigValue[A21])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21) => Z): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14 append a15 append a16 append a17 append a18 append a19 append a20 append a21).value.right.map(f.tupled)
  
  def withConfig[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, Z](a1: ConfigValue[A1], a2: ConfigValue[A2], a3: ConfigValue[A3], a4: ConfigValue[A4], a5: ConfigValue[A5], a6: ConfigValue[A6], a7: ConfigValue[A7], a8: ConfigValue[A8], a9: ConfigValue[A9], a10: ConfigValue[A10], a11: ConfigValue[A11], a12: ConfigValue[A12], a13: ConfigValue[A13], a14: ConfigValue[A14], a15: ConfigValue[A15], a16: ConfigValue[A16], a17: ConfigValue[A17], a18: ConfigValue[A18], a19: ConfigValue[A19], a20: ConfigValue[A20], a21: ConfigValue[A21])(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21) => Either[ConfigErrors, Z]): Either[ConfigErrors, Z] =
    (a1 append a2 append a3 append a4 append a5 append a6 append a7 append a8 append a9 append a10 append a11 append a12 append a13 append a14 append a15 append a16 append a17 append a18 append a19 append a20 append a21).value.right.flatMap(f.tupled)
}
