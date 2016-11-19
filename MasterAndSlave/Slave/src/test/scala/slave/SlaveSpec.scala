package com.slave


import akka.actor.ActorSystem
import akka.testkit.TestActorRef
import akka.util.Timeout
import com.slave.messages.{PingRequest, RunObjectRequest, RunRequest}
import org.scalatest.{FunSpecLike, Matchers}

import scala.concurrent.duration._

class SlaveSpec extends FunSpecLike with Matchers {
  implicit val system = ActorSystem()
  implicit val timeout = Timeout(5 seconds)

  describe("Slave") {
    describe("given RunObjectRequest"){
      it("should run given object with input"){
        val actorRef = TestActorRef(new Slave)
        case class addTwo() extends func_template {
          def run(x: Int) = x + 2
        }
        val obj = new addTwo
        val x = 10
        actorRef ! RunObjectRequest(obj, x)
        val slave = actorRef.underlyingActor
        slave.forTesting should equal(Some(12))
      }
    }
//    // For testing purpose only
//    // ====================================================
//    describe("given PingRequest"){
//      it("should send Pong back"){
//        val actorRef = TestActorRef(new Slave)
//        actorRef ! PingRequest()
//
//        val slave = actorRef.underlyingActor
//        slave should equal("Pong")
//      }
//    }
//    // ====================================================
//
//    describe("given RunRequest"){
//      it("should add 10 to 20"){
//        val actorRef = TestActorRef(new Slave)
//        val addTen = (x: Int) => x + 10
//        actorRef ! RunRequest(addTen, 20)
//
//        val slave = actorRef.underlyingActor
//        slave should equal(30)
//      }
//    }
  }


}
