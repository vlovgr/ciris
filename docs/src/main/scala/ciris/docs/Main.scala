package ciris.docs

import ciris.build.info._
import java.nio.file.{FileSystems, Path}
import scala.collection.Seq

object Main {
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

  def main(args: Array[String]): Unit = {
    val scalaMinorVersion = minorVersion(scalaVersion)

    val settings = mdoc
      .MainSettings()
      .withSiteVariables {
        Map(
          "ORGANIZATION" -> organization,
          "CORE_MODULE_NAME" -> coreModuleName,
          "CORE_CROSS_SCALA_VERSIONS" -> minorVersionsString(coreCrossScalaVersions),
          "ENUMERATUM_MODULE_NAME" -> enumeratumModuleName,
          "ENUMERATUM_CROSS_SCALA_VERSIONS" -> minorVersionsString(enumeratumCrossScalaVersions),
          "LATEST_VERSION" -> latestVersion,
          "LATEST_MAJOR_VERSION" -> majorVersion(latestVersion),
          "DOCS_SCALA_MINOR_VERSION" -> scalaMinorVersion,
          "CATS_EFFECT_VERSION" -> catsEffectVersion,
          "ENUMERATUM_VERSION" -> enumeratumVersion,
          "REFINED_VERSION" -> refinedVersion,
          "REFINED_MODULE_NAME" -> refinedModuleName,
          "REFINED_CROSS_SCALA_VERSIONS" -> minorVersionsString(refinedCrossScalaVersions),
          "SCALA_PUBLISH_VERSIONS" -> minorVersionsString(crossScalaVersions),
          "API_BASE_URL" -> s"/api/ciris"
        )
      }
      .withScalacOptions(scalacOptions.mkString(" "))
      .withIn(sourceDirectoryPath("main", "mdoc"))
      .withArgs(args.toList)

    val exitCode = mdoc.Main.process(settings)
    if (exitCode != 0) sys.exit(exitCode)
  }
}
