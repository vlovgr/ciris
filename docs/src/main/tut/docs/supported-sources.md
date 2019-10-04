---
layout: docs
title: "Current Supported Sources"
permalink: /docs/supported-sources
---

```tut:invisible
val args: Array[String] = Array("10")

val (tempFile, tempFileName) = {
  val file = java.io.File.createTempFile("temp-", ".tmp")
  file.deleteOnExit()

  val writer = new java.io.FileWriter(file)
  try {
    writer.write("1234 ")
  } finally {
    writer.close()
  }

  (file, file.getPath.toString)
}
```

# Current Supported Sources

The following sources are the currently supported [configuration sources](/docs/sources) in the core module. The core module only includes configuration sources which can be supported without any external dependencies. [External libraries](/#external-libraries), like [ciris-kubernetes](https://github.com/ovotech/ciris-kubernetes) and [ciris-aws-ssm](https://github.com/ovotech/ciris-aws-ssm), provide support for configuration sources which do not meet this requirement. You can also easily write your own configuration sources, refer to the [supporting new sources](/docs/supporting-new-sources) section for more information.

- _Command-line arguments_ are supported with [`arg`][arg] and [`argF`][argf] (for suspending reading on mutable `IndexedSeq`). If you're writing non-trivial command-line applications, you might want to take a look at dedicated full-featured command-line parsing libraries, like [decline](https://github.com/bkirwi/decline).

```tut:book
import cats.effect.IO
import ciris.{arg, argF}

// The arguments from main(Array[String]): Unit
args

arg[Int](args)(0)

argF[IO, Int](args)(0)
```

- _Environment variables_ are supported with the [`env`][env] and [`envF`][envf] (for lifting values into context `F`) functions. Since environment variables are immutable, both the [`env`][env] and [`envF`][envf] functions are pure and safe to use. Note that [`envF`][envf] is just making use of [`transformF`][transformf] on [`ConfigEntry`][configentry].

```tut:book
import ciris.{env, envF}

env[String]("LANG")

envF[IO, String]("LANG")
```

- _Files_ are supported with the [`file`][file] and [`fileWithName`][filewithname] (and [`fileSync`][filesync] and [`fileWithNameSync`][filewithnamesync] for suspending reading) functions, which read the file contents of a specified file, optionally transforming the file contents before trying to convert it to the specified type. Note that reading files with [`file`][file] or [`fileWithName`][filewithname] is not pure, so using [`fileSync`][filesync] or [`fileWithNameSync`][filewithnamesync] is generally recommended.

```tut:book
import ciris.{file, fileSync, fileWithName, fileWithNameSync}

file[Int](tempFile, _.trim)

fileSync[IO, Int](tempFile, _.trim)

fileWithName[Int](tempFileName, _.trim)

fileWithNameSync[IO, Int](tempFileName, _.trim)
```

- _System properties_ are supported with the [`prop`][prop] and [`propF`][propf] (for suspending reading) functions. Note that system properties are mutable, so using [`prop`][prop] is generally not pure if system properties are modified at runtime. If you want to be on the safe side, prefer to use [`propF`][propf] for reading system properties.

```tut:book
import ciris.{prop, propF}

prop[String]("file.encoding")

propF[IO, String]("file.encoding")
```

[arg]: /api/ciris/index.html#arg[Value](args:IndexedSeq[String])(index:Int)(implicitdecoder:ciris.ConfigDecoder[String,Value]):ciris.ConfigEntry[ciris.api.Id,Int,String,Value][argf]: /api/ciris/index.html#argF[F[\_],Value](args:IndexedSeq[String])(index:Int)(implicitevidence$3:ciris.api.Sync[F],implicitdecoder:ciris.ConfigDecoder[String,Value]):ciris.ConfigEntry[F,Int,String,Value]
[envF]: /api/ciris/index.html#envF[F[_],Value](key:String)(implicitevidence$1:ciris.api.Applicative[F],implicitdecoder:ciris.ConfigDecoder[String,Value]):ciris.ConfigEntry[F,String,String,Value][filesync]: /api/ciris/index.html#fileSync[F[\_],Value](file:java.io.File,modifyFileContents:String=>String,charset:java.nio.charset.Charset)(implicitevidence$1:ciris.api.Sync[F],implicitdecoder:ciris.ConfigDecoder[String,Value]):ciris.ConfigEntry[F,(java.io.File,java.nio.charset.Charset),String,Value]
[fileWithNameSync]: /api/ciris/index.html#fileWithNameSync[F[_],Value](name:String,modifyFileContents:String=>String,charset:java.nio.charset.Charset)(implicitevidence$2:ciris.api.Sync[F],implicitdecoder:ciris.ConfigDecoder[String,Value]):ciris.ConfigEntry[F,(java.io.File,java.nio.charset.Charset),String,Value][propf]: /api/ciris/index.html#propF[F[\_],Value](key:String)(implicitevidence$2:ciris.api.Sync[F],implicitdecoder:ciris.ConfigDecoder[String,Value]):ciris.ConfigEntry[F,String,String,Value]
[env]: /api/ciris/index.html#env[Value](key:String)(implicitdecoder:ciris.ConfigDecoder[String,Value]):ciris.ConfigEntry[ciris.api.Id,String,String,Value]
[prop]: /api/ciris/index.html#prop[Value](key:String)(implicitdecoder:ciris.ConfigDecoder[String,Value]):ciris.ConfigEntry[ciris.api.Id,String,String,Value]
[file]: /api/ciris/index.html#file[Value](file:java.io.File,modifyFileContents:String=>String,charset:java.nio.charset.Charset)(implicitdecoder:ciris.ConfigDecoder[String,Value]):ciris.ConfigEntry[ciris.api.Id,(java.io.File,java.nio.charset.Charset),String,Value]
[fileWithName]: /api/ciris/index.html#fileWithName[Value](name:String,modifyFileContents:String=>String,charset:java.nio.charset.Charset)(implicitdecoder:ciris.ConfigDecoder[String,Value]):ciris.ConfigEntry[ciris.api.Id,(java.io.File,java.nio.charset.Charset),String,Value]
[transformF]: /api/ciris/ConfigEntry.html#transformF[G[_]](implicitevidence$2:ciris.api.Apply[G],implicitf:F~>G):ciris.ConfigEntry[G,K,S,V][configentry]: /api/ciris/ConfigEntry.html
