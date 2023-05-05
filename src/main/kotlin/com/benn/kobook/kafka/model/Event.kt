package com.benn.kobook.kafka.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class Event(
    val type: EventType,
    val time: Long
)

enum class EventType(val type: String) {
    JOIN("join"), LEAVE("leave");

    companion object {
        @JsonCreator
        @JvmStatic
        fun fromValue(value: String): EventType {
            return EventType.valueOf(value.uppercase())
        }
    }
}
