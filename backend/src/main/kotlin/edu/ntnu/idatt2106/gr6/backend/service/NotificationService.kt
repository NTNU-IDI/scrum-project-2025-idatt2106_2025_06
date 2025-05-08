package edu.ntnu.idatt2106.gr6.backend.service

import edu.ntnu.idatt2106.gr6.backend.DTOs.EventResponse
import edu.ntnu.idatt2106.gr6.backend.model.Event
import edu.ntnu.idatt2106.gr6.backend.model.Notification
import edu.ntnu.idatt2106.gr6.backend.model.NotificationType
import edu.ntnu.idatt2106.gr6.backend.repository.NotificationRepository
import org.springframework.stereotype.Service

@Service
class NotificationService(
    private val notificationRepository: NotificationRepository,
    private val eventService: EventService
) {
    private val logger = org.slf4j.LoggerFactory.getLogger(NotificationService::class.java)
    fun sendNotification(): List<EventResponse> {
        return eventService.getAllEvents()
    }

    fun getNewestNotifications(): List<Notification> {
        try {
            val notifications: List<Notification> = notificationRepository.getRecentMarkerEventNews()
            logger.info("Fetched notifications: $notifications")
            return notifications
        } catch (e: Exception) {
            throw RuntimeException("Error fetching notificationssss: ${e.message}")
        }
    }

    fun getNews(): List<EventResponse> {
        return eventService.getAllEvents()
    }

    fun getRecentAlerts(): List<Notification> {
        return notificationRepository.getRecentMarkerEventNews()
    }


    fun senddddNotification(notification: Notification): Notification {
        return notificationRepository.saveNotification(notification)
    }
}