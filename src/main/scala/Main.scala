import Retriever.{getExpectedReturn, getOpeningPrices, getVolatility}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Random, Success}

object Main {
  def main(args: Array[String]): Unit = {

    // setting up necessary info
    val openingPricesIBM = getOpeningPrices("IBM")
    val volatility = getVolatility(openingPricesIBM)
    val returnRate = getExpectedReturn(openingPricesIBM)

    // create StockPredictor for IBM
    var IBM = new StockPredictor("IBM", 130.88, returnRate, 252, volatility) //252 is number of working days

    println(IBM.toString)
    println(IBM.nextPrice())

    val p = IBM.getMultiverseOfPrice()
    println(p.head)
    println(p.last)
  }
}