ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

lazy val root = (project in file("."))
  .settings(
    name := "kafka_first_project"
  )

libraryDependencies ++= Seq(
  "org.apache.kafka"  % "kafka-clients" % "3.5.0",
  "com.typesafe"      % "config"        % "1.4.2"
)
