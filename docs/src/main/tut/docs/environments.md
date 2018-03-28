---
layout: docs
title: "Multiple Environments"
permalink: /docs/environments
---

# Multiple Environments
Being able to have different configuration values in different environments is one of the main reasons why we use configurations. For example, we might want to run our application in a local, testing, and production environment. To be able to work with multiple environments, we first need a representation of them in our application.

One of the most convenient ways to deal with multiple environments with Ciris, is to define them as an [enumeratum](/docs/enumeratum-module) enumeration. Since Ciris integrates with enumeratum, we get the ability to load values of that enumeration without having to write any additional code. Alternatively, we could define our own representation of multiple environments, and also define a [configuration decoder](/docs/decoders) for that representation.

```tut:silent
object environments {
  import enumeratum._

  sealed abstract class AppEnvironment extends EnumEntry

  object AppEnvironment extends Enum[AppEnvironment] {
    case object Local extends AppEnvironment
    case object Testing extends AppEnvironment
    case object Production extends AppEnvironment

    val values = findValues
  }
}

import environments._
import AppEnvironment._
```

We can now easily load values of that enumeration by just referring to the `AppEnvironment` type. Since the environments are represented as `case object`s, we can, for example, use pattern matching to use different values for different environments as necessary. Values which are the same across environments can remain as they would have before.

First, let's define the configuration we want our application to use. We're using refinement types, together with [refined](/docs/refined-module), to represent our validation logic. For more information, refer to the [encoding validation](/docs/validation) section. The [`Secret`][Secret] type is used to denote secret configuration values, to avoid accidentally including the secrets in logs. The [logging configurations](/docs/logging) section contains more information.

```tut:silent
import ciris.Secret
import eu.timepit.refined.api.Refined
import eu.timepit.refined.string.MatchesRegex
import eu.timepit.refined.types.net.UserPortNumber
import eu.timepit.refined.types.numeric.PosInt
import eu.timepit.refined.types.string.NonEmptyString
import eu.timepit.refined.W

type ApiKey = String Refined MatchesRegex[W.`"[a-zA-Z0-9]{25,40}"`.T]

final case class ApiConfig(
  key: Secret[ApiKey],
  port: UserPortNumber,
  timeoutSeconds: PosInt
)

final case class Config(
  appName: NonEmptyString,
  api: ApiConfig
)
```

As part of the configuration loading, we're loading the `AppEnvironment` we should be running in from the `APP_ENV` environment variable. As an example, we're using different default port numbers depending on the environment -- port 4000 in the local and testing environments, and port 9000 in the production environment. Note that the default port is only used if the `http.port` system property has not been set.

```tut:book
import ciris.{env, loadConfig, prop}
import ciris.enumeratum._
import ciris.refined._
import eu.timepit.refined.auto._

val config =
  loadConfig(
    env[AppEnvironment]("APP_ENV"),
    env[Secret[ApiKey]]("API_KEY").
      orElse(prop("api.key")),
    prop[Option[UserPortNumber]]("http.port")
  ) { (environment, apiKey, port) =>
    Config(
      appName = "my-api",
      api = ApiConfig(
        key = apiKey,
        timeoutSeconds = 10,
        port = port getOrElse (environment match {
          case Local | Testing => 4000
          case Production      => 9000
        })
      )
    )
  }
```

Sometimes it might be necessary to have different configuration loading take place in different environments. Ciris provides the [`withValues`][withValues] (and [`withValue`][withValue] for a single value) function, which allows you to do exactly that. The function takes a number of configuration values, and effectively wraps your [`loadConfig`][loadConfig] functions. For example, let's assume you would want a static configuration in the local and testing environments, while using the configuration loading above in the production environment.

```tut:book
import ciris.withValue

val config =
  withValue(env[AppEnvironment]("APP_ENV")) {
    case Local | Testing =>
      loadConfig {
        Config(
          appName = "my-api",
          api = ApiConfig(
            key = Secret("RacrqvWjuu4KVmnTG9b6xyZMTP7jnX"),
            timeoutSeconds = 10,
            port = 4000
          )
        )
      }

    case Production =>
      loadConfig(
        env[Secret[ApiKey]]("API_KEY").
          orElse(prop("api.key")),
        prop[Option[UserPortNumber]]("http.port")
      ) { (apiKey, port) =>
        Config(
          appName = "my-api",
          api = ApiConfig(
            key = apiKey,
            timeoutSeconds = 10,
            port = port getOrElse 4000
          )
        )
      }
  }
```

Note that when using [`withValues`][withValues], errors for the specified values (in this case, the `APP_ENV` environment variable) means we will not continue to try to load the configuration, meaning potential further errors are not included. Assuming we could load an `AppEnvironment`, any further errors will be accumulated as you would normally expect.

You might have noticed that there is some duplication of values when loading configurations across the different environments. If we would like to avoid that duplication, we can simply extract the shared parts to a function, like in the following example. Since your configuration models and most values are in code, you can refactor them just like any other code.

```tut:book
def configWithDefaults(
  apiKey: Secret[ApiKey],
  port: Option[UserPortNumber] = None
): Config = {
  Config(
    appName = "my-api",
    api = ApiConfig(
      key = apiKey,
      timeoutSeconds = 10,
      port = port getOrElse 4000
    )
  )
}

val config =
  withValue(env[AppEnvironment]("APP_ENV")) {
    case Local | Testing =>
      loadConfig {
        configWithDefaults(
          apiKey = Secret("RacrqvWjuu4KVmnTG9b6xyZMTP7jnX")
        )
      }

    case Production =>
      loadConfig(
        env[Secret[ApiKey]]("API_KEY").
          orElse(prop("api.key")),
        prop[Option[UserPortNumber]]("http.port")
      )(configWithDefaults)
  }
```

[Secret]: /api/ciris/Secret.html
[loadConfig]: /api/ciris/index.html#loadConfig[F[_],A1,A2,Z](a1:ciris.ConfigValue[F,A1],a2:ciris.ConfigValue[F,A2])(f:(A1,A2)=>Z)(implicitevidence$5:ciris.api.Functor[F]):F[Either[ciris.ConfigErrors,Z]]
[withValues]: /api/ciris/index.html#withValues[F[_],A1,A2,Z](a1:ciris.ConfigValue[F,A1],a2:ciris.ConfigValue[F,A2])(f:(A1,A2)=>F[Either[ciris.ConfigErrors,Z]])(implicitevidence$6:ciris.api.Monad[F]):F[Either[ciris.ConfigErrors,Z]]
[withValue]: /api/ciris/index.html#withValue[F[_],A1,Z](a1:ciris.ConfigValue[F,A1])(f:A1=>F[Either[ciris.ConfigErrors,Z]])(implicitevidence$3:ciris.api.Monad[F]):F[Either[ciris.ConfigErrors,Z]]
