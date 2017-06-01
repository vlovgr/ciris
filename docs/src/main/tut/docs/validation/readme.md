---
layout: docs
title: "Encoding Validation"
position: 4
permalink: /docs/validation
---

# <a name="encoding-validation" href="#encoding-validation">Encoding Validation</a>
Ciris intentionally forces you to encode validation in your data types. This means that you have to put more thought into your configurations, and in turn, your domain models. For example, if you want to load an API key, you probably don't want it to be any `String` (not empty) and if you want to load a port value, you probably don't want it to be any `Int` (valid ports are 0 to 65535).

By using more precise types, we get type-safety and a guarantee that values conform to our requirements throughout the application. One of the easiest ways to encode validation in data types is by using [refined][refined]. Ciris provides a `ciris-refined` module which allows you to read any refined type. You can also get compile-time validation for literals from refined (supported by macros).

Let's modify the configuration seen in [Usage Basics](/docs/basics) to use refined types.

```tut:book
import eu.timepit.refined.auto._
import eu.timepit.refined.types.net.PortNumber
import eu.timepit.refined.types.string.NonEmptyString

import scala.concurrent.duration._

final case class Config(
  apiKey: NonEmptyString,
  timeout: Duration,
  port: PortNumber
)
```

Here we've said that the API key must be a non-empty `String` and that the port number must be an `Int` between 0 and 65535. We could also require that the timeout must be finite and within a certain range, but since it's always specified in the code, it's arguably harder to get wrong. Now, let's see how we can simply change the types to read in the `env` and `prop` methods to the refined types, and how Ciris will take care of loading them for you.

```tut:book
import ciris._
import ciris.refined._

val config =
  loadConfig(
    env[NonEmptyString]("API_KEY"),
    prop[Option[PortNumber]]("http.port")
  ) { (apiKey, port) =>
    Config(
      apiKey = apiKey,
      timeout = 10 seconds,
      port = port getOrElse 4000
    )
  }
```

[refined]: https://github.com/fthomas/refined
