package edu.ntnu.idatt2106.gr6.backend.controller

import edu.ntnu.idatt2106.gr6.backend.DTOs.UserDTOs

import edu.ntnu.idatt2106.gr6.backend.model.Location
import edu.ntnu.idatt2106.gr6.backend.service.LocationService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal
import java.util.UUID

@RestController
@RequestMapping("/api/location")
class LocationController(
    private val locationService: LocationService
) {
    private val logger = org.slf4j.LoggerFactory.getLogger(LocationController::class.java)

    //REMOVE THIS ENDPOINT!!!!!!
    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('CREATE_STORAGE')") //!!!!!!!!
    @Operation(summary = "Get coordinates of a user")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Location retrieved"),
            ApiResponse(responseCode = "404", description = "User not found"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun getLocation(@PathVariable userId: UUID): Location {
        logger.info("Received request to get location for user: ${userId}")
        return locationService.getUserLocation(userId)
    }

    @MessageMapping("private/location/update")
    @Operation(summary = "Update coordinates of a user")
    fun updateLocation(@Payload updateUserLocationRequest: UserDTOs.UpdateUserLocationRequest, principal: Principal) {
        logger.info("Received request to update location")
        val userid = principal.name
        locationService.updateUserLocation(updateUserLocationRequest, principal)
    }
}