/*
 * Copyright (c) Microsoft Corporation.
 * Licensed under the MIT License.
 */
package scm.service.publisher

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

/**
 * Dapr's HTTP callback implementation via SpringBoot.
 */
@SpringBootApplication(scanBasePackages = ["scm.service.publisher", "scm.controller.publisher"])
class DaprApplicationPublisher {
    /**
     * Starts Dapr's callback in a given port.
     * @param port Port to listen to.
     */
    fun start(port: Int) {
        val app = SpringApplication(DaprApplicationPublisher::class.java)
        app.run(String.format("--server.port=%d", port))
    }
}