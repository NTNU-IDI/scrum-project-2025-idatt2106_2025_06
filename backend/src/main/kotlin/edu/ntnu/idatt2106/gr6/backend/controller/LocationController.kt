package edu.ntnu.idatt2106.gr6.backend.controller

import edu.ntnu.idatt2106.gr6.backend.DTOs.UserDTOs

import edu.ntnu.idatt2106.gr6.backend.model.Location
import edu.ntnu.idatt2106.gr6.backend.service.LocationService
import io.swagger.v3.oas.annotations.Parameter
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class LocationController(
    private val locationService: LocationService
) {
    @GetMapping("/api/location/")
    fun getLocation(@Parameter @RequestBody getUserLocationRequest: UserDTOs.GetUserLocationRequest): Location {
        return locationService.getUserLocation(getUserLocationRequest)
    }
}