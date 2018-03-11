---
layout: docs
title: "Squants Module"
permalink: /docs/squants-module
---

# Squants Module
The `ciris-squants` module enables decoding of quantities with unit of measure from [squants][squants]. For a complete list of supported dimensions, refer to the [documentation](/api/ciris/squants). Let's see how this works by first defining a [configuration source](/docs/sources) with a few different keys mapping to `Time` values, each having a different unit of measure.

```tut:silent
import ciris.{ConfigKeyType, ConfigSource}
import ciris.squants._
import squants.time.Time

val source = {
  val keyType = ConfigKeyType[String]("squants key")
  ConfigSource.fromEntries(keyType)(
    "seconds" -> "3 s",
    "minutes" -> "23 m",
    "hours" -> "12 h"
  )
}
```

We can then read entries from the source and decode values as `Time`.

```tut:book
val seconds = source.read("seconds").decodeValue[Time]

val minutes = source.read("minutes").decodeValue[Time]

val hours = source.read("hours").decodeValue[Time]
```

[squants]: https://github.com/typelevel/squants
