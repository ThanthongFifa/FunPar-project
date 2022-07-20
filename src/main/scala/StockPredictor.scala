import scala.util.Random
import scala.concurrent.{Await, Future, Promise}

/**
 * The class that will take value below to simulate the movement of a stock for the next <period> days.
 *
 * @param name           : this discribe the name of the stock.
 * @param currentPrice   : starting price (today price).
 * @param expectedReturn : expected ravenue of this stock.
 * @param period         : the length of time in the future in days.
 * @param votality       : the votality of the stock.
 */
class StockPredictor(val name: String, currentPrice: Double, expectedReturn: Double, period: Double, votality: Double) {
  val dt = 1 / period

  /**
   * override toString so it will be easier to read
   *
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
    return currentPrice + (currentPrice * ((expectedReturn * dt) + (votality * math.sqrt(dt) * Random.nextGaussian())))
  }

  def nextPrice(cp: Double): Double = {
    return cp + (cp * ((expectedReturn * dt) + (votality * math.sqrt(dt) * Random.nextGaussian())))
  }

  /**
   * Generate nest n price
   * @return
   */
  def prices(): List[Double] = {
    def priceList: LazyList[Double] = {
      def from(i: Double): LazyList[Double] = {
        i #:: from(nextPrice(i))
      }
      from(currentPrice)
    }
    priceList.take(period.toInt).toList
  }

}

/*
https://scala-lang.org/api/3.x/scala/util/Random$.html#nextGaussian-999
 */
