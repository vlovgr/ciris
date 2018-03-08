---
layout: docs
title: "Cats Module"
permalink: /docs/cats-module
---

# Cats Module
The `ciris-cats` module provides type class instances for contexts `F[_]` from [cats][cats] when used together with Ciris. [`Id`][Id] is the default context, and is used when no explicit context is desired. The `ciris-cats` module is useful in cases when `F` is not [`Id`][Id], but for example `Future`, like in the following example. Note that Ciris does not provide type class instances for any other contexts than [`Id`][Id] and relies on instances for other contexts from libraries like [cats][cats].

Let's define an example [`ConfigSource`][ConfigSource] which reads `Future` values.

```tut:silent
import cats.implicits._
import ciris._
import ciris.cats._
import ciris.ConfigError.right
import scala.concurrent._
import scala.concurrent.duration._

implicit val executionContext: ExecutionContext =
  ExecutionContext.Implicits.global

val source: ConfigSource[Future, String, String] = {
  val keyType = ConfigKeyType[String]("example key")
  ConfigSource.applyF(keyType) { key: String =>
    Future.successful(right(key.length.toString))
  }
}
```

We can then load, decode, and combine values into a configuration as follows.

```tut:book
final case class Config(first: Int, second: Int)

val futureConfig =
  loadConfig(
    source.read("firstKey").decodeValue[Int],
    source.read("secondKey").decodeValue[Int]
  )(Config)

val config = Await.result(futureConfig, 1.second)
```

The `ciris-cats` module also provides [`Show`][Show] type class instances for [logging configurations](/docs/logging#logging-improvements).

[ConfigSource]: /api/ciris/ConfigSource.html
[Show]: https://typelevel.org/cats/typeclasses/show.html
[cats]: https://github.com/typelevel/cats
[Id]: /api/ciris/api/index.html#Id[A]=A
