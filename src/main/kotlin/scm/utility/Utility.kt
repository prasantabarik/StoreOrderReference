package scm.utility

import io.dapr.client.DaprClient
import io.dapr.client.DaprClientBuilder

import java.io.FileInputStream
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
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

    fun publish(PUBSUB_NAME: String, TOPIC_NAME: String, message: String) {
        getClientInstance()?.publishEvent(PUBSUB_NAME, TOPIC_NAME, message)?.block()
        println("Published message: $message")

//        message.stream().forEach { getClientInstance()?.publishEvent(PUBSUB_NAME, TOPIC_NAME, message)?.block() }

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
        val client : DaprClient = DaprClientBuilder().build()

        var mapParams: MutableMap<String, String> = mutableMapOf<String, String>()
        mapParams.put("metadata.namespace", getConfig()["namespace"].toString())

//        var secret = client.getSecret(secretStore,secretKey).block()
        var secret = client.getSecret(secretStore,secretKey, mapParams).block()
        println(secret?.get(secretKey.toString()))
        return secret?.get(secretKey.toString())
    }


}