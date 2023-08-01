package configurations

import com.typesafe.config.Config

case class ConsumerConfiguration(brokers: String,
                                 groupId: String,
                                 topicName: String,
                                 timeout: Long)

object ConsumerConfiguration {
  def apply(config: Config): ConsumerConfiguration = ConsumerConfiguration(
    brokers =   config.getString("kafka.brokers"),
    groupId =   config.getString("kafka.groupId"),
    topicName = config.getString("kafka.topicName"),
    timeout =   config.getLong  ("kafka.timeout")
  )
}
