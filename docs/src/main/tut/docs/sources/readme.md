---
layout: docs
title: "Configuration Sources"
position: 6
permalink: /docs/sources
---

# <a name="configuration-sources" href="#configuration-sources">Configuration Sources</a>
Ciris provides support for reading environment variables and system properties. If you're looking for a common configuration source not yet supported, please [file an issue](https://github.com/vlovgr/ciris/issues/new) or, even better, submit a pull-request. If you require other configurations sources, you can easily define your own. You'll find helper methods for creating custom configuration sources in the companion object of [`ConfigSource`](https://cir.is/api/ciris/ConfigSource$.html). Let's illustrate this by writing a configuration source which captures the current system properties at a certain point in time, ignoring any later changes.

To create a configuration source, we need to provide a `ConfigKeyType` which is the name of the type of key the source reads (to support sensible error messages). We can convert the current system properties to a `Map` and create a `ConfigSource` from it using the `fromMap` method. If we make the source implicit and have it in scope, the `read` method will read `ConfigValue` configuration values from it. We can convert a `ConfigValue[T]` to an `Either[ConfigError, T]` by calling the `value` method.

```tut:invisible
if(!sys.props.get("file.encoding").isDefined)
  sys.props.put("file.encoding", "UTF-8")
```

```tut:book
import ciris._

implicit val fixedProperties = {
  val keyType = ConfigKeyType[String]("fixed system property")
  ConfigSource.fromMap(keyType)(sys.props.toMap)
}

read[String]("file.encoding")

prop[String]("file.encoding")
```

To verify that the source does not change, let's delete the `file.encoding` key, and verify that we can still read it.

```tut:book
sys.props.remove("file.encoding")

read[String]("file.encoding")

prop[String]("file.encoding")
```
