import com.typesafe.config.ConfigFactory
import configurations.{AdminConfiguration, ConsumerConfiguration}
import org.apache.kafka.clients.admin.{AdminClientConfig, AdminClient, NewTopic}
import org.apache.kafka.common.KafkaFuture

import java.util.Arrays.asList
import java.util.Properties
import java.util.concurrent.TimeUnit
import scala.jdk.CollectionConverters._
import scala.util.{Failure, Success, Try}

object KafkaAdminApp extends App {
  val adminConfig = AdminConfiguration(ConfigFactory.load("dev.conf"))
  val consumerConfig = ConsumerConfiguration(ConfigFactory.load("dev.conf"))
  val properties: Properties = new Properties()
  properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, adminConfig.brokers)

  val admin  = AdminClient.create(properties)

  println(s"Topic names: $topicNameList")

  println(s"Topic offsets: $offsets")

  // Create topic
  private val topicName:          String =  "newTopic"
  private val partitionsCount:    Int =     3
  private val replicationFactor:  Short =   1
  private val retentionConf       =         Map("retention.ms" -> "100").asJava

  val newTopic = new NewTopic(topicName, partitionsCount, replicationFactor).configs(retentionConf)
  if (!topicNameList.contains(topicName))
    returnKafkaFutureResult(admin.createTopics(asList(newTopic)).all())
  println(s"Topic names: $topicNameList")

  // Delete topic
  if (topicNameList.contains(topicName))
    returnKafkaFutureResult(admin.deleteTopics(asList(topicName)).all())
  println(s"Topic names: $topicNameList")


  // Get topic name list
  private def topicNameList = returnKafkaFutureResult(admin.listTopics.names()).asScala

  // Get offsets for topic
  private def offsets =
    returnKafkaFutureResult(admin.listConsumerGroupOffsets(consumerConfig.groupId).partitionsToOffsetAndMetadata())
      .asScala
      .collect { case (partition, metadata) if partition.topic() == consumerConfig.topicName => partition.partition() -> metadata.offset() }
      .toMap

  private def returnKafkaFutureResult[T](future: KafkaFuture[T]): T = {
    Try(future.get(adminConfig.timeout, TimeUnit.MILLISECONDS)) match {
      case Success(res) => res
      case Failure(exception) => throw new Exception(s"Error with Kafka: ${exception.getMessage}")
    }
  }
}
