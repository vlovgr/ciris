// format: off

/**
  * Generated using sbt source generators.
  * You should not modify this file directly.
  */

package ciris

import ciris.api._
import ciris.api.syntax._

private[ciris] final class ConfigValue2[F[_]: Apply, A1, A2](val value: F[Either[ConfigErrors, (A1, A2)]]) {
  def append[A3](next: ConfigEntry[F, _, _, A3]): ConfigValue3[F, A1, A2, A3] = {
    new ConfigValue3((value product next.value).map {
      case (Right((a1, a2)), Right(a3)) => Right((a1, a2, a3))
      case (Left(errors), Right(_)) => Left(errors)
      case (Right(_), Left(error)) => Left(ConfigErrors(error))
      case (Left(errors), Left(error)) => Left(errors append error)
    })
  }
}

private[ciris] final class ConfigValue3[F[_]: Apply, A1, A2, A3](val value: F[Either[ConfigErrors, (A1, A2, A3)]]) {
  def append[A4](next: ConfigEntry[F, _, _, A4]): ConfigValue4[F, A1, A2, A3, A4] = {
    new ConfigValue4((value product next.value).map {
      case (Right((a1, a2, a3)), Right(a4)) => Right((a1, a2, a3, a4))
      case (Left(errors), Right(_)) => Left(errors)
      case (Right(_), Left(error)) => Left(ConfigErrors(error))
      case (Left(errors), Left(error)) => Left(errors append error)
    })
  }
}

private[ciris] final class ConfigValue4[F[_]: Apply, A1, A2, A3, A4](val value: F[Either[ConfigErrors, (A1, A2, A3, A4)]]) {
  def append[A5](next: ConfigEntry[F, _, _, A5]): ConfigValue5[F, A1, A2, A3, A4, A5] = {
    new ConfigValue5((value product next.value).map {
      case (Right((a1, a2, a3, a4)), Right(a5)) => Right((a1, a2, a3, a4, a5))
      case (Left(errors), Right(_)) => Left(errors)
      case (Right(_), Left(error)) => Left(ConfigErrors(error))
      case (Left(errors), Left(error)) => Left(errors append error)
    })
  }
}

private[ciris] final class ConfigValue5[F[_]: Apply, A1, A2, A3, A4, A5](val value: F[Either[ConfigErrors, (A1, A2, A3, A4, A5)]]) {
  def append[A6](next: ConfigEntry[F, _, _, A6]): ConfigValue6[F, A1, A2, A3, A4, A5, A6] = {
    new ConfigValue6((value product next.value).map {
      case (Right((a1, a2, a3, a4, a5)), Right(a6)) => Right((a1, a2, a3, a4, a5, a6))
      case (Left(errors), Right(_)) => Left(errors)
      case (Right(_), Left(error)) => Left(ConfigErrors(error))
      case (Left(errors), Left(error)) => Left(errors append error)
    })
  }
}

private[ciris] final class ConfigValue6[F[_]: Apply, A1, A2, A3, A4, A5, A6](val value: F[Either[ConfigErrors, (A1, A2, A3, A4, A5, A6)]]) {
  def append[A7](next: ConfigEntry[F, _, _, A7]): ConfigValue7[F, A1, A2, A3, A4, A5, A6, A7] = {
    new ConfigValue7((value product next.value).map {
      case (Right((a1, a2, a3, a4, a5, a6)), Right(a7)) => Right((a1, a2, a3, a4, a5, a6, a7))
      case (Left(errors), Right(_)) => Left(errors)
      case (Right(_), Left(error)) => Left(ConfigErrors(error))
      case (Left(errors), Left(error)) => Left(errors append error)
    })
  }
}

private[ciris] final class ConfigValue7[F[_]: Apply, A1, A2, A3, A4, A5, A6, A7](val value: F[Either[ConfigErrors, (A1, A2, A3, A4, A5, A6, A7)]]) {
  def append[A8](next: ConfigEntry[F, _, _, A8]): ConfigValue8[F, A1, A2, A3, A4, A5, A6, A7, A8] = {
    new ConfigValue8((value product next.value).map {
      case (Right((a1, a2, a3, a4, a5, a6, a7)), Right(a8)) => Right((a1, a2, a3, a4, a5, a6, a7, a8))
      case (Left(errors), Right(_)) => Left(errors)
      case (Right(_), Left(error)) => Left(ConfigErrors(error))
      case (Left(errors), Left(error)) => Left(errors append error)
    })
  }
}

private[ciris] final class ConfigValue8[F[_]: Apply, A1, A2, A3, A4, A5, A6, A7, A8](val value: F[Either[ConfigErrors, (A1, A2, A3, A4, A5, A6, A7, A8)]]) {
  def append[A9](next: ConfigEntry[F, _, _, A9]): ConfigValue9[F, A1, A2, A3, A4, A5, A6, A7, A8, A9] = {
    new ConfigValue9((value product next.value).map {
      case (Right((a1, a2, a3, a4, a5, a6, a7, a8)), Right(a9)) => Right((a1, a2, a3, a4, a5, a6, a7, a8, a9))
      case (Left(errors), Right(_)) => Left(errors)
      case (Right(_), Left(error)) => Left(ConfigErrors(error))
      case (Left(errors), Left(error)) => Left(errors append error)
    })
  }
}

private[ciris] final class ConfigValue9[F[_]: Apply, A1, A2, A3, A4, A5, A6, A7, A8, A9](val value: F[Either[ConfigErrors, (A1, A2, A3, A4, A5, A6, A7, A8, A9)]]) {
  def append[A10](next: ConfigEntry[F, _, _, A10]): ConfigValue10[F, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10] = {
    new ConfigValue10((value product next.value).map {
      case (Right((a1, a2, a3, a4, a5, a6, a7, a8, a9)), Right(a10)) => Right((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10))
      case (Left(errors), Right(_)) => Left(errors)
      case (Right(_), Left(error)) => Left(ConfigErrors(error))
      case (Left(errors), Left(error)) => Left(errors append error)
    })
  }
}

private[ciris] final class ConfigValue10[F[_]: Apply, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10](val value: F[Either[ConfigErrors, (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10)]]) {
  def append[A11](next: ConfigEntry[F, _, _, A11]): ConfigValue11[F, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11] = {
    new ConfigValue11((value product next.value).map {
      case (Right((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10)), Right(a11)) => Right((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11))
      case (Left(errors), Right(_)) => Left(errors)
      case (Right(_), Left(error)) => Left(ConfigErrors(error))
      case (Left(errors), Left(error)) => Left(errors append error)
    })
  }
}

private[ciris] final class ConfigValue11[F[_]: Apply, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11](val value: F[Either[ConfigErrors, (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11)]]) {
  def append[A12](next: ConfigEntry[F, _, _, A12]): ConfigValue12[F, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12] = {
    new ConfigValue12((value product next.value).map {
      case (Right((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11)), Right(a12)) => Right((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12))
      case (Left(errors), Right(_)) => Left(errors)
      case (Right(_), Left(error)) => Left(ConfigErrors(error))
      case (Left(errors), Left(error)) => Left(errors append error)
    })
  }
}

private[ciris] final class ConfigValue12[F[_]: Apply, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12](val value: F[Either[ConfigErrors, (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12)]]) {
  def append[A13](next: ConfigEntry[F, _, _, A13]): ConfigValue13[F, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13] = {
    new ConfigValue13((value product next.value).map {
      case (Right((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12)), Right(a13)) => Right((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13))
      case (Left(errors), Right(_)) => Left(errors)
      case (Right(_), Left(error)) => Left(ConfigErrors(error))
      case (Left(errors), Left(error)) => Left(errors append error)
    })
  }
}

private[ciris] final class ConfigValue13[F[_]: Apply, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13](val value: F[Either[ConfigErrors, (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13)]]) {
  def append[A14](next: ConfigEntry[F, _, _, A14]): ConfigValue14[F, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14] = {
    new ConfigValue14((value product next.value).map {
      case (Right((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13)), Right(a14)) => Right((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14))
      case (Left(errors), Right(_)) => Left(errors)
      case (Right(_), Left(error)) => Left(ConfigErrors(error))
      case (Left(errors), Left(error)) => Left(errors append error)
    })
  }
}

private[ciris] final class ConfigValue14[F[_]: Apply, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14](val value: F[Either[ConfigErrors, (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14)]]) {
  def append[A15](next: ConfigEntry[F, _, _, A15]): ConfigValue15[F, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15] = {
    new ConfigValue15((value product next.value).map {
      case (Right((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14)), Right(a15)) => Right((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15))
      case (Left(errors), Right(_)) => Left(errors)
      case (Right(_), Left(error)) => Left(ConfigErrors(error))
      case (Left(errors), Left(error)) => Left(errors append error)
    })
  }
}

private[ciris] final class ConfigValue15[F[_]: Apply, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15](val value: F[Either[ConfigErrors, (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15)]]) {
  def append[A16](next: ConfigEntry[F, _, _, A16]): ConfigValue16[F, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16] = {
    new ConfigValue16((value product next.value).map {
      case (Right((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15)), Right(a16)) => Right((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16))
      case (Left(errors), Right(_)) => Left(errors)
      case (Right(_), Left(error)) => Left(ConfigErrors(error))
      case (Left(errors), Left(error)) => Left(errors append error)
    })
  }
}

private[ciris] final class ConfigValue16[F[_]: Apply, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16](val value: F[Either[ConfigErrors, (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16)]]) {
  def append[A17](next: ConfigEntry[F, _, _, A17]): ConfigValue17[F, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17] = {
    new ConfigValue17((value product next.value).map {
      case (Right((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16)), Right(a17)) => Right((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17))
      case (Left(errors), Right(_)) => Left(errors)
      case (Right(_), Left(error)) => Left(ConfigErrors(error))
      case (Left(errors), Left(error)) => Left(errors append error)
    })
  }
}

private[ciris] final class ConfigValue17[F[_]: Apply, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17](val value: F[Either[ConfigErrors, (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17)]]) {
  def append[A18](next: ConfigEntry[F, _, _, A18]): ConfigValue18[F, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18] = {
    new ConfigValue18((value product next.value).map {
      case (Right((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17)), Right(a18)) => Right((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18))
      case (Left(errors), Right(_)) => Left(errors)
      case (Right(_), Left(error)) => Left(ConfigErrors(error))
      case (Left(errors), Left(error)) => Left(errors append error)
    })
  }
}

private[ciris] final class ConfigValue18[F[_]: Apply, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18](val value: F[Either[ConfigErrors, (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18)]]) {
  def append[A19](next: ConfigEntry[F, _, _, A19]): ConfigValue19[F, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19] = {
    new ConfigValue19((value product next.value).map {
      case (Right((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18)), Right(a19)) => Right((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19))
      case (Left(errors), Right(_)) => Left(errors)
      case (Right(_), Left(error)) => Left(ConfigErrors(error))
      case (Left(errors), Left(error)) => Left(errors append error)
    })
  }
}

private[ciris] final class ConfigValue19[F[_]: Apply, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19](val value: F[Either[ConfigErrors, (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19)]]) {
  def append[A20](next: ConfigEntry[F, _, _, A20]): ConfigValue20[F, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20] = {
    new ConfigValue20((value product next.value).map {
      case (Right((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19)), Right(a20)) => Right((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20))
      case (Left(errors), Right(_)) => Left(errors)
      case (Right(_), Left(error)) => Left(ConfigErrors(error))
      case (Left(errors), Left(error)) => Left(errors append error)
    })
  }
}

private[ciris] final class ConfigValue20[F[_]: Apply, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20](val value: F[Either[ConfigErrors, (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20)]]) {
  def append[A21](next: ConfigEntry[F, _, _, A21]): ConfigValue21[F, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21] = {
    new ConfigValue21((value product next.value).map {
      case (Right((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20)), Right(a21)) => Right((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21))
      case (Left(errors), Right(_)) => Left(errors)
      case (Right(_), Left(error)) => Left(ConfigErrors(error))
      case (Left(errors), Left(error)) => Left(errors append error)
    })
  }
}

private[ciris] final class ConfigValue21[F[_]: Apply, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21](val value: F[Either[ConfigErrors, (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21)]])
