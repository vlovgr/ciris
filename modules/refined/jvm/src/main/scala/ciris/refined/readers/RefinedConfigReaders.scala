package ciris.refined.readers

import ciris.ConfigReader
import eu.timepit.refined.api.{RefType, Validate}

import scala.reflect.runtime.universe.WeakTypeTag

trait RefinedConfigReaders {
  implicit def refTypeConfigReader[F[_, _], T, P](
    implicit reader: ConfigReader[T],
    refType: RefType[F],
    validate: Validate[T, P],
    typeTag: WeakTypeTag[F[T, P]]
  ): ConfigReader[F[T, P]] = {
    val typeName = typeTag.tpe.toString
    reader.mapEither(typeName)(refType.refine[P].apply(_))
  }
}
