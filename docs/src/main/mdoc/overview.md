---
id: overview
title: Overview
---

Ciris is a functional programming library for loading configurations.

In particular, the following features are supported.

- Loading values from multiple sources and defining default values.
- Composing multiple configuration values into larger configurations.
- Preventing secret values from being shown, redacting sensitive errors.
- Decoding configuration values to various commonly used types.
- Accumulating errors when multiple values cannot be loaded.

Documentation is kept up-to-date, currently documenting v@LATEST_VERSION@ on Scala @DOCS_SCALA_VERSION@.

## Getting Started

To get started with [sbt](https://scala-sbt.org), add the following line to your `build.sbt` file.

```scala
libraryDependencies += "@ORGANIZATION@" %% "@CORE_MODULE_NAME@" % "@LATEST_VERSION@"
```

Published for Scala @SCALA_PUBLISH_VERSIONS@, [Scala.js](https://www.scala-js.org) @SCALA_JS_MAJOR_MINOR_VERSION@ and [Scala Native](https://scala-native.org) @SCALA_NATIVE_MAJOR_MINOR_VERSION@.

For changes between versions, please refer to the [release notes](https://github.com/vlovgr/ciris/releases).

If you are using Scala.js or Scala Native, make sure to replace `%%` with `%%%` above.

For Scala 2.12, enable partial unification by adding the following line to `build.sbt`.

```scala
scalacOptions += "-Ypartial-unification"
```

### Modules

Following are additional provided modules.

#### Circe

For [circe](modules.md#circe) support, add the following line to your `build.sbt` file.

```scala
libraryDependencies += "@ORGANIZATION@" %% "@CIRCE_MODULE_NAME@" % "@LATEST_VERSION@"
```

#### Circe YAML

For [circe-yaml](modules.md#circe-yaml) support, add the following line to your `build.sbt` file.

```scala
libraryDependencies += "@ORGANIZATION@" %% "@CIRCE_YAML_MODULE_NAME@" % "@LATEST_VERSION@"
```

#### Enumeratum

For [enumeratum](modules.md#enumeratum) support, add the following line to your `build.sbt` file.

```scala
libraryDependencies += "@ORGANIZATION@" %% "@ENUMERATUM_MODULE_NAME@" % "@LATEST_VERSION@"
```

#### Http4s

For [http4s](modules.md#http4s) support, add the following line to your `build.sbt` file.

```scala
libraryDependencies += "@ORGANIZATION@" %% "@HTTP4S_MODULE_NAME@" % "@LATEST_VERSION@"
```

#### Refined

For [refined](modules.md#refined) support, add the following line to your `build.sbt` file.

```scala
libraryDependencies += "@ORGANIZATION@" %% "@REFINED_MODULE_NAME@" % "@LATEST_VERSION@"
```

#### Squants

For [squants](modules.md#squants) support, add the following line to your `build.sbt` file.

```scala
libraryDependencies += "@ORGANIZATION@" %% "@SQUANTS_MODULE_NAME@" % "@LATEST_VERSION@"
```

#### External Modules

Following is an incomplete list of third-party integrations.

- [`ciris-aiven-kafka`](https://github.com/ovotech/ciris-aiven-kafka)
- [`ciris-aws-secretsmanager`](https://github.com/ovotech/ciris-aws-secretsmanager)
- [`ciris-aws-ssm`](https://github.com/ovotech/ciris-aws-ssm)
- [`ciris-credstash`](https://github.com/ovotech/ciris-credstash)
- [`ciris-hocon`](https://github.com/2m/ciris-hocon)
- [`ciris-kubernetes`](https://github.com/ovotech/ciris-kubernetes)

### Signatures

Stable release artifacts are signed with the [`3C73 EC3D A303 8ED3`](https://keys.openpgp.org/search?q=A130DFFBE3EB5850069A54173C73EC3DA3038ED3) key.

### Compatibility

Backwards binary-compatibility for the library is guaranteed between minor and patch versions.<br>
Version `@LATEST_MAJOR_VERSION@.a.b` is backwards binary-compatible with `@LATEST_MAJOR_VERSION@.c.d` for any `a > c` or `a = c` and `b > d`.

Please note binary-compatibility is not guaranteed between milestone releases.

### Snapshot Releases

To use the latest snapshot release, add the following lines to your `build.sbt` file.

```scala
resolvers += Resolver.sonatypeRepo("snapshots")

libraryDependencies += "@ORGANIZATION@" %% "@CORE_MODULE_NAME@" % "@LATEST_SNAPSHOT_VERSION@"
```

## Dependencies

Refer to the table below for dependencies and version support across modules.

| Module                     | Dependencies                                                                                                                                    | Scala                                   |
| -------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------- | --------------------------------------- |
| `@CORE_MODULE_NAME@`       | [Cats Effect @CATS_EFFECT_VERSION@](https://github.com/typelevel/cats-effect)                                                                   | Scala @CORE_CROSS_SCALA_VERSIONS@       |
| `@CIRCE_MODULE_NAME@`      | [Circe @CIRCE_VERSION@](https://github.com/circe/circe)                                                                                         | Scala @CIRCE_CROSS_SCALA_VERSIONS@      |
| `@CIRCE_YAML_MODULE_NAME@` | [Circe YAML @CIRCE_YAML_VERSION@](https://github.com/circe/circe-yaml)                                                                          | Scala @CIRCE_YAML_CROSS_SCALA_VERSIONS@ |
| `@ENUMERATUM_MODULE_NAME@` | [Enumeratum @ENUMERATUM_VERSION@](https://github.com/lloydmeta/enumeratum), [TypeName @TYPENAME_VERSION@](https://github.com/tpolecat/typename) | Scala @ENUMERATUM_CROSS_SCALA_VERSIONS@ |
| `@HTTP4S_MODULE_NAME@`     | [Http4s @HTTP4S_VERSION@](https://github.com/http4s/http4s)                                                                                     | Scala @HTTP4S_CROSS_SCALA_VERSIONS@     |
| `@REFINED_MODULE_NAME@`    | [Refined @REFINED_VERSION@](https://github.com/fthomas/refined), [TypeName @TYPENAME_VERSION@](https://github.com/tpolecat/typename)            | Scala @REFINED_CROSS_SCALA_VERSIONS@    |
| `@SQUANTS_MODULE_NAME@`    | [Squants @SQUANTS_VERSION@](https://github.com/typelevel/squants)                                                                               | Scala @SQUANTS_CROSS_SCALA_VERSIONS@    |

For Scala.js and Scala Native version support, refer to the following table.

| Module                     | Scala.js                                                                         | Scala Native                                                                                 |
| -------------------------- | -------------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------- |
| `@CORE_MODULE_NAME@`       | Scala.js @SCALA_JS_MAJOR_MINOR_VERSION@ (Scala @CORE_JS_CROSS_SCALA_VERSIONS@)   | Scala Native @SCALA_NATIVE_MAJOR_MINOR_VERSION@ (Scala @CORE_NATIVE_CROSS_SCALA_VERSIONS@)   |
| `@CIRCE_MODULE_NAME@`      | Scala.js @SCALA_JS_MAJOR_MINOR_VERSION@ (Scala @CIRCE_JS_CROSS_SCALA_VERSIONS@)  | Scala Native @SCALA_NATIVE_MAJOR_MINOR_VERSION@ (Scala @CIRCE_NATIVE_CROSS_SCALA_VERSIONS@)  |
| `@CIRCE_YAML_MODULE_NAME@` | :x:                                                                              | :x:                                                                                          |
| `@ENUMERATUM_MODULE_NAME@` | :x:                                                                              | :x:                                                                                          |
| `@HTTP4S_MODULE_NAME@`     | Scala.js @SCALA_JS_MAJOR_MINOR_VERSION@ (Scala @HTTP4S_JS_CROSS_SCALA_VERSIONS@) | Scala Native @SCALA_NATIVE_MAJOR_MINOR_VERSION@ (Scala @HTTP4S_NATIVE_CROSS_SCALA_VERSIONS@) |
| `@REFINED_MODULE_NAME@`    | :x:                                                                              | :x:                                                                                          |
| `@SQUANTS_MODULE_NAME@`    |                                                                                  |                                                                                              |

## Participation

Ciris embraces pure, typeful, idiomatic functional programming in Scala, and wants to provide a safe and friendly environment for teaching, learning, and contributing as described in the [Scala Code of Conduct](https://www.scala-lang.org/conduct/).

## License

Licensed under the [MIT license](https://opensource.org/licenses/MIT). Refer to the [license file](https://github.com/vlovgr/ciris/blob/master/license.txt).
