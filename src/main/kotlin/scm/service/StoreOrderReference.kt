/*
 * Copyright (c) Microsoft Corporation.
 * Licensed under the MIT License.
 */
package scm.service

import com.mongodb.client.MongoChangeStreamCursor
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Aggregates
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Projections.include
import com.mongodb.client.model.changestream.ChangeStreamDocument
import com.mongodb.client.model.changestream.FullDocument
import scm.utility.DataBaseConnectionConfig
import scm.utility.Utility
import org.bson.Document

/**
 * Message publisher.
 * 1. Build and install jars:
 * mvn clean install
 * 2. Run the program:
 * dapr run --components-path ./components --app-id changestream --dapr-http-port 3007 -- java -jar
 * target/dapr-service-exec.jar io.dapr.service.ChangeStream
 */
object StoreOrderReference {

    /**
     * This is the entry point of the publisher app example.
     * @param args Args, unused.
     * @throws Exception A startup Exception.
     */
    @Throws(Exception::class)
    @JvmStatic
    fun mainFunction(args: Array<String>) {
        val collection: MongoCollection<org.bson.Document> = DataBaseConnectionConfig.mongoCollection()

        try {
            var cursor: MongoChangeStreamCursor<ChangeStreamDocument<Document>>  = collection.watch(listOf(
                    Aggregates.match(Filters.`in`("operationType", listOf("insert", "update", "replace"))),
                    Aggregates.project(fields(include("_id", "ns", "documentKey", "fullDocument")))))
                    .fullDocument(FullDocument.UPDATE_LOOKUP).cursor()

            
            while (cursor.hasNext()) {
                var csDoc: ChangeStreamDocument<org.bson.Document>  = cursor.next()
                println("POSTED VALUE: " + csDoc.toString())
                //Publishing messages
                Utility.publish(Utility.getConfig()["pubsub"].toString(), Utility.getConfig()["topic"].toString(), csDoc.toString())
            }

            cursor.close()
        } catch (ex: Exception) {
            println(ex.message)
        } finally {
            //clean up
        }
    }
}
