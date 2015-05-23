package dw.actor

import akka.actor.Actor
import akka.event.Logging

class ContextActor extends Actor {
  
  val log = Logging(context.system, this)
  def receive = {
    case "test" => log.info("received test")
    case _      => log.info("received unknown message")
  }

}