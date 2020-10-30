package KefkaSparkStreaming

import java.io
import java.io.File
import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, Producer, ProducerRecord}

import scala.io.Source


object Producer {

  val props = new Properties()
  props.put("bootstrap.servers", "localhost:9092")
  props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

  val producer = new KafkaProducer[String, String](props)

  def getListOfFiles(dir: String):List[File] = {
    val d = new File(dir)
    if (d.exists && d.isDirectory) {
      d.listFiles.filter(_.isFile).toList
    } else {
      List[File]()
    }
  }

  def main(args: Array[String]): Unit = {
    val dir = "src/main/resources"
    val files = getListOfFiles(dir)
    val f2 = getListOfFiles("src/main/resources/abc")

    for(i <- f2){
      val contents = Source.fromFile(i).getLines.mkString
      val key = i.getName
      val record = new ProducerRecord("ishwar", key, contents)
      producer.send(record)
    }

    producer.close()



  }

}
