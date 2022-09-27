/*
 * Copyright 2017-2022 Viktor Lövgren
 *
 * SPDX-License-Identifier: MIT
 */

package ciris

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import cats.Eq
import cats.laws.discipline.{ApplyTests, FlatMapTests, NonEmptyParallelTests}
import cats.syntax.all._

final class ConfigValueLawsSpec extends DisciplineSuite with Generators {
  implicit def configValueEq[A](implicit eq: Eq[A]): Eq[ConfigValue[IO, A]] =
    Eq.instance { (v1, v2) =>
      val a1 = v1.to[IO].use(IO.pure).attempt.unsafeRunSync()
      val a2 = v2.to[IO].use(IO.pure).attempt.unsafeRunSync()

      (a1, a2) match {
        case (Left(e1: ConfigException), Left(e2: ConfigException)) => e1 === e2
        case (Right(e1), Right(e2))                                 => e1 === e2
        case (_, _)                                                 => false
      }
    }

  implicit def configValueParEq[A](implicit eq: Eq[A]): Eq[ConfigValue.Par[IO, A]] =
    Eq.by(_.unwrap)

  checkAll(
    "ConfigValue",
    FlatMapTests[ConfigValue[IO, *]].flatMap[String, String, String]
  )

  checkAll(
    "ConfigValue.Par",
    ApplyTests[ConfigValue.Par[IO, *]].apply[String, String, String]
  )

  checkAll(
    "ConfigValue",
    NonEmptyParallelTests[ConfigValue[IO, *]].nonEmptyParallel[String, String]
  )
}
