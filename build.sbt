import com.typesafe.sbt.SbtGit.GitKeys._
import ReleaseTransformations._

/* Variables */

lazy val scala212 = "2.12.8"
lazy val scala213 = "2.13.0"

lazy val catsEffectVersion = "2.0.0-RC3"
lazy val catsVersion = "2.0.0-RC3"
lazy val commonsCodecVersion = "1.13"
lazy val enumeratumVersion = "1.5.13"
lazy val kittensVersion = "2.0.0-M1"
lazy val refinedVersion = "0.9.9"
lazy val shapelessVersion = "2.3.3"
lazy val spireVersion = "0.17.0-M1"
lazy val squantsVersion = "1.4.0"

lazy val scriptsDirectory = "scripts"

lazy val root = project
  .in(file("."))
  .settings(
    mimaSettings,
    noPublishSettings,
    scalaVersion := scala212,
    console := (console in (core, Compile)).value,
    console in Test := (console in (core, Test)).value
  )
  .aggregate(cats, catsEffect, core, enumeratum, generic, refined, spire, squants)

lazy val core = project
  .in(file("modules/core"))
  .settings(
    moduleName := "ciris-core",
    name := moduleName.value,
    publishSettings,
    mimaSettings,
    scalaSettings,
    testSettings
  )

lazy val cats = project
  .in(file("modules/cats"))
  .settings(
    moduleName := "ciris-cats",
    name := moduleName.value,
    libraryDependencies += "org.typelevel" %% "cats-core" % catsVersion,
    publishSettings,
    mimaSettings,
    scalaSettings,
    testSettings
  )
  .dependsOn(core % "compile->compile;test->test")

lazy val catsEffect = project
  .in(file("modules/cats-effect"))
  .settings(
    moduleName := "ciris-cats-effect",
    name := moduleName.value,
    libraryDependencies += "org.typelevel" %% "cats-effect" % catsEffectVersion,
    publishSettings,
    mimaSettings,
    scalaSettings,
    testSettings
  )
  .dependsOn(cats % "compile->compile;test->test")

lazy val enumeratum = project
  .in(file("modules/enumeratum"))
  .settings(
    moduleName := "ciris-enumeratum",
    name := moduleName.value,
    libraryDependencies += "com.beachape" %% "enumeratum" % enumeratumVersion,
    publishSettings,
    mimaSettings,
    scalaSettings,
    testSettings
  )
  .dependsOn(core % "compile->compile;test->test")

lazy val generic = project
  .in(file("modules/generic"))
  .settings(
    moduleName := "ciris-generic",
    name := moduleName.value,
    libraryDependencies += "com.chuusai" %% "shapeless" % shapelessVersion,
    publishSettings,
    mimaSettings,
    scalaSettings,
    testSettings
  )
  .dependsOn(core % "compile->compile;test->test")

lazy val refined = project
  .in(file("modules/refined"))
  .settings(
    moduleName := "ciris-refined",
    name := moduleName.value,
    libraryDependencies += "eu.timepit" %% "refined" % refinedVersion,
    publishSettings,
    mimaSettings,
    scalaSettings,
    testSettings
  )
  .dependsOn(core % "compile->compile;test->test")

lazy val spire = project
  .in(file("modules/spire"))
  .settings(
    moduleName := "ciris-spire",
    name := moduleName.value,
    libraryDependencies += "org.typelevel" %% "spire" % spireVersion,
    publishSettings,
    mimaSettings,
    scalaSettings,
    testSettings
  )
  .dependsOn(core % "compile->compile;test->test")

lazy val squants = project
  .in(file("modules/squants"))
  .settings(
    moduleName := "ciris-squants",
    name := moduleName.value,
    libraryDependencies += "org.typelevel" %% "squants" % squantsVersion,
    publishSettings,
    mimaSettings,
    scalaSettings ++ Seq(
      crossScalaVersions := Seq(scala212)
    ),
    testSettings
  )
  .dependsOn(core % "compile->compile;test->test")

lazy val docs = project
  .in(file("docs"))
  .settings(
    moduleName := "ciris-docs",
    name := moduleName.value,
    noPublishSettings,
    mimaSettings,
    scalaSettings,
    testSettings
  )
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
      crossScalaVersions in core,
      BuildInfoKey.map(moduleName in cats) { case (k, v) => "cats" + k.capitalize -> v },
      BuildInfoKey
        .map(moduleName in catsEffect) { case (k, v)     => "catsEffect" + k.capitalize -> v },
      BuildInfoKey.map(moduleName in core) { case (k, v) => "core" + k.capitalize -> v },
      BuildInfoKey
        .map(moduleName in enumeratum) { case (k, v)             => "enumeratum" + k.capitalize -> v },
      BuildInfoKey.map(moduleName in generic) { case (k, v)      => "generic" + k.capitalize -> v },
      BuildInfoKey.map(moduleName in refined) { case (k, v)      => "refined" + k.capitalize -> v },
      BuildInfoKey.map(moduleName in spire) { case (k, v)        => "spire" + k.capitalize -> v },
      BuildInfoKey.map(moduleName in squants) { case (k, v)      => "squants" + k.capitalize -> v },
      BuildInfoKey.map(crossScalaVersions in cats) { case (k, v) => "cats" + k.capitalize -> v },
      BuildInfoKey.map(crossScalaVersions in catsEffect) {
        case (k, v) => "catsEffect" + k.capitalize -> v
      },
      BuildInfoKey.map(crossScalaVersions in core) { case (k, v) => "core" + k.capitalize -> v },
      BuildInfoKey.map(crossScalaVersions in enumeratum) {
        case (k, v) => "enumeratum" + k.capitalize -> v
      },
      BuildInfoKey.map(crossScalaVersions in generic) {
        case (k, v) => "generic" + k.capitalize -> v
      },
      BuildInfoKey.map(crossScalaVersions in refined) {
        case (k, v) => "refined" + k.capitalize -> v
      },
      BuildInfoKey.map(crossScalaVersions in spire) { case (k, v) => "spire" + k.capitalize -> v },
      BuildInfoKey.map(crossScalaVersions in squants) {
        case (k, v) => "squants" + k.capitalize -> v
      },
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
    scalacOptions --= Seq("-Xlint", "-Ywarn-unused"),
    unidocProjectFilter in (ScalaUnidoc, unidoc) := inAnyProject,
    siteSubdirName in ScalaUnidoc := micrositeDocumentationUrl.value,
    addMappingsToSiteDir(mappings in (ScalaUnidoc, packageDoc), siteSubdirName in ScalaUnidoc),
    gitRemoteRepo := "git@github.com:vlovgr/ciris.git",
    scalacOptions in (ScalaUnidoc, unidoc) ++= Seq(
      "-skip-packages",
      buildInfoPackage.value,
      "-doc-source-url",
      s"https://github.com/vlovgr/ciris/tree/v${(latestVersion in ThisBuild).value}€{FILE_PATH}.scala",
      "-sourcepath",
      baseDirectory.in(LocalRootProject).value.getAbsolutePath,
      "-doc-title",
      "Ciris",
      "-doc-version",
      s"v${(latestVersion in ThisBuild).value}",
      "-doc-root-content",
      (generateApiIndexFile.value).getAbsolutePath
    )
  )
  .dependsOn(cats, catsEffect, core, enumeratum, generic, refined, spire, squants)
  .enablePlugins(BuildInfoPlugin, MicrositesPlugin, ScalaUnidocPlugin)

/* Settings */

lazy val scalaSettings = Seq(
  scalaVersion := scala212,
  crossScalaVersions := Seq(scala212, scala213),
  scalacOptions ++= Seq(
    "-deprecation",
    "-encoding",
    "UTF-8",
    "-feature",
    "-language:higherKinds",
    "-language:implicitConversions",
    "-unchecked",
    "-Xfatal-warnings",
    "-Xlint",
    "-Yno-adapted-args",
    "-Ywarn-dead-code",
    "-Ywarn-numeric-widen",
    "-Ywarn-value-discard",
    "-Ywarn-unused",
    "-Ypartial-unification"
  ).filter {
    case ("-Yno-adapted-args" | "-Ypartial-unification") if scalaVersion.value.startsWith("2.13") =>
      false
    case _ => true
  },
  scalacOptions in (Compile, console) --= Seq("-Xlint", "-Ywarn-unused"),
  scalacOptions in (Test, console) := (scalacOptions in (Compile, console)).value,
  addCompilerPlugin("org.typelevel" % "kind-projector" % "0.10.3" cross CrossVersion.binary)
)

lazy val metadataSettings = Seq(
  name := "Ciris",
  organization := "is.cir",
  organizationName := "Ciris",
  organizationHomepage := Some(url("https://cir.is"))
)

lazy val publishSettings =
  metadataSettings ++ Seq(
    homepage := organizationHomepage.value,
    publishMavenStyle := true,
    publishArtifact in Test := false,
    pomIncludeRepository := { _ =>
      false
    },
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
    publishTo := sonatypePublishTo.value,
    releaseCrossBuild := false, // See https://github.com/sbt/sbt-release/issues/214
    releaseTagName := s"v${(version in ThisBuild).value}",
    releaseTagComment := s"Release version ${(version in ThisBuild).value}",
    releaseCommitMessage := s"Set version to ${(version in ThisBuild).value}",
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
      releaseStepCommandAndRemaining("+publish"),
      releaseStepCommand("sonatypeRelease"),
      setNextVersion,
      commitNextVersion,
      pushChanges,
      releaseStepCommand("docs/publishMicrosite")
    )
  )

lazy val testSettings = Seq(
  logBuffered in Test := false,
  parallelExecution in Test := false,
  testOptions in Test += Tests.Argument("-oDF"),
  scalacOptions in Test --= Seq("-deprecation", "-Xfatal-warnings"), // sbt-doctest generates code with deprecation warnings on Scala 2.13
  scalacOptions in Test --= Seq("-Xlint", "-Ywarn-unused"),
  doctestTestFramework := DoctestTestFramework.ScalaTest,
  libraryDependencies ++= Seq(
    "org.scalatestplus" %% "scalatestplus-scalacheck" % "1.0.0-SNAP8",
    "commons-codec" % "commons-codec" % commonsCodecVersion
  ).map(_ % Test)
)

lazy val mimaSettings = Seq(
  mimaPreviousArtifacts := {
    val released = !unreleasedModuleNames.value.contains(moduleName.value)
    val publishing = publishArtifact.value

    if (publishing && released)
      binaryCompatibleVersions.value
        .map(version => organization.value %% moduleName.value % version)
    else
      Set()
  },
  mimaBinaryIssueFilters ++= {
    import com.typesafe.tools.mima.core._
    // format: off
    Seq(
      ProblemFilters.exclude[Problem]("ciris.internal.*")
    )
    // format: on
  }
)

lazy val coverageSettings =
  Seq(
    coverageExcludedPackages := List(
      "ciris.internal.digest.GeneralDigest"
    ).mkString(";")
  )

lazy val noPublishSettings =
  publishSettings ++ Seq(
    skip in publish := true,
    publishArtifact := false
  )

lazy val sourceGeneratorSettings = Seq(
  sourceGenerators in Compile +=
    Def
      .task(
        generateSources(
          (sourceManaged in Compile).value,
          (sourceManaged in Test).value,
          "ciris"
        )
      )
      .taskValue
)

val generateReadme = taskKey[File]("Generates the readme")
generateReadme in ThisBuild := {
  (tutOnly in docs).toTask(" index.md").value
  val source = IO.read((tutTargetDirectory in docs).value / "index.md")
  val readme =
    source
      .replaceAll("^\\s*---[^(---)]*---\\s*", "") // Remove metadata
  val target = (baseDirectory in LocalRootProject).value / "readme.md"
  IO.write(target, readme)
  target
}

val updateReadme = taskKey[Unit]("Generates and commits the readme")
updateReadme in ThisBuild := {
  (generateReadme in ThisBuild).value
  sbtrelease.Vcs.detect((baseDirectory in LocalRootProject).value).foreach { vcs =>
    vcs.add("readme.md").!
    vcs.commit("Update readme to latest version", sign = true, signOff = false).!
  }
}

val generateContributing = taskKey[File]("Generates the contributing guide")
generateContributing in ThisBuild := {
  (tutOnly in docs).toTask(" docs/contributing.md").value
  val source = IO.read((tutTargetDirectory in docs).value / "docs" / "contributing.md")
  val contributing =
    source
      .replaceAll("^\\s*---[^(---)]*---\\s*", "") // Remove metadata
  val target = (baseDirectory in LocalRootProject).value / "contributing.md"
  IO.write(target, contributing)
  target
}

val updateContributing = taskKey[Unit]("Generates and commits the contributing guide")
updateContributing in ThisBuild := {
  (generateContributing in ThisBuild).value
  sbtrelease.Vcs.detect((baseDirectory in LocalRootProject).value).foreach { vcs =>
    vcs.add("contributing.md").!
    vcs.commit("Update contributing guide to latest version", sign = true, signOff = false).!
  }
}

val generateScripts = taskKey[Unit]("Generates scripts")
generateScripts in ThisBuild := {
  val output = file(scriptsDirectory)
  val organizationId = (organization in core).value
  val moduleVersion = (latestVersion in ThisBuild).value

  def tryScript(
    extraCoursierArgs: Seq[String] = Seq.empty,
    extraPredefCode: Seq[String] = Seq.empty,
    ammoniteScalaVersion: String = "2.12.8"
  ) = {
    val coursierArgs =
      if (extraCoursierArgs.nonEmpty)
        "\n" + extraCoursierArgs.map(s => s"  $s \\").mkString("\n")
      else ""

    val predefCode =
      if (extraPredefCode.nonEmpty)
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
       |  $organizationId:${(moduleName in cats).value}_2.12:$moduleVersion \\
       |  $organizationId:${(moduleName in catsEffect).value}_2.12:$moduleVersion \\
       |  $organizationId:${(moduleName in core).value}_2.12:$moduleVersion \\
       |  $organizationId:${(moduleName in enumeratum).value}_2.12:$moduleVersion \\
       |  $organizationId:${(moduleName in generic).value}_2.12:$moduleVersion \\
       |  $organizationId:${(moduleName in refined).value}_2.12:$moduleVersion \\
       |  $organizationId:${(moduleName in spire).value}_2.12:$moduleVersion \\
       |  $organizationId:${(moduleName in squants).value}_2.12:$moduleVersion \\
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
  IO.write(
    output / "try.typelevel.sh",
    tryScript(
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
    )
  )
}

val updateScripts = taskKey[Unit]("Generates and commits scripts")
updateScripts in ThisBuild := {
  (generateScripts in ThisBuild).value
  sbtrelease.Vcs.detect((baseDirectory in LocalRootProject).value).foreach { vcs =>
    vcs.add(scriptsDirectory).!
    vcs
      .commit(
        "Update scripts to latest version",
        sign = true,
        signOff = false
      )
      .!
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
  if (!notes.isFile) {
    throw new IllegalStateException(
      s"no release notes found for version [$currentVersion] at [$notes]."
    )
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

  sbtrelease.Vcs.detect((baseDirectory in root).value).foreach { vcs =>
    vcs.add(file.getAbsolutePath).!
    vcs
      .commit(
        s"Add release date for v${(version in ThisBuild).value}",
        sign = true,
        signOff = false
      )
      .!
  }
}

def addCommandsAlias(name: String, values: List[String]) =
  addCommandAlias(name, values.mkString(";", ";", ""))

addCommandsAlias(
  "validate",
  List(
    "+clean",
    "+coverage",
    "+test",
    "+coverageReport",
    "+mimaReportBinaryIssues",
    "+doc",
    "docs/tut"
  )
)
