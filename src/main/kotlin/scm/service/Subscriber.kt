/*
 * Copyright (c) Microsoft Corporation.
 * Licensed under the MIT License.
 */
package scm.service

import org.apache.commons.cli.CommandLineParser
import org.apache.commons.cli.DefaultParser
import org.apache.commons.cli.Options
import scm.service.subscriber.DaprApplicationSubscriber
import kotlin.jvm.JvmStatic
import java.lang.Exception

/**
 * Service for subscriber.
 * 1. Build and install jars:
 * mvn clean install
 * 2. Run the server:
 * dapr run --components-path ./components --app-id subscriber --app-port 3000 --dapr-http-port 3005 -- \
 * java -jar target/dapr-service-exec.jar scm.service.Subscriber -p 3000
 */
object Subscriber {
    /**
     * This is the entry point for this example app, which subscribes to a topic.
     * @param args The port this app will listen on.
     * @throws Exception An Exception on startup.
     */
    @Throws(Exception::class)
    @JvmStatic
    fun mainFunction(args: Array<String>) {
        val options = Options()
        options.addRequiredOption("p", "port", true, "The port this app will listen on")
        val parser: CommandLineParser = DefaultParser()
        val cmd = parser.parse(options, args)

        // If port string is not valid, it will throw an exception.
        val port = cmd.getOptionValue("port").toInt()
        DaprApplicationSubscriber().start(port)
    }
}