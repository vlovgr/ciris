---
layout: docs
title: "Supporting New Sources"
permalink: /docs/supporting-new-sources
---

```tut:invisible
val (tempFile, tempFileName) = {
  val file = java.io.File.createTempFile("temp-", ".properties")
  file.deleteOnExit()

  val props = new java.util.Properties
  props.put("port", "9000")

  val stream = new java.io.FileOutputStream(file)
  try {
    props.store(stream, "")
  } finally {
    stream.close()
  }

  (file, file.getPath.toString)
}
```

# Supporting New Sources
Ciris already has [support](/docs/supported-sources) for common sources in the core module, while [external libraries](/#external-libraries) provide additional [configuration sources](/docs/sources). However, it's also easy to create your own configuration sources, and Ciris provides many helper functions in the [companion object][ConfigSourceCompanion] of [`ConfigSource`][ConfigSource] for that purpose. Following, we'll show how we can create a simple configuration source for reading [property files](https://en.wikipedia.org/wiki/.properties). While property files generally shouldn't be necessary when using _configurations as code_, they can definitely be supported with Ciris when necessary.

We start by defining a [`ConfigSource`][ConfigSource] for reading property files as `Map[String, String]`s. We could reuse the existing [`ConfigSource.File`][ConfigSourceFile] for reading files, but we would rather avoid having to create an intermediate `String` representation, so we'll instead define our own [`ConfigSource`][ConfigSource] for property files.

```tut:silent
import ciris.{ConfigKeyType, ConfigSource}
import ciris.api.Id
import java.io.{File, FileInputStream, InputStreamReader}
import java.nio.charset.Charset
import java.util.Properties
import scala.collection.JavaConverters._
import scala.util.Try

val propFileSource: ConfigSource[Id, (File, Charset), Map[String, String]] =
  ConfigSource.catchNonFatal(ConfigKeyType.File) {
    case (file, charset) =>
      val fis = new FileInputStream(file)
      try {
        val isr = new InputStreamReader(fis, charset)
        val props = new Properties
        props.load(isr)
        props.asScala.toMap
      } finally {
        val _ = Try(fis.close())
      }
  }
```

The [`ConfigSource`][ConfigSource] is using the existing [`ConfigKeyType.File`][ConfigKeyTypeFile], which uses `(File, Charset)` as the key type. The source also makes use of [`ConfigSource.catchNonFatal`][ConfigSourceCatchNonFatal] to catch any exceptions when reading the properties file. Finally, the properties are converted to a `Map`, and the `FileInputStream` is closed, ignoring any closing exceptions.

If you're creating a custom [`ConfigSource`][ConfigSource] by directly extending [`ConfigSource`][ConfigSource], rather than by using any of the helper functions in the companion object, you need to make sure you provide appropriate [`ConfigError`][ConfigError]s. In particular, you should return [`missingKey`][missingKey] if a key is not available from the source. This error is used by various functions, like [`orElse`][orElse] and [`orNone`][orNone], to fall back to other values if the previous keys have not been set.

The `PropFileKey` case class fully identifies a property file key. It is a combination of the `File`, `Charset`, and `String` key which we are retrieving. The `toString` function has been overridden to provide the `String` representation we would like in error messages. We'll also describe the name and type of the key by creating a [`ConfigKeyType`][ConfigKeyType].

```tut:silent
final case class PropFileKey(
  file: File,
  charset: Charset,
  key: String
) {
  override def toString: String =
    s"file = $file, charset = $charset, key = $key"
}

val propFileKeyType: ConfigKeyType[PropFileKey] =
  ConfigKeyType("property file key")
```

Since we would like to avoid having to read the file contents multiple times when reading more than one key, we define a helper class `PropFileAt`, which partially applies `PropFileKey` on `File` and `Charset`, reading the file once for multiple keys. The class defines an `apply` function for reading and decoding keys, similar to [`env`][env], [`prop`][prop], and [`file`][file] included in the core module.

```tut:silent
import ciris.{ConfigEntry, ConfigError, ConfigDecoder}

final class PropFileAt(file: File, charset: Charset) {
  private val propFile: Either[ConfigError, Map[String, String]] =
    propFileSource
      .read((file, charset))
      .value

  private def propFileKey(key: String): PropFileKey =
    PropFileKey(file, charset, key)

  private def propFileAt(key: String): Either[ConfigError, String] =
    propFile.flatMap { props =>
      props.get(key).toRight {
        ConfigError.missingKey(
          propFileKey(key),
          propFileKeyType
        )
      }
    }

  def apply[Value](key: String)(
    implicit decoder: ConfigDecoder[String, Value]
  ): ConfigEntry[Id, PropFileKey, String, Value] = {
    ConfigEntry(
      propFileKey(key),
      propFileKeyType,
      propFileAt(key)
    ).decodeValue[Value]
  }

  override def toString: String =
    s"PropFileAt($file, $charset)"
}
```

Finally, we define `propFileAt` as a convenience function for creating instances of `PropFileAt`.

```tut:silent
def propFileAt(
  name: String,
  charset: Charset = Charset.defaultCharset
): PropFileAt = {
  new PropFileAt(new File(name), charset)
}
```

We can then use the newly defined property file source as follows.

```tut:book
import eu.timepit.refined.types.net.UserPortNumber
import ciris.refined._

val propFile = propFileAt(tempFileName)

propFile[UserPortNumber]("port")
```

## Suspending Effects
Since reading the property file contents isn't _pure_, we could make use of [effect types](/docs/basics#suspending-effects) to suspend the reading of the file. We start by extracting the reading of the property file to outside of `PropFileAt`, so it doesn't have to deal with effects explicitly. `PropFileAt` now simply accepts the property file as an argument with context `F`. We require that there is a [`Monad`][Monad] instance available for `F`, since the [`ConfigDecoder`][ConfigDecoder] requires it for [`decodeValue`][decodeValue].

```tut:silent
import ciris.api.Monad
import ciris.api.syntax._

final class PropFileAt[F[_]: Monad](
  file: File,
  charset: Charset,
  propFile: F[Either[ConfigError, Map[String, String]]]
) {
  private def propFileKey(key: String): PropFileKey =
    PropFileKey(file, charset, key)

  private def propFileAt(key: String): F[Either[ConfigError, String]] =
    propFile.map { errorOrProps =>
      errorOrProps.flatMap { props =>
        props.get(key).toRight {
          ConfigError.missingKey(
            propFileKey(key),
            propFileKeyType
          )
        }
      }
    }

  def apply[Value](key: String)(
    implicit decoder: ConfigDecoder[String, Value]
  ): ConfigEntry[F, PropFileKey, String, Value] = {
    ConfigEntry
      .applyF(
        propFileKey(key),
        propFileKeyType,
        propFileAt(key)
      )
      .decodeValue[Value]
  }

  override def toString: String =
    s"PropFileAt($file, $charset, $propFile)"
}
```

With the property file reading extracted, we can now define `propFileAtF`, which suspends the reading of the property file into context `F`. We would also like that the file is not read more than once, so we also need to memoize the result. The [cats-effect](/docs/cats-effect-module) module provides a [`suspendMemoizeF`][suspendMemoizeF] function on [`ConfigSource`][ConfigSource] with a syntax import, which creates a [`ConfigSource`][ConfigSource] with both suspended reading and memoized results. The function works on any context `F` for which there is a [`LiftIO`][LiftIO] instance defined.

```tut:silent
import cats.effect.LiftIO
import ciris.cats.effect.syntax._

def propFileAtF[F[_]: Monad: LiftIO](
  name: String,
  charset: Charset = Charset.defaultCharset
): PropFileAt[F] = {
  val file = new File(name)

  val propFile =
    propFileSource
      .suspendMemoizeF[F]
      .read((file, charset))
      .value

  new PropFileAt(file, charset, propFile)
}
```

The `propFileAt` function can also be changed to use the new `PropFileAt` class.

```tut:silent
def propFileAt(
  name: String,
  charset: Charset = Charset.defaultCharset
): PropFileAt[Id] = {
  val file = new File(name)

  val propFile =
    propFileSource
      .read((file, charset))
      .value

  new PropFileAt(file, charset, propFile)
}
```

We can then use `propFileAtF` to read property file keys as follows.

```tut:book
import cats.effect.IO
import ciris.cats.effect._

val propFileF = propFileAtF[IO](tempFileName)

propFileF[UserPortNumber]("port")
```

[decodeValue]: /api/ciris/ConfigEntry.html#decodeValue[A](implicitdecoder:ciris.ConfigDecoder[V,A],implicitmonad:ciris.api.Monad[F]):ciris.ConfigEntry[F,K,S,A]
[ConfigDecoder]: /api/ciris/ConfigDecoder.html
[ConfigError]: /api/ciris/ConfigError.html
[missingKey]: /api/ciris/ConfigError$.html#missingKey[K](key:K,keyType:ciris.ConfigKeyType[K]):ciris.ConfigError
[orElse]: /api/ciris/ConfigValue.html#orElse(that:=>ciris.ConfigValue[F,V])(implicitm:ciris.api.Monad[F]):ciris.ConfigValue[F,V]
[orNone]: /api/ciris/ConfigValue.html#orNone:ciris.ConfigValue[F,Option[V]]
[Monad]: /api/ciris/api/Monad.html
[env]: /api/ciris/index.html#env[Value](key:String)(implicitdecoder:ciris.ConfigDecoder[String,Value]):ciris.ConfigEntry[ciris.api.Id,String,String,Value]
[prop]: /api/ciris/index.html#prop[Value](key:String)(implicitdecoder:ciris.ConfigDecoder[String,Value]):ciris.ConfigEntry[ciris.api.Id,String,String,Value]
[file]: /api/ciris/index.html#file[Value](file:java.io.File,modifyFileContents:String=>String,charset:java.nio.charset.Charset)(implicitdecoder:ciris.ConfigDecoder[String,Value]):ciris.ConfigEntry[ciris.api.Id,(java.io.File,java.nio.charset.Charset),String,Value]
[ConfigKeyType]: /api/ciris/ConfigKeyType.html
[ConfigKeyTypeFile]: /api/ciris/ConfigKeyType$.html#File:ciris.ConfigKeyType[(java.io.File,java.nio.charset.Charset)]
[ConfigSource]: /api/ciris/ConfigSource.html
[Sync]: /api/ciris/api/Sync.html
[ConfigSourceFile]: /api/ciris/ConfigSource$.html#File
[ConfigSourceCompanion]: /api/ciris/ConfigSource$.html
[ConfigSourceCatchNonFatal]: /api/ciris/ConfigSource$.html#catchNonFatal[K,V](keyType:ciris.ConfigKeyType[K])(read:K=>V):ciris.ConfigSource[ciris.api.Id,K,V]
[suspendF]: /api/ciris/ConfigSource.html#suspendF[G[_]](implicitevidence$1:ciris.api.Sync[G],implicitf:F~>G):ciris.ConfigSource[G,K,V]
[LiftIO]: https://github.com/typelevel/cats-effect/blob/master/core/shared/src/main/scala/cats/effect/LiftIO.scala
[suspendMemoizeF]: /api/ciris/cats/effect/syntax$$CatsEffectConfigSourceIdSyntax.html#suspendMemoizeF[F[_]](implicitevidence$1:ciris.api.Apply[F],implicitevidence$2:cats.effect.LiftIO[F]):ciris.ConfigSource[F,K,V]
