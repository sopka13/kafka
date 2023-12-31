ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.11"

lazy val root = (project in file("."))
  .settings(
    name := "Kafka_lag"
  )

libraryDependencies ++= Seq(
  "org.apache.kafka" % "kafka-clients" % "3.5.0"
)