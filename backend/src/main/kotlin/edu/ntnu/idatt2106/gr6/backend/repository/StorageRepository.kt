package edu.ntnu.idatt2106.gr6.backend.repository

import edu.ntnu.idatt2106.gr6.backend.DTOs.StorageResponse
import org.springframework.stereotype.Repository
import java.sql.Connection
import java.sql.Timestamp
import java.util.*
import javax.sql.DataSource

@Repository
class StorageRepository(private val dataSource: DataSource) {

    fun saveStorage(
        name: String,
        storageOwner: String,
        token: Int,
        latitude: Double?,
        longitude: Double?
    ): StorageResponse {
        val id = UUID.randomUUID()
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
                stmt.setString(3, storageOwner.toString())
                stmt.setInt(4, token)
                stmt.setObject(5, latitude)
                stmt.setObject(6, longitude)
                stmt.setTimestamp(7, createdAt)
                stmt.setTimestamp(8, updatedAt)
                stmt.executeUpdate()
            }
        }

        return StorageResponse(
            id = id.toString(),
            name = name,
            storageOwner = storageOwner,
            token = token,
            createdAt = createdAt.toInstant(),
            updatedAt = updatedAt.toInstant()
        )
    }
}
