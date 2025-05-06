package edu.ntnu.idatt2106.gr6.backend.DTOs

import edu.ntnu.idatt2106.gr6.backend.model.NotificationType
import java.time.Instant
import java.util.UUID

data class CreateNotificationRequest(
    val title: String,
    val description: String,
    val type: NotificationType,
    val userId: String? = null,
    val eventId: String? = null,
    val markerId: String? = null,
    val storageId: String? = null,
)

