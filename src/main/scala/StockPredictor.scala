import Retriever.{getExpectedReturn, getOpeningPrices, getVolatility}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.util.{Failure, Random, Success}
import scala.concurrent.{Await, Future, Promise}

/**
 * The class that will take value below to simulate the movement of a stock for the next <period> days.
 * @param name           : this discribe the name of the stock.
 * @param currentPrice   : starting price (today price).
 * @param period         : the length of time in the future in days.
 */
class StockPredictor(val name: String, currentPrice: Double, period: Double) {
  val dt = 1 / period
  val openingPrices = getOpeningPrices(name)
  val votality = getVolatility(openingPrices)
  val expectedReturn = getExpectedReturn(openingPrices)

  /**
   * override toString so it will be easier to read
   * @return
   */
  override def toString: String = {
    s"Name: $name \nCurrent Price: $currentPrice \nExpected Return: $expectedReturn \nTime Period: $period \nVotality: $votality"
  }

  /**
   * use GBM to get next day price
   * @return currentPrice + predicted change in price
   */
  def nextPrice(): Double = {
    currentPrice + (currentPrice * ((expectedReturn * dt) + (votality * math.sqrt(dt) * Random.nextGaussian())))
  }

  def nextPrice(cp: Double): Double = {
    cp + (cp * ((expectedReturn * dt) + (votality * math.sqrt(dt) * Random.nextGaussian())))
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
    priceList.take(period.toInt).toList
  }

  /**
   * generate 1000 possibility of a stock price
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

}

/*
https://scala-lang.org/api/3.x/scala/util/Random$.html#nextGaussian-999
 */

