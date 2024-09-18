/*
 * Copyright 2017-2024 Viktor Rudebeck
 *
 * SPDX-License-Identifier: MIT
 */

package ciris.circe

import cats.syntax.all._
import ciris.{ConfigDecoder, ConfigError}
import ciris.ConfigCodec
import io.circe.{Decoder, Json}
import io.circe.{DecodingFailure, ParsingFailure}
import io.circe.Encoder
import io.circe.yaml.parser.parse

package object yaml {

  @deprecated("Use ConfigCodec and circeYamlConfigCodec instead", "3.7.0")
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

  @deprecated("Use ConfigCodec instead", "3.7.0")
  implicit final val yamlConfigDecoder: ConfigDecoder[String, Json] =
    circeYamlConfigDecoder("Yaml")

  final def circeYamlConfigCodec[A](
    typeName: String
  )(implicit decoder: Decoder[A], encoder: Encoder[A]): ConfigCodec[String, A] =
    ConfigCodec[String].imapEither { (key, value) =>
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
    }(encoder(_).toString)

  implicit final val yamlConfigCodec: ConfigCodec[String, Json] =
    circeYamlConfigCodec("Yaml")
}
