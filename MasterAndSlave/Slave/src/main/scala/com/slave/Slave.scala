package com.slave

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorSystem, Props, Status}
import akka.event.Logging
import com.slave.messages._
import java.net._
import java.io._

abstract class func_template {
  def run(x: Int): Int
}

//class FunRequest[A](prog: A => A, i:Int) extends Serializable
//class Response(msg: String) extends Serializable

class Slave extends Actor {
  val log = Logging(context.system, this)

  // For testing purpose only
  // ====================================================
  var forTesting: Option[Int] = None
  // ====================================================

  def receiveJarFile() = {
    val s = new Socket(InetAddress.getByName("localhost"), 9999)
    lazy val in = s.getInputStream()
    val out = new PrintStream(s.getOutputStream())

    out.println("Give me the jar file!")
    out.flush()

    val file = new FileOutputStream("src/main/resources/mapReduce.jar" )

    var count = 0
    while({count = in.read; count != -1}) {
      file.write(count)
    }

    file.close
    s.close()
  }

  override def receive = {

    case RunRequest(func, input) =>
      log.info("received RunRequest")

      val result: Option[Int] = Some(func(input))

      log.info("RunRequest result: {}", result)
      result match {
        case Some(x) => sender() ! x
        case None => sender() ! Status.Failure(new ErrorException)
      }

    // For testing purpose only
    // ====================================================
    case PingRequest() =>
      log.info("received PingRequest")
      val reply: Option[String] = Some("Pong")

      reply match {
        case Some(x) => sender() ! x
        case None => sender() ! Status.Failure(new PingFailException)
      }

    case AddMeOneRequest(x) =>
      log.info("received AddMeOneRequest")

      val result: Option[Int] = Some(x + 1)
      result match {
        case Some(x) => sender() ! x
        case None => sender() ! Status.Failure(new ErrorException)
      }

    case FunRequest(prog, i) =>
      log.info("received FunRequest")

      val result: Option[Int] = Some(prog(i))
      result match {
        case Some(x) => sender() ! x
        case None => sender() ! Status.Failure(new ErrorException)
      }

    case JarReady =>
      log.info("received JarRequest")

      receiveJarFile()


//      val file: File  = new File("src/main/resources/mapReduce.jar")
//
//      val url: URL = file.toURI().toURL()
//      val cl: URLClassLoader = new URLClassLoader(Array(url))
//      val cls: Class[_] = cl.loadClass("MapTemplate")
//
//      val testguy: Class[_] = cl.loadClass("TestGuy")
//      println(cls.getCanonicalName)
//      println(testguy.getCanonicalName)
//
//      val m = testguy.getMethod("Map",  Seq[Int].asInstanceOf[AnyRef].getClass); // get the method you want to call
//      val xs = Seq(1,2,3); // the arguments. Change this if you want to pass different args
//      val ans = m.invoke(null, xs);
//      println(ans.toString)
//      println("__")
//
//      val tint = Seq[Int].getClass
//      val n = testguy.getMethod("MapReduce", "".asInstanceOf[AnyRef].getClass, tint); // get the method you want to call
//      val key = "A"
//      val seq = Seq(1,2,3); // the arguments. Change this if you want to pass different args
//      val res = n.invoke(null, key, seq);
//      println(res.toString)
//      println("__")


      //////////

//      val url: URL = file.toURI().toURL()
//      val classLoader = ClassLoader.getSystemClassLoader()
//      val method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
//      method.setAccessible(true);
//      method.invoke(classLoader, url);
      /////////////
//      val url: URL = file.toURI().toURL()
//      val loader = URLClassLoader.newInstance(
//        Array(url),
//        getClass().getClassLoader()
//      );
//      val clazz = Class.forName("mypackage.MyClass", true, loader);
//      val runClass = clazz.asSubclass(Runnable.class);
//      // Avoid Class.newInstance, for it is evil.
//      Constructor<? extends Runnable> ctor = runClass.getConstructor();
//      Runnable doRun = ctor.newInstance();
//      doRun.run();

//      testguy.Map()

      //doMapWork()

      sender() ! "Done!"



    case RunObjectRequest(obj, input) =>
      log.info("received RunObjectRequest")

      val result: Option[Int] = Some(obj.run(input))
      sender() ! 50
//      forTesting = result
//      result match {
//        case Some(x) => sender() ! x
//        case None => sender() ! Status.Failure(new ErrorException)
//      }

    // ====================================================

    case o =>
      log.info("Well I'm Fucked")
      Status.Failure(new ClassNotFoundException)
  }
}

object Main extends App {
  val system = ActorSystem("Slave")
  val helloActor = system.actorOf(Props[Slave], name = "slave")
}