package dw.job.playview

import scala.math.random
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import java.util.concurrent.atomic.AtomicInteger
import scala.collection.mutable.HashSet
import scala.concurrent.Future

import scala.util.control.Breaks._
import java.io.File
import dw.job.click.LogUtil
// 

/** Computes an approximation to pi */
object PlayviewDataJob {

  val conf = new SparkConf()
    .setMaster("local")
    .setAppName("PlayViewBatchAnalytics")
    .set("spark.executor.memory", "1g")
  val sc = new SparkContext(conf)
  val sqlContext = new org.apache.spark.sql.SQLContext(sc)

  // this is used to implicitly convert an RDD to a DataFrame.
  import sqlContext.implicits._

  val counter = new AtomicInteger()

  def processLogFile(logFile: String) {
    val logData = sc.textFile(logFile, 2).cache()

    val words = logData.foreach({ line =>
      {
        //val toks = line.split(";")
        println(line)

      }
    })

    println("------------------------------------------------------------")
    val numAs = logData.filter(line => line.contains("www.yan.vn")).count()
    println("Lines with a: %s".format(numAs))
    println("line count %s".format(logData.count()))
    println("first line: %s".format(logData.first()))

    logData.unpersist(true)
  }

  case class Playview(loggedTime: Int, placementId: Int, ip: String, uuid: String, url: String, 
      urlReferer: String, locCity: String, locCountry: String, platformId: Int, deviceType: String, deviceOs: String)

  def main(args: Array[String]) {
    var path = "/home/trieu/data/raw_logs/playview-fptlay/day-2015-06-30/hour-20/"
    //    val files = LogUtil.recursiveListFiles(new File(path))
    //    def handler(f: File) {
    //      println(f.getAbsolutePath)
    //      processLogFile(f.getAbsolutePath)
    //      Thread.sleep(500)
    //    }
    //    files.foreach { handler }

    val file = path + "3.log"
    // Create a custom class to represent the Customer
   
//    sc.textFile(file, 2).cache().foreach({ line =>
//      {
//        val toks = line.split("\t")
//        var i = counter.incrementAndGet()
//        println(i +" "+toks(8))
//      }
//    })

    // Create a DataFrame of Playview objects from the dataset text file.
    val dfPlayviews = sc.textFile(file).cache().map(_.split("\t")).map(p =>
      Playview(
          p(0).trim.toInt, 
          p(1).trim.toInt, 
          p(2), 
          p(3), 
          p(4), 
          p(5), 
          p(6), 
          p(7), 
          p(8).trim.toInt,  
          p(9),
          p(10))).toDF()

    // Register DataFrame as a table.
    dfPlayviews.registerTempTable("playviews")

    // Display the content of DataFrame
    //dfPlayviews.show()

    // SQL statements can be run by using the sql methods provided by sqlContext.
    val results = sqlContext.sql("SELECT DISTINCT url FROM playviews WHERE locCity = 'Ho Chi Minh City' AND platformId = 3")
    //playviewsByCity.show()
   results.map(t => "url: " + String.valueOf(t(0)).split("#").apply(0) ).collect().foreach(println)

    //sc.stop()
  }
}