---
layout: docs
title: "Usage Basics"
position: 1
permalink: /docs/basics
---

# Usage Basics
Ciris configuration loading is done in two parts: define what to load and what to create once everything is loaded. Let's start simple by defining a configuration and loading only the necessary parts of it from the environment. If you haven't already, now is also a good time to separate your application from your configuration, so that the configuration can be loaded separately.

The configuration can be modeled with nested case classes. Here we'll define a small example configuration for an HTTP service, binding at a certain port, using an API key for request authorization, and using a maximum timeout when making HTTP requests to other services.

```tut:book
import scala.concurrent.duration._

final case class Config(
  apiKey: String,
  timeout: Duration,
  port: Int
)
```

In this case, the API key is a secret and we would like to load it from the environment. The same goes for the port, which needs to be dynamic depending on the environment. We can read environment variables using the `env` method and system properties using the `prop` method, both returning a `ConfigValue`. The `loadConfig` method then accepts `ConfigValue`s and expects a function creating the configuration using the loaded values. If there are errors while reading values, Ciris will deal with them and accumulate them for you as `ConfigErrors`.

```tut:book
import ciris._

val config =
  loadConfig(
    env[String]("API_KEY"), // Reads environment variable API_KEY
    prop[Option[Int]]("http.port") // Reads system property http.port
  ) { (apiKey, port) =>
    Config(
      apiKey = apiKey,
      timeout = 10 seconds,
      port = port getOrElse 4000
    )
  }
```

The values which do not need to be loaded from the environment can be written as literals. It's also possible to optionally read values (like for the port in the example above), providing defaults if they have not been specified. In this case, we forgot to specify the `API_KEY` environment variable and therefore get an appropriate error. If we want more sensible error messages, simply use the `messages` method on `ConfigErrors`.

```tut:book
config.left.map(_.messages)
```
