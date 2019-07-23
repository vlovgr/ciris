import sbtcrossproject.{crossProject, CrossType}
import com.typesafe.sbt.SbtGit.GitKeys._
import ReleaseTransformations._

/* Variables */

lazy val scala211 = "2.11.12"
lazy val scala212 = "2.12.8"

lazy val catsEffectVersion = "1.3.0"
lazy val catsVersion = "1.6.1"
lazy val commonsCodecVersion = "1.13"
lazy val enumeratumVersion = "1.5.13"
lazy val kindProjectorVersion = "0.9.10"
lazy val kittensVersion = "1.1.0"
lazy val macroParadiseVerison = "2.1.1"
lazy val refinedVersion = "0.9.8"
lazy val scalaCheckVersion = "1.14.0"
lazy val scalaTestVersion = "3.0.8"
lazy val shapelessVersion = "2.3.3"
lazy val spireVersion = "0.16.2"
lazy val squantsVersion = "1.4.0"

lazy val scriptsDirectory = "scripts"

/* Module Setup */

lazy val moduleNames = List(
  "cats",
  "catsEffect",
  "core",
  "enumeratum",
  "generic",
  "refined",
  "spire",
  "squants"
)

lazy val jsModuleNames = moduleNames.map(_ + "JS")

lazy val jvmModuleNames = moduleNames.map(_ + "JVM")

lazy val nativeModuleNames = List(
  "core",
  "generic",
  "refined",
  "squants"
).map(_ + "Native")

lazy val allModuleNames = jsModuleNames ++ jvmModuleNames ++ nativeModuleNames

lazy val crossModules: Seq[(Project, Option[Project], Option[Project])] =
  Seq(
    (catsJVM, Some(catsJS), None),
    (catsEffectJVM, Some(catsEffectJS), None),
    (coreJVM, Some(coreJS), Some(coreNative)),
    (enumeratumJVM, Some(enumeratumJS), None),
    (genericJVM, Some(genericJS), Some(genericNative)),
    (refinedJVM, Some(refinedJS), Some(refinedNative)),
    (spireJVM, Some(spireJS), None),
    (squantsJVM, Some(squantsJS), Some(squantsNative))
  )

lazy val noDocumentationModules: Seq[ProjectReference] = {
  val nonJvmModules =
    crossModules.flatMap {
      case (_, js, native) =>
        js ++ native
    }

  val testModules = Seq(testsJVM, testsJS)

  (nonJvmModules ++ testModules)
    .map(module => module: ProjectReference)
}

/* Module Definitions */

lazy val ciris = project
  .in(file("."))
  .settings(moduleName := "ciris", name := "Ciris")
  .settings(scalaSettings)
  .settings(noPublishSettings)
  .settings(releaseSettings)
  .settings(testSettings)
  .settings(
    console := (console in (coreJVM, Compile)).value,
    console in Test := (console in (coreJVM, Test)).value
  )
  .aggregate(
    catsJS, catsJVM,
    catsEffectJS, catsEffectJVM,
    coreJS, coreJVM, coreNative,
    enumeratumJS, enumeratumJVM,
    genericJS, genericJVM, genericNative,
    refinedJS, refinedJVM, refinedNative,
    spireJS, spireJVM,
    squantsJS, squantsJVM,
    testsJS, testsJVM
  )

lazy val cats =
  crossProject(JSPlatform, JVMPlatform)
    .in(file("modules/cats"))
    .settings(moduleName := "ciris-cats", name := "Ciris cats")
    .settings(libraryDependencies += "org.typelevel" %%% "cats-core" % catsVersion)
    .settings(scalaSettings)
    .settings(testSettings)
    .jsSettings(jsModuleSettings)
    .jvmSettings(jvmModuleSettings)
    .settings(releaseSettings)
    .dependsOn(core)

lazy val catsJS = cats.js
lazy val catsJVM = cats.jvm

lazy val catsEffect =
  crossProject(JSPlatform, JVMPlatform)
    .in(file("modules/cats-effect"))
    .settings(moduleName := "ciris-cats-effect", name := "Ciris cats effect")
    .settings(libraryDependencies += "org.typelevel" %%% "cats-effect" % catsEffectVersion)
    .settings(scalaSettings)
    .settings(testSettings)
    .jsSettings(jsModuleSettings)
    .jvmSettings(jvmModuleSettings)
    .settings(releaseSettings)
    .settings(kindProjectorSettings)
    .dependsOn(core, cats)

lazy val catsEffectJS = catsEffect.js
lazy val catsEffectJVM = catsEffect.jvm

lazy val core =
  crossProject(JSPlatform, JVMPlatform, NativePlatform)
    .in(file("modules/core"))
    .settings(moduleName := "ciris-core", name := "Ciris core")
    .settings(scalaSettings)
    .settings(testSettings)
    .jsSettings(jsModuleSettings)
    .jvmSettings(jvmModuleSettings)
    .nativeSettings(nativeModuleSettings)
    .settings(releaseSettings)

lazy val coreJS = core.js
lazy val coreJVM = core.jvm
lazy val coreNative = core.native

lazy val enumeratum =
  crossProject(JSPlatform, JVMPlatform)
    .in(file("modules/enumeratum"))
    .settings(moduleName := "ciris-enumeratum", name := "Ciris enumeratum")
    .settings(libraryDependencies += "com.beachape" %%% "enumeratum" % enumeratumVersion)
    .settings(scalaSettings)
    .settings(testSettings)
    .jsSettings(jsModuleSettings)
    .jvmSettings(jvmModuleSettings)
    .settings(releaseSettings)
    .dependsOn(core)

lazy val enumeratumJS = enumeratum.js
lazy val enumeratumJVM = enumeratum.jvm

lazy val generic =
  crossProject(JSPlatform, JVMPlatform, NativePlatform)
    .in(file("modules/generic"))
    .settings(moduleName := "ciris-generic", name := "Ciris generic")
    .settings(libraryDependencies += "com.chuusai" %%% "shapeless" % shapelessVersion)
    .settings(scalaSettings)
    .settings(testSettings)
    .jsSettings(jsModuleSettings)
    .jvmSettings(jvmModuleSettings)
    .nativeSettings(nativeModuleSettings)
    .settings(releaseSettings)
    .dependsOn(core)

lazy val genericJS = generic.js
lazy val genericJVM = generic.jvm
lazy val genericNative = generic.native

lazy val refined =
  crossProject(JSPlatform, JVMPlatform, NativePlatform)
    .in(file("modules/refined"))
    .settings(moduleName := "ciris-refined", name := "Ciris refined")
    .settings(libraryDependencies += "eu.timepit" %%% "refined" % refinedVersion)
    .settings(scalaSettings)
    .settings(testSettings)
    .jsSettings(jsModuleSettings)
    .jvmSettings(jvmModuleSettings)
    .nativeSettings(nativeModuleSettings)
    .settings(releaseSettings)
    .dependsOn(core)

lazy val refinedJS = refined.js
lazy val refinedJVM = refined.jvm
lazy val refinedNative = refined.native

lazy val spire =
  crossProject(JSPlatform, JVMPlatform)
    .in(file("modules/spire"))
    .settings(moduleName := "ciris-spire", name := "Ciris spire")
    .settings(libraryDependencies += "org.typelevel" %%% "spire" % spireVersion)
    .settings(scalaSettings)
    .settings(testSettings)
    .jsSettings(jsModuleSettings)
    .jvmSettings(jvmModuleSettings)
    .settings(releaseSettings)
    .dependsOn(core)

lazy val spireJS = spire.js
lazy val spireJVM = spire.jvm

lazy val squants =
  crossProject(JSPlatform, JVMPlatform, NativePlatform)
    .in(file("modules/squants"))
    .settings(moduleName := "ciris-squants", name := "Ciris squants")
    .settings(libraryDependencies += "org.typelevel" %%% "squants" % squantsVersion)
    .settings(scalaSettings)
    .settings(testSettings)
    .jsSettings(jsModuleSettings)
    .jvmSettings(jvmModuleSettings)
    .nativeSettings(nativeModuleSettings)
    .settings(releaseSettings)
    .dependsOn(core)

lazy val squantsJS = squants.js
lazy val squantsJVM = squants.jvm
lazy val squantsNative = squants.native

lazy val tests =
  crossProject(JSPlatform, JVMPlatform)
    .in(file("tests"))
    .settings(moduleName := "ciris-tests", name := "Ciris tests")
    .settings(libraryDependencies += "commons-codec" % "commons-codec" % commonsCodecVersion % Test)
    .settings(libraryDependencies += compilerPlugin("org.scalamacros" % "paradise" % macroParadiseVerison % Test cross CrossVersion.patch))
    .settings(scalaSettings)
    .settings(noPublishSettings)
    .settings(testSettings)
    .jsSettings(jsModuleSettings)
    .dependsOn(cats, catsEffect, core, enumeratum, generic, refined, spire, squants)

lazy val testsJS = tests.js
lazy val testsJVM = tests.jvm

lazy val docs = project
  .in(file("docs"))
  .settings(moduleName := "ciris-docs", name := "Ciris docs")
  .settings(scalaSettings)
  .settings(testSettings)
  .settings(noPublishSettings)
  .settings(kindProjectorSettings)
  .settings(
    micrositeName := "Ciris",
    micrositeDescription := "Lightweight, extensible, and validated configuration loading in Scala",
    micrositeDocumentationLabelDescription := "API Documentation",
    micrositeDocumentationUrl := "api",
    micrositeKazariEnabled := false,
    micrositeShareOnSocial := false,
    micrositeAuthor := "Viktor Lövgren",
    micrositeOrganizationHomepage := "https://vlovgr.se",
    micrositeGithubOwner := "vlovgr",
    micrositeGithubRepo := "ciris",
    micrositeTwitterCreator := "@vlovgr",
    micrositeHighlightTheme := "atom-one-light",
    micrositePalette := Map(
      "brand-primary" -> "#3e4959",
      "brand-secondary" -> "#3e4959",
      "brand-tertiary" -> "#3e4959",
      "gray-dark" -> "#3e4959",
      "gray" -> "#837f84",
      "gray-light" -> "#e3e2e3",
      "gray-lighter" -> "#f4f3f4",
      "white-color" -> "#f3f3f3"
    )
  )
  .settings(
    buildInfoObject := "info",
    buildInfoPackage := "ciris.build",
    buildInfoKeys := Seq[BuildInfoKey](
      organization,
      latestVersion in ThisBuild,
      crossScalaVersions in coreJVM,
      BuildInfoKey.map(moduleName in catsJVM) { case (k, v) => "cats" + k.capitalize -> v },
      BuildInfoKey.map(moduleName in catsEffectJVM) { case (k, v) => "catsEffect" + k.capitalize -> v },
      BuildInfoKey.map(moduleName in coreJVM) { case (k, v) => "core" + k.capitalize -> v },
      BuildInfoKey.map(moduleName in enumeratumJVM) { case (k, v) => "enumeratum" + k.capitalize -> v },
      BuildInfoKey.map(moduleName in genericJVM) { case (k, v) => "generic" + k.capitalize -> v },
      BuildInfoKey.map(moduleName in refinedJVM) { case (k, v) => "refined" + k.capitalize -> v },
      BuildInfoKey.map(moduleName in spireJVM) { case (k, v) => "spire" + k.capitalize -> v },
      BuildInfoKey.map(moduleName in squantsJVM) { case (k, v) => "squants" + k.capitalize -> v },
      BuildInfoKey.map(crossScalaVersions in catsJVM) { case (k, v) => "catsJvm" + k.capitalize -> v },
      BuildInfoKey.map(crossScalaVersions in catsJS) { case (k, v) => "catsJs" + k.capitalize -> v },
      BuildInfoKey.map(crossScalaVersions in catsEffectJVM) { case (k, v) => "catsEffectJvm" + k.capitalize -> v },
      BuildInfoKey.map(crossScalaVersions in catsEffectJS) { case (k, v) => "catsEffectJs" + k.capitalize -> v },
      BuildInfoKey.map(crossScalaVersions in coreJVM) { case (k, v) => "coreJvm" + k.capitalize -> v },
      BuildInfoKey.map(crossScalaVersions in coreJS) { case (k, v) => "coreJs" + k.capitalize -> v },
      BuildInfoKey.map(crossScalaVersions in coreNative) { case (k, v) => "coreNative" + k.capitalize -> v },
      BuildInfoKey.map(crossScalaVersions in enumeratumJVM) { case (k, v) => "enumeratumJvm" + k.capitalize -> v },
      BuildInfoKey.map(crossScalaVersions in enumeratumJS) { case (k, v) => "enumeratumJs" + k.capitalize -> v },
      BuildInfoKey.map(crossScalaVersions in genericJVM) { case (k, v) => "genericJvm" + k.capitalize -> v },
      BuildInfoKey.map(crossScalaVersions in genericJS) { case (k, v) => "genericJs" + k.capitalize -> v },
      BuildInfoKey.map(crossScalaVersions in genericNative) { case (k, v) => "genericNative" + k.capitalize -> v },
      BuildInfoKey.map(crossScalaVersions in refinedJVM) { case (k, v) => "refinedJvm" + k.capitalize -> v },
      BuildInfoKey.map(crossScalaVersions in refinedJS) { case (k, v) => "refinedJs" + k.capitalize -> v },
      BuildInfoKey.map(crossScalaVersions in refinedNative) { case (k, v) => "refinedNative" + k.capitalize -> v },
      BuildInfoKey.map(crossScalaVersions in spireJVM) { case (k, v) => "spireJvm" + k.capitalize -> v },
      BuildInfoKey.map(crossScalaVersions in spireJS) { case (k, v) => "spireJs" + k.capitalize -> v },
      BuildInfoKey.map(crossScalaVersions in squantsJVM) { case (k, v) => "squantsJvm" + k.capitalize -> v },
      BuildInfoKey.map(crossScalaVersions in squantsJS) { case (k, v) => "squantsJs" + k.capitalize -> v },
      BuildInfoKey.map(crossScalaVersions in squantsNative) { case (k, v) => "squantsNative" + k.capitalize -> v },
      BuildInfoKey("catsVersion" -> catsVersion),
      BuildInfoKey("catsEffectVersion" -> catsEffectVersion),
      BuildInfoKey("enumeratumVersion" -> enumeratumVersion),
      BuildInfoKey("refinedVersion" -> refinedVersion),
      BuildInfoKey("shapelessVersion" -> shapelessVersion),
      BuildInfoKey("spireVersion" -> spireVersion),
      BuildInfoKey("squantsVersion" -> squantsVersion)
    )
  )
  .settings(
    generateApiIndexFile := {
      val target = resourceManaged.value / "api.txt"
      val version = (latestVersion in ThisBuild).value
      val scalaTargetVersion = scalaVersion.value.split('.').init.mkString(".")

      val content =
        s"""
          |This is the API documentation for [[https://cir.is Ciris]]: lightweight, extensible, and validated configuration loading in Scala.<br>
          |The documentation is kept up-to-date with new releases, currently documenting release [[https://github.com/vlovgr/ciris/releases/tag/v$version v$version]] on Scala $scalaTargetVersion.<br>
          |Please note that the documentation targets the JVM, and there may be differences on Scala.js and Scala Native.
          |
          |Ciris is divided into the following set of modules.
          |
          | - The [[ciris.cats cats]] module integrates with [[https://github.com/typelevel/cats cats]] for type classes and type class instances.
          | - The [[ciris.cats.effect cats-effect]] module integrates with [[https://github.com/typelevel/cats-effect cats-effect]] for type classes for effect types.
          | - The [[ciris core]] module provides basic functionality and support for standard library types.
          | - The [[ciris.enumeratum enumeratum]] module integrates with [[https://github.com/lloydmeta/enumeratum enumeratum]] to be able to read enumerations.
          | - The [[ciris.generic generic]] module uses [[https://github.com/milessabin/shapeless shapeless]] to be able to read products and coproducts.
          | - The [[ciris.refined refined]] module integrates with [[https://github.com/fthomas/refined refined]] to be able to read refinement types.
          | - The [[ciris.spire spire]] module integrates with [[https://github.com/non/spire spire]] to be able to read more number types.
          | - The [[ciris.squants squants]] module integrates with [[https://github.com/typelevel/squants squants]] to read values with unit of measure.
          |
          |If you're looking for usage instructions, please refer to the [[https://cir.is/docs usage guide]].
        """.stripMargin.trim

      IO.write(target, content)
      target
    },
    libraryDependencies ++= Seq(
      "org.typelevel" %% "kittens" % kittensVersion,
      "eu.timepit" %% "refined-cats" % refinedVersion
    ),
    crossScalaVersions := Seq(scalaVersion.value),
    scalacOptions --= Seq("-Xlint", "-Ywarn-unused", "-Ywarn-unused-import"),
    scalacOptions += "-Ypartial-unification",
    unidocProjectFilter in (ScalaUnidoc, unidoc) := inAnyProject -- inProjects(noDocumentationModules: _*),
    siteSubdirName in ScalaUnidoc := micrositeDocumentationUrl.value,
    addMappingsToSiteDir(mappings in (ScalaUnidoc, packageDoc), siteSubdirName in ScalaUnidoc),
    gitRemoteRepo := "git@github.com:vlovgr/ciris.git",
    scalacOptions in (ScalaUnidoc, unidoc) ++= Seq(
      "-skip-packages", buildInfoPackage.value,
      "-doc-source-url", s"https://github.com/vlovgr/ciris/tree/v${(latestVersion in ThisBuild).value}€{FILE_PATH}.scala",
      "-sourcepath", baseDirectory.in(LocalRootProject).value.getAbsolutePath,
      "-doc-root-content", (generateApiIndexFile.value).getAbsolutePath
    )
  )
  .dependsOn(catsJVM, catsEffectJVM, coreJVM, enumeratumJVM, genericJVM, refinedJVM, spireJVM, squantsJVM)
  .enablePlugins(BuildInfoPlugin, MicrositesPlugin, ScalaUnidocPlugin)

/* Settings */

lazy val scalaSettings = Seq(
  scalaVersion := scala212,
  crossScalaVersions := Seq(scala211, scala212),
  scalacOptions ++= Seq(
    "-deprecation",
    "-encoding", "UTF-8",
    "-target:jvm-1.8",
    "-feature",
    "-language:existentials",
    "-language:higherKinds",
    "-language:implicitConversions",
    "-language:postfixOps",
    "-unchecked",
    "-Xfatal-warnings",
    "-Xlint",
    "-Yno-adapted-args",
    "-Ywarn-dead-code",
    "-Ywarn-numeric-widen",
    "-Ywarn-value-discard",
    "-Xfuture",
    "-Ywarn-unused-import",
    "-Ywarn-unused"
  ).filter {
    case "-Ywarn-unused" if !(scalaVersion.value startsWith "2.12") => false
    case _ => true
  },
  scalacOptions in (Compile, console) --= Seq("-Xlint", "-Ywarn-unused", "-Ywarn-unused-import"),
  scalacOptions in (Test, console) := (scalacOptions in (Compile, console)).value,
  makePomConfiguration := makePomConfiguration.value.withConfigurations(Configurations.defaultMavenConfigurations)
)

lazy val metadataSettings = Seq(
  name := "Ciris",
  organization := "is.cir",
  organizationName := "Ciris",
  organizationHomepage := Some(url("https://cir.is"))
)

lazy val releaseSettings =
  metadataSettings ++ Seq(
    homepage := organizationHomepage.value,
    publishMavenStyle := true,
    publishArtifact in Test := false,
    useGpg := false,
    pomIncludeRepository := { _ => false },
    autoAPIMappings := true,
    apiURL := Some(url("https://cir.is/api")),
    licenses := Seq("MIT License" -> url("http://www.opensource.org/licenses/mit-license.php")),
    scmInfo := Some(
      ScmInfo(
        url("https://github.com/vlovgr/ciris"),
        "scm:git@github.com:vlovgr/ciris.git"
      )
    ),
    developers := List(
      Developer(
        id = "vlovgr",
        name = "Viktor Lövgren",
        email = "github@vlovgr.se",
        url = url("https://vlovgr.se")
      )
    ),
    publishTo :=  sonatypePublishTo.value,
    releaseCrossBuild := false, // See https://github.com/sbt/sbt-release/issues/214
    releaseTagName := s"v${(version in ThisBuild).value}",
    releaseTagComment := s"Release version ${(version in ThisBuild).value}",
    releaseCommitMessage := s"Set version to ${(version in ThisBuild).value}",
    releasePublishArtifactsAction := PgpKeys.publishSigned.value,
    releaseUseGlobalVersion := true,
    releaseProcess := Seq[ReleaseStep](
      checkSnapshotDependencies,
      inquireVersions,
      runClean,
      releaseStepCommandAndRemaining("+compile"),
      setReleaseVersion,
      setLatestVersion,
      releaseStepTask(updateReadme in ThisBuild),
      releaseStepTask(updateContributing in ThisBuild),
      releaseStepTask(updateScripts in ThisBuild),
      releaseStepTask(addDateToReleaseNotes in ThisBuild),
      commitReleaseVersion,
      tagRelease,
      releaseStepCommandAndRemaining("publishSignedAll"),
      releaseStepCommand("sonatypeRelease"),
      setNextVersion,
      commitNextVersion,
      pushChanges,
      releaseStepCommand("+docs/publishMicrosite")
    )
  )

lazy val testSettings = Seq(
  logBuffered in Test := false,
  parallelExecution in Test := false,
  testOptions in Test += Tests.Argument("-oDF"),
  scalacOptions in Test --= Seq("-Xlint", "-Ywarn-unused", "-Ywarn-unused-import"),
  doctestTestFramework := DoctestTestFramework.ScalaTest,
  libraryDependencies ++= Seq(
    "org.scalatest" %%% "scalatest" % scalaTestVersion % Test,
    "org.scalacheck" %%% "scalacheck" % scalaCheckVersion % Test
  )
)

lazy val mimaSettings = Seq(
  mimaPreviousArtifacts := {
    val released = !unreleasedModuleNames.value.contains(moduleName.value)
    val publishing = publishArtifact.value

    if(publishing && released)
      binaryCompatibleVersions.value
        .map(version => organization.value %% moduleName.value % version)
    else
      Set()
  },
  mimaBinaryIssueFilters ++= {
    import com.typesafe.tools.mima.core._
    // format: off
    Seq()
    // format: on
  }
)

lazy val kindProjectorSettings = Seq(
  addCompilerPlugin("org.spire-math" % "kind-projector" % kindProjectorVersion cross CrossVersion.binary)
)

lazy val jvmModuleSettings =
  mimaSettings ++ Seq(
    coverageExcludedPackages := List(
      "ciris.internal.digest.GeneralDigest"
    ).mkString(";")
  )

lazy val nonJvmModuleSettings = Seq(
  doctestGenTests := Seq.empty,
  coverageEnabled := false
)

lazy val jsModuleSettings =
  nonJvmModuleSettings

lazy val nativeModuleSettings =
  nonJvmModuleSettings ++ Seq(
    scalaVersion := scala211,
    crossScalaVersions := Seq(scala211),
    sources in (Compile, doc) := Seq.empty, // See https://github.com/scala-native/scala-native/issues/1121
    libraryDependencies --= Seq( // Does not support Scala Native yet
      "org.scalatest" %%% "scalatest" % scalaTestVersion % Test,
      "org.scalacheck" %%% "scalacheck" % scalaCheckVersion % Test
    )
  )

lazy val noPublishSettings =
  metadataSettings ++ Seq(
    skip in publish := true,
    publishArtifact := false
  )

lazy val sourceGeneratorSettings = Seq(
  sourceGenerators in Compile +=
    Def.task(generateSources(
      (sourceManaged in Compile).value,
      (sourceManaged in Test).value,
      "ciris"
    )).taskValue
)

/* Tasks */

val generateReadme = taskKey[File]("Generates the readme")
generateReadme in ThisBuild := {
  (tutOnly in docs).toTask(" index.md").value
  val source = IO.read((tutTargetDirectory in docs).value / "index.md")
  val readme =
    source
      .replaceAll("^\\s*---[^(---)]*---\\s*", "") // Remove metadata
  val target = (baseDirectory in ciris).value / "readme.md"
  IO.write(target, readme)
  target
}

val updateReadme = taskKey[Unit]("Generates and commits the readme")
updateReadme in ThisBuild := {
  (generateReadme in ThisBuild).value
  sbtrelease.Vcs.detect((baseDirectory in ciris).value).foreach { vcs =>
    vcs.add("readme.md").!
    vcs.commit("Update readme to latest version", sign = true).!
  }
}

val generateContributing = taskKey[File]("Generates the contributing guide")
generateContributing in ThisBuild := {
  (tutOnly in docs).toTask(" docs/contributing.md").value
  val source = IO.read((tutTargetDirectory in docs).value / "docs" / "contributing.md")
  val contributing =
    source
      .replaceAll("^\\s*---[^(---)]*---\\s*", "") // Remove metadata
  val target = (baseDirectory in ciris).value / "contributing.md"
  IO.write(target, contributing)
  target
}

val updateContributing = taskKey[Unit]("Generates and commits the contributing guide")
updateContributing in ThisBuild := {
  (generateContributing in ThisBuild).value
  sbtrelease.Vcs.detect((baseDirectory in ciris).value).foreach { vcs =>
    vcs.add("contributing.md").!
    vcs.commit("Update contributing guide to latest version", sign = true).!
  }
}

val generateScripts = taskKey[Unit]("Generates scripts")
generateScripts in ThisBuild := {
  val output = file(scriptsDirectory)
  val organizationId = (organization in coreJVM).value
  val moduleVersion = (latestVersion in ThisBuild).value

  def tryScript(
    extraCoursierArgs: Seq[String] = Seq.empty,
    extraPredefCode: Seq[String] = Seq.empty,
    ammoniteScalaVersion: String = "2.12.8"
  ) = {
    val coursierArgs =
      if(extraCoursierArgs.nonEmpty)
        "\n" + extraCoursierArgs.map(s => s"  $s \\").mkString("\n")
      else ""

    val predefCode =
      if(extraPredefCode.nonEmpty)
        "\n" + extraPredefCode.map(s => s"        $s;\\").mkString("\n")
      else ""

    s"""
       |#!/usr/bin/env sh
       |test -e ~/.coursier/coursier || ( \\
       |  mkdir -p ~/.coursier && \\
       |  curl -Lso ~/.coursier/coursier https://git.io/vgvpD && \\
       |  chmod +x ~/.coursier/coursier \\
       |)
       |
       |~/.coursier/coursier launch -q -P -M ammonite.Main \\
       |  com.lihaoyi:ammonite_$ammoniteScalaVersion:1.6.5 \\$coursierArgs
       |  $organizationId:${(moduleName in catsJVM).value}_2.12:$moduleVersion \\
       |  $organizationId:${(moduleName in catsEffectJVM).value}_2.12:$moduleVersion \\
       |  $organizationId:${(moduleName in coreJVM).value}_2.12:$moduleVersion \\
       |  $organizationId:${(moduleName in enumeratumJVM).value}_2.12:$moduleVersion \\
       |  $organizationId:${(moduleName in genericJVM).value}_2.12:$moduleVersion \\
       |  $organizationId:${(moduleName in refinedJVM).value}_2.12:$moduleVersion \\
       |  $organizationId:${(moduleName in spireJVM).value}_2.12:$moduleVersion \\
       |  $organizationId:${(moduleName in squantsJVM).value}_2.12:$moduleVersion \\
       |  -- --predef-code "\\$predefCode
       |        interp.configureCompiler(_.settings.YpartialUnification.value = true);\\
       |        import ciris.{cats => _, enumeratum => _, spire => _, squants => _, _},\\
       |        ciris.cats._,\\
       |        ciris.cats.effect._,\\
       |        ciris.cats.effect.syntax._,\\
       |        ciris.enumeratum._,\\
       |        ciris.refined._,\\
       |        ciris.refined.syntax._,\\
       |        ciris.spire._,\\
       |        ciris.squants._\\
       |      " < /dev/tty
     """.stripMargin.trim + "\n"
  }

  IO.createDirectory(output)
  IO.write(output / "try.sh", tryScript())
  IO.write(output / "try.typelevel.sh", tryScript(
    extraCoursierArgs = Seq(
      "-E org.scala-lang:scala-library",
      "-E org.scala-lang:scala-compiler",
      "-E org.scala-lang:scala-reflect",
      "org.typelevel:scala-compiler:2.12.4-bin-typelevel-4",
      "org.typelevel:scala-library:2.12.4-bin-typelevel-4",
      "org.typelevel:scala-reflect:2.12.4-bin-typelevel-4"
    ),
    extraPredefCode = Seq(
      "interp.configureCompiler(_.settings.YliteralTypes.value = true)"
    ),
    ammoniteScalaVersion = "2.12.4"
  ))
}

val updateScripts = taskKey[Unit]("Generates and commits scripts")
updateScripts in ThisBuild := {
  (generateScripts in ThisBuild).value
  sbtrelease.Vcs.detect((baseDirectory in ciris).value).foreach { vcs =>
    vcs.add(scriptsDirectory).!
    vcs.commit("Update scripts to latest version", sign = true).!
  }
}

val generateApiIndexFile = taskKey[File]("Generates the API index file")

val releaseNotesFile = taskKey[File]("Release notes for current version")
releaseNotesFile in ThisBuild := {
  val currentVersion = (version in ThisBuild).value
  file("notes") / s"$currentVersion.markdown"
}

val ensureReleaseNotesExists = taskKey[Unit]("Ensure release notes exists")
ensureReleaseNotesExists in ThisBuild := {
  val currentVersion = (version in ThisBuild).value
  val notes = releaseNotesFile.value
  if(!notes.isFile) {
    throw new IllegalStateException(s"no release notes found for version [$currentVersion] at [$notes].")
  }
}

val addDateToReleaseNotes = taskKey[Unit]("Add current date to release notes")
addDateToReleaseNotes in ThisBuild := {
  ensureReleaseNotesExists.value

  val dateString = {
    val formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val now = java.time.ZonedDateTime.now()
    now.format(formatter)
  }

  val file = releaseNotesFile.value
  val newContents = IO.read(file).trim + s"\n\nReleased on $dateString.\n"
  IO.write(file, newContents)

  sbtrelease.Vcs.detect((baseDirectory in ciris).value).foreach { vcs =>
    vcs.add(file.getAbsolutePath).!
    vcs.commit(s"Add release date for v${(version in ThisBuild).value}", sign = true).!
  }
}

/* Aliases */

def addCommandsAlias(name: String, values: List[String]) =
  addCommandAlias(name, values.mkString(";", ";", ""))

addCommandsAlias("docTests", jvmModuleNames.map(_ + "/test"))

addCommandsAlias("publishSignedAll", allModuleNames.map(m => s"+$m/publishSigned"))

addCommandsAlias("validate", List(
  "clean",
  "testsJS/test",
  "coverage",
  "testsJVM/test",
  "coverageReport",
  "coverageAggregate",
  "mimaReportBinaryIssues"
))

addCommandsAlias("validateDocs", List("docTests", "docs/unidoc", "docs/tut"))

addCommandsAlias("validateNative", nativeModuleNames.map(_ + "/test"))
