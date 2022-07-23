import scala.io.Source
import scala.collection.mutable.ListBuffer
import sys.process._
import java.net.URL
import java.io.File
import scala.language.postfixOps


object Retriever extends App {
  // API key is limited to 500 requests per day, 5 per minute
  // https://www.alphavantage.co/documentation/
  // https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY_EXTENDED&symbol=IBM&interval=15min&slice=year1month1&apikey=E007RI6Q8GHF36VA

  // Download the csv file from the url
  def downloadCsv(url: String, filename: String): String = {
    new URL(url) #> new File(filename) !!
  }

  // FIXME:
  //  - if possible make this concurrent
  //  - if possible, get other stock info like P/E, dividend, ect.
  //  - Lastly, put those info in the report in StockPredictor

  def getOpeningPrices(stock: String): (List[Float],Int) = {
    if (!new java.io.File(s"$stock.csv").isFile){
      println("No file found, downloading new csv file...")
      val csv = downloadCsv(s"https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=$stock&apikey=E007RI6Q8GHF36VA&datatype=csv",
        s"$stock.csv")
      println("Download completed.")
    }

    val bufferedSource = Source.fromFile(s"$stock.csv")
    val data = bufferedSource.getLines.drop(1).map(_.split(",").map(_.trim)).toList
    val openingPrices: List[Float] = data.map(_(1).toFloat)
    val tradeVolume: List[Int] = data.map(_(5).toInt)
    val totalTradeVolume = tradeVolume.sum
    bufferedSource.close()

    (openingPrices.reverse, totalTradeVolume)
  }

  def getVolatility(prices: scala.collection.Seq[Float], n: Double): Double = {
    //calculate n(num of day) Volatility
    val n = prices.length
    val mean = prices.sum/n

    val sd = prices.map(i => math.pow(i - mean,2)).sum/n
    math.sqrt(sd) * math.sqrt(n)
  }

  def getExpectedReturn(prices: scala.collection.Seq[Float]): Double = {
    val initPrice = prices.head
    val ror = prices.map(price => ((price-initPrice)/initPrice)*100)
    ror.sum/ror.length
  }

  println(getOpeningPrices("IBM"))
}