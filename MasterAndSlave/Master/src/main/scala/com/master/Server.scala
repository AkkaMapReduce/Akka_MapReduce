package com.master

// Simple server

object server extends App {
  import java.net._
  import java.io._
  import scala.io._

  import scala.io.Source

  val server = new ServerSocket(9999)

  //Master should ping the slave actor to request for jar file
  while (true) {
    val s = server.accept()
    val in = new BufferedSource(s.getInputStream()).getLines()
    val out = new PrintStream(s.getOutputStream())

    val filename = "mapReduce.jar"
    for (line <- Source.fromFile(filename, "ISO-8859-1").getLines) {
      out.print(line)
      // println(line)
    }

    // val f = new FileInputStream(filename)
    // val bos = new BufferedOutputStream(out)
    // Stream.continually(f.read).takeWhile(_ != -1).foreach(bos.write)

//    println(bos)
//
//    f.close
//    bos.close



    out.flush()
    s.close()
  }
}