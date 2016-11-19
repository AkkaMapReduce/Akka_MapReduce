package com.slave

import akka.actor.{Actor, ActorSystem, Props, Status}
import akka.event.Logging
import com.slave.messages._

abstract class func_template {
  def run(x: Int): Int
}

class Slave extends Actor {
  val log = Logging(context.system, this)

  // For testing purpose only
  // ====================================================
  var forTesting: Option[Int] = None
  // ====================================================

  override def receive = {

    case RunRequest(func, input) =>
      log.info("received RunRequest")

      val result: Option[Int] = Some(func(input))

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

    case RunObjectRequest(obj, input) =>
      log.info("received RunObjectRequest")

      val result: Option[Int] = Some(obj.run(input))
      sender() ! 50
//      forTesting = result
//      result match {
//        case Some(x) => sender() ! x
//        case None => sender() ! Status.Failure(new ErrorException)
//      }

    // ====================================================

    case o =>
      log.info("Well I'm Fucked")
      Status.Failure(new ClassNotFoundException)
  }
}

object Main extends App {
  val system = ActorSystem("Slave")
  val helloActor = system.actorOf(Props[Slave], name = "slave")
}