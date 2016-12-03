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
		    out.println(line)
		    // println(line)
		}

	    
	    out.flush()
	    s.close()
	}
}