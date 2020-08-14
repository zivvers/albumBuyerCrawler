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


class MongoTool(dataB: String) {

   private val codecRegistry = fromRegistries(fromProviders(classOf[BandAlbum], classOf[BuyerLink]), DEFAULT_CODEC_REGISTRY );

   private val uri : String = "mongodb://db:27017"
                     
   private val client : MongoClient = MongoClient( uri );

   private val db: MongoDatabase = client.getDatabase( dataB ).withCodecRegistry(codecRegistry);


   def insertList( docList : List[BandCampPage]) : Unit =
   {

      collection.insertMany( docList ).subscribe(
          new Observer[Completed](){

          	var successInsertion : Int = 0

            override def onSubscribe(subscription: Subscription): Unit = {

              println("doc successfully inserted")

            }

            override def onNext(result: Completed): Unit = { successInsertion++ };

            override def onError(e: Throwable): Unit = println(s"Error: $e")

            override def onComplete(): Unit = println("Successfully inserted " + successInsertion + docs)
          }
      );

      /*
          data
      .grouped(1000)
      .foreach(docs => {
        val observable =
          collection.insertMany(docs.map(str => Document(BsonDocument(str))))
        Await.result(observable.head(), 10 seconds)
      })
	  */

   }




   def getDocCount() : Int =
   {




   }




}