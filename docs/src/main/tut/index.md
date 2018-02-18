---
layout: home
section: "home"
---

```tut:invisible
import ciris.build.info._

def minorVersion(version: String): String =
  version.split('.').init.mkString(".")

val scalaPublishVersions: Seq[String] =
  crossScalaVersions.map(minorVersion)

val scalaPublishVersionsString: String =
  scalaPublishVersions.init.mkString(", ") ++ scalaPublishVersions.lastOption.map(", and " + _).mkString

val latestMinorVersion =
  minorVersion(latestVersion)
```

[![Typelevel](https://img.shields.io/badge/typelevel-library-fd3d50.svg)](https://typelevel.org/projects/#ciris) [![Travis](https://img.shields.io/travis/vlovgr/ciris/master.svg)](https://travis-ci.org/vlovgr/ciris) [![Codecov](https://img.shields.io/codecov/c/github/vlovgr/ciris.svg)](https://codecov.io/gh/vlovgr/ciris) [![Gitter](https://img.shields.io/gitter/room/vlovgr/ciris.svg?colorB=36bc97)](https://gitter.im/vlovgr/ciris) [![Version](https://img.shields.io/maven-central/v/is.cir/ciris-core_2.12.svg?label=version)](https://index.scala-lang.org/vlovgr/ciris)

## <a name="ciris" href="#ciris">Ciris</a>
Lightweight, extensible, and validated configuration loading in [Scala][scala], [Scala.js][scalajs], and [Scala Native][scalanative].  
The core library is dependency-free, while modules provide integrations with external libraries.

The name comes from being an abbreviation of the word _configurations_.  
Ciris' logo was inspired by the epyllion Ciris from [Appendix Vergiliana](https://en.wikipedia.org/wiki/Appendix_Vergiliana#Ciris_.28.22The_Sea-Bird.22.29).

> The Ciris is an epyllion in 541 hexameters describing the myth of Nisus, the king of Megara and his daughter Scylla.<br/>
> After the city falls and Scylla is taken prisoner on the Cretan ships, Amphitrite transforms her into the _ciris_ seabird.

Ciris is a new project under active development. Feedback and contributions are welcome.

### <a name="introduction" href="#introduction">Introduction</a>
Ciris is a _configuration as code_ library for compile-time safe configurations. For the configuration values which cannot reside in code, Ciris helps you to load and decode values from various sources, while dealing with effects and errors. Validation is encoded by using appropriate data types, with integrations to libraries such as [enumeratum][enumeratum], [refined][refined], [spire][spire], and [squants][squants].

Ciris is an alternative to configuration files in situations where it's easy to change and deploy software. Ciris aims to make it easy, safe, and secure to work with configurations, by eliminating many common configuration errors, by preventing errors from occurring as early as possible, and by loading secret configuration values directly from vault services.

For a more detailed introduction, please refer to the [usage guide](https://cir.is/docs).

<p align="center">
  <a href="https://asciinema.org/a/151742">
    <img src="https://asciinema.org/a/151742.png" width="65%"/>
  </a>
</p>

### <a name="getting-started" href="#getting-started">Getting Started</a>
To get started with [SBT][sbt], simply add the following lines to your `build.sbt` file.  
For an overview, usage instructions, and examples, please see the [usage guide](https://cir.is/docs).

```tut:passthrough
println(
s"""
 |```scala
 |val cirisVersion = "$latestVersion"
 |
 |libraryDependencies ++= Seq(
 |  "$organization" %% "$catsModuleName",
 |  "$organization" %% "$catsEffectModuleName",
 |  "$organization" %% "$coreModuleName",
 |  "$organization" %% "$enumeratumModuleName",
 |  "$organization" %% "$genericModuleName",
 |  "$organization" %% "$refinedModuleName",
 |  "$organization" %% "$spireModuleName",
 |  "$organization" %% "$squantsModuleName"
 |).map(_ % cirisVersion)
 |```
 """.stripMargin.trim
)
```

```tut:passthrough
println(
s"""
 |Make sure to replace `%%` with `%%%` above if you are using Scala.js or Scala Native.  
 |Libraries are published for Scala $scalaPublishVersionsString, and Java 8 where possible.  
 |For changes between versions, please see the [release notes](https://github.com/vlovgr/ciris/releases).
 |
 |Refer to the table below for platform and version support across modules.
 |
 || Module                  | Scala                                                                        | Scala.js                                                                          | Scala Native                                                                       |
 ||-------------------------|------------------------------------------------------------------------------|-----------------------------------------------------------------------------------|------------------------------------------------------------------------------------|
 || `$catsModuleName`       | &#10003; ${catsJvmCrossScalaVersions.map(minorVersion).mkString(", ")}       | &#10003; 0.6 (${catsJsCrossScalaVersions.map(minorVersion).mkString(", ")})       | &#65794;                                                                           |
 || `$catsEffectModuleName` | &#10003; ${catsEffectJvmCrossScalaVersions.map(minorVersion).mkString(", ")} | &#10003; 0.6 (${catsEffectJsCrossScalaVersions.map(minorVersion).mkString(", ")}) | &#65794;                                                                           |
 || `$coreModuleName`       | &#10003; ${coreJvmCrossScalaVersions.map(minorVersion).mkString(", ")}       | &#10003; 0.6 (${coreJsCrossScalaVersions.map(minorVersion).mkString(", ")})       | &#10003; 0.3 (${coreNativeCrossScalaVersions.map(minorVersion).mkString(", ")})    |
 || `$enumeratumModuleName` | &#10003; ${enumeratumJvmCrossScalaVersions.map(minorVersion).mkString(", ")} | &#10003; 0.6 (${enumeratumJsCrossScalaVersions.map(minorVersion).mkString(", ")}) | &#65794;                                                                           |
 || `$genericModuleName`    | &#10003; ${genericJvmCrossScalaVersions.map(minorVersion).mkString(", ")}    | &#10003; 0.6 (${genericJsCrossScalaVersions.map(minorVersion).mkString(", ")})    | &#10003; 0.3 (${genericNativeCrossScalaVersions.map(minorVersion).mkString(", ")}) |
 || `$refinedModuleName`    | &#10003; ${refinedJvmCrossScalaVersions.map(minorVersion).mkString(", ")}    | &#10003; 0.6 (${refinedJsCrossScalaVersions.map(minorVersion).mkString(", ")})    | &#65794;                                                                           |
 || `$spireModuleName`      | &#10003; ${spireJvmCrossScalaVersions.map(minorVersion).mkString(", ")}      | &#10003; 0.6 (${spireJsCrossScalaVersions.map(minorVersion).mkString(", ")})      | &#65794;                                                                           |
 || `$squantsModuleName`    | &#10003; ${squantsJvmCrossScalaVersions.map(minorVersion).mkString(", ")}    | &#10003; 0.6 (${squantsJsCrossScalaVersions.map(minorVersion).mkString(", ")})    | &#65794;                                                                           |
 |
 |Backwards binary compatibility for the library is guaranteed between minor versions.  
 |For example, `$latestMinorVersion.x` is backwards binary compatible with `$latestMinorVersion.y` for any `x > y`.  
 |More recent minor versions are drop-in replacements for earlier minor versions.
 """.stripMargin.trim
)
```

The only required module is `ciris-core`, the rest are optional library integrations.  
For an explanation of how to use the modules, refer to the [modules overview](https://cir.is/docs/modules) section.

- The `ciris-cats` module provides typeclasses and typeclass instances from [cats][cats].
- The `ciris-cats-effect` module provides typeclasses for effect types from [cats-effect][cats-effect].
- The `ciris-enumeratum` module allows loading [enumeratum][enumeratum] enumerations.
- The `ciris-generic` module allows loading more types with [shapeless][shapeless].
- The `ciris-refined` module allows loading [refined][refined] refinement types.
- The `ciris-spire` module allows loading [spire][spire] number types.
- The `ciris-squants` module allows loading [squants][squants] data types.

If you're using `ciris-generic` with Scala 2.10, you'll need to include the [Macro Paradise](https://docs.scala-lang.org/overviews/macros/paradise.html) compiler plugin.

```scala
libraryDependencies += compilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.patch)
```

If you're using `ciris-cats` or `ciris-cats-effect` with Scala 2.11.9 or later, you should enable [partial unification](https://github.com/scala/bug/issues/2712):

```scala
scalacOptions += "-Ypartial-unification"
```

or, if you need to support Scala 2.10.6 or later, you can use the [sbt-partial-unification](https://github.com/fiadliel/sbt-partial-unification) plugin.

```scala
addSbtPlugin("org.lyranthe.sbt" % "partial-unification" % "1.1.0")
```

#### <a name="ammonite" href="#ammonite">Ammonite</a>
To start an [Ammonite REPL](http://www.lihaoyi.com/Ammonite/#Ammonite-REPL) with Ciris loaded and imported, simply run the following.
```bash
curl -Ls try.cir.is | sh
```
You will need to have a JDK installed. The [script](https://try.cir.is) will then:
* download and install the latest available version of [coursier](https://github.com/coursier/coursier),
* use coursier to fetch Ammonite and the latest Ciris version,
* start an Ammonite REPL with Ciris already imported.

To use [Typelevel Scala](https://typelevel.org/scala/) with literal types enabled, instead use the following.
```bash
curl -Ls try.cir.is/typelevel | sh
```

If you already have the Ammonite REPL installed, you can load Ciris using the following commands.
```tut:passthrough
println(
s"""
 |```scala
 |import $$ivy.`$organization::$catsModuleName:$latestVersion`, ciris.cats._
 |import $$ivy.`$organization::$catsEffectModuleName:$latestVersion`, ciris.cats.effect._, ciris.cats.effect.syntax._
 |import $$ivy.`$organization::$coreModuleName:$latestVersion`, ciris._, ciris.syntax._
 |import $$ivy.`$organization::$enumeratumModuleName:$latestVersion`, ciris.enumeratum._
 |import $$ivy.`$organization::$genericModuleName:$latestVersion`, ciris.generic._
 |import $$ivy.`$organization::$refinedModuleName:$latestVersion`, ciris.refined._, ciris.refined.syntax._
 |import $$ivy.`$organization::$spireModuleName:$latestVersion`, ciris.spire._
 |import $$ivy.`$organization::$squantsModuleName:$latestVersion`, ciris.squants._
 |```
 """.stripMargin.trim
)
```

#### <a name="external-libraries" href="#external-libraries">External Libraries</a>
Below is an incomplete list of third-party libraries that integrate with Ciris.  
If your library is not included in the list, then please open a pull request.

* [`ciris-aiven-kafka`][ciris-aiven-kafka]
* [`ciris-aws-ssm`][ciris-aws-ssm]
* [`ciris-credstash`][ciris-credstash]
* [`ciris-kubernetes`][ciris-kubernetes]

### <a name="documentation" href="#documentation">Documentation</a>
For an overview, with examples and explanations of the most common use cases, please refer to the [usage guide](https://cir.is/docs).  
If you're looking for a more detailed code-centric overview, you can instead take a look at the [API documentation](https://cir.is/api).

### <a name="participation" href="#participation">Participation</a>
Ciris embraces pure, typeful, idiomatic functional programming in Scala, and wants to provide a safe and friendly environment for teaching, learning, and contributing as described in the [Typelevel Code of Conduct](https://typelevel.org/conduct.html). It is expected that participants follow the code of conduct in all official channels, including on GitHub and in the Gitter chat room.

If you would like to be involved in building Ciris, check out the [contributing guide](https://cir.is/docs/contributing).

### <a name="license" href="#license">License</a>
Ciris is available under the MIT license, available at [https://opensource.org/licenses/MIT](https://opensource.org/licenses/MIT) and in the [license file](https://github.com/vlovgr/ciris/blob/master/license.txt).

[cats]: https://github.com/typelevel/cats
[cats-effect]: https://github.com/typelevel/cats-effect
[ciris-aiven-kafka]: https://github.com/ovotech/ciris-aiven-kafka
[ciris-aws-ssm]: https://github.com/ovotech/ciris-aws-ssm
[ciris-credstash]: https://github.com/ovotech/ciris-credstash
[ciris-kubernetes]: https://github.com/ovotech/ciris-kubernetes
[enumeratum]: https://github.com/lloydmeta/enumeratum
[refined]: https://github.com/fthomas/refined
[shapeless]: https://github.com/milessabin/shapeless
[spire]: https://github.com/non/spire
[squants]: https://github.com/typelevel/squants
[sbt]: https://www.scala-sbt.org
[scala]: https://www.scala-lang.org
[scalajs]: https://www.scala-js.org
[scalanative]: http://scala-native.org
