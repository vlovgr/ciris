/*
 * Copyright 2017-2023 Viktor Rudebeck
 *
 * SPDX-License-Identifier: MIT
 */

package ciris

import munit.ScalaCheckSuite
import org.typelevel.discipline.Laws

trait DisciplineSuite extends ScalaCheckSuite {
  def checkAll(typeName: String, rules: Laws#RuleSet): Unit =
    rules.all.properties.foreach { case (name, prop) =>
      property(s"$typeName.$name")(prop)
    }
}
