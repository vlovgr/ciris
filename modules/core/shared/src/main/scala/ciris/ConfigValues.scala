// format: off

/**
  * Generated using sbt source generators.
  * You should not modify this file directly.
  */

package ciris

private[ciris] final class ConfigValue2[A1, A2](val value: Either[ConfigErrors, (A1, A2)]) extends AnyVal {
  def append[A3](next: ConfigValue[A3]): ConfigValue3[A1, A2, A3] = {
    (value, next.value) match {
      case (Right((a1, a2)), Right(a3)) => new ConfigValue3(Right((a1, a2, a3)))
      case (Left(errors), Right(_)) => new ConfigValue3(Left(errors))
      case (Right(_), Left(error)) => new ConfigValue3(Left(ConfigErrors(error)))
      case (Left(errors), Left(error)) => new ConfigValue3(Left(errors append error))
    }
  }
}

private[ciris] final class ConfigValue3[A1, A2, A3](val value: Either[ConfigErrors, (A1, A2, A3)]) extends AnyVal {
  def append[A4](next: ConfigValue[A4]): ConfigValue4[A1, A2, A3, A4] = {
    (value, next.value) match {
      case (Right((a1, a2, a3)), Right(a4)) => new ConfigValue4(Right((a1, a2, a3, a4)))
      case (Left(errors), Right(_)) => new ConfigValue4(Left(errors))
      case (Right(_), Left(error)) => new ConfigValue4(Left(ConfigErrors(error)))
      case (Left(errors), Left(error)) => new ConfigValue4(Left(errors append error))
    }
  }
}

private[ciris] final class ConfigValue4[A1, A2, A3, A4](val value: Either[ConfigErrors, (A1, A2, A3, A4)]) extends AnyVal {
  def append[A5](next: ConfigValue[A5]): ConfigValue5[A1, A2, A3, A4, A5] = {
    (value, next.value) match {
      case (Right((a1, a2, a3, a4)), Right(a5)) => new ConfigValue5(Right((a1, a2, a3, a4, a5)))
      case (Left(errors), Right(_)) => new ConfigValue5(Left(errors))
      case (Right(_), Left(error)) => new ConfigValue5(Left(ConfigErrors(error)))
      case (Left(errors), Left(error)) => new ConfigValue5(Left(errors append error))
    }
  }
}

private[ciris] final class ConfigValue5[A1, A2, A3, A4, A5](val value: Either[ConfigErrors, (A1, A2, A3, A4, A5)]) extends AnyVal {
  def append[A6](next: ConfigValue[A6]): ConfigValue6[A1, A2, A3, A4, A5, A6] = {
    (value, next.value) match {
      case (Right((a1, a2, a3, a4, a5)), Right(a6)) => new ConfigValue6(Right((a1, a2, a3, a4, a5, a6)))
      case (Left(errors), Right(_)) => new ConfigValue6(Left(errors))
      case (Right(_), Left(error)) => new ConfigValue6(Left(ConfigErrors(error)))
      case (Left(errors), Left(error)) => new ConfigValue6(Left(errors append error))
    }
  }
}

private[ciris] final class ConfigValue6[A1, A2, A3, A4, A5, A6](val value: Either[ConfigErrors, (A1, A2, A3, A4, A5, A6)]) extends AnyVal {
  def append[A7](next: ConfigValue[A7]): ConfigValue7[A1, A2, A3, A4, A5, A6, A7] = {
    (value, next.value) match {
      case (Right((a1, a2, a3, a4, a5, a6)), Right(a7)) => new ConfigValue7(Right((a1, a2, a3, a4, a5, a6, a7)))
      case (Left(errors), Right(_)) => new ConfigValue7(Left(errors))
      case (Right(_), Left(error)) => new ConfigValue7(Left(ConfigErrors(error)))
      case (Left(errors), Left(error)) => new ConfigValue7(Left(errors append error))
    }
  }
}

private[ciris] final class ConfigValue7[A1, A2, A3, A4, A5, A6, A7](val value: Either[ConfigErrors, (A1, A2, A3, A4, A5, A6, A7)]) extends AnyVal {
  def append[A8](next: ConfigValue[A8]): ConfigValue8[A1, A2, A3, A4, A5, A6, A7, A8] = {
    (value, next.value) match {
      case (Right((a1, a2, a3, a4, a5, a6, a7)), Right(a8)) => new ConfigValue8(Right((a1, a2, a3, a4, a5, a6, a7, a8)))
      case (Left(errors), Right(_)) => new ConfigValue8(Left(errors))
      case (Right(_), Left(error)) => new ConfigValue8(Left(ConfigErrors(error)))
      case (Left(errors), Left(error)) => new ConfigValue8(Left(errors append error))
    }
  }
}

private[ciris] final class ConfigValue8[A1, A2, A3, A4, A5, A6, A7, A8](val value: Either[ConfigErrors, (A1, A2, A3, A4, A5, A6, A7, A8)]) extends AnyVal {
  def append[A9](next: ConfigValue[A9]): ConfigValue9[A1, A2, A3, A4, A5, A6, A7, A8, A9] = {
    (value, next.value) match {
      case (Right((a1, a2, a3, a4, a5, a6, a7, a8)), Right(a9)) => new ConfigValue9(Right((a1, a2, a3, a4, a5, a6, a7, a8, a9)))
      case (Left(errors), Right(_)) => new ConfigValue9(Left(errors))
      case (Right(_), Left(error)) => new ConfigValue9(Left(ConfigErrors(error)))
      case (Left(errors), Left(error)) => new ConfigValue9(Left(errors append error))
    }
  }
}

private[ciris] final class ConfigValue9[A1, A2, A3, A4, A5, A6, A7, A8, A9](val value: Either[ConfigErrors, (A1, A2, A3, A4, A5, A6, A7, A8, A9)]) extends AnyVal {
  def append[A10](next: ConfigValue[A10]): ConfigValue10[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10] = {
    (value, next.value) match {
      case (Right((a1, a2, a3, a4, a5, a6, a7, a8, a9)), Right(a10)) => new ConfigValue10(Right((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10)))
      case (Left(errors), Right(_)) => new ConfigValue10(Left(errors))
      case (Right(_), Left(error)) => new ConfigValue10(Left(ConfigErrors(error)))
      case (Left(errors), Left(error)) => new ConfigValue10(Left(errors append error))
    }
  }
}

private[ciris] final class ConfigValue10[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10](val value: Either[ConfigErrors, (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10)]) extends AnyVal {
  def append[A11](next: ConfigValue[A11]): ConfigValue11[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11] = {
    (value, next.value) match {
      case (Right((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10)), Right(a11)) => new ConfigValue11(Right((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11)))
      case (Left(errors), Right(_)) => new ConfigValue11(Left(errors))
      case (Right(_), Left(error)) => new ConfigValue11(Left(ConfigErrors(error)))
      case (Left(errors), Left(error)) => new ConfigValue11(Left(errors append error))
    }
  }
}

private[ciris] final class ConfigValue11[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11](val value: Either[ConfigErrors, (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11)]) extends AnyVal {
  def append[A12](next: ConfigValue[A12]): ConfigValue12[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12] = {
    (value, next.value) match {
      case (Right((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11)), Right(a12)) => new ConfigValue12(Right((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12)))
      case (Left(errors), Right(_)) => new ConfigValue12(Left(errors))
      case (Right(_), Left(error)) => new ConfigValue12(Left(ConfigErrors(error)))
      case (Left(errors), Left(error)) => new ConfigValue12(Left(errors append error))
    }
  }
}

private[ciris] final class ConfigValue12[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12](val value: Either[ConfigErrors, (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12)]) extends AnyVal {
  def append[A13](next: ConfigValue[A13]): ConfigValue13[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13] = {
    (value, next.value) match {
      case (Right((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12)), Right(a13)) => new ConfigValue13(Right((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13)))
      case (Left(errors), Right(_)) => new ConfigValue13(Left(errors))
      case (Right(_), Left(error)) => new ConfigValue13(Left(ConfigErrors(error)))
      case (Left(errors), Left(error)) => new ConfigValue13(Left(errors append error))
    }
  }
}

private[ciris] final class ConfigValue13[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13](val value: Either[ConfigErrors, (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13)]) extends AnyVal {
  def append[A14](next: ConfigValue[A14]): ConfigValue14[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14] = {
    (value, next.value) match {
      case (Right((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13)), Right(a14)) => new ConfigValue14(Right((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14)))
      case (Left(errors), Right(_)) => new ConfigValue14(Left(errors))
      case (Right(_), Left(error)) => new ConfigValue14(Left(ConfigErrors(error)))
      case (Left(errors), Left(error)) => new ConfigValue14(Left(errors append error))
    }
  }
}

private[ciris] final class ConfigValue14[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14](val value: Either[ConfigErrors, (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14)]) extends AnyVal {
  def append[A15](next: ConfigValue[A15]): ConfigValue15[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15] = {
    (value, next.value) match {
      case (Right((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14)), Right(a15)) => new ConfigValue15(Right((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15)))
      case (Left(errors), Right(_)) => new ConfigValue15(Left(errors))
      case (Right(_), Left(error)) => new ConfigValue15(Left(ConfigErrors(error)))
      case (Left(errors), Left(error)) => new ConfigValue15(Left(errors append error))
    }
  }
}

private[ciris] final class ConfigValue15[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15](val value: Either[ConfigErrors, (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15)]) extends AnyVal {
  def append[A16](next: ConfigValue[A16]): ConfigValue16[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16] = {
    (value, next.value) match {
      case (Right((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15)), Right(a16)) => new ConfigValue16(Right((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16)))
      case (Left(errors), Right(_)) => new ConfigValue16(Left(errors))
      case (Right(_), Left(error)) => new ConfigValue16(Left(ConfigErrors(error)))
      case (Left(errors), Left(error)) => new ConfigValue16(Left(errors append error))
    }
  }
}

private[ciris] final class ConfigValue16[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16](val value: Either[ConfigErrors, (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16)]) extends AnyVal {
  def append[A17](next: ConfigValue[A17]): ConfigValue17[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17] = {
    (value, next.value) match {
      case (Right((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16)), Right(a17)) => new ConfigValue17(Right((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17)))
      case (Left(errors), Right(_)) => new ConfigValue17(Left(errors))
      case (Right(_), Left(error)) => new ConfigValue17(Left(ConfigErrors(error)))
      case (Left(errors), Left(error)) => new ConfigValue17(Left(errors append error))
    }
  }
}

private[ciris] final class ConfigValue17[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17](val value: Either[ConfigErrors, (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17)]) extends AnyVal {
  def append[A18](next: ConfigValue[A18]): ConfigValue18[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18] = {
    (value, next.value) match {
      case (Right((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17)), Right(a18)) => new ConfigValue18(Right((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18)))
      case (Left(errors), Right(_)) => new ConfigValue18(Left(errors))
      case (Right(_), Left(error)) => new ConfigValue18(Left(ConfigErrors(error)))
      case (Left(errors), Left(error)) => new ConfigValue18(Left(errors append error))
    }
  }
}

private[ciris] final class ConfigValue18[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18](val value: Either[ConfigErrors, (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18)]) extends AnyVal {
  def append[A19](next: ConfigValue[A19]): ConfigValue19[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19] = {
    (value, next.value) match {
      case (Right((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18)), Right(a19)) => new ConfigValue19(Right((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19)))
      case (Left(errors), Right(_)) => new ConfigValue19(Left(errors))
      case (Right(_), Left(error)) => new ConfigValue19(Left(ConfigErrors(error)))
      case (Left(errors), Left(error)) => new ConfigValue19(Left(errors append error))
    }
  }
}

private[ciris] final class ConfigValue19[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19](val value: Either[ConfigErrors, (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19)]) extends AnyVal {
  def append[A20](next: ConfigValue[A20]): ConfigValue20[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20] = {
    (value, next.value) match {
      case (Right((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19)), Right(a20)) => new ConfigValue20(Right((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20)))
      case (Left(errors), Right(_)) => new ConfigValue20(Left(errors))
      case (Right(_), Left(error)) => new ConfigValue20(Left(ConfigErrors(error)))
      case (Left(errors), Left(error)) => new ConfigValue20(Left(errors append error))
    }
  }
}

private[ciris] final class ConfigValue20[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20](val value: Either[ConfigErrors, (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20)]) extends AnyVal {
  def append[A21](next: ConfigValue[A21]): ConfigValue21[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21] = {
    (value, next.value) match {
      case (Right((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20)), Right(a21)) => new ConfigValue21(Right((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21)))
      case (Left(errors), Right(_)) => new ConfigValue21(Left(errors))
      case (Right(_), Left(error)) => new ConfigValue21(Left(ConfigErrors(error)))
      case (Left(errors), Left(error)) => new ConfigValue21(Left(errors append error))
    }
  }
}

private[ciris] final class ConfigValue21[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21](val value: Either[ConfigErrors, (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21)]) extends AnyVal
