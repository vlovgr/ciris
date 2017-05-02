lazy val ciris = project
  .in(file("."))
  .settings(moduleName := "ciris", name := "Ciris")
  .settings(inThisBuild(testSettings))
  .settings(inThisBuild(scalaSettings))
  .settings(inThisBuild(metadataSettings))
  .aggregate(core, enumeratum, refined, squants)

lazy val core = project
  .in(file("modules/core"))
  .settings(moduleName := "ciris-core", name := "Ciris core")

lazy val enumeratum = project
  .in(file("modules/enumeratum"))
  .settings(moduleName := "ciris-enumeratum", name := "Ciris enumeratum")
  .settings(libraryDependencies += "com.beachape" %% "enumeratum" % "1.5.12")
  .dependsOn(core % "compile;test->test")

lazy val refined = project
  .in(file("modules/refined"))
  .settings(moduleName := "ciris-refined", name := "Ciris refined")
  .settings(libraryDependencies += "eu.timepit" %% "refined" % "0.8.0")
  .dependsOn(core % "compile;test->test")

lazy val squants = project
  .in(file("modules/squants"))
  .settings(moduleName := "ciris-squants", name := "Ciris squants")
  .settings(libraryDependencies += "org.typelevel" %% "squants" % "1.2.0")
  .dependsOn(core % "compile;test->test")

lazy val docs = project
  .in(file("docs"))
  .settings(moduleName := "ciris-docs", name := "Ciris docs")
  .dependsOn(core, enumeratum, refined, squants)
  .enablePlugins(BuildInfoPlugin, MicrositesPlugin)
  .settings(noReleaseSettings)
  .settings(
    micrositeName := "Ciris",
    micrositeDescription := "Lightweight, extensible, and validated configuration loading in Scala",
    micrositeAuthor := "Viktor Lövgren",
    micrositeOrganizationHomepage := "https://vlovgr.se",
    micrositeGithubOwner := "vlovgr",
    micrositeGithubRepo := "ciris",
    micrositeHighlightTheme := "atom-one-light"
  )
  .settings(
    buildInfoObject := "build",
    buildInfoPackage := "ciris",
    buildInfoKeys := Seq[BuildInfoKey](
      organization in ThisBuild,
      latestVersion in ThisBuild,
      crossScalaVersions in ThisBuild,
      BuildInfoKey.map(moduleName in core) { case (k, v) ⇒ "core" + k.capitalize -> v },
      BuildInfoKey.map(moduleName in enumeratum) { case (k, v) ⇒ "enumeratum" + k.capitalize -> v },
      BuildInfoKey.map(moduleName in refined) { case (k, v) ⇒ "refined" + k.capitalize -> v },
      BuildInfoKey.map(moduleName in squants) { case (k, v) ⇒ "squants" + k.capitalize -> v }
    )
  )

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

lazy val testSettings = Seq(
  logBuffered in Test := false,
  parallelExecution in Test := false,
  testOptions in Test += Tests.Argument("-oDF"),
  libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest" % "3.0.3" % Test,
    "org.scalacheck" %% "scalacheck" % "1.13.5" % Test
  )
)

lazy val noReleaseSettings = Seq(
  publish := (),
  publishLocal := (),
  publishArtifact := false,
  releaseProcess := Seq()
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
