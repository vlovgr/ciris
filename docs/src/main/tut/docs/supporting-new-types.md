---
layout: docs
title: "Supporting New Types"
permalink: /docs/supporting-new-types
---

# Supporting New Types
There may be times when you need to write decoders for custom types, when the type isn't already supported. Decoders are represented with [`ConfigDecoder`][ConfigDecoder]s and include several combinators for creating new decoders from existing ones. The [companion object][ConfigDecoderCompanion] of [`ConfigDecoder`][ConfigDecoder] also contain several functions for helping you create new decoders. Following, we'll show how to write decoding support for a custom `Odd` type representing odd numbers.

Let's say you're dealing with a sealed `Odd` class, where you can only construct instances using an `odd` method, which accepts an `Int` and returns an `Option[Odd]`. Note that an alternative way to represent `Odd` values is by using the `Odd` predicate from [refined](/docs/refined-module), which saves you having to write custom decoders. However, there may be times when you need to resort to custom types, so `Odd` is simply serving as an example here.

```tut:silent
sealed abstract case class Odd(value: Int)

def odd(value: Int): Option[Odd] = {
  Option(value)
    .filter(_ % 2 == 1)
    .map(new Odd(_) {})
}
```

When we try to decode values of type `Odd`, we'll get an error at compile-time.

```tut:book:fail
{
  import ciris.env
  env[Odd]("SHLVL")
}
```

As the error suggests, we'll have to define an implicit `ConfigDecoder[String, Odd]` instance and include it in scope. We can take this one step further, by saying that if there is a `ConfigDecoder[A, Int]` instance available, we can provide a `ConfigDecoder[A, Odd]` instance. The [`mapOption`][mapOption] function on [`ConfigDecoder`][ConfigDecoder] can be used to convert the `Int` to an `Odd`. Most [`ConfigDecoder`][ConfigDecoder] functions accept a `typeName` argument, which is the name of the type we are attempting to decode. In this case, and in general when the type you're dealing with does not have type parameters, it's simple enough to provide the type name. When dealing with types with type parameters, you might have to resort to using [type tags][typeTags], depending on the platform you're running on (Scala, Scala.js, Scala Native).

```tut:book
import ciris.ConfigDecoder

implicit def oddConfigDecoder[A](
  implicit decoder: ConfigDecoder[A, Int]
): ConfigDecoder[A, Odd] = {
  decoder.mapOption("Odd")(odd)
}
```

If we now try to decode an `Odd` value, we should no longer get an error at compile-time.

```tut:book
import ciris.env

env[Odd]("SHLVL")
```

[ConfigDecoderCompanion]: /api/ciris/ConfigDecoder$.html
[ConfigDecoder]: /api/ciris/ConfigDecoder.html
[mapOption]: /api/ciris/ConfigDecoder.html#mapOption[C](typeName:String)(f:B=>Option[C]):ciris.ConfigDecoder[A,C]
[typeTags]: https://docs.scala-lang.org/overviews/reflection/typetags-manifests.html
