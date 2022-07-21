
object Main {
  def main(args: Array[String]): Unit = {

    // risky
    val IBM = new StockPredictor("IBM", 130.88, 252) //252 is number of working days

    // dividend
    val INTC = new StockPredictor("INTC", 40.56, 252) //Intel

    // value
    val TSLA = new StockPredictor("TSLA", 742.50, 252) //Tesla

    IBM.printReport()
    INTC.printReport()
    TSLA.printReport()

//    IBM.writeReport()
//    INTC.writeReport()
//    TSLA.writeReport()
  }
}