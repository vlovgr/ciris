package ciris

import java.io.{FileWriter, File => JFile}
import java.nio.charset.Charset

class JvmPropertySpec extends PropertySpec {
  def withFile[T](fileContents: String)(f: (JFile, Charset) => T): T = {
    val file = JFile.createTempFile("ciris-", ".test")
    file.delete()
    file.deleteOnExit()

    val writer = new FileWriter(file)
    try { writer.write(fileContents) } finally { writer.close() }
    try { f(file, Charset.defaultCharset) } finally { file.delete(); () }
  }
}
