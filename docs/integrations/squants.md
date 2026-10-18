# squants

The `@SQUANTS_MODULE_NAME@` module provides [`ConfigDecoder`][configdecoder]s for [squants](https://github.com/typelevel/squants) quantities.

## Getting Started

To get started with [sbt](https://scala-sbt.org), add the following line to your `build.sbt` file.

```scala
libraryDependencies += "@ORGANIZATION@" %% "@SQUANTS_MODULE_NAME@" % "@VERSION@"
```

If you are using Scala.js, make sure to replace `%%` with `%%%` above.

## Usage

```scala mdoc
import ciris.env
import ciris.squants._
import squants.market.Money

env("MONEY").as[Money]
```

[configdecoder]: @API_URL@ciris/ConfigDecoder.html
