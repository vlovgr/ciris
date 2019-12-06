package ciris

import cats.effect.Blocker
import ciris.{ConfigException => _}
import com.typesafe.config.ConfigFactory
import com.typesafe.{config => tsc}

package object hocon extends HoconConfigDecoders {

  sealed abstract class HoconAt(configV: ConfigValue[tsc.Config]) {

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
    new HoconAt(ConfigValue.suspend {
      val _config = config
      ConfigValue.default(_config)
    }) {}

  def hoconLoad(blocker: Blocker): HoconAt =
    new HoconAt(ConfigValue.blockOn(blocker)(ConfigValue.suspend {
      val _config = ConfigFactory.load()
      ConfigValue.default(_config)
    })) {}
}
