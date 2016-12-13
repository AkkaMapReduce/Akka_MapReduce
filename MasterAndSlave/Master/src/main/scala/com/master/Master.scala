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
//    remoteDb ? RunMapRequest(seq)
    val addresses = Seq("127.0.0.1:2552", "127.0.0.1:2553")
    val remotes = addresses.map(add => system.actorSelection(s"akka.tcp://Slave@$add/user/slave"))
    var idx = 0
    seq.grouped(seq.size/remotes.size)
        .toList
        .map(subSeq => {
          remotes(idx) ? RunMapRequest(subSeq)
          idx += 1
        })

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
