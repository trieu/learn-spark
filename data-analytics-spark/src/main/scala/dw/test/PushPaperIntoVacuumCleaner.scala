package dw.test

object PushPaperIntoVacuumCleaner {
  
  case class DataEvent(c: Char, i: Int) {
    var _c: Char = c
    var _i: Int = i
    override def toString = "%s at %d".format(_c, _i)
    def getData = "%s".format(_c)
  }

  def toElement(c: Char, i: Int): DataEvent = {
    return new DataEvent(c,i)
  }
  
  def pushIntoVacuumCleaner(elements: Array[DataEvent]): Seq[DataEvent] = {
    var stream = util.Random.shuffle(elements.toSeq)
    // blah blah tasks will be done
    return stream;
  }

  def main(args: Array[String]) {
    //this is a paper we have
    val paper = "*****HELLO******"
    
    //transform into a set of elements
    val paperSet = paper.toCharArray()    
    val elements = Array.tabulate(paper.length) { i => toElement(paperSet(i), i) }
    
    //treat data as stream
    val stream = pushIntoVacuumCleaner(elements)    
    println( stream.foldLeft("")((x,y) => x + "\n" + y  ) )
    
    println("RAW message: " + paper )
    
    //print it out as unordered list 
    println("After take out from VacuumCleaner: " + stream.map { x:DataEvent => x.getData  } )
    
    //sorting the list and print it out as ordered list
    val newStream = stream.sortWith(_._i < _._i)
    println("Sorted data event: " + newStream.map { x:DataEvent => x.getData  } )
  }

}