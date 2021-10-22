/*
 * Copyright 2017-2021 Viktor Lövgren
 *
 * SPDX-License-Identifier: MIT
 */

package ciris.circe

import ciris.ConfigDecoder
import io.circe.Json
import io.circe.yaml.parser.parse
import io.circe.Decoder

package object yaml {

  final def circeYamlConfigDecoder[A](
    typeName: String
  )(implicit decoder: Decoder[A]): ConfigDecoder[String, A] = circeConfigDecoder(typeName, parse)

  implicit final val yamlConfigDecoder: ConfigDecoder[String, Json] =
    circeConfigDecoder("Yaml")
}
