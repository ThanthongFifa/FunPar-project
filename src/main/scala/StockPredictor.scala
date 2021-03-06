import Retriever.{getData, getDividend, getExpectedReturn, getVolatility}

import java.io.PrintWriter
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.util.Random
import scala.concurrent.{Await, Future}

/**
 * The class that will take value below to simulate the movement of a stock for the next <period> days.
 * @param name           : this discribe the name of the stock.
 * @param currentPrice   : starting price (today price).
 * @param n         : the length of time in the future in days.
 */
class StockPredictor(val name: String, currentPrice: Double, n: Double) {
  val dt: Double = 1 / n
  val temp: (List[Float], Int) = getData(name)
  val openingPrices: List[Float] = temp._1
  val tradeVolume: Int = temp._2
  val volatility: Double = getVolatility(openingPrices,n) // annual volatility
  val expectedReturn: Double = getExpectedReturn(openingPrices)
  val possiblePrice: List[List[Double]] = getMultiverseOfPrice
  val endPrice: List[Double] = possiblePrice.map(p => p.last)
  val dividend: Double = getDividend(name)
  val peRatio: Double = getPERatio(openingPrices)

  /**
   * override toString so it will be easier to read
   * @return
   */
  override def toString: String = {
    s"Name: $name \nCurrent Price: $currentPrice \nExpected Return: $expectedReturn \nTime Period: $n days \nVolatility: $volatility\nDividend: $dividend\nP/E Ratio: $peRatio"
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
  def getMultiverseOfPrice: List[List[Double]] = {
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
    val ep = endPrice.map(i => helper(i,margin))
    ep.sum/ep.length
  }

  /**
   * Calculate the possibility of losing money
   * @return
   */
  def lossChance(): Double = {
    def helper(price: Double): Double = {
      if (price < currentPrice) 1 else 0
    }
    val ep = endPrice.map(i => helper(i))
    ep.sum/ep.length
  }

  /**
   * get max and min price
   * @return
   */
  def priceRange(): (Double,Double) = {
    (round(endPrice.max),
      round(endPrice.min))
  }

  /**
   * round x to 2 decimal digits
   * @param x
   * @return
   */
  def round(x: Double): Double = {
    BigDecimal(x).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble
  }

  /**
   * Calculates the PE Ratio
   * @param prices, stock
   * @return 0 if there is no dividend data available
   */

  def getPERatio(prices: scala.collection.Seq[Float]): Double = {
    val n = prices.length
    val avgPrice = prices.sum/n
    avgPrice/dividend
  }

  /**
   * write a simple report to .txt file
   */
  def writeReport(): Unit = {
    val report = new PrintWriter(s"$name _report.txt")
    report.println("================= Basic Info ====================")
    report.println(this.toString)
    report.println("================= Predictions ====================")
    report.println("Note: predict from " + openingPrices.length + " days of data")
    report.println("Total trading volume from data: " + tradeVolume)
    report.println("Tomorrow's price: " + round(nextPrice()))
    report.println("Expected price for the period ended:")
    val pr = priceRange()
    report.println("     max: " + pr._1)
    val avg = round(endPrice.sum/endPrice.length)
    report.println("     avg: " + avg)
    report.println("     min: " + pr._2)
    report.println("Chance of making profit: " + profitChance(1)*100 + "%")
    report.println("Chance of losing money: " + lossChance()*100 + "%")
    if (avg < currentPrice){
      report.println("Trend: downwards trend")
    } else {
      report.println("Trend: upwards trend")
    }
    report.println("==================================================")
    report.close()
  }

  /**
   * print a simple report
   */
  def printReport(): Unit = {
    println("================= Basic Info ====================")
    println(this.toString)
    println("================= Predictions ====================")
    println("Note: predict from " + openingPrices.length + " days of data")
    println("Total trading volume from data: " + tradeVolume)
    println("Tomorrow's price: " + round(nextPrice()))
    println("Expected price for the period ended:")
    val pr = priceRange()
    println("     max: " + pr._1)
    val avg = round(endPrice.sum/endPrice.length)
    println("     avg: " + avg)
    println("     min: " + pr._2)
    println("Chance of making profit: " + profitChance(1)*100 + "%")
    println("Chance of losing money: " + lossChance()*100 + "%")
    if (avg < currentPrice){
      println("Trend: downwards trend")
    } else {
      println("Trend: upwards trend")
    }
    println("==================================================")
  }


}

/*
https://scala-lang.org/api/3.x/scala/util/Random$.html#nextGaussian-999
 */

