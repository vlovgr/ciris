# http4s

The `@HTTP4S_MODULE_NAME@` module provides [`ConfigDecoder`][configdecoder]s for [ip4s](https://github.com/Comcast/ip4s) and [http4s](https://github.com/http4s/http4s) types.

## Getting Started

To get started with [sbt](https://scala-sbt.org), add the following line to your `build.sbt` file.

```scala
libraryDependencies += "@ORGANIZATION@" %% "@HTTP4S_MODULE_NAME@" % "@VERSION@"
```

If you are using Scala.js or Scala Native, make sure to replace `%%` with `%%%` above.

## Usage

```scala mdoc
import ciris.env
import ciris.http4s._
import com.comcast.ip4s.Host
import com.comcast.ip4s.Port
import org.http4s.headers.Origin
import org.http4s.Uri

env("HOST").as[Host]
env("PORT").as[Port]
env("ORIGIN").as[Origin]
env("URI").as[Uri]
```

[configdecoder]: @API_URL@ciris/ConfigDecoder.html
