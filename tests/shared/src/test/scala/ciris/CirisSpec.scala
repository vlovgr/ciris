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

      "be able to lift variables with envF" in {
        import _root_.cats.implicits._
        import ciris.cats._

        if(sys.env.nonEmpty) {
          forAll(Gen.oneOf(sys.env.keys.toList)) { key =>
            envF[List, String](key).value shouldBe List(Right(sys.env(key)))
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

      "should be able to suspend the reading with propF" in {
        import _root_.cats.effect.IO
        import ciris.cats.effect._

        if(sys.props.nonEmpty) {
          forAll(Gen.oneOf(sys.props.keys.toList)) { key =>
            val value = sys.props(key)

            try {
              val value = propF[IO, String](key).value
              sys.props.remove(key)
              value.unsafeRunSync() shouldBe a[Left[_, _]]
            } finally {
              sys.props.put(key, value)
              ()
            }
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

      "be able to lift arguments with argF" in {
        import _root_.cats.implicits._
        import ciris.cats._

        forAll(sized(arbitrary[immutable.IndexedSeq[String]])) { args =>
          whenever(args.nonEmpty) {
            forAll(Gen.chooseNum(0, args.length - 1), minSuccessful(10)) { index =>
              argF[List, String](args)(index).value shouldBe a[List[_]]
            }
          }
        }
      }

      "return a failure for non-existing indexes" in {
        forAll(sized(arbitrary[immutable.IndexedSeq[String]])) { args =>
          forAll({
            Gen.oneOf(
              Gen.chooseNum(Int.MinValue, -1),
              Gen.chooseNum(args.length, Int.MaxValue)
            )
          }, minSuccessful(10)){ index =>
            arg[String](args)(index).value shouldBe a[Left[_, _]]
          }
        }
      }
    }
  }
}
