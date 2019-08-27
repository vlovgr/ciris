package ciris.internal

import ciris.internal

import scala.annotation.tailrec

package object digest {
  private[this] val hexChars: Array[Char] =
    Array('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')

  private[ciris] def sha1Hex(s: String): String = {
    def sha1(bytes: Array[Byte]): Array[Byte] = {
      val digest = new internal.digest.SHA1Digest()
      val hash = new Array[Byte](digest.digestSize)
      digest.update(bytes, 0, bytes.length)
      digest.doFinal(hash, 0)
      hash
    }

    def hex(in: Array[Byte]): Array[Char] = {
      val length = in.length

      @tailrec def encode(out: Array[Char], i: Int, j: Int): Array[Char] = {
        if (i < length) {
          out(j) = hexChars((0xf0 & in(i)) >>> 4)
          out(j + 1) = hexChars(0x0f & in(i))
          encode(out, i + 1, j + 2)
        } else out
      }

      encode(new Array(length << 1), 0, 0)
    }

    new String(hex(sha1(s.getBytes("UTF-8"))))
  }
}
