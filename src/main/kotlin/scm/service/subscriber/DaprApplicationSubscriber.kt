/*
 * Copyright (c) Microsoft Corporation.
 * Licensed under the MIT License.
 */
package scm.service.subscriber

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

/**
 * Dapr's HTTP callback implementation via SpringBoot.
 * Scanning package io.dapr.springboot is required.
 */
@SpringBootApplication(scanBasePackages = ["io.dapr.springboot", "scm.service.subscriber","scm.controller.subscriber"])
class DaprApplicationSubscriber {
    /**
     * Starts Dapr's callback in a given port.
     * @param port Port to listen to.
     */
    fun start(port: Int) {
        val app = SpringApplication(DaprApplicationSubscriber::class.java)
        app.run(String.format("--server.port=%d", port))
    }
}