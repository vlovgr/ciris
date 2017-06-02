[![Travis](https://img.shields.io/travis/vlovgr/ciris/master.svg)](https://travis-ci.org/vlovgr/ciris) [![Codecov](https://img.shields.io/codecov/c/github/vlovgr/ciris.svg)](https://codecov.io/gh/vlovgr/ciris) [![Gitter](https://img.shields.io/gitter/room/vlovgr/ciris.svg?colorB=36bc97)](https://gitter.im/vlovgr/ciris) [![Version](https://img.shields.io/maven-central/v/is.cir/ciris-core_2.12.svg?color=blue&label=version)](https://index.scala-lang.org/vlovgr/ciris) [![Documentation](https://img.shields.io/maven-central/v/is.cir/ciris-core_2.12.svg?color=blue&label=docs)](https://cir.is/api)

## Ciris
Lightweight, extensible, and validated configuration loading in [Scala][scala] and [Scala.js][scalajs].  
The core library is dependency-free, while modules provide library integrations.

The name comes from being an abbreviation of the word _configurations_.  
Ciris' logo was inspired by the epyllion Ciris from [Appendix Vergiliana](https://en.wikipedia.org/wiki/Appendix_Vergiliana#Ciris_.28.22The_Sea-Bird.22.29).

> The Ciris is an epyllion in 541 hexameters describing the myth of Nisus, the king of Megara and his daughter Scylla.<br/>
> After the city falls and Scylla is taken prisoner on the Cretan ships, Amphitrite transforms her into the _ciris_ seabird.

Ciris is a new project under active development. Feedback and contributions are welcome.

### Introduction
Ciris encourages compile-time safety by defining as much as possible of your configurations in Scala. For the data which cannot reside in code, Ciris helps you to load and decode values, while dealing with errors. Validation is encoded by using appropriate data types, with available integrations to libraries such as [refined][refined] and [squants][squants] to support even more types. Configurations are typically modeled as case class hierarchies, but you are free to choose the model you see fit.

### Getting Started
To get started with [SBT][sbt], simply add the following lines to your `build.sbt` file:

```
// Libraries are published for Scala 2.10, 2.11, 2.12
libraryDependencies ++= Seq(
  "is.cir" %% "ciris-core" % "0.3.2",
  "is.cir" %% "ciris-enumeratum" % "0.3.2",
  "is.cir" %% "ciris-generic" % "0.3.2",
  "is.cir" %% "ciris-refined" % "0.3.2",
  "is.cir" %% "ciris-squants" % "0.3.2"
)
```

and make sure to replace `%%` with `%%%` if you are using Scala.js.  
For changes between versions, please refer to the [release notes](https://github.com/vlovgr/ciris/releases).

The only required module is `ciris-core`, the rest are optional library integrations.

- The `ciris-enumeratum` module allows loading [enumeratum][enumeratum] enumerations.
- The `ciris-generic` module allows loading more types with [shapeless][shapeless].
- The `ciris-refined` module allows loading [refined][refined] refinement types.
- The `ciris-squants` module allows loading [squants][squants] data types.

The [Modules Overview](https://cir.is/docs/modules) section explains how to use these modules.

If you're using `ciris-generic` with Scala 2.10, you'll need to include the [Macro Paradise](http://docs.scala-lang.org/overviews/macros/paradise.html) compiler plugin.

```
libraryDependencies += compilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.patch)
```

#### Ammonite
To start an [Ammonite REPL](http://www.lihaoyi.com/Ammonite/#Ammonite-REPL) with Ciris loaded and imported, simply run the following.
```
curl -Ls try.cir.is | sh
```
You will need to have a JDK installed. The [script](https://try.cir.is) will then:
* download and install the latest available version of [coursier](https://github.com/coursier/coursier),
* use coursier to fetch Ammonite and the latest Ciris version,
* start an Ammonite REPL with Ciris already imported.

If you already have the Ammonite REPL installed, you can load Ciris using the following commands.
```
import $ivy.`is.cir::ciris-core:0.3.2`, ciris._
import $ivy.`is.cir::ciris-enumeratum:0.3.2`, ciris.enumeratum._
import $ivy.`is.cir::ciris-generic:0.3.2`, ciris.generic._
import $ivy.`is.cir::ciris-refined:0.3.2`, ciris.refined._
import $ivy.`is.cir::ciris-squants:0.3.2`, ciris.squants._
```

### Motivation
When it takes little effort to change and release software, for example when employing [continuous deployment](https://www.agilealliance.org/glossary/continuous-deployment/) practices, writing your configurations in Scala can be a viable alternative to configuration files, in order to increase compile-time safety. Since configuration files are not validated at compile-time, any errors will occur at runtime. Tests and macros can be used to perform validation, but by simply using Scala as a configuration language, we ensure that the configuration is correct when compiling, thereby eliminating many potential runtime errors, without having to resort to macros.

For security reasons, it's desirable that secrets, like passwords, are not stored in the source code. For a Scala configuration, this means that the code containing your secrets should be stored in a different place, and later be compiled together with the rest of your application. If you require that your secrets shouldn't be persisted to disk, that might not be feasible. Alternatively, you can define most of your configuration in Scala and only load secrets, and other values which cannot reside in code, from the application's environment.

While it's possible to not use any libraries in the latter case, loading values from the environment typically means dealing with: different environments and configuration sources, type conversions, error handling, and validation. This is where Ciris comes in: a small library, dependency-free at its core, helping you to deal with all of that more easily.

### Documentation
For an overview, with examples and explanations of the most common use cases, please refer to the [usage guide](https://cir.is/docs/basics).  
If you're looking for a more detailed code-centric overview, you can instead take a look at the [API documentation](https://cir.is/api).

### Participation
Ciris embraces pure, typeful, idiomatic functional programming in Scala, and wants to provide a safe and friendly environment for teaching, learning, and contributing as described in the [Typelevel Code of Conduct](http://typelevel.org/conduct.html). It is expected that participants follow the code of conduct in all official channels, including on GitHub and in the Gitter chat room.

### License
Ciris is available under the MIT license, available at [https://opensource.org/licenses/MIT](https://opensource.org/licenses/MIT) and in the [license file](https://github.com/vlovgr/ciris/blob/master/license.txt).

[enumeratum]: https://github.com/lloydmeta/enumeratum
[refined]: https://github.com/fthomas/refined
[shapeless]: https://github.com/milessabin/shapeless
[squants]: https://github.com/typelevel/squants
[sbt]: http://www.scala-sbt.org
[scala]: http://www.scala-lang.org
[scalajs]: https://www.scala-js.org
