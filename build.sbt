import scala.scalanative.build.Mode

val catsEffectVersion = "3.7.1"
val circeVersion = "0.14.17"
val circeYamlVersion = "0.16.1"
val enumeratumVersion = "1.9.7"
val http4sAwsVersion = "6.5.0"
val http4sVersion = "0.23.38"
val munitCatsEffectVersion = "2.2.1"
val refinedVersion = "0.11.3"
val scalaCheckEffectMunitVersion = "2.1.0"
val squantsVersion = "1.8.3"

val scala212 = "2.12.21"
val scala213 = "2.13.18"
val scala3 = "3.3.8"

inThisBuild(
  Seq(
    crossScalaVersions := Seq(scala212, scala213, scala3),
    developers := List(tlGitHubDev("vlovgr", "Viktor Rudebeck")),
    doctestTestFramework := DoctestTestFramework.Munit,
    githubWorkflowBuild := Seq(
      WorkflowStep.Sbt(
        List(
          "++ ${{ matrix.scala }} test",
          "++ ${{ matrix.scala }} mimaReportBinaryIssues",
          "++ ${{ matrix.scala }} doc"
        )
      ),
      WorkflowStep.Sbt(
        List("headerCheckAll", "scalafmtCheckAll", "project /", "scalafmtSbtCheck"),
        cond = Some("matrix.scala == '2.13'")
      ),
      WorkflowStep.Sbt(
        List("docs/run", "docs/unidoc"),
        cond = Some("matrix.scala == '2.13' && matrix.project == 'rootJVM'")
      )
    ),
    githubWorkflowBuildMatrixExclusions +=
      MatrixExclude(Map("project" -> "rootNative", "scala" -> "2.12")),
    githubWorkflowBuildSbtStepPreamble := Seq("project ${{ matrix.project }}"),
    githubWorkflowJavaVersions := Seq(JavaSpec.temurin("21")),
    githubWorkflowOSes := Seq("ubuntu-latest"),
    githubWorkflowPublish += WorkflowStep.Sbt(
      List("docs/docusaurusPublishGhpages"),
      name = Some("Publish website"),
      env = Map("GIT_DEPLOY_KEY" -> "${{ secrets.GIT_DEPLOY_KEY }}")
    ),
    homepage := Some(url("https://cir.is")),
    licenses := Seq("MIT License" -> url("http://www.opensource.org/licenses/mit-license.php")),
    mimaBinaryIssueFilters ++= {
      import com.typesafe.tools.mima.core._
      Seq(
        ProblemFilters.exclude[IncompatibleMethTypeProblem](
          "ciris.http4s.aws.AwsSsmParameters.fromSigningClient"
        )
      )
    },
    nativeConfig ~= (_.withMode(Mode.debug)),
    organization := "is.cir",
    organizationHomepage := Some(url("https://cir.is")),
    organizationName := "Ciris",
    scalaVersion := scala213,
    startYear := Some(2017),
    tlBaseVersion := "3.15"
  )
)

lazy val root = tlCrossRootProject
  .aggregate(core, circe, circeYaml, enumeratum, http4s, http4sAws, refined, squants)
  .settings(
    headerSettings,
    crossScalaVersions := Nil,
    scalaVersion := (ThisBuild / scalaVersion).value
  )

lazy val core = crossProject(JSPlatform, JVMPlatform, NativePlatform)
  .in(file("modules/core"))
  .settings(
    name := "ciris",
    libraryDependencies += "org.typelevel" %%% "cats-effect-kernel" % catsEffectVersion,
    moduleSettings,
    scalacOptions ++= {
      if (scalaVersion.value.startsWith("2.13")) Seq("-Wconf:cat=lint-overload&msg=mistake:s")
      else Seq()
    },
    headerSources / excludeFilter :=
      HiddenFileFilter ||
        "*GeneralDigest.scala" ||
        "*Pack.scala" ||
        "*SHA1Digest.scala"
  )
  .jsSettings(doctestGenTests := Seq.empty)
  .nativeSettings(nativeSettings)

lazy val circe = crossProject(JSPlatform, JVMPlatform, NativePlatform)
  .in(file("modules/circe"))
  .dependsOn(core)
  .settings(
    name := "ciris-circe",
    libraryDependencies += "io.circe" %%% "circe-parser" % circeVersion,
    moduleSettings
  )
  .nativeSettings(nativeSettings)

lazy val circeYaml = project
  .in(file("modules/circe-yaml"))
  .dependsOn(core.jvm)
  .settings(
    name := "ciris-circe-yaml",
    libraryDependencies += "io.circe" %% "circe-yaml" % circeYamlVersion,
    moduleSettings,
    withoutScala212
  )

lazy val enumeratum = crossProject(JSPlatform, JVMPlatform, NativePlatform)
  .in(file("modules/enumeratum"))
  .dependsOn(core)
  .settings(
    name := "ciris-enumeratum",
    libraryDependencies += "com.beachape" %%% "enumeratum" % enumeratumVersion,
    moduleSettings,
    Test / scalacOptions ++= (if (tlIsScala3.value) Seq("-Yretain-trees") else Seq()),
    tlVersionIntroduced := Map("3" -> "3.1.0")
  )
  .nativeSettings(
    nativeSettings,
    tlVersionIntroduced := Map("2.13" -> "3.14.1", "3" -> "3.14.1")
  )

lazy val http4s = crossProject(JSPlatform, JVMPlatform, NativePlatform)
  .in(file("modules/http4s"))
  .dependsOn(core)
  .settings(
    name := "ciris-http4s",
    libraryDependencies += "org.http4s" %%% "http4s-core" % http4sVersion,
    moduleSettings
  )
  .jsSettings(Test / scalaJSLinkerConfig ~= (_.withModuleKind(ModuleKind.CommonJSModule)))
  .nativeSettings(nativeSettings)

lazy val http4sAws = crossProject(JSPlatform, JVMPlatform, NativePlatform)
  .in(file("modules/http4s-aws"))
  .dependsOn(http4s)
  .settings(
    name := "ciris-http4s-aws",
    libraryDependencies += "com.magine" %%% "http4s-aws" % http4sAwsVersion,
    moduleSettings,
    withoutScala212
  )
  .jvmSettings(tlVersionIntroduced := Map("2.13" -> "3.8.0", "3" -> "3.8.0"))
  .jsSettings(tlVersionIntroduced := Map("2.13" -> "3.10.0", "3" -> "3.10.0"))
  .nativeSettings(
    nativeSettings,
    tlVersionIntroduced := Map("2.13" -> "3.14.0", "3" -> "3.14.0")
  )

lazy val refined = crossProject(JSPlatform, JVMPlatform, NativePlatform)
  .in(file("modules/refined"))
  .dependsOn(core)
  .settings(
    name := "ciris-refined",
    libraryDependencies += "eu.timepit" %%% "refined" % refinedVersion,
    moduleSettings
  )
  .nativeSettings(nativeSettings)

lazy val squants = crossProject(JSPlatform, JVMPlatform)
  .in(file("modules/squants"))
  .dependsOn(core)
  .settings(
    name := "ciris-squants",
    libraryDependencies += "org.typelevel" %%% "squants" % squantsVersion,
    moduleSettings
  )

lazy val docs = project
  .in(file("docs"))
  .enablePlugins(BuildInfoPlugin, DocusaurusPlugin, MdocPlugin, NoPublishPlugin, ScalaUnidocPlugin)
  .dependsOn(
    core.jvm,
    circe.jvm,
    circeYaml,
    enumeratum.jvm,
    http4s.jvm,
    http4sAws.jvm,
    refined.jvm,
    squants.jvm
  )
  .settings(
    name := "ciris-docs",
    crossScalaVersions := Seq(scala213),
    scalacOptions -= "-Wunused",
    libraryDependencies ++= Seq(
      "eu.timepit" %% "refined-cats" % refinedVersion,
      "org.http4s" %% "http4s-ember-client" % http4sVersion,
      "org.typelevel" %% "cats-effect" % catsEffectVersion
    ),
    mdoc := (Compile / run).evaluated,
    buildInfoPackage := "ciris.build",
    buildInfoObject := "info",
    buildInfoKeys := Seq[BuildInfoKey](
      scalaVersion,
      scalacOptions,
      sourceDirectory,
      LocalRootProject / organization,
      ThisBuild / latestVersion,
      core.jvm / crossScalaVersions,
      BuildInfoKey.map(ThisBuild / version) { case (_, v) => "latestSnapshotVersion" -> v },
      BuildInfoKey.map(LocalRootProject / baseDirectory) { case (_, v) => "rootDirectory" -> v },
      BuildInfoKey("catsEffectVersion" -> catsEffectVersion),
      BuildInfoKey("circeVersion" -> circeVersion),
      BuildInfoKey("circeYamlVersion" -> circeYamlVersion),
      BuildInfoKey("enumeratumVersion" -> enumeratumVersion),
      BuildInfoKey("http4sVersion" -> http4sVersion),
      BuildInfoKey("http4sAwsVersion" -> http4sAwsVersion),
      BuildInfoKey("refinedVersion" -> refinedVersion),
      BuildInfoKey("squantsVersion" -> squantsVersion),
      BuildInfoKey("scalaJsMajorMinorVersion" -> scalaJsMajorMinorVersion),
      BuildInfoKey("scalaNativeMajorMinorVersion" -> scalaNativeMajorMinorVersion)
    ) ++
      moduleInfo("core", core) ++
      moduleInfo("circe", circe) ++
      moduleInfo("circeYaml", circeYaml) ++
      moduleInfo("enumeratum", enumeratum) ++
      moduleInfo("http4s", http4s) ++
      moduleInfo("http4sAws", http4sAws) ++
      moduleInfo("refined", refined) ++
      moduleInfo("squants", squants),
    docusaurusVersion := DocusaurusVersion.V1,
    docusaurusCreateSite := docusaurusCreateSite
      .dependsOn(Compile / unidoc)
      .dependsOn(ThisBuild / updateSiteVariables)
      .value,
    docusaurusPublishGhpages := docusaurusPublishGhpages
      .dependsOn(Compile / unidoc)
      .dependsOn(ThisBuild / updateSiteVariables)
      .value,
    ScalaUnidoc / unidoc / unidocProjectFilter := inProjects(
      core.jvm,
      circe.jvm,
      circeYaml,
      enumeratum.jvm,
      http4s.jvm,
      http4sAws.jvm,
      refined.jvm,
      squants.jvm
    ),
    ScalaUnidoc / unidoc / target := (LocalRootProject / baseDirectory).value / "website" / "static" / "api",
    cleanFiles += (ScalaUnidoc / unidoc / target).value,
    // format: off
    ScalaUnidoc / unidoc / scalacOptions ++= Seq(
      "-doc-source-url", s"https://github.com/vlovgr/ciris/tree/v${(ThisBuild / latestVersion).value}/€{FILE_PATH}.scala",
      "-sourcepath", (LocalRootProject / baseDirectory).value.getAbsolutePath,
      "-doc-title", "Ciris",
      "-doc-version", s"v${(ThisBuild / latestVersion).value}",
      "-groups"
    )
    // format: on
  )

lazy val headerSettings = Seq(
  headerLicense := Some(
    HeaderLicense.MIT(
      startYear.value.get.toString,
      "Viktor Rudebeck",
      HeaderLicenseStyle.SpdxSyntax
    )
  )
)

lazy val moduleSettings = headerSettings ++ Seq(
  libraryDependencies ++= Seq(
    "org.typelevel" %%% "cats-effect" % catsEffectVersion,
    "org.typelevel" %%% "cats-effect-laws" % catsEffectVersion,
    "org.typelevel" %%% "munit-cats-effect" % munitCatsEffectVersion,
    "org.typelevel" %%% "scalacheck-effect-munit" % scalaCheckEffectMunitVersion
  ).map(_ % Test)
)

lazy val nativeSettings = withoutScala212 ++ Seq(
  tlVersionIntroduced := Map("2.13" -> "3.13.0", "3" -> "3.13.0")
)

lazy val withoutScala212 = Seq(
  crossScalaVersions := Seq(scala213, scala3),
  scalaVersion := scala213
)

def moduleInfo(prefix: String, project: CompositeProject): Seq[BuildInfoKey] =
  project.componentProjects.flatMap { p =>
    val platform = p.id.stripPrefix(prefix) match {
      case "" | "JVM" => ""
      case "JS"       => "Js"
      case other      => other
    }

    val moduleNameKey =
      if (platform.nonEmpty) Seq()
      else Seq(BuildInfoKey.map(p / moduleName) { case (_, v) => s"${prefix}ModuleName" -> v })

    moduleNameKey :+ BuildInfoKey.map(p / crossScalaVersions) { case (_, v) =>
      s"$prefix${platform}CrossScalaVersions" -> v
    }
  }

def scalaVersionOf(version: String): String = {
  if (version.contains("-")) version
  else {
    val (major, minor) =
      CrossVersion.partialVersion(version).get
    s"$major.$minor"
  }
}

lazy val scalaJsMajorMinorVersion =
  """"org.scala-js" % "sbt-scalajs" % "([^"]+)"""".r
    .findFirstMatchIn(IO.read(file("project/plugins.sbt")))
    .map(_.group(1))
    .flatMap(CrossVersion.partialVersion)
    .map { case (major, minor) => s"$major.$minor" }
    .getOrElse(throw new MessageOnlyException("Unable to determine Scala.js plugin version."))

lazy val scalaNativeMajorMinorVersion =
  """"org.scala-native" % "sbt-scala-native" % "([^"]+)"""".r
    .findFirstMatchIn(IO.read(file("project/plugins.sbt")))
    .map(_.group(1))
    .flatMap(CrossVersion.partialVersion)
    .map { case (major, minor) => s"$major.$minor" }
    .getOrElse(throw new MessageOnlyException("Unable to determine Scala Native plugin version."))

val latestVersion = settingKey[String]("Latest stable released version")
ThisBuild / latestVersion := tlLatestVersion.value
  .getOrElse(throw new IllegalStateException("No tagged version found"))

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
