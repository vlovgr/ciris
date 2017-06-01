---
layout: docs
title: "Core Concepts"
position: 2
permalink: /docs/concepts
---

# <a name="core-concepts" href="#core-concepts">Core Concepts</a>
This section explains the core concepts in Ciris, including the types `ConfigSource`, `ConfigKeyType`, `ConfigReader`, `ConfigError`, `ConfigErrors`, and `ConfigValue` and the methods `loadConfig`, `withValue`, `withValues`, `env`, `prop`, and `read`. For [basic usage](/docs/basics), you do not need to have a complete understanding of these concepts, although it might be helpful. If you need to do some integration with Ciris, like creating a new [module](/docs/modules), defining a new [configuration source](/docs/sources), or writing a [custom reader](/docs/readers) to support new types, understanding these core concepts will be beneficial.

## <a name="configuration-sources" href="#configuration-sources">Configuration Sources</a>
Configuration values can be read from different sources, represented by `ConfigSource`s, which are anything that can map `String` keys to `String` values, like `Map[String, String]` for example, and that return a `ConfigError` error if there was an error while reading the value (for example, when no value exists for a given key). A `ConfigSource` reads keys of type `ConfigKeyType`, which is simply a wrapper around the key name, for example `environment variable`. The abstract class `ConfigSource` is defined as follows.

```scala
abstract class ConfigSource(val keyType: ConfigKeyType) {
  def read(key: String): Either[ConfigError, String]
}
```

Ciris provides `ConfigSource`s for environment variables and system properties in the core library. If you require other configuration sources, you can easily create your own. You can read more about `ConfigSource`s in the [Configuration Sources](/docs/sources) section.

## <a name="configuration-readers" href="#configuration-readers">Configuration Readers</a>
Values read from a `ConfigSource` can be converted into another type `A` using a `ConfigReader[A]`. `ConfigReader`s accept an implicit `ConfigSource`, reads values for specified keys, and tries to convert the `String` values into type `A`, returning a `ConfigError` error if the conversion was unsuccessful or if the source could not provide a value for the key. The sealed abstract class `ConfigReader` is defined as follows.

```scala
sealed abstract class ConfigReader[A] {
  def read(key: String)(implicit source: ConfigSource): Either[ConfigError, A]
}
```

Ciris provides `ConfigReader` instances for many types in the standard library, and for third-party library types in separate modules. Modules like `ciris-generic` use [shapeless](https://github.com/milessabin/shapeless) to derive `ConfigReader` instances for certain types, like case classes with one argument and [value classes](http://docs.scala-lang.org/overviews/core/value-classes.html). For more information on Ciris modules, see [Modules Overview](/docs/modules). You can also easily create your own `ConfigReader`s, as described in the [Custom Readers](/docs/readers) section.

## <a name="configuration-values" href="#configuration-values">Configuration Values</a>
A configuration value read for a specific key, from a `ConfigSource`, and converted to type `A` using a `ConfigReader[A]`, results in a `ConfigValue[A]`, which is a thin wrapper around an `Either[ConfigError, A]` instance. Ciris provides methods like `env`, `prop`, and `read` for reading `ConfigValue`s from environment variables, system properties, and other configuration sources. `ConfigValue`s are used internally to deal with error accumulation, and when loading configurations you'll typically see `ConfigErrors`, which is the result of error accumulation using `ConfigValue`s.

## <a name="loading-configurations" href="#loading-configurations">Loading Configurations</a>
The `loadConfig` and `withValues` (and `withValue`) methods allow you to combine multiple `ConfigValue`s, while accumulating errors, and using those values to create your configuration. The difference between them is that `withValues` declares a dependency required to be able to use `loadConfig` (think `flatMap`), while `loadConfig` loads your configuration using `ConfigValue`s. The `withValues` method is useful, for example, when dealing with [Multiple Environments](/docs/environments).
