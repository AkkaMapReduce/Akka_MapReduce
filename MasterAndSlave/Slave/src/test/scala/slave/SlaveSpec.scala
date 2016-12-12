package com.slave


import akka.actor.ActorSystem
import akka.testkit.TestActorRef
import akka.util.Timeout
import com.slave.messages.{PingRequest, RunMapRequest}
import org.scalatest.{FunSpecLike, Matchers}

import scala.concurrent.duration._

class SlaveSpec extends FunSpecLike with Matchers {
  implicit val system = ActorSystem()
  implicit val timeout = Timeout(5 seconds)

  describe("Slave") {

    // For testing purpose only
    // ====================================================
    describe("given PingRequest"){
      it("should send Pong back"){
        val actorRef = TestActorRef(new Slave)
        actorRef ! PingRequest()

        val slave = actorRef.underlyingActor
        slave should equal("Pong")
      }
    }
    // ====================================================

    describe("given RunMapRequest"){
      it("should map Seq to Seq(grp, res)"){
        val actorRef = TestActorRef(new Slave)
        val testSeq = Seq(1,2,3,4)

        actorRef ! RunMapRequest(testSeq)

        val slave = actorRef.underlyingActor
        slave should equal(testSeq)
      }
    }
  }


}
