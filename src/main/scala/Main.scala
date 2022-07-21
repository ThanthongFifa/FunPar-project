
object Main {
  def main(args: Array[String]): Unit = {

    // low risk
    val IBM = new StockPredictor("IBM", 130.88, 252) //252 is number of working days

    // high risk
    val MRNA = new StockPredictor("MRNA", 166.89, 252) //Moderna

    println(IBM.toString)
    println(IBM.nextPrice())
    println(IBM.profitChance(90)*100 + "%")
    println(IBM.lossChance())

    println(MRNA.toString)
    println(MRNA.nextPrice())
    println(MRNA.profitChance(1)*100 + "%")
    println(MRNA.lossChance())
  }
}