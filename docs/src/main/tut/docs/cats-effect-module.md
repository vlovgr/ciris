---
layout: docs
title: "Cats Effect Module"
permalink: /docs/cats-effect-module
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

# Cats Effect Module
The `ciris-cats-effect` module provides effect types and effect type class instances for contexts `F[_]` from [cats-effect][cats-effect]. Effect types are useful for explicitly modelling side-effects, including [suspending effects](/docs/basics#suspending-effects) for reading configuration values. Ciris provides functions [`envF`][envF], [`propF`][propF], and [`fileSync`][fileSync] and [`fileWithNameSync`][fileWithNameSync], which suspend the reading in a context `F[_]`, for which there is a [`Sync`][Sync] instance available.

```tut:book
import ciris.{propF, fileSync}
import ciris.cats.effect._
import cats.effect.IO

// Suspend reading of system property file.encoding as a String
propF[IO, String]("file.encoding")

// Suspend reading the file, trim the file contents, and convert to Int
fileSync[IO, Int](tempFile, _.trim)
```

If we have a [`Sync`][Sync] instance for `F[_]`, we can take an existing [`ConfigSource`][ConfigSource] and create a new [`ConfigSource`][ConfigSource] with suspended reading using the [`suspendF`][suspendF] function. If we also need to memoize the results, there is a [`suspendMemoizeF`][suspendMemoizeF] function for that purpose.

```tut:book
import ciris.ConfigSource
import ciris.cats.effect.syntax._

ConfigSource.Properties.suspendF[IO]

ConfigSource.Properties.suspendMemoizeF[IO]
```

The [supporting new sources](/docs/supporting-new-sources#suspending-effects) section contains an example of how these functions can be used.

[cats-effect]: https://github.com/typelevel/cats-effect
[envF]: /api/ciris/index.html#envF[F[_],Value](key:String)(implicitevidence$1:ciris.api.Applicative[F],implicitdecoder:ciris.ConfigDecoder[String,Value]):ciris.ConfigEntry[F,String,String,Value]
[fileSync]: /api/ciris/index.html#fileSync[F[_],Value](file:java.io.File,modifyFileContents:String=>String,charset:java.nio.charset.Charset)(implicitevidence$1:ciris.api.Sync[F],implicitdecoder:ciris.ConfigDecoder[String,Value]):ciris.ConfigEntry[F,(java.io.File,java.nio.charset.Charset),String,Value]
[fileWithNameSync]: /api/ciris/index.html#fileWithNameSync[F[_],Value](name:String,modifyFileContents:String=>String,charset:java.nio.charset.Charset)(implicitevidence$2:ciris.api.Sync[F],implicitdecoder:ciris.ConfigDecoder[String,Value]):ciris.ConfigEntry[F,(java.io.File,java.nio.charset.Charset),String,Value]
[propF]: /api/ciris/index.html#propF[F[_],Value](key:String)(implicitevidence$2:ciris.api.Sync[F],implicitdecoder:ciris.ConfigDecoder[String,Value]):ciris.ConfigEntry[F,String,String,Value]
[suspendF]: /api/ciris/ConfigSource.html#suspendF[G[_]](implicitevidence$1:ciris.api.Sync[G],implicitf:F~>G):ciris.ConfigSource[G,K,V]
[suspendMemoizeF]: /api/ciris/cats/effect/syntax$$CatsEffectConfigSourceIdSyntax.html#suspendMemoizeF[F[_]](implicitevidence$1:ciris.api.Apply[F],implicitevidence$2:cats.effect.LiftIO[F]):ciris.ConfigSource[F,K,V]
[Sync]: /api/ciris/api/Sync.html
[ConfigSource]: /api/ciris/ConfigSource.html
