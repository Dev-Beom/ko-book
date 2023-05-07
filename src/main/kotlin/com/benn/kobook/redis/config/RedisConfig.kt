package com.benn.kobook.redis.config

import com.benn.kobook.common.KoBookConfiguration
import com.benn.kobook.redis.model.RedisObject
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class RedisConfig(
    @Value("\${spring.data.redis.host}")
    val host: String,

    @Value("\${spring.data.redis.port")
    val port: Int
): KoBookConfiguration() {
    @Bean
    fun redisConnectionFactory(): RedisConnectionFactory = LettuceConnectionFactory(host, port)

    @Bean
    fun redisTemplate(): RedisTemplate<*, *> = RedisTemplate<Any, Any>().apply {
        setConnectionFactory(redisConnectionFactory())
        keySerializer = StringRedisSerializer()
        hashKeySerializer = StringRedisSerializer()
        valueSerializer = StringRedisSerializer()
    }

    @Bean
    fun objectRedisTemplate(): RedisTemplate<*, *> = RedisTemplate<Any, Any>().apply {
        setConnectionFactory(redisConnectionFactory())
        keySerializer = StringRedisSerializer()
        valueSerializer = Jackson2JsonRedisSerializer(RedisObject::class.java)
    }
}