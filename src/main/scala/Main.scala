import Retriever.{getExpectedReturn, getOpeningPrices, getVolatility}

import scala.util.Random

object Main{
  def main(args: Array[String]): Unit = {

    val openingPricesIBM = getOpeningPrices("IBM")
    val volatility = getVolatility(openingPricesIBM)
    val returnRate = getExpectedReturn(openingPricesIBM)
    var IBM = new StockPredictor("IBM",130.88,returnRate,252,volatility) //252 is number of working days

    println(IBM.toString)
    println(IBM.nextPrice())
    println(IBM.prices().toString()) // quite random

  }
}