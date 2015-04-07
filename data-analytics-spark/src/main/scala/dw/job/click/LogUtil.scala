package dw.job.click

import java.io.File

object LogUtil {

  def recursiveListFiles(f: File): Array[File] = {
    val these = f.listFiles
    these ++ these.filter(_.isDirectory).flatMap(recursiveListFiles)
  }
  
  
  def main(args: Array[String]) {
    val files = LogUtil.recursiveListFiles(new File("/home/trieunt/data/demo_targeting_data/log_trueimp/day=2014-10-26/hour=09/"))
    files.foreach { println }
  }
}