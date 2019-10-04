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

Ciris already has [support](/docs/supported-sources) for common sources in the core module, while [external libraries](/#external-libraries) provide additional [configuration sources](/docs/sources). However, it's also easy to create your own configuration sources, and Ciris provides many helper functions in the [companion object][configsourcecompanion] of [`ConfigSource`][configsource] for that purpose. Following, we'll show how we can create a simple configuration source for reading [property files](https://en.wikipedia.org/wiki/.properties). While property files generally shouldn't be necessary when using _configurations as code_, they can definitely be supported with Ciris when necessary.

We start by defining a [`ConfigSource`][configsource] for reading property files as `Map[String, String]`s. We could reuse the existing [`ConfigSource.File`][configsourcefile] for reading files, but we would rather avoid having to create an intermediate `String` representation, so we'll instead define our own [`ConfigSource`][configsource] for property files.

```tut:silent
import cats.Id
import ciris.{ConfigKeyType, ConfigSource}
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

The [`ConfigSource`][configsource] is using the existing [`ConfigKeyType.File`][configkeytypefile], which uses `(File, Charset)` as the key type. The source also makes use of [`ConfigSource.catchNonFatal`][configsourcecatchnonfatal] to catch any exceptions when reading the properties file. Finally, the properties are converted to a `Map`, and the `FileInputStream` is closed, ignoring any closing exceptions.

If you're creating a custom [`ConfigSource`][configsource] by directly extending [`ConfigSource`][configsource], rather than by using any of the helper functions in the companion object, you need to make sure you provide appropriate [`ConfigError`][configerror]s. In particular, you should return [`missingKey`][missingkey] if a key is not available from the source. This error is used by various functions, like [`orElse`][orelse] and [`orNone`][ornone], to fall back to other values if the previous keys have not been set.

The `PropFileKey` case class fully identifies a property file key. It is a combination of the `File`, `Charset`, and `String` key which we are retrieving. The `toString` function has been overridden to provide the `String` representation we would like in error messages. We'll also describe the name and type of the key by creating a [`ConfigKeyType`][configkeytype].

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

[decodeValue]: /api/ciris/ConfigEntry.html#decodeValue[A](implicitdecoder:ciris.ConfigDecoder[V,A],implicitmonad:ciris.api.Monad[F]):ciris.ConfigEntry[F,K,S,A][configdecoder]: /api/ciris/ConfigDecoder.html
[ConfigError]: /api/ciris/ConfigError.html
[missingKey]: /api/ciris/ConfigError$.html#missingKey[K](key:K,keyType:ciris.ConfigKeyType[K]):ciris.ConfigError
[orElse]: /api/ciris/ConfigValue.html#orElse(that:=>ciris.ConfigValue[F,V])(implicitm:ciris.api.Monad[F]):ciris.ConfigValue[F,V]
[orNone]: /api/ciris/ConfigValue.html#orNone:ciris.ConfigValue[F,Option[V]]
[Monad]: /api/ciris/api/Monad.html
[env]: /api/ciris/index.html#env[Value](key:String)(implicitdecoder:ciris.ConfigDecoder[String,Value]):ciris.ConfigEntry[ciris.api.Id,String,String,Value]
[prop]: /api/ciris/index.html#prop[Value](key:String)(implicitdecoder:ciris.ConfigDecoder[String,Value]):ciris.ConfigEntry[ciris.api.Id,String,String,Value]
[file]: /api/ciris/index.html#file[Value](file:java.io.File,modifyFileContents:String=>String,charset:java.nio.charset.Charset)(implicitdecoder:ciris.ConfigDecoder[String,Value]):ciris.ConfigEntry[ciris.api.Id,(java.io.File,java.nio.charset.Charset),String,Value]
[ConfigKeyType]: /api/ciris/ConfigKeyType.html
[ConfigKeyTypeFile]: /api/ciris/ConfigKeyType$.html#File:ciris.ConfigKeyType[(java.io.File,java.nio.charset.Charset)][configsource]: /api/ciris/ConfigSource.html
[Sync]: /api/ciris/api/Sync.html
[ConfigSourceFile]: /api/ciris/ConfigSource$.html#File
[ConfigSourceCompanion]: /api/ciris/ConfigSource$.html
[ConfigSourceCatchNonFatal]: /api/ciris/ConfigSource$.html#catchNonFatal[K,V](keyType:ciris.ConfigKeyType[K])(read:K=>V):ciris.ConfigSource[ciris.api.Id,K,V]
[suspendMemoizeF]: /api/ciris/cats/effect/syntax\$\$CatsEffectConfigSourceIdSyntax.html#suspendMemoizeF[F[\_]](implicitF:cats.effect.Concurrent[F]):ciris.ConfigSource[[v]F[F[v]],K,V]
