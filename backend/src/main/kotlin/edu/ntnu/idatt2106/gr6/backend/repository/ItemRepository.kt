package edu.ntnu.idatt2106.gr6.backend.repository

import edu.ntnu.idatt2106.gr6.backend.DTOs.StorageItemResponse
import edu.ntnu.idatt2106.gr6.backend.model.Item
import edu.ntnu.idatt2106.gr6.backend.model.ItemInstance
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.sql.Date
import java.sql.ResultSet
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDate
import java.util.*
import javax.sql.DataSource

@Repository
class ItemRepository(
    private val dataSource: DataSource
) {

    fun createItem(
        name: String,
        typeId: Int,
        unitId: Int
    ): Item {
        val existingItem = findItemByName(name)
        if (existingItem != null) {
            return existingItem
        }

        val id = UUID.randomUUID().toString()
        val createdAt = Timestamp(System.currentTimeMillis())
        val updatedAt = createdAt

        val sql = """
            INSERT INTO items (id, name, type_id, unit_id, created_at, update_at)
            VALUES (?, ?, ?, ?, ?, ?)
        """.trimIndent()

        dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, id)
                stmt.setString(2, name)
                stmt.setInt(3, typeId)
                stmt.setInt(4, unitId)
                stmt.setTimestamp(5, createdAt)
                stmt.setTimestamp(6, updatedAt)
                stmt.executeUpdate()
            }
        }

        return Item(
            id = id,
            name = name,
            typeId = typeId,
            unitId = unitId,
            createdAt = createdAt.toInstant(),
            updatedAt = updatedAt.toInstant()
        )
    }

    fun saveItemInstance(
        itemId: String,
        storageId: String,
        amount: BigDecimal,
        expiryDate: LocalDate?
    ): ItemInstance {
        val id = UUID.randomUUID().toString()
        val createdAt = Timestamp(System.currentTimeMillis())
        val updatedAt = createdAt

        val sql = """
            INSERT INTO item_instances (id, item_id, storage_id, amount, expiry_date, created_at, updated_at)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """.trimIndent()

        dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, id)
                stmt.setString(2, itemId)
                stmt.setString(3, storageId)
                stmt.setBigDecimal(4, amount)
                stmt.setDate(5, expiryDate?.let { Date.valueOf(it) })
                stmt.setTimestamp(6, createdAt)
                stmt.setTimestamp(7, updatedAt)
                stmt.executeUpdate()
            }
        }

        return ItemInstance(
            id = id,
            itemId = itemId,
            storageId = storageId,
            amount = amount,
            expiryDate = expiryDate,
            createdAt = createdAt.toInstant(),
            updatedAt = updatedAt.toInstant()
        )
    }

    fun findItemById(id: String): Item? {
        val sql = """
            SELECT id, name, type_id, unit_id, created_at, update_at
            FROM items
            WHERE id = ?
        """.trimIndent()

            dataSource.connection.use { conn ->
                conn.prepareStatement(sql).use { stmt ->
                    stmt.setString(1, id)
                    stmt.executeQuery().use { rs ->
                        return if (rs.next()) mapRowToItem(rs) else null
                    }
                }
            }
    }

    fun findStorageItemInstances(storageId: String, typeId: String): List<ItemInstance> {
        val sql = """
            SELECT ii.id, ii.item_id, ii.storage_id, ii.expiry_date, ii.amount, ii.created_at, ii.updated_at
            FROM item_instances ii
            JOIN items i ON ii.item_id = i.id
            WHERE ii.storage_id = ? AND i.type_id = ?
            ORDER BY i.name ASC
        """.trimIndent()

        val instances = mutableListOf<ItemInstance>()

        dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, storageId)
                stmt.setString(2, typeId)
                stmt.executeQuery().use { rs ->
                    while (rs.next()) {
                        instances.add(
                            ItemInstance(
                                id = rs.getString("id"),
                                itemId = rs.getString("item_id"),
                                storageId = rs.getString("storage_id"),
                                amount = rs.getBigDecimal("amount"),
                                expiryDate = rs.getDate("expiry_date")?.toLocalDate(),
                                createdAt = rs.getTimestamp("created_at").toInstant(),
                                updatedAt = rs.getTimestamp("updated_at").toInstant()
                            )
                        )
                    }
                }
            }
        }
        return instances
    }


    fun deleteItemInstancesByIds(instanceIds: List<String>) {
        if (instanceIds.isEmpty()) return

        val placeholders = instanceIds.joinToString(",") { "?" }
        val sql = "DELETE FROM item_instances WHERE id IN ($placeholders)"

        dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                instanceIds.forEachIndexed { index, id ->
                    stmt.setString(index + 1, id)
                }
                stmt.executeUpdate()
            }
        }
    }






    private fun findItemByName(name: String): Item? {
        val sql = """
        SELECT id, name, type_id, unit_id, created_at, update_at
        FROM items
        WHERE name = ?
    """.trimIndent()

        dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, name)

                stmt.executeQuery().use { rs ->
                    return if (rs.next()) mapRowToItem(rs) else null
                }
            }
        }
    }

    private fun mapRowToItem(rs: ResultSet): Item {
        return Item(
            id = rs.getString("id"),
            name = rs.getString("name"),
            typeId = rs.getInt("type_id"),
            unitId = rs.getInt("unit_id"),
            createdAt = rs.getTimestamp("created_at").toInstant(),
            updatedAt = rs.getTimestamp("update_at").toInstant()
        )
    }

    private fun mapRowToItemInstance(rs: ResultSet): ItemInstance {
        return ItemInstance(
            id = rs.getString("id"),
            itemId = rs.getString("item_id"),
            storageId = rs.getString("storage_id"),
            amount = rs.getBigDecimal("amount"),
            expiryDate = rs.getDate("expiry_date")?.toLocalDate(),
            createdAt = rs.getTimestamp("created_at").toInstant(),
            updatedAt = rs.getTimestamp("updated_at").toInstant()
        )
    }
}
