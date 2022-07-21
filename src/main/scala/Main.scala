
object Main {
  def main(args: Array[String]): Unit = {

    // risky
    val IBM = new StockPredictor("IBM", 130.88, 252) //252 is number of working days

    // dividend
    val INTC = new StockPredictor("INTC", 40.56, 252) //Intel

    IBM.report()
    INTC.report()
  }
}