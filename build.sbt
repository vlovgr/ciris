val awsRegionsVersion = "1.2.0"
val caseInsensitiveVersion = "1.5.0"
val catsEffectVersion = "3.7.1"
val catsVersion = "2.13.0"
val circeVersion = "0.14.17"
val circeYamlVersion = "0.16.1"
val enumeratumVersion = "1.9.8"
val fs2Version = "3.14.0"
val http4sAwsVersion = "6.7.0"
val http4sVersion = "0.23.38"
val ip4sVersion = "3.8.0"
val munitCatsEffectVersion = "2.2.1"
val refinedVersion = "0.11.4"
val scalaCheckEffectMunitVersion = "2.1.0"
val squantsVersion = "1.8.3"
val vaultVersion = "3.7.0"

val scala212 = "2.12.21"
val scala213 = "3.10.0"
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
      )
    ),
    githubWorkflowBuildMatrixExclusions +=
      MatrixExclude(Map("project" -> "rootNative", "scala" -> "2.12")),
    githubWorkflowBuildSbtStepPreamble := Seq("project ${{ matrix.project }}"),
    githubWorkflowJavaVersions := Seq(JavaSpec.temurin("21")),
    githubWorkflowOSes := Seq("ubuntu-latest"),
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
    organization := "is.cir",
    organizationHomepage := Some(url("https://cir.is")),
    organizationName := "Ciris",
    scalaVersion := scala213,
    startYear := Some(2017),
    tlBaseVersion := "3.15",
    tlSiteKeepFiles := false,
    tlSitePublishBranch := Some("main")
  )
)

lazy val root = tlCrossRootProject
  .aggregate(core, circe, circeYaml, enumeratum, http4s, http4sAws, refined, squants, unidocs)
  .settings(
    headerSettings,
    crossScalaVersions := Nil,
    scalaVersion := (ThisBuild / scalaVersion).value
  )

lazy val core = crossProject(JSPlatform, JVMPlatform, NativePlatform)
  .in(file("modules/core"))
  .settings(
    name := "ciris",
    libraryDependencies ++= Seq(
      "org.typelevel" %%% "cats-core" % catsVersion,
      "org.typelevel" %%% "cats-effect-kernel" % catsEffectVersion,
      "org.typelevel" %%% "cats-kernel" % catsVersion
    ),
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
    libraryDependencies ++= Seq(
      "io.circe" %%% "circe-core" % circeVersion,
      "io.circe" %%% "circe-parser" % circeVersion,
      "org.typelevel" %%% "cats-core" % catsVersion
    ),
    moduleSettings
  )
  .nativeSettings(nativeSettings)

lazy val circeYaml = project
  .in(file("modules/circe-yaml"))
  .dependsOn(core.jvm)
  .settings(
    name := "ciris-circe-yaml",
    libraryDependencies ++= Seq(
      "io.circe" %%% "circe-core" % circeVersion,
      "io.circe" %% "circe-yaml" % circeYamlVersion,
      "org.typelevel" %%% "cats-core" % catsVersion
    ),
    moduleSettings,
    withoutScala212
  )

lazy val enumeratum = crossProject(JSPlatform, JVMPlatform, NativePlatform)
  .in(file("modules/enumeratum"))
  .dependsOn(core)
  .settings(
    name := "ciris-enumeratum",
    libraryDependencies ++= Seq(
      "com.beachape" %%% "enumeratum" % enumeratumVersion,
      "org.typelevel" %%% "cats-core" % catsVersion
    ),
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
    libraryDependencies ++= Seq(
      "com.comcast" %%% "ip4s-core" % ip4sVersion,
      "org.http4s" %%% "http4s-core" % http4sVersion,
      "org.typelevel" %%% "cats-core" % catsVersion
    ),
    moduleSettings
  )
  .jsSettings(Test / scalaJSLinkerConfig ~= (_.withModuleKind(ModuleKind.CommonJSModule)))
  .nativeSettings(nativeSettings)

lazy val http4sAws = crossProject(JSPlatform, JVMPlatform, NativePlatform)
  .in(file("modules/http4s-aws"))
  .dependsOn(http4s)
  .settings(
    name := "ciris-http4s-aws",
    libraryDependencies ++= Seq(
      "co.fs2" %%% "fs2-core" % fs2Version,
      "com.comcast" %%% "ip4s-core" % ip4sVersion,
      "com.magine" %%% "aws-regions" % awsRegionsVersion,
      "com.magine" %%% "http4s-aws" % http4sAwsVersion,
      "io.circe" %%% "circe-core" % circeVersion,
      "org.http4s" %%% "http4s-circe" % http4sVersion,
      "org.http4s" %%% "http4s-client" % http4sVersion,
      "org.http4s" %%% "http4s-core" % http4sVersion,
      "org.typelevel" %%% "case-insensitive" % caseInsensitiveVersion,
      "org.typelevel" %%% "cats-effect-kernel" % catsEffectVersion,
      "org.typelevel" %%% "cats-effect" % catsEffectVersion,
      "org.typelevel" %%% "cats-kernel" % catsVersion,
      "org.typelevel" %%% "vault" % vaultVersion,
      "org.typelevel" %%% "cats-core" % catsVersion
    ),
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
    libraryDependencies ++= Seq(
      "eu.timepit" %%% "refined" % refinedVersion,
      "org.typelevel" %%% "cats-core" % catsVersion
    ),
    moduleSettings
  )
  .nativeSettings(nativeSettings)

lazy val squants = crossProject(JSPlatform, JVMPlatform)
  .in(file("modules/squants"))
  .dependsOn(core)
  .settings(
    name := "ciris-squants",
    libraryDependencies ++= Seq(
      "org.typelevel" %%% "cats-core" % catsVersion,
      "org.typelevel" %%% "squants" % squantsVersion
    ),
    moduleSettings
  )

lazy val docs = project
  .in(file("site"))
  .enablePlugins(TypelevelSitePlugin)
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
  .settings {
    import laika.ast.Image
    import laika.ast.InlineSVGIcon
    import laika.ast.LengthUnit.em
    import laika.ast.LengthUnit.px
    import laika.ast.Path.Root
    import laika.ast.Styles
    import laika.helium.Helium
    import laika.helium.config.ButtonLink
    import laika.helium.config.ColorQuintet
    import laika.helium.config.Favicon
    import laika.helium.config.HeliumIcon
    import laika.helium.config.IconLink
    import laika.helium.config.LinkGroup
    import laika.helium.config.LinkPanel
    import laika.helium.config.ReleaseInfo
    import laika.helium.config.TextLink
    import laika.theme.config.Color
    import org.typelevel.sbt.site.GenericSiteSettings

    Seq(
      crossScalaVersions := Seq(scala213),
      scalacOptions -= "-Wunused",
      libraryDependencies ++= Seq(
        "eu.timepit" %%% "refined-cats" % refinedVersion,
        "org.http4s" %%% "http4s-ember-client" % http4sVersion,
        "org.typelevel" %%% "cats-effect" % catsEffectVersion
      ),
      Compile / packageDoc / mappings := (unidocs / Compile / packageDoc / mappings).value,
      laikaIncludeAPI := true,
      tlSiteApiUrl := None,
      tlSitePreview := tlSitePreview.dependsOn(laikaGenerateAPI).value,
      mdocVariables ++= Map(
        "API_URL" -> "/api/",
        "CATS_EFFECT_VERSION" -> catsEffectVersion,
        "CIRCE_CROSS_SCALA_VERSIONS" -> crossScalaVersionsOf(circe.jvm).value,
        "CIRCE_JS_CROSS_SCALA_VERSIONS" -> crossScalaVersionsOf(circe.js).value,
        "CIRCE_MODULE_NAME" -> (circe.jvm / moduleName).value,
        "CIRCE_NATIVE_CROSS_SCALA_VERSIONS" -> crossScalaVersionsOf(circe.native).value,
        "CIRCE_VERSION" -> circeVersion,
        "CIRCE_YAML_CROSS_SCALA_VERSIONS" -> crossScalaVersionsOf(circeYaml).value,
        "CIRCE_YAML_MODULE_NAME" -> (circeYaml / moduleName).value,
        "CIRCE_YAML_VERSION" -> circeYamlVersion,
        "CORE_CROSS_SCALA_VERSIONS" -> crossScalaVersionsOf(core.jvm).value,
        "CORE_JS_CROSS_SCALA_VERSIONS" -> crossScalaVersionsOf(core.js).value,
        "CORE_MODULE_NAME" -> (core.jvm / moduleName).value,
        "CORE_NATIVE_CROSS_SCALA_VERSIONS" -> crossScalaVersionsOf(core.native).value,
        "ENUMERATUM_CROSS_SCALA_VERSIONS" -> crossScalaVersionsOf(enumeratum.jvm).value,
        "ENUMERATUM_JS_CROSS_SCALA_VERSIONS" -> crossScalaVersionsOf(enumeratum.js).value,
        "ENUMERATUM_MODULE_NAME" -> (enumeratum.jvm / moduleName).value,
        "ENUMERATUM_NATIVE_CROSS_SCALA_VERSIONS" -> crossScalaVersionsOf(enumeratum.native).value,
        "ENUMERATUM_VERSION" -> enumeratumVersion,
        "HTTP4S_CROSS_SCALA_VERSIONS" -> crossScalaVersionsOf(http4s.jvm).value,
        "HTTP4S_JS_CROSS_SCALA_VERSIONS" -> crossScalaVersionsOf(http4s.js).value,
        "HTTP4S_MODULE_NAME" -> (http4s.jvm / moduleName).value,
        "HTTP4S_NATIVE_CROSS_SCALA_VERSIONS" -> crossScalaVersionsOf(http4s.native).value,
        "HTTP4S_VERSION" -> http4sVersion,
        "HTTP4SAWS_CROSS_SCALA_VERSIONS" -> crossScalaVersionsOf(http4sAws.jvm).value,
        "HTTP4SAWS_JS_CROSS_SCALA_VERSIONS" -> crossScalaVersionsOf(http4sAws.js).value,
        "HTTP4SAWS_MODULE_NAME" -> (http4sAws.jvm / moduleName).value,
        "HTTP4SAWS_NATIVE_CROSS_SCALA_VERSIONS" -> crossScalaVersionsOf(http4sAws.native).value,
        "HTTP4SAWS_VERSION" -> http4sAwsVersion,
        "MAJOR_VERSION" -> majorVersion(version.value),
        "ORGANIZATION" -> (ThisBuild / organization).value,
        "REFINED_CROSS_SCALA_VERSIONS" -> crossScalaVersionsOf(refined.jvm).value,
        "REFINED_JS_CROSS_SCALA_VERSIONS" -> crossScalaVersionsOf(refined.js).value,
        "REFINED_MODULE_NAME" -> (refined.jvm / moduleName).value,
        "REFINED_NATIVE_CROSS_SCALA_VERSIONS" -> crossScalaVersionsOf(refined.native).value,
        "REFINED_VERSION" -> refinedVersion,
        "SCALA_DOCS_VERSION" -> scalaVersionOf(scalaVersion.value),
        "SCALA_JS_MAJOR_MINOR_VERSION" -> scalaJsMajorMinorVersion,
        "SCALA_NATIVE_MAJOR_MINOR_VERSION" -> scalaNativeMajorMinorVersion,
        "SCALA_PUBLISH_VERSIONS" -> crossScalaVersionsOf(core.jvm).value,
        "SQUANTS_CROSS_SCALA_VERSIONS" -> crossScalaVersionsOf(squants.jvm).value,
        "SQUANTS_JS_CROSS_SCALA_VERSIONS" -> crossScalaVersionsOf(squants.js).value,
        "SQUANTS_MODULE_NAME" -> (squants.jvm / moduleName).value,
        "SQUANTS_VERSION" -> squantsVersion
      ),
      tlSiteHelium := {
        val gitHubUrl = "https://github.com/vlovgr/ciris"

        val projectLinks = List(
          IconLink.internal(Root / "api" / "ciris" / "index.html", HeliumIcon.api),
          IconLink.external(gitHubUrl, HeliumIcon.github),
          IconLink.external("https://discord.gg/XF3CXcMzqD", HeliumIcon.chat)
        )

        Helium.defaults
          .extendWith(GenericSiteSettings.themeExtensions.value)
          .site
          .metadata(
            title = Some("Ciris · Functional Configurations for Scala"),
            description = Some("Functional Configurations for Scala"),
            authors = developers.value.map(_.name),
            language = Some("en"),
            version = Some(version.value)
          )
          .site
          .themeColors(
            primary = Color.hex("122932"),
            secondary = Color.hex("19647e"),
            primaryMedium = Color.hex("c5d3da"),
            primaryLight = Color.hex("f2f5f6"),
            text = Color.hex("24292e"),
            background = Color.hex("ffffff"),
            bgGradient = (Color.hex("122932"), Color.hex("153243"))
          )
          .site
          .syntaxHighlightingColors(
            base = ColorQuintet(
              Color.hex("f8f8f8"), // background
              Color.hex("999988"), // comments
              Color.hex("333333"), // punctuation
              Color.hex("333333"), // identifiers
              Color.hex("333333") // unclassified
            ),
            wheel = ColorQuintet(
              Color.hex("999999"), // substitutions, annotations
              Color.hex("333333"), // keywords, escape sequences
              Color.hex("990000"), // declaration names
              Color.hex("dd1144"), // literals
              Color.hex("445588") // types
            )
          )
          .site
          .clearFontResources
          .site
          .fontSizes(
            body = px(16),
            code = em(0.85),
            title = px(32),
            header2 = px(24),
            header3 = px(20),
            header4 = px(16),
            small = px(12)
          )
          .site
          .darkMode
          .disabled
          .site
          .layout(topBarHeight = px(50))
          .site
          .topNavigationBar(
            homeLink = IconLink.internal(
              Root / "README",
              InlineSVGIcon(IO.read(file("docs/img/logo.svg"))),
              text = Some("Ciris"),
              options = Styles("home-link")
            ),
            navLinks = projectLinks,
            highContrast = true
          )
          .site
          .internalCSS(Root / "css")
          .site
          .favIcons(Favicon.internal(Root / "img" / "favicon.svg"))
          .site
          .landingPage(
            logo = Some(
              Image.internal(Root / "img" / "logo.svg", height = Some(px(150)), alt = Some(""))
            ),
            title = Some("Ciris"),
            subtitle = Some("Functional Configurations for Scala"),
            latestReleases = tlLatestVersion.value.toList.map(ReleaseInfo("Latest Release", _)),
            license = Some("MIT"),
            titleLinks = Seq(
              ButtonLink.internal(Root / "overview.md", "Documentation"),
              LinkGroup.create(projectLinks.head, projectLinks.tail: _*)
            ),
            linkPanel = Some(
              LinkPanel(
                "Documentation",
                TextLink.internal(Root / "overview.md", "Overview"),
                TextLink.internal(Root / "quick-example.md", "Quick Example"),
                TextLink.internal(Root / "configurations.md", "Configurations"),
                TextLink.internal(Root / "integrations" / "circe.md", "Integrations")
              )
            )
          )
          .site
          .footer(
            "Licensed under the <a href=\"https://github.com/vlovgr/ciris/blob/main/license.txt\">MIT License</a>.<br>" +
              "Copyright © 2017 Viktor Rudebeck."
          )
          .site
          .pageNavigation(sourceBaseURL = Some("https://github.com/vlovgr/ciris/tree/main/docs"))
      }
    )
  }

lazy val unidocs = project
  .enablePlugins(TypelevelUnidocPlugin)
  .settings(
    name := "ciris-docs",
    headerSettings,
    withoutScala212,
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
    ScalaUnidoc / unidoc / scalacOptions ++= {
      val docVersion = tlLatestVersion.value.getOrElse(version.value)
      if (tlIsScala3.value) Seq()
      else Seq("-doc-title", "Ciris", "-doc-version", s"v$docVersion", "-groups")
    }
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

def majorVersion(version: String): String =
  version.split('.').head

def scalaVersionOf(version: String): String = {
  if (version.contains("-")) version
  else {
    val (major, minor) =
      CrossVersion.partialVersion(version).get
    s"$major.$minor"
  }
}

def crossScalaVersionsOf(project: Project): Def.Initialize[String] =
  Def.setting {
    val scalaVersions = (project / crossScalaVersions).value.map(scalaVersionOf)
    if (scalaVersions.size <= 2) scalaVersions.mkString(" and ")
    else scalaVersions.init.mkString(", ") ++ " and " ++ scalaVersions.last
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
