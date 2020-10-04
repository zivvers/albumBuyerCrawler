package testing


//import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.{Matchers, FlatSpec} 
import org.junit.runner.RunWith 
import org.scalatest.junit.JUnitRunner
import lib.mongoUtil.MongoTool 
import java.util.Date;
import doctype._;


class FirstSpec extends FlatSpec {

	"My MongoTool" can "Ingest Data" in {

		    val dbConnect : MongoTool = new MongoTool("testDB")

		                                                  
		    def main(args: Array[String]): Unit = {


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


		    dbConnect.insertList( testItems, "testColl" )

		    dbConnect.closeConnection()

		    assert( true )
		}

    }
    // create partial Buyer records
    //val nonFinalBuyers = buyers.map( buyr /*(url, imgURL)*/ => BuyerLink(buyr._1, websiteURL, buyr._2, false) )



}