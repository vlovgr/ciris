val catsEffectVersion = "3.1.1"

val circeVersion = "0.13.0"

val enumeratumVersion = "1.6.1"

val refinedVersion = "0.9.25"

val squantsVersion = "1.8.0"

val typeNameVersion = "1.0.0"

val scala212 = "2.12.13"

val scala213 = "2.13.6"

val scala3 = "3.0.0"

ThisBuild / versionScheme := Some("early-semver")

lazy val ciris = project
  .in(file("."))
  .settings(
    mimaSettings,
    scalaSettings,
    noPublishSettings,
    console := (core / Compile / console).value,
    Test / console := (core / Test / console).value
  )
  .aggregate(core, circe, enumeratum, refined, squants)

lazy val core = project
  .in(file("modules/core"))
  .settings(
    moduleName := "ciris",
    name := moduleName.value,
    dependencySettings ++ Seq(
      libraryDependencies += "org.typelevel" %% "cats-effect-kernel" % catsEffectVersion
    ),
    publishSettings,
    mimaSettings,
    scalaSettings ++ Seq(
      crossScalaVersions += scala3
    ),
    testSettings
  )

lazy val circe = project
  .in(file("modules/circe"))
  .settings(
    moduleName := "ciris-circe",
    name := moduleName.value,
    dependencySettings ++ Seq(
      libraryDependencies += "io.circe" %% "circe-parser" % circeVersion
    ),
    publishSettings,
    mimaSettings,
    scalaSettings,
    testSettings
  )
  .dependsOn(core)

lazy val enumeratum = project
  .in(file("modules/enumeratum"))
  .settings(
    moduleName := "ciris-enumeratum",
    name := moduleName.value,
    dependencySettings ++ Seq(
      libraryDependencies ++= Seq(
        "com.beachape" %% "enumeratum" % enumeratumVersion,
        "org.tpolecat" %% "typename" % typeNameVersion
      )
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
      libraryDependencies ++= Seq(
        "eu.timepit" %% "refined" % refinedVersion,
        "org.tpolecat" %% "typename" % typeNameVersion
      )
    ),
    publishSettings,
    mimaSettings,
    scalaSettings ++ Seq(
      crossScalaVersions += scala3
    ),
    testSettings
  )
  .dependsOn(core)

lazy val squants = project
  .in(file("modules/squants"))
  .settings(
    moduleName := "ciris-squants",
    name := moduleName.value,
    dependencySettings ++ Seq(
      libraryDependencies += "org.typelevel" %% "squants" % squantsVersion
    ),
    publishSettings,
    mimaSettings ++ Seq(
      mimaPreviousArtifacts := Set()
    ),
    scalaSettings,
    testSettings
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
  .dependsOn(core, circe, enumeratum, refined, squants)
  .enablePlugins(BuildInfoPlugin, DocusaurusPlugin, MdocPlugin, ScalaUnidocPlugin)

lazy val dependencySettings = Seq(
  libraryDependencies ++= {
    if (scalaVersion.value.startsWith("3")) Nil
    else
      Seq(compilerPlugin(("org.typelevel" %% "kind-projector" % "0.13.0").cross(CrossVersion.full)))
  },
  libraryDependencies ++= Seq(
    "org.typelevel" %% "discipline-scalatest" % "2.1.5",
    "org.typelevel" %% "cats-effect" % catsEffectVersion,
    "org.typelevel" %% "cats-effect-laws" % catsEffectVersion,
    "org.typelevel" %% "cats-testkit-scalatest" % "2.1.5",
    "commons-codec" % "commons-codec" % "1.15"
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
    core,
    circe,
    enumeratum,
    refined,
    squants
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
    BuildInfoKey.map(ThisBuild / version) { case (_, v) =>
      "latestSnapshotVersion" -> v
    },
    BuildInfoKey.map(LocalRootProject / baseDirectory) { case (k, v) =>
      "rootDirectory" -> v
    },
    BuildInfoKey.map(core / moduleName) { case (k, v) =>
      "core" ++ k.capitalize -> v
    },
    BuildInfoKey.map(core / crossScalaVersions) { case (k, v) =>
      "core" ++ k.capitalize -> v
    },
    BuildInfoKey.map(circe / moduleName) { case (k, v) =>
      "circe" ++ k.capitalize -> v
    },
    BuildInfoKey.map(circe / crossScalaVersions) { case (k, v) =>
      "circe" ++ k.capitalize -> v
    },
    BuildInfoKey.map(enumeratum / moduleName) { case (k, v) =>
      "enumeratum" ++ k.capitalize -> v
    },
    BuildInfoKey.map(enumeratum / crossScalaVersions) { case (k, v) =>
      "enumeratum" ++ k.capitalize -> v
    },
    BuildInfoKey.map(refined / moduleName) { case (k, v) =>
      "refined" ++ k.capitalize -> v
    },
    BuildInfoKey.map(refined / crossScalaVersions) { case (k, v) =>
      "refined" ++ k.capitalize -> v
    },
    BuildInfoKey.map(squants / moduleName) { case (k, v) =>
      "squants" ++ k.capitalize -> v
    },
    BuildInfoKey.map(squants / crossScalaVersions) { case (k, v) =>
      "squants" ++ k.capitalize -> v
    },
    LocalRootProject / organization,
    core / crossScalaVersions,
    BuildInfoKey("catsEffectVersion" -> catsEffectVersion),
    BuildInfoKey("circeVersion" -> circeVersion),
    BuildInfoKey("enumeratumVersion" -> enumeratumVersion),
    BuildInfoKey("refinedVersion" -> refinedVersion),
    BuildInfoKey("squantsVersion" -> squantsVersion),
    BuildInfoKey("typeNameVersion" -> typeNameVersion)
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
        "Viktor Lövgren",
        HeaderLicenseStyle.SpdxSyntax
      )
    ),
    headerSources / excludeFilter := HiddenFileFilter,
    developers := List(
      Developer(
        id = "vlovgr",
        name = "Viktor Lövgren",
        email = "github@vlovgr.se",
        url = url("https://vlovgr.se")
      )
    )
  )

lazy val mimaSettings = Seq(
  mimaPreviousArtifacts := {
    if (publishArtifact.value) {
      //Set(organization.value %% moduleName.value % (previousStableVersion in ThisBuild).value.get)
      Set()
    } else Set()
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
    publish / skip := true,
    publishArtifact := false
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
      "coreModuleName" -> (core / moduleName).value,
      "latestVersion" -> (ThisBuild / latestVersion).value,
      "scalaPublishVersions" -> {
        val scalaVersions = (core / crossScalaVersions).value.map(scalaVersionOf)
        if (scalaVersions.size <= 2) scalaVersions.mkString(" and ")
        else scalaVersions.init.mkString(", ") ++ " and " ++ scalaVersions.last
      }
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
    "+scalafmtCheck",
    "scalafmtSbtCheck",
    "+headerCheck",
    "+doc",
    "docs/run"
  )
)
