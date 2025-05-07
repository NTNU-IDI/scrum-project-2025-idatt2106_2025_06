package edu.ntnu.idatt2106.gr6.backend.repository

import edu.ntnu.idatt2106.gr6.backend.model.Notification
import edu.ntnu.idatt2106.gr6.backend.model.NotificationType
import edu.ntnu.idatt2106.gr6.backend.model.getNotificationType
import org.springframework.stereotype.Repository
import java.util.UUID
import javax.sql.DataSource

@Repository
class NotificationRepository(
    private val dataSource: DataSource
) {
    private val logger = org.slf4j.LoggerFactory.getLogger(NotificationRepository::class.java)
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