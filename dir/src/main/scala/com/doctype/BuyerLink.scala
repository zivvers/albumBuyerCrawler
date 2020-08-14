package doctype
//import doctype.BandCampPage; -> already in scope
import org.mongodb.scala.bson.ObjectId;
//import org.mongodb.scala.bson.ISODate;
import java.util.Date;

/* a buyer link represents a single instance of a buyer that bought 
   the primary album and secondary album */

object BuyerLink {

   def apply( name: String
          , url: String
   				, primaryAlbumURL: String
   				, secondaryAlbumURL: String
   				, rawLocation: String
          , parsedLocation: String): BuyerLink =
      new BuyerLink( new ObjectId(), name, url, primaryAlbumURL, secondaryAlbumURL, rawLocation, parsedLocation, new Date() )   


}
/* NOTE: use url as ID! */
/* wtf does "warning: a pure expression does nothing in statement position" mean in regards to below...*/
case class BuyerLink(_id: ObjectId
						, name: String
						, url: String
            , primaryAlbumURL: String
            , secondaryAlbumURL: String
            , rawLocation: String
            , parsedLocation: String
            , numActualPurchase: Int
            , numScrapedPurchase: Int  
					  , dateAccessed : Date) extends BandCampPage(_id, url, rawLocation, parsedLocation)
{

	override def print() : Unit =
  {
    printf("buyer name: " + name + ", url: " + url + ", unparsed location: " + rawLocation);
  }
}
