package dw.actor

import akka.actor.ActorSystem
import akka.actor.Props

object ActorPoolStarter {
  def main(args: Array[String]) {
    val system = ActorSystem("mySystem")
    val myActor = system.actorOf(Props[ContextActor], "myactor2")
    myActor ! "mm"
    system.shutdown()
    
  }
}