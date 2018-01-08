---
layout: docs
title: "Multiple Environments"
position: 5
permalink: /docs/environments
---

# Multiple Environments
One of the most common use cases for configurations is having different values for different environments. There are several ways to deal with environments using Ciris: one way being to define an enumeration with [enumeratum][enumeratum] and use the `ciris-enumeratum` module to be able to load values of that enumeration. You can then switch configuration by just writing conditional statements in plain code.

Let's start off by defining an enumeration for our environments: local, testing, and production.

```tut:book
import enumeratum._

object configuration {
  sealed abstract class AppEnvironment extends EnumEntry
  object AppEnvironment extends Enum[AppEnvironment] {
    case object Local extends AppEnvironment
    case object Testing extends AppEnvironment
    case object Production extends AppEnvironment

    val values = findValues
  }
}
```

We'll then reuse the same configuration as seen in the [Encoding Validation](/docs/validation) section.

```tut:book
import eu.timepit.refined.auto._
import eu.timepit.refined.types.net.PortNumber
import eu.timepit.refined.types.string.NonEmptyString

import scala.concurrent.duration._

final case class Config(
  apiKey: NonEmptyString,
  timeout: Duration,
  port: PortNumber
)
```

Then, let's read the API key and port from the environment, along with which environment the application is running in. In this case, we're using a default configuration, overriding the timeout value in the testing and production environments. If no environment was set, we will use the default configuration.

```tut:book
import configuration._
import ciris.enumeratum._
import ciris.refined._
import ciris._

val config =
  loadConfig(
    env[NonEmptyString]("API_KEY"),
    prop[Option[PortNumber]]("http.port"),
    env[Option[AppEnvironment]]("APP_ENV")
  ) { (apiKey, port, appEnvironment) =>
    val default =
      Config(
        apiKey = apiKey,
        timeout = 10 seconds,
        port = port getOrElse 4000
      )

    appEnvironment match {
      case Some(AppEnvironment.Local) | None => default
      case _ => default.copy(timeout = 5 seconds)
    }
  }
```

What about reading different configuration values depending on the environment? For example, you could use defaults for everything in a local environment, while reading configuration values, like the API key and port, in the other environments. For that purpose, there is a `withValues` (and `withValue`) method you can use. It works exactly like `loadConfig` except it wraps your `loadConfig` statements, only executing them if all `withValues` values could be read successfully. If it helps, think of `loadConfig` as `map` and `withValues` as `flatMap` (which is also how they are defined internally).

```tut:book
withValue(env[Option[AppEnvironment]]("APP_ENV")) {
  case Some(AppEnvironment.Local) | None =>
    loadConfig {
      Config(
        apiKey = "changeme",
        timeout = 10 seconds,
        port = 4000
      )
    }
  case _ =>
    loadConfig(
      env[NonEmptyString]("API_KEY"),
      prop[PortNumber]("http.port")
    ) { (apiKey, port) =>
      Config(
        apiKey = apiKey,
        timeout = 5 seconds,
        port = port
      )
    }
}
```

[enumeratum]: https://github.com/lloydmeta/enumeratum
