# FunPar-project: concurrent GMB in Scala

### Team members: 
- Fifa
- Lorenzo 

### Project description
We want to do a stock price simulator using Geometric Brownian Motion(GBM). The project will be divided into 2 main parts. The first part is to work with stock API to get data from the stock market and process that data, then we will use that data to calculate a necessary value for GBM. the second part is to do GBM using Future.

### Work distribution
  - Lorenzo: find good and free API that will provide with necessary data.
  
  - Fifa: Do the GMB using Future
  
Note Lorenzo's spend most time looking for and testing stock API that we can use in scala, so he have fewer commit.

### references

https://medium.com/analytics-vidhya/building-a-monte-carlo-method-stock-price-simulator-with-geometric-brownian-motion-and-bootstrap-e346ff464894

https://www.investopedia.com/articles/07/montecarlo.asp

https://corpgov.law.harvard.edu/2019/08/18/making-sense-of-monte-carlo/

https://scala-lang.org/api/3.x/scala/util/Random$.html#nextGaussian-999

