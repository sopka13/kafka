package configurations

import com.typesafe.config.Config

case class ProducerConfiguration(brokers: String,
                                 topicName: String,
                                 timeout: Long)

object ProducerConfiguration {
  def apply(config: Config): ProducerConfiguration = ProducerConfiguration(
    brokers =   config.getString("kafka.src.brokers"),
    topicName = config.getString("kafka.src.topicName"),
    timeout =   config.getLong  ("kafka.src.timeout")
  )
}
