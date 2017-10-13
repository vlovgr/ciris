import sbt.Keys._
import sbt._
import sbtrelease.ReleasePlugin.autoImport.ReleaseStep
import sbtrelease.ReleaseStateTransformations.reapply
import sbtrelease.Vcs

object LatestVersion extends AutoPlugin {
  object autoImport {
    lazy val latestVersion: SettingKey[String] =
      settingKey[String]("Latest released version")

    lazy val latestBinaryCompatibleVersion: SettingKey[Option[String]] =
      settingKey[Option[String]]("Latest released binary-compatible version")

    lazy val unreleasedModuleNames: SettingKey[Set[String]] =
      settingKey[Set[String]]("Module names which have not yet been released")

    lazy val setLatestVersion: ReleaseStep = { state: State =>
      val extracted = Project.extract(state)

      val newLatestVersion = extracted.get(version in ThisBuild)
      val latestVersionFile = file("latestVersion.sbt")
      val latestVersionFileContents =
        s"""
          |latestVersion in ThisBuild := "$newLatestVersion"
          |latestBinaryCompatibleVersion in ThisBuild := Some("$newLatestVersion")
          |unreleasedModuleNames in ThisBuild := Set()
         """.stripMargin.trim + "\n"

      IO.write(latestVersionFile, latestVersionFileContents)
      Vcs.detect(file(".")).foreach { vcs =>
        vcs.add(latestVersionFile.getPath) !! state.log
        vcs.commit(s"Set latest version to $newLatestVersion", sign = true) !! state.log
      }

      reapply(Seq(latestVersion in ThisBuild := newLatestVersion), state)
    }
  }
}
