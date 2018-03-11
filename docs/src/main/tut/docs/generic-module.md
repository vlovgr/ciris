---
layout: docs
title: "Generic Module"
permalink: /docs/generic-module
---

# Generic Module
The `ciris-generic` module provides the ability to decode products and coproducts using [shapeless][shapeless]. This allows you to decode case classes, [value classes](http://docs.scala-lang.org/overviews/core/value-classes.html), and shapeless coproducts, plus anything else that shapeless `Generic` supports. Let's take a look at these cases to see how it works. We start by defining a source from which we can read configuration values.

```tut:silent
import ciris.{ConfigKeyType, ConfigSource}
import ciris.generic._

val source = {
  val keyType = ConfigKeyType[String]("generic key")
  ConfigSource.fromEntries(keyType)("key" -> "5.0")
}
```

We can then define and load a unary product, for example a case class with one value.

```tut:book
final case class DoubleValue(value: Double)

source.read("key").decodeValue[DoubleValue]
```

It also works for value classes and any other unary products shapeless `Generic` supports.

```tut:book
final case class FloatValue(val value: Float) extends AnyVal

source.read("key").decodeValue[FloatValue]
```

If we define a product with more than one value:

```tut:book
final case class TwoValues(value1: Double, value2: Float)
```

we will try to decode it twice, once as a `Double`, and once as a `Float`.

```tut:book
source.read("key").decodeValue[TwoValues]
```

You can also customize the decoding on a per-type basis. For example, if you have a [`ConfigSource`][ConfigSource] which reads values of type `Map[String, String]`, you can customize which key gets decoded for which type, as in the example below.

```tut:book
import ciris.ConfigDecoder

val mapSource = {
  val keyType = ConfigKeyType[String]("generic key")
  ConfigSource.fromEntries(keyType)(
    "key" -> Map(
      "key1" -> "1.0",
      "key2" -> "2.0"
    )
  )
}

implicit val decodeDouble: ConfigDecoder[Map[String, String], Double] =
  ConfigDecoder.catchNonFatal("Double")(map => map("key1").toDouble)

implicit val decodeFloat: ConfigDecoder[Map[String, String], Float] =
  ConfigDecoder.catchNonFatal("Float")(map => map("key2").toFloat)

mapSource.read("key").decodeValue[TwoValues]
```

We can also define a shapeless coproduct and attempt to decode it.

```tut:book
import shapeless.{:+:, CNil}

type DoubleOrFloat = DoubleValue :+: FloatValue :+: CNil

source.read("key").decodeValue[DoubleOrFloat]
```

If there is no public constructor or `apply` method:

```tut:book
object PrivateValues {
  final class PrivateFloatValue private(val value: Float) extends AnyVal
  object PrivateFloatValue {
    def withValue(value: Float) =
      new PrivateFloatValue(value)
  }
}

import PrivateValues._
```

we will not be able to decode values, resulting in an error at compile-time.

```tut:fail:book
source.read("key").decodeValue[PrivateFloatValue]
```

[shapeless]: https://github.com/milessabin/shapeless
[ConfigSource]: /api/ciris/ConfigSource.html
