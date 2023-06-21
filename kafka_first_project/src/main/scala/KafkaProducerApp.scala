import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

import java.util.Properties

object KafkaProducerApp extends App {

  val properties: Properties = new Properties()
  properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  properties.put("bootstrap.servers","localhost:9092")
  properties.put("acks","all")

  val topic = "text_topic"

  val producer = new KafkaProducer[String, String](properties)

  try {
    for (i <- 0 to 10)
      producer.send(new ProducerRecord(topic, s"$i", "this is value"))
  }finally {
    producer.flush()
    producer.close()
  }

}