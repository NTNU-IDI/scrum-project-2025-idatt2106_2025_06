package edu.ntnu.idatt2106.gr6.backend.repository

import edu.ntnu.idatt2106.gr6.backend.model.Item
import edu.ntnu.idatt2106.gr6.backend.model.ItemInstance
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.sql.Date
import java.sql.ResultSet
import java.sql.Timestamp
import java.time.LocalDate
import java.util.*
import javax.sql.DataSource

/**
 * Repository class responsible for db operations related to item management
 *
 * @property dataSource The data source for database connections
 */
@Repository
class ItemRepository(
    private val dataSource: DataSource
) {

    /**
     * Creates a new item in the database if it doesn't already exist.
     *
     * @param name The name of the item.
     * @param typeId The type ID of the item.
     * @param unitId The unit ID of the item.
     * @return The created or existing item.
     */
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

    /**
     * Retrieves all items from the database.
     *
     * @return A list rows mapped to items.
     */

    fun getAllItems(): List<Item> {
        val sql = """
        SELECT id, name, type_id, unit_id, created_at, update_at
        FROM items
        ORDER BY name
    """.trimIndent()

        val items = mutableListOf<Item>()
        dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.executeQuery().use { rs ->
                    while (rs.next()) {
                        items.add(mapRowToItem(rs))
                    }
                }
            }
        }
        return items
    }

    /**
     * Saves an item instance to the database.
     *
     * @param itemId The ID of the item.
     * @param storageId The ID of the storage.
     * @param amount The amount of the item.
     * @param expiryDate The expiry date of the item.
     * @return The created item instance.
     */

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

    /**
     * Deletes item instances by their IDs.
     *
     * @param instanceIds The list of item instance IDs to delete.
     * @return The number of deleted item instances.
     */

    fun deleteItemInstancesByIds(instanceIds: List<String>): Int {
        if (instanceIds.isEmpty()) return 0

        val placeholders = instanceIds.joinToString(",") { "?" }
        val sql = "DELETE FROM item_instances WHERE id IN ($placeholders)"

        dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                instanceIds.forEachIndexed { index, id ->
                    stmt.setString(index + 1, id)
                }
                return stmt.executeUpdate()
            }
        }
    }

    /**
     * Finds an item by its ID.
     *
     * @param id The ID of the item.
     * @return The item if found, null otherwise.
     */

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

    /**
     * Finds an item instance by its ID.
     *
     * @param id The ID of the item instance.
     * @return The item instance if found, null otherwise.
     */

    fun getItemInstanceById(id: String): ItemInstance? {
        val sql = """
        SELECT id, item_id, storage_id, amount, expiry_date, created_at, updated_at
        FROM item_instances
        WHERE id = ?
    """.trimIndent()

        dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, id)
                stmt.executeQuery().use { rs ->
                    return if (rs.next()) mapRowToItemInstance(rs) else null
                }
            }
        }
    }

    /**
     * Retrieves item instances by storage ID and type ID.
     *
     * @param storageId The ID of the storage.
     * @param typeId The ID of the item type.
     * @return A list of item instances matching the criteria.
     */
    fun getItemInstancesByType(storageId: String, typeId: String): List<ItemInstance> {
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

    /**
     * Updates an item instance in the database.
     *
     * @param id The ID of the item instance.
     * @param amount The new amount of the item instance.
     * @param expiryDate The new expiry date of the item instance.
     * @return True if the update was successful, false otherwise.
     */

    fun updateItemInstance(
        id: String,
        amount: BigDecimal,
        expiryDate: LocalDate?
    ): Boolean {
        val updatedAt = Timestamp(System.currentTimeMillis())

        val sql = """
        UPDATE item_instances
        SET amount = ?, expiry_date = ?, updated_at = ?
        WHERE id = ?
    """.trimIndent()

        dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setBigDecimal(1, amount)
                stmt.setDate(2, expiryDate?.let { Date.valueOf(it) })
                stmt.setTimestamp(3, updatedAt)
                stmt.setString(4, id)
                return stmt.executeUpdate() > 0
            }
        }
    }

    /**
     * Finds an item by its name.
     *
     * @param name The name of the item.
     * @return The item if found, null otherwise.
     */

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

    /**
     * Maps a row from the result set to an Item object.
     */

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

    /**
     * Maps a row from the result set to an ItemInstance object.
     */

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
