trait MapTemplate[A, B, C, D] {
  def Map(x: Seq[A]): Seq[(B, C)]
  def MapReduce(key: B, seq: Seq[(C)]): D
}
 
class TestGuy extends MapTemplate[Int, String, Int, Double] {
  def Map(xs: Seq[Int]): Seq[(String, Int)] = {
    val grps = "ABC"
    val func = (x: Int) => (grps.charAt(x%3).toString, x)
    xs.map(func)
  }
 
  def MapReduce(key: String, seq: Seq[Int]) = {
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
