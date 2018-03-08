---
layout: docs
title: "Enumeratum Module"
permalink: /docs/enumeratum-module
---

# Enumeratum Module
The `ciris-enumeratum` module provides support for decoding [enumeratum][enumeratum] enumerations. Enumerations are especially useful for dealing with [multiple environments](/docs/environments). Let's take a look at an example, where we define an enumeration and try to load values of that enumeration. Enumeratum provides mixins, like `Lowercase` below, which can be used to customize the name of our enumeration values, and in turn, what values we will be able to decode.

```tut:silent
import enumeratum._
import enumeratum.EnumEntry._

object environments {
  sealed abstract class AppEnvironment extends EnumEntry with Lowercase

  object AppEnvironment extends Enum[AppEnvironment] {
    case object Local extends AppEnvironment
    case object Testing extends AppEnvironment
    case object Production extends AppEnvironment

    val values = findValues
  }
}
```

We also define a custom [configuration source](/docs/sources), holding some values we can attempt to decode.

```tut:silent
import ciris.{ConfigKeyType, ConfigSource}
import ciris.enumeratum._
import environments._

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

Finally, we can read values from the source and decode them as enumeration values.

```tut:book
source.read("localEnv").decodeValue[AppEnvironment]

source.read("testingEnv").decodeValue[AppEnvironment]

source.read("TestingEnv").decodeValue[AppEnvironment]

source.read("invalidEnv").decodeValue[AppEnvironment]
```

[enumeratum]: https://github.com/lloydmeta/enumeratum
