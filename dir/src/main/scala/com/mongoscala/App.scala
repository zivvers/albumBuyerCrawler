package com.mongoscala


//import org.mongodb.scala._ ;

/* document type */

import seleniumTool._
import lib.mongoUtil.MongoTool 
import doctype._;

import java.time._

import java.util.Date;

object App {

  def main(args: Array[String]) {

    var websiteURL : String = "";
    //var creeper : BandCampCrawler = _;
    if (args.length == 0) {
        //throw new IllegalArgumentException("need argument for website ;-)");

        websiteURL = "https://sandy.bandcamp.com/album/race";
        

        //creeper.expandBuyers()
    }
    else {

        websiteURL = args(0);
        
        //creeper.waitGetScreenshot();
    }

    val coll : String = "AlexG_Race"
    

    var creeper : BandCampCrawler = new BandCampCrawler( websiteURL );

    val deets : BandAlbum = creeper.getAlbumDeets( websiteURL ) // just get the artist / album info for page
    
    val buyers : List[ (String, String) ] = creeper.expandBuyers( 300 )

    val dbConnect : MongoTool = new MongoTool("testDB")


    val testItems : List[BandCampPage] = List(  BandAlbum( "alexg.com"
                                                            , "alexg"
                                                            , "race"
                                                            , "philly"
                                                            , ""
                                                            , new Date()
                                                            , "alexgPic.com"
                                                            , true) 
                                                 , BandAlbum( "shrimp.com"
                                                            , "shrimp"
                                                            , "big ocean"
                                                            , "new haven, CT"
                                                            , ""
                                                            , new Date()
                                                            , "shrimpPic.com"
                                                            , true)
                                                 , BandAlbum( "milesDavis.com"
                                                            , "MilesDavis"
                                                            , "Kinda Blu"
                                                            , "New York, NY"
                                                            , ""
                                                            , new Date()
                                                            , "milesSmiles.com"
                                                            , true)     
                                                  , Buyer( "mike.com"
                                                            , "mike"
                                                            , List("milesDavis.com", "alexg.com")
                                                            , "boston"
                                                            , ""
                                                            , 2
                                                            , 2
                                                            , new Date()
                                                            , "mikeFace.com"
                                                            , true )
                                                  , Buyer( "brock.com"
                                                            , "brock"
                                                            , List("milesDavis.com", "alexg.com", "shrimp.com")
                                                            , "amherst, MA"
                                                            , ""
                                                            , 3
                                                            , 3
                                                            , new Date()
                                                            , "brockFace.com"
                                                            , true )
                                                  , Buyer( "sarah.com"
                                                            , "sarah"
                                                            , List("alexg.com", "shrimp.com")
                                                            , "boston"
                                                            , ""
                                                            , 3
                                                            , 3
                                                            , new Date()
                                                            , "brockFace.com"
                                                            , true )    )

                                                  

    // create partial Buyer records
    //val nonFinalBuyers = buyers.map( buyr /*(url, imgURL)*/ => BuyerLink(buyr._1, websiteURL, buyr._2, false) )

    dbConnect.insertList( testItems, "coll" )

    dbConnect.closeConnection()

    //buyers = buyers.take(1) // testing

    //val buyerLinks : List[BuyerLink] = buyers.flatMap( buyr => creeper.getBuyerDeets(buyr,  websiteURL) )

    println("buyer list size: " + buyers.size )
    

    //val testBuyerList = List( BuyerLink("Shrimp", "Shrimp.com", "ShrimpAlbum.com", "???", "Dayton, OH", "", 10, 10) )
    

    








    //deets.print()




    /*
    var testURL : String = "https://bandcamp.com/sometimesgreg";

    //creeper.expandBuyerPurchases( testURL )
    println("size of buyers curr process: " + purchaseURLs)
    //creeper.testPull()
    */
    /*
    ( artist: String
                , album: String
                , url: String
                , rawLocation: String
                , parsedLocation: String
                , parent: String
                , numBuyers: Int)*/
    /*
    val db : MongoTool = new MongoTool("test")

    val testL : List[BandAlbum] = List( BandAlbum("Alex G", "Race", "www...", "Philly", "", "", 500)
                                        , BandAlbum("Alex G", "House", "www...H", "Philly", "", "", 500) )

    db.insertList( testL )
    */

    /*
    println("Website is: " + websiteURL);
    
    creeper.testUpload();

    Thread.sleep(5000);

    creeper.printFirst();

    println("..ok done");
    */
  }

};





