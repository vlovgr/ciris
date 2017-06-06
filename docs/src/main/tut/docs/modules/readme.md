---
layout: docs
title: "Modules Overview"
position: 3
permalink: /docs/modules
---

# <a name="modules-overview" href="#modules-overview">Modules Overview</a>
Ciris core module, `ciris-core`, provides basic functionality and readers for many standard library types. See [Core Concepts](/docs/concepts) for an explanation of the concepts in the core module. Remaining sections mostly cover the core module in greater detail, in particular [Configuration Sources](/docs/sources) and [Custom Readers](/docs/readers). In the sections below, Ciris' other modules are explained briefly.

## <a name="enumeratum" href="#enumeratum">Enumeratum</a>
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

Let's also define a custom implicit [configuration source](/docs/sources), holding some values we can ask Ciris to load.

```tut:book
import ciris._
import ciris.enumeratum._
import configuration._

implicit val source = {
  val keyType = ConfigKeyType[String]("enumeratum key")
  ConfigSource.fromMap(keyType)(Map(
    "localEnv" -> "local",
    "testingEnv" -> "testing",
    "TestingEnv" -> "Testing",
    "invalidEnv" -> "invalid"
  ))
}
```

We can then ask Ciris to load values of the enumeration, from the implicit source, using the `read` method.

```tut:book
read[AppEnvironment]("localEnv")

read[AppEnvironment]("testingEnv")

read[AppEnvironment]("TestingEnv")

read[AppEnvironment]("invalidEnv")
```

Dealing with multiple environments is a common use case for configurations, explained in greater detail in the [Multiple Environments](/docs/environments) section.

## <a name="generic" href="#generic">Generic</a>
The `ciris-generic` module provides the ability to read unary products, and coproducts using [shapeless][shapeless]. This allows you to load case classes with one argument, including [value classes](http://docs.scala-lang.org/overviews/core/value-classes.html), and shapeless coproducts, plus anything else that shapeless' `Generic` supports. Let's take a brief look at these cases to see how it works in practice. We start by defining a source from which we can read configuration values.

```tut:book:reset
import ciris._
import ciris.generic._

implicit val source = {
  val keyType = ConfigKeyType[String]("generic key")
  ConfigSource.fromMap(keyType)(Map("key" -> "5.0"))
}
```

We can then define and load a unary product, for example a case class with one value.

```tut:book
final case class DoubleValue(value: Double)

read[DoubleValue]("key")
```

It also works for value classes and any other unary products shapeless' `Generic` supports.

```tut:book
final class FloatValue(val value: Float) extends AnyVal

read[FloatValue]("key")
```

We can also define a shapeless coproduct and load it.

```tut:book
import shapeless.{:+:, CNil}

type DoubleOrFloat = DoubleValue :+: FloatValue :+: CNil

read[DoubleOrFloat]("key")
```

If we define a product with more than one value:

```tut:book
final case class TwoValues(value1: Double, value2: Float)
```

we will not be able to load it, resulting in an error at compile-time.

```tut:fail:book
read[TwoValues]("key")
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
read[PrivateFloatValue]("key")
```

## <a name="refined" href="#refined">Refined</a>
The `ciris-refined` module allows you to load refinement types from [refined][refined]. Let's start by defining a configuration source from which to read some values.

```tut:book:reset
import ciris._
import ciris.refined._

implicit val source = {
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

read[PosInt]("negative")

read[PosInt]("zero")

read[PosInt]("positive")

read[PosInt]("other")
```

Refinement types are useful for making sure your configuration is valid. See the [Encoding Validation](/docs/validation) section for more information.

## <a name="squants" href="#squants">Squants</a>
The `ciris-squants` module allows loading values with unit of measure from [squants][squants]. For a complete list of supported dimensions, refer to the [documentation](/api/ciris/squants). Let's see how this works by first defining a configuration source with a few different keys mapping to `Time` values, each having a different unit of measure.

```tut:book:reset
import squants.time.Time
import ciris._
import ciris.squants._

implicit val source = {
  val keyType = ConfigKeyType[String]("squants key")
  ConfigSource.fromMap(keyType)(Map(
    "seconds" -> "3 s",
    "minutes" -> "23 m",
    "hours" -> "12 h"
  ))
}
```

We can then load these values by simply specifying that we want to read values of type `Time`.

```tut:book
val seconds = read[Time]("seconds")
val minutes = read[Time]("minutes")
val hours = read[Time]("hours")

loadConfig(seconds, minutes, hours)(_ + _ + _).map(_.toMinutes)
```

[enumeratum]: https://github.com/lloydmeta/enumeratum
[refined]: https://github.com/fthomas/refined
[shapeless]: https://github.com/milessabin/shapeless
[squants]: https://github.com/typelevel/squants
