package edu.ntnu.idatt2106.gr6.backend.repository

import edu.ntnu.idatt2106.gr6.backend.model.Permission
import edu.ntnu.idatt2106.gr6.backend.model.Role
import org.springframework.stereotype.Repository
import javax.sql.DataSource

/**
 * Repository for accessing and managing user roles and their associated permissions in the database.
 *
 * @property dataSource The datasource for connecting to the database.
 */
@Repository
class RoleRepository(
    private val dataSource: DataSource
) {

    /**
     * Updates the role of a user.
     *
     * @param userId The ID of the user whose role is to be updated.
     * @param roleId The new role ID to assign to the user.
     */
    fun updateUserRole(userId: String, roleId: Int) {
        val sql = "UPDATE users SET role_id = ? WHERE id = ?"
        dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setInt(1, roleId)
                stmt.setString(2, userId)
                stmt.executeUpdate()
            }
        }
    }

    /**
     * Finds a role by its ID.
     *
     * @param roleId The ID of the role to retrieve.
     * @return The [Role] object if found, or `null` if not found.
     */
    fun findRoleByRoleId(roleId: Int): Role? {
        val sql = "SELECT * FROM roles WHERE id = ?"
        dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setInt(1, roleId)
                stmt.executeQuery().use { rs ->
                    if (rs.next()) {
                        return Role(
                            id = rs.getInt("id"),
                            name = rs.getString("role_name"),
                            permissions = findPermissionsByRole(rs.getInt("id"))
                        )
                    }
                }
            }
        }
        return null
    }

    /**
     * Retrieves the role associated with a specific user.
     *
     * @param userId The ID of the user whose role is to be retrieved.
     * @return The [Role] object if found, or `null` if the user has no role assigned.
     */
    fun findRoleByUserId(userId: String): Role? {
        val sql = "SELECT r.* FROM roles r JOIN users u ON r.id = u.role_id WHERE u.id = ?"
        dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, userId)
                stmt.executeQuery().use { rs ->
                    if (rs.next()) {
                        return Role(
                            id = rs.getInt("id"),
                            name = rs.getString("role_name"),
                            permissions = findPermissionsByRole(rs.getInt("id"))
                        )
                    }
                }
            }
        }
        return null
    }

    /**
     * Finds the default role, typically assigned to new users.
     *
     * @return The default [Role] object if found, or `null` if no default role is configured.
     */
    fun findDefaultRole(): Role? {
        val sql = "SELECT * FROM roles WHERE role_name = ?"
        dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, Role.ROLE_USER)
                stmt.executeQuery().use { rs ->
                    if (rs.next()) {
                        return Role(
                            id = rs.getInt("id"),
                            name = rs.getString("role_name"),
                            permissions = findPermissionsByRole(rs.getInt("id"))
                        )
                    }
                }
            }
        }
        return null
    }

    /**
     * Retrieves the set of permissions assigned to a specific role.
     *
     * @param roleId The ID of the role.
     * @return A [Set] of [Permission]s associated with the role.
     */
    fun findPermissionsByRole(roleId: Int): Set<Permission> {
        val sql = """
        SELECT p.id, p.name, p.description
        FROM permissions p
        JOIN role_permissions rp ON p.id = rp.permission_id
        WHERE rp.role_id = ?
    """.trimIndent()

        val permissions = mutableSetOf<Permission>()
        dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setInt(1, roleId)
                stmt.executeQuery().use { rs ->
                    while (rs.next()) {
                        permissions.add(
                            Permission(
                                id = rs.getInt("id"),
                                name = rs.getString("name"),
                                description = rs.getString("description")
                            )
                        )
                    }
                }
            }
        }
        return permissions
    }
}