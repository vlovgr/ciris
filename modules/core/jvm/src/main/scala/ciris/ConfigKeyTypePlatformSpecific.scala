package ciris

import java.io.{File => JFile}
import java.nio.charset.Charset

private[ciris] trait ConfigKeyTypePlatformSpecific {

  /**
    * [[ConfigKeyType]] for a file of type `File` with charset `Charset`.
    */
  val File: ConfigKeyType[(JFile, Charset)] = ConfigKeyType("file")
}
