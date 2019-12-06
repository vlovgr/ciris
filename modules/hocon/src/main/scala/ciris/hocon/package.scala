package ciris

import cats.Show
import ciris.{ConfigException => _, _}
import com.typesafe.{config => tsc}
import cats.syntax.all

import scala.util.Try

package object hocon extends HoconConfigDecoders {

  final class HoconAt(configV: ConfigValue[tsc.Config]) {

    def apply(name: String): ConfigValue[tsc.ConfigValue] =
      ConfigValue.suspend {
        val key = ConfigKey(s"hocon key $name")
        configV.flatMap { config =>
          try {
            val value = config.getValue(name)
            ConfigValue.loaded(key, value)
          } catch {
            case _: tsc.ConfigException.Missing => ConfigValue.missing(key)
          }
        }
      }

  }

  def hoconConfig(config: => tsc.Config): HoconAt =
    new HoconAt(ConfigValue.suspend(ConfigValue.default(config)))

  def hoconLoad(): HoconAt = hoconConfig(tsc.ConfigFactory.load())
}
