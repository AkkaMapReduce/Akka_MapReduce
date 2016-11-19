package com.master

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.util.Timeout
import com.slave.func_template
import com.slave.messages.{AddMeOneRequest, PingRequest, RunObjectRequest}

import scala.concurrent.duration._

class Master(remoteAddress: String){
  private implicit val timeout = Timeout(2 seconds)
  private implicit val system = ActorSystem("LocalSystem")
  private val remoteDb = system.actorSelection(s"akka.tcp://Slave@$remoteAddress/user/slave")

  def run(func: func_template, input: Int) = {
    remoteDb ? RunObjectRequest(func, input)
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