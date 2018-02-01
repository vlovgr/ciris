package ciris.refined.decoders

import ciris.ConfigDecoder
import eu.timepit.refined.api.{RefType, Validate}

import scala.reflect.ClassTag

private[ciris] trait RefinedConfigDecoders {
  implicit def refTypeConfigDecoder[F[_, _], A, T, P](
    implicit decoder: ConfigDecoder[A, T],
    refType: RefType[F],
    validate: Validate[T, P],
    classTag: ClassTag[F[T, P]]
  ): ConfigDecoder[A, F[T, P]] = {
    val typeName = classTag.runtimeClass.getSimpleName
    decoder.mapEither(typeName)(refType.refine[P].apply(_))
  }
}
