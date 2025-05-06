package edu.ntnu.idatt2106.gr6.backend.repository

import edu.ntnu.idatt2106.gr6.backend.model.Permission
import edu.ntnu.idatt2106.gr6.backend.model.Role
import edu.ntnu.idatt2106.gr6.backend.model.User
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.sql.Statement
import java.util.UUID
import javax.sql.DataSource

@Repository
class UserRepository (
    private val dataSource: DataSource
) {
    fun save(user: User): User {
        val userId = UUID.randomUUID()
        val email = user.email
        val name = user.name
        val password = user.password
        dataSource.connection.use { conn ->
            val sql = "INSERT INTO users (id, email, name, password) VALUES (?, ?, ?, ?)"
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, userId.toString())
                stmt.setString(2, email)
                stmt.setString(3, name)
                stmt.setString(4, password)

                val affectedRows = stmt.executeUpdate()
                if (affectedRows == 0) {
                    throw RuntimeException("Creating user failed, no rows affected.")
                }
            }
        }
        return user.copy(id = userId)
    }

    fun loginUser(email: String, password: String): User? {
        dataSource.connection.use { conn ->
            val sql = "SELECT * FROM users WHERE email = ? AND password = ?"
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, email)
                stmt.setString(2, password)
                stmt.executeQuery().use { rows ->
                    if (rows.next()) {
                        return mapRowToUser(rows)
                    }
                }
            }
        }
        return null
    }

    fun updateUser(userId: UUID, newName: String, newEmail: String): Boolean {
        val sql = """
        UPDATE users SET name = ?, email = ? WHERE id = ?
    """.trimIndent()

        dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, newName)
                stmt.setString(2, newEmail)
                stmt.setString(3, userId.toString())

                return stmt.executeUpdate() > 0
            }
        }
    }

    fun updatePassword(userId: UUID, hashedPassword: String): Boolean {
        dataSource.connection.use { conn ->
            val sql = "UPDATE users SET password = ? WHERE id = ?"
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, hashedPassword)
                stmt.setString(2, userId.toString())
                return stmt.executeUpdate() > 0
            }
        }
    }

    fun findById(userId: UUID): User? {
        val sql = """
        SELECT u.*, r.role_name AS role_name 
        FROM users u
        JOIN roles r ON u.role_id = r.id
        WHERE u.id = ?
    """.trimIndent()

        dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, userId.toString())
                stmt.executeQuery().use { rows ->
                    if (rows.next()) {
                        return mapRowToUser(rows)
                    }
                }
            }
        }
        return null
    }

    fun updateUserTrackingPreferences(userId: UUID, trackingEnabled: Boolean): Boolean {
        val sql = "UPDATE users SET tracking_enabled = ? WHERE id = ?"
        dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setBoolean(1, trackingEnabled)
                stmt.setString(2, userId.toString())
                return stmt.executeUpdate() > 0
            }
        }
    }

    fun getUserTrackingPreferences(userId: UUID): Boolean {
        val sql = "SELECT tracking_enabled FROM users WHERE id = ?"
        dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, userId.toString())
                stmt.executeQuery().use { rows ->
                    if (rows.next()) {
                        return rows.getBoolean("tracking_enabled")
                    }
                }
            }
        }
        return false
    }

    fun deleteUserTrackingHistory(userId: UUID): Boolean {
        val sql = "UPDATE users SET location = ? WHERE id = ?"
        dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, null)
                stmt.setString(2, userId.toString())
                return stmt.executeUpdate() > 0
            }
        }
    }


    fun findByEmail(email: String): User? {
        dataSource.connection.use { conn ->
            val sql = """
            SELECT u.*, r.role_name AS role_name 
            FROM users u
            JOIN roles r ON u.role_id = r.id
            WHERE u.email = ?
        """
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, email)
                stmt.executeQuery().use { rows ->
                    if (rows.next()) {
                        return mapRowToUser(rows)
                    }
                }
            }
        }
        return null
    }

     fun mapRowToUser(rs: ResultSet): User =
        User(
            id = UUID.fromString(rs.getString("id")),
            name = rs.getString("name"),
            email = rs.getString("email"),
            createdAt = rs.getTimestamp("created_at").toInstant(),
            verified = rs.getBoolean("verified"),
            role = Role(
                id = rs.getInt("role_id"),
                name = rs.getString("role_name"),
                permissions = fetchPermissions(rs.getInt("role_id")),
            ),
            passwordHashed = rs.getString("password"),
        )

    fun fetchPermissions(roleId: Int): Set<Permission> {
        val permissions = mutableSetOf<Permission>()

        val sql = """
        SELECT p.id, p.name, p.description
        FROM role_permissions rp
        JOIN permissions p ON rp.permission_id = p.id
        WHERE rp.role_id = ?
    """

        dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setInt(1, roleId)
                stmt.executeQuery().use { rows ->
                    while (rows.next()) {
                        val permission = Permission(
                            id = rows.getInt("id"),
                            name = rows.getString("name"),
                            description = rows.getString("description")
                        )
                        permissions.add(permission)
                    }
                }
            }
        }
        return permissions
    }
}

