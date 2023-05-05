package com.benn.kobook

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KoBookApplication

fun main(args: Array<String>) {
    runApplication<KoBookApplication>(*args)
}
