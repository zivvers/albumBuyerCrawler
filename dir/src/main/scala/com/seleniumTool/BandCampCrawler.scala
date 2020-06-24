package seleniumTool

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TimeoutException;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.Augmenter

import scala.collection.JavaConverters._
import scala.util.control.Breaks._ //need for scala break statement
import scala.concurrent.Await
//import scala.concurrent.duration.TimeUnit
import scala.concurrent.duration.Duration


import java.net.URL;
import java.io.File;
import java.util.concurrent.TimeUnit
import org.apache.commons.io.FileUtils;

import org.mongodb.scala.MongoClient
import org.mongodb.scala.MongoDatabase;
import org.mongodb.scala.MongoCollection;
import org.mongodb.scala.Observer
import org.mongodb.scala.Subscription
import org.mongodb.scala.Completed


import org.mongodb.scala.bson.codecs.Macros._
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.bson.codecs.configuration.CodecRegistries.{fromRegistries, fromProviders}

/* for error:  Cannot find an implicit ExecutionContext */
import scala.concurrent.ExecutionContext.Implicits.global

import doctype._;

class BandCampCrawler(url : String)  {

   private val remoteURL : URL = new URL( "http://remote-webdriver:4444/wd/hub");
   // val bandName = 
   private var driver : RemoteWebDriver = new RemoteWebDriver( remoteURL , DesiredCapabilities.firefox() );

   driver.get(url);
   println("navigating to website ... ")

   
   /* hmm should this b a class variable?? */
   private val waitForLoad = new WebDriverWait(driver, 20, 250);


   private val codecRegistry = fromRegistries(fromProviders(classOf[BandAlbum]), DEFAULT_CODEC_REGISTRY );

   private val uri = "mongodb://db:27017";
                     /// mongodb://mongo:27017
   private val client : MongoClient = MongoClient( "mongodb://db:27017" );

   private val db: MongoDatabase = client.getDatabase("test").withCodecRegistry(codecRegistry);

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
      
      val testEntry : BandAlbum = BandAlbum("Alex G", "Race", "www", "Philly", "");
      
      val collection: MongoCollection[BandAlbum] = db.getCollection("test");

      collection.insertOne(testEntry).subscribe(
          new Observer[Completed](){
            override def onSubscribe(subscription: Subscription): Unit = {

              println("doc successfully inserted")

            }

            override def onNext(result: Completed): Unit = println("inserted!");

            override def onError(e: Throwable): Unit = println(s"Error: $e")

            override def onComplete(): Unit = println("Completed")
          }
      );

   }

   def printFirst()
   {

      val collection: MongoCollection[BandAlbum] = db.getCollection("test");

      println("get doc...");
      //val res = collection.find().first().toFuture().foreach(record => record.print() );
      //println( res );

      // org.mongodb.scala.SingleObservable[Long]
      //collection.count().head().foreach(cnt => println("doc count: "+cnt) );

      //println("doc count: " + cnt);

      //collection.find().collect().subscribe((results: Seq[BandAlbum]) => println(s"Found: #${results.size}") )

      val res = collection.find().first();
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
      val resAwait = Await.result(res.toFuture, Duration(10, TimeUnit.SECONDS)).asInstanceOf[BandAlbum]

      resAwait.print()


   }


   def waitGetScreenshot()
   {

      Thread.sleep(15000); // sleepy boi 

      println("ok get screenshot");
      val filed: File = driver.getScreenshotAs(OutputType.FILE);

      val conciseNameArr : Array[String] = url.split("\\.", 3);

      val fileName : String = "/usr/src/app/MongoScala/screenshots/"+ conciseNameArr(1) +".jpg";

      println("Ok saving " + fileName);
      
      FileUtils.copyFile(filed, new File(fileName))

   }





   // this is a method to expand the buyers list of an album (by clicking "more...")
   // precondition(s): driver MUST be on loaded band page
   // postcondition(s): driver has all buyers loaded, or at least all that can be loaded :( 
   def expandBuyers() 
   {

    var numBuyers : Int = 0;
    var buyerList = List[WebElement]();

    breakable {


      // maxes out at 3000 buyers for testing sake
      while( driver.findElements(By.cssSelector("a.more-thumbs")).asScala.toList.size > 0 && numBuyers < 3000 )
      {
        // get all buyer elements
        buyerList = driver.findElements(By.cssSelector("a.fan.pic")).asScala.toList;
        numBuyers = buyerList.size; 
        println( s"number of buyers: ${ driver.findElements( By.cssSelector("a.fan.pic") ).asScala.toList.size }" );

        var elem : WebElement = driver.findElement(By.cssSelector("a.more-thumbs"))
        //var js: JavascriptExecutor = webDriver.asInstanceOf[JavascriptExecutor]
        driver.asInstanceOf[JavascriptExecutor].executeScript("arguments[0].scrollIntoView(true);", elem);

        //driver.findElement(By.cssSelector("a.more-thumbs"))
        val augmentedDriver = new Augmenter().augment(driver);
        val filed: File = driver.getScreenshotAs(OutputType.FILE);
        println("taking screenshot")
        FileUtils.copyFile(filed, new File("/usr/src/app/MongoScala/screenshots/bandcamp_2.jpg"))

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
        /*
        breakable {
          while( true )
          {
            if ( driver.findElements(By.cssSelector("a.fan.pic")).asScala.toList.size > numBuyers )
            {

              println( s"number of buyers: ${ driver.findElements( By.cssSelector("a.fan.pic") ).asScala.toList.size }" );
              break;
            
            }

          }
        } */
      }

      val buyerURLs = buyerList.map(x => x.getAttribute("href"));

      println(buyerURLs.take(10));


    }


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


   def getAlbumDeets() : Map[String,String] = 
   //def getAlbumDeets()
   {

    val albumTitle : String = driver.findElement(By.cssSelector("#name-section .trackTitle")).getAttribute("innerText").trim()
    val artist : String = driver.findElement(By.cssSelector("#name-section a")).getAttribute("innerText").trim()
    val location : String = driver.findElement(By.cssSelector("#band-name-location .location")).getAttribute("innerText").trim()


    //println(s" number of album title sections: ${albumTitle.size} ")
    println(s"album title: $albumTitle ") 
    println(s"artist name: $artist ")


    val albumDeetMap = Map( "title" -> albumTitle
                           ,"artist" -> artist
                           ,"location" -> location)

    albumDeetMap


   }

  /*
  def main(args: Array[String]) {

     var url:URL = new URL( "http://remote-webdriver:4444/wd/hub");

     var driver = new RemoteWebDriver( url , DesiredCapabilities.firefox());

     driver.get("https://sandy.bandcamp.com/album/race")

     val waitForLoad = new WebDriverWait(driver, 12)

     waitForLoad.until(
        ExpectedConditions.elementToBeClickable( By.className("playbutton") )
     )


     println("TIME FOR STUFF")

     while (true)

     {
     	println("loopin")

     	driver.findElement(By.cssSelector("a.more-thumbs"))

     }


     //val band1 : Band = Band("123", "garbanzo", "http://bandcamp.garbanzo", "philly")

     //println( band1.location)


  }
} */
}




