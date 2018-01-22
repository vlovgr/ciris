---
layout: docs
title: "Modules Overview"
position: 3
permalink: /docs/modules
---

# Modules Overview
Ciris core module, `ciris-core`, provides basic functionality and decoders for many standard library types. See [Core Concepts](/docs/concepts) for an explanation of the concepts in the core module. Remaining sections mostly cover the core module in greater detail, in particular [Configuration Sources](/docs/sources) and [Custom Decoders](/docs/decoders). In the sections below, Ciris' other modules are explained briefly.

## Enumeratum
The `ciris-enumeratum` module provides support for reading [enumeratum][enumeratum] enumerations. You can refer to the [documentation](/api/ciris/enumeratum) for a complete list of supported enumerations. As an example, let's define an `Enum` and see how we can load values of that enumeration using Ciris. Enumeratum provides mixins, like `Lowercase` below, which we can use to customize the name of our enumerations, and in turn, what values we will be able to load with Ciris.

```tut:book:reset
import enumeratum._
import enumeratum.EnumEntry._

object configuration {
  sealed abstract class AppEnvironment extends EnumEntry with Lowercase
  object AppEnvironment extends Enum[AppEnvironment] {
    case object Local extends AppEnvironment
    case object Testing extends AppEnvironment
    case object Production extends AppEnvironment

    val values = findValues
  }
}
```

Let's also define a custom [configuration source](/docs/sources), holding some values we can ask Ciris to load.

```tut:book
import ciris._
import ciris.enumeratum._
import configuration._

val source = {
  val keyType = ConfigKeyType[String]("enumeratum key")
  ConfigSource.fromMap(keyType)(Map(
    "localEnv" -> "local",
    "testingEnv" -> "testing",
    "TestingEnv" -> "Testing",
    "invalidEnv" -> "invalid"
  ))
}
```

We can then ask Ciris to load values from the source and decode them as enumeration values.

```tut:book
source.read("localEnv").decodeValue[AppEnvironment]

source.read("testingEnv").decodeValue[AppEnvironment]

source.read("TestingEnv").decodeValue[AppEnvironment]

source.read("invalidEnv").decodeValue[AppEnvironment]
```

Dealing with multiple environments is a common use case for configurations, explained in greater detail in the [Multiple Environments](/docs/environments) section.

## Generic
The `ciris-generic` module provides the ability to read unary products, and coproducts using [shapeless][shapeless]. This allows you to load case classes with one argument, including [value classes](http://docs.scala-lang.org/overviews/core/value-classes.html), and shapeless coproducts, plus anything else that shapeless' `Generic` supports. Let's take a brief look at these cases to see how it works in practice. We start by defining a source from which we can read configuration values.

```tut:book:reset
import ciris._
import ciris.generic._

val source = {
  val keyType = ConfigKeyType[String]("generic key")
  ConfigSource.fromMap(keyType)(Map("key" -> "5.0"))
}
```

We can then define and load a unary product, for example a case class with one value.

```scala
final case class DoubleValue(value: Double)

source.read("key").decodeValue[DoubleValue]
```

It also works for value classes and any other unary products shapeless' `Generic` supports.

```scala
final class FloatValue(val value: Float) extends AnyVal

source.read("key").decodeValue[FloatValue]
```

We can also define a shapeless coproduct and load it.

```scala
import shapeless.{:+:, CNil}

type DoubleOrFloat = DoubleValue :+: FloatValue :+: CNil

source.read("key").decodeValue[DoubleOrFloat]
```

If we define a product with more than one value:

```tut:book
final case class TwoValues(value1: Double, value2: Float)
```

we will not be able to load it, resulting in an error at compile-time.

```tut:fail:book
source.read("key").decodeValue[TwoValues]
```

Also, if there's no public constructor or apply method:

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

we will not be able to load it, again resulting in an error at compile-time.

```tut:fail:book
source.read("key").decodeValue[PrivateFloatValue]
```

## Refined
The `ciris-refined` module allows you to load refinement types from [refined][refined]. Let's start by defining a configuration source from which to read some values.

```tut:book:reset
import ciris._
import ciris.refined._

val source = {
  val keyType = ConfigKeyType[String]("refined key")
  ConfigSource.fromMap(keyType)(Map(
    "negative" -> "-1",
    "zero" -> "0",
    "positive" -> "1",
    "other" -> "abc"
  ))
}
```

In this example, we'll simply try to read `PosInt` values, which are all `Int`s strictly greater than zero.

```tut:book
import eu.timepit.refined.types.numeric.PosInt

source.read("negative").decodeValue[PosInt]

source.read("zero").decodeValue[PosInt]

source.read("positive").decodeValue[PosInt]

source.read("other").decodeValue[PosInt]
```

Refinement types are useful for making sure your configuration is valid. See the [Encoding Validation](/docs/validation) section for more information.

## Spire
The `ciris-spire` module adds support for loading [spire][spire] number types. For a complete list of support number types, refer to the [documentation](/api/ciris/spire). Let's see how we can load spire number types by first defining a custom configuration source.

```tut:book:reset
import spire.math._
import ciris._
import ciris.spire._

val source = {
  val keyType = ConfigKeyType[String]("spire key")
  ConfigSource.fromEntries(keyType)(
    "natural" -> "847365894625891365137596378546725",
    "interval" -> "(1/3, 524/51]",
    "rational" -> "-194712/-129831",
    "uint" -> (Int.MaxValue.toLong + 1L).toString
  )
}
```

We can then simply read entries from the source, using `read`, and decode the values into spire number types.

```tut:book
source.read("natural").decodeValue[Natural]

source.read("interval").decodeValue[Interval[Rational]]

source.read("rational").decodeValue[Rational]

source.read("natural").decodeValue[Number]

source.read("rational").decodeValue[Real]

source.read("trilean").decodeValue[Trilean]

source.read("uint").decodeValue[UInt]
```

## Squants
The `ciris-squants` module allows loading values with unit of measure from [squants][squants]. For a complete list of supported dimensions, refer to the [documentation](/api/ciris/squants). Let's see how this works by first defining a configuration source with a few different keys mapping to `Time` values, each having a different unit of measure.

```tut:book:reset
import squants.time.Time
import ciris._
import ciris.squants._

val source = {
  val keyType = ConfigKeyType[String]("squants key")
  ConfigSource.fromMap(keyType)(Map(
    "seconds" -> "3 s",
    "minutes" -> "23 m",
    "hours" -> "12 h"
  ))
}
```

We can then load these entries and decode the values into type `Time`.

```tut:book
val seconds = source.read("seconds").decodeValue[Time]
val minutes = source.read("minutes").decodeValue[Time]
val hours = source.read("hours").decodeValue[Time]

loadConfig(seconds, minutes, hours)(_ + _ + _).right.map(_.toMinutes)
```

[enumeratum]: https://github.com/lloydmeta/enumeratum
[refined]: https://github.com/fthomas/refined
[shapeless]: https://github.com/milessabin/shapeless
[spire]: https://github.com/non/spire
[squants]: https://github.com/typelevel/squants
