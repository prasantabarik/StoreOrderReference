package scm.utility

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection

object DataBaseConnectionConfig {
    var mongoClient: MongoClient? = null

    init {
        val connectionString = ConnectionString(Utility.getUtilitySecret(Config.properties!!["secretStore"].toString(), Config.properties!!["secretKey"].toString()).toString()  + "&retrywrites=false")
        val mongoClientSettings = MongoClientSettings.builder().applyConnectionString(connectionString).build()
        mongoClient = MongoClients.create(mongoClientSettings)
    }

    fun mongoCollection(): MongoCollection<org.bson.Document> {
        return mongoClient!!.getDatabase(Config.properties!!["database"].toString()).getCollection(Config.properties!!["collection"].toString())
    }
}
