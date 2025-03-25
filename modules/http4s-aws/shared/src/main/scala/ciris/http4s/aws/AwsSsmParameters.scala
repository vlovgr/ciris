/*
 * Copyright 2017-2025 Viktor Rudebeck
 *
 * SPDX-License-Identifier: MIT
 */

package ciris.http4s.aws

import cats.effect.Concurrent
import cats.effect.Temporal
import cats.effect.syntax.all._
import cats.syntax.all._
import ciris.ConfigKey
import ciris.ConfigValue
import ciris.Secret
import com.magine.aws.Region
import com.magine.http4s.aws.AwsServiceName.SystemsManager
import com.magine.http4s.aws.AwsSigningClient
import com.magine.http4s.aws.CredentialsProvider
import fs2.hashing.Hashing
import io.circe.Decoder
import io.circe.Encoder
import org.http4s.EntityDecoder
import org.http4s.EntityEncoder
import org.http4s.Header
import org.http4s.Headers
import org.http4s.Method
import org.http4s.Request
import org.http4s.Status
import org.http4s.Status.Successful
import org.http4s.Uri
import org.http4s.circe.CirceEntityDecoder
import org.http4s.circe.CirceEntityEncoder
import org.http4s.client.Client
import org.typelevel.ci._
import scala.util.control.NoStackTrace

/**
  * Capability to retrieve decrypted values from the AWS
  * Systems Manager (SSM) Parameter Store.
  */
trait AwsSsmParameters[F[_]] {

  /**
    * Returns the decrypted value of the parameter with the specified name.
    */
  def apply(name: String): ConfigValue[F, Secret[String]]
}

object AwsSsmParameters {

  /**
    * Alias for [[AwsSsmParameters.fromClient]].
    */
  def apply[F[_]: Temporal: Hashing](
    client: Client[F],
    provider: CredentialsProvider[F],
    region: Region
  ): AwsSsmParameters[F] =
    fromClient(client, provider, region)

  /**
    * Returns a new [[AwsSsmParameters]] which requests
    * parameter values using the specified `Client`.
    *
    * Requests will be signed using credentials from the
    * provided `CredentialsProvider` and the target `Region`.
    */
  def fromClient[F[_]: Temporal: Hashing](
    client: Client[F],
    provider: CredentialsProvider[F],
    region: Region
  ): AwsSsmParameters[F] =
    fromSigningClient(AwsSigningClient(provider, region, SystemsManager)(client), region)

  /**
    * Returns a new [[AwsSsmParameters]] which requests
    * parameter values using the specified `Client`.
    *
    * The provided `Client` is responsible for signing
    * requests using credentials and the target `Region`.
    */
  def fromSigningClient[F[_]: Concurrent](
    client: Client[F],
    region: Region
  ): AwsSsmParameters[F] =
    new AwsSsmParameters[F] {
      override def apply(name: String): ConfigValue[F, Secret[String]] =
        ConfigValue.eval {
          val key =
            ConfigKey(s"parameter $name from AWS SSM")

          val request =
            Uri.fromString(s"https://ssm.$region.amazonaws.com").liftTo[F].map { uri =>
              Request[F](Method.POST, uri)
                .withEntity(GetRequest(name))
                .withHeaders(GetRequest.headers)
            }

          request.toResource.flatMap(client.run).use {
            case Successful(response) =>
              GetResponse.entityDecoder
                .decode(response, strict = false)
                .leftWiden[Throwable]
                .rethrowT
                .map(_.value)
                .map(ConfigValue.loaded(key, _).secret.covary[F])
            case response =>
              GetErrorResponse.entityDecoder
                .decode(response, strict = false)
                .leftWiden[Throwable]
                .rethrowT
                .flatMap {
                  case response if response.notFound =>
                    Concurrent[F].pure(ConfigValue.missing(key))
                  case unexpected =>
                    Concurrent[F].raiseError(
                      UnexpectedError(
                        unexpected.code.value,
                        unexpected.message,
                        response.status
                      )
                    )
                }
          }
        }
    }

  final case class UnexpectedError(
    errorCode: String,
    message: Option[String],
    status: Status
  ) extends RuntimeException
      with NoStackTrace {
    override def getMessage: String = {
      val trailing = message.foldMap(message => s": $message")
      s"unexpected HTTP status $status with error code $errorCode$trailing"
    }
  }

  private final case class GetRequest(name: String)
  private object GetRequest {
    implicit val encoder: Encoder[GetRequest] =
      Encoder.forProduct2("Name", "WithDecryption")(request => (request.name, true))

    implicit def entityEncoder[F[_]]: EntityEncoder[F, GetRequest] =
      CirceEntityEncoder.circeEntityEncoder

    val headers: Headers =
      Headers(
        Header.Raw(ci"X-Amz-Target", "AmazonSSM.GetParameter"),
        Header.Raw(ci"Content-Type", "application/x-amz-json-1.1")
      )
  }

  private final case class GetResponse(value: String)
  private object GetResponse {
    implicit val decoder: Decoder[GetResponse] =
      Decoder.instance(_.downField("Parameter").downField("Value").as[String].map(apply))

    implicit def entityDecoder[F[_]: Concurrent]: EntityDecoder[F, GetResponse] =
      CirceEntityDecoder.circeEntityDecoder
  }

  private sealed abstract class GetErrorResponseCode(val value: String)
  private object GetErrorResponseCode {
    case object InternalServerError extends GetErrorResponseCode("InternalServerError")
    case object InvalidKeyId extends GetErrorResponseCode("InvalidKeyId")
    case object ParameterNotFound extends GetErrorResponseCode("ParameterNotFound")
    case object ParameterVersionNotFound extends GetErrorResponseCode("ParameterVersionNotFound")
    final case class Other(override val value: String) extends GetErrorResponseCode(value)

    implicit val decoder: Decoder[GetErrorResponseCode] =
      Decoder[String].map {
        case InternalServerError.value      => InternalServerError
        case InvalidKeyId.value             => InvalidKeyId
        case ParameterNotFound.value        => ParameterNotFound
        case ParameterVersionNotFound.value => ParameterVersionNotFound
        case code                           => Other(code)
      }
  }

  private final case class GetErrorResponse(
    code: GetErrorResponseCode,
    message: Option[String]
  ) {
    def notFound: Boolean =
      code match {
        case GetErrorResponseCode.InternalServerError      => false
        case GetErrorResponseCode.InvalidKeyId             => false
        case GetErrorResponseCode.Other(_)                 => false
        case GetErrorResponseCode.ParameterNotFound        => true
        case GetErrorResponseCode.ParameterVersionNotFound => true
      }
  }
  private object GetErrorResponse {
    implicit val decoder: Decoder[GetErrorResponse] =
      Decoder.forProduct2("__type", "message")(apply)

    implicit def entityDecoder[F[_]: Concurrent]: EntityDecoder[F, GetErrorResponse] =
      CirceEntityDecoder.circeEntityDecoder
  }
}
