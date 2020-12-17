package scm.utility

import com.mongodb.client.MongoChangeStreamCursor
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Aggregates
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Projections
import com.mongodb.client.model.changestream.ChangeStreamDocument
import com.mongodb.client.model.changestream.FullDocument
import org.bson.Document

object Utility {

    fun StoreOrderReference() {
        val collection: MongoCollection<Document> = DataBaseConnectionConfig.mongoCollection()

        try {
            var cursor: MongoChangeStreamCursor<ChangeStreamDocument<Document>> = collection.watch(listOf(
                    Aggregates.match(Filters.`in`("operationType", listOf("insert", "update", "replace"))),
                    Aggregates.project(Projections.fields(Projections.include("_id", "ns", "documentKey", "fullDocument")))))
                    .fullDocument(FullDocument.UPDATE_LOOKUP).cursor()

            while (cursor.hasNext()) {
                var csDoc: ChangeStreamDocument<Document> = cursor.next()
                println("INSERT/UPDATE DB OPERATIONS: " + csDoc.toString())
                Client.invokeService(Config.properties!!["appid-publish"].toString(), Config.properties!!["publish"].toString(), csDoc.toString())
            }

            cursor.close()
        } catch (ex: Exception) {
            println(ex.message)
        } finally {
            //clean up
        }
    }

    fun publish(PUBSUB_NAME: String, TOPIC_NAME: String, message: String) {
        Client.instance?.publishEvent(PUBSUB_NAME, TOPIC_NAME, message)?.block()
        println("Published message: $message")

        try {
            Thread.sleep((1000 * Math.random()).toLong())
        } catch (e: InterruptedException) {
            e.printStackTrace()
            Thread.currentThread().interrupt()
            return
        }
    }

    fun getUtilitySecret(secretStore: String, secretKey: String): String? {
        var mapParams: MutableMap<String, String> = mutableMapOf<String, String>()
        mapParams.put("metadata.namespace", Config.properties!!["namespace"].toString())

        var secret = Client.instance?.getSecret(secretStore,secretKey, mapParams)?.block()

        return secret?.get(secretKey.toString())
    }
}