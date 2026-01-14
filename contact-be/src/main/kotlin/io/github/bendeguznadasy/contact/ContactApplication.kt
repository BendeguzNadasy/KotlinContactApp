package io.github.bendeguznadasy.contact

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ContactApplication

fun main(args: Array<String>) {
    runApplication<ContactApplication>(*args)
}
