import com.typesafe.config.ConfigFactory
import configurations.ConsumerConfiguration
import org.apache.kafka.clients.consumer.{ConsumerConfig, KafkaConsumer}
import org.apache.kafka.common.TopicPartition

import java.time.Duration
import java.util.Properties
import scala.jdk.CollectionConverters.{IterableHasAsScala, SeqHasAsJava}

object KafkaConsumerApp extends App {
  val config = ConsumerConfiguration(ConfigFactory.load("dev.conf"))
  val property: Properties = new Properties()
  property.put(ConsumerConfig.GROUP_ID_CONFIG, config.groupId)
  property.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, config.brokers)

  property.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
  property.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
  property.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 2)
  property.put("enable.auto.commit", "true")
  property.put("auto.commit.interval.ms", "1000")

  val consumer = new KafkaConsumer(property)
  val topic = config.topicName
  val topicName_0 = "src_test_topic"
  val topicName_1 = "dst_test_topic"

  try {
    consumer.assign(List(new TopicPartition(topicName_1, 0)).asJava)
    consumer.seekToBeginning(List(new TopicPartition(topicName_1, 0)).asJava)
    while (true) {
      val records = consumer.poll(Duration.ofMillis(100)).asScala
      for (record <- records) {
        println(s"Message: key: ${record.key()}; value: ${record.value()}")
      }
    }
  } finally {
    consumer.close()
  }
}
