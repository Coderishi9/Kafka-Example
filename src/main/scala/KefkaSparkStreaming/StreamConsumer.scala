package KefkaSparkStreaming
import org.json.JSONObject
import com.sksamuel.elastic4s.ElasticApi.{RichFuture, indexInto}
import com.sksamuel.elastic4s.ElasticDsl.IndexHandler
import com.sksamuel.elastic4s.{ElasticClient, ElasticProperties}
import com.sksamuel.elastic4s.http.JavaClient
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


    val mmsg = KafkaUtils.createDirectStream[String,String](
      ssc,
      LocationStrategies.PreferConsistent,
      ConsumerStrategies.Subscribe[String,String](Set("testdata"),kafkaParams)
    )

    val esClient =  ElasticClient(JavaClient(ElasticProperties(s"http://${sys.env.getOrElse("ES_HOST", "127.0.0.1")}:${sys.env.getOrElse("ES_PORT", "9200")}")))


    val jsonObject:JSONObject = new JSONObject("{hello: hey}")
    val word = mmsg.map(record => {
      val res = esClient.execute{
        indexInto("testing1234").doc(jsonObject.toString)
      }.await
      println(res)
    }
    )

    word.print()


    esClient.close()
    ssc.start()
    ssc.awaitTermination()
  }

}
