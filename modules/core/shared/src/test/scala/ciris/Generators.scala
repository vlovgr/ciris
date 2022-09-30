/*
 * Copyright 2017-2022 Viktor Rudebeck
 *
 * SPDX-License-Identifier: MIT
 */

package ciris

import cats.{Eq, Show}
import cats.data.Chain
import org.scalacheck.{Arbitrary, Cogen, Gen}
import org.scalacheck.Arbitrary.arbitrary
import scala.collection.JavaConverters._
import scala.util.control.NoStackTrace
import scala.util.Try

trait Generators extends GeneratorsRuntimePlatform {
  val configKeyGen: Gen[ConfigKey] =
    for {
      description <- Gen.alphaNumStr
    } yield ConfigKey(description)

  implicit val configKeyArbitrary: Arbitrary[ConfigKey] =
    Arbitrary(configKeyGen)

  def chainGen[A](gen: Gen[A]): Gen[Chain[A]] =
    Gen.listOf(gen).map(Chain.fromSeq)

  implicit def chainArbitrary[A: Arbitrary]: Arbitrary[Chain[A]] =
    Arbitrary(chainGen(arbitrary[A]))

  def configKeyFunctionGen(gen: Gen[String => String]): Gen[ConfigKey => ConfigKey] =
    gen.map(f => (key: ConfigKey) => ConfigKey(f(key.description)))

  implicit def configKeyFunctionArbitrary(
    implicit arb: Arbitrary[String => String]
  ): Arbitrary[ConfigKey => ConfigKey] =
    Arbitrary(configKeyFunctionGen(arbitrary[String => String]))

  val configErrorMessageGen: Gen[String] =
    Gen.oneOf(
      Gen.alphaNumStr,
      Gen.alphaNumStr.map(_ ++ ConfigException.entryTrailing)
    )

  val configErrorGen: Gen[ConfigError] =
    Gen.lzy {
      val and =
        for {
          first <- configErrorGen
          second <- configErrorGen
        } yield first.and(second)

      val apply =
        for {
          message <- configErrorMessageGen
          trailing <- arbitrary[Boolean]
        } yield ConfigError(message)

      val decode =
        for {
          typeName <- Gen.alphaNumStr
          key <- Gen.option(configKeyGen)
          value <- Gen.alphaNumStr
        } yield ConfigError.decode(typeName, key, value)

      val empty =
        Gen.const(ConfigError.Empty)

      val loaded =
        Gen.const(ConfigError.Loaded)

      val missing =
        for {
          key <- configKeyGen
        } yield ConfigError.Missing(key)

      val or =
        for {
          first <- configErrorGen
          second <- configErrorGen
        } yield first.or(second)

      val sensitive =
        for {
          message <- configErrorMessageGen
          redactedMessage <- configErrorMessageGen
        } yield ConfigError.sensitive(message, redactedMessage)

      Gen
        .oneOf(and, apply, decode, empty, loaded, missing, or, sensitive)
        .flatMap(error => Gen.oneOf(error, error.redacted))
    }

  implicit val configErrorArbitrary: Arbitrary[ConfigError] =
    Arbitrary(configErrorGen)

  val configErrorFunctionGen: Gen[ConfigError => ConfigError] =
    configErrorGen.map(error => (_: ConfigError) => error)

  implicit val configErrorFunctionArbitrary: Arbitrary[ConfigError => ConfigError] =
    Arbitrary(configErrorFunctionGen)

  implicit val configErrorCogen: Cogen[ConfigError] =
    Cogen[List[String]].contramap(_.messages.toList)

  def configEntryGen[A](gen: Gen[A]): Gen[ConfigEntry[A]] = {
    val default =
      for {
        error <- configErrorGen
        a <- gen
      } yield ConfigEntry.Default(error, a)

    val failed =
      for {
        error <- configErrorGen
      } yield ConfigEntry.Failed(error)

    val loaded =
      for {
        error <- configErrorGen
        key <- Gen.option(configKeyGen)
        a <- gen
      } yield ConfigEntry.Loaded(error, key, a)

    Gen.oneOf(default, failed, loaded)
  }

  implicit def configEntryArbitrary[A: Arbitrary]: Arbitrary[ConfigEntry[A]] =
    Arbitrary(configEntryGen(arbitrary[A]))

  def configEntryFunctionGen[A](gen: Gen[A => A]): Gen[ConfigEntry[A] => ConfigEntry[A]] =
    gen.map(f => (entry: ConfigEntry[A]) => entry.map(f))

  implicit def configEntryFunctionArbitrary[A](
    implicit arb: Arbitrary[A => A]
  ): Arbitrary[ConfigEntry[A] => ConfigEntry[A]] =
    Arbitrary(configEntryFunctionGen(arbitrary[A => A]))

  def configValueGen[F[_], A](gen: Gen[A]): Gen[ConfigValue[F, A]] =
    Gen.frequency(
      3 -> configEntryGen(gen).map(ConfigValue.pure),
      1 -> Gen.const(ConfigValue.suspend[F, A](throw ConfigException(ConfigError.Empty)))
    )

  implicit def configValueArbitrary[F[_], A: Arbitrary]: Arbitrary[ConfigValue[F, A]] =
    Arbitrary(configValueGen(arbitrary[A]))

  def configValueParGen[F[_], A](gen: Gen[A]): Gen[ConfigValue.Par[F, A]] =
    configValueGen(gen).map(ConfigValue.Par(_))

  implicit def configValueParArbitrary[F[_], A: Arbitrary]: Arbitrary[ConfigValue.Par[F, A]] =
    Arbitrary(configValueParGen(arbitrary[A]))

  def secretGen[A: Show](gen: Gen[A]): Gen[Secret[A]] =
    gen.map(Secret(_))

  implicit def secretArbitrary[A: Arbitrary: Show]: Arbitrary[Secret[A]] =
    Arbitrary(secretGen(arbitrary[A]))

  def secretFunctionGen[A: Show](gen: Gen[A => A]): Gen[Secret[A] => Secret[A]] =
    gen.map(f => (secret: Secret[A]) => Secret(f(secret.value)))

  implicit def secretFunctionArbitrary[A](
    implicit arb: Arbitrary[A => A],
    show: Show[A]
  ): Arbitrary[Secret[A] => Secret[A]] =
    Arbitrary(secretFunctionGen(arbitrary[A => A]))

  def configExceptionGen(gen: Gen[ConfigError]): Gen[ConfigException] =
    gen.map(ConfigException(_))

  implicit def configExceptionArbitrary(
    implicit arb: Arbitrary[ConfigError]
  ): Arbitrary[ConfigException] =
    Arbitrary(configExceptionGen(arb.arbitrary))

  def configExceptionFunctionGen(
    gen: Gen[ConfigError => ConfigError]
  ): Gen[ConfigException => ConfigException] =
    gen.map(f => (exception: ConfigException) => ConfigException(f(exception.error)))

  implicit def configExceptionFunctionArbitrary(
    implicit arb: Arbitrary[ConfigError => ConfigError]
  ): Arbitrary[ConfigException => ConfigException] =
    Arbitrary(configExceptionFunctionGen(arb.arbitrary))

  def configDecoderGen[A, B](gen: Gen[A => Either[ConfigError, B]]): Gen[ConfigDecoder[A, B]] =
    gen.map(f => ConfigDecoder.instance((_, a) => f(a)))

  implicit def configDecoderArbitrary[A, B](
    implicit arb: Arbitrary[A => Either[ConfigError, B]]
  ): Arbitrary[ConfigDecoder[A, B]] =
    Arbitrary(configDecoderGen(arbitrary[A => Either[ConfigError, B]]))
}
