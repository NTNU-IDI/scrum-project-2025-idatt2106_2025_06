package edu.ntnu.idatt2106.gr6.backend.repository

import edu.ntnu.idatt2106.gr6.backend.DTOs.Location
import edu.ntnu.idatt2106.gr6.backend.model.ContactInfo
import edu.ntnu.idatt2106.gr6.backend.model.Marker
import edu.ntnu.idatt2106.gr6.backend.model.OpeningHours
import edu.ntnu.idatt2106.gr6.backend.model.getLocation
import edu.ntnu.idatt2106.gr6.backend.model.getMarkerType
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import javax.sql.DataSource
import kotlin.collections.get

@Repository
class MarkerRepository(
    private val dataSource: DataSource
) {
    private val logger = org.slf4j.LoggerFactory.getLogger(MarkerRepository::class.java)

    fun createMarker(marker: Marker): Marker {
        val sql = """
        INSERT INTO marker (
            id, storage_id, event_id, name, ST_, description, 
            contact_info, opening_hours, image_id, type
        ) VALUES (?, ?, ?, ?, ST_PointFromText(?, 4326), ?, ?, ?, ?, ?)
    """.trimIndent()

        dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, marker.id)
                stmt.setString(2, marker.storageId)
                stmt.setString(3, marker.eventId)
                stmt.setString(4, marker.name)
                stmt.setString(5, "POINT(${marker.location.longitude} ${marker.location.latitude})") // 🛠 Fixed
                stmt.setString(6, marker.description)
                stmt.setObject(7, marker.contactInfo.let {
                    it?.name + "," + it?.email + "," + it?.phone })
                stmt.setObject(8, marker.openingHours.let {
                    it?.monday + "," + it?.tuesday + "," + it?.wednesday + "," +
                            it?.thursday + "," + it?.friday + "," + it?.saturday + "," + it?.sunday })
                stmt.setString(9, marker.imageId)
                stmt.setString(10, marker.type.toString())

                logger.info("raw location: ${marker.location}")
                logger.info("location: ${marker.location.latitude}, ${marker.location.longitude}")

                val rowsAffected = stmt.executeUpdate()

                if (rowsAffected > 0) {
                    return marker
                } else {
                    throw Exception("Failed to create marker")
                }
            }
        }
    }

    fun findMarkerById(id: String): Marker? {
        val sql = """SELECT id, storage_id, event_id, name, ST_AsText(location) AS location,
            description, contact_info, opening_hours, image_id, type FROM marker WHERE id = ?
        """
        dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, id)
                stmt.executeQuery().use { rs ->
                    if (rs.next()) {
                        return mapRowToMarker(rs)
                    }
                }
            }
        }
        return null
    }


    fun findAllMarkers(): List<Marker> {
        val sql = """SELECT id, storage_id, event_id, name, ST_AsText(location) AS location,
            description, contact_info, opening_hours, image_id, type FROM marker
        """
        val markers = mutableListOf<Marker>()
        dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.executeQuery().use { rs ->
                    while (rs.next()) {
                        markers.add(mapRowToMarker(rs))
                    }
                }
            }
        }
        return markers
    }


    fun updateMarker(marker: Marker): Marker {
        val sql = """
            UPDATE marker SET 
                storage_id = ?, event_id = ?, name = ?, location = ST_PointFromText(?, 4326), 
                description = ?, contact_info = ?, opening_hours = ?, image_id = ?, type = ?
            WHERE id = ?
        """.trimIndent()

        dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, marker.storageId)
                stmt.setString(2, marker.eventId)
                stmt.setString(3, marker.name)
                stmt.setString(4, "POINT(${marker.location.longitude} ${marker.location.latitude})")
                stmt.setString(5, marker.description)
                stmt.setObject(6, marker.contactInfo.let {
                    it?.name + "," + it?.email + "," + it?.phone })
                stmt.setObject(7, marker.openingHours.let {
                    it?.monday + "," + it?.tuesday + "," + it?.wednesday + "," +
                            it?.thursday + "," + it?.friday + "," + it?.saturday + "," + it?.sunday })
                stmt.setString(8, marker.imageId)
                stmt.setString(9, marker.type.toString())
                stmt.setString(10, marker.id)

                val rowsAffected = stmt.executeUpdate()

                if (rowsAffected > 0) {
                    return marker
                } else {
                    throw Exception("Failed to update marker")
                }
            }
        }
    }

    fun deleteMarkerById(id: String): Boolean {
        val sql = "DELETE FROM marker WHERE id = ?"
        dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, id)
                val rowsAffected = stmt.executeUpdate()
                return rowsAffected > 0
            }
        }
    }
    fun findMarkersByType(type: String): List<Marker> {
        val sql = "SELECT * FROM marker WHERE type = ?"
        val markers = mutableListOf<Marker>()
        dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, type)
                stmt.executeQuery().use { rs ->
                    while (rs.next()) {
                        markers.add(mapRowToMarker(rs))
                    }
                }
            }
        }
        return markers
    }

    fun findMarkersByDistance(
        latitude: Double,
        longitude: Double,
        distance: Double
    ): List<Marker> {
        val sql = """
            SELECT *, ST_Distance(location, ST_PointFromText(?, 4326)) AS distance 
            FROM markers 
            WHERE ST_Distance(location, ST_PointFromText(?, 4326)) <= ?
        """.trimIndent()

        val markers = mutableListOf<Marker>()
        dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, "POINT($longitude $latitude)")
                stmt.setString(2, "POINT($longitude $latitude)")
                stmt.setDouble(3, distance)
                stmt.executeQuery().use { rs ->
                    while (rs.next()) {
                        markers.add(mapRowToMarker(rs))
                    }
                }
            }
        }
        return markers
    }

    fun mapRowToMarker(rs: ResultSet): Marker {
        val location = rs.getLocation() ?: throw Exception("Location cannot be null")

        val contactInfoStr = rs.getString("contact_info")
        val contactInfo = if (contactInfoStr != null) {
            val parts = contactInfoStr.split(",", limit = 3)
            if (parts.size == 3) {
                ContactInfo(
                    name = parts[0],
                    email = parts[1],
                    phone = parts[2]
                )
            } else {
                null
            }
        } else {
            null
        }

        val openingHoursStr = rs.getString("opening_hours")
        val openingHours = if (openingHoursStr != null) {
            val parts = openingHoursStr.split(",", limit = 7)
            if (parts.size == 7) {
                OpeningHours(
                    monday = parts[0],
                    tuesday = parts[1],
                    wednesday = parts[2],
                    thursday = parts[3],
                    friday = parts[4],
                    saturday = parts[5],
                    sunday = parts[6]
                )
            } else {
                null
            }
        } else {
            null
        }
        return Marker(
            id = rs.getString("id"),
            storageId = rs.getString("storage_id"),
            eventId = rs.getString("event_id"),
            name = rs.getString("name"),
            location = location,
            description = rs.getString("description"),
            contactInfo = contactInfo,
            openingHours = openingHours,
            imageId = rs.getString("image_id"),
            type = rs.getMarkerType()
        )
    }
}