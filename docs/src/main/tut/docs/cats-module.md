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

## Compositional loading of configuration using `parMapN`

The `ciris-cats` module provides a [`Semigroup`][Semigroup] instance for
`ConfigErrors`, because two lists of errors can be combined by concatenating
them.

This means that you can load small parts of your configuration using
`loadConfig` and then compose the results together using `parMapN`.

If any of the smaller configs failed to load, the final result will be a
`ConfigErrors` containing all the error messages.

Let's look at an example.

Our app needs to talk to a database and an external API, so its configuration
includes DB credentials and an API key. We have separate case classes for the
different types of configuration.

```tut:silent
case class DbConfig(user: String, password: String)
case class ApiClientConfig(apiKey: String)
case class AppConfig(db: DbConfig, api: ApiClientConfig)
```

We load each of the configurations using `loadConfig`:

```tut:invisible
sys.props.put("db.user", "myapp")
sys.props.put("db.password", "secretsauce")
sys.props.put("api.key", "abc123")
```

```tut:book
val dbConfig: Either[ConfigErrors, DbConfig] =
  loadConfig(
    prop[String]("db.user"),
    prop[String]("db.password")
  )(DbConfig.apply)

val apiConfig: Either[ConfigErrors, ApiClientConfig] =
  loadConfig(prop[String]("api.key"))(ApiClientConfig.apply)
```

We can then compose the results into an `Either[ConfigErrors, AppConfig]`:

```tut:book
import _root_.cats.instances.parallel._
import _root_.cats.syntax.parallel._
import ciris.cats._

(dbConfig, apiConfig).parMapN(AppConfig.apply)
```

[ConfigSource]: /api/ciris/ConfigSource.html
[Show]: https://typelevel.org/cats/typeclasses/show.html
[Semigroup]: https://typelevel.org/cats/typeclasses/semigroup.html
[cats]: https://github.com/typelevel/cats
[Id]: /api/ciris/api/index.html#Id[A]=A
