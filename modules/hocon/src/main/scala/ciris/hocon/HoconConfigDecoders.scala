package ciris.hocon

import cats.Show
import ciris.ConfigDecoder
import com.typesafe.config.{ConfigList, ConfigObject}
import com.typesafe.config.impl.AbstractConfigObject
import com.typesafe.{config => tsc}
import scala.collection.JavaConverters._

import scala.concurrent.duration.Duration

trait HoconConfigDecoders {

  implicit def configValueShow: Show[tsc.ConfigValue] = Show.show(_.render())

  implicit val stringDecoder: ConfigDecoder[tsc.ConfigValue, String] =
    ConfigDecoder.identity[tsc.ConfigValue].collect("String") {
      case cv if (cv.valueType() == tsc.ConfigValueType.STRING) =>
        cv.unwrapped().asInstanceOf[String]
    }

  implicit val booleanDecoder: ConfigDecoder[tsc.ConfigValue, Boolean] =
    ConfigDecoder.identity[tsc.ConfigValue].collect("Boolean") {
      case cv if (cv.valueType() == tsc.ConfigValueType.BOOLEAN) =>
        cv.unwrapped().asInstanceOf[Boolean]
    }

  implicit val numberDecoder: ConfigDecoder[tsc.ConfigValue, Number] =
    ConfigDecoder.identity[tsc.ConfigValue].collect("Number") {
      case cv if (cv.valueType() == tsc.ConfigValueType.NUMBER) =>
        cv.unwrapped().asInstanceOf[Number]
    }

  implicit val durationDecoder: ConfigDecoder[tsc.ConfigValue, Duration] =
    ConfigDecoder.identity[tsc.ConfigValue].collect("Duration") {
      case cv if (cv.valueType() == tsc.ConfigValueType.STRING) =>
        val str = cv.unwrapped().asInstanceOf[String]
        Duration(str) // can throw, do we need to handle explicitly?
    }

  implicit val configDecoder: ConfigDecoder[tsc.ConfigValue, tsc.Config] =
    ConfigDecoder.identity[tsc.ConfigValue].collect("Config") {
      case cv if (cv.valueType() == tsc.ConfigValueType.OBJECT) =>
        cv.unwrapped().asInstanceOf[ConfigObject].toConfig
    }

  implicit val configListDecoder: ConfigDecoder[tsc.ConfigValue, List[tsc.ConfigValue]] =
    ConfigDecoder.identity[tsc.ConfigValue].collect("Config") {
      case cv if (cv.valueType() == tsc.ConfigValueType.LIST) =>
        cv.unwrapped().asInstanceOf[ConfigList].asScala.toList
    }
}
