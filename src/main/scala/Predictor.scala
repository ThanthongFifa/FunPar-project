import play.api.libs.json.{JsValue, Json}

object Predictor extends App {

  // Returns a list of opening prices of each day over the last 100 days
  // API key is limited to 500 requests per day, 5 per minute
  // https://www.alphavantage.co/documentation/
  def getOpeningPrices(stock: String): scala.collection.Seq[JsValue] = {
    val r = requests.get(s"https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=$stock&apikey=E007RI6Q8GHF36VA").data
    val json: JsValue = Json.parse(s"""$r""")
    val openingPrices = json \\ "1. open"
    openingPrices
  }

  // The numbers of openingPrices are in the wrong order for some reason.
  // Currently can't figure out why but the parser seems to randomly pick the order of opening prices to put in.
  // https://index.scala-lang.org/playframework/play-json
  println(getOpeningPrices("IBM"))

}