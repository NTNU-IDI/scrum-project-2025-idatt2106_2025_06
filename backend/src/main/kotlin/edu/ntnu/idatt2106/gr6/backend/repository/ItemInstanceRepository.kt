package edu.ntnu.idatt2106.gr6.backend.repository

import edu.ntnu.idatt2106.gr6.backend.DTOs.ItemInstancesResponse
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.util.*
import javax.sql.DataSource
import kotlin.toString

@Repository
class ItemInstanceRepository(private val dataSource: DataSource) {

    fun findByUserId(userId: UUID): List<ItemInstancesResponse> {
        val sql = """
            SELECT ii.* FROM item_instances ii
            JOIN user_storages us ON ii.storage_id = us.storage_id
            WHERE us.user_id = ?
        """.trimIndent()

        return dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, userId.toString())
                stmt.executeQuery().use { rs ->
                    val results = mutableListOf<ItemInstancesResponse>()
                    while (rs.next()) {
                        results.add(mapRowToItemInstance(rs))
                    }
                    results
                }
            }
        }
    }

    private fun mapRowToItemInstance(rs: ResultSet): ItemInstancesResponse {
        return ItemInstancesResponse(
            id = rs.getString("id"),
            itemId = rs.getString("item_id"),
            storageId = rs.getString("storage_id"),
            expiryDate = rs.getDate("expiry_date")?.toLocalDate(),
            amount = rs.getDouble("amount"),
            createdAt = rs.getTimestamp("created_at").toInstant(),
            updatedAt = rs.getTimestamp("updated_at").toInstant()
        )
    }
}