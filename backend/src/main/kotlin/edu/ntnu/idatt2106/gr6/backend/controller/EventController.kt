package edu.ntnu.idatt2106.gr6.backend.controller

import edu.ntnu.idatt2106.gr6.backend.DTOs.CreateEventRequest
import edu.ntnu.idatt2106.gr6.backend.DTOs.EventResponse
import edu.ntnu.idatt2106.gr6.backend.service.EventService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/events")
class EventController(
    private val eventService: EventService,
) {
    private val logger = LoggerFactory.getLogger(EventController::class.java)

    @PostMapping("/create")
    @Operation(
        summary = "Create new event",
        description = "Creates a new event and links it to the current authenticated user"
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Event created"),
            ApiResponse(responseCode = "400", description = "Invalid input or token already in use"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "500", description = "Internal server error"),
        ],
    )
    fun createEvent(
        @RequestBody
        @Parameter request: CreateEventRequest): ResponseEntity<EventResponse> {
        logger.info("Received event create request: $request")
        val response = eventService.createEvent(request)
        logger.info("Event created with ID: ${response.id}")
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @PostMapping("/update/{id}")
    @Operation(
        summary = "Update event",
        description = "Updates an existing event with the given ID"
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Event updated"),
            ApiResponse(responseCode = "400", description = "Invalid input"),
            ApiResponse(responseCode = "404", description = "Event not found"),
            ApiResponse(responseCode = "500", description = "Internal server error"),
        ],
    )
    fun updateEvent(
        @PathVariable id: String,
        @RequestBody request: CreateEventRequest
    ): ResponseEntity<EventResponse> {
        logger.info("Received event update request for ID: $id")
        val response = eventService.updateEvent(id, request)
        logger.info("Event updated with ID: $id")
        return ResponseEntity.status(HttpStatus.OK).body(response)
    }
}