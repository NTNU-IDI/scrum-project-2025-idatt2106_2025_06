package edu.ntnu.idatt2106.gr6.backend.repository

import edu.ntnu.idatt2106.gr6.backend.model.Notification
import edu.ntnu.idatt2106.gr6.backend.model.NotificationType
import edu.ntnu.idatt2106.gr6.backend.model.getNotificationType
import org.springframework.stereotype.Repository
import java.util.UUID
import javax.sql.DataSource

/**
 * Repository for performing database operations on [Notification] entities.
 */
@Repository
class NotificationRepository(
    private val dataSource: DataSource
) {
    private val logger = org.slf4j.LoggerFactory.getLogger(NotificationRepository::class.java)

    /**
     * Saves a new notification to the database.
     *
     * @param notification The notification to save.
     * @return The saved notification.
     */
    fun saveNotification(notification: Notification): Notification {
        val sql = """
            INSERT INTO notifications (id, user_id, event_id, marker_id, storage_id, title, description, type, created_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """.trimIndent()

        dataSource.connection.use { connection ->
            connection.prepareStatement(sql).use { statement ->
                statement.setString(1, notification.id)
                statement.setObject(2, notification.userId)
                statement.setString(3, notification.eventId)
                statement.setString(4, notification.markerId)
                statement.setString(5, notification.storageId)
                statement.setString(6, notification.title)
                statement.setString(7, notification.description)
                statement.setString(8, notification.type.toString())
                statement.setTimestamp(9, java.sql.Timestamp.from(notification.createdAt))
                statement.executeUpdate()
            }
        }
        return notification
    }

    /**
     * Retrieves a notification by its ID.
     *
     * @param id The ID of the notification.
     * @return The found notification.
     * @throws IllegalArgumentException if no notification with the given ID is found.
     */
    fun getNotificationById(id: String): Notification {
        val sql = """
            SELECT * FROM notifications WHERE id = ?
        """.trimIndent()

        dataSource.connection.use { connection ->
            connection.prepareStatement(sql).use { statement ->
                statement.setString(1, id)
                val resultSet = statement.executeQuery()
                if (resultSet.next()) {
                    return Notification(
                        id = resultSet.getString("id"),
                        userId = resultSet.getObject("user_id", UUID::class.java),
                        eventId = resultSet.getString("event_id"),
                        markerId = resultSet.getString("marker_id"),
                        storageId = resultSet.getString("storage_id"),
                        title = resultSet.getString("title"),
                        description = resultSet.getString("description"),
                        type = resultSet.getNotificationType(),
                        createdAt = resultSet.getTimestamp("created_at").toInstant()
                    )
                }
            }
        }
        throw IllegalArgumentException("Notification with ID $id not found")
    }

    /**
     * Deletes a notification by its ID.
     *
     * @param id The ID of the notification to delete.
     * @return `true` if a notification was deleted, `false` otherwise.
     */
    fun deleteNotification(id: String): Boolean {
        val sql = """
            DELETE FROM notifications WHERE id = ?
        """.trimIndent()

        dataSource.connection.use { connection ->
            connection.prepareStatement(sql).use { statement ->
                statement.setString(1, id)
                return statement.executeUpdate() > 0
            }
        }
    }

    /**
     * Retrieves all notifications for a specific user.
     *
     * @param userId The ID of the user.
     * @return A list of notifications belonging to the user.
     */
    fun getAllNotifications(userId: UUID): List<Notification> {
        val sql = """
            SELECT * FROM notification WHERE user_id = ?
        """.trimIndent()

        val notifications = mutableListOf<Notification>()
        dataSource.connection.use { connection ->
            connection.prepareStatement(sql).use { statement ->
                statement.setObject(1, userId)
                val resultSet = statement.executeQuery()
                while (resultSet.next()) {
                    notifications.add(
                        Notification(
                            id = resultSet.getString("id"),
                            userId = resultSet.getObject("user_id", UUID::class.java),
                            eventId = resultSet.getString("event_id"),
                            markerId = resultSet.getString("marker_id"),
                            storageId = resultSet.getString("storage_id"),
                            title = resultSet.getString("title"),
                            description = resultSet.getString("description"),
                            type = resultSet.getNotificationType(),
                            createdAt = resultSet.getTimestamp("created_at").toInstant()
                        )
                    )
                }
            }
        }
        return notifications
    }

    /**
     * Finds all notifications created in the past 24 hours.
     *
     * @return A list of recent notifications.
     */
    fun findNewNotifications(): List<Notification> {
          val sql = """
            SELECT * FROM notification WHERE created_at > NOW() - INTERVAL '1 DAY'
        """.trimIndent()

        val notifications = mutableListOf<Notification>()
        dataSource.connection.use { connection ->
            connection.prepareStatement(sql).use { statement ->
                val resultSet = statement.executeQuery()
                while (resultSet.next()) {
                    notifications.add(
                        Notification(
                            id = resultSet.getString("id"),
                            userId = resultSet.getObject("user_id", UUID::class.java),
                            eventId = resultSet.getString("event_id"),
                            markerId = resultSet.getString("marker_id"),
                            storageId = resultSet.getString("storage_id"),
                            title = resultSet.getString("title"),
                            description = resultSet.getString("description"),
                            type = resultSet.getNotificationType(),
                            createdAt = resultSet.getTimestamp("created_at").toInstant()
                        )
                    )
                }
            }
        }
        return notifications
    }

    /**
     * Retrieves the 10 most recent notifications of type 'event' or 'marker'.
     *
     * @return A list of the most recent event and marker notifications.
     */
    fun getRecentMarkerEventNews(): List<Notification> {
        val sql = """
            SELECT * FROM notification WHERE type IN ('event', 'marker') ORDER BY created_at DESC LIMIT 10
        """.trimIndent()

        val notifications = mutableListOf<Notification>()
        dataSource.connection.use { connection ->
            connection.prepareStatement(sql).use { statement ->
                val resultSet = statement.executeQuery()
                while (resultSet.next()) {
                    notifications.add(
                        Notification(
                            id = resultSet.getString("id"),
                            eventId = resultSet.getString("event_id"),
                            markerId = resultSet.getString("marker_id"),
                            title = resultSet.getString("title"),
                            description = resultSet.getString("description"),
                            type = resultSet.getNotificationType(),
                            createdAt = resultSet.getTimestamp("created_at").toInstant()
                        )
                    )
                }
            }
        }
        return notifications
    }

    /**
     * Maps a single row from a [ResultSet] to a [Notification] object.
     *
     * @param resultSet The result set containing the row data.
     * @return The mapped notification.
     * @throws IllegalArgumentException if an error occurs during mapping.
     */
    fun mapRowToNotification(resultSet: java.sql.ResultSet): Notification {
        try {
            return Notification(
                id = resultSet.getString("id"),
                userId = null,
                eventId = resultSet.getString("event_id"),
                markerId = resultSet.getString("marker_id"),
                storageId = null,
                title = resultSet.getString("title"),
                description = resultSet.getString("description"),
                type = resultSet.getNotificationType(),
                createdAt = resultSet.getTimestamp("created_at").toInstant()
            )
        } catch (e: Exception) {
            throw IllegalArgumentException("Error mapping row to Notification: ${e.message}", e)
        }
    }
}