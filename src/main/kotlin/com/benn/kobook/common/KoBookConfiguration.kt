package com.benn.kobook.common

import org.slf4j.LoggerFactory

open class KoBookConfiguration {
    private val logger = LoggerFactory.getLogger(KoBookConfiguration::class.java)

    fun initMessage(clazz: Class<*>, vararg pairs: Pair<String, Any>) {
        val stringBuilder = StringBuilder("\n##${clazz.simpleName} information##")
        pairs.forEach { (key, value) ->
            stringBuilder.append("\n$key: $value")
        }
        stringBuilder.append("\n")
        logger.info(stringBuilder.toString())
    }
}