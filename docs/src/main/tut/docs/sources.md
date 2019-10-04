---
layout: docs
title: "Configuration Sources"
position: 3
permalink: /docs/sources
---

# Configuration Sources

Configuration sources are represented in Ciris with [`ConfigSource`][configsource]s, and are essentially functions which can retrieve values of type `V` in context `F`, for keys of type `K`, and which handle errors. More specifically, [`ConfigSource`][configsource]s require a [`ConfigKeyType`][configkeytype], which is a description of the type of keys the source supports. [`ConfigSource`][configsource]s can then be expressed as `K => ConfigEntry[F, K, V, V]`, and a simplified definition of [`ConfigSource`][configsource] is provided below.

```tut:silent
import ciris.{ConfigEntry, ConfigKeyType}

{
  abstract class ConfigSource[F[_], K, V](val keyType: ConfigKeyType[K]) {
    def read(key: K): ConfigEntry[F, K, V, V]
  }
}
```

[`ConfigKeyType`][configkeytype] includes the name and type of the key. Ciris includes [`ConfigKeyType`][configkeytype]s for keys used by sources supported in the core module, which include the following. You can easily create your own [`ConfigKeyType`][configkeytype]s by specifying the name and type of the key.

```tut:book
ConfigKeyType.Argument

ConfigKeyType.Environment

ConfigKeyType.File

ConfigKeyType.Property

ConfigKeyType[String]("custom key")
```

[`ConfigEntry`][configentry]s include the key which was retrieved, the [`ConfigKeyType`][configkeytype], the unmodified source value retrieved from the [`ConfigSource`][configsource], and the source value with additional transformations applied -- for example, the result of attempting to decode the source value to type `Int`. A simplified definition of [`ConfigEntry`][configentry] is provided below.

```tut:silent
import cats.{Apply, Id}
import ciris.{ConfigError, ConfigValue}

{
  final class ConfigEntry[F[_]: Apply, K, S, V] private (
    val key: K,
    val keyType: ConfigKeyType[K],
    val sourceValue: F[Either[ConfigError, S]],
    val value: F[Either[ConfigError, V]]
  ) extends ConfigValue[F, V]
}
```

Both the source value and the value might not be available, and in such cases, the [`ConfigError`][configerror] will provide more details. [`ConfigEntry`][configentry]s require that the context `F` has an [`Apply`][apply] instance defined, to be able to transform the value, and to combine multiple values. Ciris provides convenience functions, like [`env`][env], [`prop`][prop], and [`file`][file] (and [`envF`][envf], [`propF`][propf], and [`fileSync`][filesync] for suspending effects), which all make use of [`ConfigSource`][configsource]s, and return [`ConfigEntry`][configentry]s. These convenience functions also attempts to decode the value with a [configuration decoder](/docs/decoders), represented with [`ConfigDecoder`][configdecoder]s. For example, here is how you could define `env` for reading environment variables.

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

Ciris provides many convenience functions for creating [`ConfigSource`][configsource]s in the [companion object][configsourcecompanion] of [`ConfigSource`][configsource]. For more information on how to create additional [`ConfigSource`][configsource]s, please refer to the [supporting new sources](/docs/supporting-new-sources) section.

[cats-effect]: https://github.com/typelevel/cats-effect
[configsource]: /api/ciris/ConfigSource.html
[configdecoder]: /api/ciris/ConfigDecoder.html
[configkeytype]: /api/ciris/ConfigKeyType.html
[configentry]: /api/ciris/ConfigEntry.html
[configerror]: /api/ciris/ConfigError.html
[apply]: /api/ciris/api/Apply.html
[sync]: /api/ciris/api/Sync.html
[configsourcecompanion]: /api/ciris/ConfigSource$.html

[transformF]: /api/ciris/ConfigSource.html#transformF[G[\_]](implicitevidence$2:ciris.api.Apply[G],implicitf:F~>G):ciris.ConfigSource[G,K,V][transformfentry]: /api/ciris/ConfigEntry.html#transformF[G[\_]](implicitevidence$2:ciris.api.Apply[G],implicitf:F~>G):ciris.ConfigEntry[G,K,S,V][configsourceproperties]: /api/ciris/ConfigSource$.html#Properties
[suspendF]: /api/ciris/ConfigSource.html#suspendF[G[_]](implicitevidence$1:ciris.api.Sync[G],implicitf:F~>G):ciris.ConfigSource[G,K,V][env]: /api/ciris/index.html#env[Value](key:String)(implicitdecoder:ciris.ConfigDecoder[String,Value]):ciris.ConfigEntry[ciris.api.Id,String,String,Value][prop]: /api/ciris/index.html#prop[Value](key:String)(implicitdecoder:ciris.ConfigDecoder[String,Value]):ciris.ConfigEntry[ciris.api.Id,String,String,Value][file]: /api/ciris/index.html#file[Value](file:java.io.File,modifyFileContents:String=>String,charset:java.nio.charset.Charset)(implicitdecoder:ciris.ConfigDecoder[String,Value]):ciris.ConfigEntry[ciris.api.Id,(java.io.File,java.nio.charset.Charset),String,Value][envf]: /api/ciris/index.html#envF[F[\_],Value](key:String)(implicitevidence$1:ciris.api.Applicative[F],implicitdecoder:ciris.ConfigDecoder[String,Value]):ciris.ConfigEntry[F,String,String,Value]
[fileSync]: /api/ciris/index.html#fileSync[F[_],Value](file:java.io.File,modifyFileContents:String=>String,charset:java.nio.charset.Charset)(implicitevidence$1:ciris.api.Sync[F],implicitdecoder:ciris.ConfigDecoder[String,Value]):ciris.ConfigEntry[F,(java.io.File,java.nio.charset.Charset),String,Value][propf]: /api/ciris/index.html#propF[F[\_],Value](key:String)(implicitevidence$2:ciris.api.Sync[F],implicitdecoder:ciris.ConfigDecoder[String,Value]):ciris.ConfigEntry[F,String,String,Value]
[suspendMemoizeF]: /api/ciris/cats/effect/syntax$\$CatsEffectConfigSourceIdSyntax.html#suspendMemoizeF[F[\_]](implicitF:cats.effect.Concurrent[F]):ciris.ConfigSource[[v]F[F[v]],K,V][concurrent]: https://typelevel.org/cats-effect/typeclasses/concurrent.html
