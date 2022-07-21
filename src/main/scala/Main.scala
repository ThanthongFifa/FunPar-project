
object Main {
  def main(args: Array[String]): Unit = {

    val IBM = new StockPredictor("IBM", 130.88, 252) //252 is number of working days
    val AAPL = new StockPredictor("AAPL", 153.04, 252) //252 is number of working days

    println(IBM.toString)
    println(IBM.nextPrice())
    println(IBM.profitChance(90)*100 + "%")

    println(AAPL.toString)
    println(AAPL.nextPrice())
    println(AAPL.profitChance(1)*100 + "%")
  }
}