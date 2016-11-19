package com.master


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

  describe("func: add20 && input: 30") {
    it("should add 20 to 30"){
      case class add20() extends func_template {
        def run(x: Int) = x + 20
      }
      val obj = new add20()
      val x = 30

      val futureResult = client.run(obj, x)
      val result = Await.result(futureResult, 10 seconds)
      result should equal(50)
    }
  }
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

