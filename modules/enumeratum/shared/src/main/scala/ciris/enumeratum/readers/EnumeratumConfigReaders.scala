package ciris.enumeratum.readers

import _root_.enumeratum._
import _root_.enumeratum.values._
import ciris.ConfigError.WrongType
import ciris.{ConfigError, ConfigReader, ConfigSourceEntry}

import scala.reflect.ClassTag

trait EnumeratumConfigReaders {
  private def fromOption[
    From: ConfigReader,
    To: ClassTag
  ](f: From => Option[To]): ConfigReader[To] =
    new ConfigReader[To] {
      override def read[Key](entry: ConfigSourceEntry[Key]): Either[ConfigError, To] =
        ConfigReader[From].read(entry).right.flatMap { value =>
          f(value) match {
            case Some(to) => Right(to)
            case None =>
              val typeName = implicitly[ClassTag[To]].runtimeClass.getName
              Left(WrongType(entry.key, value, typeName, entry.keyType))
          }
        }
    }

  implicit def byteEnumEntryConfigReader[A <: ByteEnumEntry: ClassTag](
    implicit enum: ByteEnum[A]): ConfigReader[A] = fromOption(enum.withValueOpt)

  implicit def charEnumEntryConfigReader[A <: CharEnumEntry: ClassTag](
    implicit enum: CharEnum[A]): ConfigReader[A] = fromOption(enum.withValueOpt)

  implicit def enumEntryConfigReader[A <: EnumEntry: ClassTag](
    implicit enum: Enum[A]): ConfigReader[A] = fromOption(enum.withNameOption)

  implicit def intEnumEntryConfigReader[A <: IntEnumEntry: ClassTag](
    implicit enum: IntEnum[A]): ConfigReader[A] = fromOption(enum.withValueOpt)

  implicit def longEnumEntryConfigReader[A <: LongEnumEntry: ClassTag](
    implicit enum: LongEnum[A]): ConfigReader[A] = fromOption(enum.withValueOpt)

  implicit def shortEnumEntryConfigReader[A <: ShortEnumEntry: ClassTag](
    implicit enum: ShortEnum[A]): ConfigReader[A] = fromOption(enum.withValueOpt)

  implicit def stringEnumEntryConfigReader[A <: StringEnumEntry: ClassTag](
    implicit enum: StringEnum[A]): ConfigReader[A] = fromOption(enum.withValueOpt)
}
