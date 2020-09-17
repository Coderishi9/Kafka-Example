package KefkaSparkStreaming

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
object StreamConsumer {

  def main(args: Array[String]): Unit = {
    val kafkaParams = Map[String,Object](
      ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> "localhost:9092",
      ConsumerConfig.AUTO_OFFSET_RESET_CONFIG -> "earliest",
      ConsumerConfig.GROUP_ID_CONFIG -> "New-Group-For-Every-Stream",
      ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer],
      ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer]
    )
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("KafkaStreaming")
    val ssc = new StreamingContext(sparkConf, Seconds(5))
    val sc = ssc.sparkContext
    sc.setLogLevel("OFF")
    val topicSet = Set("testRun")
    val message = KafkaUtils.createDirectStream[String,String](
      ssc,
      LocationStrategies.PreferConsistent,
      ConsumerStrategies.Subscribe[String,String](topicSet,kafkaParams)
    )

    val words = message.map(record => (s"File Name: ${record.key()}, No. of words in file: ${record.value().length}"))
    words.print()

    ssc.start()
    ssc.stop(true,true)
  }

}
