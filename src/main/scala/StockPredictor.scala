import Retriever.{getExpectedReturn, getOpeningPrices, getVolatility}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.util.{Failure, Random, Success}
import scala.concurrent.{Await, Future, Promise}

/**
 * The class that will take value below to simulate the movement of a stock for the next <period> days.
 * @param name           : this discribe the name of the stock.
 * @param currentPrice   : starting price (today price).
 * @param n         : the length of time in the future in days.
 */
class StockPredictor(val name: String, currentPrice: Double, n: Double) {
  val dt: Double = 1 / n
  val openingPrices: List[Float] = getOpeningPrices(name)
  val volatility: Double = getVolatility(openingPrices)
  val expectedReturn: Double = getExpectedReturn(openingPrices)
  val possiblePrice: List[List[Double]] = getMultiverseOfPrice()

  /**
   * override toString so it will be easier to read
   * @return
   */
  override def toString: String = {
    s"Name: $name \nCurrent Price: $currentPrice \nExpected Return: $expectedReturn \nTime Period: $n \nVotality: $volatility"
  }

  /**
   * use GBM to get next day price
   * @return currentPrice + predicted change in price
   */
  def nextPrice(): Double = {
    currentPrice + (currentPrice * ((expectedReturn * dt) + (volatility * math.sqrt(dt) * Random.nextGaussian())))
  }

  def nextPrice(cp: Double): Double = {
    cp + (cp * ((expectedReturn * dt) + (volatility * math.sqrt(dt) * Random.nextGaussian())))
  }

  /**
   * Generate next n price
   * @return
   */
  def prices(): List[Double] = {
    // I Love LazyList
    def priceList: LazyList[Double] = {
      def from(i: Double): LazyList[Double] = {
        i #:: from(nextPrice(i))
      }
      from(currentPrice)
    }
    priceList.take(n.toInt).toList
  }

  /**
   * generate 1000 possibility of a stock price for the next n days
   * @return
   */
  def getMultiverseOfPrice(): List[List[Double]] = {
    val futures: List[Future[List[Double]]] =
      List.fill(1000)(Future {
        prices()
      })
    val future: Future[List[List[Double]]] = Future.foldLeft(futures) (List[List[Double]]()) ((acc, e) => acc :+ e)

    val f = Await.result(future, Duration.Inf)
    f
  }

  /**
   * Calculate the possibility of making profit of $margin
   * @param margin: margin of profit
   * @return
   */
  def profitChance(margin: Double): Double ={
    def helper(price: Double, margin: Double): Double = {
      if (price > currentPrice+(currentPrice*margin/100)) 1 else 0
    }
    val endPrice = possiblePrice.map(p => p.last).map(i => helper(i,margin))
    endPrice.sum/endPrice.length
  }

  /**
   * Calculate the possibility of losing money
   * @return
   */
  def lossChance(): Double = {
    def helper(price: Double): Double = {
      if (price < currentPrice) 1 else 0
    }
    val endPrice = possiblePrice.map(p => p.last).map(i => helper(i))
    endPrice.sum/endPrice.length
  }

}

/*
https://scala-lang.org/api/3.x/scala/util/Random$.html#nextGaussian-999
 */

