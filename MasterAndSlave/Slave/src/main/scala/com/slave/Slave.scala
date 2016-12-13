package com.slave

import akka.actor.{Actor, ActorSystem, Props, Status}
import akka.event.Logging
import com.mapper.TestGuy
import com.slave.messages._


class Slave() extends Actor {
  val log = Logging(context.system, this)

  override def receive = {

    case RunMapRequest(xs: Any) => {
      log.info("received RunMapRequest")
      log.info(xs.toString())
      val res = TestGuy.Map(xs.asInstanceOf[TestGuy.MapSeq.type])
      log.info(res.toString())

      val resMap = res.groupBy(_._1)
      log.info(resMap.toString())

      sender() ! resMap
    }

    case RunMapReduceRequest(key: Any, xs: Any) => {
      log.info("received RunMapReduceRequest")
      log.info(xs.toString())

      val res = TestGuy.MapReduce(key.asInstanceOf[TestGuy.MapReduceKey.type ],
                                  xs.asInstanceOf[TestGuy.MapReduceSeq.type])
      log.info(res.toString)

      sender() ! res
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
  val helloActor = system.actorOf(Props(new Slave), name = "slave")
}