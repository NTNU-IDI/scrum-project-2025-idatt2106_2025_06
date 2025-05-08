package edu.ntnu.idatt2106.gr6.backend.model

import java.time.Instant
import java.util.UUID

sealed class NotificationType {
    object Event : NotificationType()
    object Storage : NotificationType()
    object Other : NotificationType()

    override fun toString(): String {
        return when (this) {
            is Event -> "event"
            is Storage -> "storage"
            is Other -> "other"
        }
    }

    companion object {
        fun fromString(value: String): NotificationType =
            when (value) {
                "event" -> Event
                "storage" -> Storage
                "other" -> Other
                else -> throw IllegalArgumentException("Invalid notification type: $value")
            }
    }
}

data class Notification(
    val id: String,
    val userId: UUID? = null,
    val eventId: String? = null,
    val markerId: String? = null,
    val storageId: String? = null,
    val title: String,
    val description: String,
    val type: NotificationType = NotificationType.Other,
    val createdAt: Instant = Instant.now(),
) {
    init {
        require(title.isNotBlank()) { "Title must be specified" }
        require(description.isNotBlank()) { "Description must be specified" }
    }
}