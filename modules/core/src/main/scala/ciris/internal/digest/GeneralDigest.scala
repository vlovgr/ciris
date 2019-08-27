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

private[digest] abstract class GeneralDigest {
  private val xBuf: Array[Byte] = new Array(4)
  private var xBufOff: Int = 0

  private var byteCount: Long = 0L

  def update(in: Byte): Unit = {
    xBuf(xBufOff) = in
    xBufOff += 1

    if (xBufOff == xBuf.length) {
      processWord(xBuf, 0)
      xBufOff = 0
    }

    byteCount += 1
  }

  def update(in: Array[Byte], inOff: Int, len: Int): Unit = {
    val length = Math.max(0, len)

    //
    // fill the current word
    //
    var i = 0
    var done = false
    if (xBufOff != 0) {
      while (!done && i < length) {
        xBuf(xBufOff) = in(inOff + i)
        xBufOff += 1
        i += 1

        if (xBufOff == 4) {
          processWord(xBuf, 0)
          xBufOff = 0
          done = true
        }
      }
    }

    //
    // process whole words.
    //
    val limit = ((length - i) & ~3) + i
    while (i < limit) {
      processWord(in, inOff + i)
      i += 4
    }

    //
    // load in the remainder.
    //
    while (i < length) {
      xBuf(xBufOff) = in(inOff + i)
      xBufOff += 1
      i += 1
    }

    byteCount += length
  }

  def finish(): Unit = {
    val bitLength = byteCount << 3
    //
    // add the pad bytes.
    //
    update(128.toByte)
    while (xBufOff != 0) update(0.toByte)
    processLength(bitLength)
    processBlock()
  }

  def reset(): Unit = {
    byteCount = 0
    xBufOff = 0
    var i = 0
    while (i < xBuf.length) {
      xBuf(i) = 0
      i += 1
    }
  }

  protected def processWord(in: Array[Byte], inOff: Int): Unit

  protected def processLength(bitLength: Long): Unit

  protected def processBlock(): Unit
}
