import ReleaseTransformations._

val catsEffectVersion = "2.0.0"

val enumeratumVersion = "1.5.13"

val refinedVersion = "0.9.10"

val scala212 = "2.12.10"

val scala213 = "2.13.0"

lazy val ciris = project
  .in(file("."))
  .settings(
    mimaSettings,
    scalaSettings,
    noPublishSettings,
    console := (console in (core, Compile)).value,
    console in Test := (console in (core, Test)).value
  )
  .aggregate(core, enumeratum, refined)

lazy val core = project
  .in(file("modules/core"))
  .settings(
    moduleName := "ciris",
    name := moduleName.value,
    dependencySettings,
    publishSettings,
    mimaSettings,
    scalaSettings,
    testSettings
  )

lazy val enumeratum = project
  .in(file("modules/enumeratum"))
  .settings(
    moduleName := "ciris-enumeratum",
    name := moduleName.value,
    dependencySettings ++ Seq(
      libraryDependencies += "com.beachape" %% "enumeratum" % enumeratumVersion
    ),
    publishSettings,
    mimaSettings,
    scalaSettings,
    testSettings
  )
  .dependsOn(core)

lazy val refined = project
  .in(file("modules/refined"))
  .settings(
    moduleName := "ciris-refined",
    name := moduleName.value,
    dependencySettings ++ Seq(
      libraryDependencies += "eu.timepit" %% "refined" % refinedVersion
    ),
    publishSettings,
    mimaSettings,
    scalaSettings,
    testSettings
  )
  .dependsOn(core)

lazy val docs = project
  .in(file("docs"))
  .settings(
    moduleName := "ciris-docs",
    name := moduleName.value,
    dependencySettings,
    noPublishSettings,
    scalaSettings,
    mdocSettings,
    buildInfoSettings
  )
  .dependsOn(core, enumeratum, refined)
  .enablePlugins(BuildInfoPlugin, DocusaurusPlugin, MdocPlugin, ScalaUnidocPlugin)

lazy val dependencySettings = Seq(
  libraryDependencies += "org.typelevel" %% "cats-effect" % catsEffectVersion,
  addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.11.0" cross CrossVersion.full),
  libraryDependencies ++= Seq(
    "org.scalatestplus" %% "scalatestplus-scalacheck" % "3.1.0.0-RC2",
    "org.typelevel" %% "discipline-scalatest" % "1.0.0-RC1",
    "org.typelevel" %% "cats-effect-laws" % catsEffectVersion,
    "commons-codec" % "commons-codec" % "1.13"
  ).map(_ % Test)
)

lazy val mdocSettings = Seq(
  mdoc := run.in(Compile).evaluated,
  scalacOptions --= Seq("-Xfatal-warnings", "-Ywarn-unused"),
  crossScalaVersions := Seq(scalaVersion.value),
  unidocProjectFilter in (ScalaUnidoc, unidoc) := inProjects(core, enumeratum, refined),
  target in (ScalaUnidoc, unidoc) := (baseDirectory in LocalRootProject).value / "website" / "static" / "api",
  cleanFiles += (target in (ScalaUnidoc, unidoc)).value,
  docusaurusPublishGhpages := docusaurusPublishGhpages.dependsOn(unidoc in Compile).value,
  libraryDependencies ++= Seq(
    "eu.timepit" %% "refined-cats" % refinedVersion
  ),
  // format: off
  scalacOptions in (ScalaUnidoc, unidoc) ++= Seq(
    "-doc-source-url", s"https://github.com/vlovgr/ciris/tree/v${(latestVersion in ThisBuild).value}€{FILE_PATH}.scala",
    "-sourcepath", baseDirectory.in(LocalRootProject).value.getAbsolutePath,
    "-doc-title", "Ciris",
    "-doc-version", s"v${(latestVersion in ThisBuild).value}",
    "-groups"
  )
  // format: on
)

lazy val buildInfoSettings = Seq(
  buildInfoPackage := "ciris.build",
  buildInfoObject := "info",
  buildInfoKeys := Seq[BuildInfoKey](
    scalaVersion,
    scalacOptions,
    sourceDirectory,
    latestVersion in ThisBuild,
    BuildInfoKey.map(baseDirectory in LocalRootProject) {
      case (k, v) => "rootDirectory" -> v
    },
    BuildInfoKey.map(moduleName in core) {
      case (k, v) => "core" ++ k.capitalize -> v
    },
    BuildInfoKey.map(crossScalaVersions in core) {
      case (k, v) => "core" ++ k.capitalize -> v
    },
    BuildInfoKey.map(moduleName in enumeratum) {
      case (k, v) => "enumeratum" ++ k.capitalize -> v
    },
    BuildInfoKey.map(crossScalaVersions in enumeratum) {
      case (k, v) => "enumeratum" ++ k.capitalize -> v
    },
    BuildInfoKey.map(moduleName in refined) {
      case (k, v) => "refined" ++ k.capitalize -> v
    },
    BuildInfoKey.map(crossScalaVersions in refined) {
      case (k, v) => "refined" ++ k.capitalize -> v
    },
    organization in LocalRootProject,
    crossScalaVersions in core,
    BuildInfoKey("catsEffectVersion" -> catsEffectVersion),
    BuildInfoKey("enumeratumVersion" -> enumeratumVersion),
    BuildInfoKey("refinedVersion" -> refinedVersion)
  )
)

lazy val metadataSettings = Seq(
  organization := "is.cir",
  organizationName := "Ciris",
  organizationHomepage := Some(url("https://cir.is"))
)

lazy val publishSettings =
  metadataSettings ++ Seq(
    publishMavenStyle := true,
    publishArtifact in Test := false,
    publishTo := sonatypePublishToBundle.value,
    pomIncludeRepository := (_ => false),
    homepage := Some(url("https://cir.is")),
    licenses := Seq("MIT License" -> url("http://www.opensource.org/licenses/mit-license.php")),
    startYear := Some(2017),
    headerLicense := Some(
      de.heikoseeberger.sbtheader.License.MIT(
        s"${startYear.value.get}-${java.time.Year.now}",
        "Viktor Lövgren",
        HeaderLicenseStyle.SpdxSyntax
      )
    ),
    excludeFilter.in(headerSources) := HiddenFileFilter,
    developers := List(
      Developer(
        id = "vlovgr",
        name = "Viktor Lövgren",
        email = "github@vlovgr.se",
        url = url("https://vlovgr.se")
      )
    ),
    scmInfo := Some(
      ScmInfo(
        url("https://github.com/vlovgr/ciris"),
        "scm:git@github.com:vlovgr/ciris.git"
      )
    ),
    releaseCrossBuild := false, // See https://github.com/sbt/sbt-release/issues/214
    releaseUseGlobalVersion := true,
    releaseTagName := s"v${(version in ThisBuild).value}",
    releaseTagComment := s"Release version ${(version in ThisBuild).value}",
    releaseCommitMessage := s"Set version to ${(version in ThisBuild).value}",
    releaseProcess := Seq[ReleaseStep](
      checkSnapshotDependencies,
      inquireVersions,
      runClean,
      releaseStepCommandAndRemaining("+compile"),
      setReleaseVersion,
      setLatestVersion,
      releaseStepTask(updateSiteVariables in ThisBuild),
      releaseStepTask(addDateToReleaseNotes in ThisBuild),
      commitReleaseVersion,
      tagRelease,
      releaseStepCommandAndRemaining("+publish"),
      releaseStepCommand("sonatypeBundleRelease"),
      setNextVersion,
      commitNextVersion,
      pushChanges,
      releaseStepCommand("docs/docusaurusPublishGhpages")
    )
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
      Seq()
      // format: on
  }
)

lazy val noPublishSettings =
  publishSettings ++ Seq(
    skip in publish := true,
    publishArtifact := false
  )

lazy val scalaSettings = Seq(
  scalaVersion := scala213,
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
  scalacOptions in (Test, console) := (scalacOptions in (Compile, console)).value
)

lazy val testSettings = Seq(
  logBuffered in Test := false,
  parallelExecution in Test := false,
  testOptions in Test += Tests.Argument("-oDF"),
  scalacOptions in Test --= Seq("-deprecation", "-Xfatal-warnings", "-Xlint", "-Ywarn-unused")
)

def minorVersion(version: String): String = {
  val (major, minor) =
    CrossVersion.partialVersion(version).get
  s"$major.$minor"
}

val releaseNotesFile = taskKey[File]("Release notes for current version")
releaseNotesFile in ThisBuild := {
  val currentVersion = (version in ThisBuild).value
  file("notes") / s"$currentVersion.markdown"
}

val updateSiteVariables = taskKey[Unit]("Update site variables")
updateSiteVariables in ThisBuild := {
  val file = (baseDirectory in LocalRootProject).value / "website" / "siteConfig.js"
  val lines = IO.read(file).trim.split('\n').toVector

  val variables =
    Map[String, String](
      "organization" -> (organization in LocalRootProject).value,
      "coreModuleName" -> (moduleName in core).value,
      "latestVersion" -> (latestVersion in ThisBuild).value,
      "scalaPublishVersions" -> {
        val minorVersions = (crossScalaVersions in core).value.map(minorVersion)
        if (minorVersions.size <= 2) minorVersions.mkString(" and ")
        else minorVersions.init.mkString(", ") ++ " and " ++ minorVersions.last
      }
    )

  val newLine =
    variables.toList
      .map { case (k, v) => s"$k: '$v'" }
      .mkString("const buildInfo = { ", ", ", " };")

  val lineIndex = lines.indexWhere(_.trim.startsWith("const buildInfo"))
  val newLines = lines.updated(lineIndex, newLine)
  val newFileContents = newLines.mkString("", "\n", "\n")
  IO.write(file, newFileContents)

  sbtrelease.Vcs.detect((baseDirectory in LocalRootProject).value).foreach { vcs =>
    vcs.add(file.getAbsolutePath).!
    vcs
      .commit(
        s"Update site variables for v${(version in ThisBuild).value}",
        sign = true,
        signOff = false
      )
      .!
  }
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

  sbtrelease.Vcs.detect((baseDirectory in LocalRootProject).value).foreach { vcs =>
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
    "scalafmtCheck",
    "scalafmtSbtCheck",
    "headerCheck",
    "+doc",
    "docs/run"
  )
)
