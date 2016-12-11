//package com.slave
//
//// Simple client
//
//object client extends App {
//  import java.net._
//  import java.io._
//  import scala.io._
//  import java.util.jar._
//
//  val s = new Socket(InetAddress.getByName("localhost"), 9999)
//  lazy val in = new BufferedSource(s.getInputStream())
//  val out = new PrintStream(s.getOutputStream())
//
//  out.println("Give me the jar file!")
//  out.flush()
//
//  // val file = new PrintWriter(new File("testMapReduce.scala" ))
//
//  // while(in.hasNext) {
//  // 	val buf = in.next()
//  // 	println("Received: " + buf)
//  // 	file.write(buf)//(codec = Codec.UTF8)
//  // }
//  // file.close
//
//  val file = new File("testmapReduce.jar")
//  val bw = new BufferedWriter(new FileWriter(file))
//  println("whut")
//  println(in)
//  println("so..")
//  while(in.hasNext) {
//    val buf = in.next()
//    println(buf)
//    bw.write(buf)
//    // println(buf)
//  }
//
//
//  s.close()
//  bw.close()
//
//
//  println("Done!")
//  // val jar = new JarFile(file)
//}