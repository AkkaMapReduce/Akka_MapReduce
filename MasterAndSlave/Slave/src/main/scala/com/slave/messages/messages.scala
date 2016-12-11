package com.slave.messages

import com.slave.func_template

case class RunRequest(func: Int => Int, input: Int) extends Serializable
case class FunRequest(prog: Int => Int, i:Int) extends Serializable
case class ErrorException() extends Exception

class Stock(prog: Int => Int, i: Int) extends Serializable {  }

case object JarReady;

// For testing purpose only
// ====================================================
case class PingRequest()
case class AddMeOneRequest(input: Int)
case class RunObjectRequest(obj: func_template, input: Int)
case class PingFailException() extends Exception
// ====================================================