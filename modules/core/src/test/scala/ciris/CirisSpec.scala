package ciris

import org.scalacheck.{Gen, Shrink}
import org.scalacheck.Arbitrary.arbitrary

import scala.collection.immutable

final class CirisSpec extends PropertySpec {
  implicit def noShrink[A]: Shrink[A] = Shrink.apply(_ => Stream.empty)

  "Ciris" when {
    "loading environment variables" should {
      "be able to load all variables as string" in {
        if (sys.env.nonEmpty) {
          forAll(Gen.oneOf(sys.env.keys.toList)) { key =>
            env[String](key).value shouldBe Right(sys.env(key))
          }
        }
      }

      "return a failure for non-existing variables" in {
        forAll { key: String =>
          whenever(!sys.env.contains(key)) {
            env[String](key).value shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "loading system properties" should {
      "be able to load all properties as string" in {
        if (sys.props.nonEmpty) {
          forAll(Gen.oneOf(sys.props.keys.toList)) { key =>
            prop[String](key).value shouldBe Right(sys.props(key))
          }
        }
      }

      "return a failure for the empty property" in {
        prop[String]("").value shouldBe a[Left[_, _]]
      }

      "return a failure for non-existing properties" in {
        forAll { key: String =>
          whenever(key.nonEmpty && !sys.props.contains(key)) {
            prop[String](key).value shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "loading command-line arguments" should {
      "be able to load all arguments as string" in {
        forAll(sized(arbitrary[immutable.IndexedSeq[String]])) { args =>
          whenever(args.nonEmpty) {
            forAll(Gen.chooseNum(0, args.length - 1), minSuccessful(10)) { index =>
              arg[String](args)(index).value shouldBe a[Right[_, _]]
            }
          }
        }
      }
    }

    "loading files" when {
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
}
