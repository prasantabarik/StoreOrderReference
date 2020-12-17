/*
 * Copyright (c) Microsoft Corporation.
 * Licensed under the MIT License.
 */
package scm.service

import org.apache.commons.cli.CommandLineParser
import org.apache.commons.cli.DefaultParser
import org.apache.commons.cli.Options
import scm.service.publisher.DaprApplicationPublisher
import scm.utility.Utility
import kotlin.jvm.JvmStatic
import java.lang.Exception

/**
 * Message publisher.
 * 1. Build and install jars:
 * mvn clean install
 * 2. Run the program:
 * dapr run --components-path ./components --app-id publisher --app-port 8085 --dapr-http-port 3006 -- \
 * java -jar  target/dapr-service-exec.jar scm.service.Publisher -p 8085
 */
object Publisher {
    private const val NUM_MESSAGES = 1

    /**
     * This is the entry point of the publisher app example.
     * @param args Args, unused.
     * @throws Exception A startup Exception.
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
        DaprApplicationPublisher().start(port)
        // Utility.StoreOrderReference()
    }
}