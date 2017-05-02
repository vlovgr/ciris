package ciris

import org.scalacheck.Gen

final class CirisSpec extends PropertySpec {
  "Ciris" when {
    "loading environment variables" should {
      "be able to load all variables as string" in {
        forAll(Gen.oneOf(sys.env.keys.toList)) { key =>
          env[String](key).value shouldBe Right(sys.env(key))
        }
      }

      "return a failure for non-existing variables" in {
        forAll { key: String ⇒
          whenever(!sys.env.contains(key)) {
            env[String](key).value shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "loading system properties" should {
      "be able to load all properties as string" in {
        forAll(Gen.oneOf(sys.props.keys.toList)) { key =>
          prop[String](key).value shouldBe Right(sys.props(key))
        }
      }

      "return a failure for the empty property" in {
        prop[String]("").value shouldBe a[Left[_, _]]
      }

      "return a failure for non-existing properties" in {
        forAll { key: String ⇒
          whenever(key.nonEmpty && !sys.props.contains(key)) {
            prop[String](key).value shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "loading configurations" when {
      implicit val source =
        sourceWith(
          "key1" → "value1",
          "key2" → "value2",
          "key3" → "value3",
          "key4" → "value4",
          "key5" → "value5",
          "key6" → "value6",
          "key7" → "value7",
          "key8" → "value8",
          "key9" → "value9",
          "key10" → "value10",
          "key11" → "value11",
          "key12" → "value12",
          "key13" → "value13",
          "key14" → "value14",
          "key15" → "value15",
          "key16" → "value16",
          "key17" → "value17",
          "key18" → "value18",
          "key19" → "value19",
          "key20" → "value20",
          "key21" → "value21",
          "key22" → "value22"
        )

      "loading 2 keys" should {
        "be able to load" in {
          loadConfig(
            read[String]("key1"),
            read[String]("key2")
          )((v1, v2) ⇒ (v1, v2)) shouldBe Right(("value1", "value2"))
        }

        "fail to load if one is missing" in {
          loadConfig(
            read[String]("key1"),
            read[String]("akey2")
          )((v1, v2) ⇒ (v1, v2)) shouldBe a[Left[_, _]]
        }

        "fail to load if one type is wrong" in {
          loadConfig(
            read[String]("key1"),
            read[Int]("key2")
          )((v1, v2) ⇒ (v1, v2)) shouldBe a[Left[_, _]]
        }

        "accumulate the errors" in {
          loadConfig(
            read[Int]("key1"),
            read[Int]("key2")
          )((v1, v2) ⇒ (v1, v2)).left.map(_.size) shouldBe Left(2)
        }
      }

      "loading 3 keys" should {
        "be able to load" in {
          loadConfig(
            read[String]("key1"),
            read[String]("key2"),
            read[String]("key3")
          )((v1, v2, v3) ⇒ (v1, v2, v3)) shouldBe Right(("value1", "value2", "value3"))
        }

        "fail to load if one is missing" in {
          loadConfig(
            read[String]("key1"),
            read[String]("akey2"),
            read[String]("key3")
          )((v1, v2, v3) ⇒ (v1, v2, v3)) shouldBe a[Left[_, _]]
        }

        "fail to load if one type is wrong" in {
          loadConfig(
            read[String]("key1"),
            read[Int]("key2"),
            read[String]("key3")
          )((v1, v2, v3) ⇒ (v1, v2, v3)) shouldBe a[Left[_, _]]
        }

        "accumulate the errors" in {
          loadConfig(
            read[Int]("key1"),
            read[Int]("key2"),
            read[Int]("key3")
          )((v1, v2, v3) ⇒ (v1, v2, v3)).left.map(_.size) shouldBe Left(3)
        }
      }

      "loading 4 keys" should {
        "be able to load" in {
          loadConfig(
            read[String]("key1"),
            read[String]("key2"),
            read[String]("key3"),
            read[String]("key4")
          )((v1, v2, v3, v4) ⇒ (v1, v2, v3, v4)) shouldBe Right(("value1", "value2", "value3", "value4"))
        }

        "fail to load if one is missing" in {
          loadConfig(
            read[String]("key1"),
            read[String]("akey2"),
            read[String]("key3"),
            read[String]("key4")
          )((v1, v2, v3, v4) ⇒ (v1, v2, v3, v4)) shouldBe a[Left[_, _]]
        }

        "fail to load if one type is wrong" in {
          loadConfig(
            read[String]("key1"),
            read[Int]("key2"),
            read[String]("key3"),
            read[String]("key4")
          )((v1, v2, v3, v4) ⇒ (v1, v2, v3, v4)) shouldBe a[Left[_, _]]
        }

        "accumulate the errors" in {
          loadConfig(
            read[Int]("key1"),
            read[Int]("key2"),
            read[Int]("key3"),
            read[Int]("key4")
          )((v1, v2, v3, v4) ⇒ (v1, v2, v3, v4)).left.map(_.size) shouldBe Left(4)
        }
      }

      "loading 5 keys" should {
        "be able to load" in {
          loadConfig(
            read[String]("key1"),
            read[String]("key2"),
            read[String]("key3"),
            read[String]("key4"),
            read[String]("key5")
          )((v1, v2, v3, v4, v5) ⇒ (v1, v2, v3, v4, v5)) shouldBe Right(("value1", "value2", "value3", "value4", "value5"))
        }

        "fail to load if one is missing" in {
          loadConfig(
            read[String]("key1"),
            read[String]("akey2"),
            read[String]("key3"),
            read[String]("key4"),
            read[String]("key5")
          )((v1, v2, v3, v4, v5) ⇒ (v1, v2, v3, v4, v5)) shouldBe a[Left[_, _]]
        }

        "fail to load if one type is wrong" in {
          loadConfig(
            read[String]("key1"),
            read[Int]("key2"),
            read[String]("key3"),
            read[String]("key4"),
            read[String]("key5")
          )((v1, v2, v3, v4, v5) ⇒ (v1, v2, v3, v4, v5)) shouldBe a[Left[_, _]]
        }

        "accumulate the errors" in {
          loadConfig(
            read[Int]("key1"),
            read[Int]("key2"),
            read[Int]("key3"),
            read[Int]("key4"),
            read[Int]("key5")
          )((v1, v2, v3, v4, v5) ⇒ (v1, v2, v3, v4, v5)).left.map(_.size) shouldBe Left(5)
        }
      }

      "loading 6 keys" should {
        "be able to load" in {
          loadConfig(
            read[String]("key1"),
            read[String]("key2"),
            read[String]("key3"),
            read[String]("key4"),
            read[String]("key5"),
            read[String]("key6")
          )((v1, v2, v3, v4, v5, v6) ⇒ (v1, v2, v3, v4, v5, v6)) shouldBe Right(("value1", "value2", "value3", "value4", "value5", "value6"))
        }

        "fail to load if one is missing" in {
          loadConfig(
            read[String]("key1"),
            read[String]("akey2"),
            read[String]("key3"),
            read[String]("key4"),
            read[String]("key5"),
            read[String]("key6")
          )((v1, v2, v3, v4, v5, v6) ⇒ (v1, v2, v3, v4, v5, v6)) shouldBe a[Left[_, _]]
        }

        "fail to load if one type is wrong" in {
          loadConfig(
            read[String]("key1"),
            read[Int]("key2"),
            read[String]("key3"),
            read[String]("key4"),
            read[String]("key5"),
            read[String]("key6")
          )((v1, v2, v3, v4, v5, v6) ⇒ (v1, v2, v3, v4, v5, v6)) shouldBe a[Left[_, _]]
        }

        "accumulate the errors" in {
          loadConfig(
            read[Int]("key1"),
            read[Int]("key2"),
            read[Int]("key3"),
            read[Int]("key4"),
            read[Int]("key5"),
            read[Int]("key6")
          )((v1, v2, v3, v4, v5, v6) ⇒ (v1, v2, v3, v4, v5, v6)).left.map(_.size) shouldBe Left(6)
        }
      }

      "loading 7 keys" should {
        "be able to load" in {
          loadConfig(
            read[String]("key1"),
            read[String]("key2"),
            read[String]("key3"),
            read[String]("key4"),
            read[String]("key5"),
            read[String]("key6"),
            read[String]("key7")
          )((v1, v2, v3, v4, v5, v6, v7) ⇒ (v1, v2, v3, v4, v5, v6, v7)) shouldBe Right(("value1", "value2", "value3", "value4", "value5", "value6", "value7"))
        }

        "fail to load if one is missing" in {
          loadConfig(
            read[String]("key1"),
            read[String]("akey2"),
            read[String]("key3"),
            read[String]("key4"),
            read[String]("key5"),
            read[String]("key6"),
            read[String]("key7")
          )((v1, v2, v3, v4, v5, v6, v7) ⇒ (v1, v2, v3, v4, v5, v6, v7)) shouldBe a[Left[_, _]]
        }

        "fail to load if one type is wrong" in {
          loadConfig(
            read[String]("key1"),
            read[Int]("key2"),
            read[String]("key3"),
            read[String]("key4"),
            read[String]("key5"),
            read[String]("key6"),
            read[String]("key7")
          )((v1, v2, v3, v4, v5, v6, v7) ⇒ (v1, v2, v3, v4, v5, v6, v7)) shouldBe a[Left[_, _]]
        }

        "accumulate the errors" in {
          loadConfig(
            read[Int]("key1"),
            read[Int]("key2"),
            read[Int]("key3"),
            read[Int]("key4"),
            read[Int]("key5"),
            read[Int]("key6"),
            read[Int]("key7")
          )((v1, v2, v3, v4, v5, v6, v7) ⇒ (v1, v2, v3, v4, v5, v6, v7)).left.map(_.size) shouldBe Left(7)
        }
      }

      "loading 8 keys" should {
        "be able to load" in {
          loadConfig(
            read[String]("key1"),
            read[String]("key2"),
            read[String]("key3"),
            read[String]("key4"),
            read[String]("key5"),
            read[String]("key6"),
            read[String]("key7"),
            read[String]("key8")
          )((v1, v2, v3, v4, v5, v6, v7, v8) ⇒ (v1, v2, v3, v4, v5, v6, v7, v8)) shouldBe Right(("value1", "value2", "value3", "value4", "value5", "value6", "value7", "value8"))
        }

        "fail to load if one is missing" in {
          loadConfig(
            read[String]("key1"),
            read[String]("akey2"),
            read[String]("key3"),
            read[String]("key4"),
            read[String]("key5"),
            read[String]("key6"),
            read[String]("key7"),
            read[String]("key8")
          )((v1, v2, v3, v4, v5, v6, v7, v8) ⇒ (v1, v2, v3, v4, v5, v6, v7, v8)) shouldBe a[Left[_, _]]
        }

        "fail to load if one type is wrong" in {
          loadConfig(
            read[String]("key1"),
            read[Int]("key2"),
            read[String]("key3"),
            read[String]("key4"),
            read[String]("key5"),
            read[String]("key6"),
            read[String]("key7"),
            read[String]("key8")
          )((v1, v2, v3, v4, v5, v6, v7, v8) ⇒ (v1, v2, v3, v4, v5, v6, v7, v8)) shouldBe a[Left[_, _]]
        }

        "accumulate the errors" in {
          loadConfig(
            read[Int]("key1"),
            read[Int]("key2"),
            read[Int]("key3"),
            read[Int]("key4"),
            read[Int]("key5"),
            read[Int]("key6"),
            read[Int]("key7"),
            read[Int]("key8")
          )((v1, v2, v3, v4, v5, v6, v7, v8) ⇒ (v1, v2, v3, v4, v5, v6, v7, v8)).left.map(_.size) shouldBe Left(8)
        }
      }

      "loading 9 keys" should {
        "be able to load" in {
          loadConfig(
            read[String]("key1"),
            read[String]("key2"),
            read[String]("key3"),
            read[String]("key4"),
            read[String]("key5"),
            read[String]("key6"),
            read[String]("key7"),
            read[String]("key8"),
            read[String]("key9")
          )((v1, v2, v3, v4, v5, v6, v7, v8, v9) ⇒ (v1, v2, v3, v4, v5, v6, v7, v8, v9)) shouldBe Right(("value1", "value2", "value3", "value4", "value5", "value6", "value7", "value8", "value9"))
        }

        "fail to load if one is missing" in {
          loadConfig(
            read[String]("key1"),
            read[String]("akey2"),
            read[String]("key3"),
            read[String]("key4"),
            read[String]("key5"),
            read[String]("key6"),
            read[String]("key7"),
            read[String]("key8"),
            read[String]("key9")
          )((v1, v2, v3, v4, v5, v6, v7, v8, v9) ⇒ (v1, v2, v3, v4, v5, v6, v7, v8, v9)) shouldBe a[Left[_, _]]
        }

        "fail to load if one type is wrong" in {
          loadConfig(
            read[String]("key1"),
            read[Int]("key2"),
            read[String]("key3"),
            read[String]("key4"),
            read[String]("key5"),
            read[String]("key6"),
            read[String]("key7"),
            read[String]("key8"),
            read[String]("key9")
          )((v1, v2, v3, v4, v5, v6, v7, v8, v9) ⇒ (v1, v2, v3, v4, v5, v6, v7, v8, v9)) shouldBe a[Left[_, _]]
        }

        "accumulate the errors" in {
          loadConfig(
            read[Int]("key1"),
            read[Int]("key2"),
            read[Int]("key3"),
            read[Int]("key4"),
            read[Int]("key5"),
            read[Int]("key6"),
            read[Int]("key7"),
            read[Int]("key8"),
            read[Int]("key9")
          )((v1, v2, v3, v4, v5, v6, v7, v8, v9) ⇒ (v1, v2, v3, v4, v5, v6, v7, v8, v9)).left.map(_.size) shouldBe Left(9)
        }
      }

      "loading 10 keys" should {
        "be able to load" in {
          loadConfig(
            read[String]("key1"),
            read[String]("key2"),
            read[String]("key3"),
            read[String]("key4"),
            read[String]("key5"),
            read[String]("key6"),
            read[String]("key7"),
            read[String]("key8"),
            read[String]("key9"),
            read[String]("key10")
          )((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10) ⇒ (v1, v2, v3, v4, v5, v6, v7, v8, v9, v10)) shouldBe Right(("value1", "value2", "value3", "value4", "value5", "value6", "value7", "value8", "value9", "value10"))
        }

        "fail to load if one is missing" in {
          loadConfig(
            read[String]("key1"),
            read[String]("akey2"),
            read[String]("key3"),
            read[String]("key4"),
            read[String]("key5"),
            read[String]("key6"),
            read[String]("key7"),
            read[String]("key8"),
            read[String]("key9"),
            read[String]("key10")
          )((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10) ⇒ (v1, v2, v3, v4, v5, v6, v7, v8, v9, v10)) shouldBe a[Left[_, _]]
        }

        "fail to load if one type is wrong" in {
          loadConfig(
            read[String]("key1"),
            read[Int]("key2"),
            read[String]("key3"),
            read[String]("key4"),
            read[String]("key5"),
            read[String]("key6"),
            read[String]("key7"),
            read[String]("key8"),
            read[String]("key9"),
            read[String]("key10")
          )((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10) ⇒ (v1, v2, v3, v4, v5, v6, v7, v8, v9, v10)) shouldBe a[Left[_, _]]
        }

        "accumulate the errors" in {
          loadConfig(
            read[Int]("key1"),
            read[Int]("key2"),
            read[Int]("key3"),
            read[Int]("key4"),
            read[Int]("key5"),
            read[Int]("key6"),
            read[Int]("key7"),
            read[Int]("key8"),
            read[Int]("key9"),
            read[Int]("key10")
          )((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10) ⇒ (v1, v2, v3, v4, v5, v6, v7, v8, v9, v10)).left.map(_.size) shouldBe Left(10)
        }
      }

      "loading 11 keys" should {
        "be able to load" in {
          loadConfig(
            read[String]("key1"),
            read[String]("key2"),
            read[String]("key3"),
            read[String]("key4"),
            read[String]("key5"),
            read[String]("key6"),
            read[String]("key7"),
            read[String]("key8"),
            read[String]("key9"),
            read[String]("key10"),
            read[String]("key11")
          )((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11) ⇒ (v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11)) shouldBe Right(("value1", "value2", "value3", "value4", "value5", "value6", "value7", "value8", "value9", "value10", "value11"))
        }

        "fail to load if one is missing" in {
          loadConfig(
            read[String]("key1"),
            read[String]("akey2"),
            read[String]("key3"),
            read[String]("key4"),
            read[String]("key5"),
            read[String]("key6"),
            read[String]("key7"),
            read[String]("key8"),
            read[String]("key9"),
            read[String]("key10"),
            read[String]("key11")
          )((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11) ⇒ (v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11)) shouldBe a[Left[_, _]]
        }

        "fail to load if one type is wrong" in {
          loadConfig(
            read[String]("key1"),
            read[Int]("key2"),
            read[String]("key3"),
            read[String]("key4"),
            read[String]("key5"),
            read[String]("key6"),
            read[String]("key7"),
            read[String]("key8"),
            read[String]("key9"),
            read[String]("key10"),
            read[String]("key11")
          )((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11) ⇒ (v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11)) shouldBe a[Left[_, _]]
        }

        "accumulate the errors" in {
          loadConfig(
            read[Int]("key1"),
            read[Int]("key2"),
            read[Int]("key3"),
            read[Int]("key4"),
            read[Int]("key5"),
            read[Int]("key6"),
            read[Int]("key7"),
            read[Int]("key8"),
            read[Int]("key9"),
            read[Int]("key10"),
            read[Int]("key11")
          )((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11) ⇒ (v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11)).left.map(_.size) shouldBe Left(11)
        }
      }

      "loading 12 keys" should {
        "be able to load" in {
          loadConfig(
            read[String]("key1"),
            read[String]("key2"),
            read[String]("key3"),
            read[String]("key4"),
            read[String]("key5"),
            read[String]("key6"),
            read[String]("key7"),
            read[String]("key8"),
            read[String]("key9"),
            read[String]("key10"),
            read[String]("key11"),
            read[String]("key12")
          )((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12) ⇒ (v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12)) shouldBe Right(("value1", "value2", "value3", "value4", "value5", "value6", "value7", "value8", "value9", "value10", "value11", "value12"))
        }

        "fail to load if one is missing" in {
          loadConfig(
            read[String]("key1"),
            read[String]("akey2"),
            read[String]("key3"),
            read[String]("key4"),
            read[String]("key5"),
            read[String]("key6"),
            read[String]("key7"),
            read[String]("key8"),
            read[String]("key9"),
            read[String]("key10"),
            read[String]("key11"),
            read[String]("key12")
          )((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12) ⇒ (v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12)) shouldBe a[Left[_, _]]
        }

        "fail to load if one type is wrong" in {
          loadConfig(
            read[String]("key1"),
            read[Int]("key2"),
            read[String]("key3"),
            read[String]("key4"),
            read[String]("key5"),
            read[String]("key6"),
            read[String]("key7"),
            read[String]("key8"),
            read[String]("key9"),
            read[String]("key10"),
            read[String]("key11"),
            read[String]("key12")
          )((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12) ⇒ (v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12)) shouldBe a[Left[_, _]]
        }

        "accumulate the errors" in {
          loadConfig(
            read[Int]("key1"),
            read[Int]("key2"),
            read[Int]("key3"),
            read[Int]("key4"),
            read[Int]("key5"),
            read[Int]("key6"),
            read[Int]("key7"),
            read[Int]("key8"),
            read[Int]("key9"),
            read[Int]("key10"),
            read[Int]("key11"),
            read[Int]("key12")
          )((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12) ⇒ (v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12)).left.map(_.size) shouldBe Left(12)
        }
      }

      "loading 13 keys" should {
        "be able to load" in {
          loadConfig(
            read[String]("key1"),
            read[String]("key2"),
            read[String]("key3"),
            read[String]("key4"),
            read[String]("key5"),
            read[String]("key6"),
            read[String]("key7"),
            read[String]("key8"),
            read[String]("key9"),
            read[String]("key10"),
            read[String]("key11"),
            read[String]("key12"),
            read[String]("key13")
          )((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13) ⇒ (v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13)) shouldBe Right(("value1", "value2", "value3", "value4", "value5", "value6", "value7", "value8", "value9", "value10", "value11", "value12", "value13"))
        }

        "fail to load if one is missing" in {
          loadConfig(
            read[String]("key1"),
            read[String]("akey2"),
            read[String]("key3"),
            read[String]("key4"),
            read[String]("key5"),
            read[String]("key6"),
            read[String]("key7"),
            read[String]("key8"),
            read[String]("key9"),
            read[String]("key10"),
            read[String]("key11"),
            read[String]("key12"),
            read[String]("key13")
          )((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13) ⇒ (v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13)) shouldBe a[Left[_, _]]
        }

        "fail to load if one type is wrong" in {
          loadConfig(
            read[String]("key1"),
            read[Int]("key2"),
            read[String]("key3"),
            read[String]("key4"),
            read[String]("key5"),
            read[String]("key6"),
            read[String]("key7"),
            read[String]("key8"),
            read[String]("key9"),
            read[String]("key10"),
            read[String]("key11"),
            read[String]("key12"),
            read[String]("key13")
          )((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13) ⇒ (v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13)) shouldBe a[Left[_, _]]
        }

        "accumulate the errors" in {
          loadConfig(
            read[Int]("key1"),
            read[Int]("key2"),
            read[Int]("key3"),
            read[Int]("key4"),
            read[Int]("key5"),
            read[Int]("key6"),
            read[Int]("key7"),
            read[Int]("key8"),
            read[Int]("key9"),
            read[Int]("key10"),
            read[Int]("key11"),
            read[Int]("key12"),
            read[Int]("key13")
          )((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13) ⇒ (v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13)).left.map(_.size) shouldBe Left(13)
        }
      }

      "loading 14 keys" should {
        "be able to load" in {
          loadConfig(
            read[String]("key1"),
            read[String]("key2"),
            read[String]("key3"),
            read[String]("key4"),
            read[String]("key5"),
            read[String]("key6"),
            read[String]("key7"),
            read[String]("key8"),
            read[String]("key9"),
            read[String]("key10"),
            read[String]("key11"),
            read[String]("key12"),
            read[String]("key13"),
            read[String]("key14")
          )((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14) ⇒ (v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14)) shouldBe Right(("value1", "value2", "value3", "value4", "value5", "value6", "value7", "value8", "value9", "value10", "value11", "value12", "value13", "value14"))
        }

        "fail to load if one is missing" in {
          loadConfig(
            read[String]("key1"),
            read[String]("akey2"),
            read[String]("key3"),
            read[String]("key4"),
            read[String]("key5"),
            read[String]("key6"),
            read[String]("key7"),
            read[String]("key8"),
            read[String]("key9"),
            read[String]("key10"),
            read[String]("key11"),
            read[String]("key12"),
            read[String]("key13"),
            read[String]("key14")
          )((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14) ⇒ (v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14)) shouldBe a[Left[_, _]]
        }

        "fail to load if one type is wrong" in {
          loadConfig(
            read[String]("key1"),
            read[Int]("key2"),
            read[String]("key3"),
            read[String]("key4"),
            read[String]("key5"),
            read[String]("key6"),
            read[String]("key7"),
            read[String]("key8"),
            read[String]("key9"),
            read[String]("key10"),
            read[String]("key11"),
            read[String]("key12"),
            read[String]("key13"),
            read[String]("key14")
          )((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14) ⇒ (v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14)) shouldBe a[Left[_, _]]
        }

        "accumulate the errors" in {
          loadConfig(
            read[Int]("key1"),
            read[Int]("key2"),
            read[Int]("key3"),
            read[Int]("key4"),
            read[Int]("key5"),
            read[Int]("key6"),
            read[Int]("key7"),
            read[Int]("key8"),
            read[Int]("key9"),
            read[Int]("key10"),
            read[Int]("key11"),
            read[Int]("key12"),
            read[Int]("key13"),
            read[Int]("key14")
          )((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14) ⇒ (v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14)).left.map(_.size) shouldBe Left(14)
        }
      }

      "loading 15 keys" should {
        "be able to load" in {
          loadConfig(
            read[String]("key1"),
            read[String]("key2"),
            read[String]("key3"),
            read[String]("key4"),
            read[String]("key5"),
            read[String]("key6"),
            read[String]("key7"),
            read[String]("key8"),
            read[String]("key9"),
            read[String]("key10"),
            read[String]("key11"),
            read[String]("key12"),
            read[String]("key13"),
            read[String]("key14"),
            read[String]("key15")
          )((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15) ⇒ (v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15)) shouldBe Right(("value1", "value2", "value3", "value4", "value5", "value6", "value7", "value8", "value9", "value10", "value11", "value12", "value13", "value14", "value15"))
        }

        "fail to load if one is missing" in {
          loadConfig(
            read[String]("key1"),
            read[String]("akey2"),
            read[String]("key3"),
            read[String]("key4"),
            read[String]("key5"),
            read[String]("key6"),
            read[String]("key7"),
            read[String]("key8"),
            read[String]("key9"),
            read[String]("key10"),
            read[String]("key11"),
            read[String]("key12"),
            read[String]("key13"),
            read[String]("key14"),
            read[String]("key15")
          )((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15) ⇒ (v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15)) shouldBe a[Left[_, _]]
        }

        "fail to load if one type is wrong" in {
          loadConfig(
            read[String]("key1"),
            read[Int]("key2"),
            read[String]("key3"),
            read[String]("key4"),
            read[String]("key5"),
            read[String]("key6"),
            read[String]("key7"),
            read[String]("key8"),
            read[String]("key9"),
            read[String]("key10"),
            read[String]("key11"),
            read[String]("key12"),
            read[String]("key13"),
            read[String]("key14"),
            read[String]("key15")
          )((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15) ⇒ (v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15)) shouldBe a[Left[_, _]]
        }

        "accumulate the errors" in {
          loadConfig(
            read[Int]("key1"),
            read[Int]("key2"),
            read[Int]("key3"),
            read[Int]("key4"),
            read[Int]("key5"),
            read[Int]("key6"),
            read[Int]("key7"),
            read[Int]("key8"),
            read[Int]("key9"),
            read[Int]("key10"),
            read[Int]("key11"),
            read[Int]("key12"),
            read[Int]("key13"),
            read[Int]("key14"),
            read[Int]("key15")
          )((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15) ⇒ (v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15)).left.map(_.size) shouldBe Left(15)
        }
      }

      "loading 16 keys" should {
        "be able to load" in {
          loadConfig(
            read[String]("key1"),
            read[String]("key2"),
            read[String]("key3"),
            read[String]("key4"),
            read[String]("key5"),
            read[String]("key6"),
            read[String]("key7"),
            read[String]("key8"),
            read[String]("key9"),
            read[String]("key10"),
            read[String]("key11"),
            read[String]("key12"),
            read[String]("key13"),
            read[String]("key14"),
            read[String]("key15"),
            read[String]("key16")
          )((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16) ⇒ (v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16)) shouldBe Right(("value1", "value2", "value3", "value4", "value5", "value6", "value7", "value8", "value9", "value10", "value11", "value12", "value13", "value14", "value15", "value16"))
        }

        "fail to load if one is missing" in {
          loadConfig(
            read[String]("key1"),
            read[String]("akey2"),
            read[String]("key3"),
            read[String]("key4"),
            read[String]("key5"),
            read[String]("key6"),
            read[String]("key7"),
            read[String]("key8"),
            read[String]("key9"),
            read[String]("key10"),
            read[String]("key11"),
            read[String]("key12"),
            read[String]("key13"),
            read[String]("key14"),
            read[String]("key15"),
            read[String]("key16")
          )((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16) ⇒ (v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16)) shouldBe a[Left[_, _]]
        }

        "fail to load if one type is wrong" in {
          loadConfig(
            read[String]("key1"),
            read[Int]("key2"),
            read[String]("key3"),
            read[String]("key4"),
            read[String]("key5"),
            read[String]("key6"),
            read[String]("key7"),
            read[String]("key8"),
            read[String]("key9"),
            read[String]("key10"),
            read[String]("key11"),
            read[String]("key12"),
            read[String]("key13"),
            read[String]("key14"),
            read[String]("key15"),
            read[String]("key16")
          )((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16) ⇒ (v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16)) shouldBe a[Left[_, _]]
        }

        "accumulate the errors" in {
          loadConfig(
            read[Int]("key1"),
            read[Int]("key2"),
            read[Int]("key3"),
            read[Int]("key4"),
            read[Int]("key5"),
            read[Int]("key6"),
            read[Int]("key7"),
            read[Int]("key8"),
            read[Int]("key9"),
            read[Int]("key10"),
            read[Int]("key11"),
            read[Int]("key12"),
            read[Int]("key13"),
            read[Int]("key14"),
            read[Int]("key15"),
            read[Int]("key16")
          )((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16) ⇒ (v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16)).left.map(_.size) shouldBe Left(16)
        }
      }

      "loading 17 keys" should {
        "be able to load" in {
          loadConfig(
            read[String]("key1"),
            read[String]("key2"),
            read[String]("key3"),
            read[String]("key4"),
            read[String]("key5"),
            read[String]("key6"),
            read[String]("key7"),
            read[String]("key8"),
            read[String]("key9"),
            read[String]("key10"),
            read[String]("key11"),
            read[String]("key12"),
            read[String]("key13"),
            read[String]("key14"),
            read[String]("key15"),
            read[String]("key16"),
            read[String]("key17")
          )((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17) ⇒ (v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17)) shouldBe Right(("value1", "value2", "value3", "value4", "value5", "value6", "value7", "value8", "value9", "value10", "value11", "value12", "value13", "value14", "value15", "value16", "value17"))
        }

        "fail to load if one is missing" in {
          loadConfig(
            read[String]("key1"),
            read[String]("akey2"),
            read[String]("key3"),
            read[String]("key4"),
            read[String]("key5"),
            read[String]("key6"),
            read[String]("key7"),
            read[String]("key8"),
            read[String]("key9"),
            read[String]("key10"),
            read[String]("key11"),
            read[String]("key12"),
            read[String]("key13"),
            read[String]("key14"),
            read[String]("key15"),
            read[String]("key16"),
            read[String]("key17")
          )((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17) ⇒ (v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17)) shouldBe a[Left[_, _]]
        }

        "fail to load if one type is wrong" in {
          loadConfig(
            read[String]("key1"),
            read[Int]("key2"),
            read[String]("key3"),
            read[String]("key4"),
            read[String]("key5"),
            read[String]("key6"),
            read[String]("key7"),
            read[String]("key8"),
            read[String]("key9"),
            read[String]("key10"),
            read[String]("key11"),
            read[String]("key12"),
            read[String]("key13"),
            read[String]("key14"),
            read[String]("key15"),
            read[String]("key16"),
            read[String]("key17")
          )((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17) ⇒ (v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17)) shouldBe a[Left[_, _]]
        }

        "accumulate the errors" in {
          loadConfig(
            read[Int]("key1"),
            read[Int]("key2"),
            read[Int]("key3"),
            read[Int]("key4"),
            read[Int]("key5"),
            read[Int]("key6"),
            read[Int]("key7"),
            read[Int]("key8"),
            read[Int]("key9"),
            read[Int]("key10"),
            read[Int]("key11"),
            read[Int]("key12"),
            read[Int]("key13"),
            read[Int]("key14"),
            read[Int]("key15"),
            read[Int]("key16"),
            read[Int]("key17")
          )((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17) ⇒ (v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17)).left.map(_.size) shouldBe Left(17)
        }
      }

      "loading 18 keys" should {
        "be able to load" in {
          loadConfig(
            read[String]("key1"),
            read[String]("key2"),
            read[String]("key3"),
            read[String]("key4"),
            read[String]("key5"),
            read[String]("key6"),
            read[String]("key7"),
            read[String]("key8"),
            read[String]("key9"),
            read[String]("key10"),
            read[String]("key11"),
            read[String]("key12"),
            read[String]("key13"),
            read[String]("key14"),
            read[String]("key15"),
            read[String]("key16"),
            read[String]("key17"),
            read[String]("key18")
          )((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18) ⇒ (v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18)) shouldBe Right(("value1", "value2", "value3", "value4", "value5", "value6", "value7", "value8", "value9", "value10", "value11", "value12", "value13", "value14", "value15", "value16", "value17", "value18"))
        }

        "fail to load if one is missing" in {
          loadConfig(
            read[String]("key1"),
            read[String]("akey2"),
            read[String]("key3"),
            read[String]("key4"),
            read[String]("key5"),
            read[String]("key6"),
            read[String]("key7"),
            read[String]("key8"),
            read[String]("key9"),
            read[String]("key10"),
            read[String]("key11"),
            read[String]("key12"),
            read[String]("key13"),
            read[String]("key14"),
            read[String]("key15"),
            read[String]("key16"),
            read[String]("key17"),
            read[String]("key18")
          )((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18) ⇒ (v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18)) shouldBe a[Left[_, _]]
        }

        "fail to load if one type is wrong" in {
          loadConfig(
            read[String]("key1"),
            read[Int]("key2"),
            read[String]("key3"),
            read[String]("key4"),
            read[String]("key5"),
            read[String]("key6"),
            read[String]("key7"),
            read[String]("key8"),
            read[String]("key9"),
            read[String]("key10"),
            read[String]("key11"),
            read[String]("key12"),
            read[String]("key13"),
            read[String]("key14"),
            read[String]("key15"),
            read[String]("key16"),
            read[String]("key17"),
            read[String]("key18")
          )((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18) ⇒ (v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18)) shouldBe a[Left[_, _]]
        }

        "accumulate the errors" in {
          loadConfig(
            read[Int]("key1"),
            read[Int]("key2"),
            read[Int]("key3"),
            read[Int]("key4"),
            read[Int]("key5"),
            read[Int]("key6"),
            read[Int]("key7"),
            read[Int]("key8"),
            read[Int]("key9"),
            read[Int]("key10"),
            read[Int]("key11"),
            read[Int]("key12"),
            read[Int]("key13"),
            read[Int]("key14"),
            read[Int]("key15"),
            read[Int]("key16"),
            read[Int]("key17"),
            read[Int]("key18")
          )((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18) ⇒ (v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18)).left.map(_.size) shouldBe Left(18)
        }
      }

      "loading 19 keys" should {
        "be able to load" in {
          loadConfig(
            read[String]("key1"),
            read[String]("key2"),
            read[String]("key3"),
            read[String]("key4"),
            read[String]("key5"),
            read[String]("key6"),
            read[String]("key7"),
            read[String]("key8"),
            read[String]("key9"),
            read[String]("key10"),
            read[String]("key11"),
            read[String]("key12"),
            read[String]("key13"),
            read[String]("key14"),
            read[String]("key15"),
            read[String]("key16"),
            read[String]("key17"),
            read[String]("key18"),
            read[String]("key19")
          )((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19) ⇒ (v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19)) shouldBe Right(("value1", "value2", "value3", "value4", "value5", "value6", "value7", "value8", "value9", "value10", "value11", "value12", "value13", "value14", "value15", "value16", "value17", "value18", "value19"))
        }

        "fail to load if one is missing" in {
          loadConfig(
            read[String]("key1"),
            read[String]("akey2"),
            read[String]("key3"),
            read[String]("key4"),
            read[String]("key5"),
            read[String]("key6"),
            read[String]("key7"),
            read[String]("key8"),
            read[String]("key9"),
            read[String]("key10"),
            read[String]("key11"),
            read[String]("key12"),
            read[String]("key13"),
            read[String]("key14"),
            read[String]("key15"),
            read[String]("key16"),
            read[String]("key17"),
            read[String]("key18"),
            read[String]("key19")
          )((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19) ⇒ (v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19)) shouldBe a[Left[_, _]]
        }

        "fail to load if one type is wrong" in {
          loadConfig(
            read[String]("key1"),
            read[Int]("key2"),
            read[String]("key3"),
            read[String]("key4"),
            read[String]("key5"),
            read[String]("key6"),
            read[String]("key7"),
            read[String]("key8"),
            read[String]("key9"),
            read[String]("key10"),
            read[String]("key11"),
            read[String]("key12"),
            read[String]("key13"),
            read[String]("key14"),
            read[String]("key15"),
            read[String]("key16"),
            read[String]("key17"),
            read[String]("key18"),
            read[String]("key19")
          )((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19) ⇒ (v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19)) shouldBe a[Left[_, _]]
        }

        "accumulate the errors" in {
          loadConfig(
            read[Int]("key1"),
            read[Int]("key2"),
            read[Int]("key3"),
            read[Int]("key4"),
            read[Int]("key5"),
            read[Int]("key6"),
            read[Int]("key7"),
            read[Int]("key8"),
            read[Int]("key9"),
            read[Int]("key10"),
            read[Int]("key11"),
            read[Int]("key12"),
            read[Int]("key13"),
            read[Int]("key14"),
            read[Int]("key15"),
            read[Int]("key16"),
            read[Int]("key17"),
            read[Int]("key18"),
            read[Int]("key19")
          )((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19) ⇒ (v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19)).left.map(_.size) shouldBe Left(19)
        }
      }

      "loading 20 keys" should {
        "be able to load" in {
          loadConfig(
            read[String]("key1"),
            read[String]("key2"),
            read[String]("key3"),
            read[String]("key4"),
            read[String]("key5"),
            read[String]("key6"),
            read[String]("key7"),
            read[String]("key8"),
            read[String]("key9"),
            read[String]("key10"),
            read[String]("key11"),
            read[String]("key12"),
            read[String]("key13"),
            read[String]("key14"),
            read[String]("key15"),
            read[String]("key16"),
            read[String]("key17"),
            read[String]("key18"),
            read[String]("key19"),
            read[String]("key20")
          )((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20) ⇒ (v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20)) shouldBe Right(("value1", "value2", "value3", "value4", "value5", "value6", "value7", "value8", "value9", "value10", "value11", "value12", "value13", "value14", "value15", "value16", "value17", "value18", "value19", "value20"))
        }

        "fail to load if one is missing" in {
          loadConfig(
            read[String]("key1"),
            read[String]("akey2"),
            read[String]("key3"),
            read[String]("key4"),
            read[String]("key5"),
            read[String]("key6"),
            read[String]("key7"),
            read[String]("key8"),
            read[String]("key9"),
            read[String]("key10"),
            read[String]("key11"),
            read[String]("key12"),
            read[String]("key13"),
            read[String]("key14"),
            read[String]("key15"),
            read[String]("key16"),
            read[String]("key17"),
            read[String]("key18"),
            read[String]("key19"),
            read[String]("key20")
          )((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20) ⇒ (v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20)) shouldBe a[Left[_, _]]
        }

        "fail to load if one type is wrong" in {
          loadConfig(
            read[String]("key1"),
            read[Int]("key2"),
            read[String]("key3"),
            read[String]("key4"),
            read[String]("key5"),
            read[String]("key6"),
            read[String]("key7"),
            read[String]("key8"),
            read[String]("key9"),
            read[String]("key10"),
            read[String]("key11"),
            read[String]("key12"),
            read[String]("key13"),
            read[String]("key14"),
            read[String]("key15"),
            read[String]("key16"),
            read[String]("key17"),
            read[String]("key18"),
            read[String]("key19"),
            read[String]("key20")
          )((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20) ⇒ (v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20)) shouldBe a[Left[_, _]]
        }

        "accumulate the errors" in {
          loadConfig(
            read[Int]("key1"),
            read[Int]("key2"),
            read[Int]("key3"),
            read[Int]("key4"),
            read[Int]("key5"),
            read[Int]("key6"),
            read[Int]("key7"),
            read[Int]("key8"),
            read[Int]("key9"),
            read[Int]("key10"),
            read[Int]("key11"),
            read[Int]("key12"),
            read[Int]("key13"),
            read[Int]("key14"),
            read[Int]("key15"),
            read[Int]("key16"),
            read[Int]("key17"),
            read[Int]("key18"),
            read[Int]("key19"),
            read[Int]("key20")
          )((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20) ⇒ (v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20)).left.map(_.size) shouldBe Left(20)
        }
      }

      "loading 21 keys" should {
        "be able to load" in {
          loadConfig(
            read[String]("key1"),
            read[String]("key2"),
            read[String]("key3"),
            read[String]("key4"),
            read[String]("key5"),
            read[String]("key6"),
            read[String]("key7"),
            read[String]("key8"),
            read[String]("key9"),
            read[String]("key10"),
            read[String]("key11"),
            read[String]("key12"),
            read[String]("key13"),
            read[String]("key14"),
            read[String]("key15"),
            read[String]("key16"),
            read[String]("key17"),
            read[String]("key18"),
            read[String]("key19"),
            read[String]("key20"),
            read[String]("key21")
          )((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21) ⇒ (v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21)) shouldBe Right(("value1", "value2", "value3", "value4", "value5", "value6", "value7", "value8", "value9", "value10", "value11", "value12", "value13", "value14", "value15", "value16", "value17", "value18", "value19", "value20", "value21"))
        }

        "fail to load if one is missing" in {
          loadConfig(
            read[String]("key1"),
            read[String]("akey2"),
            read[String]("key3"),
            read[String]("key4"),
            read[String]("key5"),
            read[String]("key6"),
            read[String]("key7"),
            read[String]("key8"),
            read[String]("key9"),
            read[String]("key10"),
            read[String]("key11"),
            read[String]("key12"),
            read[String]("key13"),
            read[String]("key14"),
            read[String]("key15"),
            read[String]("key16"),
            read[String]("key17"),
            read[String]("key18"),
            read[String]("key19"),
            read[String]("key20"),
            read[String]("key21")
          )((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21) ⇒ (v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21)) shouldBe a[Left[_, _]]
        }

        "fail to load if one type is wrong" in {
          loadConfig(
            read[String]("key1"),
            read[Int]("key2"),
            read[String]("key3"),
            read[String]("key4"),
            read[String]("key5"),
            read[String]("key6"),
            read[String]("key7"),
            read[String]("key8"),
            read[String]("key9"),
            read[String]("key10"),
            read[String]("key11"),
            read[String]("key12"),
            read[String]("key13"),
            read[String]("key14"),
            read[String]("key15"),
            read[String]("key16"),
            read[String]("key17"),
            read[String]("key18"),
            read[String]("key19"),
            read[String]("key20"),
            read[String]("key21")
          )((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21) ⇒ (v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21)) shouldBe a[Left[_, _]]
        }

        "accumulate the errors" in {
          loadConfig(
            read[Int]("key1"),
            read[Int]("key2"),
            read[Int]("key3"),
            read[Int]("key4"),
            read[Int]("key5"),
            read[Int]("key6"),
            read[Int]("key7"),
            read[Int]("key8"),
            read[Int]("key9"),
            read[Int]("key10"),
            read[Int]("key11"),
            read[Int]("key12"),
            read[Int]("key13"),
            read[Int]("key14"),
            read[Int]("key15"),
            read[Int]("key16"),
            read[Int]("key17"),
            read[Int]("key18"),
            read[Int]("key19"),
            read[Int]("key20"),
            read[Int]("key21")
          )((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21) ⇒ (v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21)).left.map(_.size) shouldBe Left(21)
        }
      }
    }
  }
}
