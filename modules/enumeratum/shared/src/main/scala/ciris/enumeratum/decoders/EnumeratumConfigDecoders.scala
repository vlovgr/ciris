package ciris.enumeratum.decoders

import _root_.enumeratum._
import _root_.enumeratum.values._
import ciris.ConfigError.wrongType
import ciris.{ConfigError, ConfigDecoder, ConfigEntry}

import scala.reflect.ClassTag

trait EnumeratumConfigDecoders {
  private def fromOption[From, To: ClassTag](f: From => Option[To])(
    implicit decoder: ConfigDecoder[String, From]
  ): ConfigDecoder[String, To] =
    new ConfigDecoder[String, To] {
      override def decode[K, S](entry: ConfigEntry[K, S, String]): Either[ConfigError, To] =
        decoder.decode(entry).right.flatMap { value =>
          f(value) match {
            case Some(to) => Right(to)
            case None =>
              val typeName = implicitly[ClassTag[To]].runtimeClass.getName
              Left(wrongType(entry.key, entry.keyType, entry.sourceValue, value, typeName))
          }
        }
    }

  implicit def byteEnumEntryConfigDecoder[A <: ByteEnumEntry: ClassTag](
    implicit enum: ByteEnum[A]): ConfigDecoder[String, A] = fromOption(enum.withValueOpt)

  implicit def charEnumEntryConfigDecoder[A <: CharEnumEntry: ClassTag](
    implicit enum: CharEnum[A]): ConfigDecoder[String, A] = fromOption(enum.withValueOpt)

  implicit def enumEntryConfigDecoder[A <: EnumEntry: ClassTag](
    implicit enum: Enum[A]): ConfigDecoder[String, A] = fromOption(enum.withNameOption)

  implicit def intEnumEntryConfigDecoder[A <: IntEnumEntry: ClassTag](
    implicit enum: IntEnum[A]): ConfigDecoder[String, A] = fromOption(enum.withValueOpt)

  implicit def longEnumEntryConfigDecoder[A <: LongEnumEntry: ClassTag](
    implicit enum: LongEnum[A]): ConfigDecoder[String, A] = fromOption(enum.withValueOpt)

  implicit def shortEnumEntryConfigDecoder[A <: ShortEnumEntry: ClassTag](
    implicit enum: ShortEnum[A]): ConfigDecoder[String, A] = fromOption(enum.withValueOpt)

  implicit def stringEnumEntryConfigDecoder[A <: StringEnumEntry: ClassTag](
    implicit enum: StringEnum[A]): ConfigDecoder[String, A] = fromOption(enum.withValueOpt)
}
