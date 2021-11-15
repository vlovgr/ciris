/*
 * Copyright 2017-2021 Viktor Lövgren
 *
 * SPDX-License-Identifier: MIT
 */

package ciris.circe

import cats.implicits._
import ciris.{ConfigDecoder, ConfigError}
import io.circe.{Decoder, Json}
import io.circe.{DecodingFailure, ParsingFailure}
import io.circe.yaml.parser.parse

package object yaml {
  final def circeYamlConfigDecoder[A](
    typeName: String
  )(implicit decoder: Decoder[A]): ConfigDecoder[String, A] =
    ConfigDecoder[String].mapEither { (key, value) =>
      def decodeError(json: Json, decodingFailure: DecodingFailure): ConfigError = {
        def message(valueShown: Option[String], decodingFailureMessage: Option[String]): String = {
          def trailingDecodingFailureMessage =
            decodingFailureMessage match {
              case Some(message) => s": $message"
              case None          => ""
            }

          (key, valueShown) match {
            case (Some(key), Some(value)) =>
              s"${key.description.capitalize} with json $value cannot be decoded to $typeName$trailingDecodingFailureMessage"
            case (Some(key), None) =>
              s"${key.description.capitalize} cannot be decoded to $typeName$trailingDecodingFailureMessage"
            case (None, Some(value)) =>
              s"Unable to decode json $value to $typeName$trailingDecodingFailureMessage"
            case (None, None) =>
              s"Unable to decode json to $typeName$trailingDecodingFailureMessage"
          }
        }

        ConfigError.sensitive(
          message = message(Some(json.noSpaces), Some(decodingFailure.getMessage)),
          redactedMessage = message(None, None)
        )
      }

      def parseError(parsingFailure: ParsingFailure): ConfigError = {
        def message(valueShown: Option[String], parsingFailureMessage: Option[String]): String = {
          def trailingParsingFailureMessage =
            parsingFailureMessage match {
              case Some(message) => s": $message"
              case None          => ""
            }

          (key, valueShown) match {
            case (Some(key), Some(value)) =>
              s"${key.description.capitalize} with value $value cannot be parsed as json$trailingParsingFailureMessage"
            case (Some(key), None) =>
              s"${key.description.capitalize} cannot be parsed as json$trailingParsingFailureMessage"
            case (None, Some(value)) =>
              s"Unable to parse value $value as json$trailingParsingFailureMessage"
            case (None, None) =>
              s"Unable to parse value as json$trailingParsingFailureMessage"
          }
        }

        ConfigError.sensitive(
          message = message(Some(value), Some(parsingFailure.getMessage)),
          redactedMessage = message(None, None)
        )
      }

      for {
        json <- parse(value).leftMap(parseError)
        a <- json.as[A].leftMap(decodeError(json, _))
      } yield a
    }

  implicit final val yamlConfigDecoder: ConfigDecoder[String, Json] =
    circeYamlConfigDecoder("Yaml")
}
