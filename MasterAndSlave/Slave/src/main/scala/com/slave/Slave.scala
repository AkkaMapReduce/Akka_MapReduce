package com.slave

import akka.actor.{Actor, ActorSystem, Props, Status}
import akka.event.Logging
import com.mapper.TestGuy
import com.slave.messages._


class Slave() extends Actor {
  val log = Logging(context.system, this)

  def receiveJarFile() = {
    import java.net._
    import java.io._

    val s = new Socket(InetAddress.getByName("localhost"), 9999)
    lazy val in = s.getInputStream()
    val out = new PrintStream(s.getOutputStream())

    out.println("Give me the jar file!")
    out.flush()

    val file = new FileOutputStream("src/main/resources/test.txt" )

    var count = 0
    while({count = in.read; count != -1}) {
      file.write(count)
    }

    file.close
    s.close()
  }

  override def receive = {

    case JarReady =>
      log.info("received JarRequest")

      receiveJarFile()

      sender() ! JarReceived

    case RunMapRequest(xs: Seq[Int]) => {
      log.info("received RunMapRequest")
      log.info(xs.toString())

      val res = TestGuy.Map(xs)
      log.info(res.toString())

      val resMap = res.groupBy(_._1)
      log.info(resMap.toString())

      sender() ! resMap
    }

    case RunMapReduceRequest(key: String, xs: Seq[Double]) => {
      log.info("received RunMapReduceRequest")
      log.info(xs.toString())

      val res = TestGuy.MapReduce(key, xs)
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