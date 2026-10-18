# circe

The `@CIRCE_MODULE_NAME@` module provides [`ConfigDecoder`][configdecoder]s for JSON using [circe](https://github.com/circe/circe).

## Getting Started

To get started with [sbt](https://scala-sbt.org), add the following line to your `build.sbt` file.

```scala
libraryDependencies += "@ORGANIZATION@" %% "@CIRCE_MODULE_NAME@" % "@VERSION@"
```

If you are using Scala.js or Scala Native, make sure to replace `%%` with `%%%` above.

## Usage

```scala mdoc
import ciris._
import ciris.circe._
import io.circe.Decoder

case class SerialNumber(value: String)

object SerialNumber {
  implicit val serialNumberDecoder: Decoder[SerialNumber] =
    Decoder[String].map(apply)

  implicit val serialNumberConfigDecoder: ConfigDecoder[String, SerialNumber] =
    circeConfigDecoder("SerialNumber")
}

env("SERIAL").as[SerialNumber]
```

[configdecoder]: @API_URL@ciris/ConfigDecoder.html
