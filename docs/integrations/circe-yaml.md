# circe-yaml

The `@CIRCE_YAML_MODULE_NAME@` module provides [`ConfigDecoder`][configdecoder]s for YAML using [circe-yaml](https://github.com/circe/circe-yaml).

## Getting Started

To get started with [sbt](https://scala-sbt.org), add the following line to your `build.sbt` file.

```scala
libraryDependencies += "@ORGANIZATION@" %% "@CIRCE_YAML_MODULE_NAME@" % "@VERSION@"
```

## Usage

```scala mdoc
import ciris._
import ciris.circe.yaml._
import io.circe.Decoder

case class SerialNumber(value: String)

object SerialNumber {
  implicit val serialNumberDecoder: Decoder[SerialNumber] =
    Decoder[String].map(apply)

  implicit val serialNumberConfigDecoder: ConfigDecoder[String, SerialNumber] =
    circeYamlConfigDecoder("SerialNumber")
}

env("SERIAL").as[SerialNumber]
```

[configdecoder]: @API_URL@ciris/ConfigDecoder.html
