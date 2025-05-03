package edu.ntnu.idatt2106.gr6.backend.repository

import edu.ntnu.idatt2106.gr6.backend.model.Location
import org.springframework.stereotype.Repository
import javax.sql.DataSource

@Repository
class UserLocationRepository(
    private val dataSource: DataSource,
) {
    fun saveUserLocation(id: String, encryptedLocation: String): String {
        val sql = "UPDATE users SET location = ? WHERE id = ?"
        dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, encryptedLocation)
                stmt.setString(2, id)
                stmt.executeUpdate()
            }
        }
        return encryptedLocation
    }

    fun findUserLocation(userId: String): String? {
        val sql = "SELECT location FROM users WHERE id = ?"
        dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, userId)
                stmt.executeQuery().use { rows ->
                    if (rows.next()) {
                        val location = rows.getString("location")
                        return location
                    }
                }
            }
        }
        return null
    }
}