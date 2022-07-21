
object Main {
  def main(args: Array[String]): Unit = {

    val IBM = new StockPredictor("IBM", 130.88, 252) //252 is number of working days

    println(IBM.toString)
    println(IBM.nextPrice())

    val p = IBM.getMultiverseOfPrice()
    println(p.head)
    println(p.last)
  }
}