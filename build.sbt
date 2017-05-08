lazy val ciris = project
  .in(file("."))
  .settings(moduleName := "ciris", name := "Ciris")
  .settings(inThisBuild(scalaSettings))
  .settings(inThisBuild(testSettings))
  .settings(noPublishSettings)
  .settings(releaseSettings)
  .aggregate(core, enumeratum, refined, squants)

lazy val core = project
  .in(file("modules/core"))
  .settings(moduleName := "ciris-core", name := "Ciris core")
  .settings(releaseSettings)

lazy val enumeratum = project
  .in(file("modules/enumeratum"))
  .settings(moduleName := "ciris-enumeratum", name := "Ciris enumeratum")
  .settings(libraryDependencies += "com.beachape" %% "enumeratum" % "1.5.12")
  .settings(releaseSettings)
  .dependsOn(core % "compile;test->test")

lazy val refined = project
  .in(file("modules/refined"))
  .settings(moduleName := "ciris-refined", name := "Ciris refined")
  .settings(libraryDependencies += "eu.timepit" %% "refined" % "0.8.1")
  .settings(releaseSettings)
  .dependsOn(core % "compile;test->test")

lazy val squants = project
  .in(file("modules/squants"))
  .settings(moduleName := "ciris-squants", name := "Ciris squants")
  .settings(libraryDependencies += "org.typelevel" %% "squants" % "1.2.0")
  .settings(releaseSettings)
  .dependsOn(core % "compile;test->test")

lazy val docs = project
  .in(file("docs"))
  .settings(moduleName := "ciris-docs", name := "Ciris docs")
  .settings(noPublishSettings)
  .settings(
    micrositeName := "Ciris",
    micrositeDescription := "Lightweight, extensible, and validated configuration loading in Scala",
    micrositeDocumentationUrl := "https://www.javadoc.io/doc/is.cir/ciris-core_2.12",
    micrositeAuthor := "Viktor Lövgren",
    micrositeOrganizationHomepage := "https://vlovgr.se",
    micrositeAnalyticsToken := "UA-37804684-4",
    micrositeGithubOwner := "vlovgr",
    micrositeGithubRepo := "ciris",
    micrositeHighlightTheme := "atom-one-light"
  )
  .settings(
    buildInfoObject := "build",
    buildInfoPackage := "ciris",
    buildInfoKeys := Seq[BuildInfoKey](
      organization,
      latestVersion in ThisBuild,
      crossScalaVersions in ThisBuild,
      BuildInfoKey.map(moduleName in core) { case (k, v) ⇒ "core" + k.capitalize -> v },
      BuildInfoKey.map(moduleName in enumeratum) { case (k, v) ⇒ "enumeratum" + k.capitalize -> v },
      BuildInfoKey.map(moduleName in refined) { case (k, v) ⇒ "refined" + k.capitalize -> v },
      BuildInfoKey.map(moduleName in squants) { case (k, v) ⇒ "squants" + k.capitalize -> v }
    )
  )
  .dependsOn(core, enumeratum, refined, squants)
  .enablePlugins(BuildInfoPlugin, MicrositesPlugin)

lazy val scala210 = "2.10.6"
lazy val scala211 = "2.11.11"
lazy val scala212 = "2.12.2"

lazy val scalaSettings = Seq(
  scalaVersion := scala211,
  crossScalaVersions := Seq(scala210, scala211, scala212),
  scalacOptions ++= {
    val onScala210 = scalaVersion.value == scala210
    val warnUnusedImport = "-Ywarn-unused-import"

    Seq(
      "-deprecation",
      "-encoding",
      "UTF-8",
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
      warnUnusedImport
    ).filter {
      case `warnUnusedImport` if onScala210 ⇒ false
      case _ ⇒ true
    }
  }
)

lazy val metadataSettings = Seq(
  name := "Ciris",
  organization := "is.cir",
  organizationName := "Ciris",
  organizationHomepage := Some(url("https://cir.is"))
)

import ReleaseTransformations._
lazy val releaseSettings =
  metadataSettings ++ Seq(
    homepage := organizationHomepage.value,
    publishMavenStyle := true,
    publishArtifact in Test := false,
    useGpg := true,
    pomIncludeRepository := { _ => false },
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
    publishTo := {
      val nexus = "https://oss.sonatype.org/"
      if(isSnapshot.value)
        Some("snapshots" at nexus + "content/repositories/snapshots")
      else
        Some("releases" at nexus + "service/local/staging/deploy/maven2")
    },
    releaseCrossBuild := true,
    releaseTagName := s"v${(version in ThisBuild).value}",
    releaseTagComment := s"Release version ${(version in ThisBuild).value}",
    releaseCommitMessage := s"Set version to ${(version in ThisBuild).value}",
    releasePublishArtifactsAction := PgpKeys.publishSigned.value,
    releaseUseGlobalVersion := true,
    releaseProcess := Seq[ReleaseStep](
      checkSnapshotDependencies,
      inquireVersions,
      runClean,
      runTest,
      setReleaseVersion,
      setLatestVersion,
      releaseStepTask(updateReadme in ThisBuild),
      commitReleaseVersion,
      tagRelease,
      publishArtifacts,
      releaseStepCommand("sonatypeRelease"),
      setNextVersion,
      commitNextVersion,
      pushChanges,
      releaseStepCommand("project docs"),
      releaseStepCommand("publishMicrosite")
    )
  )

lazy val testSettings = Seq(
  logBuffered in Test := false,
  parallelExecution in Test := false,
  testOptions in Test += Tests.Argument("-oDF"),
  libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest" % "3.0.3" % Test,
    "org.scalacheck" %% "scalacheck" % "1.13.5" % Test
  )
)

lazy val noPublishSettings =
  metadataSettings ++ Seq(
    publish := (),
    publishLocal := (),
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

val generateReadme = taskKey[File]("Generates the readme")
generateReadme in ThisBuild := {
  (tut in docs).value
  val source = IO.read((tutTargetDirectory in docs).value / "index.md")
  val readme = source.replaceAll("^\\s*---[^(---)]*---\\s*", "")
  val target = (baseDirectory in ciris).value / "readme.md"
  IO.write(target, readme)
  target
}

val updateReadme = taskKey[Unit]("Generates and commits the readme")
updateReadme in ThisBuild := {
  (generateReadme in ThisBuild).value
  sbtrelease.Vcs.detect((baseDirectory in ciris).value).foreach { vcs ⇒
    vcs.add("readme.md").!
    vcs.commit("Update readme to latest version", sign = true).!
  }
}
