package learning.scala

import scala.concurrent.{ Await, Future }
import scala.concurrent.duration.Duration
import scala.async.Async.{ async, await }
import scala.concurrent.ExecutionContext.Implicits.global

object BasicOOP {
  
  

  def main(args: Array[String]): Unit = {

    //Chapter 4. Pattern Matching
    for {
      x <- Seq(1, 2, 2.7, "one", "two", 'four) // 
    } {
      val str = x match { // 
        case 1          => "int 1" // 
        case i: Int     => "other int: " + i // 
        case d: Double  => "a double: " + x // 
        case "one"      => "string one" // 
        case s: String  => "other string: " + s // 
        case unexpected => "unexpected value: " + unexpected // 
      }
      println(str) // 
    }

    val pf1: PartialFunction[Any, String] = { case s: String => "YES" } // 
    val pf2: PartialFunction[Any, String] = { case d: Double => "YES" } // 

    val pf = pf1 orElse pf2 // 

    def tryPF(x: Any, f: PartialFunction[Any, String]): String = // 
      try { f(x).toString } catch { case _: MatchError => "ERROR!" }

    def d(x: Any, f: PartialFunction[Any, String]) = // 
      f.isDefinedAt(x).toString

    println("      |   pf1 - String  |   pf2 - Double  |    pf - All") // 
    println("x     | def?  |  pf1(x) | def?  |  pf2(x) | def?  |  pf(x)")
    println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++")
    List("str", 3.14, 10) foreach { x =>
      printf("%-5s | %-5s | %-6s  | %-5s | %-6s  | %-5s | %-6s\n", x.toString,
        d(x, pf1), tryPF(x, pf1), d(x, pf2), tryPF(x, pf2), d(x, pf), tryPF(x, pf))
    }

    val list = 1 until 10
    list.foreach(s => { println(s) })



    object AsyncExample {
      def recordExists(id: Long): Boolean = { // 
        println(s"recordExists($id)...")
        Thread.sleep(1)
        id > 0
      }

      def getRecord(id: Long): (Long, String) = { // 
        println(s"getRecord($id)...")
        Thread.sleep(1)
        (id, s"record: $id")
      }

      def asyncGetRecord(id: Long): Future[(Long, String)] = async { // 
        val exists = async { val b = recordExists(id); println(b); b }
        if (await(exists)) await(async { val r = getRecord(id); println(r); r })
        else (id, "Record not found!")
      }
    }

    (-1 to 1) foreach { id => // 
      val fut = AsyncExample.asyncGetRecord(id)
      println(Await.result(fut, Duration.Inf))
    }
  }
}