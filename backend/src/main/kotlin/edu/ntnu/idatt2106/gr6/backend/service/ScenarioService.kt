package edu.ntnu.idatt2106.gr6.backend.service

import edu.ntnu.idatt2106.gr6.backend.DTOs.CreateScenarioRequest
import edu.ntnu.idatt2106.gr6.backend.DTOs.ScenarioResponse
import edu.ntnu.idatt2106.gr6.backend.DTOs.UpdateScenarioRequest
import edu.ntnu.idatt2106.gr6.backend.model.Event
import edu.ntnu.idatt2106.gr6.backend.model.Scenario
import edu.ntnu.idatt2106.gr6.backend.repository.ScenarioRepository
import edu.ntnu.idatt2106.gr6.backend.util.IdGenerator
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class ScenarioService(
    private val scenarioRepository: ScenarioRepository,
    private val idGenerator: IdGenerator,
) {
    fun createScenario(request: CreateScenarioRequest): ScenarioResponse {
        var markerId: String
        var attempts = 0
        val maxAttempts = 10
        do {
            if(++attempts >= maxAttempts) {
                throw IllegalStateException("Failed to generate a unique ID after $maxAttempts attempts") // change to custom exception
            }
            markerId = idGenerator.generateId(4)
        } while (scenarioRepository.findScenarioById(markerId) != null)
        val scenario = request.toScenario(markerId)
        scenarioRepository.saveScenario(scenario)
        return scenario.toResponse()
    }

    fun updateScenario(request: UpdateScenarioRequest): ScenarioResponse {
        val existingScenario = scenarioRepository.findScenarioById(request.id)
            ?: throw IllegalArgumentException("Scenario with ID ${request.id} not found")  // change to custom exception

        val updatedScenario: Scenario = existingScenario.copy(
            title = request.title ?: existingScenario.title,
            description = request.description ?: existingScenario.description,
            content = request.content?: existingScenario.content,
            url = request.url ?: existingScenario.url,
        )

        scenarioRepository.updateScenario(updatedScenario)
        return updatedScenario.toResponse()
    }

    fun deleteScenario(id: String) {
        scenarioRepository.findScenarioById(id)
            ?: throw IllegalArgumentException("Scenario with ID $id not found") // change to custom exception
        scenarioRepository.deleteScenario(id)
        return
    }

    fun getScenarioById(id: String): ScenarioResponse? {
        return scenarioRepository.findScenarioById(id)?.toResponse() ?:
            throw IllegalArgumentException("Scenario with ID $id not found") // change to custom exception
    }

    fun getAllScenarios(): List<ScenarioResponse> {
        return scenarioRepository.findAllScenarios().map { scenario ->
            scenario.toResponse()
        }
    }




    fun CreateScenarioRequest.toScenario(id: String) = Scenario(
        id = id,
        title = this.title,
        description = this.description,
        content = this.content,
        url = this.url,
        createdAt = Instant.now(),
        updatedAt = Instant.now(),
    )

    fun UpdateScenarioRequest.toScenario(id: String, existingEvent: Event) = Scenario(
        id = id,
        title = this.title ?: "",
        description = this.description ?: "",
        content = this.content ?: "",
        url = this.url ?: "",
        updatedAt = Instant.now(),
        createdAt = existingEvent.createdAt ?: Instant.now(),
    )

    fun Scenario.toResponse() = ScenarioResponse(
        id = this.id,
        title = this.title,
        description = this.description,
        content = this.content,
        url = this.url?: "null",
        updatedAt = this.updatedAt,
    )
}