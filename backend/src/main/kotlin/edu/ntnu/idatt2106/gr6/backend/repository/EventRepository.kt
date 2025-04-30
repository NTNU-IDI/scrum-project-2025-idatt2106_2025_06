package edu.ntnu.idatt2106.gr6.backend.repository

import edu.ntnu.idatt2106.gr6.backend.DTOs.Location
import edu.ntnu.idatt2106.gr6.backend.model.Event
import edu.ntnu.idatt2106.gr6.backend.model.getEventType
import edu.ntnu.idatt2106.gr6.backend.model.getLocation
import edu.ntnu.idatt2106.gr6.backend.model.getSeverity
import edu.ntnu.idatt2106.gr6.backend.model.getStatus
import org.springframework.stereotype.Repository
import java.sql.ResultSet

import javax.sql.DataSource

@Repository
class EventRepository(
    private val dataSource: DataSource
) {
    private val logger = org.slf4j.LoggerFactory.getLogger(EventRepository::class.java)
    fun createEvent(event: Event): Event {
        val sql = """
            INSERT INTO event (id, name, description, type, impact_area_radius_km, mandatory_evacuation_area_radius_km,
            recommended_evacuation_area_radius_km, severity, start_date, end_date, status, location, content)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ST_PointFromText(?, 4326), ?)
        """.trimIndent()

        return dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, event.id)
                stmt.setString(2, event.name)
                stmt.setString(3, event.description)
                stmt.setString(4, event.type.toString())
                stmt.setDouble(5, event.impactAreaRadiusKm?: 0.0)
                stmt.setDouble(6, event.mandatoryEvacuationAreaRadiusKm?: 0.0)
                stmt.setDouble(7, event.recommendedEvacuationAreaRadiusKm?: 0.0)
                stmt.setString(8, event.severity.toString())
                stmt.setTimestamp(9, java.sql.Timestamp.from(event.startDate))
                stmt.setTimestamp(10, java.sql.Timestamp.from(event.endDate))
                stmt.setObject(11, event.status.toString())
                stmt.setObject(12, event.location?.let { "POINT(${it.longitude} ${it.latitude})" })
                stmt.setString(13, event.content)

                val affectedRows = stmt.executeUpdate()
                if (affectedRows == 0) {
                    throw Exception("Creating event failed, no rows affected.")
                }
                event
            }
        }
    }

    fun deleteEvent(eventId: String): Boolean {
        val sql = "DELETE FROM event WHERE id = ?"
        return dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, eventId)
                stmt.executeUpdate() > 0
            }
        }
    }

    fun findAllEvents(): List<Event> {
        val sql = """
        SELECT id, name, description, type, impact_area_radius_km,
        mandatory_evacuation_area_radius_km, recommended_evacuation_area_radius_km,
        severity, start_date, end_date, status,
        ST_AsText(location) as location, content, created_at, updated_at
        FROM event ORDER BY created_at DESC
    """.trimIndent()
        return dataSource.connection.use { conn->
            conn.prepareStatement(sql).use { stmt ->
                stmt.executeQuery().use { rs ->
                    val results = mutableListOf<Event>()
                    while (rs.next()) {
                        results.add(mapRowToEvent(rs))
                    }
                    results
                }
            }
        }
    }

    fun findEventById(eventId: String): Event? {
        val sql = """
            SELECT id, name, description, type, impact_area_radius_km,
            mandatory_evacuation_area_radius_km, recommended_evacuation_area_radius_km,
            severity, start_date, end_date, status,
            ST_AsText(location) as location, content, created_at, updated_at
            FROM event WHERE id = ?
        """.trimIndent()
        return dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, eventId)
                stmt.executeQuery().use { rs ->
                    if (rs.next()) {
                        mapRowToEvent(rs)
                    } else {
                        null
                    }
                }
            }
        }
    }

    fun updateEvent(event: Event): Event {
        val sql = """
            UPDATE event SET name = ?, description = ?, type = ?, impact_area_radius_km = ?, 
            mandatory_evacuation_area_radius_km = ?, recommended_evacuation_area_radius_km = ?, 
            severity = ?, start_date = ?, end_date = ?, status = ?, location = ST_PointFromText(?, 4326), content = ?
            WHERE id = ?
        """.trimIndent()

        return dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, event.name)
                stmt.setString(2, event.description)
                stmt.setString(3, event.type.toString())
                stmt.setDouble(4, event.impactAreaRadiusKm?: 0.0)
                stmt.setDouble(5, event.mandatoryEvacuationAreaRadiusKm?: 0.0)
                stmt.setDouble(6, event.recommendedEvacuationAreaRadiusKm?: 0.0)
                stmt.setString(7, event.severity.toString())
                stmt.setTimestamp(8, java.sql.Timestamp.from(event.startDate))
                stmt.setTimestamp(9, java.sql.Timestamp.from(event.endDate))
                stmt.setObject(10, event.status.toString())
                stmt.setObject(11, event.location?.let { "POINT(${it.longitude} ${it.latitude})" })
                stmt.setString(12, event.content)
                stmt.setString(13, event.id)


                if (stmt.executeUpdate() > 0) {
                    event
                } else {
                    throw Exception("Failed to update event")
                }
            }
        }
    }

    fun mapRowToEvent(rs: ResultSet): Event {
        return Event(
            id = rs.getString("id"),
            name = rs.getString("name"),
            description = rs.getString("description"),
            content = rs.getString("content"),
            type = rs.getEventType(),
            impactAreaRadiusKm = rs.getDouble("impact_area_radius_km"),
            mandatoryEvacuationAreaRadiusKm = rs.getDouble("mandatory_evacuation_area_radius_km"),
            recommendedEvacuationAreaRadiusKm = rs.getDouble("recommended_evacuation_area_radius_km"),
            severity = rs.getSeverity(),
            startDate = rs.getTimestamp("start_date").toInstant(),
            endDate = rs.getTimestamp("end_date").toInstant(),
            status = rs.getStatus(),
            location = rs.getLocation(),
            createdAt = rs.getTimestamp("created_at").toInstant(),
            updatedAt = rs.getTimestamp("updated_at").toInstant()
        )
    }

}