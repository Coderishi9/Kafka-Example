name := "kafka"

version := "0.1"

scalaVersion := "2.12.10"

libraryDependencies += "com.typesafe" % "config" % "1.3.2"
libraryDependencies += "org.apache.kafka" % "kafka-clients" % "2.4.1"
libraryDependencies += "org.apache.spark" %% "spark-streaming-kafka-0-10" % "2.4.1"
libraryDependencies += "org.apache.spark" %% "spark-streaming" % "2.4.1"
libraryDependencies += "org.apache.kafka" %% "kafka" % "2.4.1"
// https://mvnrepository.com/artifact/org.apache.spark/spark-sql
libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.4.1"
// https://mvnrepository.com/artifact/org.apache.spark/spark-sql-kafka-0-10
libraryDependencies += "org.apache.spark" %% "spark-sql-kafka-0-10" % "2.4.1" % "provided"

