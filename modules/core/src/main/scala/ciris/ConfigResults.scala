// format: off

/**
  * Generated using sbt source generators.
  * You should not modify this file directly.
  */

package ciris

import ciris.api._
import ciris.api.syntax._

private[ciris] final class ConfigResult2[F[_]: Apply, A1, A2](val result: F[Either[ConfigErrors, (A1, A2)]]) {
  def append[A3](next: ConfigResult[F, A3]): ConfigResult3[F, A1, A2, A3] =
    new ConfigResult3((result product next.result).map {
      case (Right((a1, a2)), Right(a3)) => Right((a1, a2, a3))
      case (Left(errors1), Right(_)) => Left(errors1)
      case (Right(_), Left(errors2)) => Left(errors2)
      case (Left(errors1), Left(errors2)) => Left(errors1 combine errors2)
    })
  }

private[ciris] final class ConfigResult3[F[_]: Apply, A1, A2, A3](val result: F[Either[ConfigErrors, (A1, A2, A3)]]) {
  def append[A4](next: ConfigResult[F, A4]): ConfigResult4[F, A1, A2, A3, A4] =
    new ConfigResult4((result product next.result).map {
      case (Right((a1, a2, a3)), Right(a4)) => Right((a1, a2, a3, a4))
      case (Left(errors1), Right(_)) => Left(errors1)
      case (Right(_), Left(errors2)) => Left(errors2)
      case (Left(errors1), Left(errors2)) => Left(errors1 combine errors2)
    })
  }

private[ciris] final class ConfigResult4[F[_]: Apply, A1, A2, A3, A4](val result: F[Either[ConfigErrors, (A1, A2, A3, A4)]]) {
  def append[A5](next: ConfigResult[F, A5]): ConfigResult5[F, A1, A2, A3, A4, A5] =
    new ConfigResult5((result product next.result).map {
      case (Right((a1, a2, a3, a4)), Right(a5)) => Right((a1, a2, a3, a4, a5))
      case (Left(errors1), Right(_)) => Left(errors1)
      case (Right(_), Left(errors2)) => Left(errors2)
      case (Left(errors1), Left(errors2)) => Left(errors1 combine errors2)
    })
  }

private[ciris] final class ConfigResult5[F[_]: Apply, A1, A2, A3, A4, A5](val result: F[Either[ConfigErrors, (A1, A2, A3, A4, A5)]]) {
  def append[A6](next: ConfigResult[F, A6]): ConfigResult6[F, A1, A2, A3, A4, A5, A6] =
    new ConfigResult6((result product next.result).map {
      case (Right((a1, a2, a3, a4, a5)), Right(a6)) => Right((a1, a2, a3, a4, a5, a6))
      case (Left(errors1), Right(_)) => Left(errors1)
      case (Right(_), Left(errors2)) => Left(errors2)
      case (Left(errors1), Left(errors2)) => Left(errors1 combine errors2)
    })
  }

private[ciris] final class ConfigResult6[F[_]: Apply, A1, A2, A3, A4, A5, A6](val result: F[Either[ConfigErrors, (A1, A2, A3, A4, A5, A6)]]) {
  def append[A7](next: ConfigResult[F, A7]): ConfigResult7[F, A1, A2, A3, A4, A5, A6, A7] =
    new ConfigResult7((result product next.result).map {
      case (Right((a1, a2, a3, a4, a5, a6)), Right(a7)) => Right((a1, a2, a3, a4, a5, a6, a7))
      case (Left(errors1), Right(_)) => Left(errors1)
      case (Right(_), Left(errors2)) => Left(errors2)
      case (Left(errors1), Left(errors2)) => Left(errors1 combine errors2)
    })
  }

private[ciris] final class ConfigResult7[F[_]: Apply, A1, A2, A3, A4, A5, A6, A7](val result: F[Either[ConfigErrors, (A1, A2, A3, A4, A5, A6, A7)]]) {
  def append[A8](next: ConfigResult[F, A8]): ConfigResult8[F, A1, A2, A3, A4, A5, A6, A7, A8] =
    new ConfigResult8((result product next.result).map {
      case (Right((a1, a2, a3, a4, a5, a6, a7)), Right(a8)) => Right((a1, a2, a3, a4, a5, a6, a7, a8))
      case (Left(errors1), Right(_)) => Left(errors1)
      case (Right(_), Left(errors2)) => Left(errors2)
      case (Left(errors1), Left(errors2)) => Left(errors1 combine errors2)
    })
  }

private[ciris] final class ConfigResult8[F[_]: Apply, A1, A2, A3, A4, A5, A6, A7, A8](val result: F[Either[ConfigErrors, (A1, A2, A3, A4, A5, A6, A7, A8)]]) {
  def append[A9](next: ConfigResult[F, A9]): ConfigResult9[F, A1, A2, A3, A4, A5, A6, A7, A8, A9] =
    new ConfigResult9((result product next.result).map {
      case (Right((a1, a2, a3, a4, a5, a6, a7, a8)), Right(a9)) => Right((a1, a2, a3, a4, a5, a6, a7, a8, a9))
      case (Left(errors1), Right(_)) => Left(errors1)
      case (Right(_), Left(errors2)) => Left(errors2)
      case (Left(errors1), Left(errors2)) => Left(errors1 combine errors2)
    })
  }

private[ciris] final class ConfigResult9[F[_]: Apply, A1, A2, A3, A4, A5, A6, A7, A8, A9](val result: F[Either[ConfigErrors, (A1, A2, A3, A4, A5, A6, A7, A8, A9)]]) {
  def append[A10](next: ConfigResult[F, A10]): ConfigResult10[F, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10] =
    new ConfigResult10((result product next.result).map {
      case (Right((a1, a2, a3, a4, a5, a6, a7, a8, a9)), Right(a10)) => Right((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10))
      case (Left(errors1), Right(_)) => Left(errors1)
      case (Right(_), Left(errors2)) => Left(errors2)
      case (Left(errors1), Left(errors2)) => Left(errors1 combine errors2)
    })
  }

private[ciris] final class ConfigResult10[F[_]: Apply, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10](val result: F[Either[ConfigErrors, (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10)]]) {
  def append[A11](next: ConfigResult[F, A11]): ConfigResult11[F, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11] =
    new ConfigResult11((result product next.result).map {
      case (Right((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10)), Right(a11)) => Right((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11))
      case (Left(errors1), Right(_)) => Left(errors1)
      case (Right(_), Left(errors2)) => Left(errors2)
      case (Left(errors1), Left(errors2)) => Left(errors1 combine errors2)
    })
  }

private[ciris] final class ConfigResult11[F[_]: Apply, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11](val result: F[Either[ConfigErrors, (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11)]]) {
  def append[A12](next: ConfigResult[F, A12]): ConfigResult12[F, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12] =
    new ConfigResult12((result product next.result).map {
      case (Right((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11)), Right(a12)) => Right((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12))
      case (Left(errors1), Right(_)) => Left(errors1)
      case (Right(_), Left(errors2)) => Left(errors2)
      case (Left(errors1), Left(errors2)) => Left(errors1 combine errors2)
    })
  }

private[ciris] final class ConfigResult12[F[_]: Apply, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12](val result: F[Either[ConfigErrors, (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12)]]) {
  def append[A13](next: ConfigResult[F, A13]): ConfigResult13[F, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13] =
    new ConfigResult13((result product next.result).map {
      case (Right((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12)), Right(a13)) => Right((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13))
      case (Left(errors1), Right(_)) => Left(errors1)
      case (Right(_), Left(errors2)) => Left(errors2)
      case (Left(errors1), Left(errors2)) => Left(errors1 combine errors2)
    })
  }

private[ciris] final class ConfigResult13[F[_]: Apply, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13](val result: F[Either[ConfigErrors, (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13)]]) {
  def append[A14](next: ConfigResult[F, A14]): ConfigResult14[F, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14] =
    new ConfigResult14((result product next.result).map {
      case (Right((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13)), Right(a14)) => Right((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14))
      case (Left(errors1), Right(_)) => Left(errors1)
      case (Right(_), Left(errors2)) => Left(errors2)
      case (Left(errors1), Left(errors2)) => Left(errors1 combine errors2)
    })
  }

private[ciris] final class ConfigResult14[F[_]: Apply, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14](val result: F[Either[ConfigErrors, (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14)]]) {
  def append[A15](next: ConfigResult[F, A15]): ConfigResult15[F, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15] =
    new ConfigResult15((result product next.result).map {
      case (Right((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14)), Right(a15)) => Right((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15))
      case (Left(errors1), Right(_)) => Left(errors1)
      case (Right(_), Left(errors2)) => Left(errors2)
      case (Left(errors1), Left(errors2)) => Left(errors1 combine errors2)
    })
  }

private[ciris] final class ConfigResult15[F[_]: Apply, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15](val result: F[Either[ConfigErrors, (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15)]]) {
  def append[A16](next: ConfigResult[F, A16]): ConfigResult16[F, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16] =
    new ConfigResult16((result product next.result).map {
      case (Right((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15)), Right(a16)) => Right((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16))
      case (Left(errors1), Right(_)) => Left(errors1)
      case (Right(_), Left(errors2)) => Left(errors2)
      case (Left(errors1), Left(errors2)) => Left(errors1 combine errors2)
    })
  }

private[ciris] final class ConfigResult16[F[_]: Apply, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16](val result: F[Either[ConfigErrors, (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16)]]) {
  def append[A17](next: ConfigResult[F, A17]): ConfigResult17[F, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17] =
    new ConfigResult17((result product next.result).map {
      case (Right((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16)), Right(a17)) => Right((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17))
      case (Left(errors1), Right(_)) => Left(errors1)
      case (Right(_), Left(errors2)) => Left(errors2)
      case (Left(errors1), Left(errors2)) => Left(errors1 combine errors2)
    })
  }

private[ciris] final class ConfigResult17[F[_]: Apply, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17](val result: F[Either[ConfigErrors, (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17)]]) {
  def append[A18](next: ConfigResult[F, A18]): ConfigResult18[F, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18] =
    new ConfigResult18((result product next.result).map {
      case (Right((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17)), Right(a18)) => Right((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18))
      case (Left(errors1), Right(_)) => Left(errors1)
      case (Right(_), Left(errors2)) => Left(errors2)
      case (Left(errors1), Left(errors2)) => Left(errors1 combine errors2)
    })
  }

private[ciris] final class ConfigResult18[F[_]: Apply, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18](val result: F[Either[ConfigErrors, (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18)]]) {
  def append[A19](next: ConfigResult[F, A19]): ConfigResult19[F, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19] =
    new ConfigResult19((result product next.result).map {
      case (Right((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18)), Right(a19)) => Right((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19))
      case (Left(errors1), Right(_)) => Left(errors1)
      case (Right(_), Left(errors2)) => Left(errors2)
      case (Left(errors1), Left(errors2)) => Left(errors1 combine errors2)
    })
  }

private[ciris] final class ConfigResult19[F[_]: Apply, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19](val result: F[Either[ConfigErrors, (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19)]]) {
  def append[A20](next: ConfigResult[F, A20]): ConfigResult20[F, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20] =
    new ConfigResult20((result product next.result).map {
      case (Right((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19)), Right(a20)) => Right((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20))
      case (Left(errors1), Right(_)) => Left(errors1)
      case (Right(_), Left(errors2)) => Left(errors2)
      case (Left(errors1), Left(errors2)) => Left(errors1 combine errors2)
    })
  }

private[ciris] final class ConfigResult20[F[_]: Apply, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20](val result: F[Either[ConfigErrors, (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20)]]) {
  def append[A21](next: ConfigResult[F, A21]): ConfigResult21[F, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21] =
    new ConfigResult21((result product next.result).map {
      case (Right((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20)), Right(a21)) => Right((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21))
      case (Left(errors1), Right(_)) => Left(errors1)
      case (Right(_), Left(errors2)) => Left(errors2)
      case (Left(errors1), Left(errors2)) => Left(errors1 combine errors2)
    })
  }

private[ciris] final class ConfigResult21[F[_]: Apply, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21](val result: F[Either[ConfigErrors, (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21)]])
