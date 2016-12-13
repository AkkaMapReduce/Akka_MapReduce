package com.mapper

trait MapTemplate[A, B, C, D] {
  val MapSeq: Seq[A]
  val MapReduceKey: B
  val MapReduceSeq: Seq[C]

  def Map(x: Seq[A]): Seq[(B, C)]
  def MapReduce(key: B, seq: Seq[(C)]): D
}

object TestGuy extends MapTemplate[Int, String, Double, Double] {
  val MapSeq: Seq[Int] = Seq(1)
  val MapReduceKey: String = ""
  val MapReduceSeq: Seq[Double] = Seq(1.0)

  def Map(xs: Seq[Int]): Seq[(String, Double)] = {
    val grps = "ABC"
    val func = (x: Int) => (grps.charAt(x%3).toString, x.toDouble)
    xs.map(func)
  }

  def MapReduce(key: String, seq: Seq[Double]) = {
    // Group A will return Max
    // Group B will return Min
    // Group C will return Mean
    key match {
      case "A" => seq.max
      case "B" => seq.min
      case "C" => seq.sum/seq.size
    }

  }
}