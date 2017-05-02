package ciris.enumeratum.readers

import _root_.enumeratum._
import _root_.enumeratum.values._
import ciris.ConfigError.WrongType
import ciris.ConfigReader

import scala.reflect.runtime.universe._

trait EnumeratumConfigReaders {
  private def fromOption[
    From: ConfigReader,
    To: WeakTypeTag
  ](f: From ⇒ Option[To]): ConfigReader[To] =
    ConfigReader.pure { (key, source) ⇒
      ConfigReader[From]
        .read(key)(source)
        .fold(
          Left.apply,
          value ⇒ {
            f(value) match {
              case Some(to) ⇒ Right(to)
              case None ⇒
                val typeName = weakTypeTag[To].tpe.typeSymbol.name.toString
                Left(WrongType(key, value, typeName, source))
            }
          }
        )
    }

  implicit def byteEnumEntryConfigReader[A <: ByteEnumEntry: WeakTypeTag](
    implicit enum: ByteEnum[A]): ConfigReader[A] = fromOption(enum.withValueOpt)

  implicit def charEnumEntryConfigReader[A <: CharEnumEntry: WeakTypeTag](
    implicit enum: CharEnum[A]): ConfigReader[A] = fromOption(enum.withValueOpt)

  implicit def enumEntryConfigReader[A <: EnumEntry: WeakTypeTag](
    implicit enum: Enum[A]): ConfigReader[A] = fromOption(enum.withNameOption)

  implicit def intEnumEntryConfigReader[A <: IntEnumEntry: WeakTypeTag](
    implicit enum: IntEnum[A]): ConfigReader[A] = fromOption(enum.withValueOpt)

  implicit def longEnumEntryConfigReader[A <: LongEnumEntry: WeakTypeTag](
    implicit enum: LongEnum[A]): ConfigReader[A] = fromOption(enum.withValueOpt)

  implicit def shortEnumEntryConfigReader[A <: ShortEnumEntry: WeakTypeTag](
    implicit enum: ShortEnum[A]): ConfigReader[A] = fromOption(enum.withValueOpt)

  implicit def stringEnumEntryConfigReader[A <: StringEnumEntry: WeakTypeTag](
    implicit enum: StringEnum[A]): ConfigReader[A] = fromOption(enum.withValueOpt)
}
