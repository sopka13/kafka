import com.typesafe.config.ConfigFactory
import configurations.ProducerConfiguration
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import org.apache.kafka.common.utils.Utils.sleep

import java.util.Properties

object KafkaProducerApp extends App {
  val config = ProducerConfiguration(ConfigFactory.load("dev.conf"))
  val property: Properties = new Properties()
  property.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, config.brokers)
  property.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
  property.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
  property.put("acks","all")

  val topic = config.topicName

  val producer = new KafkaProducer[String, String](property)

  try {
    for (i <- 0 to 100000000) {
      println(s"send message number $i")
      producer.send(new ProducerRecord(topic, s"message $i", "value"))
      sleep(1000)
    }
  }finally {
    producer.flush()
    producer.close()
  }
}