
package scm.controller.publisher

import org.springframework.web.bind.annotation.*
import scm.utility.Utility

/**
 * SpringBoot Controller to handle input binding.
 */
@RestController
@RequestMapping("/publish")
class PublisherController {
    @PostMapping
    fun publishMessage(@RequestBody msg: String) {
        Utility.publish(Utility.getConfig()["pubsub"].toString(), Utility.getConfig()["topic"].toString(), msg)
    }
}
