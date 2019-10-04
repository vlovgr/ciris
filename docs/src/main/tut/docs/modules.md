---
layout: docs
title: "Modules Overview"
position: 5
permalink: /docs/modules
---

```tut:invisible
val tempFile = {
  val file = java.io.File.createTempFile("temp-", ".tmp")
  file.deleteOnExit()

  val writer = new java.io.FileWriter(file)
  try {
    writer.write("1234 ")
  } finally {
    writer.close()
  }

  file
}
```

# Modules Overview

The following modules integrate with external libraries to provide extended functionality.
You might also be interested in the third-party [external libraries](/#external-libraries) which integrate with Ciris.

- The [enumeratum module](/docs/enumeratum-module) enables decoding of [enumeratum][enumeratum] enumerations.

```tut:silent
object environments {
  import enumeratum._

  sealed abstract class AppEnvironment extends EnumEntry

  object AppEnvironment extends Enum[AppEnvironment] {
    case object Local extends AppEnvironment
    case object Testing extends AppEnvironment
    case object Production extends AppEnvironment

    val values = findValues
  }
}

import ciris.enumeratum._
import ciris.env
import environments._

env[AppEnvironment]("APP_ENV")
```

- The [generic module](/docs/generic-module) enables generic decoding of product and coproduct types using [shapeless][shapeless].

```tut:silent
import ciris.generic._
import ciris.prop

final case class Decibel(val value: Int) extends AnyVal

prop[Decibel]("ratio.delta")
```

- The [refined module](/docs/refined-module) enables decoding of [refined][refined] refinement types.

```tut:silent
import ciris.refined._
import eu.timepit.refined.types.net.UserPortNumber

env[Option[UserPortNumber]]("HTTP_PORT")
```

- The [spire module](/docs/spire-module) enables decoding of [spire][spire] number types.

```tut:silent
import ciris.spire._
import spire.math.Natural

env[Natural]("DELAY_SECONDS")
```

- The [squants module](/docs/squants-module) enables decoding of [squants][squants] quantities with unit of measure.

```tut:silent
import ciris.squants._
import squants.time.Time

prop[Time]("refresh.interval")
```

[cats]: https://github.com/typelevel/cats
[cats-effect]: https://github.com/typelevel/cats-effect
[enumeratum]: https://github.com/lloydmeta/enumeratum
[shapeless]: https://github.com/milessabin/shapeless
[refined]: https://github.com/fthomas/refined
[spire]: https://github.com/non/spire
[squants]: https://github.com/typelevel/squants
