---
layout: docs
title: "Usage Basics"
position: 1
permalink: /docs/basics
---

```tut:invisible
val tempFile = {
  val file = java.io.File.createTempFile("temp-", ".tmp")
  file.deleteOnExit()

  val writer = new java.io.FileWriter(file)
  try {
    writer.write("1234 ")
  } finally {
    writer.close()
  }

  file
}
```

# Usage Basics
Ciris is a _configuration as code_ library for compile-time safe configurations. This means that your configuration models and data is part of your code, in contrast to configuration files (and libraries like [Lightbend Config](https://github.com/lightbend/config)), where the configuration data resides in configuration files with specialized syntax. Writing configurations as code is most often an easy, safe, and secure alternative to configuration files, suitable in situations where it's easy to change and deploy software. But before we begin to explore Ciris and configurations as code, let's take a brief moment to discuss why and when you might consider configuration files, and when configurations as code could be a better fit.

**Consider *configuration files* in the following situation.**
- _You want to be able to change most configuration values without releasing a new version of the software._ Often these changes are made separate to the version control repository of your software. This can be desirable if it's difficult to release and deploy software, or if you're not sure in which environments the software will run. End-users or stakeholders might need to be able to configure certain aspects of your software, and recompiling the software to make configuration changes is not a viable alternative.

**Consider *configuration as code* in the following situations.**
- _You want to load configuration values from various sources in a uniform way._ With configuration as code, and Ciris specifically, you can load values from, for example, both environment variables and vault services in the same way. With configuration files, you might still need to fetch secret values from a vault service as a separate ad-hoc step.
- _You want a single place for your configuration, including multiple environments, while avoiding duplicated values._ Configuration as code gives you the ability to deal with [multiple environments](/docs/environments) explicitly, making it clear when and what is different across environments, while avoiding any sort of duplication of configuration values.
- _You want to check that the constant values in your configuration are valid at compile-time._ Constant configuration values are literals in code, and will therefore be checked at compile-time. This is especially powerful when used together with [refinement types](/docs/validation). This means you no longer have to rely on tests to confirm that your default configuration values are valid and useable.
- _You want unused configuration values to be easy to spot._ With configuration as code, unused configuration values are dead code, making it easy to spot when certain values are not being used anymore. In contrast with configuration files, unused configuration values are often harder to spot, and get noticed first much later on.
- _You want to avoid having to use a separate configuration syntax, and would rather just write code._ There's no need to learn and use a different syntax, and there is no need to have a library for parsing that configuration file syntax. Having configurations as code also means you're able to refactor your configurations with more confidence.
- _You want a flexible configuration loading process, without being limited to a configuration syntax._ Due to the limited nature of configuration files and their syntax, some configuration loading is more inherently difficult, or even impossible, to express. With Ciris and configuration as code, you have more flexibility around configuration loading.

While it's possible to not use any libraries when writing configurations as code, loading values from the environment typically means dealing with: different environments and configuration sources, type conversions, error handling, and validation. This is where Ciris comes in: a small library, dependency-free at its core, helping you to deal with all of that.

## Configuration Values
Ciris includes functions [`env`][env] (for reading environment variables), [`prop`][prop] (for reading system properties), and [`file`][file] and [`fileWithName`][fileWithName] (for reading file contents). In a similar fashion, if you would be using any of the external libraries like [ciris-kubernetes][ciris-kubernetes] or [ciris-aws-ssm][ciris-aws-ssm], they respectively provide functions `secret` (for reading Kubernetes secrets) and `param` (for reading AWS SSM parameters). These functions have in common that they accept a type to which to convert the value, for example `String` or `Int`, and then the key to read (or in case of [`file`][file] and [`fileWithName`][fileWithName], the file which contents should be read). The result is a key-value pair, represented by [`ConfigEntry`][ConfigEntry], like in the following example.

```tut:book
import ciris.{env, prop, file}

// Read environment variable LANG as a String
env[String]("LANG")

// Read system property file.encoding as a String
prop[String]("file.encoding")

// Read the file, trim the file contents, and convert to Int
file[Int](tempFile, _.trim)
```

Ciris handles errors when reading values, for example if the environment variable or file doesn't exist, or if the value couldn't be converted to the specified type. In the background, these functions are loading values from a [configuration source](/docs/sources) (represented by [`ConfigSource`][ConfigSource]) and converting the value to the specified type with a [configuration decoder](/docs/decoders) (represented by [`ConfigDecoder`][ConfigDecoder]). For a list of currently supported types, refer to the [current supported types](/docs/supported-types) section.

It is also possible to optionally read values with `Option`.

```tut:book
val fileEncoding =
  env[Option[String]]("FILE_ENCODING")

// The unmodified source value is available in the entry,
// and here we can see that the environment variable has
// not been set
fileEncoding.sourceValue

// We get None back as the value, since the environment
// variable has not been set
fileEncoding.value
```

You can also use [`orElse`][orElse] to fall back to other values.

```tut:book
// Uses the value of the file.encoding system property as
// the FILE_ENCODING environment variable has not been set
env[String]("FILE_ENCODING").
  orElse(prop[String]("file.encoding"))
```

When using [`orElse`][orElse], we get a [`ConfigValue`][ConfigValue] back, since we've combined the values of multiple [`ConfigEntry`][ConfigEntry]s.

You can also combine [`orElse`][orElse] and [`orNone`][orNone] to fall back to other values, but not require any of them to be available.

```tut:book
env[String]("API_KEY").
  orElse(prop[String]("api.key")).
  orNone
```

It is also possible to read values with type [`Secret`][Secret], in order to prevent values from being output in logs.

```tut:book
import ciris.Secret

prop[Secret[Option[String]]]("api.key").value
```

For more information on [`Secret`][Secret], refer to the [logging configurations](/docs/logging) section.

### Suspending Effects
Most functions we've seen so far return values wrapped in a context [`Id`][Id], which is a way to say that there is no context. Since [`Id`][Id] is defined as `type Id[A] = A`, you simply get the values without any context. However, you might have noticed that certain functions, while being safe, are not _pure_ in the sense that, if the function is called more than once with the same arguments, it might return different values. This applies to, for example, reading system properties with [`prop`][prop] (properties being mutable), and [`file`][file] and [`fileWithName`][fileWithName] (file contents may change).

One way to deal with these functions not being pure, is to model the effects explicitly with _effect types_. Instead of returning the result of reading a file, for example, we merely describe _how_ to read the file, by suspending the reading of the file in a context `F[_]` for which there is a [`Sync`][Sync] instance (for example, `IO` from [cats-effect][cats-effect] with the [cats-effect module](/docs/cats-effect-module)).

Ciris provides pure functions [`argF`][argF], [`envF`][envF], [`propF`][propF], and [`fileSync`][fileSync] and [`fileWithNameSync`][fileWithNameSync], which suspend the reading in a context `F[_]` for which there is a [`Sync`][Sync] instance. (Note that [`envF`][envF] is merely a convenience method which lifts the value into `F` without suspending, since environment variables are immutable.) If you're using any of the external libraries, like [ciris-kubernetes][ciris-kubernetes] or [ciris-aws-ssm][ciris-aws-ssm], they also provide pure functions which suspend reading, like `secretF` (for reading Kubernetes secrets) and `paramF` (for reading AWS SSM parameters).

```tut:book
import ciris.{propF, fileSync}
import ciris.cats.effect._
import cats.effect.IO

// Suspend reading of system property file.encoding as a String
propF[IO, String]("file.encoding")

// Suspend reading the file, trim the file contents, and convert to Int
fileSync[IO, Int](tempFile, _.trim)
```

## Loading Configurations
We're now able to represent configuration entries (with [`ConfigEntry`][ConfigEntry]) and configuration values (with [`ConfigValue`][ConfigValue]) in our application, so now it's time to combine multiple values into a configuration. We'll start by modelling our configuration with nested case classes, like in the following example. If you haven't already separated your application from your configuration, now is a good time to do so, to be able to load the configuration seperately. In the example below, we're using [refinement types](/docs/refined-module) to [encode validation](/docs/validation) in the types of the configuration.

```tut:silent
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

The API key is secret, and we've wrapped it in [`Secret`][Secret] to denote that it shouldn't be included in log output. For the port, we need it to be dynamic depending on the environment, and default to a fixed port if it's not specified. In order to combine multiple configuration values, Ciris provides the [`loadConfig`][loadConfig] function, which accepts a number of configuration values ([`ConfigEntry`][ConfigEntry]s or [`ConfigValue`][ConfigValue]s) and the function with which to create the configuration.

```tut:book
import ciris.loadConfig
import ciris.refined._
import eu.timepit.refined.auto._

val config =
  loadConfig(
    env[Secret[ApiKey]]("API_KEY").
      orElse(prop[Secret[ApiKey]]("api.key")),
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
```

Note that the literal values above (the name, timeout, and default port) are validated at compile-time. If there are errors for the configuration values, Ciris will deal with them and accumulate them as [`ConfigErrors`][ConfigErrors], before finally returning an `Either[ConfigErrors, Config]`. Note that the result is wrapped in [`Id`][Id], which is to say that no context (for example, effect type) was used. We could just as well have described the configuration loading with, for example, `IO` from [cats-effect][cats-effect] instead, using `envF` and `propF`, as seen in the [suspending effects](#suspending-effects) section.

```tut:book
import ciris.{envF, propF}

val configF =
  loadConfig(
    envF[IO, Secret[ApiKey]]("API_KEY").
      orElse(propF[IO, Secret[ApiKey]]("api.key")),
    propF[IO, Option[UserPortNumber]]("http.port")
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
```

At this point, we can see that both the `API_KEY` environment variable and `api.key` system property are missing, so we've gotten a [`ConfigErrors`][ConfigErrors] back. We can use the [`messages`][messages] function to retrieve error messages which are a bit more readable. Alternatively, with a syntax import, you can use [`orThrow`][orThrow] to throw an exception with the error messages, if there are any, or return the configuration if it could be loaded successfully.

```tut:book
config.left.map(_.messages)
```

```tut:book:fail
{
  import ciris.syntax._
  config.orThrow()
}
```
### Dynamic Configuration Loading
Sometimes it's necessary to change how the configuration is loaded depending on some configuration value. For example, you might want to load configurations differently depending on in which environment the application is being run. You might want to use a fixed configuration in the local and testing environments, while loading secret values from a vault service in the production environment. One way to represent environments is with [enumeratum](/docs/enumeratum-module) enumerations, like in the following example. Refer to the [multiple environments](/docs/environments) section for more information.

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

Ciris provides the [`withValues`][withvalues] (and [`withValue`][withValue] for a single value) function for being able to change how the configuration is loaded depending on the specified values. For example, following is an example of how to use a fixed configuration in the local and testing environments, while keeping the previously seen configuration loading in the production environment. Note that when using [`withValues`][withValues], errors for the specified values (in this case, the `APP_ENV` environment variable) means we will not continue to try to load the configuration, meaning potential further errors are not included.

```tut:book
import ciris.withValue
import ciris.enumeratum._

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
          orElse(prop[Secret[ApiKey]]("api.key")),
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

In many cases it's not necessary to have this kind of dynamic configuration loading. If you want to keep the configuration loading process the same across environments, but want to have some values be different depending on the environment, that is also possible, like in the following example. You can also mix the two approaches as you see necessary.

```tut:book
val config =
  loadConfig(
    env[AppEnvironment]("APP_ENV"),
    env[Secret[ApiKey]]("API_KEY").
      orElse(prop[Secret[ApiKey]]("api.key")),
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

[enumeratum]: https://github.com/lloydmeta/enumeratum
[ciris-kubernetes]: https://github.com/ovotech/ciris-kubernetes
[ciris-aws-ssm]: https://github.com/ovotech/ciris-aws-ssm
[env]: /api/ciris/index.html#env[Value](key:String)(implicitdecoder:ciris.ConfigDecoder[String,Value]):ciris.ConfigEntry[ciris.api.Id,String,String,Value]
[prop]: /api/ciris/index.html#prop[Value](key:String)(implicitdecoder:ciris.ConfigDecoder[String,Value]):ciris.ConfigEntry[ciris.api.Id,String,String,Value]
[file]: /api/ciris/index.html#file[Value](file:java.io.File,modifyFileContents:String=>String,charset:java.nio.charset.Charset)(implicitdecoder:ciris.ConfigDecoder[String,Value]):ciris.ConfigEntry[ciris.api.Id,(java.io.File,java.nio.charset.Charset),String,Value]
[fileWithName]: /api/ciris/index.html#fileWithName[Value](name:String,modifyFileContents:String=>String,charset:java.nio.charset.Charset)(implicitdecoder:ciris.ConfigDecoder[String,Value]):ciris.ConfigEntry[ciris.api.Id,(java.io.File,java.nio.charset.Charset),String,Value]
[ConfigEntry]: /api/ciris/ConfigEntry.html
[ConfigSource]: /api/ciris/ConfigSource.html
[ConfigDecoder]: /api/ciris/ConfigDecoder.html
[orElse]: /api/ciris/ConfigValue.html#orElse[A>:V](that:ciris.ConfigValue[F,A]):ciris.ConfigValue[F,A]
[ConfigValue]: /api/ciris/ConfigValue.html
[Secret]: /api/ciris/Secret.html
[cats-effect]: https://github.com/typelevel/cats-effect
[loadConfig]: /api/ciris/index.html#loadConfig[F[_],A1,A2,Z](a1:ciris.ConfigValue[F,A1],a2:ciris.ConfigValue[F,A2])(f:(A1,A2)=>Z)(implicitevidence$5:ciris.api.Functor[F]):F[Either[ciris.ConfigErrors,Z]]
[argF]: /api/ciris/index.html#argF[F[_],Value](args:IndexedSeq[String])(index:Int)(implicitevidence$3:ciris.api.Sync[F],implicitdecoder:ciris.ConfigDecoder[String,Value]):ciris.ConfigEntry[F,Int,String,Value]
[envF]: /api/ciris/index.html#envF[F[_],Value](key:String)(implicitevidence$1:ciris.api.Applicative[F],implicitdecoder:ciris.ConfigDecoder[String,Value]):ciris.ConfigEntry[F,String,String,Value]
[fileSync]: /api/ciris/index.html#fileSync[F[_],Value](file:java.io.File,modifyFileContents:String=>String,charset:java.nio.charset.Charset)(implicitevidence$1:ciris.api.Sync[F],implicitdecoder:ciris.ConfigDecoder[String,Value]):ciris.ConfigEntry[F,(java.io.File,java.nio.charset.Charset),String,Value]
[fileWithNameSync]: /api/ciris/index.html#fileWithNameSync[F[_],Value](name:String,modifyFileContents:String=>String,charset:java.nio.charset.Charset)(implicitevidence$2:ciris.api.Sync[F],implicitdecoder:ciris.ConfigDecoder[String,Value]):ciris.ConfigEntry[F,(java.io.File,java.nio.charset.Charset),String,Value]
[propF]: /api/ciris/index.html#propF[F[_],Value](key:String)(implicitevidence$2:ciris.api.Sync[F],implicitdecoder:ciris.ConfigDecoder[String,Value]):ciris.ConfigEntry[F,String,String,Value]
[Secret]: /api/ciris/Secret.html
[Id]: /api/ciris/api/index.html#Id[A]=A
[ConfigErrors]: /api/ciris/ConfigErrors.html
[messages]: /api/ciris/ConfigErrors.html#messages:Vector[String]
[orThrow]: /api/ciris/syntax$$EitherConfigErrorsSyntax.html#orThrow():T
[withValues]: /api/ciris/index.html#withValues[F[_],A1,A2,Z](a1:ciris.ConfigValue[F,A1],a2:ciris.ConfigValue[F,A2])(f:(A1,A2)=>F[Either[ciris.ConfigErrors,Z]])(implicitevidence$6:ciris.api.Monad[F]):F[Either[ciris.ConfigErrors,Z]]
[withValue]: /api/ciris/index.html#withValue[F[_],A1,Z](a1:ciris.ConfigValue[F,A1])(f:A1=>F[Either[ciris.ConfigErrors,Z]])(implicitevidence$3:ciris.api.Monad[F]):F[Either[ciris.ConfigErrors,Z]]
[orNone]: /api/ciris/ConfigValue.html#orNone:ciris.ConfigValue[F,Option[V]]
[Sync]: /api/ciris/api/Sync.html
