package edu.ntnu.idatt2106.gr6.backend.repository

import edu.ntnu.idatt2106.gr6.backend.DTOs.StorageDTOs.StorageSummary
import edu.ntnu.idatt2106.gr6.backend.model.Location
import edu.ntnu.idatt2106.gr6.backend.model.SimpleUser
import edu.ntnu.idatt2106.gr6.backend.model.Storage
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.sql.Timestamp
import java.util.*
import javax.sql.DataSource

@Repository
class StorageRepository(private val dataSource: DataSource) {

    fun saveStorage(
        name: String,
        storageOwner: String,
        location: Location?,
    ): Storage {
        val id = UUID.randomUUID()
        val token = generateRandomKey()
        val createdAt = Timestamp(System.currentTimeMillis())
        val updatedAt = createdAt

        val sql = """
            INSERT INTO storages (id, name, storage_owner, token, location, created_at, updated_at)
            VALUES (?, ?, ?, ?, POINT(?, ?), ?, ?)
        """.trimIndent()

        dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, id.toString())
                stmt.setString(2, name)
                stmt.setString(3, storageOwner)
                stmt.setString(4, token)
                stmt.setObject(5, location?.latitude)
                stmt.setObject(6, location?.longitude)
                stmt.setTimestamp(7, createdAt)
                stmt.setTimestamp(8, updatedAt)
                stmt.executeUpdate()
            }
        }

        addUserToStorage(storageOwner, id.toString())

        return Storage(
            id = id.toString(),
            name = name,
            storageOwner = storageOwner,
            token = token.toString(),
            location = location,
            createdAt = createdAt.toInstant(),
            updatedAt = updatedAt.toInstant()
        )
    }

    fun findStorageByToken(token: String): Storage? {
        val sql = """
            SELECT id, name, storage_owner, token,
                   ST_Y(location) AS latitude,
                   ST_X(location) AS longitude,
                   created_at, updated_at
            FROM storages
            WHERE token = ?
        """.trimIndent()

        dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, token)
                stmt.executeQuery().use { rs ->
                    return if (rs.next()) mapRowToStorage(rs) else null
                }
            }
        }
    }

    fun addUserToStorage(userId: String, storageId: String) {
        if (isUserAlreadyInStorage(userId, storageId)) {
            throw IllegalArgumentException("User is already a member of this storage.")
        }

        val sql = """
        INSERT INTO user_storages (user_id, storage_id)
        VALUES (?, ?)
    """.trimIndent()

        dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, userId)
                stmt.setString(2, storageId)
                stmt.executeUpdate()
            }
        }
    }

    private fun isUserAlreadyInStorage(userId: String, storageId: String): Boolean {
        val sql = """
        SELECT COUNT(*)
        FROM user_storages
        WHERE user_id = ? AND storage_id = ?
    """.trimIndent()

        dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, userId)
                stmt.setString(2, storageId)
                stmt.executeQuery().use { rs ->
                    if (rs.next()) {
                        return rs.getInt(1) > 0
                    }
                }
            }
        }
        return false
    }


    fun removeUserFromStorage(userId: String, storageId: String): Boolean {
        val sql = """
        DELETE FROM user_storages
        WHERE user_id = ? AND storage_id = ?
    """.trimIndent()

        dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, userId)
                stmt.setString(2, storageId)
                val rowsDeleted = stmt.executeUpdate()
                return rowsDeleted > 0
            }
        }
    }


    fun findSimpleUsersInStorage(storageId: String): List<SimpleUser> {
        val sql = """
        SELECT u.id, u.name
        FROM user_storages us
        JOIN users u ON us.user_id = u.id
        WHERE us.storage_id = ?
    """.trimIndent()

        val users = mutableListOf<SimpleUser>()

        dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, storageId)
                stmt.executeQuery().use { rs ->
                    while (rs.next()) {
                        users.add(
                            SimpleUser(
                                id = UUID.fromString(rs.getString("id")),
                                name = rs.getString("name")
                            )
                        )
                    }
                }
            }
        }

        return users
    }



    fun findStoragesByUserId(userId: String): List<StorageSummary> {
        val sql = """
        SELECT s.id, s.name, s.token, s.storage_owner,
                ST_Y(s.location) AS latitude,
                ST_X(s.location) AS longitude
        FROM storages s
        JOIN user_storages us ON s.id = us.storage_id
        WHERE us.user_id = ?
    """.trimIndent()

        val storages = mutableListOf<StorageSummary>()

        dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, userId)
                stmt.executeQuery().use { rs ->
                    while (rs.next()) {
                        val lat = rs.getObject("latitude") as? Double
                        val lon = rs.getObject("longitude") as? Double
                        val location = if (lat != null && lon != null) Location(lat, lon) else null

                        storages.add(
                            StorageSummary(
                                id = rs.getString("id"),
                                name = rs.getString("name"),
                                token = rs.getString("token"),
                                location = location,
                                storageOwner = rs.getString("storage_owner")
                            )
                        )
                    }
                }
            }
        }
        return storages
    }

    /**
     * Updates the name and location of a storage. Only the owner can do this.
     *
     * @param storageId ID of the storage to update
     * @param userId ID of the storage trying to update
     * @param newName The new name of the storage
     * @param newLocation The new location of the storage
     * @return `true` if the storage was successfully updated, `false` if the user is not the owner
     *         or the storage doesn't exist.
     */
    fun updateStorageIfOwner(
        storageId: String,
        userId: String,
        newName: String?,
        newLocation: Location?
    ): Boolean {
        if (!isStorageOwner(storageId, userId)) return false

        val sql = """
            UPDATE storages
            SET name = ?, location = POINT(?, ?), updated_at = CURRENT_TIMESTAMP
            WHERE id = ?
        """.trimIndent()

        dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, newName)
                stmt.setObject(2, newLocation?.latitude)
                stmt.setObject(3, newLocation?.longitude)
                stmt.setString(4, storageId)
                return stmt.executeUpdate() > 0
            }
        }
    }


    /**
     * Deletes a storage from the database. Only an owner can do this
     *
     * @param storageId The ID of the storage to delete.
     * @param userId The ID of the user attempting to delete the storage.
     * @return `true` if the storage was successfully deleted, `false` if the user is not the owner
     *         or the storage does not exist.
     */
    fun deleteStorage(
        storageId: String,
        userId: String
    ): Boolean {
        if (!isStorageOwner(storageId, userId)) return false
        val sql = """
            DELETE
            FROM storages
            WHERE id = ?
        """.trimIndent()

        dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, storageId)
                val rowsAffected = stmt.executeUpdate()
                return rowsAffected > 0
            }
        }
    }

    private fun mapRowToStorage(rs: ResultSet): Storage {
        val lat = rs.getObject("latitude") as? Double
        val lon = rs.getObject("longitude") as? Double
        return Storage(
            id = rs.getString("id"),
            name = rs.getString("name"),
            storageOwner = rs.getString("storage_owner"),
            token = rs.getString("token"),
            location = if (lat != null && lon != null) Location(lat, lon) else null,
            createdAt = rs.getTimestamp("created_at").toInstant(),
            updatedAt = rs.getTimestamp("updated_at").toInstant()
        )
    }
    private fun generateRandomKey(length: Int = 6): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..length)
            .map { chars.random() }
            .joinToString("")
    }

    /**
     * Checks whether the given user is the owner of the specified storage.
     *
     * @param storageId The ID of the storage to check.
     * @param userId The ID of the user to verify as the owner.
     * @return `true` if the user is the owner of the storage, `false` otherwise.
     */
    private fun isStorageOwner(storageId: String, userId: String): Boolean {
        val sql = """
            SELECT COUNT(*)
            FROM storages
            WHERE id = ? AND storage_owner = ?
        """.trimIndent()

        dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, storageId)
                stmt.setString(2, userId)
                stmt.executeQuery().use { rs ->
                    return rs.next() && rs.getInt(1) > 0
                }
            }
        }
    }


}
