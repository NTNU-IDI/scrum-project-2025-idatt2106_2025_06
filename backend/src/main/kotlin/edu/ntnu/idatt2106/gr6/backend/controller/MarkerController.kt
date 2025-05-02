package edu.ntnu.idatt2106.gr6.backend.controller


import edu.ntnu.idatt2106.gr6.backend.DTOs.MarkerDTOs
import edu.ntnu.idatt2106.gr6.backend.service.MarkerService
import edu.ntnu.idatt2106.gr6.backend.DTOs.MarkerDTOs.CreateMarkerResponse
import edu.ntnu.idatt2106.gr6.backend.DTOs.MarkerDTOs.CreateMarkerRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/markers")
class MarkerController(
    private val markerService: MarkerService,
) {
    private val logger = org.slf4j.LoggerFactory.getLogger(MarkerController::class.java)


    @PostMapping("/create")
    @Operation(summary = "Creates markers")
    @ApiResponses (
        value = [
            ApiResponse(responseCode = "201", description = "Marker created"),
            ApiResponse(responseCode = "400", description = "Invalid input"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun createMarker(@RequestBody @Parameter createMarkerRequest: CreateMarkerRequest):
            ResponseEntity<CreateMarkerResponse> {
        logger.info("Received request to create marker")
        val marker = markerService.createMarker(createMarkerRequest)
        logger.info("Created marker with name: ${marker.name}")
        return ResponseEntity.status(201).body(marker)
    }

    @PostMapping("/update")
    @Operation(summary = "Updates markers")
    @ApiResponses (
        value = [
            ApiResponse(responseCode = "200", description = "Marker updated"),
            ApiResponse(responseCode = "400", description = "Invalid input"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun updateMarker(@RequestBody @Parameter updateMarkerRequest: MarkerDTOs.UpdateMarkerRequest):
            ResponseEntity<MarkerDTOs.CreateMarkerResponse> {
        logger.info("Received request to update marker")
        val marker = markerService.updateMarker(updateMarkerRequest)
        logger.info("Updated marker with ID: ${marker.id}")
        return ResponseEntity.ok().body(marker)
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Deletes markers")
    @ApiResponses (
        value = [
            ApiResponse(responseCode = "200", description = "Marker deleted"),
            ApiResponse(responseCode = "400", description = "Invalid input"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun deleteMarker(@PathVariable id: String): ResponseEntity<Void> {
        logger.info("Received request to delete marker with ID: $id")
        markerService.deleteMarkerById(id)
        logger.info("Deleted marker with ID: $id")
        return ResponseEntity.ok().build()
    }


    @GetMapping("/get/{id}")
    @Operation(summary = "Gets all markers")
    @ApiResponses (
        value = [
            ApiResponse(responseCode = "200", description = "Marker found"),
            ApiResponse(responseCode = "400", description = "Invalid input"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun getMarkerById(@PathVariable id: String): ResponseEntity<MarkerDTOs.CreateMarkerResponse> {
        logger.info("Received request to get marker with ID: $id")
        val marker = markerService.getMarkerById(id)
        logger.info("Retrieved marker with ID: ${marker?.id}")
        return ResponseEntity.ok().body(marker)
    }

    @GetMapping("/getAll")
    @ApiResponses
    (
        value = [
            ApiResponse(responseCode = "200", description = "Markers found"),
            ApiResponse(responseCode = "400", description = "Invalid input"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun getAllMarkers(): ResponseEntity<List<CreateMarkerResponse>> {
        logger.info("Received request to get all markers")
        val markers = markerService.getAllMarkers()
        logger.info("Retrieved ${markers.size} markers")
        return ResponseEntity.ok().body(markers)
    }
}