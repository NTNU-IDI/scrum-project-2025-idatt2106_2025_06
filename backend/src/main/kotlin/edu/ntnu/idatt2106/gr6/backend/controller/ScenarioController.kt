package edu.ntnu.idatt2106.gr6.backend.controller

import org.slf4j.LoggerFactory
import edu.ntnu.idatt2106.gr6.backend.DTOs.CreateScenarioRequest
import edu.ntnu.idatt2106.gr6.backend.DTOs.ScenarioResponse
import edu.ntnu.idatt2106.gr6.backend.DTOs.UpdateScenarioRequest
import edu.ntnu.idatt2106.gr6.backend.service.ScenarioService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.PathVariable

@RestController
@RequestMapping("/api/scenarios")
class ScenarioController(
    private val scenarioService: ScenarioService
) {
    private val logger = LoggerFactory.getLogger(ScenarioController::class.java)
    @PostMapping("/create")
    @Operation(summary = "Create Scenario", description = "Creates a new scenario.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Scenario created successfully"),
            ApiResponse(responseCode = "400", description = "Invalid input"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun createScenario(@RequestBody createScenarioRequest: CreateScenarioRequest): ResponseEntity<ScenarioResponse> {
        logger.info("Received request to create scenario: $createScenarioRequest")
        val scenarioResponse = scenarioService.createScenario(createScenarioRequest)
        logger.info("Scenario created successfully: $scenarioResponse")
        return ResponseEntity.status(201).body(scenarioResponse)
    }

    @PostMapping("/update")
    @Operation(summary = "Update Scenario", description = "Updates an existing scenario.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Scenario updated successfully"),
            ApiResponse(responseCode = "400", description = "Invalid input"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun updateScenario(@RequestBody updateScenarioRequest: UpdateScenarioRequest): ResponseEntity<ScenarioResponse> {
        logger.info("Received request to update scenario: $updateScenarioRequest")
        val scenarioResponse = scenarioService.updateScenario(updateScenarioRequest)
        logger.info("Scenario updated successfully: $scenarioResponse")
        return ResponseEntity.ok(scenarioResponse)
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete Scenario", description = "Deletes an existing scenario.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Scenario deleted successfully"),
            ApiResponse(responseCode = "400", description = "Invalid input"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun deleteScenario(@PathVariable id: String): ResponseEntity<Void> {
        logger.info("Received request to delete scenario with id: $id")
        scenarioService.deleteScenario(id)
        logger.info("Scenario deleted successfully")
        return ResponseEntity.ok().build()
    }

    @GetMapping("/get/{id}")
    @Operation(summary = "Get Scenario by ID", description = "Retrieves a scenario by its ID.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Scenario retrieved successfully"),
            ApiResponse(responseCode = "400", description = "Invalid input"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun getScenarioById(@PathVariable id: String): ResponseEntity<ScenarioResponse> {
        logger.info("Received request to get scenario with id: $id")
        val scenarioResponse = scenarioService.getScenarioById(id)
        logger.info("Scenario retrieved successfully: $scenarioResponse")
        return ResponseEntity.ok(scenarioResponse)
    }

    @GetMapping("/getAll")
    @Operation(summary = "Get All Scenarios", description = "Retrieves all scenarios.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Scenarios retrieved successfully"),
            ApiResponse(responseCode = "400", description = "Invalid input"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun getAllScenarios(): ResponseEntity<List<ScenarioResponse>> {
        logger.info("Received request to get all scenarios")
        val scenarioResponses = scenarioService.getAllScenarios()
        logger.info("Scenarios retrieved successfully: $scenarioResponses")
        return ResponseEntity.ok(scenarioResponses)
    }

}