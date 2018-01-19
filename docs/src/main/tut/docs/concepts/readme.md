---
layout: docs
title: "Core Concepts"
position: 2
permalink: /docs/concepts
---

# Core Concepts
This section explains the core concepts in Ciris, including the types `ConfigSource`, `ConfigEntry`, `ConfigKeyType`, `ConfigDecoder`, `ConfigError`, `ConfigErrors`, and `ConfigValue` and the methods `loadConfig`, `withValue`, `withValues`, `env`, `prop`, `arg`, and `read`. For [basic usage](/docs/basics), you do not need to have a complete understanding of these concepts, although it might be helpful. If you need to do some integration with Ciris, like creating a new [module](/docs/modules), defining a new [configuration source](/docs/sources), or writing a [custom decoder](/docs/decoders) to support new types, understanding these core concepts will be beneficial.

## Configuration Sources
Configuration values can be read from different sources, represented by `ConfigSource`s, which are anything that can map keys of type `Key` to `String` values, like `Map[Int, String]` for example, and that return a `ConfigError` error if there was an error while reading the value (for example, when no value exists for a given key). A `ConfigSource` reads keys of type `ConfigKeyType`, which is simply a wrapper around the key name and type, for example `environment variable` and type `String`. `ConfigSource` returns a `ConfigEntry`, which is the result of reading a key, including the read key and the `ConfigKeyType`. The abstract class `ConfigSource` is defined as follows.

```scala
abstract class ConfigSource[Key](val keyType: ConfigKeyType[Key]) {
  def read(key: Key): ConfigEntry[Key]
}
```

Ciris provides `ConfigSource`s for environment variables, system properties, and command-line arguments in the core library. If you require other configuration sources, you can easily create your own. You can read more about `ConfigSource`s in the [Configuration Sources](/docs/sources) section.

## Configuration Decoders
Values read from a `ConfigSource` can be converted into another type `A` using a `ConfigDecoder[A]`. `ConfigDecoders`s accept a `ConfigEntry` from a source and tries to convert the `String` value into type `A`, returning a `ConfigError` error if the conversion was unsuccessful. The abstract class `ConfigDecoder` is defined as follows.

```scala
abstract class ConfigDecoder[V] {
  def decode[K, S](
    entry: ConfigEntry[K, S, String]
  ): Either[ConfigError, V]
}
```

Ciris provides `ConfigDecoder` instances for many types in the standard library, and for third-party library types in separate modules. Modules like `ciris-generic` use [shapeless](https://github.com/milessabin/shapeless) to derive `ConfigDecoder` instances for certain types, like case classes with one argument and [value classes](http://docs.scala-lang.org/overviews/core/value-classes.html). For more information on Ciris modules, see [Modules Overview](/docs/modules). You can also easily create your own `ConfigDecoder`s, as described in the [Custom Decoders](/docs/decoders) section.

## Configuration Values
A configuration value read for a specific key, from a `ConfigSource`, and converted to type `A` using a `ConfigDecoder[A]`, results in a `ConfigValue[A]`, which is a thin wrapper around an `Either[ConfigError, A]` instance. Ciris provides methods like `env`, `prop`, `arg`, and `read` for reading `ConfigValue`s from environment variables, system properties, command-line arguments, and other configuration sources. `ConfigValue`s are used internally to deal with error accumulation, and when loading configurations you'll typically see `ConfigErrors`, which is the result of error accumulation using `ConfigValue`s.

## Loading Configurations
The `loadConfig` and `withValues` (and `withValue`) methods allow you to combine multiple `ConfigValue`s, while accumulating errors, and using those values to create your configuration. The difference between them is that `withValues` declares a dependency required to be able to use `loadConfig` (think `flatMap`), while `loadConfig` loads your configuration using `ConfigValue`s. The `withValues` method is useful, for example, when dealing with [Multiple Environments](/docs/environments).
