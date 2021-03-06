package de.havemann.transformer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class MicroserviceTransformerServer

fun main(args: Array<String>) {
    runApplication<MicroserviceTransformerServer>(*args)
}