package scm.utility

import java.io.FileInputStream
import java.util.*

object Config {
    var properties: Properties?  = null

    init {
        properties = FileInputStream(System.getProperty("user.dir") + "\\service.config").use {
            Properties().apply {
                load(it)
            }
        }
    }
}