package scm.utility

import com.mongodb.client.MongoChangeStreamCursor
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Aggregates
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Projections
import com.mongodb.client.model.changestream.ChangeStreamDocument
import com.mongodb.client.model.changestream.FullDocument
import io.dapr.client.DaprClient
import io.dapr.client.DaprClientBuilder
import io.dapr.client.domain.HttpExtension
import org.bson.Document
import java.io.FileInputStream
import java.util.*

object Utility {
    var client: DaprClient? = null
    var config: Properties?  = null



    @JvmName("getConfig1")
    fun getConfig(): Properties {
        if(config == null){
            config = loadConfig()
        }
        return config as Properties
    }

    fun getClientInstance(): DaprClient? {

        if(client == null){
            client = DaprClientBuilder().build()
        }

        return client
    }

    fun invokeService(app_id: String, endpoint: String, message: String) {
        getClientInstance()?.invokeService(app_id, endpoint, message,
                HttpExtension.POST)?.block()
    }

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
                Utility.invokeService(Utility.getConfig()["appid-publish"].toString(), Utility.getConfig()["publish"].toString(), csDoc.toString())
            }

            cursor.close()
        } catch (ex: Exception) {
            println(ex.message)
        } finally {
            //clean up
        }
    }

    fun publish(PUBSUB_NAME: String, TOPIC_NAME: String, message: String) {
        getClientInstance()?.publishEvent(PUBSUB_NAME, TOPIC_NAME, message)?.block()
        println("Published message: $message")

        try {
            Thread.sleep((1000 * Math.random()).toLong())
        } catch (e: InterruptedException) {
            e.printStackTrace()
            Thread.currentThread().interrupt()
            return
        }
    }

    fun loadConfig() = FileInputStream(System.getProperty("user.dir") + "\\service.config").use {
        Properties().apply {
            load(it)
        }
    }

    fun getUtilitySecret(secretStore: String, secretKey: String): String? {
        var mapParams: MutableMap<String, String> = mutableMapOf<String, String>()
        mapParams.put("metadata.namespace", getConfig()["namespace"].toString())

        var secret = getClientInstance()?.getSecret(secretStore,secretKey, mapParams)?.block()

        return secret?.get(secretKey.toString())
    }


}