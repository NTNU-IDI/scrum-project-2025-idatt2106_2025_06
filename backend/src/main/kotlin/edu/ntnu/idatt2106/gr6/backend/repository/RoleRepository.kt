package edu.ntnu.idatt2106.gr6.backend.repository

import edu.ntnu.idatt2106.gr6.backend.model.Permission
import edu.ntnu.idatt2106.gr6.backend.model.Role
import org.springframework.stereotype.Repository
import javax.sql.DataSource

@Repository
class RoleRepository(
    private val dataSource: DataSource
) {
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

    fun findPermissionsByRole(roleId: Int): Set<Permission> {
        val sql = """
        SELECT p.id, p.permission_name, p.description
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
                                name = rs.getString("permission_name"),
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