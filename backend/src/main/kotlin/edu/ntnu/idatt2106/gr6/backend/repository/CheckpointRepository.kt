package edu.ntnu.idatt2106.gr6.backend.repository

import edu.ntnu.idatt2106.gr6.backend.model.Checkpoint
import org.springframework.stereotype.Repository
import javax.sql.DataSource

@Repository
class CheckpointRepository(private val dataSource: DataSource) {

    fun getAllCheckpoints(): List<Checkpoint> {
        val sql = "SELECT id, name, description FROM checkpoint_list"
        val checkpoints = mutableListOf<Checkpoint>()

        dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.executeQuery().use { rs ->
                    while (rs.next()) {
                        checkpoints.add(
                            Checkpoint(
                                id = rs.getString("id"),
                                name = rs.getString("name"),
                                description = rs.getString("description")
                            )
                        )
                    }
                }
            }
        }

        return checkpoints
    }

    fun getCheckpointsByUserId(userId: String): List<Checkpoint> {
        val sql = """
        SELECT c.id, c.name, c.description
        FROM user_checkpoint_list ucl
        JOIN checkpoint_list c ON ucl.checkpoint_list_id = c.id
        WHERE ucl.user_id = ?
    """.trimIndent()

        val checkpoints = mutableListOf<Checkpoint>()

        dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, userId)
                stmt.executeQuery().use { rs ->
                    while (rs.next()) {
                        checkpoints.add(
                            Checkpoint(
                                id = rs.getString("id"),
                                name = rs.getString("name"),
                                description = rs.getString("description")
                            )
                        )
                    }
                }
            }
        }

        return checkpoints
    }


    fun replaceCheckpointsForUser(userId: String, entries: List<Pair<String, String>>): Boolean {
        val deleteSql = "DELETE FROM user_checkpoint_list WHERE user_id = ?"
        val insertSql = """
        INSERT INTO user_checkpoint_list (id, user_id, checkpoint_list_id)
        VALUES (?, ?, ?)
    """.trimIndent()

        dataSource.connection.use { conn ->
            conn.autoCommit = false

            try {
                conn.prepareStatement(deleteSql).use { stmt ->
                    stmt.setString(1, userId)
                    stmt.executeUpdate()
                }

                conn.prepareStatement(insertSql).use { stmt ->
                    for ((id, checkpointId) in entries) {
                        stmt.setString(1, id)
                        stmt.setString(2, userId)
                        stmt.setString(3, checkpointId)
                        stmt.addBatch()
                    }
                    stmt.executeBatch()
                }

                conn.commit()
                return true

            } catch (e: Exception) {
                conn.rollback()
                e.printStackTrace()
                return false
            } finally {
                conn.autoCommit = true
            }
        }
    }

    fun getUserCheckpointCompletionPercentage(userId: String): Double {
        val sql = """
        SELECT 
            (SELECT COUNT(*) FROM user_checkpoint_list WHERE user_id = ?) * 1.0 /
            (SELECT COUNT(*) FROM checkpoint_list)
    """.trimIndent()

        dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, userId)
                stmt.executeQuery().use { rs ->
                    return if (rs.next()) {
                        (rs.getDouble(1) * 100).coerceIn(0.0, 100.0)
                    } else {
                        0.0
                    }
                }
            }
        }
    }

}
