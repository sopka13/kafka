import KafkaProducerApp.producer
import org.apache.kafka.clients.consumer.{ConsumerConfig, KafkaConsumer}
import org.apache.kafka.common.TopicPartition

import java.awt.print.PrinterIOException
import java.time.Duration
import java.util.Collections.singletonList
import java.util.Properties
import java.util.concurrent.TimeUnit
import scala.jdk.CollectionConverters.{IterableHasAsScala, SeqHasAsJava}

object KafkaConsumerSubscribeApp extends App {

  val props:Properties = new Properties()
  props.put(ConsumerConfig.GROUP_ID_CONFIG, "group_5")
  props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092")
  props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
  props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
  props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 2)
  props.put("enable.auto.commit", "true")
  props.put("auto.commit.interval.ms", "1000")

  val consumer = new KafkaConsumer(props)
  val topic = "text_topic"

  try {
    consumer.assign(List(new TopicPartition(topic, 0)).asJava)
    consumer.seekToBeginning(List(new TopicPartition(topic, 0)).asJava)
    while (true) {
      val firstOffset = consumer.position(new TopicPartition(topic, 0))
      println(s"firstOffset = $firstOffset")
      if (firstOffset == 10) throw new Exception()
      val records = consumer.poll(Duration.ofMillis(100)).asScala
      for (record <- records) {
        println(s"Message: key: ${record.key()}; value: ${record.value()}")
      }
//      records.iterator().forEachRemaining { record =>
//        println(s"""
//             |message
//             |  offset=${record.offset}
//             |  partition=${record.partition}
//             |  key=${record.key}
//             |  value=${record.value}
//             |  schema=${record.value().getClass}
//             """.stripMargin)
//      }
    }
  } finally {
    consumer.close()
  }
}
