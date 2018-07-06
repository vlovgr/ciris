---
layout: docs
title: "Logging Configurations"
permalink: /docs/logging
---

# Logging Configurations
Configuration logging can quickly help you determine which values are being used by the application, and can aid with debugging whenever things go wrong. Since configurations often contain secret values -- avoiding having secrets in code being one of the main use cases for configurations -- we would like to avoid having these secrets included in any logs. Ciris provides the [`Secret`][Secret] wrapper type for this purpose. By wrapping your secret configuration values in [`Secret`][Secret], we can avoid  accidentally including them in logs.

For example, let's take a look at the following configuration, where the `ApiKey` is secret. We're using refinement types to encode validation in the types of our values, refer to the [encoding validation](/docs/validation) section for more information. Note that the `ApiKey` in the example below would normally be loaded from, for example, a vault service -- unless the value itself is not considered a secret, for example, if it was used for testing purposes.

```tut:silent
import ciris.Secret
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
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

val config =
  Config(
    appName = "my-api",
    api = ApiConfig(
      key = Secret("RacrqvWjuu4KVmnTG9b6xyZMTP7jnX"),
      timeoutSeconds = 10,
      port = 4000
    )
  )
```

The perhaps easiest way to log the configuration is to use `println`. As you can see in the example below, the secret configuration value is replaced with a `Secret(***)` placeholder when printed, while the remaining values of the configuration are printed with their `toString` representations as expected.

```tut:book
println(config)
```

When loading configuration values with type [`Secret`][Secret], Ciris will make sure that no sensitive details are included in error messages. In general, potentially sensitive information is only included in results from functions with `value` in the name: for example, [`value`][ConfigEntry#value], [`sourceValue`][ConfigEntry#sourceValue], [`toStringWithValue`][ConfigEntry#toStringWithValue], and [`toStringWithValues`][ConfigEntry#toStringWithValues]. So make sure you are not accidentally logging the results from such functions.

```tut:book
import ciris.{env, prop}

env[Secret[Int]]("FILE_ENCODING").
  orElse(prop("file.encoding")).
  value.left.map(_.message)

val fileEncoding =
  prop[Secret[String]]("file.encoding")

fileEncoding.sourceValue

fileEncoding.value

fileEncoding.toStringWithValue
```

## Logging Improvements
Relying on `toString` works reasonably well for small configurations, like in the example shown above, but as your configuration grows in size, it can be considerably more difficult to determine which value is which in the output. Making use of `toString` also means we're relying on every type having implemented an appropriate `toString` function, which might not always be the case.

As an alternative, we can make use of the [`Show`][Show] type class from [cats](/docs/cats-module). However, we would still like to avoid having to manually define the [`Show`][Show] behaviour for all the types in the configuration. Luckily, we can use [kittens][kittens], which in turn uses [shapeless][shapeless] to provide generic derivation of type class instances for [`Show`][Show]. The [`Show`][Show] instances derived by [kittens][kittens] also include the field names of our `case class`es, so it's easier to see which configuration value is which in the output. If we're using refinement types, there's a `refined-cats` module which provides [`Show`][Show] instances for refinement types.

```tut:silent
import cats.Show
import cats.derived._
import cats.implicits._
import ciris.cats._
import eu.timepit.refined.cats._

implicit val showConfig: Show[Config] = {
  import auto.show._
  semi.show
}
```

```tut:book
println(config.show)
```

There is also a `showPretty` derivation in [kittens][kittens], which uses pretty printing instead of simply rendering everything on one line. If your configuration is somewhat big, it might be preferable to instead use this kind of derivation over the one shown above.

```tut:silent
implicit val showConfig: Show[Config] = {
  import auto.showPretty._
  semi.showPretty
}
```

```tut:book
println(config.show)
```

[Secret]: /api/ciris/Secret.html
[kittens]: https://github.com/milessabin/kittens
[Show]: https://typelevel.org/cats/typeclasses/show.html
[shapeless]: https://github.com/milessabin/shapeless
[ConfigEntry#value]: /api/ciris/ConfigEntry.html#value:F[Either[ciris.ConfigError,V]]
[ConfigEntry#sourceValue]: /api/ciris/ConfigEntry.html#sourceValue:F[Either[ciris.ConfigError,S]]
[ConfigEntry#toStringWithValue]: /api/ciris/ConfigEntry.html#toStringWithValue:String
[ConfigEntry#toStringWithValues]: /api/ciris/ConfigEntry.html#toStringWithValues:String
