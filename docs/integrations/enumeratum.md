# enumeratum

The `@ENUMERATUM_MODULE_NAME@` module provides [`ConfigDecoder`][configdecoder]s for [enumeratum](https://github.com/lloydmeta/enumeratum) enumerations.

## Getting Started

To get started with [sbt](https://scala-sbt.org), add the following line to your `build.sbt` file.

```scala
libraryDependencies += "@ORGANIZATION@" %% "@ENUMERATUM_MODULE_NAME@" % "@VERSION@"
```

If you are using Scala.js or Scala Native, make sure to replace `%%` with `%%%` above.

## Usage

For regular `Enum`s, also mix in `CirisEnum` to derive a [`ConfigDecoder`][configdecoder] instance.

```scala mdoc:reset-object
import ciris.env
import enumeratum.{CirisEnum, Enum, EnumEntry}
import enumeratum.EnumEntry.Lowercase

sealed trait Suit extends EnumEntry with Lowercase

object Suit extends Enum[Suit] with CirisEnum[Suit] {
  case object Clubs extends Suit
  case object Diamonds extends Suit
  case object Hearts extends Suit
  case object Spades extends Suit

  val values = findValues
}

env("SUIT").as[Suit]
```

For `ValueEnum`s, also mix in the matching `CirisValueEnum` to derive a [`ConfigDecoder`][configdecoder] instance.

```scala mdoc:reset-object
import ciris.env
import enumeratum.values.{StringCirisEnum, StringEnum, StringEnumEntry}

sealed abstract class Color(val value: String) extends StringEnumEntry

object Color extends StringEnum[Color] with StringCirisEnum[Color] {
  case object Red extends Color("red")
  case object Green extends Color("green")
  case object Blue extends Color("blue")

  val values = findValues
}

env("COLOR").as[Color]
```

[configdecoder]: @API_URL@ciris/ConfigDecoder.html
