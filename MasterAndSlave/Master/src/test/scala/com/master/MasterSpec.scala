package com.master

import com.slave.messages.JarReceived
import org.scalatest.{FunSpecLike, Matchers}

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

class MasterSpec extends FunSpecLike with Matchers {
  val slaves = Seq("127.0.0.1:2552", "127.0.0.1:2553")
  val client = new Master(slaves)

  describe("PingRequest Test") {
    it("should send Ping and receive Pong back") {
      val futureResult = client.ping()
      val result = Await.result(futureResult, 10 seconds)
      result should equal("Pong")
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

    val filename = "src/main/resources/test.txt"
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
      val (futureResult, remotes) = client.sendJar()

      JarServer()

      val result = futureResult.map(Await.result(_, 10 seconds))
      result should equal(List.fill(remotes)(JarReceived))
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

  describe("RunMapRequest Test") {
    it("should receive Map of Seq in group"){
      val xs = (1 to 20)

      val futureResult = client.map(xs)
      val result = Await.result(futureResult, 10 seconds)
      val ans = Map("A" -> Vector(("A",3.0), ("A",6.0), ("A",9.0), ("A",12.0), ("A",15.0), ("A",18.0)),
                    "C" -> Vector(("C",2.0), ("C",5.0), ("C",8.0), ("C",11.0), ("C",14.0), ("C",17.0), ("C",20.0)),
                    "B" -> Vector(("B",1.0), ("B",4.0), ("B",7.0), ("B",10.0), ("B",13.0), ("B",16.0), ("B",19.0)))
      result should equal(ans)
    }
  }

//  describe("RunMapReduceRequest Test") {
//    it("should receive res based on the Seq group [A]"){
//      val xs = (1 to 20)
//
//      val futureResult_1 = client.map(xs)
//      val result_1 = Await.result(futureResult_1, 10 seconds).asInstanceOf[ans.type]
//      val ans = Map("A" -> Vector(("A",3.0), ("A",6.0), ("A",9.0), ("A",12.0), ("A",15.0), ("A",18.0)),
//        "C" -> Vector(("C",2.0), ("C",5.0), ("C",8.0), ("C",11.0), ("C",14.0), ("C",17.0), ("C",20.0)),
//        "B" -> Vector(("B",1.0), ("B",4.0), ("B",7.0), ("B",10.0), ("B",13.0), ("B",16.0), ("B",19.0)))
//
//      val inGrp = result_1.keys.map(k => result_1(k).map(_._2))
//      val futureResult_2 = client.mapReduce(result_1.keys.head, inGrp.head)
//      val result_2 = Await.result(futureResult_2, 10 seconds)
//
//      result_2 should equal(18)
//    }
//  }
//
//  describe("RunMapReduceRequest Test") {
//    it("should receive res based on the Seq group [C]"){
//      val xs = (1 to 20)
//
//      val futureResult_1 = client.map(xs)
//      val result_1 = Await.result(futureResult_1, 10 seconds).asInstanceOf[ans.type]
//      val ans = Map("A" -> Vector(("A",3.0), ("A",6.0), ("A",9.0), ("A",12.0), ("A",15.0), ("A",18.0)),
//        "C" -> Vector(("C",2.0), ("C",5.0), ("C",8.0), ("C",11.0), ("C",14.0), ("C",17.0), ("C",20.0)),
//        "B" -> Vector(("B",1.0), ("B",4.0), ("B",7.0), ("B",10.0), ("B",13.0), ("B",16.0), ("B",19.0)))
//
//      val inGrp = result_1.keys.map(k => result_1(k).map(_._2))
//      val futureResult_2 = client.mapReduce(result_1.keys.tail.head, inGrp.tail.head)
//      val result_2 = Await.result(futureResult_2, 10 seconds)
//
//      result_2 should equal(11)
//    }
//  }
//
//  describe("RunMapReduceRequest Test") {
//    it("should receive res based on the Seq group [B]"){
//      val xs = (1 to 20)
//
//      val futureResult_1 = client.map(xs)
//      val result_1 = Await.result(futureResult_1, 10 seconds).asInstanceOf[ans.type]
//      val ans = Map("A" -> Vector(("A",3.0), ("A",6.0), ("A",9.0), ("A",12.0), ("A",15.0), ("A",18.0)),
//        "C" -> Vector(("C",2.0), ("C",5.0), ("C",8.0), ("C",11.0), ("C",14.0), ("C",17.0), ("C",20.0)),
//        "B" -> Vector(("B",1.0), ("B",4.0), ("B",7.0), ("B",10.0), ("B",13.0), ("B",16.0), ("B",19.0)))
//
//      val inGrp = result_1.keys.map(k => result_1(k).map(_._2))
//      val futureResult_2 = client.mapReduce(result_1.keys.tail.tail.head, inGrp.tail.tail.head)
//      val result_2 = Await.result(futureResult_2, 10 seconds)
//
//      result_2 should equal(1)
//    }
//  }
//  describe("func: minus30 && input: 45") {
//    it("should minus 30 from 45") {
//      case class minus30() extends func_template {
//        def run(x: Int) = x - 30
//      }
//      val obj = new minus30
//      val x = 45
//
//      val futureResult = client.run(obj, x)
//      val result = Await.result(futureResult, 10 seconds)
//      result should equal(15)
//    }
//  }

}

