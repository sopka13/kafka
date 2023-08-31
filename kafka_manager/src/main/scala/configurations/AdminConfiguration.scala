package configurations

import com.typesafe.config.Config

case class AdminConfiguration(brokers: String,
                              timeout: Long)

object AdminConfiguration {
  def apply(config: Config): AdminConfiguration = AdminConfiguration(
    brokers = config.getString("kafka.brokers"),
    timeout = config.getLong("kafka.timeout")
  )
}
