---
layout: docs
title: "Encoding Validation"
permalink: /docs/validation
---

# Encoding Validation
Ensuring that your configurations are valid can be a tricky challenge. What we're trying to avoid is _latent configuration errors_[^1] which occur because configuration values are not validated upfront. When trying to use these values, we realize they are unusable, potentially causing all sorts of problems. For example, as seen below, we might accidentally use weak secret keys in our production environment, or try to start our service on ports we should never occupy in the first place.

Ciris approach to avoiding _latent configuration errors_ is to use more precise types for your configuration values, only allowing values which you know are _useable_ to exist in the application. Essentially, values are validated as they are loaded, as part of the configuration loading process, and you'll end up with a configuration you know is _useable_. As you'll see later on, determining what _useable_ means can be difficult on its own, and we'll discuss how to reason about the concept.

The main thing to remember is that we're trying to prevent errors where possible, and reduce the possibility of errors where they cannot be fully prevented. Ideally, we want to make only valid configurations representable, and discover _invalid_ configuration values as early as possible. The ultimate goal is to make working with configurations more safe.

## Precise Configurations
One challenge with loading configuration values is that most values are interpreted as `String`s, but that's rarely the type we want, or should, use to represent values. For example, you probably don't want to use any `String` as an API key (surely not the empty `String`, and not too weak keys), and not `String` or any `Int` for the port number (many port numbers are reserved or require sudo permissions to use).

Ciris encourages you to encode validation by using more precise types, and integrates with several external libraries, like [enumeratum](/docs/enumeratum-module), [refined](/docs/refined-module), and [squants](/docs/squants-module), to be able to decode values into types provided by those libraries. One of the easiest and most convenient ways to use more precise types, is to use [refined](/docs/refined-module) and refinement types.

Using refinement types, we can create a type which _refines_ an existing base type by applying a predicate type, which represents the validation logic. For example, we could express a type `ApiKey`, which, in this case, is any `String` with a length between 25 and 40 characters, and which only contains alphanumeric characters.

```tut:silent
import eu.timepit.refined.api.Refined
import eu.timepit.refined.string.MatchesRegex
import eu.timepit.refined.W

type ApiKey = String Refined MatchesRegex[W.`"[a-zA-Z0-9]{25,40}"`.T]
```

By using the `ApiKey` type instead of `String` whenever we deal with an API key, we can now be confident that the value is not an invalid variant (like the empty `String`, or a too weak key, for example). Ciris integrates with refined, so you can load configuration values of type `ApiKey` without writing any additional code.

```tut:book
import ciris.{env, prop}
import ciris.refined._

env[ApiKey]("API_KEY").
  orElse(prop("api.key")).
  orNone
```

Refinement types are also useful for ensuring that configuration values residing in code are valid. Thanks to [refined](/docs/refined-module) providing an `auto` macro, we can ensure that literal configuration values conform to their predicates at compile-time, and all we have to do is to use the appropriate import. Note that the actual `ApiKey` (or any other secret values) shouldn't be included in code, but rather loaded from, for example, a vault service. The `ApiKey` below could, for example, be used in local tests, and would there not be seen as a secret, and could therefore reside in code.

```tut:book
import eu.timepit.refined.auto._

val apiKey: ApiKey = "RacrqvWjuu4KVmnTG9b6xyZMTP7jnX"
```

If the `ApiKey` is not valid, we'll get an error at compile-time.

```tut:fail
val apiKey: ApiKey = "changeme"
```

If we need to use libraries which doesn't support our `ApiKey` type, we can retrieve the underlying `String` value.

```tut:book
apiKey.value
```

Also, if we want to avoid accidentally logging secrets, we can use [`Secret`][Secret].

```tut:book
import ciris.Secret

env[Secret[ApiKey]]("API_KEY").
  orElse(prop("api.key")).
  orNone
```

For more information about [`Secret`][Secret] and logging, refer to the [logging configurations](/docs/logging) section.

Refinement types are not limited to `String`s, and [refined](/docs/refined-module) already includes many common refinement types. One example is `UserPortNumber` for `Int`s representing port numbers in the closed interval 1024 to 49151. This is a more precise definition of port numbers than `Int`, and lets us avoid many reserved port numbers.

```tut:book
import eu.timepit.refined.types.net.UserPortNumber

env[UserPortNumber]("PORT").
  orElse(prop("http.port")).
  orNone
```

Putting everything together, we're left with a more precise configuration, with validation encoded in the types.

```tut:silent
import eu.timepit.refined.types.numeric.PosInt
import eu.timepit.refined.types.string.NonEmptyString

final case class ApiConfig(
  key: Secret[ApiKey],
  port: UserPortNumber,
  timeoutSeconds: PosInt
)

final case class Config(
  appName: NonEmptyString,
  api: ApiConfig
)
```

The literal, and default, configuration values are also validated at compile-time. Ciris helps you load refinement types without having to write any additional code, and we've already drastically reduced the risk of _latent configuration errors_.

```tut:book
import ciris.loadConfig

val config =
  loadConfig(
    env[Secret[ApiKey]]("API_KEY").
      orElse(prop("api.key")),
    prop[Option[UserPortNumber]]("http.port")
  ) { (apiKey, port) =>
    Config(
      appName = "my-api",
      api = ApiConfig(
        key = apiKey,
        timeoutSeconds = 10,
        port = port getOrElse 4000
      )
    )
  }
```

## Useable Configurations
An interesting question arises when using refinement types: how far should we go to ensure that our configuration values are _useable_? For example, despite having restricted port numbers to `UserPortNumber`s, there is nothing that guarantees that the specified port is actually available, as another service might already be using the port. Being familiar with refinement types, you might be tempted to write an `OpenPort` predicate, which checks whether the port is open or not by creating a socket and immediately closing it.

```tut:silent
import eu.timepit.refined.api.Validate
import java.net.ServerSocket

final case class OpenPort()

implicit val openPortValidate: Validate.Plain[Int, OpenPort] =
  Validate.fromPartial(new ServerSocket(_).close(), "OpenPort", OpenPort())
```

We'll then check whether some `Int`s conform to the `OpenPort` predicate.

```tut:book
import eu.timepit.refined.refineV

// System port number, requires sudo permissions
refineV[OpenPort](989)

// User port number, can be used, and is not already used
refineV[OpenPort](10000)

// Port number outside range, cannot be used
refineV[OpenPort](65536)
```

While this might seem like a good idea at first, when used in conjunction with the `auto` macro, for compile-time safe literal configuration values, we are actually performing the `OpenPort` check during compile-time. This means that the port values you specify in code, need to be open on the machine compiling the code, which is not what you would expect.

```tut:fail
val port: Int Refined OpenPort = 989
```

Maybe it's not such a good idea to use _impure_ functions in our predicates. There are still some configuration values for which we'll have to guard against errors when using the values (binding a port number, for example). However, we can still reduce the possibility of errors by being more precise in the definition of the values. For port numbers, for example, it means that we can prevent attempts to use _unuseable_ port number at compile-time (for port numbers specified in code), or as part of the configuration loading process (for port numbers loaded from the environment). If we're able to detect unuseable configuration values as early as at compile-time, or during configuration loading, we've saved valuable time by preventing errors as early as possible.

In general, it's recommended to only use _pure_ functions in predicates, and to try and be as precise as is practically possible when defining configuration value types -- you'll have to use your own judgement when it comes to this. It might take considerable effort to create very precise predicate types, but it can also pay off in terms of fewer errors and failures. Sometimes it is enough to use a more precise type than you normally would, for example `NonEmptyString` instead of `String`, which  might not be as precise as possible, but still eliminates some invalid variants.

## External Libraries
When interacting with other libraries, you'll often see uses of imprecise types, like `String`, even though a more precise type is expected. Often there is validation logic behind the scenes, which can be extracted to a predicate type, to avoid unexpected errors. An example is the name of a [Kafka](https://kafka.apache.org) topic, where Kafka libraries typically accept a `String` for the topic name, but checks to ensure that it follows some validation rules. Depending on the library, these rules may or may not be well documented, and sometimes you'll have to dive into the [code](https://github.com/apache/kafka/blob/6cfcc9d553622e7d511a849935e9b504f947399d/clients/src/main/java/org/apache/kafka/common/internals/Topic.java) to find them.

For reference, following is an example of how to express the Kafka topic name validation rules.

```tut:silent
def isKafkaTopicName(topic: String): Boolean =
  1 <= topic.size && topic.size <= 249 && (
    topic != "." && topic != ".." && (
      topic.forall(c => c.isLetterOrDigit || c == '.' || c == '_' || c == '-')
    ))
```

For comparison, following is an example of how to express the validation rules with refinement types.

```tut:silent
import eu.timepit.refined.boolean.{And, Not, Or}
import eu.timepit.refined.char.LetterOrDigit
import eu.timepit.refined.collection.{Forall, Size}
import eu.timepit.refined.generic.Equal
import eu.timepit.refined.numeric.Interval

type KafkaTopicName = String Refined
  And[Size[Interval.Closed[W.`1`.T, W.`249`.T]],
      And[Not[Equal[W.`"."`.T]],
          And[Not[Equal[W.`".."`.T]],
              Forall[Or[LetterOrDigit,
                        Or[Equal[W.`'.'`.T],
                           Or[Equal[W.`'_'`.T],
                              Equal[W.`'-'`.T]]]]]]]]
```

Note the similarities between working at the value-level with `isKafkaTopicName`, and representing the same validation rules at the type-level with `KafkaTopicName`. While the type signature above might look complicated at first glance, there is quite often a straightforward translation between validation rules at the value-level and the equivalent rules at the type-level. Note that we instead could have chosen to represent the rules with a regular expression, both at the value-level and type-level (using the `MatchesRegex` predicate).

Kafka topic names are generally not secret, and can therefore reside as configuration values in code. With the refinement type `KafkaTopicName`, we benefit from being able to validate our Kafka topic names at compile-time, meaning we can be sure at compile-time that our topic names are _useable_.

```tut:book
val kafkaTopicName: KafkaTopicName = "my-topic-v2"
```

---

[^1]: For more information on latent configuration errors, refer to the paper [Early Detection of Configuration Errors to Reduce Failure Damage](https://www.usenix.org/system/files/conference/osdi16/osdi16-xu.pdf) and Leif Wickland's presentation [Defusing the Configuration Time Bomb](http://leifwickland.github.io/presentations/configBomb/) on the subject.

[refined]: https://github.com/fthomas/refined
[Secret]: /api/ciris/Secret.html
