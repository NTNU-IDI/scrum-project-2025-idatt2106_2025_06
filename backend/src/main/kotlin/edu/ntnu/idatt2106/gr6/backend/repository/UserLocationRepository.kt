package edu.ntnu.idatt2106.gr6.backend.repository

import edu.ntnu.idatt2106.gr6.backend.model.Location
import org.springframework.stereotype.Repository
import javax.sql.DataSource

/**
 * Repository for managing user location data in the database.
 * Primarily responsible for saving and retrieving encrypted location strings.
 *
 * Assumes that the `location` column in the `users` table stores encrypted data.
 */
@Repository
class UserLocationRepository(
    private val dataSource: DataSource,
) {

    /**
     * Saves (updates) the encrypted location for a given user.
     *
     * @param id The ID of the user.
     * @param encryptedLocation The encrypted location string to save.
     * @return The same encrypted location that was saved.
     */
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

    /**
     * Retrieves the encrypted location string for a given user.
     *
     * @param userId The ID of the user.
     * @return The encrypted location if it exists, or `null` if not found.
     */
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