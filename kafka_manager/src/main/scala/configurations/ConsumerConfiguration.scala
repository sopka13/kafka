package configurations

import com.typesafe.config.Config

case class ConsumerConfiguration(brokers: String,
                                 groupId: String,
                                 topicName: String,
                                 timeout: Long)

object ConsumerConfiguration {
  def apply(config: Config): ConsumerConfiguration = ConsumerConfiguration(
    brokers =   config.getString("kafka.dst.brokers"),
    groupId =   config.getString("kafka.dst.groupId"),
    topicName = config.getString("kafka.dst.topicName"),
    timeout =   config.getLong  ("kafka.dst.timeout")
  )
}
