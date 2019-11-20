---
id: configurations
title: Configurations
---

[`ConfigValue`][configvalue] is the central concept in the library. It represents a single configuration value, or a composition of multiple values. The library provides functions like `env`, `file`, and `prop` for creating such [`ConfigValue`][configvalue]s for environment variables, file contents, and system properties. If the value is missing, `or` lets us use a fallback.

```scala mdoc:reset-object:silent
import ciris._

val port: ConfigValue[Int] =
  env("API_PORT").or(prop("api.port")).as[Int]
```

Using `as` we can attempt to decode the value to a different type.

Multiple values can be combined, and errors accumulated, using `parMapN`.

```scala mdoc:silent
import cats.implicits._
import scala.concurrent.duration._

final case class ApiConfig(port: Int, timeout: Option[Duration])

val timeout: ConfigValue[Option[Duration]] =
  env("API_TIMEOUT").as[Duration].option

val apiConfig: ConfigValue[ApiConfig] =
  (port, timeout).parMapN(ApiConfig)
```

We can also use `flatMap`, or for-comprehensions, to load values without error accumulation.

```scala mdoc:silent
for {
  port <- env("API_PORT").or(prop("api.port")).as[Int]
  timeout <- env("API_TIMEOUT").as[Duration].option
} yield ApiConfig(port, timeout)
```

Using `option` we wrap the value in `Option`, using `None` if the value is missing.

## Defaults

Instead of using `None` as default with `option`, we can specify a default with `default`.

```scala mdoc:silent
env("API_TIMEOUT").as[Duration].default(10.seconds)
```

Note that using `a.option` is equivalent to `a.map(_.some).default(None)`.

Default values will only be used if the value is missing. If the value is a composition of multiple values, the default will only be used if all of them are missing. Additionally, later defaults override any earlier defined defaults. This behaviour enables us to specify a default for a composition of values.

```scala mdoc:silent
(
  env("API_PORT").as[Int],
  env("API_TIMEOUT").as[Duration].option
).parMapN(ApiConfig).default {
  ApiConfig(3000, 20.seconds.some)
}
```

When using a fallback with `or`, defaults in the fallback will override earlier defaults.

```scala mdoc:silent
env("API_PORT").as[Int].default(9000)
  .or(prop("api.port").as[Int].default(3000))
```

We can create a default value using `default`, with `a.default(b)` equivalent to `a.or(default(b))`.

```scala mdoc:silent
env("API_PORT").as[Int].or(default(9000))
```

## Secrets

When loading sensitive configuration values, `secret` can be used.

```scala mdoc:silent
val apiKey: ConfigValue[Secret[String]] =
  env("API_KEY").secret
```

By using `secret`, the value is wrapped in [`Secret`][secret], which prevents the value from being shown. When shown, the value is replaced by the first 7 characters of the SHA-1 hash for the value. This enables us to check whether the correct secret is being used, while not exposing the value.

```scala mdoc
Secret("RacrqvWjuu4KVmnTG9b6xyZMTP7jnX")
```

To calculate the short hash ourselves, we can e.g. use `sha1sum`.

```bash
$ echo -n "RacrqvWjuu4KVmnTG9b6xyZMTP7jnX" | sha1sum | head -c 7
0a7425a
```

When using `secret`, sensitive details, like the value, are also redacted from errors.

### Redacting

In addition to `secret` there is also `redacted` which redacts sensitive details from errors, without wrapping the value in [`Secret`][secret]. We might not want to use [`Secret`][secret] and show the first 28/160 bits of the SHA-1 hash if there are few enough possible values to enable bruteforcing.

## Loading

In order to load a configuration, we can use `load` and specify an effect type.

```scala mdoc:silent
import cats.effect.{ExitCode, IO, IOApp}

object Main extends IOApp {
  def run(args: List[String]): IO[ExitCode] =
    apiConfig.load[IO].as(ExitCode.Success)
}
```

We can use `attempt` instead if we want access to the [`ConfigError`][configerror] messages.

## Decoders

When decoding using `as`, a matching [`ConfigDecoder`][configdecoder] instance has to be available.

The library provides instances for many common types, but we can also write an instance.

```scala mdoc:silent
sealed abstract case class PosInt(value: Int)

object PosInt {
  def apply(value: Int): Option[PosInt] =
    if(value > 0)
      Some(new PosInt(value) {})
    else None

  implicit val posIntConfigDecoder: ConfigDecoder[String, PosInt] =
    ConfigDecoder[String, Int].mapOption("PosInt")(apply)
}

env("MAX_RETRIES").as[PosInt]
```

## Sources

To support new configuration sources, we can use the [`ConfigValue`][configvalue$] functions.

Following is an example showing how the `env` function can be defined.

```scala mdoc:silent
def env(name: String): ConfigValue[String] =
  ConfigValue.suspend {
    val key = ConfigKey.env(name)
    val value = System.getenv(name)

    if (value != null) {
      ConfigValue.loaded(key, value)
    } else {
      ConfigValue.missing(key)
    }
  }
```

The [`ConfigKey`][configkey] is a description of the key, e.g. `s"environment variable $name"`. The function returns `missing` when there is no value for the key, and `loaded` when a value is available. Since reading environment variables can throw `SecurityException`s, we capture side effects using `suspend`.

[configdecoder]: @API_BASE_URL@/ConfigDecoder.html
[configerror]: @API_BASE_URL@/ConfigError.html
[configkey]: @API_BASE_URL@/ConfigKey.html
[configvalue]: @API_BASE_URL@/ConfigValue.html
[configvalue$]: @API_BASE_URL@/ConfigValue$.html
[secret]: @API_BASE_URL@/Secret.html
