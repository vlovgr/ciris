package ciris

import java.io.{File => JFile}
import java.nio.charset.Charset

private[ciris] trait ConfigKeyTypePlatformSpecific {
  val File: ConfigKeyType[(JFile, Charset)] = ConfigKeyType("file")
}
