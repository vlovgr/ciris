[![Travis](https://img.shields.io/travis/vlovgr/ciris/master.svg)](https://travis-ci.org/vlovgr/ciris) [![Gitter](https://img.shields.io/gitter/room/vlovgr/ciris.svg?colorB=4db798)](https://gitter.im/vlovgr/ciris)

## Ciris

[![Join the chat at https://gitter.im/vlovgr/ciris](https://badges.gitter.im/vlovgr/ciris.svg)](https://gitter.im/vlovgr/ciris?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
Lightweight, extensible, and validated configuration loading in [Scala][scala], [Scala.js][scalajs], and [Scala Native][scalanative].  
The core library is [dependency-free](build.sbt), while provided modules integrate with other popular libraries.

### Introduction
Ciris encourages compile-time safety by defining as much as possible of your configurations in Scala. For the data which cannot reside in code, Ciris helps you to load and decode values, while dealing with errors. Validation is encoded by using appropriate data types, with available integrations to libraries such as [refined][refined] and [squants][squants] to support even more types. Configurations are typically modeled as case class hierarchies, but you are free to choose the model you see fit.

### Getting Started
To get started with [SBT][sbt], simply add the following lines to your `build.sbt` file:
```
libraryDependencies ++= Seq(
  "is.cir" %% "ciris-core" % "0.0.1-SNAPSHOT",
  "is.cir" %% "ciris-enumeratum" % "0.0.1-SNAPSHOT",
  "is.cir" %% "ciris-refined" % "0.0.1-SNAPSHOT",
  "is.cir" %% "ciris-squants" % "0.0.1-SNAPSHOT"
)
```
and make sure to replace `%%` with `%%%` if you're using Scala.js or Scala Native.  
The only required module is `ciris-core`, the rest are optional library integrations.

- The `ciris-enumeratum` module allows loading [enumeratum][enumeratum] enumerations.
- The `ciris-refined` module allows loading [refined][refined] refinement types.
- The `ciris-squants` module allows loading [squants][squants] data types.

Modules are published for Scala 2.10, 2.11, and 2.12 wherever possible.  
Please take note that Scala Native currently only supports Scala 2.11.

### Usage Example
Ciris configuration loading is done in two parts: define what to load, and what to create once everything is loaded.  
We are using a case class for our configuration in the following example, but feel free to use whatever you want.  
The `ciris-refined` module is used here to integrate with [refined][refined] to encode validation in the data types.

```scala
import ciris._
import ciris.refined._

import eu.timepit.refined.auto._
import eu.timepit.refined.types.net.PortNumber
import eu.timepit.refined.types.string.NonEmptyString

import scala.concurrent.duration._

final case class Config(
  apiKey: NonEmptyString, // API key cannot be empty
  timeout: Duration,
  port: PortNumber // Only allow Ints which are port numbers
)

val config: Either[ConfigErrors, Config] =
  loadConfig(
    env[NonEmptyString]("API_KEY"), // Reads environment variable API_KEY
    prop[Option[PortNumber]]("http.port") // Reads system property http.port
  ) { (apiKey, port) =>
    Config(
      apiKey = apiKey,
      timeout = 10 seconds,
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

[enumeratum]: https://github.com/lloydmeta/enumeratum
[refined]: https://github.com/fthomas/refined
[squants]: http://www.squants.com
[sbt]: http://www.scala-sbt.org
[scala]: http://www.scala-lang.org
[scalajs]: https://www.scala-js.org
[scalanative]: http://www.scala-native.org/
