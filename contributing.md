# Contributing Guide
This guide is for people who would like to be involved in building Ciris.  
As a free and open source project, we gladly welcome contributions.

## Find something to work on
Looking for a way to help out? Take a look at the [open issues](https://github.com/vlovgr/ciris/issues). Choose an issue that looks interesting to you. If you are new to the project, the label [`low-hanging fruit`](https://github.com/vlovgr/ciris/labels/low-hanging%20fruit) can help you identify suitable issues to work on. Before you start working on an issue, make sure it's not already assigned to someone and that nobody else has left a comment saying they're working on it. Of course, you can also comment on an issue someone else is working on and offer to collaborate. If you're unsure where to start, you can always ask for help in the [Gitter](https://gitter.im/vlovgr/ciris) chat room.

Have an idea for something new? That's great! We recommend you make sure it belongs in Ciris before you put effort into creating a pull request. The preferred ways to do that are to either:

* [create an issue](https://github.com/vlovgr/ciris/issues/new) describing your idea, or
* ask for feedback in the [Gitter](https://gitter.im/vlovgr/ciris) chat room.

## Let us know you are working on it
If there is already an open issue for the task you're working on, leave a comment to let people know you're working on it. If there isn't already an issue and it's a non-trivial task, it's a good idea to create a new issue and put a note that you're working on it. This prevents contributors from duplicating effort.

## Build the project
First you'll need to checkout a local copy of the code base.
```
git clone https://github.com/vlovgr/ciris.git
```

To build the project, you'll need to have [sbt](https://www.scala-sbt.org) installed.

Run `sbt`, and then `validate` to ensure everything is setup correctly.  
Below is a list of some useful sbt commands to help you get started.

* `clean`: cleans any compiled output; to start clean again.
* `compile`: compiles the application source files.
* `docs/makeMicrosite`: generates the website (requires [Jekyll](https://jekyllrb.com)).
* `docs/tut`: compiles the documentation in [`docs`](https://github.com/vlovgr/ciris/tree/master/docs).
* `docs/unidoc`: generates the [Scaladoc](https://docs.scala-lang.org/style/scaladoc.html) API documentation.
* `docTests`: run tests to check API documentation code examples.
* `generateReadme`: generates the readme from the [index page](https://github.com/vlovgr/ciris/blob/master/docs/src/main/tut/index.md).
* `generateScripts`: generates the script files in the [`scripts`](https://github.com/vlovgr/ciris/tree/master/scripts) directory.
* `test`: run tests for the application sources.
* `validate`: compile and run the tests with code coverage enabled.
* `validateDocs`: run `docTests`, `docs/unidoc`, and `docs/tut` as described above.
* `validateNative`: run tests for Scala Native modules (requires setup for [Scala Native](http://www.scala-native.org)).

## Submit a pull request
Before you open a pull request, you should make sure that `sbt +validate` runs successfully. [Travis CI](https://travis-ci.org/vlovgr/ciris) will run this as well, but it may save you some time to be alerted to problems earlier on. Once Travis CI has run, [Codecov](https://codecov.io/gh/vlovgr/ciris) will check the code coverage and comment on your pull request with the results.

If your pull request addresses an existing issue, please include the issue number in the pull request message, or as part of your commit message. For example, if your pull request addresses issue number 52, then please include `fixes #52` in your pull request message, or in the commit message.

## Creating new modules
Following are some general guidelines when creating new modules.

- Make use of the `ciris` namespace, for example `ciris.vault` for a [Vault](https://www.vaultproject.io) module.
- Prefer dependencies which support pure, typeful, idiomatic functional programming.
- If there's not too much effort involved, try to support both Scala.js and Scala Native.

Following is a step-by-step guide on how to add a new module.

1. In `build.sbt`, define a new project `foo` which will be published as `ciris-foo`, similar to the existing ones.

   ```scala
   lazy val foo =
     crossProject(JSPlatform, JVMPlatform, NativePlatform)
       .in(file("modules/foo"))
       .settings(moduleName := "ciris-foo", name := "Ciris foo")
       .settings(scalaSettings)
       .settings(testSettings)
       .jsSettings(jsModuleSettings)
       .jvmSettings(jvmModuleSettings)
       .nativeSettings(nativeModuleSettings)
       .settings(releaseSettings)
       .dependsOn(core)

   lazy val fooJS = foo.js
   lazy val fooJVM = foo.jvm
   lazy val fooNative = foo.native
   ```

   If the module won't support Scala.js or Scala Native, remove the unsupported platforms from `crossProject`, along with `jsSettings` and `nativeSettings`, and the `fooJS` and `fooNative` projects, respectively.

   If the module has a dependency on another library, add the dependency version to the variables section:

    ```scala
    /* Variables */

    // ...

    lazy val fooDependencyVersion = "1.0.0"
    ```

   and include the dependency in the project definition (use `%%%` instead of `%%` for Scala.js or Scala Native).

    ```scala
    .settings(libraryDependencies += "com.foo" %%% "foo-dependency" % fooDependencyVersion)
    ```

2. In `build.sbt`, add the new projects `fooJS`, `fooJVM`, and `fooNative` to the aggregate `ciris` project.

   ```scala
   lazy val ciris = project
     .in(file("."))
     .settings(moduleName := "ciris", name := "Ciris")
     // ...
     .aggregate(
       catsJS, catsJVM,
       catsEffectJS, catsEffectJVM,
       coreJS, coreJVM, coreNative,
       enumeratumJS, enumeratumJVM,
       fooJS, fooJVM, fooNative,
       // ...
     )
   ```

3. In `build.sbt`, add the module name to the `moduleNames` list.

   ```scala
   lazy val moduleNames = List(
     "cats",
     "catsEffect",
     "core",
     "foo",
     // ...
   )
   ```

4. In `build.sbt`, if the module does not support Scala.js, remove it from `jsModuleNames`.

   ```scala
   lazy val jsModuleNames =
     moduleNames.map(_ + "JS")
       .filter(_ != "fooJS")
   ```

5. In `build.sbt`, if the module supports Scala Native, add it to `nativeModuleNames`.

   ```scala
   lazy val nativeModuleNames = List(
     "core",
     "foo",
     // ...
   ).map(_ + "Native")
   ```

6. In `build.sbt`, add the new projects to `crossModules`, using `None` if Scala.js or Scala Native is unsupported.

   ```scala
   lazy val crossModules: Seq[(Project, Option[Project], Option[Project])] =
     Seq(
       (catsJVM, Some(catsJS), None),
       (catsEffectJVM, Some(catsEffectJS), None),
       (coreJVM, Some(coreJS), Some(coreNative)),
       (fooJVM, Some(fooJS), Some(fooNative)),
       // ...
     )
   ```

7. In `build.sbt`, for the `docs` project, add `BuildInfoKey`s for the `moduleName` and `crossScalaVersions`.

   ```scala
   buildInfoKeys := Seq[BuildInfoKey](
     // ...
     BuildInfoKey.map(moduleName in fooJVM) { case (k, v) => "foo" + k.capitalize -> v },
     // ...
     BuildInfoKey.map(crossScalaVersions in fooJVM) { case (k, v) => "fooJvm" + k.capitalize -> v },
     BuildInfoKey.map(crossScalaVersions in fooJS) { case (k, v) => "fooJs" + k.capitalize -> v },
     BuildInfoKey.map(crossScalaVersions in fooNative) { case (k, v) => "fooNative" + k.capitalize -> v },
     // ...
   )
   ```

   You can omit `crossScalaVersions` for `fooJS` and `fooNative` if Scala.js or Scala Native is unsupported.

8. In `build.sbt`, also in the `docs` project, locate `generateApiIndexFile` and include an entry for the module.

9. In `build.sbt`, for the `docs` project, include the module in `dependsOn` so that documentation is generated.

   ```scala
   .dependsOn(
     catsJVM, catsEffectJVM, coreJVM, enumeratumJVM, fooJVM, // ...
   )
   ```

10. In `build.sbt`, for the `tests` project, include the module in `dependsOn`, so tests can use the module.

    ```scala
    .dependsOn(
      cats, catsEffect, core, enumeratum, foo // ...
    )
    ```

11. In `build.sbt`, in `generateScripts`, add the module as a dependency, and include the module imports.

12. In `latestVersion.sbt`, add the module name to `unreleasedModuleNames`.

    ```scala
    unreleasedModuleNames in ThisBuild := Set("ciris-foo")
    ```

13. In `docs/src/main/tut/index.md`, include the module in the `Getting Started` and `Ammonite` sections.

14. Documentation for the usage guide can be added to `docs/src/main/tut/docs`, similar to existing modules.

At this point, everything should be setup, and module sources go into the `module/foo/{js,jvm,native,shared}` directories. Tests for the module go into the shared `tests` project, and the `tests/{js,jvm}` directories.

## Grant of license
Ciris is licensed under the [MIT License](https://opensource.org/licenses/MIT). Opening a pull request signifies your consent to license your contributions under this license.

## Code of conduct
Ciris supports the [Typelevel Code of Conduct](https://typelevel.org/conduct.html). To provide a safe and friendly environment for teaching, learning, and contributing, it is expected that participants follow the code of conduct in all official channels, including on GitHub and in the Gitter chat room.

## Credits
This document is heavily based on the [http4s contributing guide](http://http4s.org/contributing).
