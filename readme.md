[![Travis](https://img.shields.io/travis/vlovgr/ciris/master.svg)](https://travis-ci.org/vlovgr/ciris) [![Codecov](https://img.shields.io/codecov/c/github/vlovgr/ciris.svg)](https://codecov.io/gh/vlovgr/ciris) [![Gitter](https://img.shields.io/gitter/room/vlovgr/ciris.svg?colorB=4db798)](https://gitter.im/vlovgr/ciris) [![Version](https://img.shields.io/maven-central/v/is.cir/ciris-core_2.12.svg?color=blue&label=version)](https://index.scala-lang.org/vlovgr/ciris) [![Documentation](https://img.shields.io/maven-central/v/is.cir/ciris-core_2.12.svg?color=blue&label=docs)](https://www.javadoc.io/doc/is.cir/ciris-core_2.12)

## Ciris
Lightweight, extensible, and validated configuration loading in [Scala][scala].  
The core library is dependency-free, while modules provide library integrations.

### Introduction
Ciris encourages compile-time safety by defining as much as possible of your configurations in Scala. For the data which cannot reside in code, Ciris helps you to load and decode values, while dealing with errors. Validation is encoded by using appropriate data types, with available integrations to libraries such as [refined][refined] and [squants][squants] to support even more types. Configurations are typically modeled as case class hierarchies, but you are free to choose the model you see fit.

### Getting Started
To get started with [SBT][sbt], simply add the following lines to your `build.sbt` file.

```
// Libraries are published for Scala 2.10, 2.11, 2.12
libraryDependencies ++= Seq(
  "is.cir" %% "ciris-core" % "0.1.0",
  "is.cir" %% "ciris-enumeratum" % "0.1.0",
  "is.cir" %% "ciris-refined" % "0.1.0",
  "is.cir" %% "ciris-squants" % "0.1.0"
)
```

The only required module is `ciris-core`, the rest are optional library integrations.

- The `ciris-enumeratum` module allows loading [enumeratum][enumeratum] enumerations.
- The `ciris-refined` module allows loading [refined][refined] refinement types.
- The `ciris-squants` module allows loading [squants][squants] data types.

### Usage Examples
Ciris configuration loading is done in two parts: define what to load, and what to create once everything is loaded.  
Let's start simple by defining a configuration and loading only the necessary parts of it from the environment.

```scala
import ciris._

import scala.concurrent.duration._

final case class Config(
  apiKey: String,
  timeout: Duration,
  port: Int
)

val config: Either[ConfigErrors, Config] =
  loadConfig(
    env[String]("API_KEY"), // Reads environment variable API_KEY
    prop[Option[Int]]("http.port") // Reads system property http.port
  ) { (apiKey, port) =>
    Config(
      apiKey = apiKey,
      timeout = 10.seconds,
      port = port getOrElse 4000
    )
  }

```

```scala
show(config)
// res5: String = Left(ConfigErrors(MissingKey(API_KEY,Environment)))

show(config.left.map(_.messages))
// res6: String = Left(Vector(Missing environment variable [API_KEY]))
```

#### Encoding Validation
Ciris intentionally forces you to encode validation in your data types. This means you have to put more thought into your configurations, and in turn, your domain models. If you want to load an API key, you probably don't want it to be any `String`. If you want to load a port value, you probably don't want it to be any `Int` (valid ports are 0 to 65535).

By using more precise types, we get type-safety and a guarantee that values conform to our requirements. One of the easiest ways to encode validation in data types is by using [refined][refined]. Ciris provides a `ciris-refined` module which allows you to read any refined type. You can also get compile-time validation for literals from refined.

Let's modify the configuration to use refined types and again load the same parts from the environment.

```scala
import ciris.refined._

import eu.timepit.refined.auto._
import eu.timepit.refined.types.net.PortNumber
import eu.timepit.refined.types.string.NonEmptyString

final case class Config(
  apiKey: NonEmptyString,
  timeout: Duration,
  port: PortNumber
)

val config =
  loadConfig(
    env[NonEmptyString]("API_KEY"),
    prop[Option[PortNumber]]("http.port")
  ) { (apiKey, port) =>
    Config(
      apiKey = apiKey,
      timeout = 10 seconds,
      port = port getOrElse 4000
    )
  }
```

#### Multiple Environments
One of the most common use cases for configurations is having different values for different environments. There are several ways to deal with environments with Ciris: one way being to define an enumeration with [enumeratum][enumeratum] and use the `ciris-enumeratum` module to be able to load values of that enumeration. You can then switch configuration by just writing conditional statements in plain code.

Let's define and load a configuration depending on different environments.

```scala
import _root_.enumeratum._
import ciris.enumeratum._

object configuration {
  sealed abstract class AppEnvironment extends EnumEntry
  object AppEnvironment extends Enum[AppEnvironment] {
    case object Local extends AppEnvironment
    case object Testing extends AppEnvironment
    case object Production extends AppEnvironment

    val values = findValues
  }
}

import configuration._

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

    appEnvironment.map {
      case AppEnvironment.Local => default
      case _ => default.copy(timeout = 5 seconds)
    }.getOrElse(default)
  }
```

What about reading different configuration values depending on the environment? For example, you could use defaults for everything in a local environment, while reading configuration values, like the API key and port, in the other environments.

For that purpose, there is a `withValues` construct that you can use. It works exactly like `loadConfig`, except it wraps your `loadConfig` statements, only executing them if all the `withValues` values could be read successfully. If it helps, think of `loadConfig` as `map` and `withValues` as `flatMap` (which is also how they are defined internally).

```scala
withValues(
  env[Option[AppEnvironment]]("APP_ENV")
) {
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
[refined]: https://github.com/fthomas/refined
[squants]: http://www.squants.com
[sbt]: http://www.scala-sbt.org
[scala]: http://www.scala-lang.org
