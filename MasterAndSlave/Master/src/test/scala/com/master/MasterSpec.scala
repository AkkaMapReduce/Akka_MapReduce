package com.master


import akka.actor.{ActorSystem, Props}
import com.slave.func_template
import org.scalatest.{FunSpecLike, Matchers}

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

class MasterSpec extends FunSpecLike with Matchers {
  val client = new Master("127.0.0.1:2552")

  describe("PingRequest Test") {
    it("should send Ping and receive Pong back") {
      val futureResult = client.ping()
      val result = Await.result(futureResult, 10 seconds)
      result should equal("Pong")
    }
  }

  describe("AddMeOneRequest Test") {
    it("should increment value by 1") {
      val x: Int = 10
      val futureResult = client.addOne(x)
      val result = Await.result(futureResult, 10 seconds)
      result should equal(11)
    }
  }

  def JarServer() = {
    import java.net._
    import java.io._
    import scala.io._

    val server = new ServerSocket(9999)

    //Master should ping the slave actor to request for jar file
    //    while (true) {
    val s = server.accept()
    val in = new BufferedSource(s.getInputStream()).getLines()
    val out = s.getOutputStream()

    val filename = "src/main/resources/mapReduce.jar"
    val f = new FileInputStream(filename)
    val bos = new BufferedOutputStream(out)
    var c = 0;
    while ({c = f.read; c != -1 }) {
      bos.write(c)
    }
    //    Stream.continually(f.read).takeWhile(_ != -1).foreach(bos.write)

    f.close
    bos.flush()
    bos.close

    out.flush()
    s.close()
    //    }
  }

  describe("send jar file") {
    it("should save jar file on slave and return Done!") {
      val futureResult = client.sendJar()

      JarServer()

      val result = Await.result(futureResult, 10 seconds)
      result should equal("Done!")
    }
  }

}

