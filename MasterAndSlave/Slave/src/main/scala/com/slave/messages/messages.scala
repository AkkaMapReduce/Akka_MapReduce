package com.slave.messages

case class RunRequest(func: Int => Int, input: Int)
case class AddMeOneRequest(input: Int)

case class ErrorException() extends Exception

// For testing purpose only
// ====================================================
case class PingRequest()
case class PingFailException() extends Exception
// ====================================================