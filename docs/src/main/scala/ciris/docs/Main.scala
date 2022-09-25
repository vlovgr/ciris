package ciris.docs

import ciris.build.info._
import java.nio.file.{FileSystems, Path}
import scala.collection.Seq

object Main {
  def rootDirectoryPath(rest: String*): Path =
    FileSystems.getDefault.getPath(rootDirectory.getAbsolutePath, rest: _*)

  def sourceDirectoryPath(rest: String*): Path =
    FileSystems.getDefault.getPath(sourceDirectory.getAbsolutePath, rest: _*)

  def majorVersion(version: String): String = {
    val parts = version.split('.')
    val major = parts(0)
    major
  }

  def scalaVersionOf(version: String): String = {
    if (version.contains("-")) version
    else {
      val parts = version.split('.')
      val (major, minor) = (parts(0), parts(1))
      s"$major.$minor"
    }
  }

  def scalaVersionsString(versions: Seq[String]): String = {
    val scalaVersions = versions.map(scalaVersionOf)
    if (scalaVersions.size <= 2) scalaVersions.mkString(" and ")
    else scalaVersions.init.mkString(", ") ++ " and " ++ scalaVersions.last
  }

  def run(settings: mdoc.MainSettings): Unit = {
    val exitCode = mdoc.Main.process(settings)
    if (exitCode != 0) sys.exit(exitCode)
  }

  def main(args: Array[String]): Unit = {
    val watchBlog = args.contains("--watch-blog")

    val watch = !watchBlog && args.contains("--watch")

    val mdocArgs =
      args.filter(_ != "--watch-blog").toList ++ {
        if (watchBlog && !args.contains("--watch"))
          List("--watch")
        else Nil
      }

    val scalaDocsVersion =
      scalaVersionOf(scalaVersion)

    val settings = mdoc
      .MainSettings()
      .withSiteVariables {
        // format: off
        Map(
          "API_BASE_URL" -> s"/api/ciris",
          "CATS_EFFECT_VERSION" -> catsEffectVersion,
          "CIRCE_CROSS_SCALA_VERSIONS" -> scalaVersionsString(circeCrossScalaVersions),
          "CIRCE_JS_CROSS_SCALA_VERSIONS" -> scalaVersionsString(circeJsCrossScalaVersions),
          "CIRCE_MODULE_NAME" -> circeModuleName,
          "CIRCE_NATIVE_CROSS_SCALA_VERSIONS" -> scalaVersionsString(circeNativeCrossScalaVersions),
          "CIRCE_VERSION" -> circeVersion,
          "CIRCE_YAML_CROSS_SCALA_VERSIONS" -> scalaVersionsString(circeYamlCrossScalaVersions),
          "CIRCE_YAML_MODULE_NAME" -> circeYamlModuleName,
          "CIRCE_YAML_VERSION" -> circeYamlVersion,
          "CORE_CROSS_SCALA_VERSIONS" -> scalaVersionsString(coreCrossScalaVersions),
          "CORE_JS_CROSS_SCALA_VERSIONS" -> scalaVersionsString(coreJsCrossScalaVersions),
          "CORE_MODULE_NAME" -> coreModuleName,
          "CORE_NATIVE_CROSS_SCALA_VERSIONS" -> scalaVersionsString(coreNativeCrossScalaVersions),
          "DOCS_SCALA_VERSION" -> scalaDocsVersion,
          "ENUMERATUM_CROSS_SCALA_VERSIONS" -> scalaVersionsString(enumeratumCrossScalaVersions),
          "ENUMERATUM_JS_CROSS_SCALA_VERSIONS" -> scalaVersionsString(enumeratumJsCrossScalaVersions),
          "ENUMERATUM_MODULE_NAME" -> enumeratumModuleName,
          "ENUMERATUM_VERSION" -> enumeratumVersion,
          "HTTP4S_CROSS_SCALA_VERSIONS" -> scalaVersionsString(http4sCrossScalaVersions),
          "HTTP4S_JS_CROSS_SCALA_VERSIONS" -> scalaVersionsString(http4sJsCrossScalaVersions),
          "HTTP4S_MODULE_NAME" -> http4sModuleName,
          "HTTP4S_NATIVE_CROSS_SCALA_VERSIONS" -> scalaVersionsString(http4sNativeCrossScalaVersions),
          "HTTP4S_VERSION" -> http4sVersion,
          "LATEST_MAJOR_VERSION" -> majorVersion(latestVersion),
          "LATEST_SNAPSHOT_VERSION" -> latestSnapshotVersion,
          "LATEST_VERSION" -> latestVersion,
          "ORGANIZATION" -> organization,
          "REFINED_CROSS_SCALA_VERSIONS" -> scalaVersionsString(refinedCrossScalaVersions),
          "REFINED_JS_CROSS_SCALA_VERSIONS" -> scalaVersionsString(refinedJsCrossScalaVersions),
          "REFINED_MODULE_NAME" -> refinedModuleName,
          "REFINED_NATIVE_CROSS_SCALA_VERSIONS" -> scalaVersionsString(refinedNativeCrossScalaVersions),
          "REFINED_VERSION" -> refinedVersion,
          "SCALA_JS_MAJOR_MINOR_VERSION" -> scalaJsMajorMinorVersion,
          "SCALA_NATIVE_MAJOR_MINOR_VERSION" -> scalaNativeMajorMinorVersion,
          "SCALA_PUBLISH_VERSIONS" -> scalaVersionsString(crossScalaVersions),
          "SQUANTS_CROSS_SCALA_VERSIONS" -> scalaVersionsString(squantsCrossScalaVersions),
          "SQUANTS_JS_CROSS_SCALA_VERSIONS" -> scalaVersionsString(squantsJsCrossScalaVersions),
          "SQUANTS_MODULE_NAME" -> squantsModuleName,
          "SQUANTS_NATIVE_CROSS_SCALA_VERSIONS" -> scalaVersionsString(squantsNativeCrossScalaVersions),
          "SQUANTS_VERSION" -> squantsVersion
        )
        // format: on
      }
      .withScalacOptions(scalacOptions.mkString(" "))
      .withIn(sourceDirectoryPath("main", "mdoc"))
      .withArgs(mdocArgs)

    val blogSettings =
      settings
        .withIn(rootDirectoryPath("blog"))
        .withOut(rootDirectoryPath("website", "blog"))

    if (watchBlog) {
      run(blogSettings)
    } else if (watch) {
      run(settings)
    } else {
      run(settings)
      run(blogSettings)
    }
  }
}
