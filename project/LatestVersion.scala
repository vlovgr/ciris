import sbt.Keys._
import sbt._
import sbtrelease.ReleasePlugin.autoImport.ReleaseStep
import sbtrelease.ReleaseStateTransformations.reapply
import sbtrelease.Vcs

object LatestVersion extends AutoPlugin {
  object autoImport {
    lazy val latestVersion: SettingKey[String] =
      settingKey[String]("Latest released version")

    lazy val unreleasedModuleNames: SettingKey[Set[String]] =
      settingKey[Set[String]]("Module names not yet released")

    lazy val binaryCompatibleVersions: SettingKey[Set[String]] =
      settingKey[Set[String]]("Released binary-compatible versions")

    lazy val setLatestVersion: ReleaseStep = { state: State =>
      val extracted = Project.extract(state)

      val newLatestVersion =
        extracted.get(version in ThisBuild)

      state.log.info(s"Setting latest version to '$newLatestVersion'.")

      val newLatestVersionString =
        "\"" + newLatestVersion + "\""

      val newBinaryCompatibleVersions =
        extracted.get(binaryCompatibleVersions in ThisBuild) + newLatestVersion

      val newBinaryCompatibleVersionsString =
        newBinaryCompatibleVersions.toList
          .sortBy { version =>
            val Array(major, minor, patch) =
              version.split('.').map(_.toInt)
            (major, minor, patch)
          }
          .map(version => "\"" + version + "\"")
          .mkString("Set(\n  ", ",\n  ", "\n)")

      val latestVersionFile = file("latestVersion.sbt")
      val latestVersionFileContents =
        s"""
          |latestVersion in ThisBuild := $newLatestVersionString
          |
          |unreleasedModuleNames in ThisBuild := Set()
          |
          |binaryCompatibleVersions in ThisBuild := $newBinaryCompatibleVersionsString
         """.stripMargin.trim + "\n"

      IO.write(latestVersionFile, latestVersionFileContents)
      Vcs.detect(file(".")).foreach { vcs =>
        vcs.add(latestVersionFile.getPath) !! state.log
        vcs.commit(
          s"Set latest version to $newLatestVersion",
          sign = true,
          signOff = false
        ) !! state.log
      }

      reapply(
        Seq(
          latestVersion in ThisBuild := newLatestVersion,
          unreleasedModuleNames in ThisBuild := Set(),
          binaryCompatibleVersions in ThisBuild := newBinaryCompatibleVersions
        ),
        state
      )
    }
  }
}
