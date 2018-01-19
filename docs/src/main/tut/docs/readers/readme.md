---
layout: docs
title: "Custom Decoders"
position: 7
permalink: /docs/decoders
---

# Custom Decoders
Ciris already supports reading many standard library types and provides integrations with libraries like [enumeratum][enumeratum], [refined][refined], and [squants][squants]. If you're trying to load a standard library type, it is likely that it should be supported by Ciris, so please [file an issue](https://github.com/vlovgr/ciris/issues/new) or, even better, submit a pull-request. The same applies if you're trying to integrate with a library for which Ciris does not already provide a module.

However, there may be cases where you need to resort to defining custom decoders for your types. For example, let's say you're dealing with a sealed `Odd` class, where you can only construct instances from an `odd` method, which accepts an `Int` and returns an `Option[Odd]`.

```tut:book
import ciris._

sealed abstract case class Odd(value: Int)

def odd(value: Int): Option[Odd] = {
  Option(value)
    .filter(_ % 2 == 1)
    .map(new Odd(_) {})
}
```

We would now like to load `Odd` values using Ciris. If we try and do this straight away, it will fail during compile, saying there is no implicit `ConfigDecoder` in scope.

```tut:book:fail
env[Odd]("ODD_VALUE")
```

This means we'll have to define a custom implicit `ConfigDecoder[Odd]` instance. The [`ConfigDecoder`](https://cir.is/api/ciris/ConfigDecoder$.html) companion object provides some helper methods for creating `ConfigDecoder`s. In this case, we'll rely on the default `ConfigDecoder[Int]` instance to read an `Int` and we will then try to convert the `Int` to an `Odd` instance using the `mapOption` method. Most `ConfigDecoder` methods accept a `typeName` argument which is the name of the type you're reading. Depending on which Scala version and which platform (Scala, Scala.js, &hellip;) you run on, you may be able to use [type tags](http://docs.scala-lang.org/overviews/reflection/typetags-manifests.html). In this case, and cases where you don't have type parameters, it's simple enough to just provide the type name.

```tut:book
implicit def oddConfigDecoder(implicit decoder: ConfigDecoder[Int]) =
  decoder.mapOption("Odd")(odd)
```

We can then try to read `Odd` values from a custom [configuration source](/docs/sources).

```tut:book
implicit val source = {
  val keyType = ConfigKeyType[String]("int key")
  ConfigSource.fromMap(keyType)(Map("a" -> "abc", "b" -> "6", "c" -> "3"))
}

val a = read[Odd]("a")

a.value.left.map(_.message)

val b = read[Odd]("b")

b.value.left.map(_.message)

read[Odd]("c")
```

While this demonstrates how to create custom `ConfigDecoder`s, a better way to represent `Odd` values is by using the `Odd` predicate from [refined][refined]. The [Encoding Validation](/docs/validation) section has more information on the `ciris-refined` module and how you can use it to read refined types.

[enumeratum]: https://github.com/lloydmeta/enumeratum
[refined]: https://github.com/fthomas/refined
[squants]: https://github.com/typelevel/squants
