package seleniumTool

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import scala.collection.JavaConverters._
import scala.util.control.Breaks._ //need for scala break statement
import scala.concurrent.Await
//import scala.concurrent.duration.TimeUnit
import scala.concurrent.duration.Duration

import scala.collection.mutable.ListBuffer

import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.remote.Augmenter // experimental class!

import java.net.URL;
import java.io.File;
import java.util.concurrent.TimeUnit
import org.apache.commons.io.FileUtils;

import org.mongodb.scala.MongoCollection;

/* for error:  Cannot find an implicit ExecutionContext */
import scala.concurrent.ExecutionContext.Implicits.global

import doctype._;


/*
 * how should we think about this class...? It's a single Selenium driver instance, perhaps it should
 * inherit from an abstract class that always has a driver and then takes a "master artist" for which it 
 * will scrape all buyers
 *  1) getAllAlbums
 *  2) getAllBuyers
 * 3) 
 */
class BandCampCrawler(baseURL : String) extends Crawler() {


  driver.get( baseURL );

   //waitForLoad.until(
   //   ExpectedConditions.elementToBeClickable( By.className("playbutton") ) // should be on all bandcamp pages
   //)

   //println("page loaded...")
   
   /*def this( url : String, driver : RemoteWebDriver)
   {


	   this( url )
	   this.driver = driver;
     println("IN AUX CONSTRUCTOR")
     driver.get(url);
   	 println("connection made...")

     val waitForLoad = new WebDriverWait(driver, 12);

     waitForLoad.until(
        ExpectedConditions.elementToBeClickable( By.className("playbutton") )
     )

     println("page loaded...")
   } */

   def testUpload()
   {
      
      // val testEntry : BandAlbum = BandAlbum("Alex G", "Race", "www", "Philly", "", "");

      /*
      * ( artist: String
          , album: String
          , url: String
          , rawLocation: String
          , parsedLocation: String
          , parent: String)
      *
      *
      *
      *
      */
      


   }

   def printFirst()
   {

      //val collection: MongoCollection[BandAlbum] = db.getCollection("test");

      println("get doc...");
      //val res = collection.find().first().toFuture().foreach(record => record.print() );
      //println( res );

      // org.mongodb.scala.SingleObservable[Long]
      //collection.count().head().foreach(cnt => println("doc count: "+cnt) );

      //println("doc count: " + cnt);

      //collection.find().collect().subscribe((results: Seq[BandAlbum]) => println(s"Found: #${results.size}") )

      //val res = collection.find().first();
      /*
      collection.find().first().subscribe(
          //(doc: org.mongodb.scala.bson.Document) => {
            (doc: BandAlbum) => {
              // operate on an individual document here
              doc.print()
          },
          (e: Throwable) => {
              // do something with errors here, if desired
          },
          () => {
              // this signifies that you've reached the end of your collection
              println("end of collection")
          }
      )
      */
      //val resAwait = Await.result(res.toFuture, Duration(10, TimeUnit.SECONDS)).asInstanceOf[BandAlbum]

      //resAwait.print()


   }


   /*
   *
   * BUYER PAGE METHODS - methods that operate on driver that's on a
   *                           buyer's page
   *
   */

   def testPull() /*  : List[String]*/ =
   {

       var linkList : List[String] = driver.findElements(By.cssSelector("div.collection-title-details")).asScala.toList
                          .map( x => x.findElement(By.cssSelector("a.item-link")).getAttribute("href") )


      println("size of buyers after expanding: " + linkList.size)

   }

   // expand expandBuyerPurchases
   // get basic info
   // get # supposed purchases 
   // get all buys

   def updateBuyerDeets( buyerURL : String, primaryAlbumURL : String, picURL : String) : List[BuyerLink] =
   {

    val supposedPurchases : Int = expandBuyerPurchases( buyerURL )

    val linkList = driver.findElements(By.cssSelector("div.collection-title-details")).asScala.toList
                          .map( x => x.findElement(By.cssSelector("a.item-link")).getAttribute("href") )

    //println( linkList )

    val name = driver.findElement(By.cssSelector("div.name span")).getText
 

    // we're assuming list item #1 in <div class="info"> <li> </li> <li> </li> </div>
    // is the location and #2 is genre...
    //println("# list elements: " + driver.findElements(By.cssSelector("div.info li")).asScala.toList.map( elem => elem.getText ).size)
    //val Array( location : String , genre : String ) = driver.findElements(By.cssSelector("div.info li")).asScala.toArray.map( elem => elem.getText )
    val infoArr : List[String] = driver.findElements(By.cssSelector("div.info li")).asScala.toList.map( elem => elem.getText )

    val location = if (infoArr.size < 2) "" else infoArr(0)
    val genre = if (infoArr.size < 2) infoArr(0) else infoArr(1)

    //println("LOCATION: " + location  )
    //println("GENRE: " + genre )
    println("total # purchases: " + linkList.size)
    println("supposed # purchases: " + supposedPurchases)
    println("for buyer: " + buyerURL)

    /* name: String
          , url: String
          , primaryAlbumURL: String
          , secondaryAlbumURL: String
          , rawLocation: String
          , parsedLocation: String
          , numActualPurchase : Int
          , numScrapedPurchase : Int */
    

    // revise fields in Buyer object
    /*val buyers : List[Buyer] = linkList.map( link => Buyer(name
                                                                              , buyerURL
                                                                              , primaryAlbumURL
                                                                              , link
                                                                              , location
                                                                              , "" 
                                                                              , linkList.size
                                                                              , supposedPurchases
                                                                              , picURL
                                                                              , true  )  ) */

    return Nil : List[BuyerLink]
    //return buyers

   }


   // this a method to scrape a buyer page of all the links 
   // we must use selenium due to "view all <#> items" that we
   // want to expand
   // precondition(s): none - driver navigates to buyer page :-) hooray side effects
   // postcondition(s): returns List of unique URLs that represent albums or songs
   private def expandBuyerPurchases(buyerURL : String /* e.g. https://bandcamp.com/jacobcarl */) : Int =
   {

     driver.get(buyerURL) // example
     //Thread.sleep(10000) // ten seconds 
     val countElement : WebElement = driver.findElement( By.cssSelector("div#grid-tabs-sticky span.count") )

     waitForLoad.until(ExpectedConditions.elementToBeClickable( countElement ))

     val totResults : Int = countElement.getText.toInt
     println("total number of results: " + totResults)
     
     var linkList : List[String] = List[String]() //driver.findElements(By.cssSelector("div.collection-title-details")).asScala.toList;


     var purchaseElems : List[WebElement] = Nil //can't do below b/c WebElement is abstract class
     var lastSizeList : ListBuffer[Int] = new ListBuffer[Int]()
    
      while( ( lastSizeList.size < 3 
             || ! lastSizeList.takeRight(3).forall( _ == lastSizeList.last ) ) && linkList.size < totResults )
      {

        linkList = driver.findElements(By.cssSelector("div.collection-title-details")).asScala.toList
                          .map( x => x.findElement(By.cssSelector("a.item-link")).getAttribute("href") )

        lastSizeList += linkList.size

        println("number of curr visible purchase results is: " + linkList.size)
        // go to bottom of page        
        driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL, Keys.END);
        
        try {
        
          driver.findElement(By.cssSelector("button.show-more")).click();
        
        }
        catch {

          case x : Throwable => println("no button to click!")
        
        }
        purchaseElems = driver.findElements( By.cssSelector("div.collection-title-details") ).asScala.toList

        waitForLoad.until(ExpectedConditions.elementToBeClickable( purchaseElems.last ))

      }

      return totResults 
    
  }
   

   /*
   *
   * BAND ALBUM PAGE METHODS - methods that operate on driver that's on a
   *                           band's album page
   *
   */



   // this is a method to expand the buyers list of an album (by clicking "more...")
   // precondition(s): driver MUST be on loaded band page
   // postcondition(s): driver has all buyers loaded, or at least all that can be loaded 
   //                   returns tuples of the buyer page URLs & image URLs!
   def expandBuyers( maxBuyers : Int ) : List[(String, String)] =
   {

    var numBuyers : Int = 0;
    var buyerList = List[WebElement]();
    //var buyerURLs : List[String] = Nil;
    breakable {


      // maxes out at 3000 buyers for testing sake
      while( driver.findElements(By.cssSelector("a.more-thumbs")).asScala.toList.size > 0 && numBuyers < maxBuyers )
      {
        // get all buyer elements
        buyerList = driver.findElements(By.cssSelector("a.fan.pic")).asScala.toList;
        numBuyers = buyerList.size; 
        println( s"number of buyers: ${ driver.findElements( By.cssSelector("a.fan.pic") ).asScala.toList.size }" );

        var elem : WebElement = driver.findElement(By.cssSelector("a.more-thumbs"))
        //var js: JavascriptExecutor = webDriver.asInstanceOf[JavascriptExecutor]
        driver.asInstanceOf[JavascriptExecutor].executeScript("arguments[0].scrollIntoView(true);", elem);

        //driver.findElement(By.cssSelector("a.more-thumbs"))
        
        /* below is USEFUL code for taking screenshots

        val augmentedDriver = new Augmenter().augment(driver);
        val filed: File = driver.getScreenshotAs(OutputType.FILE);
        println("taking screenshot")
        FileUtils.copyFile(filed, new File("/usr/src/app/MongoScala/screenshots/bandcamp_2.jpg"))
        */ 

        try {

          waitForLoad.until(
            ExpectedConditions.visibilityOfElementLocated( By.cssSelector("a.more-thumbs") ) // should be on all bandcamp pages
          )
        }
        catch
        {


          case x: TimeoutException => 
          {
            println("Timeout Exception");
            break;
          }

        }
        // need to add ExpectedConditions.visibilityOfElementLocated !
        
        elem.click();
        
      }
      

    }
      //List[(String, String)]
      val buyerURLs : List[(String, String)] = buyerList.map(x => (x.getAttribute("href"), x.findElement(By.cssSelector("img.thumb")).getAttribute("src")) );

      //println(buyerURLs.take(10));

      return buyerURLs;
    

   }

   def printBuyers()
   {

    var thumbBuyers : List[WebElement] = driver.findElements(By.cssSelector("a.more-thumbs")).asScala.toList
    //var numBuyers = thumbBuyers.size;
    println(s"number of visible buyers: ${ thumbBuyers.size }")
    

   }




   /* method to get the user URLs from a band's album page
      precondition(s): the band's album page has all buyers expanded ( see expandBuyers )
      postcondition(s): returns List of Strings containing buyer URLs
   */
   def getBuyerURLs() : List[String] =
   {

      val buyerURLs : List[String] = driver.findElements(By.cssSelector("a.fan.pic")).asScala.toList.map( x => x.getAttribute("href") )

      buyerURLs

   }


   def getAlbumDeets( albumURL : String ) : BandAlbum = 
   //def getAlbumDeets()
   {

    assert( albumURL == driver.getCurrentUrl() )

    val albumTitle : String = driver.findElement(By.cssSelector("#name-section .trackTitle")).getAttribute("innerText").trim()
    val artist : String = driver.findElement(By.cssSelector("#name-section a")).getAttribute("innerText").trim()
    val location : String = driver.findElement(By.cssSelector("#band-name-location .location")).getAttribute("innerText").trim()


    //println(s" number of album title sections: ${albumTitle.size} ")
    println(s"album title: $albumTitle ") 
    println(s"artist name: $artist ")

    /*
    val albumDeetMap = Map( "title" -> albumTitle
                           ,"artist" -> artist
                           ,"location" -> location)

    albumDeetMap
    */

    return BandAlbum( albumURL
          , artist
          , albumTitle
          , location
          , ""
          , ""
          , false) 

   }


   /*
   *
   * RANDOM ACCESSORY METHODS
   *
   */


   def waitGetScreenshot()
   {

      Thread.sleep(15000); // sleepy boi 

      println("ok get screenshot");
      val filed: File = driver.getScreenshotAs(OutputType.FILE);

      val conciseNameArr : Array[String] = baseURL.split("\\.", 3);

      val fileName : String = "/usr/src/app/MongoScala/screenshots/"+ conciseNameArr(1) +".jpg";

      println("Ok saving " + fileName);

      
      FileUtils.copyFile(filed, new File(fileName))

   }


}




