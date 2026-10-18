# http4s-aws

The `@HTTP4SAWS_MODULE_NAME@` module provides a way to read decrypted values from the [AWS Systems Manager (SSM) Parameter Store](https://docs.aws.amazon.com/systems-manager/latest/userguide/systems-manager-parameter-store.html) using the [http4s-aws](https://github.com/maginepro/http4s-aws) library.

## Getting Started

To get started with [sbt](https://scala-sbt.org), add the following line to your `build.sbt` file.

```scala
libraryDependencies += "@ORGANIZATION@" %% "@HTTP4SAWS_MODULE_NAME@" % "@VERSION@"
```

If you are using Scala.js or Scala Native, make sure to replace `%%` with `%%%` above.

## Usage

Following is an example of how parameter values can be retrieved. Note parameter values are wrapped in [Secret](../configurations.md#secrets) by default since they are usually sensitive.

```scala mdoc
import cats.effect.IO
import cats.effect.IOApp
import ciris._
import ciris.http4s.aws.AwsSsmParameters
import com.magine.aws.Region
import com.magine.http4s.aws.CredentialsProvider
import org.http4s.ember.client.EmberClientBuilder

object Main extends IOApp.Simple {
  case class Config(username: String, password: Secret[String])

  object Config {
    def fromParameters(parameters: AwsSsmParameters[IO]): IO[Config] =
      (
        env("USERNAME"),
        parameters("password")
      ).parMapN(apply).load[IO]
  }

  override def run: IO[Unit] =
    EmberClientBuilder.default[IO].build.use { client =>
      CredentialsProvider.default(client).use { provider =>
        val parameters = AwsSsmParameters(client, provider, Region.EU_WEST_1)
        Config.fromParameters(parameters).flatMap(IO.println)
      }
    }
}
```
