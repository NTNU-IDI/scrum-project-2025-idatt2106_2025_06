package edu.ntnu.idatt2106.gr6.backend.controller

import com.fasterxml.jackson.databind.util.JSONPObject
import edu.ntnu.idatt2106.gr6.backend.model.Notification
import edu.ntnu.idatt2106.gr6.backend.service.NotificationService
import kotlinx.serialization.json.Json
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller

@Controller
class NotificationController(
    private val notificationService: NotificationService,
    private val messagingTemplate: SimpMessagingTemplate
) {
    private val logger = org.slf4j.LoggerFactory.getLogger(NotificationController::class.java)
    @MessageMapping("/public/newsAlerts")
    @SendTo("/topic/public/newsAlerts")
    fun handleNotification(@Payload notification: String): List<Notification> {
        try {
            logger.info("Received notification: $notification")
            val event = notificationService.getNewestNotifications()
            logger.info("Sending notification: $event")
            return event
        } catch (ex: Exception) {
            logger.error("Error while sending notification: ${ex.message}")
            return emptyList()
         }
    }
}