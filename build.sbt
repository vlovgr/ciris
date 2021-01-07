val catsEffectVersion = "3.0.0-M5"

val circeVersion = "0.13.0"

val enumeratumVersion = "1.6.1"

val refinedVersion = "0.9.20"

val squantsVersion = "1.7.0"

val typeNameVersion = "0.1.1"

val scala212 = "2.12.10"

val scala213 = "2.13.4"

val scala3 = "3.0.0-M2"

lazy val ciris = project
  .in(file("."))
  .settings(
    mimaSettings,
    scalaSettings,
    noPublishSettings,
    console := (console in (core, Compile)).value,
    console in Test := (console in (core, Test)).value
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
    if (isDotty.value) Nil
    else
      Seq(compilerPlugin(("org.typelevel" %% "kind-projector" % "0.11.2").cross(CrossVersion.full)))
  },
  libraryDependencies ++= Seq(
    "org.typelevel" %% "discipline-scalatest" % "2.1.1",
    "org.typelevel" %% "cats-effect" % catsEffectVersion,
    "org.typelevel" %% "cats-effect-laws" % catsEffectVersion,
    "org.typelevel" %% "cats-testkit-scalatest" % "2.1.0",
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
  mdoc := run.in(Compile).evaluated,
  scalacOptions --= Seq("-Xfatal-warnings", "-Ywarn-unused"),
  crossScalaVersions := Seq(scalaVersion.value),
  unidocProjectFilter in (ScalaUnidoc, unidoc) := inProjects(
    core,
    circe,
    enumeratum,
    refined,
    squants
  ),
  target in (ScalaUnidoc, unidoc) := (baseDirectory in LocalRootProject).value / "website" / "static" / "api",
  cleanFiles += (target in (ScalaUnidoc, unidoc)).value,
  docusaurusCreateSite := docusaurusCreateSite
    .dependsOn(unidoc in Compile)
    .dependsOn(updateSiteVariables in ThisBuild)
    .value,
  docusaurusPublishGhpages :=
    docusaurusPublishGhpages
      .dependsOn(unidoc in Compile)
      .dependsOn(updateSiteVariables in ThisBuild)
      .value,
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
    BuildInfoKey.map(version in ThisBuild) { case (_, v) =>
      "latestSnapshotVersion" -> v
    },
    BuildInfoKey.map(baseDirectory in LocalRootProject) { case (k, v) =>
      "rootDirectory" -> v
    },
    BuildInfoKey.map(moduleName in core) { case (k, v) =>
      "core" ++ k.capitalize -> v
    },
    BuildInfoKey.map(crossScalaVersions in core) { case (k, v) =>
      "core" ++ k.capitalize -> v
    },
    BuildInfoKey.map(moduleName in circe) { case (k, v) =>
      "circe" ++ k.capitalize -> v
    },
    BuildInfoKey.map(crossScalaVersions in circe) { case (k, v) =>
      "circe" ++ k.capitalize -> v
    },
    BuildInfoKey.map(moduleName in enumeratum) { case (k, v) =>
      "enumeratum" ++ k.capitalize -> v
    },
    BuildInfoKey.map(crossScalaVersions in enumeratum) { case (k, v) =>
      "enumeratum" ++ k.capitalize -> v
    },
    BuildInfoKey.map(moduleName in refined) { case (k, v) =>
      "refined" ++ k.capitalize -> v
    },
    BuildInfoKey.map(crossScalaVersions in refined) { case (k, v) =>
      "refined" ++ k.capitalize -> v
    },
    BuildInfoKey.map(moduleName in squants) { case (k, v) =>
      "squants" ++ k.capitalize -> v
    },
    BuildInfoKey.map(crossScalaVersions in squants) { case (k, v) =>
      "squants" ++ k.capitalize -> v
    },
    organization in LocalRootProject,
    crossScalaVersions in core,
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
    publishArtifact in Test := false,
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
    Seq(
      ProblemFilters.exclude[IncompatibleResultTypeProblem]("ciris.ConfigValue.to"),
      ProblemFilters.exclude[ReversedMissingMethodProblem]("ciris.ConfigValue.to")
    )
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
      if (!isDotty.value) {
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
      if (isDotty.value) {
        Seq("-Ykind-projector")
      } else Seq()

    commonScalacOptions ++
      scala2ScalacOptions ++
      scala212ScalacOptions ++
      scala3ScalacOptions
  },
  scalacOptions in (Compile, console) --= Seq("-Xlint", "-Ywarn-unused"),
  scalacOptions in (Test, console) := (scalacOptions in (Compile, console)).value,
  scalafmtCheck in Compile := {
    if (isDotty.value) true
    else (scalafmtCheck in Compile).value
  }
)

lazy val testSettings = Seq(
  logBuffered in Test := false,
  parallelExecution in Test := false,
  testOptions in Test += Tests.Argument("-oDF"),
  scalacOptions in Test --= Seq("-deprecation", "-Xfatal-warnings", "-Xlint", "-Ywarn-unused")
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
latestVersion in ThisBuild := {
  val snapshot = (isSnapshot in ThisBuild).value
  val stable = (isVersionStable in ThisBuild).value

  if (!snapshot && stable) {
    (version in ThisBuild).value
  } else {
    (previousStableVersion in ThisBuild).value.get
  }
}

val updateSiteVariables = taskKey[Unit]("Update site variables")
updateSiteVariables in ThisBuild := {
  val file =
    (baseDirectory in LocalRootProject).value / "website" / "variables.js"

  val variables =
    Map[String, String](
      "organization" -> (organization in LocalRootProject).value,
      "coreModuleName" -> (moduleName in core).value,
      "latestVersion" -> (latestVersion in ThisBuild).value,
      "scalaPublishVersions" -> {
        val scalaVersions = (crossScalaVersions in core).value.map(scalaVersionOf)
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
