package ciris

final class CirisPlatformSpecificSpec extends JvmPropertySpec {
  "CirisPlatformSpecific" when {
    "converting to String" should {
      "be File for ConfigSource.File" in {
        ConfigSource.File.toString shouldBe "File"
      }
    }

    "reading a file" when {
      "the file exists" when {
        "the type can be read" should {
          "return the expected value" in {
            forAll { value: Int =>
              withFile(s"$value\n") { (file, charset) =>
                val fileName = file.toPath.toAbsolutePath.toString
                fileWithName[Int](fileName, _.trim).value shouldBe Right(value)
              }
            }
          }

          "return the expected value, suspending reading" in {
            import _root_.cats.effect.IO
            import ciris.cats.effect._

            forAll { value: Int =>
              withFile(s"$value\n") { (file, charset) =>
                val fileName = file.toPath.toAbsolutePath.toString
                val contents = fileWithNameSync[IO, Int](fileName, _.trim).value
                withFileOverwritten(file)(s"${value + 1}\n") {
                  contents.unsafeRunSync() shouldBe Right(value + 1)
                }
              }
            }
          }
        }

        "the type cannot be read" should {
          "fail with an error" in {
            forAll { value: Int =>
              withFile(s"$value\n") { (file, charset) =>
                val fileName = file.toPath.toAbsolutePath.toString
                fileWithName[Int](fileName).value shouldBe a[Left[_, _]]
              }
            }
          }
        }
      }

      "the file does not exist" should {
        "fail with an error" in {
          fileWithName[Int]("/tmp/does-not-exist").value shouldBe a[Left[_, _]]
        }
      }
    }
  }
}
