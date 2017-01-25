package dw.test

import org.apache.spark.SparkContext
import org.apache.spark.SparkConf

object SimpleOpsWithSpark {

  def main(args: Array[String]) {
    val conf = new SparkConf()
      .setMaster("local")
      .setAppName("PlayViewBatchAnalytics")
      .set("spark.executor.memory", "1g")
    val sc = new SparkContext(conf)
    val words = Array("one", "two", "one", "one", "one", "two", "two", "one", "one", "two", "two")
    val wordsPairRdd = sc.parallelize(words).map(w => (w, 1))

    wordsPairRdd.groupByKey().map(t => (t._1, t._2.sum))
    wordsPairRdd.reduceByKey(_ + _)
    println(wordsPairRdd)
  }
}