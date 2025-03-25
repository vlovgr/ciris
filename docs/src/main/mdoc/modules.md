---
id: modules
title: Modules
---

The following sections describe the additional modules.

## circe

The `@CIRCE_MODULE_NAME@` module provides [`ConfigDecoder`][configdecoder]s for JSON using [circe](https://github.com/circe/circe).

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

## circe-yaml

The `@CIRCE_YAML_MODULE_NAME@` module provides [`ConfigDecoder`][configdecoder]s for YAML using [circe-yaml](https://github.com/circe/circe-yaml).

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

## enumeratum

The `@ENUMERATUM_MODULE_NAME@` module provides [`ConfigDecoder`][configdecoder]s for [enumeratum](https://github.com/lloydmeta/enumeratum) enumerations.

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

## http4s

The `@HTTP4S_MODULE_NAME@` module provides [`ConfigDecoder`][configdecoder]s for [ip4s](https://github.com/Comcast/ip4s) and [http4s](https://github.com/http4s/http4s) types.

```scala mdoc:reset
import ciris.env
import ciris.http4s._
import com.comcast.ip4s.Host
import com.comcast.ip4s.Port
import org.http4s.headers.Origin
import org.http4s.Uri

env("HOST").as[Host]
env("PORT").as[Port]
env("Origin").as[Origin]
env("URI").as[Uri]
```

## http4s-aws

The `@HTTP4SAWS_MODULE_NAME@` module provides a way to read decrypted values from the [AWS Systems Manager (SSM) Parameter Store](https://docs.aws.amazon.com/systems-manager/latest/userguide/systems-manager-parameter-store.html) using the [http4s-aws](https://github.com/maginepro/http4s-aws) library. Following is an example of how parameter values can be retrieved. Note parameter values are wrapped in [Secret](configurations.md#secrets) by default since they are usually sensitive.

```scala mdoc:reset
import cats.effect.IO
import cats.effect.IOApp
import ciris._
import ciris.http4s.aws.AwsSsmParameters
import com.magine.aws.Region
import com.magine.http4s.aws.CredentialsProvider
import org.http4s.ember.client.EmberClientBuilder

object Main extends IOApp.Simple {
  case class Config(username: String, password: Secret[String])

  object Config {
    def fromParameters(parameters: AwsSsmParameters[IO]): IO[Config] =
      (
        env("USERNAME"),
        parameters("password")
      ).parMapN(apply).load[IO]
  }

  override def run: IO[Unit] =
    EmberClientBuilder.default[IO].build.use { client =>
      CredentialsProvider.default(client).use { provider =>
        val parameters = AwsSsmParameters(client, provider, Region.EU_WEST_1)
        Config.fromParameters(parameters).flatMap(IO.println)
      }
    }
}
```

## refined

The `@REFINED_MODULE_NAME@` module provides [`ConfigDecoder`][configdecoder]s for [refined](https://github.com/fthomas/refined) refinement types.

```scala mdoc:reset
import ciris.env
import ciris.refined._
import eu.timepit.refined.types.numeric.PosInt

env("POS_INT").as[PosInt]
```

## squants

The `@SQUANTS_MODULE_NAME@` module provides [`ConfigDecoder`][configdecoder]s for [squants](https://github.com/typelevel/squants) quantities.

```scala mdoc:reset
import ciris.env
import ciris.squants._
import squants.market.Money

env("MONEY").as[Money]
```

[configdecoder]: @API_BASE_URL@/ConfigDecoder.html
