import sbtcrossproject.{crossProject, CrossType}

lazy val ciris = project
  .in(file("."))
  .settings(moduleName := "ciris", name := "Ciris")
  .settings(inThisBuild(scalaSettings))
  .settings(inThisBuild(metadataSettings))
  .settings(inThisBuild(testSettings))
  .aggregate(
    coreJS, coreJVM,
    enumeratumJS, enumeratumJVM,
    refinedJS, refinedJVM,
    squantsJS, squantsJVM
  )

lazy val core =
  crossProject(JSPlatform, JVMPlatform, NativePlatform)
    .in(file("modules/core"))
    .settings(moduleName := "ciris-core", name := "Ciris core")
    .jsSettings(crossCompileSettings)
    .jvmSettings(crossCompileSettings)

lazy val coreJS = core.js
lazy val coreJVM = core.jvm
lazy val coreNative = core.native

lazy val enumeratum =
  crossProject(JSPlatform, JVMPlatform)
    .in(file("modules/enumeratum"))
    .settings(moduleName := "ciris-enumeratum", name := "Ciris enumeratum")
    .settings(libraryDependencies += "com.beachape" %%% "enumeratum" % "1.5.12")
    .settings(crossCompileSettings)
    .dependsOn(core)

lazy val enumeratumJS = enumeratum.js
lazy val enumeratumJVM = enumeratum.jvm

lazy val refined =
  crossProject(JSPlatform, JVMPlatform)
    .in(file("modules/refined"))
    .settings(moduleName := "ciris-refined", name := "Ciris refined")
    .settings(libraryDependencies += "eu.timepit" %%% "refined" % "0.8.0")
    .settings(crossCompileSettings)
    .dependsOn(core)

lazy val refinedJS = refined.js
lazy val refinedJVM = refined.jvm

lazy val squants =
  crossProject(JSPlatform, JVMPlatform)
    .in(file("modules/squants"))
    .settings(moduleName := "ciris-squants", name := "Ciris squants")
    .settings(libraryDependencies += "org.typelevel" %%% "squants" % "1.2.0")
    .settings(crossCompileSettings)
    .dependsOn(core)

lazy val squantsJS = squants.js
lazy val squantsJVM = squants.jvm

lazy val docs = project
  .in(file("docs"))
  .settings(moduleName := "ciris-docs", name := "Ciris docs")
  .dependsOn(coreJVM, refinedJVM)
  .enablePlugins(BuildInfoPlugin, MicrositesPlugin)
  .settings(
    micrositeName := "Ciris",
    micrositeDescription := "Lightweight, extensible, and validated configuration loading in Scala",
    micrositeAuthor := "Viktor Lövgren",
    micrositeOrganizationHomepage := "https://vlovgr.se",
    micrositeGithubOwner := "vlovgr",
    micrositeGithubRepo := "ciris",
    micrositeHighlightTheme := "atom-one-light"
  ).settings(
    buildInfoObject := "build",
    buildInfoPackage := "ciris",
    buildInfoKeys := Seq[BuildInfoKey](
      organization in ThisBuild,
      latestVersion in ThisBuild,
      BuildInfoKey.map(moduleName in coreJVM) { case (k, v) ⇒ "core" + k.capitalize -> v },
      BuildInfoKey.map(moduleName in enumeratumJVM) { case (k, v) ⇒ "enumeratum" + k.capitalize -> v },
      BuildInfoKey.map(moduleName in refinedJVM) { case (k, v) ⇒ "refined" + k.capitalize -> v },
      BuildInfoKey.map(moduleName in squantsJVM) { case (k, v) ⇒ "squants" + k.capitalize -> v }
    )
  )

lazy val scala210 = "2.10.6"
lazy val scala211 = "2.11.11"
lazy val scala212 = "2.12.2"

lazy val scalaSettings = Seq(
  scalaVersion := scala211,
  scalacOptions ++= {
    val onScala210 = scalaVersion.value == scala210
    val warnUnusedImport = "-Ywarn-unused-import"

    Seq(
      "-deprecation",
      "-encoding", "UTF-8",
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

lazy val testSettings = Seq(
  logBuffered in Test := false,
  parallelExecution in Test := false,
  testOptions in Test += Tests.Argument("-oDF"),
  libraryDependencies ++= Seq(
    "org.scalatest" %%% "scalatest" % "3.0.3" % Test,
    "org.scalacheck" %%% "scalacheck" % "1.13.5" % Test
  )
)

lazy val crossCompileSettings = Seq(
  crossScalaVersions := Seq(scala210, scala211, scala212)
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
