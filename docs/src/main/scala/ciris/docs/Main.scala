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
        Map(
          "ORGANIZATION" -> organization,
          "CORE_MODULE_NAME" -> coreModuleName,
          "CORE_CROSS_SCALA_VERSIONS" -> scalaVersionsString(coreCrossScalaVersions),
          "CIRCE_MODULE_NAME" -> circeModuleName,
          "CIRCE_CROSS_SCALA_VERSIONS" -> scalaVersionsString(circeCrossScalaVersions),
          "CIRCE_YAML_MODULE_NAME" -> circeYamlModuleName,
          "CIRCE_YAML_CROSS_SCALA_VERSIONS" -> scalaVersionsString(circeYamlCrossScalaVersions),
          "ENUMERATUM_MODULE_NAME" -> enumeratumModuleName,
          "ENUMERATUM_CROSS_SCALA_VERSIONS" -> scalaVersionsString(enumeratumCrossScalaVersions),
          "LATEST_VERSION" -> latestVersion,
          "LATEST_SNAPSHOT_VERSION" -> latestSnapshotVersion,
          "LATEST_MAJOR_VERSION" -> majorVersion(latestVersion),
          "DOCS_SCALA_VERSION" -> scalaDocsVersion,
          "CATS_EFFECT_VERSION" -> catsEffectVersion,
          "CIRCE_VERSION" -> circeVersion,
          "ENUMERATUM_VERSION" -> enumeratumVersion,
          "REFINED_VERSION" -> refinedVersion,
          "REFINED_MODULE_NAME" -> refinedModuleName,
          "REFINED_CROSS_SCALA_VERSIONS" -> scalaVersionsString(refinedCrossScalaVersions),
          "SQUANTS_VERSION" -> squantsVersion,
          "SQUANTS_MODULE_NAME" -> squantsModuleName,
          "SQUANTS_CROSS_SCALA_VERSIONS" -> scalaVersionsString(squantsCrossScalaVersions),
          "SCALA_PUBLISH_VERSIONS" -> scalaVersionsString(crossScalaVersions),
          "TYPENAME_VERSION" -> typeNameVersion,
          "API_BASE_URL" -> s"/api/ciris"
        )
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
