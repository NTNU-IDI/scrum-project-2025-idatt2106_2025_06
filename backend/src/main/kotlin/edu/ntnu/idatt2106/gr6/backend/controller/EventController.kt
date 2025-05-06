package edu.ntnu.idatt2106.gr6.backend.controller

import edu.ntnu.idatt2106.gr6.backend.DTOs.CreateEventRequest
import edu.ntnu.idatt2106.gr6.backend.DTOs.EventResponse
import edu.ntnu.idatt2106.gr6.backend.DTOs.UpdateEventRequest
import edu.ntnu.idatt2106.gr6.backend.service.EventService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.messaging.simp.SimpMessagingTemplate

@RestController
@RequestMapping("/api/events")
class EventController(
    private val eventService: EventService,
    private val messagingTemplate: SimpMessagingTemplate
) {
    private val logger = LoggerFactory.getLogger(EventController::class.java)

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('HANDLE_EVENTS')")
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
        messagingTemplate.convertAndSend("/topic/public/news", response)
        logger.info("Event created with ID: ${response.id}")
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @PostMapping("/update")
    @PreAuthorize("hasAuthority('HANDLE_EVENTS')")
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
        @RequestBody request: UpdateEventRequest
    ): ResponseEntity<EventResponse> {
        logger.info("Received event update request for ID: ${request.id}")
        val response = eventService.updateEvent(request)
        logger.info("Event updated with ID: ${request.id}")
        return ResponseEntity.status(HttpStatus.OK).body(response)
    }

    @GetMapping("/get/{id}")
    @Operation(
        summary = "Get event by ID",
        description = "Retrieves an event by its ID"
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Event found"),
            ApiResponse(responseCode = "404", description = "Event not found"),
            ApiResponse(responseCode = "500", description = "Internal server error"),
        ],
    )
    fun getEventById(
        @PathVariable id: String
    ): ResponseEntity<EventResponse> {
        logger.info("Received request to get event by ID: $id")
        val response = eventService.getEventById(id)
        return if (response != null) {
            logger.info("Event found with ID: $id")
            ResponseEntity.status(HttpStatus.OK).body(response)
        } else {
            logger.warn("Event not found with ID: $id")
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('HANDLE_EVENTS')")
    @Operation(
        summary = "Delete event by ID",
        description = "Deletes an event by its ID"
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Event deleted"),
            ApiResponse(responseCode = "404", description = "Event not found"),
            ApiResponse(responseCode = "500", description = "Internal server error"),
        ],
    )
    fun deleteEventById(
        @PathVariable id: String
    ): ResponseEntity<Void> {
        logger.info("Received request to delete event by ID: $id")
        val deleted = eventService.deleteEventById(id)
        return if (deleted) {
            logger.info("Event deleted with ID: $id")
            ResponseEntity.status(HttpStatus.OK).build()
        } else {
            logger.warn("Event not found with ID: $id")
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    @GetMapping("/all")
    @Operation(
        summary = "Get all events",
        description = "Retrieves all events"
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Events found"),
            ApiResponse(responseCode = "500", description = "Internal server error"),
        ],
    )
    fun getAllEvents(): ResponseEntity<List<EventResponse>> {
        logger.info("Received request to get all events")
        val events = eventService.getAllEvents()
        logger.info("Found ${events.size} events")
        return ResponseEntity.status(HttpStatus.OK).body(events)
    }
}