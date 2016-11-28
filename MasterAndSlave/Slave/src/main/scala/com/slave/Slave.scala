package com.slave

import akka.actor.{Actor, ActorSystem, Props, Status}
import akka.event.Logging
import akka.serialization.SerializationExtension
import com.slave.messages._

//abstract class func_template {
//  def run(x: Int): Int
//}

class Slave(system: ActorSystem) extends Actor {
  val log = Logging(context.system, this)
  val serialization = SerializationExtension(system)



  override def receive = {

    case RunRequest(bytesArray, input) =>
      log.info("received RunRequest")

      val serializer = serialization.findSerializerFor(_:Int => Int)

      val func = serializer.fromBinary

      log.info("RunRequest result: {}", result)
      result match {
        case Some(x) => sender() ! x
        case None => sender() ! Status.Failure(new ErrorException)
      }

    // For testing purpose only
    // ====================================================
    case PingRequest() =>
      log.info("received PingRequest")
      val reply: Option[String] = Some("Pong")

      reply match {
        case Some(x) => sender() ! x
        case None => sender() ! Status.Failure(new PingFailException)
      }

    case AddMeOneRequest(x) =>
      log.info("received AddMeOneRequest")

      val result: Option[Int] = Some(x + 1)
      result match {
        case Some(x) => sender() ! x
        case None => sender() ! Status.Failure(new ErrorException)
      }

    case o =>
      log.info("Well I'm Fucked")
      Status.Failure(new ClassNotFoundException)
  }
}

object Main extends App {
  val system = ActorSystem("Slave")
  val helloActor = system.actorOf(Props(new Slave(system)), name = "slave")
}