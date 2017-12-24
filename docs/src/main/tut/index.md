---
layout: home
title:  "Home"
section: "home"
---

```tut:invisible
import ciris.build.info._

val scalaPublishVersions: Seq[String] =
  crossScalaVersions.map(_.split('.').init.mkString("."))

val scalaPublishVersionsString: String =
  scalaPublishVersions.init.mkString(", ") ++ scalaPublishVersions.lastOption.map(", and " + _).mkString

val scalaPublishVersionsShortString: String =
  scalaPublishVersions.mkString(", ")

val minorVersion =
  latestVersion.split('.').take(2).mkString(".")
```

[![Typelevel](https://img.shields.io/badge/typelevel-library-fd3d50.svg)](https://typelevel.org/projects/#ciris) [![Travis](https://img.shields.io/travis/vlovgr/ciris/master.svg)](https://travis-ci.org/vlovgr/ciris) [![Codecov](https://img.shields.io/codecov/c/github/vlovgr/ciris.svg)](https://codecov.io/gh/vlovgr/ciris) [![Gitter](https://img.shields.io/gitter/room/vlovgr/ciris.svg?colorB=36bc97)](https://gitter.im/vlovgr/ciris) [![Version](https://img.shields.io/maven-central/v/is.cir/ciris-core_2.12.svg?color=blue&label=version)](https://index.scala-lang.org/vlovgr/ciris) [![Documentation](https://img.shields.io/maven-central/v/is.cir/ciris-core_2.12.svg?color=blue&label=docs)](https://cir.is/api)

## <a name="ciris" href="#ciris">Ciris</a>
Lightweight, extensible, and validated configuration loading in [Scala][scala], [Scala.js][scalajs], and [Scala Native][scalanative].  
The core library is dependency-free, while modules provide integrations with external libraries.

The name comes from being an abbreviation of the word _configurations_.  
Ciris' logo was inspired by the epyllion Ciris from [Appendix Vergiliana](https://en.wikipedia.org/wiki/Appendix_Vergiliana#Ciris_.28.22The_Sea-Bird.22.29).

> The Ciris is an epyllion in 541 hexameters describing the myth of Nisus, the king of Megara and his daughter Scylla.<br/>
> After the city falls and Scylla is taken prisoner on the Cretan ships, Amphitrite transforms her into the _ciris_ seabird.

Ciris is a new project under active development. Feedback and contributions are welcome.

### <a name="introduction" href="#introduction">Introduction</a>
Ciris encourages compile-time safety by defining as much as possible of your configurations in Scala. For the data which cannot reside in code, Ciris helps you to load and decode values, while dealing with errors. Validation is encoded by using appropriate data types, with available integrations to libraries such as [refined][refined], [spire][spire], and [squants][squants].

Ciris is intended as an alternative to configuration files, and libraries like [Typesafe Config](https://github.com/typesafehub/config), in situations where it's easy to change and deploy software. Ciris aims to make it easy and safe to work with configurations by completely eliminating many common configuration errors, and by preventing errors from occurring as early as possible.

Refer to the presentation [Refined types for validated configurations](https://www.youtube.com/watch?v=C3ciegxMAqA) ([slides](https://vlovgr.github.io/refined-types)) and follow-up blog post  
[Validated Configurations with Ciris](https://typelevel.org/blog/2017/06/21/ciris.html) for an introduction to Ciris and configurations with refined types.

<p align="center">
  <a href="https://asciinema.org/a/151742">
    <img src="https://asciinema.org/a/151742.png" width="75%"/>
  </a>
</p>

### <a name="getting-started" href="#getting-started">Getting Started</a>
To get started with [SBT][sbt], simply add the following lines to your `build.sbt` file.  
For an overview, usage instructions, and examples, please see the [usage guide](https://cir.is/docs/basics).

```tut:evaluated
println(
s"""
 |val cirisVersion = "$latestVersion"
 |
 |libraryDependencies ++= Seq(
 |  "$organization" %% "$coreModuleName",
 |  "$organization" %% "$enumeratumModuleName",
 |  "$organization" %% "$genericModuleName",
 |  "$organization" %% "$refinedModuleName",
 |  "$organization" %% "$spireModuleName",
 |  "$organization" %% "$squantsModuleName"
 |).map(_ % cirisVersion)
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
 || Module                | Scala                                     | Scala.js                                        | Scala Native        |
 ||-----------------------|-------------------------------------------|-------------------------------------------------|---------------------|
 || $coreModuleName       | &#10003; $scalaPublishVersionsShortString | &#10003; 0.6 ($scalaPublishVersionsShortString) | &#10003; 0.3 (2.11) |
 || $enumeratumModuleName | &#10003; $scalaPublishVersionsShortString | &#10003; 0.6 ($scalaPublishVersionsShortString) | &#65794;            |
 || $genericModuleName    | &#10003; $scalaPublishVersionsShortString | &#10003; 0.6 ($scalaPublishVersionsShortString) | &#65794;            |
 || $refinedModuleName    | &#10003; $scalaPublishVersionsShortString | &#10003; 0.6 ($scalaPublishVersionsShortString) | &#65794;            |
 || $spireModuleName      | &#10003; $scalaPublishVersionsShortString | &#10003; 0.6 ($scalaPublishVersionsShortString) | &#65794;            |
 || $squantsModuleName    | &#10003; $scalaPublishVersionsShortString | &#10003; 0.6 ($scalaPublishVersionsShortString) | &#65794;            |
 |
 |Binary compatibility for the library is guaranteed between minor versions.  
 |For example, `$minorVersion.x` is binary compatible with `$minorVersion.y` for any `x` and `y`.
 """.stripMargin.trim
)
```

The only required module is `ciris-core`, the rest are optional library integrations.  
For an explanation of how to use the modules, see the [Modules Overview](https://cir.is/docs/modules) section.

- The `ciris-enumeratum` module allows loading [enumeratum][enumeratum] enumerations.
- The `ciris-generic` module allows loading more types with [shapeless][shapeless].
- The `ciris-refined` module allows loading [refined][refined] refinement types.
- The `ciris-spire` module allows loading [spire][spire] number types.
- The `ciris-squants` module allows loading [squants][squants] data types.

If you're using `ciris-generic` with Scala 2.10, you'll need to include the [Macro Paradise](http://docs.scala-lang.org/overviews/macros/paradise.html) compiler plugin.

```scala
libraryDependencies += compilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.patch)
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
```tut:evaluated
println(
s"""
 |import $$ivy.`$organization::$coreModuleName:$latestVersion`, ciris._, ciris.syntax._
 |import $$ivy.`$organization::$enumeratumModuleName:$latestVersion`, ciris.enumeratum._
 |import $$ivy.`$organization::$genericModuleName:$latestVersion`, ciris.generic._
 |import $$ivy.`$organization::$refinedModuleName:$latestVersion`, ciris.refined._, ciris.refined.syntax._
 |import $$ivy.`$organization::$spireModuleName:$latestVersion`, ciris.spire._
 |import $$ivy.`$organization::$squantsModuleName:$latestVersion`, ciris.squants._
 """.stripMargin.trim
)
```

#### <a name="external-libraries" href="#external-libraries">External Libraries</a>
Below is an incomplete list of third-party libraries that integrate with Ciris.  
If your library is not included in the list, then please open a pull request.

* [`ciris-aws-ssm`](https://github.com/ovotech/ciris-aws-ssm) for [Amazon EC2 Systems Manager (SSM)](https://aws.amazon.com/ec2/systems-manager) parameter store as configuration source.
* [`ciris-credstash`](https://github.com/ovotech/ciris-credstash) for using [CredStash](https://github.com/fugue/credstash) as a configuration source.

### <a name="motivation" href="#motivation">Motivation</a>
When it takes little effort to change and release software, for example when employing [continuous deployment](https://www.agilealliance.org/glossary/continuous-deployment/) practices, writing your configurations in Scala can be a viable alternative to configuration files, in order to increase compile-time safety. Since configuration files are not validated at compile-time, any errors will occur at runtime. Tests and macros can be used to perform validation, but by simply using Scala as a configuration language, we ensure that the configuration is correct when compiling, thereby eliminating many potential runtime errors, without having to resort to macros.

For security reasons, it's desirable that secrets, like passwords, are not stored in the source code. For a Scala configuration, this means that the code containing your secrets should be stored in a different place, and later be compiled together with the rest of your application. If you require that your secrets shouldn't be persisted to disk, that might not be feasible. Alternatively, you can define most of your configuration in Scala and only load secrets, and other values which cannot reside in code, from the application's environment.

While it's possible to not use any libraries in the latter case, loading values from the environment typically means dealing with: different environments and configuration sources, type conversions, error handling, and validation. This is where Ciris comes in: a small library, dependency-free at its core, helping you to deal with all of that more easily.

### <a name="documentation" href="#documentation">Documentation</a>
For an overview, with examples and explanations of the most common use cases, please refer to the [usage guide](https://cir.is/docs/basics).  
If you're looking for a more detailed code-centric overview, you can instead take a look at the [API documentation](https://cir.is/api).

### <a name="participation" href="#participation">Participation</a>
Ciris embraces pure, typeful, idiomatic functional programming in Scala, and wants to provide a safe and friendly environment for teaching, learning, and contributing as described in the [Typelevel Code of Conduct](http://typelevel.org/conduct.html). It is expected that participants follow the code of conduct in all official channels, including on GitHub and in the Gitter chat room.

If you would like to be involved in building Ciris, check out the [contributing guide](https://cir.is/docs/contributing).

### <a name="license" href="#license">License</a>
Ciris is available under the MIT license, available at [https://opensource.org/licenses/MIT](https://opensource.org/licenses/MIT) and in the [license file](https://github.com/vlovgr/ciris/blob/master/license.txt).

[enumeratum]: https://github.com/lloydmeta/enumeratum
[refined]: https://github.com/fthomas/refined
[shapeless]: https://github.com/milessabin/shapeless
[spire]: https://github.com/non/spire
[squants]: https://github.com/typelevel/squants
[sbt]: http://www.scala-sbt.org
[scala]: http://www.scala-lang.org
[scalajs]: https://www.scala-js.org
[scalanative]: http://scala-native.org
