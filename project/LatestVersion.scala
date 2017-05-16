import sbt.Keys._
import sbt._
import sbtrelease.ReleasePlugin.autoImport.ReleaseStep
import sbtrelease.ReleaseStateTransformations.reapply
import sbtrelease.Vcs

object LatestVersion extends AutoPlugin {
  object autoImport {
    lazy val latestVersion: SettingKey[String] =
      settingKey[String]("Latest released version")

    lazy val setLatestVersion: ReleaseStep = { state: State =>
      val extracted = Project.extract(state)

      val newLatestVersion = extracted.get(version in ThisBuild)
      val latestVersionFile = file("latestVersion.sbt")
      val latestVersionFileContents = s"""latestVersion in ThisBuild := "$newLatestVersion"""" + "\n"

      IO.write(latestVersionFile, latestVersionFileContents)
      Vcs.detect(file(".")).foreach { vcs =>
        vcs.add(latestVersionFile.getPath) !! state.log
        vcs.commit(s"Set latest version to $newLatestVersion", sign = true) !! state.log
      }

      reapply(Seq(latestVersion in ThisBuild := newLatestVersion), state)
    }
  }
}
