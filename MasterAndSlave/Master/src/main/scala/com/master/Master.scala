package com.master

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.util.Timeout
import com.slave.messages.{AddMeOneRequest, JarReady, PingRequest, RunMapRequest}

import scala.concurrent.Future
import scala.concurrent.duration._

class Master(remoteAddresses: Seq[String]){
  private implicit val timeout = Timeout(2 seconds)
  private implicit val system = ActorSystem("Master")
  private val remotes = remoteAddresses.map(
    remoteAddress => system.actorSelection(s"akka.tcp://Slave@$remoteAddress/user/slave"))
  private val numRemotes = remotes.size

  def sendJar(): (List[Future[Any]], Int) = {
    (remotes.map(_ ? JarReady).toList, remotes.size)
  }


  def map(seq: Seq[Int]): Future[Any] = {
    val subSeq = seq.grouped(numRemotes)
    var counter = numRemotes
    val res = subSeq.map(ss => {
      remotes(counter) ? RunMapRequest(seq)
      counter -= 1
    })
  }

//  def mapReduce(grp: String, seq: Seq[Double]) = {
//    remoteDb ? RunMapReduceRequest(grp, seq)
//  }

  // For testing purpose only
  // ====================================================
  def ping() = {
    remotes(0) ? PingRequest()
  }

  def addOne(x: Int) = {
    remotes(0) ? AddMeOneRequest(x)
  }
  // ====================================================
}