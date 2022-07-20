import Retriever.{getExpectedReturn, getOpeningPrices, getVolatility}

object Main{
  def main(args: Array[String]): Unit = {

    val openingPricesIBM = getOpeningPrices("IBM")
    val volatility = getVolatility(openingPricesIBM)
    val returnRate = getExpectedReturn(openingPricesIBM)
    var IBM = new StockPredictor("IBM",130.88,returnRate,100,volatility)
    println(IBM.toString)
    println(IBM.nextPrice())
  }
}