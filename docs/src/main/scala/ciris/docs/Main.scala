package ciris.docs

import ciris.build.info._
import java.nio.file.{FileSystems, Path}
import scala.collection.Seq

object Main {
  def rootDirectoryPath(rest: String*): Path =
    FileSystems.getDefault.getPath(rootDirectory.getAbsolutePath, rest: _*)

  def sourceDirectoryPath(rest: String*): Path =
    FileSystems.getDefault.getPath(sourceDirectory.getAbsolutePath, rest: _*)

  def minorVersion(version: String): String = {
    val Array(major, minor, _) = version.split('.')
    s"$major.$minor"
  }

  def majorVersion(version: String): String = {
    val Array(major, _, _) = version.split('.')
    major
  }

  def minorVersionsString(versions: Seq[String]): String = {
    val minorVersions = versions.map(minorVersion)
    if (minorVersions.size <= 2) minorVersions.mkString(" and ")
    else minorVersions.init.mkString(", ") ++ " and " ++ minorVersions.last
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

    val scalaMinorVersion = minorVersion(scalaVersion)

    val settings = mdoc
      .MainSettings()
      .withSiteVariables {
        Map(
          "ORGANIZATION" -> organization,
          "CORE_MODULE_NAME" -> coreModuleName,
          "CORE_CROSS_SCALA_VERSIONS" -> minorVersionsString(coreCrossScalaVersions),
          "CIRCE_MODULE_NAME" -> circeModuleName,
          "CIRCE_CROSS_SCALA_VERSIONS" -> minorVersionsString(circeCrossScalaVersions),
          "ENUMERATUM_MODULE_NAME" -> enumeratumModuleName,
          "ENUMERATUM_CROSS_SCALA_VERSIONS" -> minorVersionsString(enumeratumCrossScalaVersions),
          "LATEST_VERSION" -> latestVersion,
          "LATEST_SNAPSHOT_VERSION" -> latestSnapshotVersion,
          "LATEST_MAJOR_VERSION" -> majorVersion(latestVersion),
          "DOCS_SCALA_MINOR_VERSION" -> scalaMinorVersion,
          "CATS_EFFECT_VERSION" -> catsEffectVersion,
          "CIRCE_VERSION" -> circeVersion,
          "ENUMERATUM_VERSION" -> enumeratumVersion,
          "REFINED_VERSION" -> refinedVersion,
          "REFINED_MODULE_NAME" -> refinedModuleName,
          "REFINED_CROSS_SCALA_VERSIONS" -> minorVersionsString(refinedCrossScalaVersions),
          "SQUANTS_VERSION" -> squantsVersion,
          "SQUANTS_MODULE_NAME" -> squantsModuleName,
          "SQUANTS_CROSS_SCALA_VERSIONS" -> minorVersionsString(squantsCrossScalaVersions),
          "SCALA_PUBLISH_VERSIONS" -> minorVersionsString(crossScalaVersions),
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
