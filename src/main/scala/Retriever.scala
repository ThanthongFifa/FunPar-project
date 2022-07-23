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

  // get opening prices at 15 minute intervals within the past 30 days
  // FIXME:
  //  - Lorenzo, plz make this function return 1entry/day. currently return 1/15 mins *****Fixed
  //  - if possible make this concurrent
  //  - if possible, get other stock info like P/E, dividend, ect.
  //  - and maybe calculate avg trade volume/day. *****DONE but i switch to total trade volume
  //  - Lastly, put those info in the report in StockPredictor
  def getOpeningPrices(stock: String): (List[Float],Int) = {
    if (!new java.io.File(s"$stock.csv").isFile){
      println("Downloading CSV")
      val csv = downloadCsv(s"https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=$stock&apikey=E007RI6Q8GHF36VA&datatype=csv",
        s"$stock.csv")
    }

    val bufferedSource = Source.fromFile(s"$stock.csv")
    var openingPricesMut = new ListBuffer[Float]()
    var date = ""
    var totalTradeVolume = 0
    for (line <- bufferedSource.getLines.drop(1)) {
      val cols = line.split(",").map(_.trim)
      totalTradeVolume =  totalTradeVolume + cols.last.toInt
      openingPricesMut += cols(1).toFloat
    }
    bufferedSource.close
    (openingPricesMut.toList.reverse,totalTradeVolume)
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