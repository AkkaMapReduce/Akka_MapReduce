package com.slave.messages


case class RunMapRequest[T](xs: Seq[T])
case class RunMapReduceRequest[A, B](key: A, xs: Seq[B])

//case class RunRequest(bytesArray: Array[Byte], input: Int)
case class ErrorException() extends Exception


// For testing purpose only
// ====================================================
case class PingRequest()
case class AddMeOneRequest(input: Int)
case class PingFailException() extends Exception
// ====================================================