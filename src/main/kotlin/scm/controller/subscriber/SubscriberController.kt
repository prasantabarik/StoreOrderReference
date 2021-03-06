
package scm.controller.subscriber

import io.dapr.Topic
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import reactor.core.publisher.Mono
import java.lang.Void
import io.dapr.client.domain.CloudEvent
import org.springframework.web.bind.annotation.RequestBody
import scm.utility.Config
import scm.utility.Utility
import java.lang.Exception
import java.lang.RuntimeException

/**
 * SpringBoot Controller to handle input binding.
 */
@RestController
class SubscriberController {
    /**
     * Handles a registered publish endpoint on this app.
     * @param body The body of the http message.
     * @param headers The headers of the http message.
     * @return A message containing the time.
     */
    // Load properties from disk.
    val topic: String = Config.properties!!["topic"].toString()
    val pubsub: String = Config.properties!!["pubsub"].toString()

    @Topic(name = "StoreOrderReference", pubsubName = "pubsub")
    @PostMapping(path = ["/StoreOrderReference"])

    fun handleMessage(@RequestBody(required = false) body: ByteArray?,
                      @RequestHeader headers: Map<String?, String?>?): Mono<Void> {

        return Mono.fromRunnable {
            try {
                // Dapr's event is compliant to CloudEvent.
                val envelope = CloudEvent.deserialize(body)
                val message = if (envelope.data == null) "" else envelope.data
                println("Subscriber got the DB Operations alert: $message")
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        }
    }
}
