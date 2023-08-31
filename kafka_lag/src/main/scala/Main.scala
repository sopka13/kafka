import org.apache.kafka.clients.admin.{AdminClient, AdminClientConfig}
import org.apache.kafka.clients.consumer.{ConsumerConfig, KafkaConsumer}
import org.apache.kafka.common.TopicPartition

import java.util.Properties
import scala.jdk.CollectionConverters.{SeqHasAsJava, ListHasAsScala}

object Main extends App {
  val groupId = "group_5"
  val topicName = "text_topic"
  private val adminProperties = new Properties()
  adminProperties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
  adminProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
  adminProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")

  val admin = AdminClient.create(adminProperties)

  private val consumerOffsetsForGroup = admin.listConsumerGroupOffsets(groupId).partitionsToOffsetAndMetadata().get()
  println(s"result = ${consumerOffsetsForGroup.toString}")
  consumerOffsetsForGroup.forEach((a, b) => println(s"${a.topic()} ${a.partition()}: ${b.offset()}"))
  println()

  adminProperties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId)
  val consumer = new KafkaConsumer(adminProperties)
  // Get partitions
  private val tmp = consumer.partitionsFor(topicName)
  val partitions = tmp.asScala
  // Create list
  val tpList = partitions.map(elem => new TopicPartition(elem.topic(), elem.partition())).toList
  println(s"tpList size = ${tpList.size}")

  try {
    // Subscribe
    consumer.subscribe(List(topicName).asJava)
    val endOffset = consumer.endOffsets(tpList.asJava)
    val startOffset = consumer.beginningOffsets(tpList.asJava)
    println(s"start:")
    startOffset.forEach((a, b) => println(s"  ${a.topic()} ${a.partition()}: $b"))
    println(s"end:")
    endOffset.forEach((a, b) => println(s"  ${a.topic()} ${a.partition()}: $b"))
  } finally {
    consumer.unsubscribe()
    consumer.close()
  }
}
