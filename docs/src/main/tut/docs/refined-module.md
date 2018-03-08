---
layout: docs
title: "Refined Module"
permalink: /docs/refined-module
---

# Refined Module
The `ciris-refined` module enables decoding of [refined][refined] refinement types. This is especially useful for [encoding validation](/docs/validation) in the types of your configuration values. Let's see how we can decode refinement types. We'll start by defining a [configuration source](/docs/sources) from which to read some configuration values.

```tut:silent
import ciris.{ConfigKeyType, ConfigSource}
import ciris.refined._

val source = {
  val keyType = ConfigKeyType[String]("refined key")
  ConfigSource.fromEntries(keyType)(
    "negative" -> "-1",
    "zero" -> "0",
    "positive" -> "1",
    "other" -> "abc"
  )
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

[refined]: https://github.com/fthomas/refined
