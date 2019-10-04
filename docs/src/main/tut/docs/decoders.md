---
layout: docs
title: "Configuration Decoders"
position: 4
permalink: /docs/decoders
---

# Configuration Decoders

Configuration decoders are represented in Ciris with [`ConfigDecoder`][configdecoder]s and provide the ability to decode the value of a [`ConfigEntry`][configentry] to a different type, while handling errors. [`ConfigDecoder`][configdecoder]s have access to the whole [`ConfigEntry`][configentry] to be able to provide sensible error messages, even though only the value is being decoded. [`ConfigDecoder`][configdecoder]s generally require that a [`Monad`][monad] instance is available for the context `F` of the [`ConfigEntry`][configentry], in order to support most decoders. Following is a simplified definition of [`ConfigDecoder`][configdecoder] for reference.

```tut:silent
import cats.Monad
import ciris.{ConfigEntry, ConfigError}

{
  abstract class ConfigDecoder[A, B] {
    def decode[F[_]: Monad, K, S](
      entry: ConfigEntry[F, K, S, A]
    ): F[Either[ConfigError, B]]
  }
}
```

Most [`ConfigDecoder`][configdecoder]s provided by Ciris support decoding from `String`, but there is nothing preventing you from creating decoders for other types. [`ConfigDecoder`][configdecoder] has several combinators helping you create new decoders from existing ones, and the [companion object][configdecodercompanion] of [`ConfigDecoder`][configdecoder] provides several functions for helping you create new decoders. The [supporting new types](/docs/supporting-new-types) section provides more information on how to create decoders for new types. For currently support types, instead refer to the [current supported types](/docs/supported-types) section.

[`ConfigDecoder`][configdecoder]s are most often used indirectly via [`decodeValue`][decodevalue] on [`ConfigEntry`][configentry]. For example, if we take a look at the [`env`][env] function for reading and decoding environment variables, we'll see that it simply reads the environment variable from [`ConfigSource.Environment`][configsourceenvironment] and then decodes the value with [`decodeValue`][decodevalue].

```tut:silent
import cats.Id
import ciris.{ConfigDecoder, ConfigSource}

{
  def env[Value](key: String)(
    implicit decoder: ConfigDecoder[String, Value]
  ): ConfigEntry[Id, String, String, Value] = {
    ConfigSource.Environment
      .read(key)
      .decodeValue[Value]
  }
}
```

[configdecodercompanion]: /api/ciris/ConfigDecoder$.html
[configdecoder]: /api/ciris/ConfigDecoder.html
[configentry]: /api/ciris/ConfigEntry.html
[monad]: /api/ciris/api/Monad.html

[decodeValue]: /api/ciris/ConfigEntry.html#decodeValue[A](implicitdecoder:ciris.ConfigDecoder[V,A],implicitmonad:ciris.api.Monad[F]):ciris.ConfigEntry[F,K,S,A][env]: /api/ciris/index.html#env[Value](key:String)(implicitdecoder:ciris.ConfigDecoder[String,Value]):ciris.ConfigEntry[ciris.api.Id,String,String,Value][configsourceenvironment]: /api/ciris/ConfigSource\$.html#Environment
