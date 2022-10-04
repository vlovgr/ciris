---
id: modules
title: Modules
---

The following sections describe the additional modules.

## Circe

The `@CIRCE_MODULE_NAME@` module provides [`ConfigDecoder`][configdecoder]s for JSON using [Circe](https://github.com/circe/circe).

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

## Circe YAML

The `@CIRCE_YAML_MODULE_NAME@` module provides [`ConfigDecoder`][configdecoder]s for YAML using [`circe-yaml`](https://github.com/circe/circe-yaml).

```scala mdoc:reset
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

## Enumeratum

The `@ENUMERATUM_MODULE_NAME@` module provides [`ConfigDecoder`][configdecoder]s for [Enumeratum](https://github.com/lloydmeta/enumeratum) enumerations.

For regular `Enum`s, also mix in `CirisEnum` to derive a [`ConfigDecoder`][configdecoder] instance.

```scala mdoc:reset
import ciris.env
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

env("SUIT").as[Suit]
```

For `ValueEnum`s, also mix in the matching `CirisValueEnum` to derive a [`ConfigDecoder`][configdecoder] instance.

```scala mdoc:reset
import ciris.env
import enumeratum.values.{StringCirisEnum, StringEnum, StringEnumEntry}

sealed abstract class Color(val value: String) extends StringEnumEntry

object Color extends StringEnum[Color] with StringCirisEnum[Color] {
  case object Red extends Color("red")
  case object Green extends Color("green")
  case object Blue extends Color("blue")

  val values = findValues
}

env("COLOR").as[Color]
```

## Http4s

The `@HTTP4S_MODULE_NAME@` module provides [`ConfigDecoder`][configdecoder]s for the [http4s](https://github.com/http4s/http4s) `Uri` type.

```scala mdoc:reset
import ciris.env
import ciris.http4s._
import org.http4s.Uri

env("URI").as[Uri]
```

## Refined

The `@REFINED_MODULE_NAME@` module provides [`ConfigDecoder`][configdecoder]s for [refined](https://github.com/fthomas/refined) refinement types.

```scala mdoc:reset
import ciris.env
import ciris.refined._
import eu.timepit.refined.types.numeric.PosInt

env("POS_INT").as[PosInt]
```

## Squants

The `@SQUANTS_MODULE_NAME@` module provides [`ConfigDecoder`][configdecoder]s for [squants](https://github.com/typelevel/squants) quantities.

```scala mdoc:reset
import ciris.env
import ciris.squants._
import squants.market.Money

env("MONEY").as[Money]
```

[configdecoder]: @API_BASE_URL@/ConfigDecoder.html
