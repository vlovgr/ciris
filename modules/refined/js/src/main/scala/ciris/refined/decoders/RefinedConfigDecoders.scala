package ciris.refined.decoders

import ciris.ConfigDecoder
import eu.timepit.refined.api.{RefType, Validate}

import scala.reflect.ClassTag

trait RefinedConfigDecoders {
  implicit def refTypeConfigDecoder[F[_, _], T, P](
    implicit decoder: ConfigDecoder[T],
    refType: RefType[F],
    validate: Validate[T, P],
    classTag: ClassTag[F[T, P]]
  ): ConfigDecoder[F[T, P]] = {
    val typeName = classTag.runtimeClass.getSimpleName
    decoder.mapEither(typeName)(refType.refine[P].apply(_))
  }
}
