---
id: quick-example
title: Quick Example
---

Following is an example showing how to:

- use `env` and `prop` to load environment variables and system properties,
- use `or` with fallback value when values are missing, accumulating errors,
- use combinators like `as`, `default`, `option`, and `secret` on values,
- use `flatMap` to load a single value, without accumulation of errors,
- use `parMapN` to combine multiple values with error accumulation,
- use the [ciris-enumeratum](modules.md#enumeratum) and [ciris-refined](modules.md#refined) integration modules,
- use `load` to return an effect for loading the configuration.

```scala mdoc:reset-object:silent
import cats.effect.{IO, IOApp}
import cats.syntax.all._
import ciris._
import ciris.refined._
import enumeratum.{CirisEnum, Enum, EnumEntry}
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.cats._
import eu.timepit.refined.collection.MinSize
import eu.timepit.refined.string.MatchesRegex
import eu.timepit.refined.types.net.UserPortNumber
import eu.timepit.refined.types.string.NonEmptyString
import scala.concurrent.duration._

sealed trait AppEnvironment extends EnumEntry

object AppEnvironment extends Enum[AppEnvironment] with CirisEnum[AppEnvironment] {
  case object Local extends AppEnvironment
  case object Testing extends AppEnvironment
  case object Production extends AppEnvironment

  val values = findValues
}

import AppEnvironment.{Local, Testing, Production}

type ApiKey = String Refined MatchesRegex["[a-zA-Z0-9]{25,40}"]

type DatabasePassword = String Refined MinSize[30]

final case class ApiConfig(
  port: Option[UserPortNumber],
  key: Secret[ApiKey],
  timeout: Option[FiniteDuration]
)

final case class DatabaseConfig(
  username: NonEmptyString,
  password: Secret[DatabasePassword]
)

final case class Config(
  name: NonEmptyString,
  api: ApiConfig,
  database: DatabaseConfig
)

def apiConfig(environment: AppEnvironment): ConfigValue[Effect, ApiConfig] =
  (
    env("API_PORT").or(prop("api.port")).as[UserPortNumber].option,
    env("API_KEY").as[ApiKey].secret,
    default(environment match {
      case Local | Testing => None
      case Production      => Some(10.seconds)
    })
  ).parMapN(ApiConfig)

val databaseConfig: ConfigValue[Effect, DatabaseConfig] =
  (
    env("DATABASE_USERNAME").as[NonEmptyString].default("username"),
    env("DATABASE_PASSWORD").as[DatabasePassword].secret
  ).parMapN(DatabaseConfig)

val config: ConfigValue[Effect, Config] =
  (
    default("my-api").as[NonEmptyString],
    env("APP_ENV").as[AppEnvironment].flatMap(apiConfig),
    databaseConfig
  ).parMapN(Config)

object Main extends IOApp.Simple {
  def run: IO[Unit] =
    config.load[IO].flatMap(IO.println)
}
```
