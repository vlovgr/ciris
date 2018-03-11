---
layout: docs
title: "Configuration Decoders"
position: 4
permalink: /docs/decoders
---

# Configuration Decoders
Configuration decoders are represented in Ciris with [`ConfigDecoder`][ConfigDecoder]s and provide the ability to decode the value of a [`ConfigEntry`][ConfigEntry] to a different type, while handling errors. [`ConfigDecoder`][ConfigDecoder]s have access to the whole [`ConfigEntry`][ConfigEntry] to be able to provide sensible error messages, even though only the value is being decoded. [`ConfigDecoder`][ConfigDecoder]s generally require that a [`Monad`][Monad] instance is available for the context `F` of the [`ConfigEntry`][ConfigEntry], in order to support most decoders. Following is a simplified definition of [`ConfigDecoder`][ConfigDecoder] for reference.

```tut:silent
import ciris.{ConfigEntry, ConfigError}
import ciris.api.Monad

{
  abstract class ConfigDecoder[A, B] {
    def decode[F[_]: Monad, K, S](
      entry: ConfigEntry[F, K, S, A]
    ): F[Either[ConfigError, B]]
  }
}
```

Most [`ConfigDecoder`][ConfigDecoder]s provided by Ciris support decoding from `String`, but there is nothing preventing you from creating decoders for other types. [`ConfigDecoder`][ConfigDecoder] has several combinators helping you create new decoders from existing ones, and the [companion object][ConfigDecoderCompanion] of [`ConfigDecoder`][ConfigDecoder] provides several functions for helping you create new decoders. The [supporting new types](/docs/supporting-new-types) section provides more information on how to create decoders for new types. For currently support types, instead refer to the [current supported types](/docs/supported-types) section.

[`ConfigDecoder`][ConfigDecoder]s are most often used indirectly via [`decodeValue`][decodeValue] on [`ConfigEntry`][ConfigEntry]. For example, if we take a look at the [`env`][env] function for reading and decoding environment variables, we'll see that it simply reads the environment variable from [`ConfigSource.Environment`][ConfigSourceEnvironment] and then decodes the value with [`decodeValue`][decodeValue].

```tut:silent
import ciris.{ConfigDecoder, ConfigSource}
import ciris.api.Id

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

[ConfigDecoderCompanion]: /api/ciris/ConfigDecoder$.html
[ConfigDecoder]: /api/ciris/ConfigDecoder.html
[ConfigEntry]: /api/ciris/ConfigEntry.html
[Monad]: /api/ciris/api/Monad.html
[decodeValue]: /api/ciris/ConfigEntry.html#decodeValue[A](implicitdecoder:ciris.ConfigDecoder[V,A],implicitmonad:ciris.api.Monad[F]):ciris.ConfigEntry[F,K,S,A]
[env]: /api/ciris/index.html#env[Value](key:String)(implicitdecoder:ciris.ConfigDecoder[String,Value]):ciris.ConfigEntry[ciris.api.Id,String,String,Value]
[ConfigSourceEnvironment]: /api/ciris/ConfigSource$.html#Environment
