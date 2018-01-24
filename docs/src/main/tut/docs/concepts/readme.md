---
layout: docs
title: "Core Concepts"
position: 2
permalink: /docs/concepts
---

# Core Concepts
This section explains the core concepts in Ciris, including the types `ConfigSource`, `ConfigEntry`, `ConfigKeyType`, `ConfigDecoder`, `ConfigError`, and `ConfigErrors` and the methods `loadConfig`, `withValue`, `withValues`, `env`, `prop`, `arg`, `file`, and `fileWithName`. For [basic usage](/docs/basics), you do not need to have a complete understanding of these concepts, although it might be helpful. If you need to do some integration with Ciris, like creating a new [module](/docs/modules), defining a new [configuration source](/docs/sources), or writing a [custom decoder](/docs/decoders) to support new types, understanding these core concepts will be beneficial.

## Configuration Sources
Configuration values can be read from different sources, represented by `ConfigSource[K, V]`s, which are anything that can map keys of type `K` to values of type `V`, like `Map[Int, String]` for example, and that return a `ConfigError` error if there was an error while reading the value (for example, when no value exists for a given key). A `ConfigSource` reads keys described by `ConfigKeyType`, which is simply a wrapper around the key type and name, for example `environment variable` and type `String`. `ConfigSource` returns a `ConfigEntry`, which is the result of reading a key, including the key itself and the `ConfigKeyType`. The abstract class `ConfigSource` is defined as follows.

```scala
abstract class ConfigSource[K, V](val keyType: ConfigKeyType[K]) {
  def read(key: K): ConfigEntry[K, V, V]
}
```

Ciris provides `ConfigSource`s for environment variables, system properties, command-line arguments, and files in the core library. If you require other configuration sources, you can easily create your own. You can read more about `ConfigSource`s in the [Configuration Sources](/docs/sources) section.

## Configuration Decoders
Values of type `A` retrieved from a `ConfigSource` can be converted into another type `B` using a `ConfigDecoder[A, B]`. `ConfigDecoders`s accept a `ConfigEntry` from a source and tries to convert the `A` value into type `B`, returning a `ConfigError` error if the conversion was unsuccessful. The abstract class `ConfigDecoder` is defined as follows.

```scala
abstract class ConfigDecoder[A, B] {
  def decode[K, S](entry: ConfigEntry[K, S, A]): Either[ConfigError, B]
}
```

Ciris provides `ConfigDecoder` instances for many types in the standard library, and for third-party library types in separate modules. Modules like `ciris-generic` use [shapeless](https://github.com/milessabin/shapeless) to derive `ConfigDecoder` instances for certain types, like case classes with one argument and [value classes](http://docs.scala-lang.org/overviews/core/value-classes.html). For more information on Ciris modules, see [Modules Overview](/docs/modules). You can also easily create your own `ConfigDecoder`s, as described in the [Custom Decoders](/docs/decoders) section.

## Configuration Entries
A `ConfigEntry[K, S, V]` is a key-value pair retrieved from a `ConfigSource[K, S]` where the source value has been decoded into type `V` (normally with a `ConfigDecoder[S, V]`). `ConfigEntry` keeps the key, key type, and unmodifed source value to support sensible error messages. Ciris provides methods, like `env` and `prop`, for reading `ConfigEntry`s from environment variables, system properties, and other configuration sources.

## Loading Configurations
The `loadConfig` and `withValues` (and `withValue`) methods allow you to combine multiple `ConfigEntry`s, while accumulating errors, and using those values to create your configuration. The difference between them is that `withValues` declares a dependency required to be able to use `loadConfig` (think `flatMap`), while `loadConfig` loads your configuration using `ConfigEntry`s. The `withValues` method is useful, for example, when dealing with [Multiple Environments](/docs/environments).
