package edu.ntnu.idatt2106.gr6.backend.repository

import edu.ntnu.idatt2106.gr6.backend.model.Scenario
import org.springframework.stereotype.Repository
import java.sql.Timestamp
import java.time.Instant
import java.util.UUID
import javax.sql.DataSource

@Repository
class ScenarioRepository(
    private val datasource: DataSource
) {
    fun saveScenario(scenario: Scenario): Scenario {
        val sql = """
            INSERT INTO scenario (id, title, description, content, url, created_at, updated_at)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """.trimIndent()

        datasource.connection.use { connection ->
            connection.prepareStatement(sql).use { statement ->
                statement.setString(1, scenario.id)
                statement.setString(2, scenario.title)
                statement.setString(3, scenario.description)
                statement.setString(4, scenario.content)
                statement.setString(5, scenario.url)
                statement.setTimestamp(6, Timestamp.from(scenario.createdAt))
                statement.setTimestamp(7, Timestamp.from(scenario.updatedAt))
                statement.executeUpdate()
            }
        }
        return scenario
    }

    fun updateScenario(scenario: Scenario): Scenario {
        val sql = """
            UPDATE scenario
            SET title = ?, description = ?, content = ?, url = ?, updated_at = ?
            WHERE id = ?
        """.trimIndent()

        datasource.connection.use { connection ->
            connection.prepareStatement(sql).use { statement ->
                statement.setString(1, scenario.title)
                statement.setString(2, scenario.description)
                statement.setString(3, scenario.content)
                statement.setString(4, scenario.url)
                statement.setTimestamp(5, Timestamp.from(scenario.updatedAt))
                statement.setString(6, scenario.id)
                statement.executeUpdate()
            }
        }
        return scenario
    }

    fun deleteScenario(id: String): Boolean {
        val sql = "DELETE FROM scenario WHERE id = ?"
        datasource.connection.use { connection ->
            connection.prepareStatement(sql).use { statement ->
                statement.setString(1, id.toString())
                return statement.executeUpdate() > 0
            }
        }
    }

    fun findScenarioById(id: String): Scenario? {
        val sql = "SELECT * FROM scenario WHERE id = ?"
        datasource.connection.use { connection ->
            connection.prepareStatement(sql).use { statement ->
                statement.setString(1, id.toString())
                val resultSet = statement.executeQuery()
                if (resultSet.next()) {
                    return mapRowToScenario(resultSet)
                }
            }
        }
        return null
    }

    fun findAllScenarios(): List<Scenario> {
        val sql = "SELECT * FROM scenario"
        val scenarios = mutableListOf<Scenario>()
        datasource.connection.use { connection ->
            connection.prepareStatement(sql).use { statement ->
                val resultSet = statement.executeQuery()
                while (resultSet.next()) {
                    scenarios.add(mapRowToScenario(resultSet))
                }
            }
        }
        return scenarios
    }

    fun mapRowToScenario(resultSet: java.sql.ResultSet): Scenario {
        return Scenario(
            id = resultSet.getString("id"),
            title = resultSet.getString("title"),
            description = resultSet.getString("description"),
            content = resultSet.getString("content"),
            url = resultSet.getString("url"),
            createdAt = resultSet.getTimestamp("created_at").toInstant(),
            updatedAt = resultSet.getTimestamp("updated_at").toInstant(),
        )
    }
}