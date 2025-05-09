package edu.ntnu.idatt2106.gr6.backend.repository

import edu.ntnu.idatt2106.gr6.backend.model.Scenario
import org.springframework.stereotype.Repository
import java.sql.Timestamp
import java.time.Instant
import java.util.UUID
import javax.sql.DataSource

/**
 * Repository for handling CRUD operations on [Scenario] entities using JDBC and a [DataSource].
 *
 * @property datasource The database connection source.
 */
@Repository
class ScenarioRepository(
    private val datasource: DataSource
) {

    /**
     * Saves a new scenario to the database.
     *
     * @param scenario The [Scenario] object to be saved.
     * @return The saved [Scenario] instance.
     */
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

    /**
     * Updates an existing scenario in the database.
     *
     * @param scenario The [Scenario] object containing updated data.
     * @return The updated [Scenario] instance.
     */
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

    /**
     * Deletes a scenario from the database by its ID.
     *
     * @param id The ID of the scenario to delete.
     * @return `true` if a scenario was deleted, `false` otherwise.
     */
    fun deleteScenario(id: String): Boolean {
        val sql = "DELETE FROM scenario WHERE id = ?"
        datasource.connection.use { connection ->
            connection.prepareStatement(sql).use { statement ->
                statement.setString(1, id.toString())
                return statement.executeUpdate() > 0
            }
        }
    }

    /**
     * Finds a scenario in the database by its ID.
     *
     * @param id The ID of the scenario to retrieve.
     * @return The [Scenario] if found, or `null` if not found.
     */
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

    /**
     * Retrieves all scenarios from the database.
     *
     * @return A list of all [Scenario] objects.
     */
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

    /**
     * Maps a row from a [ResultSet] to a [Scenario] object.
     *
     * @param resultSet The result set containing scenario data.
     * @return The mapped [Scenario] object.
     */
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