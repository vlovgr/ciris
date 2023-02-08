val catsEffectVersion = "3.3.14"

val circeVersion = "0.14.3"

val circeYamlVersion = "0.14.2"

val enumeratumVersion = "1.7.2"

val http4sVersion = "0.23.16"

val refinedVersion = "0.10.1"

val squantsVersion = "1.8.3"

val scala212 = "2.12.17"

val scala213 = "2.13.10"

val scala3 = "3.2.2"

val scalaJsMajorMinorVersion = "1.11"

val scalaNativeMajorMinorVersion = "0.4"

ThisBuild / versionScheme := Some("early-semver")

ThisBuild / doctestTestFramework := DoctestTestFramework.Munit

lazy val ciris = project
  .in(file("."))
  .settings(
    mimaSettings,
    scalaSettings,
    noPublishSettings,
    console := (core.jvm / Compile / console).value,
    Test / console := (core.jvm / Test / console).value
  )
  .aggregate(
    core.js,
    core.jvm,
    core.native,
    circe.js,
    circe.jvm,
    circe.native,
    circeYaml,
    enumeratum.js,
    enumeratum.jvm,
    http4s.js,
    http4s.jvm,
    http4s.native,
    refined.js,
    refined.jvm,
    refined.native,
    squants.js,
    squants.jvm,
    squants.native
  )

lazy val core = crossProject(JSPlatform, JVMPlatform, NativePlatform)
  .in(file("modules/core"))
  .settings(
    moduleName := "ciris",
    name := moduleName.value,
    dependencySettings ++ Seq(
      libraryDependencies += "org.typelevel" %%% "cats-effect-kernel" % catsEffectVersion
    ),
    publishSettings,
    mimaSettings,
    scalaSettings ++ Seq(
      crossScalaVersions += scala3
    ),
    testSettings,
    headerSources / excludeFilter :=
      HiddenFileFilter ||
        "*GeneralDigest.scala" ||
        "*Pack.scala" ||
        "*SHA1Digest.scala"
  )
  .jsSettings(sharedJsSettings)
  .nativeSettings(sharedNativeSettings)

lazy val circe = crossProject(JSPlatform, JVMPlatform, NativePlatform)
  .in(file("modules/circe"))
  .settings(
    moduleName := "ciris-circe",
    name := moduleName.value,
    dependencySettings ++ Seq(
      libraryDependencies += "io.circe" %%% "circe-parser" % circeVersion
    ),
    publishSettings,
    mimaSettings,
    scalaSettings ++ Seq(
      crossScalaVersions += scala3
    ),
    testSettings
  )
  .jsSettings(sharedJsSettings)
  .nativeSettings(sharedNativeSettings)
  .dependsOn(core)

lazy val circeYaml = project
  .in(file("modules/circe-yaml"))
  .settings(
    moduleName := "ciris-circe-yaml",
    name := moduleName.value,
    dependencySettings ++ Seq(
      libraryDependencies += "io.circe" %% "circe-yaml" % circeYamlVersion
    ),
    publishSettings,
    mimaSettings,
    scalaSettings ++ Seq(
      crossScalaVersions += scala3
    ),
    testSettings
  )
  .dependsOn(core.jvm)

lazy val enumeratum = crossProject(JSPlatform, JVMPlatform)
  .in(file("modules/enumeratum"))
  .settings(
    moduleName := "ciris-enumeratum",
    name := moduleName.value,
    dependencySettings ++ Seq(
      libraryDependencies ++= Seq(
        "com.beachape" %%% "enumeratum" % enumeratumVersion
      )
    ),
    publishSettings,
    mimaSettings,
    scalaSettings,
    testSettings
  )
  .jsSettings(sharedJsSettings)
  .dependsOn(core)

lazy val http4s = crossProject(JSPlatform, JVMPlatform, NativePlatform)
  .in(file("modules/http4s"))
  .settings(
    moduleName := "ciris-http4s",
    name := moduleName.value,
    dependencySettings ++ Seq(
      libraryDependencies += "org.http4s" %%% "http4s-core" % http4sVersion
    ),
    publishSettings,
    mimaSettings,
    scalaSettings ++ Seq(
      crossScalaVersions += scala3
    ),
    testSettings
  )
  .jsSettings(
    sharedJsSettings,
    Test / scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.CommonJSModule) }
  )
  .nativeSettings(sharedNativeSettings)
  .dependsOn(core)

lazy val refined = crossProject(JSPlatform, JVMPlatform, NativePlatform)
  .in(file("modules/refined"))
  .settings(
    moduleName := "ciris-refined",
    name := moduleName.value,
    dependencySettings ++ Seq(
      libraryDependencies ++= Seq(
        "eu.timepit" %%% "refined" % refinedVersion
      )
    ),
    publishSettings,
    mimaSettings,
    scalaSettings ++ Seq(
      crossScalaVersions += scala3
    ),
    testSettings
  )
  .jsSettings(sharedJsSettings)
  .nativeSettings(sharedNativeSettings)
  .dependsOn(core)

lazy val squants = crossProject(JSPlatform, JVMPlatform, NativePlatform)
  .in(file("modules/squants"))
  .settings(
    moduleName := "ciris-squants",
    name := moduleName.value,
    dependencySettings ++ Seq(
      libraryDependencies += "org.typelevel" %%% "squants" % squantsVersion
    ),
    publishSettings,
    mimaSettings,
    scalaSettings ++ Seq(
      crossScalaVersions += scala3
    ),
    testSettings
  )
  .jsSettings(sharedJsSettings)
  .nativeSettings(
    sharedNativeSettings,
    crossScalaVersions -= scala3
  )
  .dependsOn(core)

lazy val docs = project
  .in(file("docs"))
  .settings(
    moduleName := "ciris-docs",
    name := moduleName.value,
    dependencySettings ++ Seq(
      libraryDependencies += "org.typelevel" %% "cats-effect" % catsEffectVersion
    ),
    noPublishSettings,
    scalaSettings,
    mdocSettings,
    buildInfoSettings
  )
  .dependsOn(core.jvm, circe.jvm, circeYaml, enumeratum.jvm, http4s.jvm, refined.jvm, squants.jvm)
  .enablePlugins(BuildInfoPlugin, DocusaurusPlugin, MdocPlugin, ScalaUnidocPlugin)

lazy val dependencySettings = Seq(
  libraryDependencies ++= {
    if (scalaVersion.value.startsWith("3")) Nil
    else
      Seq(compilerPlugin(("org.typelevel" %% "kind-projector" % "0.13.2").cross(CrossVersion.full)))
  },
  libraryDependencies ++= Seq(
    "org.typelevel" %%% "munit-cats-effect" % "2.0.0-M3",
    "org.typelevel" %%% "scalacheck-effect-munit" % "2.0.0-M2",
    "org.typelevel" %%% "cats-effect-laws" % catsEffectVersion,
    "org.typelevel" %%% "cats-effect" % catsEffectVersion
  ).map(_ % Test),
  pomPostProcess := { (node: xml.Node) =>
    new xml.transform.RuleTransformer(new xml.transform.RewriteRule {
      def scopedDependency(e: xml.Elem): Boolean =
        e.label == "dependency" && e.child.exists(_.label == "scope")

      override def transform(node: xml.Node): xml.NodeSeq =
        node match {
          case e: xml.Elem if scopedDependency(e) => Nil
          case _                                  => Seq(node)
        }
    }).transform(node).head
  }
)

lazy val mdocSettings = Seq(
  mdoc := (Compile / run).evaluated,
  scalacOptions --= Seq("-Xfatal-warnings", "-Ywarn-unused"),
  crossScalaVersions := Seq(scalaVersion.value),
  ScalaUnidoc / unidoc / unidocProjectFilter := inProjects(
    core.jvm,
    circe.jvm,
    circeYaml,
    enumeratum.jvm,
    http4s.jvm,
    refined.jvm,
    squants.jvm
  ),
  ScalaUnidoc / unidoc / target := (LocalRootProject / baseDirectory).value / "website" / "static" / "api",
  cleanFiles += (ScalaUnidoc / unidoc / target).value,
  docusaurusCreateSite := docusaurusCreateSite
    .dependsOn(Compile / unidoc)
    .dependsOn(ThisBuild / updateSiteVariables)
    .value,
  docusaurusPublishGhpages :=
    docusaurusPublishGhpages
      .dependsOn(Compile / unidoc)
      .dependsOn(ThisBuild / updateSiteVariables)
      .value,
  libraryDependencies ++= Seq(
    "eu.timepit" %% "refined-cats" % refinedVersion
  ),
  // format: off
  ScalaUnidoc / unidoc / scalacOptions ++= Seq(
    "-doc-source-url", s"https://github.com/vlovgr/ciris/tree/v${(ThisBuild / latestVersion).value}€{FILE_PATH}.scala",
    "-sourcepath", (LocalRootProject / baseDirectory).value.getAbsolutePath,
    "-doc-title", "Ciris",
    "-doc-version", s"v${(ThisBuild / latestVersion).value}",
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
    ThisBuild / latestVersion,
    // format: off
    BuildInfoKey.map(ThisBuild / version) { case (_, v) => "latestSnapshotVersion" -> v },
    BuildInfoKey.map(LocalRootProject / baseDirectory) { case (k, v) => "rootDirectory" -> v },
    BuildInfoKey.map(core.jvm / moduleName) { case (k, v) => "core" ++ k.capitalize -> v },
    BuildInfoKey.map(core.jvm / crossScalaVersions) { case (k, v) => "core" ++ k.capitalize -> v },
    BuildInfoKey.map(core.js / crossScalaVersions) { case (k, v) => "coreJs" ++ k.capitalize -> v },
    BuildInfoKey.map(core.native / crossScalaVersions) { case (k, v) => "coreNative" ++ k.capitalize -> v },
    BuildInfoKey.map(circe.jvm / moduleName) { case (k, v) => "circe" ++ k.capitalize -> v },
    BuildInfoKey.map(circe.jvm / crossScalaVersions) { case (k, v) => "circe" ++ k.capitalize -> v },
    BuildInfoKey.map(circe.js / crossScalaVersions) { case (k, v) => "circeJs" ++ k.capitalize -> v },
    BuildInfoKey.map(circe.native / crossScalaVersions) { case (k, v) => "circeNative" ++ k.capitalize -> v },
    BuildInfoKey.map(circeYaml / moduleName) { case (k, v) => "circeYaml" ++ k.capitalize -> v },
    BuildInfoKey.map(circeYaml / crossScalaVersions) { case (k, v) => "circeYaml" ++ k.capitalize -> v },
    BuildInfoKey.map(enumeratum.jvm / moduleName) { case (k, v) => "enumeratum" ++ k.capitalize -> v },
    BuildInfoKey.map(enumeratum.jvm / crossScalaVersions) { case (k, v) => "enumeratum" ++ k.capitalize -> v },
    BuildInfoKey.map(enumeratum.js / crossScalaVersions) { case (k, v) => "enumeratumJs" ++ k.capitalize -> v },
    BuildInfoKey.map(http4s.jvm / moduleName) { case (k, v) => "http4s" ++ k.capitalize -> v },
    BuildInfoKey.map(http4s.jvm / crossScalaVersions) { case (k, v) => "http4s" ++ k.capitalize -> v },
    BuildInfoKey.map(http4s.js / crossScalaVersions) { case (k, v) => "http4sJs" ++ k.capitalize -> v },
    BuildInfoKey.map(http4s.native / crossScalaVersions) { case (k, v) => "http4sNative" ++ k.capitalize -> v },
    BuildInfoKey.map(refined.jvm / moduleName) { case (k, v) => "refined" ++ k.capitalize -> v },
    BuildInfoKey.map(refined.jvm / crossScalaVersions) { case (k, v) => "refined" ++ k.capitalize -> v },
    BuildInfoKey.map(refined.js / crossScalaVersions) { case (k, v) => "refinedJs" ++ k.capitalize -> v },
    BuildInfoKey.map(refined.native / crossScalaVersions) { case (k, v) => "refinedNative" ++ k.capitalize -> v },
    BuildInfoKey.map(squants.jvm / moduleName) { case (k, v) => "squants" ++ k.capitalize -> v },
    BuildInfoKey.map(squants.jvm / crossScalaVersions) { case (k, v) => "squants" ++ k.capitalize -> v },
    BuildInfoKey.map(squants.js / crossScalaVersions) { case (k, v) => "squantsJs" ++ k.capitalize -> v },
    BuildInfoKey.map(squants.native / crossScalaVersions) { case (k, v) => "squantsNative" ++ k.capitalize -> v },
    LocalRootProject / organization,
    core.jvm / crossScalaVersions,
    BuildInfoKey("catsEffectVersion" -> catsEffectVersion),
    BuildInfoKey("circeVersion" -> circeVersion),
    BuildInfoKey("circeYamlVersion" -> circeYamlVersion),
    BuildInfoKey("enumeratumVersion" -> enumeratumVersion),
    BuildInfoKey("http4sVersion" -> http4sVersion),
    BuildInfoKey("refinedVersion" -> refinedVersion),
    BuildInfoKey("squantsVersion" -> squantsVersion),
    BuildInfoKey("scalaJsMajorMinorVersion" -> scalaJsMajorMinorVersion),
    BuildInfoKey("scalaNativeMajorMinorVersion" -> scalaNativeMajorMinorVersion)
    // format: on
  )
)

lazy val metadataSettings = Seq(
  organization := "is.cir",
  organizationName := "Ciris",
  organizationHomepage := Some(url("https://cir.is"))
)

lazy val publishSettings =
  metadataSettings ++ Seq(
    Test / publishArtifact := false,
    pomIncludeRepository := (_ => false),
    homepage := Some(url("https://cir.is")),
    licenses := Seq("MIT License" -> url("http://www.opensource.org/licenses/mit-license.php")),
    startYear := Some(2017),
    headerLicense := Some(
      de.heikoseeberger.sbtheader.License.MIT(
        s"${startYear.value.get}-${java.time.Year.now}",
        "Viktor Rudebeck",
        HeaderLicenseStyle.SpdxSyntax
      )
    ),
    headerSources / excludeFilter := HiddenFileFilter,
    developers := List(
      Developer(
        id = "vlovgr",
        name = "Viktor Rudebeck",
        email = "github@vlovgr.se",
        url = url("https://vlovgr.se")
      )
    )
  )

lazy val mimaSettings = Seq(
  mimaPreviousArtifacts := {
    val unpublishedModules = Set[String]()
    if (publishArtifact.value && !unpublishedModules.contains(moduleName.value)) {
      Set(organization.value %% moduleName.value % (ThisBuild / previousStableVersion).value.get)
    } else Set()
  },
  mimaBinaryIssueFilters ++= {
    import com.typesafe.tools.mima.core._
    // format: off
    Seq(
      ProblemFilters.exclude[DirectMissingMethodProblem]("ciris.ConfigKey.file"),
      ProblemFilters.exclude[DirectMissingMethodProblem]("ciris.package.file")
    )
    // format: on
  }
)

lazy val noPublishSettings =
  publishSettings ++ Seq(
    publish / skip := true,
    publishArtifact := false
  )

lazy val sharedJsSettings = Seq(
  doctestGenTests := Seq.empty
)

lazy val sharedNativeSettings = Seq(
  crossScalaVersions -= scala212
)

lazy val scalaSettings = Seq(
  scalaVersion := scala213,
  crossScalaVersions := Seq(scala212, scala213),
  scalacOptions ++= {
    val commonScalacOptions =
      Seq(
        "-deprecation",
        "-encoding",
        "UTF-8",
        "-feature",
        "-unchecked",
        "-Xfatal-warnings"
      )

    val scala2ScalacOptions =
      if (scalaVersion.value.startsWith("2.")) {
        Seq(
          "-language:higherKinds",
          "-Xlint",
          "-Ywarn-dead-code",
          "-Ywarn-numeric-widen",
          "-Ywarn-value-discard",
          "-Ywarn-unused"
        )
      } else Seq()

    val scala212ScalacOptions =
      if (scalaVersion.value.startsWith("2.12")) {
        Seq("-Yno-adapted-args", "-Ypartial-unification")
      } else Seq()

    val scala3ScalacOptions =
      if (scalaVersion.value.startsWith("3")) {
        Seq("-Ykind-projector")
      } else Seq()

    commonScalacOptions ++
      scala2ScalacOptions ++
      scala212ScalacOptions ++
      scala3ScalacOptions
  },
  Compile / console / scalacOptions --= Seq("-Xlint", "-Ywarn-unused"),
  Test / console / scalacOptions := (Compile / console / scalacOptions).value
)

lazy val testSettings = Seq(
  Test / logBuffered := false,
  Test / parallelExecution := false,
  Test / testOptions += Tests.Argument("-oDF"),
  Test / scalacOptions --= Seq("-deprecation", "-Xfatal-warnings", "-Xlint", "-Ywarn-unused")
)

def scalaVersionOf(version: String): String = {
  if (version.contains("-")) version
  else {
    val (major, minor) =
      CrossVersion.partialVersion(version).get
    s"$major.$minor"
  }
}

val latestVersion = settingKey[String]("Latest stable released version")
ThisBuild / latestVersion := {
  val snapshot = (ThisBuild / isSnapshot).value
  val stable = (ThisBuild / isVersionStable).value

  if (!snapshot && stable) {
    (ThisBuild / version).value
  } else {
    (ThisBuild / previousStableVersion).value.get
  }
}

val updateSiteVariables = taskKey[Unit]("Update site variables")
ThisBuild / updateSiteVariables := {
  val file =
    (LocalRootProject / baseDirectory).value / "website" / "variables.js"

  val variables =
    Map[String, String](
      "organization" -> (LocalRootProject / organization).value,
      "coreModuleName" -> (core.jvm / moduleName).value,
      "latestVersion" -> (ThisBuild / latestVersion).value,
      "scalaPublishVersions" -> {
        val scalaVersions = (core.jvm / crossScalaVersions).value.map(scalaVersionOf)
        if (scalaVersions.size <= 2) scalaVersions.mkString(" and ")
        else scalaVersions.init.mkString(", ") ++ " and " ++ scalaVersions.last
      },
      "scalaJsMajorMinorVersion" -> scalaJsMajorMinorVersion,
      "scalaNativeMajorMinorVersion" -> scalaNativeMajorMinorVersion
    )

  val fileHeader =
    "// Generated by sbt. Do not edit directly."

  val fileContents =
    variables.toList
      .sortBy { case (key, _) => key }
      .map { case (key, value) => s"  $key: '$value'" }
      .mkString(s"$fileHeader\nmodule.exports = {\n", ",\n", "\n};\n")

  IO.write(file, fileContents)
}

def addCommandsAlias(name: String, values: List[String]) =
  addCommandAlias(name, values.mkString(";", ";", ""))

addCommandsAlias(
  "validate",
  List(
    "+clean",
    "+test",
    "+mimaReportBinaryIssues",
    "scalafmtCheckAll",
    "scalafmtSbtCheck",
    "headerCheckAll",
    "+doc",
    "docs/run"
  )
)
