package ciris

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import java.nio.charset.{Charset, StandardCharsets}
import java.nio.file.{Files, Path, StandardOpenOption}
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen

final class CirisSpec extends BaseSpec {
  test("default") {
    forAll { value: String =>
      assert {
        default(value).to[IO].use(IO.pure).unsafeRunSync() match {
          case ConfigEntry.Default(ConfigError.Empty, default) => default() === value
          case _                                               => false
        }
      }
    }
  }

  test("env") {
    val envGen: Gen[String] =
      Gen.oneOf(
        Gen.oneOf(sys.env.keys.toList),
        arbitrary[String]
      )

    forAll(envGen) { name: String =>
      assert {
        val description = ConfigKey.env(name).description

        env(name).to[IO].use(IO.pure).unsafeRunSync() match {
          case ConfigEntry.Loaded(ConfigError.Loaded, Some(ConfigKey(`description`)), value) =>
            sys.env.get(name).contains(value)

          case ConfigEntry.Failed(ConfigError.Missing(ConfigKey(`description`))) =>
            !sys.env.contains(name)

          case _ =>
            false
        }
      }
    }
  }

  test("file.path") {
    def existingFile(content: String): Path = {
      val path = Files.createTempFile("test-", ".tmp")
      path.toFile.deleteOnExit()
      Files.write(path, content.getBytes(StandardCharsets.UTF_8), StandardOpenOption.WRITE)
      path
    }

    val nonExistingFile: Path = {
      val path = Files.createTempFile("test-", ".tmp")
      path.toFile.delete()
      path
    }

    val pathContentGen: Gen[(Path, String)] =
      for {
        charset <- charsetGen
        content <- Gen.alphaNumStr
        path <- Gen.oneOf(
          Gen.const(existingFile(content)),
          Gen.const(nonExistingFile)
        )
      } yield (path, content)

    forAll(pathContentGen) { case (path, content) =>
      assert {
        val description = ConfigKey.file(path, StandardCharsets.UTF_8).description
        file(path).to[IO].use(IO.pure).unsafeRunSync() match {
          case ConfigEntry.Loaded(ConfigError.Loaded, Some(ConfigKey(`description`)), value) =>
            value === content

          case ConfigEntry.Failed(ConfigError.Missing(ConfigKey(`description`))) =>
            !path.toFile.exists()

          case _ =>
            false
        }
      }
    }
  }

  test("file.path charset") {
    def existingFile(charset: Charset, content: String): Path = {
      val path = Files.createTempFile("test-", ".tmp")
      path.toFile.deleteOnExit()
      Files.write(path, content.getBytes(charset), StandardOpenOption.WRITE)
      path
    }

    val nonExistingFile: Path = {
      val path = Files.createTempFile("test-", ".tmp")
      path.toFile.delete()
      path
    }

    val pathContentCharsetGen: Gen[(Path, String, Charset)] =
      for {
        charset <- charsetGen
        content <- Gen.alphaNumStr
        path <- Gen.oneOf(
          Gen.const(existingFile(charset, content)),
          Gen.const(nonExistingFile)
        )
      } yield (path, content, charset)

    forAll(pathContentCharsetGen) { case (path, content, charset) =>
      assert {
        val description = ConfigKey.file(path, charset).description
        file(path, charset).to[IO].use(IO.pure).unsafeRunSync() match {
          case ConfigEntry.Loaded(ConfigError.Loaded, Some(ConfigKey(`description`)), value) =>
            value === content

          case ConfigEntry.Failed(ConfigError.Missing(ConfigKey(`description`))) =>
            !path.toFile.exists()

          case _ =>
            false
        }
      }
    }
  }

  test("prop") {
    val propGen: Gen[String] =
      Gen.oneOf(
        Gen.oneOf(sys.props.keys.toList),
        arbitrary[String]
      )

    forAll(propGen) { name: String =>
      assert {
        val description = ConfigKey.prop(name).description

        prop(name).to[IO].use(IO.pure).unsafeRunSync() match {
          case ConfigEntry.Loaded(ConfigError.Loaded, Some(ConfigKey(`description`)), value) =>
            sys.props.get(name).contains(value)

          case ConfigEntry.Failed(ConfigError.Missing(ConfigKey(`description`))) =>
            name.isEmpty || !sys.props.contains(name)

          case _ =>
            false
        }
      }
    }
  }
}
