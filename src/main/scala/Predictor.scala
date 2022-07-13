import scala.io.Source
import scala.collection.mutable.ListBuffer
import sys.process._
import java.net.URL
import java.io.File
import scala.language.postfixOps

object Predictor extends App {
  // API key is limited to 500 requests per day, 5 per minute
  // https://www.alphavantage.co/documentation/
  // https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY_EXTENDED&symbol=IBM&interval=15min&slice=year1month1&apikey=E007RI6Q8GHF36VA

  // Download the csv file from the url
  def downloadCsv(url: String, filename: String): String = {
    new URL(url) #> new File(filename) !!
  }

  // get opening prices at 15 minute intervals within the past 30 days
  def getOpeningPrices(stock: String): List[Float] = {
    val csv = downloadCsv(s"https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY_EXTENDED&symbol=$stock&interval=15min&slice=year1month1&apikey=E007RI6Q8GHF36VA",
      s"$stock.csv")

    val bufferedSource = Source.fromFile(s"$stock.csv")
    var openingPricesMut = new ListBuffer[Float]()
    for (line <- bufferedSource.getLines.drop(1)) {
      val cols = line.split(",").map(_.trim)
      openingPricesMut += cols(1).toFloat
    }
    bufferedSource.close
    openingPricesMut.toList
  }

  def getVolatility(prices: scala.collection.Seq[Float]): Double = {
    val n = prices.length
    val mean = prices.sum/n

    val sd = prices.map(i => math.pow(i - mean,2)).sum/n
    math.sqrt(sd)/1000
  }

//  val openingPricesIBM = getOpeningPrices("IBM")
//  val volatility = getVolatility(openingPricesIBM)
//  println(openingPricesIBM)
//  println(volatility)


}