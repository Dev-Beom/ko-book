package com.benn.kobook.redis.model

import jakarta.persistence.Id
import org.springframework.data.redis.core.RedisHash
import java.io.Serializable

@RedisHash("redisObject")
data class RedisObject(
    @Id
    val id: Long = 0L,
    val content: String = ""
) : Serializable