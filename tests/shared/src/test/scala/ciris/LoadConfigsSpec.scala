// format: off

/**
  * Generated using sbt source generators.
  * You should not modify this file directly.
  */

package ciris

import ciris.api._

final class LoadConfigsSpec extends PropertySpec {
  "LoadConfigs" when {
    "loading configurations" when {
      implicit val source: ConfigSource[Id, String, String] = sourceWith("key1" -> "value1", "key2" -> "value2", "key3" -> "value3", "key4" -> "value4", "key5" -> "value5", "key6" -> "value6", "key7" -> "value7", "key8" -> "value8", "key9" -> "value9", "key10" -> "value10", "key11" -> "value11", "key12" -> "value12", "key13" -> "value13", "key14" -> "value14", "key15" -> "value15", "key16" -> "value16", "key17" -> "value17", "key18" -> "value18", "key19" -> "value19", "key20" -> "value20", "key21" -> "value21", "key22" -> "value22")

      def read[Value](key: String)(implicit decoder: ConfigDecoder[String, Value]): ConfigEntry[Id, String, String, Value] =
        source.read(key).decodeValue[Value]

      "loading 0 keys" should {
        "always be able to load" in {
          forAll { int: Int =>
            loadConfig(int) shouldBe Right(int)
          }
        }
      }

      "loading 1 keys" should {
        "be able to load" in {
          loadConfig(read[String]("key1"))((a1) => (a1)) shouldBe Right(("value1"))
        }
      
        "be able to load values" in {
          withValues(read[String]("key1"))((b1) => loadConfig(read[String]("key1"))((a1) => (a1))) shouldBe Right(("value1"))
          withValue(read[String]("key1"))((b1) => loadConfig(read[String]("key1"))((a1) => (a1))) shouldBe Right(("value1"))
        }
      
        "fail to load if the first one is missing" in {
          loadConfig(read[String]("akey1"))((a1) => (a1)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the first one is missing" in {
          withValues(read[String]("akey1"))((b1) => loadConfig(read[String]("key1"))((a1) => (a1))) shouldBe a[Left[_, _]]
          withValue(read[String]("akey1"))((b1) => loadConfig(read[String]("key1"))((a1) => (a1))) shouldBe a[Left[_, _]]
        }
      
        "fail to load if the last one is missing in" in {
          loadConfig(read[String]("akey1"))((a1) => (a1)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the last one is missing" in {
          withValues(read[String]("akey1"))((b1) => loadConfig(read[String]("key1"))((a1) => (a1))) shouldBe a[Left[_, _]]
          withValue(read[String]("akey1"))((b1) => loadConfig(read[String]("key1"))((a1) => (a1))) shouldBe a[Left[_, _]]
        }
      
        "fail to load if the first type is wrong" in {
          loadConfig(read[Int]("key1"))((a1) => (a1)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the first type is wrong" in {
          withValues(read[Int]("key1"))((b1) => loadConfig(read[String]("key1"))((a1) => (a1))) shouldBe a[Left[_, _]]
          withValue(read[Int]("key1"))((b1) => loadConfig(read[String]("key1"))((a1) => (a1))) shouldBe a[Left[_, _]]
        }
      
        "fail to load if the last type is wrong" in {
          loadConfig(read[Int]("key1"))((a1) => (a1)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the last type is wrong" in {
          withValues(read[Int]("key1"))((b1) => loadConfig(read[String]("key1"))((a1) => (a1))) shouldBe a[Left[_, _]]
          withValue(read[Int]("key1"))((b1) => loadConfig(read[String]("key1"))((a1) => (a1))) shouldBe a[Left[_, _]]
        }
      
        "fail to load and accumulate the errors" in {
          loadConfig(read[Int]("key1"))((a1) => (a1)).left.map(_.size) shouldBe Left(1)
        }
      
        "fail to load values and accumulate the errors" in {
          withValues(read[Int]("key1"))((b1) => loadConfig(read[String]("key1"))((a1) => (a1))) shouldBe a[Left[_, _]]
          withValue(read[Int]("key1"))((b1) => loadConfig(read[String]("key1"))((a1) => (a1))) shouldBe a[Left[_, _]]
        }
      }

      "loading 2 keys" should {
        "be able to load" in {
          loadConfig(read[String]("key1"), read[String]("key2"))((a1, a2) => (a1, a2)) shouldBe Right(("value1", "value2"))
        }
      
        "be able to load values" in {
          withValues(read[String]("key1"), read[String]("key2"))((b1, b2) => loadConfig(read[String]("key1"), read[String]("key2"))((a1, a2) => (a1, a2))) shouldBe Right(("value1", "value2"))
        }
      
        "fail to load if the first one is missing" in {
          loadConfig(read[String]("akey1"), read[String]("key2"))((a1, a2) => (a1, a2)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the first one is missing" in {
          withValues(read[String]("akey1"), read[String]("key2"))((b1, b2) => loadConfig(read[String]("key1"), read[String]("key2"))((a1, a2) => (a1, a2))) shouldBe a[Left[_, _]]
        }
      
        "fail to load if the last one is missing in" in {
          loadConfig(read[String]("key1"), read[String]("akey2"))((a1, a2) => (a1, a2)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the last one is missing" in {
          withValues(read[String]("key1"), read[String]("akey2"))((b1, b2) => loadConfig(read[String]("key1"), read[String]("key2"))((a1, a2) => (a1, a2))) shouldBe a[Left[_, _]]
        }
      
        "fail to load if the first type is wrong" in {
          loadConfig(read[Int]("key1"), read[String]("key2"))((a1, a2) => (a1, a2)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the first type is wrong" in {
          withValues(read[Int]("key1"), read[String]("key2"))((b1, b2) => loadConfig(read[String]("key1"), read[String]("key2"))((a1, a2) => (a1, a2))) shouldBe a[Left[_, _]]
        }
      
        "fail to load if the last type is wrong" in {
          loadConfig(read[String]("key1"), read[Int]("key2"))((a1, a2) => (a1, a2)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the last type is wrong" in {
          withValues(read[String]("key1"), read[Int]("key2"))((b1, b2) => loadConfig(read[String]("key1"), read[String]("key2"))((a1, a2) => (a1, a2))) shouldBe a[Left[_, _]]
        }
      
        "fail to load and accumulate the errors" in {
          loadConfig(read[Int]("key1"), read[Int]("key2"))((a1, a2) => (a1, a2)).left.map(_.size) shouldBe Left(2)
        }
      
        "fail to load values and accumulate the errors" in {
          withValues(read[Int]("key1"), read[Int]("key2"))((b1, b2) => loadConfig(read[String]("key1"), read[String]("key2"))((a1, a2) => (a1, a2))) shouldBe a[Left[_, _]]
        }
      }

      "loading 3 keys" should {
        "be able to load" in {
          loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"))((a1, a2, a3) => (a1, a2, a3)) shouldBe Right(("value1", "value2", "value3"))
        }
      
        "be able to load values" in {
          withValues(read[String]("key1"), read[String]("key2"), read[String]("key3"))((b1, b2, b3) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"))((a1, a2, a3) => (a1, a2, a3))) shouldBe Right(("value1", "value2", "value3"))
        }
      
        "fail to load if the first one is missing" in {
          loadConfig(read[String]("akey1"), read[String]("key2"), read[String]("key3"))((a1, a2, a3) => (a1, a2, a3)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the first one is missing" in {
          withValues(read[String]("akey1"), read[String]("key2"), read[String]("key3"))((b1, b2, b3) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"))((a1, a2, a3) => (a1, a2, a3))) shouldBe a[Left[_, _]]
        }
      
        "fail to load if the last one is missing in" in {
          loadConfig(read[String]("key1"), read[String]("key2"), read[String]("akey3"))((a1, a2, a3) => (a1, a2, a3)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the last one is missing" in {
          withValues(read[String]("key1"), read[String]("key2"), read[String]("akey3"))((b1, b2, b3) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"))((a1, a2, a3) => (a1, a2, a3))) shouldBe a[Left[_, _]]
        }
      
        "fail to load if the first type is wrong" in {
          loadConfig(read[Int]("key1"), read[String]("key2"), read[String]("key3"))((a1, a2, a3) => (a1, a2, a3)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the first type is wrong" in {
          withValues(read[Int]("key1"), read[String]("key2"), read[String]("key3"))((b1, b2, b3) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"))((a1, a2, a3) => (a1, a2, a3))) shouldBe a[Left[_, _]]
        }
      
        "fail to load if the last type is wrong" in {
          loadConfig(read[String]("key1"), read[String]("key2"), read[Int]("key3"))((a1, a2, a3) => (a1, a2, a3)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the last type is wrong" in {
          withValues(read[String]("key1"), read[String]("key2"), read[Int]("key3"))((b1, b2, b3) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"))((a1, a2, a3) => (a1, a2, a3))) shouldBe a[Left[_, _]]
        }
      
        "fail to load and accumulate the errors" in {
          loadConfig(read[Int]("key1"), read[Int]("key2"), read[Int]("key3"))((a1, a2, a3) => (a1, a2, a3)).left.map(_.size) shouldBe Left(3)
        }
      
        "fail to load values and accumulate the errors" in {
          withValues(read[Int]("key1"), read[Int]("key2"), read[Int]("key3"))((b1, b2, b3) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"))((a1, a2, a3) => (a1, a2, a3))) shouldBe a[Left[_, _]]
        }
      }

      "loading 4 keys" should {
        "be able to load" in {
          loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"))((a1, a2, a3, a4) => (a1, a2, a3, a4)) shouldBe Right(("value1", "value2", "value3", "value4"))
        }
      
        "be able to load values" in {
          withValues(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"))((b1, b2, b3, b4) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"))((a1, a2, a3, a4) => (a1, a2, a3, a4))) shouldBe Right(("value1", "value2", "value3", "value4"))
        }
      
        "fail to load if the first one is missing" in {
          loadConfig(read[String]("akey1"), read[String]("key2"), read[String]("key3"), read[String]("key4"))((a1, a2, a3, a4) => (a1, a2, a3, a4)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the first one is missing" in {
          withValues(read[String]("akey1"), read[String]("key2"), read[String]("key3"), read[String]("key4"))((b1, b2, b3, b4) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"))((a1, a2, a3, a4) => (a1, a2, a3, a4))) shouldBe a[Left[_, _]]
        }
      
        "fail to load if the last one is missing in" in {
          loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("akey4"))((a1, a2, a3, a4) => (a1, a2, a3, a4)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the last one is missing" in {
          withValues(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("akey4"))((b1, b2, b3, b4) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"))((a1, a2, a3, a4) => (a1, a2, a3, a4))) shouldBe a[Left[_, _]]
        }
      
        "fail to load if the first type is wrong" in {
          loadConfig(read[Int]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"))((a1, a2, a3, a4) => (a1, a2, a3, a4)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the first type is wrong" in {
          withValues(read[Int]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"))((b1, b2, b3, b4) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"))((a1, a2, a3, a4) => (a1, a2, a3, a4))) shouldBe a[Left[_, _]]
        }
      
        "fail to load if the last type is wrong" in {
          loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[Int]("key4"))((a1, a2, a3, a4) => (a1, a2, a3, a4)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the last type is wrong" in {
          withValues(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[Int]("key4"))((b1, b2, b3, b4) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"))((a1, a2, a3, a4) => (a1, a2, a3, a4))) shouldBe a[Left[_, _]]
        }
      
        "fail to load and accumulate the errors" in {
          loadConfig(read[Int]("key1"), read[Int]("key2"), read[Int]("key3"), read[Int]("key4"))((a1, a2, a3, a4) => (a1, a2, a3, a4)).left.map(_.size) shouldBe Left(4)
        }
      
        "fail to load values and accumulate the errors" in {
          withValues(read[Int]("key1"), read[Int]("key2"), read[Int]("key3"), read[Int]("key4"))((b1, b2, b3, b4) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"))((a1, a2, a3, a4) => (a1, a2, a3, a4))) shouldBe a[Left[_, _]]
        }
      }

      "loading 5 keys" should {
        "be able to load" in {
          loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"))((a1, a2, a3, a4, a5) => (a1, a2, a3, a4, a5)) shouldBe Right(("value1", "value2", "value3", "value4", "value5"))
        }
      
        "be able to load values" in {
          withValues(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"))((b1, b2, b3, b4, b5) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"))((a1, a2, a3, a4, a5) => (a1, a2, a3, a4, a5))) shouldBe Right(("value1", "value2", "value3", "value4", "value5"))
        }
      
        "fail to load if the first one is missing" in {
          loadConfig(read[String]("akey1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"))((a1, a2, a3, a4, a5) => (a1, a2, a3, a4, a5)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the first one is missing" in {
          withValues(read[String]("akey1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"))((b1, b2, b3, b4, b5) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"))((a1, a2, a3, a4, a5) => (a1, a2, a3, a4, a5))) shouldBe a[Left[_, _]]
        }
      
        "fail to load if the last one is missing in" in {
          loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("akey5"))((a1, a2, a3, a4, a5) => (a1, a2, a3, a4, a5)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the last one is missing" in {
          withValues(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("akey5"))((b1, b2, b3, b4, b5) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"))((a1, a2, a3, a4, a5) => (a1, a2, a3, a4, a5))) shouldBe a[Left[_, _]]
        }
      
        "fail to load if the first type is wrong" in {
          loadConfig(read[Int]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"))((a1, a2, a3, a4, a5) => (a1, a2, a3, a4, a5)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the first type is wrong" in {
          withValues(read[Int]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"))((b1, b2, b3, b4, b5) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"))((a1, a2, a3, a4, a5) => (a1, a2, a3, a4, a5))) shouldBe a[Left[_, _]]
        }
      
        "fail to load if the last type is wrong" in {
          loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[Int]("key5"))((a1, a2, a3, a4, a5) => (a1, a2, a3, a4, a5)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the last type is wrong" in {
          withValues(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[Int]("key5"))((b1, b2, b3, b4, b5) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"))((a1, a2, a3, a4, a5) => (a1, a2, a3, a4, a5))) shouldBe a[Left[_, _]]
        }
      
        "fail to load and accumulate the errors" in {
          loadConfig(read[Int]("key1"), read[Int]("key2"), read[Int]("key3"), read[Int]("key4"), read[Int]("key5"))((a1, a2, a3, a4, a5) => (a1, a2, a3, a4, a5)).left.map(_.size) shouldBe Left(5)
        }
      
        "fail to load values and accumulate the errors" in {
          withValues(read[Int]("key1"), read[Int]("key2"), read[Int]("key3"), read[Int]("key4"), read[Int]("key5"))((b1, b2, b3, b4, b5) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"))((a1, a2, a3, a4, a5) => (a1, a2, a3, a4, a5))) shouldBe a[Left[_, _]]
        }
      }

      "loading 6 keys" should {
        "be able to load" in {
          loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"))((a1, a2, a3, a4, a5, a6) => (a1, a2, a3, a4, a5, a6)) shouldBe Right(("value1", "value2", "value3", "value4", "value5", "value6"))
        }
      
        "be able to load values" in {
          withValues(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"))((b1, b2, b3, b4, b5, b6) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"))((a1, a2, a3, a4, a5, a6) => (a1, a2, a3, a4, a5, a6))) shouldBe Right(("value1", "value2", "value3", "value4", "value5", "value6"))
        }
      
        "fail to load if the first one is missing" in {
          loadConfig(read[String]("akey1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"))((a1, a2, a3, a4, a5, a6) => (a1, a2, a3, a4, a5, a6)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the first one is missing" in {
          withValues(read[String]("akey1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"))((b1, b2, b3, b4, b5, b6) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"))((a1, a2, a3, a4, a5, a6) => (a1, a2, a3, a4, a5, a6))) shouldBe a[Left[_, _]]
        }
      
        "fail to load if the last one is missing in" in {
          loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("akey6"))((a1, a2, a3, a4, a5, a6) => (a1, a2, a3, a4, a5, a6)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the last one is missing" in {
          withValues(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("akey6"))((b1, b2, b3, b4, b5, b6) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"))((a1, a2, a3, a4, a5, a6) => (a1, a2, a3, a4, a5, a6))) shouldBe a[Left[_, _]]
        }
      
        "fail to load if the first type is wrong" in {
          loadConfig(read[Int]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"))((a1, a2, a3, a4, a5, a6) => (a1, a2, a3, a4, a5, a6)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the first type is wrong" in {
          withValues(read[Int]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"))((b1, b2, b3, b4, b5, b6) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"))((a1, a2, a3, a4, a5, a6) => (a1, a2, a3, a4, a5, a6))) shouldBe a[Left[_, _]]
        }
      
        "fail to load if the last type is wrong" in {
          loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[Int]("key6"))((a1, a2, a3, a4, a5, a6) => (a1, a2, a3, a4, a5, a6)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the last type is wrong" in {
          withValues(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[Int]("key6"))((b1, b2, b3, b4, b5, b6) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"))((a1, a2, a3, a4, a5, a6) => (a1, a2, a3, a4, a5, a6))) shouldBe a[Left[_, _]]
        }
      
        "fail to load and accumulate the errors" in {
          loadConfig(read[Int]("key1"), read[Int]("key2"), read[Int]("key3"), read[Int]("key4"), read[Int]("key5"), read[Int]("key6"))((a1, a2, a3, a4, a5, a6) => (a1, a2, a3, a4, a5, a6)).left.map(_.size) shouldBe Left(6)
        }
      
        "fail to load values and accumulate the errors" in {
          withValues(read[Int]("key1"), read[Int]("key2"), read[Int]("key3"), read[Int]("key4"), read[Int]("key5"), read[Int]("key6"))((b1, b2, b3, b4, b5, b6) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"))((a1, a2, a3, a4, a5, a6) => (a1, a2, a3, a4, a5, a6))) shouldBe a[Left[_, _]]
        }
      }

      "loading 7 keys" should {
        "be able to load" in {
          loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"))((a1, a2, a3, a4, a5, a6, a7) => (a1, a2, a3, a4, a5, a6, a7)) shouldBe Right(("value1", "value2", "value3", "value4", "value5", "value6", "value7"))
        }
      
        "be able to load values" in {
          withValues(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"))((b1, b2, b3, b4, b5, b6, b7) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"))((a1, a2, a3, a4, a5, a6, a7) => (a1, a2, a3, a4, a5, a6, a7))) shouldBe Right(("value1", "value2", "value3", "value4", "value5", "value6", "value7"))
        }
      
        "fail to load if the first one is missing" in {
          loadConfig(read[String]("akey1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"))((a1, a2, a3, a4, a5, a6, a7) => (a1, a2, a3, a4, a5, a6, a7)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the first one is missing" in {
          withValues(read[String]("akey1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"))((b1, b2, b3, b4, b5, b6, b7) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"))((a1, a2, a3, a4, a5, a6, a7) => (a1, a2, a3, a4, a5, a6, a7))) shouldBe a[Left[_, _]]
        }
      
        "fail to load if the last one is missing in" in {
          loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("akey7"))((a1, a2, a3, a4, a5, a6, a7) => (a1, a2, a3, a4, a5, a6, a7)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the last one is missing" in {
          withValues(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("akey7"))((b1, b2, b3, b4, b5, b6, b7) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"))((a1, a2, a3, a4, a5, a6, a7) => (a1, a2, a3, a4, a5, a6, a7))) shouldBe a[Left[_, _]]
        }
      
        "fail to load if the first type is wrong" in {
          loadConfig(read[Int]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"))((a1, a2, a3, a4, a5, a6, a7) => (a1, a2, a3, a4, a5, a6, a7)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the first type is wrong" in {
          withValues(read[Int]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"))((b1, b2, b3, b4, b5, b6, b7) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"))((a1, a2, a3, a4, a5, a6, a7) => (a1, a2, a3, a4, a5, a6, a7))) shouldBe a[Left[_, _]]
        }
      
        "fail to load if the last type is wrong" in {
          loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[Int]("key7"))((a1, a2, a3, a4, a5, a6, a7) => (a1, a2, a3, a4, a5, a6, a7)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the last type is wrong" in {
          withValues(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[Int]("key7"))((b1, b2, b3, b4, b5, b6, b7) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"))((a1, a2, a3, a4, a5, a6, a7) => (a1, a2, a3, a4, a5, a6, a7))) shouldBe a[Left[_, _]]
        }
      
        "fail to load and accumulate the errors" in {
          loadConfig(read[Int]("key1"), read[Int]("key2"), read[Int]("key3"), read[Int]("key4"), read[Int]("key5"), read[Int]("key6"), read[Int]("key7"))((a1, a2, a3, a4, a5, a6, a7) => (a1, a2, a3, a4, a5, a6, a7)).left.map(_.size) shouldBe Left(7)
        }
      
        "fail to load values and accumulate the errors" in {
          withValues(read[Int]("key1"), read[Int]("key2"), read[Int]("key3"), read[Int]("key4"), read[Int]("key5"), read[Int]("key6"), read[Int]("key7"))((b1, b2, b3, b4, b5, b6, b7) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"))((a1, a2, a3, a4, a5, a6, a7) => (a1, a2, a3, a4, a5, a6, a7))) shouldBe a[Left[_, _]]
        }
      }

      "loading 8 keys" should {
        "be able to load" in {
          loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"))((a1, a2, a3, a4, a5, a6, a7, a8) => (a1, a2, a3, a4, a5, a6, a7, a8)) shouldBe Right(("value1", "value2", "value3", "value4", "value5", "value6", "value7", "value8"))
        }
      
        "be able to load values" in {
          withValues(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"))((b1, b2, b3, b4, b5, b6, b7, b8) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"))((a1, a2, a3, a4, a5, a6, a7, a8) => (a1, a2, a3, a4, a5, a6, a7, a8))) shouldBe Right(("value1", "value2", "value3", "value4", "value5", "value6", "value7", "value8"))
        }
      
        "fail to load if the first one is missing" in {
          loadConfig(read[String]("akey1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"))((a1, a2, a3, a4, a5, a6, a7, a8) => (a1, a2, a3, a4, a5, a6, a7, a8)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the first one is missing" in {
          withValues(read[String]("akey1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"))((b1, b2, b3, b4, b5, b6, b7, b8) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"))((a1, a2, a3, a4, a5, a6, a7, a8) => (a1, a2, a3, a4, a5, a6, a7, a8))) shouldBe a[Left[_, _]]
        }
      
        "fail to load if the last one is missing in" in {
          loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("akey8"))((a1, a2, a3, a4, a5, a6, a7, a8) => (a1, a2, a3, a4, a5, a6, a7, a8)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the last one is missing" in {
          withValues(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("akey8"))((b1, b2, b3, b4, b5, b6, b7, b8) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"))((a1, a2, a3, a4, a5, a6, a7, a8) => (a1, a2, a3, a4, a5, a6, a7, a8))) shouldBe a[Left[_, _]]
        }
      
        "fail to load if the first type is wrong" in {
          loadConfig(read[Int]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"))((a1, a2, a3, a4, a5, a6, a7, a8) => (a1, a2, a3, a4, a5, a6, a7, a8)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the first type is wrong" in {
          withValues(read[Int]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"))((b1, b2, b3, b4, b5, b6, b7, b8) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"))((a1, a2, a3, a4, a5, a6, a7, a8) => (a1, a2, a3, a4, a5, a6, a7, a8))) shouldBe a[Left[_, _]]
        }
      
        "fail to load if the last type is wrong" in {
          loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[Int]("key8"))((a1, a2, a3, a4, a5, a6, a7, a8) => (a1, a2, a3, a4, a5, a6, a7, a8)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the last type is wrong" in {
          withValues(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[Int]("key8"))((b1, b2, b3, b4, b5, b6, b7, b8) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"))((a1, a2, a3, a4, a5, a6, a7, a8) => (a1, a2, a3, a4, a5, a6, a7, a8))) shouldBe a[Left[_, _]]
        }
      
        "fail to load and accumulate the errors" in {
          loadConfig(read[Int]("key1"), read[Int]("key2"), read[Int]("key3"), read[Int]("key4"), read[Int]("key5"), read[Int]("key6"), read[Int]("key7"), read[Int]("key8"))((a1, a2, a3, a4, a5, a6, a7, a8) => (a1, a2, a3, a4, a5, a6, a7, a8)).left.map(_.size) shouldBe Left(8)
        }
      
        "fail to load values and accumulate the errors" in {
          withValues(read[Int]("key1"), read[Int]("key2"), read[Int]("key3"), read[Int]("key4"), read[Int]("key5"), read[Int]("key6"), read[Int]("key7"), read[Int]("key8"))((b1, b2, b3, b4, b5, b6, b7, b8) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"))((a1, a2, a3, a4, a5, a6, a7, a8) => (a1, a2, a3, a4, a5, a6, a7, a8))) shouldBe a[Left[_, _]]
        }
      }

      "loading 9 keys" should {
        "be able to load" in {
          loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"))((a1, a2, a3, a4, a5, a6, a7, a8, a9) => (a1, a2, a3, a4, a5, a6, a7, a8, a9)) shouldBe Right(("value1", "value2", "value3", "value4", "value5", "value6", "value7", "value8", "value9"))
        }
      
        "be able to load values" in {
          withValues(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"))((b1, b2, b3, b4, b5, b6, b7, b8, b9) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"))((a1, a2, a3, a4, a5, a6, a7, a8, a9) => (a1, a2, a3, a4, a5, a6, a7, a8, a9))) shouldBe Right(("value1", "value2", "value3", "value4", "value5", "value6", "value7", "value8", "value9"))
        }
      
        "fail to load if the first one is missing" in {
          loadConfig(read[String]("akey1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"))((a1, a2, a3, a4, a5, a6, a7, a8, a9) => (a1, a2, a3, a4, a5, a6, a7, a8, a9)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the first one is missing" in {
          withValues(read[String]("akey1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"))((b1, b2, b3, b4, b5, b6, b7, b8, b9) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"))((a1, a2, a3, a4, a5, a6, a7, a8, a9) => (a1, a2, a3, a4, a5, a6, a7, a8, a9))) shouldBe a[Left[_, _]]
        }
      
        "fail to load if the last one is missing in" in {
          loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("akey9"))((a1, a2, a3, a4, a5, a6, a7, a8, a9) => (a1, a2, a3, a4, a5, a6, a7, a8, a9)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the last one is missing" in {
          withValues(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("akey9"))((b1, b2, b3, b4, b5, b6, b7, b8, b9) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"))((a1, a2, a3, a4, a5, a6, a7, a8, a9) => (a1, a2, a3, a4, a5, a6, a7, a8, a9))) shouldBe a[Left[_, _]]
        }
      
        "fail to load if the first type is wrong" in {
          loadConfig(read[Int]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"))((a1, a2, a3, a4, a5, a6, a7, a8, a9) => (a1, a2, a3, a4, a5, a6, a7, a8, a9)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the first type is wrong" in {
          withValues(read[Int]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"))((b1, b2, b3, b4, b5, b6, b7, b8, b9) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"))((a1, a2, a3, a4, a5, a6, a7, a8, a9) => (a1, a2, a3, a4, a5, a6, a7, a8, a9))) shouldBe a[Left[_, _]]
        }
      
        "fail to load if the last type is wrong" in {
          loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[Int]("key9"))((a1, a2, a3, a4, a5, a6, a7, a8, a9) => (a1, a2, a3, a4, a5, a6, a7, a8, a9)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the last type is wrong" in {
          withValues(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[Int]("key9"))((b1, b2, b3, b4, b5, b6, b7, b8, b9) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"))((a1, a2, a3, a4, a5, a6, a7, a8, a9) => (a1, a2, a3, a4, a5, a6, a7, a8, a9))) shouldBe a[Left[_, _]]
        }
      
        "fail to load and accumulate the errors" in {
          loadConfig(read[Int]("key1"), read[Int]("key2"), read[Int]("key3"), read[Int]("key4"), read[Int]("key5"), read[Int]("key6"), read[Int]("key7"), read[Int]("key8"), read[Int]("key9"))((a1, a2, a3, a4, a5, a6, a7, a8, a9) => (a1, a2, a3, a4, a5, a6, a7, a8, a9)).left.map(_.size) shouldBe Left(9)
        }
      
        "fail to load values and accumulate the errors" in {
          withValues(read[Int]("key1"), read[Int]("key2"), read[Int]("key3"), read[Int]("key4"), read[Int]("key5"), read[Int]("key6"), read[Int]("key7"), read[Int]("key8"), read[Int]("key9"))((b1, b2, b3, b4, b5, b6, b7, b8, b9) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"))((a1, a2, a3, a4, a5, a6, a7, a8, a9) => (a1, a2, a3, a4, a5, a6, a7, a8, a9))) shouldBe a[Left[_, _]]
        }
      }

      "loading 10 keys" should {
        "be able to load" in {
          loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10)) shouldBe Right(("value1", "value2", "value3", "value4", "value5", "value6", "value7", "value8", "value9", "value10"))
        }
      
        "be able to load values" in {
          withValues(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10))) shouldBe Right(("value1", "value2", "value3", "value4", "value5", "value6", "value7", "value8", "value9", "value10"))
        }
      
        "fail to load if the first one is missing" in {
          loadConfig(read[String]("akey1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the first one is missing" in {
          withValues(read[String]("akey1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10))) shouldBe a[Left[_, _]]
        }
      
        "fail to load if the last one is missing in" in {
          loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("akey10"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the last one is missing" in {
          withValues(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("akey10"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10))) shouldBe a[Left[_, _]]
        }
      
        "fail to load if the first type is wrong" in {
          loadConfig(read[Int]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the first type is wrong" in {
          withValues(read[Int]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10))) shouldBe a[Left[_, _]]
        }
      
        "fail to load if the last type is wrong" in {
          loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[Int]("key10"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the last type is wrong" in {
          withValues(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[Int]("key10"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10))) shouldBe a[Left[_, _]]
        }
      
        "fail to load and accumulate the errors" in {
          loadConfig(read[Int]("key1"), read[Int]("key2"), read[Int]("key3"), read[Int]("key4"), read[Int]("key5"), read[Int]("key6"), read[Int]("key7"), read[Int]("key8"), read[Int]("key9"), read[Int]("key10"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10)).left.map(_.size) shouldBe Left(10)
        }
      
        "fail to load values and accumulate the errors" in {
          withValues(read[Int]("key1"), read[Int]("key2"), read[Int]("key3"), read[Int]("key4"), read[Int]("key5"), read[Int]("key6"), read[Int]("key7"), read[Int]("key8"), read[Int]("key9"), read[Int]("key10"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10))) shouldBe a[Left[_, _]]
        }
      }

      "loading 11 keys" should {
        "be able to load" in {
          loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11)) shouldBe Right(("value1", "value2", "value3", "value4", "value5", "value6", "value7", "value8", "value9", "value10", "value11"))
        }
      
        "be able to load values" in {
          withValues(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11))) shouldBe Right(("value1", "value2", "value3", "value4", "value5", "value6", "value7", "value8", "value9", "value10", "value11"))
        }
      
        "fail to load if the first one is missing" in {
          loadConfig(read[String]("akey1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the first one is missing" in {
          withValues(read[String]("akey1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11))) shouldBe a[Left[_, _]]
        }
      
        "fail to load if the last one is missing in" in {
          loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("akey11"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the last one is missing" in {
          withValues(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("akey11"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11))) shouldBe a[Left[_, _]]
        }
      
        "fail to load if the first type is wrong" in {
          loadConfig(read[Int]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the first type is wrong" in {
          withValues(read[Int]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11))) shouldBe a[Left[_, _]]
        }
      
        "fail to load if the last type is wrong" in {
          loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[Int]("key11"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the last type is wrong" in {
          withValues(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[Int]("key11"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11))) shouldBe a[Left[_, _]]
        }
      
        "fail to load and accumulate the errors" in {
          loadConfig(read[Int]("key1"), read[Int]("key2"), read[Int]("key3"), read[Int]("key4"), read[Int]("key5"), read[Int]("key6"), read[Int]("key7"), read[Int]("key8"), read[Int]("key9"), read[Int]("key10"), read[Int]("key11"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11)).left.map(_.size) shouldBe Left(11)
        }
      
        "fail to load values and accumulate the errors" in {
          withValues(read[Int]("key1"), read[Int]("key2"), read[Int]("key3"), read[Int]("key4"), read[Int]("key5"), read[Int]("key6"), read[Int]("key7"), read[Int]("key8"), read[Int]("key9"), read[Int]("key10"), read[Int]("key11"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11))) shouldBe a[Left[_, _]]
        }
      }

      "loading 12 keys" should {
        "be able to load" in {
          loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12)) shouldBe Right(("value1", "value2", "value3", "value4", "value5", "value6", "value7", "value8", "value9", "value10", "value11", "value12"))
        }
      
        "be able to load values" in {
          withValues(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12))) shouldBe Right(("value1", "value2", "value3", "value4", "value5", "value6", "value7", "value8", "value9", "value10", "value11", "value12"))
        }
      
        "fail to load if the first one is missing" in {
          loadConfig(read[String]("akey1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the first one is missing" in {
          withValues(read[String]("akey1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12))) shouldBe a[Left[_, _]]
        }
      
        "fail to load if the last one is missing in" in {
          loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("akey12"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the last one is missing" in {
          withValues(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("akey12"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12))) shouldBe a[Left[_, _]]
        }
      
        "fail to load if the first type is wrong" in {
          loadConfig(read[Int]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the first type is wrong" in {
          withValues(read[Int]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12))) shouldBe a[Left[_, _]]
        }
      
        "fail to load if the last type is wrong" in {
          loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[Int]("key12"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the last type is wrong" in {
          withValues(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[Int]("key12"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12))) shouldBe a[Left[_, _]]
        }
      
        "fail to load and accumulate the errors" in {
          loadConfig(read[Int]("key1"), read[Int]("key2"), read[Int]("key3"), read[Int]("key4"), read[Int]("key5"), read[Int]("key6"), read[Int]("key7"), read[Int]("key8"), read[Int]("key9"), read[Int]("key10"), read[Int]("key11"), read[Int]("key12"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12)).left.map(_.size) shouldBe Left(12)
        }
      
        "fail to load values and accumulate the errors" in {
          withValues(read[Int]("key1"), read[Int]("key2"), read[Int]("key3"), read[Int]("key4"), read[Int]("key5"), read[Int]("key6"), read[Int]("key7"), read[Int]("key8"), read[Int]("key9"), read[Int]("key10"), read[Int]("key11"), read[Int]("key12"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12))) shouldBe a[Left[_, _]]
        }
      }

      "loading 13 keys" should {
        "be able to load" in {
          loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13)) shouldBe Right(("value1", "value2", "value3", "value4", "value5", "value6", "value7", "value8", "value9", "value10", "value11", "value12", "value13"))
        }
      
        "be able to load values" in {
          withValues(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13))) shouldBe Right(("value1", "value2", "value3", "value4", "value5", "value6", "value7", "value8", "value9", "value10", "value11", "value12", "value13"))
        }
      
        "fail to load if the first one is missing" in {
          loadConfig(read[String]("akey1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the first one is missing" in {
          withValues(read[String]("akey1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13))) shouldBe a[Left[_, _]]
        }
      
        "fail to load if the last one is missing in" in {
          loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("akey13"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the last one is missing" in {
          withValues(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("akey13"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13))) shouldBe a[Left[_, _]]
        }
      
        "fail to load if the first type is wrong" in {
          loadConfig(read[Int]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the first type is wrong" in {
          withValues(read[Int]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13))) shouldBe a[Left[_, _]]
        }
      
        "fail to load if the last type is wrong" in {
          loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[Int]("key13"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the last type is wrong" in {
          withValues(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[Int]("key13"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13))) shouldBe a[Left[_, _]]
        }
      
        "fail to load and accumulate the errors" in {
          loadConfig(read[Int]("key1"), read[Int]("key2"), read[Int]("key3"), read[Int]("key4"), read[Int]("key5"), read[Int]("key6"), read[Int]("key7"), read[Int]("key8"), read[Int]("key9"), read[Int]("key10"), read[Int]("key11"), read[Int]("key12"), read[Int]("key13"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13)).left.map(_.size) shouldBe Left(13)
        }
      
        "fail to load values and accumulate the errors" in {
          withValues(read[Int]("key1"), read[Int]("key2"), read[Int]("key3"), read[Int]("key4"), read[Int]("key5"), read[Int]("key6"), read[Int]("key7"), read[Int]("key8"), read[Int]("key9"), read[Int]("key10"), read[Int]("key11"), read[Int]("key12"), read[Int]("key13"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13))) shouldBe a[Left[_, _]]
        }
      }

      "loading 14 keys" should {
        "be able to load" in {
          loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14)) shouldBe Right(("value1", "value2", "value3", "value4", "value5", "value6", "value7", "value8", "value9", "value10", "value11", "value12", "value13", "value14"))
        }
      
        "be able to load values" in {
          withValues(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14))) shouldBe Right(("value1", "value2", "value3", "value4", "value5", "value6", "value7", "value8", "value9", "value10", "value11", "value12", "value13", "value14"))
        }
      
        "fail to load if the first one is missing" in {
          loadConfig(read[String]("akey1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the first one is missing" in {
          withValues(read[String]("akey1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14))) shouldBe a[Left[_, _]]
        }
      
        "fail to load if the last one is missing in" in {
          loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("akey14"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the last one is missing" in {
          withValues(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("akey14"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14))) shouldBe a[Left[_, _]]
        }
      
        "fail to load if the first type is wrong" in {
          loadConfig(read[Int]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the first type is wrong" in {
          withValues(read[Int]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14))) shouldBe a[Left[_, _]]
        }
      
        "fail to load if the last type is wrong" in {
          loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[Int]("key14"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the last type is wrong" in {
          withValues(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[Int]("key14"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14))) shouldBe a[Left[_, _]]
        }
      
        "fail to load and accumulate the errors" in {
          loadConfig(read[Int]("key1"), read[Int]("key2"), read[Int]("key3"), read[Int]("key4"), read[Int]("key5"), read[Int]("key6"), read[Int]("key7"), read[Int]("key8"), read[Int]("key9"), read[Int]("key10"), read[Int]("key11"), read[Int]("key12"), read[Int]("key13"), read[Int]("key14"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14)).left.map(_.size) shouldBe Left(14)
        }
      
        "fail to load values and accumulate the errors" in {
          withValues(read[Int]("key1"), read[Int]("key2"), read[Int]("key3"), read[Int]("key4"), read[Int]("key5"), read[Int]("key6"), read[Int]("key7"), read[Int]("key8"), read[Int]("key9"), read[Int]("key10"), read[Int]("key11"), read[Int]("key12"), read[Int]("key13"), read[Int]("key14"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14))) shouldBe a[Left[_, _]]
        }
      }

      "loading 15 keys" should {
        "be able to load" in {
          loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15)) shouldBe Right(("value1", "value2", "value3", "value4", "value5", "value6", "value7", "value8", "value9", "value10", "value11", "value12", "value13", "value14", "value15"))
        }
      
        "be able to load values" in {
          withValues(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14, b15) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15))) shouldBe Right(("value1", "value2", "value3", "value4", "value5", "value6", "value7", "value8", "value9", "value10", "value11", "value12", "value13", "value14", "value15"))
        }
      
        "fail to load if the first one is missing" in {
          loadConfig(read[String]("akey1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the first one is missing" in {
          withValues(read[String]("akey1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14, b15) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15))) shouldBe a[Left[_, _]]
        }
      
        "fail to load if the last one is missing in" in {
          loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("akey15"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the last one is missing" in {
          withValues(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("akey15"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14, b15) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15))) shouldBe a[Left[_, _]]
        }
      
        "fail to load if the first type is wrong" in {
          loadConfig(read[Int]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the first type is wrong" in {
          withValues(read[Int]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14, b15) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15))) shouldBe a[Left[_, _]]
        }
      
        "fail to load if the last type is wrong" in {
          loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[Int]("key15"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the last type is wrong" in {
          withValues(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[Int]("key15"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14, b15) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15))) shouldBe a[Left[_, _]]
        }
      
        "fail to load and accumulate the errors" in {
          loadConfig(read[Int]("key1"), read[Int]("key2"), read[Int]("key3"), read[Int]("key4"), read[Int]("key5"), read[Int]("key6"), read[Int]("key7"), read[Int]("key8"), read[Int]("key9"), read[Int]("key10"), read[Int]("key11"), read[Int]("key12"), read[Int]("key13"), read[Int]("key14"), read[Int]("key15"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15)).left.map(_.size) shouldBe Left(15)
        }
      
        "fail to load values and accumulate the errors" in {
          withValues(read[Int]("key1"), read[Int]("key2"), read[Int]("key3"), read[Int]("key4"), read[Int]("key5"), read[Int]("key6"), read[Int]("key7"), read[Int]("key8"), read[Int]("key9"), read[Int]("key10"), read[Int]("key11"), read[Int]("key12"), read[Int]("key13"), read[Int]("key14"), read[Int]("key15"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14, b15) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15))) shouldBe a[Left[_, _]]
        }
      }

      "loading 16 keys" should {
        "be able to load" in {
          loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16)) shouldBe Right(("value1", "value2", "value3", "value4", "value5", "value6", "value7", "value8", "value9", "value10", "value11", "value12", "value13", "value14", "value15", "value16"))
        }
      
        "be able to load values" in {
          withValues(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14, b15, b16) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16))) shouldBe Right(("value1", "value2", "value3", "value4", "value5", "value6", "value7", "value8", "value9", "value10", "value11", "value12", "value13", "value14", "value15", "value16"))
        }
      
        "fail to load if the first one is missing" in {
          loadConfig(read[String]("akey1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the first one is missing" in {
          withValues(read[String]("akey1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14, b15, b16) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16))) shouldBe a[Left[_, _]]
        }
      
        "fail to load if the last one is missing in" in {
          loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("akey16"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the last one is missing" in {
          withValues(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("akey16"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14, b15, b16) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16))) shouldBe a[Left[_, _]]
        }
      
        "fail to load if the first type is wrong" in {
          loadConfig(read[Int]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the first type is wrong" in {
          withValues(read[Int]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14, b15, b16) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16))) shouldBe a[Left[_, _]]
        }
      
        "fail to load if the last type is wrong" in {
          loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[Int]("key16"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the last type is wrong" in {
          withValues(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[Int]("key16"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14, b15, b16) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16))) shouldBe a[Left[_, _]]
        }
      
        "fail to load and accumulate the errors" in {
          loadConfig(read[Int]("key1"), read[Int]("key2"), read[Int]("key3"), read[Int]("key4"), read[Int]("key5"), read[Int]("key6"), read[Int]("key7"), read[Int]("key8"), read[Int]("key9"), read[Int]("key10"), read[Int]("key11"), read[Int]("key12"), read[Int]("key13"), read[Int]("key14"), read[Int]("key15"), read[Int]("key16"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16)).left.map(_.size) shouldBe Left(16)
        }
      
        "fail to load values and accumulate the errors" in {
          withValues(read[Int]("key1"), read[Int]("key2"), read[Int]("key3"), read[Int]("key4"), read[Int]("key5"), read[Int]("key6"), read[Int]("key7"), read[Int]("key8"), read[Int]("key9"), read[Int]("key10"), read[Int]("key11"), read[Int]("key12"), read[Int]("key13"), read[Int]("key14"), read[Int]("key15"), read[Int]("key16"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14, b15, b16) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16))) shouldBe a[Left[_, _]]
        }
      }

      "loading 17 keys" should {
        "be able to load" in {
          loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17)) shouldBe Right(("value1", "value2", "value3", "value4", "value5", "value6", "value7", "value8", "value9", "value10", "value11", "value12", "value13", "value14", "value15", "value16", "value17"))
        }
      
        "be able to load values" in {
          withValues(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14, b15, b16, b17) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17))) shouldBe Right(("value1", "value2", "value3", "value4", "value5", "value6", "value7", "value8", "value9", "value10", "value11", "value12", "value13", "value14", "value15", "value16", "value17"))
        }
      
        "fail to load if the first one is missing" in {
          loadConfig(read[String]("akey1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the first one is missing" in {
          withValues(read[String]("akey1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14, b15, b16, b17) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17))) shouldBe a[Left[_, _]]
        }
      
        "fail to load if the last one is missing in" in {
          loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("akey17"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the last one is missing" in {
          withValues(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("akey17"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14, b15, b16, b17) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17))) shouldBe a[Left[_, _]]
        }
      
        "fail to load if the first type is wrong" in {
          loadConfig(read[Int]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the first type is wrong" in {
          withValues(read[Int]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14, b15, b16, b17) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17))) shouldBe a[Left[_, _]]
        }
      
        "fail to load if the last type is wrong" in {
          loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[Int]("key17"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the last type is wrong" in {
          withValues(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[Int]("key17"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14, b15, b16, b17) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17))) shouldBe a[Left[_, _]]
        }
      
        "fail to load and accumulate the errors" in {
          loadConfig(read[Int]("key1"), read[Int]("key2"), read[Int]("key3"), read[Int]("key4"), read[Int]("key5"), read[Int]("key6"), read[Int]("key7"), read[Int]("key8"), read[Int]("key9"), read[Int]("key10"), read[Int]("key11"), read[Int]("key12"), read[Int]("key13"), read[Int]("key14"), read[Int]("key15"), read[Int]("key16"), read[Int]("key17"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17)).left.map(_.size) shouldBe Left(17)
        }
      
        "fail to load values and accumulate the errors" in {
          withValues(read[Int]("key1"), read[Int]("key2"), read[Int]("key3"), read[Int]("key4"), read[Int]("key5"), read[Int]("key6"), read[Int]("key7"), read[Int]("key8"), read[Int]("key9"), read[Int]("key10"), read[Int]("key11"), read[Int]("key12"), read[Int]("key13"), read[Int]("key14"), read[Int]("key15"), read[Int]("key16"), read[Int]("key17"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14, b15, b16, b17) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17))) shouldBe a[Left[_, _]]
        }
      }

      "loading 18 keys" should {
        "be able to load" in {
          loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"), read[String]("key18"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18)) shouldBe Right(("value1", "value2", "value3", "value4", "value5", "value6", "value7", "value8", "value9", "value10", "value11", "value12", "value13", "value14", "value15", "value16", "value17", "value18"))
        }
      
        "be able to load values" in {
          withValues(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"), read[String]("key18"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14, b15, b16, b17, b18) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"), read[String]("key18"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18))) shouldBe Right(("value1", "value2", "value3", "value4", "value5", "value6", "value7", "value8", "value9", "value10", "value11", "value12", "value13", "value14", "value15", "value16", "value17", "value18"))
        }
      
        "fail to load if the first one is missing" in {
          loadConfig(read[String]("akey1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"), read[String]("key18"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the first one is missing" in {
          withValues(read[String]("akey1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"), read[String]("key18"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14, b15, b16, b17, b18) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"), read[String]("key18"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18))) shouldBe a[Left[_, _]]
        }
      
        "fail to load if the last one is missing in" in {
          loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"), read[String]("akey18"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the last one is missing" in {
          withValues(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"), read[String]("akey18"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14, b15, b16, b17, b18) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"), read[String]("key18"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18))) shouldBe a[Left[_, _]]
        }
      
        "fail to load if the first type is wrong" in {
          loadConfig(read[Int]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"), read[String]("key18"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the first type is wrong" in {
          withValues(read[Int]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"), read[String]("key18"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14, b15, b16, b17, b18) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"), read[String]("key18"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18))) shouldBe a[Left[_, _]]
        }
      
        "fail to load if the last type is wrong" in {
          loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"), read[Int]("key18"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the last type is wrong" in {
          withValues(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"), read[Int]("key18"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14, b15, b16, b17, b18) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"), read[String]("key18"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18))) shouldBe a[Left[_, _]]
        }
      
        "fail to load and accumulate the errors" in {
          loadConfig(read[Int]("key1"), read[Int]("key2"), read[Int]("key3"), read[Int]("key4"), read[Int]("key5"), read[Int]("key6"), read[Int]("key7"), read[Int]("key8"), read[Int]("key9"), read[Int]("key10"), read[Int]("key11"), read[Int]("key12"), read[Int]("key13"), read[Int]("key14"), read[Int]("key15"), read[Int]("key16"), read[Int]("key17"), read[Int]("key18"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18)).left.map(_.size) shouldBe Left(18)
        }
      
        "fail to load values and accumulate the errors" in {
          withValues(read[Int]("key1"), read[Int]("key2"), read[Int]("key3"), read[Int]("key4"), read[Int]("key5"), read[Int]("key6"), read[Int]("key7"), read[Int]("key8"), read[Int]("key9"), read[Int]("key10"), read[Int]("key11"), read[Int]("key12"), read[Int]("key13"), read[Int]("key14"), read[Int]("key15"), read[Int]("key16"), read[Int]("key17"), read[Int]("key18"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14, b15, b16, b17, b18) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"), read[String]("key18"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18))) shouldBe a[Left[_, _]]
        }
      }

      "loading 19 keys" should {
        "be able to load" in {
          loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"), read[String]("key18"), read[String]("key19"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19)) shouldBe Right(("value1", "value2", "value3", "value4", "value5", "value6", "value7", "value8", "value9", "value10", "value11", "value12", "value13", "value14", "value15", "value16", "value17", "value18", "value19"))
        }
      
        "be able to load values" in {
          withValues(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"), read[String]("key18"), read[String]("key19"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14, b15, b16, b17, b18, b19) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"), read[String]("key18"), read[String]("key19"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19))) shouldBe Right(("value1", "value2", "value3", "value4", "value5", "value6", "value7", "value8", "value9", "value10", "value11", "value12", "value13", "value14", "value15", "value16", "value17", "value18", "value19"))
        }
      
        "fail to load if the first one is missing" in {
          loadConfig(read[String]("akey1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"), read[String]("key18"), read[String]("key19"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the first one is missing" in {
          withValues(read[String]("akey1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"), read[String]("key18"), read[String]("key19"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14, b15, b16, b17, b18, b19) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"), read[String]("key18"), read[String]("key19"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19))) shouldBe a[Left[_, _]]
        }
      
        "fail to load if the last one is missing in" in {
          loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"), read[String]("key18"), read[String]("akey19"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the last one is missing" in {
          withValues(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"), read[String]("key18"), read[String]("akey19"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14, b15, b16, b17, b18, b19) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"), read[String]("key18"), read[String]("key19"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19))) shouldBe a[Left[_, _]]
        }
      
        "fail to load if the first type is wrong" in {
          loadConfig(read[Int]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"), read[String]("key18"), read[String]("key19"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the first type is wrong" in {
          withValues(read[Int]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"), read[String]("key18"), read[String]("key19"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14, b15, b16, b17, b18, b19) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"), read[String]("key18"), read[String]("key19"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19))) shouldBe a[Left[_, _]]
        }
      
        "fail to load if the last type is wrong" in {
          loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"), read[String]("key18"), read[Int]("key19"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the last type is wrong" in {
          withValues(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"), read[String]("key18"), read[Int]("key19"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14, b15, b16, b17, b18, b19) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"), read[String]("key18"), read[String]("key19"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19))) shouldBe a[Left[_, _]]
        }
      
        "fail to load and accumulate the errors" in {
          loadConfig(read[Int]("key1"), read[Int]("key2"), read[Int]("key3"), read[Int]("key4"), read[Int]("key5"), read[Int]("key6"), read[Int]("key7"), read[Int]("key8"), read[Int]("key9"), read[Int]("key10"), read[Int]("key11"), read[Int]("key12"), read[Int]("key13"), read[Int]("key14"), read[Int]("key15"), read[Int]("key16"), read[Int]("key17"), read[Int]("key18"), read[Int]("key19"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19)).left.map(_.size) shouldBe Left(19)
        }
      
        "fail to load values and accumulate the errors" in {
          withValues(read[Int]("key1"), read[Int]("key2"), read[Int]("key3"), read[Int]("key4"), read[Int]("key5"), read[Int]("key6"), read[Int]("key7"), read[Int]("key8"), read[Int]("key9"), read[Int]("key10"), read[Int]("key11"), read[Int]("key12"), read[Int]("key13"), read[Int]("key14"), read[Int]("key15"), read[Int]("key16"), read[Int]("key17"), read[Int]("key18"), read[Int]("key19"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14, b15, b16, b17, b18, b19) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"), read[String]("key18"), read[String]("key19"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19))) shouldBe a[Left[_, _]]
        }
      }

      "loading 20 keys" should {
        "be able to load" in {
          loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"), read[String]("key18"), read[String]("key19"), read[String]("key20"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20)) shouldBe Right(("value1", "value2", "value3", "value4", "value5", "value6", "value7", "value8", "value9", "value10", "value11", "value12", "value13", "value14", "value15", "value16", "value17", "value18", "value19", "value20"))
        }
      
        "be able to load values" in {
          withValues(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"), read[String]("key18"), read[String]("key19"), read[String]("key20"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14, b15, b16, b17, b18, b19, b20) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"), read[String]("key18"), read[String]("key19"), read[String]("key20"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20))) shouldBe Right(("value1", "value2", "value3", "value4", "value5", "value6", "value7", "value8", "value9", "value10", "value11", "value12", "value13", "value14", "value15", "value16", "value17", "value18", "value19", "value20"))
        }
      
        "fail to load if the first one is missing" in {
          loadConfig(read[String]("akey1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"), read[String]("key18"), read[String]("key19"), read[String]("key20"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the first one is missing" in {
          withValues(read[String]("akey1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"), read[String]("key18"), read[String]("key19"), read[String]("key20"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14, b15, b16, b17, b18, b19, b20) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"), read[String]("key18"), read[String]("key19"), read[String]("key20"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20))) shouldBe a[Left[_, _]]
        }
      
        "fail to load if the last one is missing in" in {
          loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"), read[String]("key18"), read[String]("key19"), read[String]("akey20"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the last one is missing" in {
          withValues(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"), read[String]("key18"), read[String]("key19"), read[String]("akey20"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14, b15, b16, b17, b18, b19, b20) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"), read[String]("key18"), read[String]("key19"), read[String]("key20"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20))) shouldBe a[Left[_, _]]
        }
      
        "fail to load if the first type is wrong" in {
          loadConfig(read[Int]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"), read[String]("key18"), read[String]("key19"), read[String]("key20"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the first type is wrong" in {
          withValues(read[Int]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"), read[String]("key18"), read[String]("key19"), read[String]("key20"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14, b15, b16, b17, b18, b19, b20) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"), read[String]("key18"), read[String]("key19"), read[String]("key20"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20))) shouldBe a[Left[_, _]]
        }
      
        "fail to load if the last type is wrong" in {
          loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"), read[String]("key18"), read[String]("key19"), read[Int]("key20"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the last type is wrong" in {
          withValues(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"), read[String]("key18"), read[String]("key19"), read[Int]("key20"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14, b15, b16, b17, b18, b19, b20) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"), read[String]("key18"), read[String]("key19"), read[String]("key20"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20))) shouldBe a[Left[_, _]]
        }
      
        "fail to load and accumulate the errors" in {
          loadConfig(read[Int]("key1"), read[Int]("key2"), read[Int]("key3"), read[Int]("key4"), read[Int]("key5"), read[Int]("key6"), read[Int]("key7"), read[Int]("key8"), read[Int]("key9"), read[Int]("key10"), read[Int]("key11"), read[Int]("key12"), read[Int]("key13"), read[Int]("key14"), read[Int]("key15"), read[Int]("key16"), read[Int]("key17"), read[Int]("key18"), read[Int]("key19"), read[Int]("key20"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20)).left.map(_.size) shouldBe Left(20)
        }
      
        "fail to load values and accumulate the errors" in {
          withValues(read[Int]("key1"), read[Int]("key2"), read[Int]("key3"), read[Int]("key4"), read[Int]("key5"), read[Int]("key6"), read[Int]("key7"), read[Int]("key8"), read[Int]("key9"), read[Int]("key10"), read[Int]("key11"), read[Int]("key12"), read[Int]("key13"), read[Int]("key14"), read[Int]("key15"), read[Int]("key16"), read[Int]("key17"), read[Int]("key18"), read[Int]("key19"), read[Int]("key20"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14, b15, b16, b17, b18, b19, b20) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"), read[String]("key18"), read[String]("key19"), read[String]("key20"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20))) shouldBe a[Left[_, _]]
        }
      }

      "loading 21 keys" should {
        "be able to load" in {
          loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"), read[String]("key18"), read[String]("key19"), read[String]("key20"), read[String]("key21"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21)) shouldBe Right(("value1", "value2", "value3", "value4", "value5", "value6", "value7", "value8", "value9", "value10", "value11", "value12", "value13", "value14", "value15", "value16", "value17", "value18", "value19", "value20", "value21"))
        }
      
        "be able to load values" in {
          withValues(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"), read[String]("key18"), read[String]("key19"), read[String]("key20"), read[String]("key21"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14, b15, b16, b17, b18, b19, b20, b21) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"), read[String]("key18"), read[String]("key19"), read[String]("key20"), read[String]("key21"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21))) shouldBe Right(("value1", "value2", "value3", "value4", "value5", "value6", "value7", "value8", "value9", "value10", "value11", "value12", "value13", "value14", "value15", "value16", "value17", "value18", "value19", "value20", "value21"))
        }
      
        "fail to load if the first one is missing" in {
          loadConfig(read[String]("akey1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"), read[String]("key18"), read[String]("key19"), read[String]("key20"), read[String]("key21"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the first one is missing" in {
          withValues(read[String]("akey1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"), read[String]("key18"), read[String]("key19"), read[String]("key20"), read[String]("key21"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14, b15, b16, b17, b18, b19, b20, b21) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"), read[String]("key18"), read[String]("key19"), read[String]("key20"), read[String]("key21"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21))) shouldBe a[Left[_, _]]
        }
      
        "fail to load if the last one is missing in" in {
          loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"), read[String]("key18"), read[String]("key19"), read[String]("key20"), read[String]("akey21"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the last one is missing" in {
          withValues(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"), read[String]("key18"), read[String]("key19"), read[String]("key20"), read[String]("akey21"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14, b15, b16, b17, b18, b19, b20, b21) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"), read[String]("key18"), read[String]("key19"), read[String]("key20"), read[String]("key21"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21))) shouldBe a[Left[_, _]]
        }
      
        "fail to load if the first type is wrong" in {
          loadConfig(read[Int]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"), read[String]("key18"), read[String]("key19"), read[String]("key20"), read[String]("key21"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the first type is wrong" in {
          withValues(read[Int]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"), read[String]("key18"), read[String]("key19"), read[String]("key20"), read[String]("key21"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14, b15, b16, b17, b18, b19, b20, b21) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"), read[String]("key18"), read[String]("key19"), read[String]("key20"), read[String]("key21"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21))) shouldBe a[Left[_, _]]
        }
      
        "fail to load if the last type is wrong" in {
          loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"), read[String]("key18"), read[String]("key19"), read[String]("key20"), read[Int]("key21"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21)) shouldBe a[Left[_, _]]
        }
      
        "fail to load values if the last type is wrong" in {
          withValues(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"), read[String]("key18"), read[String]("key19"), read[String]("key20"), read[Int]("key21"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14, b15, b16, b17, b18, b19, b20, b21) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"), read[String]("key18"), read[String]("key19"), read[String]("key20"), read[String]("key21"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21))) shouldBe a[Left[_, _]]
        }
      
        "fail to load and accumulate the errors" in {
          loadConfig(read[Int]("key1"), read[Int]("key2"), read[Int]("key3"), read[Int]("key4"), read[Int]("key5"), read[Int]("key6"), read[Int]("key7"), read[Int]("key8"), read[Int]("key9"), read[Int]("key10"), read[Int]("key11"), read[Int]("key12"), read[Int]("key13"), read[Int]("key14"), read[Int]("key15"), read[Int]("key16"), read[Int]("key17"), read[Int]("key18"), read[Int]("key19"), read[Int]("key20"), read[Int]("key21"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21)).left.map(_.size) shouldBe Left(21)
        }
      
        "fail to load values and accumulate the errors" in {
          withValues(read[Int]("key1"), read[Int]("key2"), read[Int]("key3"), read[Int]("key4"), read[Int]("key5"), read[Int]("key6"), read[Int]("key7"), read[Int]("key8"), read[Int]("key9"), read[Int]("key10"), read[Int]("key11"), read[Int]("key12"), read[Int]("key13"), read[Int]("key14"), read[Int]("key15"), read[Int]("key16"), read[Int]("key17"), read[Int]("key18"), read[Int]("key19"), read[Int]("key20"), read[Int]("key21"))((b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14, b15, b16, b17, b18, b19, b20, b21) => loadConfig(read[String]("key1"), read[String]("key2"), read[String]("key3"), read[String]("key4"), read[String]("key5"), read[String]("key6"), read[String]("key7"), read[String]("key8"), read[String]("key9"), read[String]("key10"), read[String]("key11"), read[String]("key12"), read[String]("key13"), read[String]("key14"), read[String]("key15"), read[String]("key16"), read[String]("key17"), read[String]("key18"), read[String]("key19"), read[String]("key20"), read[String]("key21"))((a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21) => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21))) shouldBe a[Left[_, _]]
        }
      }
    }
  }
}