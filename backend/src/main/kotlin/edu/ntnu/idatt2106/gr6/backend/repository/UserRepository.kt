package edu.ntnu.idatt2106.gr6.backend.repository

import edu.ntnu.idatt2106.gr6.backend.model.User
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.sql.Statement
import javax.sql.DataSource

@Repository
class UserRepository (
    private val dataSource: DataSource
) {
    fun saveUser(user: User): User {
        val email = user.email
        val name = user.name
        val password = user.password
        dataSource.connection.use { conn ->
            val sql = "INSERT INTO users (email, name, password) VALUES (?, ?, ?)"
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, email)
                stmt.setString(2, name)
                stmt.setString(3, password)

                val affectedRows = stmt.executeUpdate()
                if (affectedRows == 0) {
                    throw RuntimeException("Creating user failed, no rows affected.")
                }

                stmt.
            }
        }
    }
}