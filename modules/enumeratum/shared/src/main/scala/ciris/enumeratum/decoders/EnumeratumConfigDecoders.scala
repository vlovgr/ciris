package ciris.enumeratum.decoders

import _root_.enumeratum._
import _root_.enumeratum.values._
import ciris.ConfigError.wrongType
import ciris.{ConfigError, ConfigDecoder, ConfigEntry}

import scala.reflect.ClassTag

trait EnumeratumConfigDecoders {
  private def fromOption[
    From: ConfigDecoder,
    To: ClassTag
  ](f: From => Option[To]): ConfigDecoder[To] =
    new ConfigDecoder[To] {
      override def decode[Key, S](entry: ConfigEntry[Key, S, String]): Either[ConfigError, To] =
        ConfigDecoder[From].decode(entry).right.flatMap { value =>
          f(value) match {
            case Some(to) => Right(to)
            case None =>
              val typeName = implicitly[ClassTag[To]].runtimeClass.getName
              Left(wrongType(entry.key, value, typeName, entry.keyType))
          }
        }
    }

  implicit def byteEnumEntryConfigDecoder[A <: ByteEnumEntry: ClassTag](
    implicit enum: ByteEnum[A]): ConfigDecoder[A] = fromOption(enum.withValueOpt)

  implicit def charEnumEntryConfigDecoder[A <: CharEnumEntry: ClassTag](
    implicit enum: CharEnum[A]): ConfigDecoder[A] = fromOption(enum.withValueOpt)

  implicit def enumEntryConfigDecoder[A <: EnumEntry: ClassTag](
    implicit enum: Enum[A]): ConfigDecoder[A] = fromOption(enum.withNameOption)

  implicit def intEnumEntryConfigDecoder[A <: IntEnumEntry: ClassTag](
    implicit enum: IntEnum[A]): ConfigDecoder[A] = fromOption(enum.withValueOpt)

  implicit def longEnumEntryConfigDecoder[A <: LongEnumEntry: ClassTag](
    implicit enum: LongEnum[A]): ConfigDecoder[A] = fromOption(enum.withValueOpt)

  implicit def shortEnumEntryConfigDecoder[A <: ShortEnumEntry: ClassTag](
    implicit enum: ShortEnum[A]): ConfigDecoder[A] = fromOption(enum.withValueOpt)

  implicit def stringEnumEntryConfigDecoder[A <: StringEnumEntry: ClassTag](
    implicit enum: StringEnum[A]): ConfigDecoder[A] = fromOption(enum.withValueOpt)
}
