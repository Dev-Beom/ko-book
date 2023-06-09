package com.benn.kobook.kafka.config

import com.benn.kobook.common.KoBookConfiguration
import com.benn.kobook.kafka.model.Event
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.listener.ContainerProperties
import org.springframework.kafka.listener.DefaultErrorHandler
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.util.backoff.FixedBackOff

@Configuration
class ConsumerConfig(
    @Value("\${spring.kafka.bootstrap-servers}")
    private val BOOTSTRAP_SERVER: String,

    @Value("\${spring.kafka.consumer.enable-auto-commit}")
    private val ENABLE_AUTO_COMMIT: String,

    @Value("\${spring.kafka.consumer.auto-offset-reset}")
    private val AUTO_OFFSET_RESET: String,

    @Value("\${spring.kafka.consumer.batch-size}")
    private val BATCH_SIZE: String,

    @Value("\${spring.kafka.consumer.concurrency}")
    private val CONCURRENCY: String
) : KoBookConfiguration() {
    private val logger = LoggerFactory.getLogger(ConsumerConfig::class.java)

    init {
        super.initMessage(
            ConsumerConfig::class.java,
            "BOOTSTRAP_SERVER" to BOOTSTRAP_SERVER,
            "ENABLE_AUTO_COMMIT" to ENABLE_AUTO_COMMIT,
            "AUTO_OFFSET_RESET" to AUTO_OFFSET_RESET,
            "BATCH_SIZE" to BATCH_SIZE,
            "CONCURRENCY" to CONCURRENCY
        )
    }

    @Bean
    fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, Event> {
        val containerFactory = ConcurrentKafkaListenerContainerFactory<String, Event>()
        containerFactory.consumerFactory = consumerFactory()
        containerFactory.isBatchListener = true
        containerFactory.containerProperties.ackMode = ContainerProperties.AckMode.MANUAL
        containerFactory.setCommonErrorHandler(customErrorHandler())
        containerFactory.setConcurrency(CONCURRENCY.toInt())
        return containerFactory
    }

    private fun customErrorHandler(): DefaultErrorHandler =
        DefaultErrorHandler(
            { consumerRecord, exception ->
                logger.error("consume Fail. topic: ${consumerRecord.topic()}, key: ${consumerRecord.key()}, value: ${consumerRecord.value()} \n exception: ${exception.message}")
            }, FixedBackOff(1000, 1)
        )


    @Bean
    fun consumerFactory(): ConsumerFactory<in String, in Event> =
        DefaultKafkaConsumerFactory(
            consumerProperties(),
            StringDeserializer(),
            ErrorHandlingDeserializer(JsonDeserializer<Event>())
        )


    @Bean
    fun consumerProperties(): Map<String, Any> {
        return hashMapOf(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to BOOTSTRAP_SERVER,
            ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG to ENABLE_AUTO_COMMIT,
            ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to AUTO_OFFSET_RESET,
            ConsumerConfig.MAX_POLL_RECORDS_CONFIG to BATCH_SIZE,
            JsonDeserializer.VALUE_DEFAULT_TYPE to Event::class.java,
            JsonDeserializer.TRUSTED_PACKAGES to "*"
        )
    }
}