package edu.ntnu.idatt2106.gr6.backend.repository

import edu.ntnu.idatt2106.gr6.backend.DTOs.StorageResponse
import org.springframework.stereotype.Repository
import java.sql.Connection
import java.sql.Timestamp
import java.util.*
import javax.sql.DataSource

@Repository
class StorageRepository(private val dataSource: DataSource) {

    fun saveStorage(name: String, token: Int, latitude: Double?, longitude: Double?): StorageResponse {
        val id = UUID.randomUUID()
        val createdAt = Timestamp(System.currentTimeMillis())
        val updatedAt = createdAt

        val sql = """
            INSERT INTO storages (id, name, token, location, created_at, updated_at)
            VALUES (?, ?, ?, POINT(?, ?), ?, ?)
        """.trimIndent()

        dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, id.toString())
                stmt.setString(2, name)
                stmt.setInt(3, token)
                stmt.setObject(4, latitude)
                stmt.setObject(5, longitude)
                stmt.setTimestamp(6, createdAt)
                stmt.setTimestamp(7, updatedAt)
                stmt.executeUpdate()
            }
        }

        return StorageResponse(
            id = id.toString(),
            name = name,
            token = token,
            createdAt = createdAt.toInstant(),
            updatedAt = updatedAt.toInstant()
        )
    }
}
