---
layout: docs
title: "Spire Module"
permalink: /docs/spire-module
---

# Spire Module
The `ciris-spire` module enables decoding of [spire][spire] number types. For a complete list of supported types, refer to the [documentation](/api/ciris/spire). Let's see how we can decode spire number types by first defining a custom [configuration source](/docs/sources) from which to load some configuration values.

```tut:silent
import ciris.{ConfigKeyType, ConfigSource}
import ciris.spire._
import spire.math._

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

We can then read entries from the source and decode values into spire number types.

```tut:book
source.read("natural").decodeValue[Natural]

source.read("interval").decodeValue[Interval[Rational]]

source.read("rational").decodeValue[Rational]

source.read("natural").decodeValue[Number]

source.read("rational").decodeValue[Real]

source.read("trilean").decodeValue[Trilean]

source.read("uint").decodeValue[UInt]
```

[spire]: https://github.com/non/spire
