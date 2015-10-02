package dw.job.analytics.log

import scala.math.random
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import java.util.concurrent.atomic.AtomicInteger
import scala.collection.mutable.HashSet
import scala.concurrent.Future
import scala.concurrent.impl.Future
import scala.util.control.Breaks._
import java.io.File

object AccessLogParser {

  val conf = new SparkConf()
    .setMaster("local")
    .setAppName("PlayViewBatchAnalytics")
    .set("spark.executor.memory", "1g")
  val sc = new SparkContext(conf)
  val sqlContext = new org.apache.spark.sql.SQLContext(sc)

  // this is used to implicitly convert an RDD to a DataFrame.
  import sqlContext.implicits._

  def main(args: Array[String]): Unit = {
    val logFile = "file:///D:/data/nguyentantrieu.info-Nov-2013.txt";
    val logData = sc.textFile(logFile, 2).cache()

    val words = logData.foreach({ line =>
      {
        val toks = line.split(" ")
        println(toks.apply(0))

      }
    })

  }
}