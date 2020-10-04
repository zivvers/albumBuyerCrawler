package doctype


//import doctype.BandCampPage; -> already in scope
import org.mongodb.scala.bson.ObjectId;
//import org.mongodb.scala.bson.ISODate;
import java.util.Date;


/* would prefer to make this an abstract class, but 
   can't create mongodb codec for if abstract... */
sealed class BandCampPage/*(_id: Unit
							, url: String
							, rawLocation: String
							, parsedLocation: String)*/ // 
{

	//def print() : Unit
	
}

object Buyer {

	/* full constructor */
   def apply( url: String
          , name: String
		  , purchaseAlbumURLs : List[String]
		  , rawLocation: String
          , parsedLocation: String
          , numActualPurchase : Int
          , numScrapedPurchase : Int
          , picURL : String
          , finalRecord : Boolean ): Buyer =
      new Buyer( url
  					, name
  					, purchaseAlbumURLs
  					, rawLocation
  					, parsedLocation
  					, numActualPurchase
  					, numScrapedPurchase
  					, new Date()
  					, picURL
  					, finalRecord )   

    /* secondary constructor */
   def apply(url: String
		  , primaryAlbumURL: String
		  , picURL : String
          , finalRecord : Boolean ): Buyer =
      new Buyer( url, "", List(primaryAlbumURL), "", "", 0, 0, new Date(), picURL, finalRecord )   


}
/* NOTE: use url as ID! */
/* what does "warning: a pure expression does nothing in statement position" mean in regards to below...*/
case class Buyer(_id: String /* the url of the buyer */
						, name: String
                        , purchaseAlbumURLs : List[String]
                        , rawLocation: String
                        , parsedLocation: String
                        , numActualPurchase: Int
                        , numScrapedPurchase: Int  
					    , dateAccessed : Date
					    , picURL : String
					    , finalRecord : Boolean ) extends BandCampPage/*(_id, url, rawLocation, parsedLocation) */
{

  def print() : Unit =
  {
    printf("buyer name: " + name + ", url: " + _id + ", unparsed location: " + rawLocation);
  }
}


object BandAlbum {
   def apply( url: String
   				, artist: String
   				, album: String
   				, rawLocation: String
   				, parsedLocation: String
   				/*, parent: String */	
			    , picURL : String
			    , finalRecord : Boolean ): BandAlbum =
      new BandAlbum( url, artist, album, rawLocation, parsedLocation, new Date(), picURL, finalRecord )   



}
/* NOTE: use url as ID! */
/* wtf does "warning: a pure expression does nothing in statement position" mean in regards to below...*/
case class BandAlbum(_id: String /* the url of the band's album */
						, artist: String
						, album: String
						, rawLocation: String
						, parsedLocation: String
						/*, parent: String */
					    , dateAccessed : Date
						, picURL : String
					    , finalRecord : Boolean) extends BandCampPage/*(_id, url, rawLocation, parsedLocation) */
{

	def print() : Unit =
    {
    	printf("artist: " + artist + ", album: " + album + ", unparsed location: " + rawLocation);
    }
}