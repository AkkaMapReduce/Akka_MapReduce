package com.master

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.util.Timeout
import com.slave.messages._

import scala.concurrent.duration._

class Master(remoteAddress: String){
  private implicit val timeout = Timeout(2 seconds)
  private implicit val system = ActorSystem("LocalSystem")
  private val remoteDb = system.actorSelection(s"akka.tcp://Slave@$remoteAddress/user/slave")

  def map(seq: Seq[Int]) = {
    remoteDb ? RunMapRequest(seq)
  }

  def mapReduce(grp: String, seq: Seq[Double]) = {
    remoteDb ? RunMapReduceRequest(grp, seq)
  }



  // For testing purpose only
  // ====================================================
  def ping() = {
    remoteDb ? PingRequest()
  }

  def addOne(x: Int) = {
    remoteDb ? AddMeOneRequest(x)
  }
  // ====================================================
}
