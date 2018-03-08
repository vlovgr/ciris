---
layout: docs
title: "Current Supported Types"
permalink: /docs/supported-types
---

# Current Supported Types
Ciris has support for many standard library types, and integrates with libraries like [enumeratum](/docs/enumeratum-module), [refined](/docs/refined-module), [spire](/docs/spire-module), and [squants](/docs/squants-module), to support even more types. If you're trying to decode a standard library type, it's likely that it should be supported by Ciris, so please [file an issue](https://github.com/vlovgr/ciris/issues/new) or, even better, submit a pull request. The same applies if you're trying to integrate with a library for which Ciris does not already provide a module.

The following basic types have support for decoding from `String` in the core module.

- `Boolean`, `Byte`, `Char`, `Double`, `Float`, `Int`, `Long`, `Short`, and `String`.  
  - `Double` and `Float` decoders also support percent format ending with `%`.
  - `Boolean`s can be decoded from `true`/`false`, `yes`/`no`, and `on`/`off`.
- `java.io`: `File`.
- `java.math`: `BigInteger` and `BigDecimal`.
- `java.net`: `InetAddress`, `URI`, and `URL`.
- `java.nio.charset`: `Charset`.
- `java.nio.file`: `Path`.
- `java.time.chrono`: `Chronology`, `HijrahEra`, `IsoEra`, `JapaneseEra`, `MinguoEra`, `ThaiBuddhistEra`.
- `java.time.format`: `DateTimeFormatter`, `FormatStyle`, `ResolverStyle`, `SignStyle`, and `TextStyle`.
- `java.time`: `DayOfWeek`, `Duration`, `Instant`, `LocalDate`, `LocalDateTime`, `LocalTime`, `Month`, `MonthDay`, `OffsetDateTime`, `OffsetTime`, `Period`, `Year`, `YearMonth`, `ZonedDateTime`, `ZoneId`, and `ZoneOffset`. Some of these decoders can optionally be configured, see the [configurable decoders](#configurable-decoders) section.
- `java.util.regex`: `Pattern`.
- `java.util`: `UUID`.
- `scala.concurrent.duration`: `Duration` and `FiniteDuration`.
- `scala.math`: `BigInt` and `BigDecimal`.
- `scala.util.matching`: `Regex`.

The following composite types are supported in the core module.

- `Option` for values which are not required to be present. See the [usage basics](/docs/basics#configuration-values) section for an example.
- [`Secret`][Secret] for avoiding logging secret configuration values. See [logging configurations](/docs/logging) for more details.

Modules provide support for additional types, please refer to [modules overview](/docs/modules) for more information.  
For an explanation of how you can write decoders to support new types, refer to [supporting new types](/docs/supporting-new-types).

## Configurable Decoders
Some `java.time` decoders can optionally be configured to use a different parsing format. Unless configured, decoders will rely on the default parsing behaviour and format. These decoders look for an implicit `DateTimeFormatter`, so to override the behaviour, simply provide one in scope.

```tut:book
import ciris.{ConfigEntry, ConfigKeyType}
import java.time.format.DateTimeFormatter
import java.time.LocalDate

val entry = ConfigEntry("key", ConfigKeyType("key type"), Right("20180307"))

implicit val format: DateTimeFormatter =
  DateTimeFormatter.ofPattern("yyyyMMdd")

entry.decodeValue[LocalDate]
```

Note that the implicit `DateTimeFormatter` format will be used for all configurable `java.time` decoders in its scope. If you're decoding multiple `java.time` types, you might need to limit the scope of the implicit format. Alternatively, you can also create the decoder manually by specifying the format to use.

```tut:book
import ciris.ConfigDecoder

implicit val localDateDecoder: ConfigDecoder[String, LocalDate] =
  ConfigDecoder.localDateConfigDecoder(format)

entry.decodeValue[LocalDate]
```

[Secret]: /api/ciris/Secret.html
