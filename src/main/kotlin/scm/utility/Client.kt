package scm.utility

import io.dapr.client.DaprClient
import io.dapr.client.DaprClientBuilder
import io.dapr.client.domain.HttpExtension

object Client {
    var instance: DaprClient? = null

    init {
        instance = DaprClientBuilder().build()
    }

    fun invokeService(app_id: String, endpoint: String, message: String) {
        instance?.invokeService(app_id, endpoint, message,
                HttpExtension.POST)?.block()
    }
}
