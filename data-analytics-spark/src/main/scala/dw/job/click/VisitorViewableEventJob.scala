package dw.job.click

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

/** Computes an approximation to pi */
object VisitorViewableEventJob {

  case class VisitorViewableEvent(_timeStr: String, _uuid: String, _url: String, _platform: String, _ip: String) {
    var timeStr: String = _timeStr
    var uuid: String = _uuid
    var url: String = _url
    var platform: String = _platform
    var ip: String = _ip

    override def toString = "%s %s %s %s %s".format(timeStr, uuid, url, platform, ip)

  }

  var events: HashSet[VisitorViewableEvent] = HashSet()
  val conf = new SparkConf()
    .setMaster("local")
    .setAppName("ClickCounting")
    .set("spark.executor.memory", "1g")
  val sc = new SparkContext(conf)

  def processLogFile(logFile: String) {
    val logData = sc.textFile(logFile, 2).cache()

    val words = logData.foreach({ line =>
      {
        val toks = line.split("\t")
        if (toks.length == 21) {
          if (!toks(2).contains("=")) {
            val e = new VisitorViewableEvent(toks(1), toks(2), toks(4), toks(17), toks(19))
            // 
            events += e
          }
        }
      }
    })

    // done
    println("done: " + events.size)
    val i = new AtomicInteger()
    def processEvent(e: VisitorViewableEvent) {
      println(i.incrementAndGet() + ":" + e)
    }
    events.foreach(processEvent)

    println("------------------------------------------------------------")
    val numAs = logData.filter(line => line.contains("www.yan.vn")).count()
    println("Lines with a: %s".format(numAs))
    println("line count %s".format(logData.count()))
    println("first line: %s".format(logData.first()))

    logData.unpersist(true)
  }

  def main(args: Array[String]) {
    val logFile = "/home/trieunt/data/demo_targeting_data/log_trueimp/day=2014-10-26/hour=09/raw-log-2014-10-26-09-3-0.log" //args(0)
    val files = LogUtil.recursiveListFiles(new File("/home/trieunt/data/demo_targeting_data/log_trueimp/day=2014-10-26/hour=09/"))
    def handler(f: File) {
      println(f.getAbsolutePath)
      processLogFile(f.getAbsolutePath)
      Thread.sleep(2000)
    }
    files.foreach { handler }

    //sc.stop()
  }
}