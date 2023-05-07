package com.benn.kobook.kafka.config

import com.benn.kobook.common.KoBookConfiguration
import com.benn.kobook.kafka.model.Event
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.serializer.JsonSerializer
import java.io.Serializable

@Configuration
class ProducerConfig(
    @Value("\${spring.kafka.bootstrap-servers}")
    private val BOOTSTRAP_SERVER: String,

    @Value("\${spring.kafka.producer.max-block-ms}")
    private val MAX_BLOCK_MS: String
) : KoBookConfiguration() {

    init {
        super.initMessage(
            ProducerConfig::class.java,
            "BOOTSTRAP_SERVER" to BOOTSTRAP_SERVER,
            "MAX_BLOCK_MS" to MAX_BLOCK_MS
        )
    }

    @Bean
    fun kafkaTemplate(): KafkaTemplate<String, Event> {
        val jsonSerializer = JsonSerializer<Event>()
        jsonSerializer.isAddTypeInfo = false
        val factory = DefaultKafkaProducerFactory(producerConfigs(), StringSerializer(), jsonSerializer)
        return KafkaTemplate(factory)
    }

    fun producerConfigs(): Map<String, Serializable> =
        mapOf(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to BOOTSTRAP_SERVER,
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to JsonSerializer::class.java,
            ProducerConfig.MAX_BLOCK_MS_CONFIG to MAX_BLOCK_MS
        )
}