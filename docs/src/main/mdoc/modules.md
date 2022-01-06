---
id: modules
title: Modules
---

The following sections describe the additional modules.

## Circe

The `@CIRCE_MODULE_NAME@` module provides [`ConfigDecoder`][configdecoder]s for JSON using [Circe](https://github.com/circe/circe).

```scala mdoc
import ciris.circe._
import ciris.ConfigDecoder
import io.circe.{Decoder, Json}

ConfigDecoder[String, Json]

case class SerialNumber(value: String)

object SerialNumber {
  implicit val serialNumberDecoder: Decoder[SerialNumber] =
    Decoder[String].map(apply)
}

circeConfigDecoder[SerialNumber]("SerialNumber")
```

## Circe YAML

The `@CIRCE_YAML_MODULE_NAME@` module provides [`ConfigDecoder`][configdecoder]s for YAML using [`circe-yaml`](https://github.com/circe/circe-yaml).

```scala mdoc:reset
import ciris.circe.yaml._
import ciris.ConfigDecoder
import io.circe.{Decoder, Json}

ConfigDecoder[String, Json]

case class SerialNumber(value: String)

object SerialNumber {
  implicit val serialNumberDecoder: Decoder[SerialNumber] =
    Decoder[String].map(apply)
}

circeYamlConfigDecoder[SerialNumber]("SerialNumber")
```

## Enumeratum

The `@ENUMERATUM_MODULE_NAME@` module provides [`ConfigDecoder`][configdecoder]s for [Enumeratum](https://github.com/lloydmeta/enumeratum) enumerations.

For regular `Enum`s, also mix in `CirisEnum` to derive a [`ConfigDecoder`][configdecoder] instance.

```scala mdoc
import enumeratum.{CirisEnum, Enum, EnumEntry}
import enumeratum.EnumEntry.Lowercase

sealed trait Suit extends EnumEntry with Lowercase

object Suit extends Enum[Suit] with CirisEnum[Suit] {
  case object Clubs extends Suit
  case object Diamonds extends Suit
  case object Hearts extends Suit
  case object Spades extends Suit

  val values = findValues
}

ConfigDecoder[String, Suit]
```

For `ValueEnum`s, also mix in the matching `CirisValueEnum` to derive a [`ConfigDecoder`][configdecoder] instance.

```scala mdoc
import enumeratum.values.{StringCirisEnum, StringEnum, StringEnumEntry}

sealed abstract class Color(val value: String) extends StringEnumEntry

object Color extends StringEnum[Color] with StringCirisEnum[Color] {
  case object Red extends Color("red")
  case object Green extends Color("green")
  case object Blue extends Color("blue")

  val values = findValues
}

ConfigDecoder[String, Color]
```

## Http4s

The `@HTTP4S_MODULE_NAME@` module provides [`ConfigDecoder`][configdecoder]s for [http4s](https://github.com/http4s/http4s) Uri type.

```scala mdoc
import ciris.http4s._
import org.http4s.Uri

ConfigDecoder[String, Uri]
```

## Refined

The `@REFINED_MODULE_NAME@` module provides [`ConfigDecoder`][configdecoder]s for [refined](https://github.com/fthomas/refined) refinement types.

```scala mdoc
import ciris.refined._
import cats.implicits._
import eu.timepit.refined.types.numeric.PosInt

ConfigDecoder[String, PosInt]
```

## Squants

The `@SQUANTS_MODULE_NAME@` module provides [`ConfigDecoder`][configdecoder]s for [squants](https://github.com/typelevel/squants) quantities.

```scala mdoc
import ciris.squants._
import squants.market.Money

ConfigDecoder[String, Money]
```

[configdecoder]: @API_BASE_URL@/ConfigDecoder.html
