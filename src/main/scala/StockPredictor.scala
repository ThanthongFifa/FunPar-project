/**
 * The class that will take value below to simulate the movement of a stock for the next <period> days.
 * @param name: this discribe the name of the stock.
 * @param currentPrice: starting price (today price).
 * @param expectedReturn: expected ravenue of this stock.
 * @param period: the length of time in the future in days.
 * @param votality: the votality of the stock.
 */
class StockPredictor(val name: String, currentPrice: Double, expectedReturn: Double, period: Double, votality: Double) {

  override def toString: String = {
    s"Name: $name \nCurrent Price: $currentPrice \nExpected Return: $expectedReturn \nTime Period: $period \nVotality: $votality"
  }

}

