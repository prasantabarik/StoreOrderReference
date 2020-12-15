/*
 * Copyright (c) Microsoft Corporation.
 * Licensed under the MIT License.
 */
package scm.service

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

/**
 * Dapr's HTTP callback implementation via SpringBoot.
 * Scanning package io.dapr.springboot is required.
 */
@SpringBootApplication(scanBasePackages = ["io.dapr.springboot", "scm.service","scm.controller"])
class DaprApplication {
    /**
     * Starts Dapr's callback in a given port.
     * @param port Port to listen to.
     */
    fun start(port: Int) {
        println("SERVER PORT :: " + port)
        val app = SpringApplication(DaprApplication::class.java)
        app.run(String.format("--server.port=%d", port))
    }
}