# refined

The `@REFINED_MODULE_NAME@` module provides @:api(ciris.ConfigDecoder)s for [refined](https://github.com/fthomas/refined) refinement types.

## Getting Started

To get started with [sbt](https://scala-sbt.org), add the following line to your `build.sbt` file.

```scala
libraryDependencies += "@ORGANIZATION@" %% "@REFINED_MODULE_NAME@" % "@VERSION@"
```

If you are using Scala.js or Scala Native, make sure to replace `%%` with `%%%` above.

## Usage

```scala mdoc
import ciris.env
import ciris.refined._
import eu.timepit.refined.types.numeric.PosInt

env("POS_INT").as[PosInt]
```
