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

```scala mdoc:silent
import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._
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
import eu.timepit.refined.W
import scala.concurrent.duration._

sealed trait AppEnvironment extends EnumEntry

object AppEnvironment extends Enum[AppEnvironment] with CirisEnum[AppEnvironment] {
  case object Local extends AppEnvironment
  case object Testing extends AppEnvironment
  case object Production extends AppEnvironment

  val values = findValues
}

import AppEnvironment.{Local, Testing, Production}

type ApiKey = String Refined MatchesRegex[W.`"[a-zA-Z0-9]{25,40}"`.T]

type DatabasePassword = String Refined MinSize[W.`30`.T]

final case class ApiConfig(
  port: UserPortNumber,
  key: Secret[ApiKey],
  timeout: Option[FiniteDuration]
)

final case class DatabaseConfig(
  username: NonEmptyString,
  password: Secret[DatabasePassword]
)

final case class Config(
  appName: NonEmptyString,
  environment: AppEnvironment,
  api: ApiConfig,
  database: DatabaseConfig
)

def apiConfig(environment: AppEnvironment): ConfigValue[ApiConfig] =
  (
    env("API_PORT").or(prop("api.port")).as[UserPortNumber].option,
    env("API_KEY").as[ApiKey].secret
  ).parMapN { (port, key) =>
    ApiConfig(
      port = port getOrElse 9000,
      key = key,
      timeout = environment match {
        case Local | Testing => None
        case Production      => Some(10.seconds)
      }
    )
  }

val databaseConfig: ConfigValue[DatabaseConfig] =
  (
    env("DATABASE_USERNAME").as[NonEmptyString].default("username"),
    env("DATABASE_PASSWORD").as[DatabasePassword].secret
  ).parMapN(DatabaseConfig)

val config: ConfigValue[Config] =
  env("APP_ENV").as[AppEnvironment].flatMap { environment =>
    (
      apiConfig(environment),
      databaseConfig
    ).parMapN { (api, database) =>
      Config(
        appName = "my-api",
        environment = environment,
        api = api,
        database = database
      )
    }
  }

object Main extends IOApp {
  def run(args: List[String]): IO[ExitCode] =
    config.load[IO].as(ExitCode.Success)
}
```
