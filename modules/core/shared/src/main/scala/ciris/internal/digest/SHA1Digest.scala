/*
 * Copyright (c) 2000-2018 The Legion of the Bouncy Castle Inc. (http://www.bouncycastle.org)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package ciris.internal.digest

import ciris.internal.digest.SHA1Digest._

private[ciris] final class SHA1Digest extends GeneralDigest {
  private var H1, H2, H3, H4, H5: Int = 0

  private val X: Array[Int] = new Array(80)
  private var xOff: Int = 0

  reset()

  def digestSize: Int =
    digestLength

  override protected def processWord(in: Array[Byte], inOff: Int): Unit = {
    // Note: Inlined for performance
    //       X(xOff) = Pack.bigEndianToInt(in, inOff)
    var off = inOff
    var n = in(off) << 24
    n |= (in({ off += 1; off }) & 0xff) << 16
    n |= (in({ off += 1; off }) & 0xff) << 8
    n |= (in({ off += 1; off }) & 0xff)
    X(xOff) = n
    xOff += 1
    if (xOff == 16) processBlock()
  }

  override protected def processLength(bitLength: Long): Unit = {
    if (xOff > 14) processBlock()
    X(14) = (bitLength >>> 32).toInt
    X(15) = bitLength.toInt
  }

  def doFinal(out: Array[Byte], outOff: Int): Int = {
    finish()

    Pack.intToBigEndian(H1, out, outOff)
    Pack.intToBigEndian(H2, out, outOff + 4)
    Pack.intToBigEndian(H3, out, outOff + 8)
    Pack.intToBigEndian(H4, out, outOff + 12)
    Pack.intToBigEndian(H5, out, outOff + 16)

    reset()

    SHA1Digest.digestLength
  }

  override def reset(): Unit = {
    super.reset()

    H1 = 0x67452301
    H2 = 0xefcdab89
    H3 = 0x98badcfe
    H4 = 0x10325476
    H5 = 0xc3d2e1f0

    xOff = 0
    var i = 0
    while (i != X.length) {
      X(i) = 0
      i += 1
    }
  }

  private def f(u: Int, v: Int, w: Int): Int =
    (u & v) | (~u & w)

  private def h(u: Int, v: Int, w: Int): Int =
    u ^ v ^ w

  private def g(u: Int, v: Int, w: Int): Int =
    (u & v) | (u & w) | (v & w)

  override protected def processBlock(): Unit = {
    //
    // expand 16 word block into 80 word block.
    //
    var i = 16
    while (i < 80) {
      val t = X(i - 3) ^ X(i - 8) ^ X(i - 14) ^ X(i - 16)
      X(i) = t << 1 | t >>> 31
      i += 1
    }

    //
    // set up working variables.
    //
    var A = H1
    var B = H2
    var C = H3
    var D = H4
    var E = H5

    //
    // round 1
    //
    var idx = 0

    var j = 0
    while (j < 4) {
      // E = rotateLeft(A, 5) + f(B, C, D) + E + X[idx++] + Y1
      // B = rotateLeft(B, 30)
      E += (A << 5 | A >>> 27) + f(B, C, D) + X(idx) + Y1
      B = B << 30 | B >>> 2
      idx += 1

      D += (E << 5 | E >>> 27) + f(A, B, C) + X(idx) + Y1
      A = A << 30 | A >>> 2
      idx += 1

      C += (D << 5 | D >>> 27) + f(E, A, B) + X(idx) + Y1
      E = E << 30 | E >>> 2
      idx += 1

      B += (C << 5 | C >>> 27) + f(D, E, A) + X(idx) + Y1
      D = D << 30 | D >>> 2
      idx += 1

      A += (B << 5 | B >>> 27) + f(C, D, E) + X(idx) + Y1
      C = C << 30 | C >>> 2
      idx += 1

      j += 1
    }

    //
    // round 2
    //
    j = 0
    while (j < 4) {
      // E = rotateLeft(A, 5) + h(B, C, D) + E + X[idx++] + Y2
      // B = rotateLeft(B, 30)
      E += (A << 5 | A >>> 27) + h(B, C, D) + X(idx) + Y2
      B = B << 30 | B >>> 2
      idx += 1

      D += (E << 5 | E >>> 27) + h(A, B, C) + X(idx) + Y2
      A = A << 30 | A >>> 2
      idx += 1

      C += (D << 5 | D >>> 27) + h(E, A, B) + X(idx) + Y2
      E = E << 30 | E >>> 2
      idx += 1

      B += (C << 5 | C >>> 27) + h(D, E, A) + X(idx) + Y2
      D = D << 30 | D >>> 2
      idx += 1

      A += (B << 5 | B >>> 27) + h(C, D, E) + X(idx) + Y2
      C = C << 30 | C >>> 2
      idx += 1

      j += 1
    }

    //
    // round 3
    //
    j = 0
    while (j < 4) {
      // E = rotateLeft(A, 5) + g(B, C, D) + E + X[idx++] + Y3
      // B = rotateLeft(B, 30)
      E += (A << 5 | A >>> 27) + g(B, C, D) + X(idx) + Y3
      B = B << 30 | B >>> 2
      idx += 1

      D += (E << 5 | E >>> 27) + g(A, B, C) + X(idx) + Y3
      A = A << 30 | A >>> 2
      idx += 1

      C += (D << 5 | D >>> 27) + g(E, A, B) + X(idx) + Y3
      E = E << 30 | E >>> 2
      idx += 1

      B += (C << 5 | C >>> 27) + g(D, E, A) + X(idx) + Y3
      D = D << 30 | D >>> 2
      idx += 1

      A += (B << 5 | B >>> 27) + g(C, D, E) + X(idx) + Y3
      C = C << 30 | C >>> 2
      idx += 1

      j += 1
    }

    //
    // round 4
    //
    j = 0
    while (j <= 3) {
      // E = rotateLeft(A, 5) + h(B, C, D) + E + X[idx++] + Y4
      // B = rotateLeft(B, 30)
      E += (A << 5 | A >>> 27) + h(B, C, D) + X(idx) + Y4
      B = B << 30 | B >>> 2
      idx += 1

      D += (E << 5 | E >>> 27) + h(A, B, C) + X(idx) + Y4
      A = A << 30 | A >>> 2
      idx += 1

      C += (D << 5 | D >>> 27) + h(E, A, B) + X(idx) + Y4
      E = E << 30 | E >>> 2
      idx += 1

      B += (C << 5 | C >>> 27) + h(D, E, A) + X(idx) + Y4
      D = D << 30 | D >>> 2
      idx += 1

      A += (B << 5 | B >>> 27) + h(C, D, E) + X(idx) + Y4
      C = C << 30 | C >>> 2
      idx += 1

      j += 1
    }

    H1 += A
    H2 += B
    H3 += C
    H4 += D
    H5 += E

    //
    // reset start of the buffer.
    //
    xOff = 0
    i = 0
    while (i < 16) {
      X(i) = 0
      i += 1
    }
  }
}

private object SHA1Digest {
  val digestLength: Int = 20

  //
  // Additive constants
  //
  val Y1 = 0x5a827999
  val Y2 = 0x6ed9eba1
  val Y3 = 0x8f1bbcdc
  val Y4 = 0xca62c1d6
}
