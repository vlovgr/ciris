---
layout: docs
title: "Configuration Sources"
position: 3
permalink: /docs/sources
---

# Configuration Sources
Configuration sources are represented in Ciris with [`ConfigSource`][ConfigSource]s, and are essentially functions which can retrieve values of type `V` in context `F`, for keys of type `K`, and which handle errors. More specifically, [`ConfigSource`][ConfigSource]s require a [`ConfigKeyType`][ConfigKeyType], which is a description of the type of keys the source supports. [`ConfigSource`][ConfigSource]s can then be expressed as `K => ConfigEntry[F, K, V, V]`, and a simplified definition of [`ConfigSource`][ConfigSource] is provided below.

```tut:silent
import ciris.{ConfigEntry, ConfigKeyType}

{
  abstract class ConfigSource[F[_], K, V](val keyType: ConfigKeyType[K]) {
    def read(key: K): ConfigEntry[F, K, V, V]
  }
}
```

[`ConfigKeyType`][ConfigKeyType] includes the name and type of the key. Ciris includes [`ConfigKeyType`][ConfigKeyType]s for keys used by sources supported in the core module, which include the following. You can easily create your own [`ConfigKeyType`][ConfigKeyType]s by specifying the name and type of the key.

```tut:book
ConfigKeyType.Argument

ConfigKeyType.Environment

ConfigKeyType.File

ConfigKeyType.Property

ConfigKeyType[String]("custom key")
```

[`ConfigEntry`][ConfigEntry]s include the key which was retrieved, the [`ConfigKeyType`][ConfigKeyType], the unmodified source value retrieved from the [`ConfigSource`][ConfigSource], and the source value with additional transformations applied -- for example, the result of attempting to decode the source value to type `Int`. A simplified definition of [`ConfigEntry`][ConfigEntry] is provided below.

```tut:silent
import ciris.{ConfigError, ConfigValue}
import ciris.api.Apply

{
  final class ConfigEntry[F[_]: Apply, K, S, V] private (
    val key: K,
    val keyType: ConfigKeyType[K],
    val sourceValue: F[Either[ConfigError, S]],
    val value: F[Either[ConfigError, V]]
  ) extends ConfigValue[F, V]
}
```

Both the source value and the value might not be available, and in such cases, the [`ConfigError`][ConfigError] will provide more details. [`ConfigEntry`][ConfigEntry]s require that the context `F` has an [`Apply`][Apply] instance defined, to be able to transform the value, and to combine multiple values. Ciris provides convenience functions, like [`env`][env], [`prop`][prop], and [`file`][file] (and [`envF`][envF], [`propF`][propF], and [`fileSync`][fileSync] for suspending effects), which all make use of [`ConfigSource`][ConfigSource]s, and return [`ConfigEntry`][ConfigEntry]s. These convenience functions also attempts to decode the value with a [configuration decoder](/docs/decoders), represented with [`ConfigDecoder`][ConfigDecoder]s. For example, here is how you could define `env` for reading environment variables.

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

Ciris provides many convenience functions for creating [`ConfigSource`][ConfigSource]s in the [companion object][ConfigSourceCompanion] of [`ConfigSource`][ConfigSource]. For more information on how to create additional [`ConfigSource`][ConfigSource]s, please refer to the [supporting new sources](/docs/supporting-new-sources) section.

## Source Transformations
Configuration sources can be transformed in different ways. Most notably, we can take an existing [`ConfigSource`][ConfigSource] and suspend the reading of values into context `G`, by using [`suspendF`][suspendF], provided that there is a [`Sync`][Sync] instance defined for `G`, and a natural transformation `F ~> G`. This effectively means that we can take a synchronous _impure_ configuration source, and make it _pure_ with [`suspendF`][suspendF].

For example, system properties are mutable, and reading them is not _pure_ by definition -- since we can get different results for the same arguments, if the properties are modified in-between the function invocations. We can create a _pure_ version of [`ConfigSource.Properties`][ConfigSourceProperties] by making use of [`suspendF`][suspendF] and, for example, `IO` from [cats-effect][cats-effect].

```tut:book
import cats.effect.IO
import ciris.cats.effect._

ConfigSource.Properties.suspendF[IO]
```

The natural transformation `Id ~> F` (where `F` is `IO` here) exists as long as we have an `Applicative[F]` defined, which we have via the `Sync[F]` instance. If we take a look at [`propF`][propF], which is the _pure_ version of [`prop`][prop], we'll see that it's also defined in a very similar fashion with [`suspendF`][suspendF].

```tut:silent
import ciris.api.Sync

{
  def propF[F[_]: Sync, Value](key: String)(
    implicit decoder: ConfigDecoder[String, Value]
  ): ConfigEntry[F, String, String, Value] = {
    ConfigSource.Properties
      .suspendF[F]
      .read(key)
      .decodeValue[Value]
  }
}
```

If we simply want to transform the context of a [`ConfigSource`][ConfigSource], we can instead use [`transformF`][transformF], which expects a natural transformation `F ~> G`, and that there is an [`Apply`][Apply] instance for `G`. The [`transformF`][transformF] function which is available on [`ConfigSource`][ConfigSource] makes use of the similar [`transformF`][transformFentry] on [`ConfigEntry`][ConfigEntry].

```tut:book
ConfigSource.Environment.transformF[IO]
```

Sometimes it's necessary to combine suspended reading and memoization in a context `F`, and the [cats-effect](/docs/cats-effect-module) module provides the [`suspendMemoizeF`][suspendMemoizeF] function on [`ConfigSource`][ConfigSource] for this purpose. The function supports any `F` for which a [`LiftIO`][LiftIO] instance is available. The [supporting new sources](/docs/supporting-new-sources#suspending-effects) section provides an example of how the function can be used.

[cats-effect]: https://github.com/typelevel/cats-effect
[ConfigSource]: /api/ciris/ConfigSource.html
[ConfigDecoder]: /api/ciris/ConfigDecoder.html
[ConfigKeyType]: /api/ciris/ConfigKeyType.html
[ConfigEntry]: /api/ciris/ConfigEntry.html
[ConfigError]: /api/ciris/ConfigError.html
[Apply]: /api/ciris/api/Apply.html
[Sync]: /api/ciris/api/Sync.html
[ConfigSourceCompanion]: /api/ciris/ConfigSource$.html
[transformF]: /api/ciris/ConfigSource.html#transformF[G[_]](implicitevidence$2:ciris.api.Apply[G],implicitf:F~>G):ciris.ConfigSource[G,K,V]
[transformFentry]: /api/ciris/ConfigEntry.html#transformF[G[_]](implicitevidence$2:ciris.api.Apply[G],implicitf:F~>G):ciris.ConfigEntry[G,K,S,V]
[ConfigSourceProperties]: /api/ciris/ConfigSource$.html#Properties
[suspendF]: /api/ciris/ConfigSource.html#suspendF[G[_]](implicitevidence$1:ciris.api.Sync[G],implicitf:F~>G):ciris.ConfigSource[G,K,V]
[env]: /api/ciris/index.html#env[Value](key:String)(implicitdecoder:ciris.ConfigDecoder[String,Value]):ciris.ConfigEntry[ciris.api.Id,String,String,Value]
[prop]: /api/ciris/index.html#prop[Value](key:String)(implicitdecoder:ciris.ConfigDecoder[String,Value]):ciris.ConfigEntry[ciris.api.Id,String,String,Value]
[file]: /api/ciris/index.html#file[Value](file:java.io.File,modifyFileContents:String=>String,charset:java.nio.charset.Charset)(implicitdecoder:ciris.ConfigDecoder[String,Value]):ciris.ConfigEntry[ciris.api.Id,(java.io.File,java.nio.charset.Charset),String,Value]
[envF]: /api/ciris/index.html#envF[F[_],Value](key:String)(implicitevidence$1:ciris.api.Applicative[F],implicitdecoder:ciris.ConfigDecoder[String,Value]):ciris.ConfigEntry[F,String,String,Value]
[fileSync]: /api/ciris/index.html#fileSync[F[_],Value](file:java.io.File,modifyFileContents:String=>String,charset:java.nio.charset.Charset)(implicitevidence$1:ciris.api.Sync[F],implicitdecoder:ciris.ConfigDecoder[String,Value]):ciris.ConfigEntry[F,(java.io.File,java.nio.charset.Charset),String,Value]
[propF]: /api/ciris/index.html#propF[F[_],Value](key:String)(implicitevidence$2:ciris.api.Sync[F],implicitdecoder:ciris.ConfigDecoder[String,Value]):ciris.ConfigEntry[F,String,String,Value]
[suspendMemoizeF]: /api/ciris/cats/effect/syntax$$CatsEffectConfigSourceIdSyntax.html#suspendMemoizeF[F[_]](implicitevidence$1:ciris.api.Apply[F],implicitevidence$2:cats.effect.LiftIO[F]):ciris.ConfigSource[F,K,V]
[LiftIO]: https://github.com/typelevel/cats-effect/blob/master/core/shared/src/main/scala/cats/effect/LiftIO.scala
