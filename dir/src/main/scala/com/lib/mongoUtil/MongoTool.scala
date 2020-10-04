package lib.mongoUtil

import org.mongodb.scala.MongoClient
import org.mongodb.scala.MongoDatabase;
import org.mongodb.scala.MongoCollection;
import org.mongodb.scala.Observer
import org.mongodb.scala.Subscription
import org.mongodb.scala.Completed

import org.mongodb.scala.bson.codecs.Macros._
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.bson.codecs.configuration.CodecRegistries.{fromRegistries, fromProviders}

import scala.concurrent.{Promise, Await}
import scala.util.{Failure, Success}
import scala.concurrent.duration.Duration

import scala.concurrent.ExecutionContext.Implicits.global

import doctype._;

class MongoTool(dataB: String) {

   private val codecRegistry = fromRegistries(fromProviders( classOf[BandCampPage] ), DEFAULT_CODEC_REGISTRY );

   private val uri : String = "mongodb://db:27017"
                     
   private val client : MongoClient = MongoClient( uri );

   private val db: MongoDatabase = client.getDatabase( "test" ).withCodecRegistry(codecRegistry);

   


   /*
   def listDB() = {


    return client.listDatabaseNames()


   }
   */

   def insertList( docList : List[BandCampPage], collName : String) : Unit =
   {


      val collection: MongoCollection[BandCampPage] = db.getCollection( collName );
      
      val insertFuture = collection.insertMany( docList ).toFuture().andThen {
            /* side effects indicating success/failure 
               need to convert to logging at some point */
            case Success(successMsg) => println("successful insertion")
            case Failure(failureMsg) => print("insertion error: {}", failureMsg)
      }

      Await.result(insertFuture, Duration(5, "seconds")) // let

      

   }

   def closeConnection() : Unit = {

    client.close()

   }


   def getDocCount() : Int =
   {

    1


   }




}