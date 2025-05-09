package edu.ntnu.idatt2106.gr6.backend.repository

import edu.ntnu.idatt2106.gr6.backend.model.Permission
import edu.ntnu.idatt2106.gr6.backend.model.Role
import edu.ntnu.idatt2106.gr6.backend.model.User
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.sql.Statement
import java.util.UUID
import javax.sql.DataSource

/**
 * Repository for managing user data and operations related to authentication, profile updates,
 * email verification, and tracking preferences.
 *
 * Uses JDBC and raw SQL to interact with the `users`, `roles`, `permissions`, and `email_verification` tables.
 */
@Repository
class UserRepository (
    private val dataSource: DataSource
) {

    /**
     * Saves a new user in the database.
     *
     * @param user The User object to be saved.
     * @return The same User object, with a generated UUID assigned to the `id` field.
     * @throws RuntimeException if no rows were affected.
     */
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

    /**
     * Logs in a user by matching email and password.
     *
     * @param email The user's email.
     * @param password The hashed password.
     * @return The matching User object or `null` if credentials are invalid.
     */
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

    /**
     * Updates a user's name, email, and verification status.
     *
     * @param userId The user's UUID.
     * @param newName New name to set.
     * @param newEmail New email to set.
     * @param verified New verification status.
     * @return `true` if the update was successful, `false` otherwise.
     */
    fun updateUser(userId: UUID, newName: String, newEmail: String, verified: Boolean): Boolean {
        val sql = """
        UPDATE users SET name = ?, email = ?, verified = ?  WHERE id = ?
    """.trimIndent()

        dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, newName)
                stmt.setString(2, newEmail)
                stmt.setBoolean(3, verified)
                stmt.setString(4, userId.toString())

                return stmt.executeUpdate() > 0
            }
        }
    }

    /**
     * Updates a user's password.
     *
     * @param userId The user's UUID.
     * @param hashedPassword The new hashed password.
     * @return `true` if the password was updated successfully.
     */
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

    /**
     * Retrieves a user by ID, including role and permissions.
     *
     * @param userId UUID of the user.
     * @return The User object or `null` if not found.
     */
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

    /**
     * Enables or disables tracking for a user.
     *
     * @param userId UUID of the user.
     * @param trackingEnabled `true` to enable, `false` to disable.
     * @return `true` if the preference was updated.
     */
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

    /**
     * Retrieves whether tracking is enabled for a user.
     *
     * @param userId UUID of the user.
     * @return `true` if tracking is enabled, `false` otherwise.
     */
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

    /**
     * Deletes a user's location data (used for tracking history).
     *
     * @param userId UUID of the user.
     * @return `true` if the update was successful.
     */
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

    /**
     * Saves a token used for email verification.
     *
     * @param id Token entry ID.
     * @param userId UUID of the user.
     * @param token The token string.
     * @return `true` if the token was saved.
     */
    fun saveEmailVerificationToken(id: String,userId: UUID, token: String): Boolean {
        val sql = "INSERT INTO email_verification (id, user_id, token, expires_at) VALUES (?, ?, ?,?)"
        dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, id)
                stmt.setString(2, userId.toString())
                stmt.setString(3, token)
                stmt.setTimestamp(4, java.sql.Timestamp(System.currentTimeMillis() + 600000)) // 1 hour expiration
                return stmt.executeUpdate() > 0
            }
        }
    }

    /**
     * Retrieves the email verification token for a user.
     *
     * @param userid The user's ID as a String.
     * @return The token string or `null` if not found.
     */
    fun findEmailVerificationToken(userid: String): String? {
        val sql = "SELECT token FROM email_verification WHERE user_id = ?"
        dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, userid)
                stmt.executeQuery().use { rows ->
                    if (rows.next()) {
                        return rows.getString("token")
                    }
                }
            }
        }
        return null
    }

    /**
     * Finds a user by email, including their role and permissions.
     *
     * @param email The user's email.
     * @return The User object or `null` if not found.
     */
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

    /**
     * Maps a SQL result row to a full User object, including nested Role and permissions.
     *
     * @param rs The ResultSet with user data.
     * @return A fully populated User object.
     */
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
            trackingEnabled = rs.getBoolean("tracking_enabled"),
        )

    /**
     * Fetches all permissions associated with a given role.
     *
     * @param roleId ID of the role.
     * @return A set of Permission objects.
     */
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

