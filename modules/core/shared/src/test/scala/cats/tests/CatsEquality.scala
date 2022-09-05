/*
 * Copyright (c) 2019 Typelevel
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package cats
package tests

import org.scalactic._
import scala.language.implicitConversions
import TripleEqualsSupport.AToBEquivalenceConstraint
import TripleEqualsSupport.BToAEquivalenceConstraint

// The code in this file was taken and only slightly modified from
// https://github.com/bvenners/equality-integration-demo
// Thanks for the great examples, Bill!

final class CatsEquivalence[T](T: Eq[T]) extends Equivalence[T] {
  def areEquivalent(a: T, b: T): Boolean = T.eqv(a, b)
}

trait LowPriorityStrictCatsConstraints extends TripleEquals {
  implicit def lowPriorityCatsCanEqual[A, B](implicit B: Eq[B], ev: A <:< B): CanEqual[A, B] =
    new AToBEquivalenceConstraint[A, B](new CatsEquivalence(B), ev)
}

trait StrictCatsEquality extends LowPriorityStrictCatsConstraints {
  override def convertToEqualizer[T](left: T): Equalizer[T] = super.convertToEqualizer[T](left)
  implicit override def convertToCheckingEqualizer[T](left: T): CheckingEqualizer[T] =
    new CheckingEqualizer(left)
  override def unconstrainedEquality[A, B](implicit equalityOfA: Equality[A]): CanEqual[A, B] =
    super.unconstrainedEquality[A, B]
  implicit def catsCanEqual[A, B](implicit A: Eq[A], ev: B <:< A): CanEqual[A, B] =
    new BToAEquivalenceConstraint[A, B](new CatsEquivalence(A), ev)
}

object StrictCatsEquality extends StrictCatsEquality
