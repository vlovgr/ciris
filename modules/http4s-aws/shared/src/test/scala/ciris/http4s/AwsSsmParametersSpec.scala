/*
 * Copyright 2017-2026 Viktor Rudebeck
 *
 * SPDX-License-Identifier: MIT
 */

package ciris.http4s

import cats.data.Chain
import cats.effect.IO
import cats.effect.Ref
import cats.syntax.all._
import ciris.http4s.aws.AwsSsmParameters
import ciris.http4s.aws.AwsSsmParameters.UnexpectedError
import com.magine.aws.Region
import io.circe.Json
import io.circe.parser.parse
import munit.CatsEffectSuite
import org.http4s.HttpApp
import org.http4s.MediaType
import org.http4s.Method
import org.http4s.Request
import org.http4s.Response
import org.http4s.Status
import org.http4s.client.Client
import org.http4s.headers.`Content-Type`
import org.http4s.syntax.literals._
import org.typelevel.ci._

final class AwsSsmParametersSpec extends CatsEffectSuite {
  type Recorded = (Request[IO], String)

  def recording(response: Response[IO]): IO[(AwsSsmParameters[IO], IO[List[Recorded]])] =
    Ref[IO].of(List.empty[Recorded]).map { recorded =>
      val app =
        HttpApp[IO] { request =>
          request.as[String].flatMap { body =>
            recorded.update(_ :+ ((request, body))).as(response)
          }
        }

      val parameters =
        AwsSsmParameters.fromSigningClient(Client.fromHttpApp(app), Region.EU_WEST_1)

      (parameters, recorded.get)
    }

  def jsonResponse(status: Status, body: Json): Response[IO] =
    Response[IO](status)
      .withEntity(body.noSpaces)
      .withContentType(`Content-Type`(new MediaType("application", "x-amz-json-1.1")))

  def parameterResponse(value: String): Response[IO] =
    jsonResponse(
      Status.Ok,
      Json.obj(
        "Parameter" -> Json.obj(
          "Name" -> Json.fromString("name"),
          "Type" -> Json.fromString("SecureString"),
          "Value" -> Json.fromString(value),
          "Version" -> Json.fromInt(1)
        )
      )
    )

  def errorResponse(status: Status, errorType: String, message: Option[String]): Response[IO] =
    jsonResponse(
      status,
      Json.fromFields(
        ("__type" -> Json.fromString(errorType)) ::
          message.map(message => "message" -> Json.fromString(message)).toList
      )
    )

  test("fromSigningClient.loaded") {
    recording(parameterResponse("value")).flatMap { case (parameters, _) =>
      parameters("name").load[IO].map(_.value).assertEquals("value")
    }
  }

  test("fromSigningClient.request") {
    recording(parameterResponse("value")).flatMap { case (parameters, recorded) =>
      parameters("name").load[IO] >> recorded.map {
        case List((request, body)) =>
          assertEquals(request.method, Method.POST)
          assertEquals(request.uri, uri"https://ssm.eu-west-1.amazonaws.com")
          assertEquals(
            request.headers.get(ci"X-Amz-Target").map(_.head.value),
            Some("AmazonSSM.GetParameter")
          )
          assertEquals(
            request.headers.get(ci"Content-Type").map(_.head.value),
            Some("application/x-amz-json-1.1")
          )
          assertEquals(
            parse(body),
            Right(Json.obj("Name" -> Json.fromString("name"), "WithDecryption" -> Json.True))
          )
        case requests =>
          fail(s"expected one request, got $requests")
      }
    }
  }

  List("ParameterNotFound", "ParameterVersionNotFound").foreach { errorType =>
    test(s"fromSigningClient.missing $errorType") {
      recording(errorResponse(Status.BadRequest, errorType, None)).flatMap { case (parameters, _) =>
        val parameter = parameters("name")

        val attempt =
          parameter
            .attempt[IO]
            .map(_.leftMap(_.messages))
            .assertEquals(Left(Chain.one("Missing parameter name from AWS SSM")))

        val option =
          parameter.option.load[IO].assertEquals(None)

        attempt >> option
      }
    }
  }

  test("fromSigningClient.unexpected") {
    recording(errorResponse(Status.BadRequest, "InvalidKeyId", Some("Invalid key"))).flatMap {
      case (parameters, _) =>
        interceptIO[UnexpectedError](parameters("name").load[IO]).map { error =>
          assertEquals(
            error,
            UnexpectedError("InvalidKeyId", Some("Invalid key"), Status.BadRequest)
          )
          assertEquals(
            error.getMessage,
            "unexpected HTTP status 400 Bad Request with error code InvalidKeyId: Invalid key"
          )
        }
    }
  }
}
