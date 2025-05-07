package edu.ntnu.idatt2106.gr6.backend.service

import edu.ntnu.idatt2106.gr6.backend.repository.UserLocationRepository
import edu.ntnu.idatt2106.gr6.backend.DTOs.UserDTOs.UpdateUserLocationRequest
import edu.ntnu.idatt2106.gr6.backend.DTOs.UserDTOs.GetUserLocationRequest
import edu.ntnu.idatt2106.gr6.backend.exception.LocationNotAvailableException
import edu.ntnu.idatt2106.gr6.backend.exception.UserLocationDisabledException
import edu.ntnu.idatt2106.gr6.backend.exception.UserMismatchException
import edu.ntnu.idatt2106.gr6.backend.model.Location
import edu.ntnu.idatt2106.gr6.backend.repository.UserRepository
import edu.ntnu.idatt2106.gr6.backend.util.LocationParser
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Service
import java.sql.ResultSet
import java.util.UUID

@Service
class LocationService(
    private val locationEncryptionService: LocationEncryptionService,
    private val userLocationRepository: UserLocationRepository,
    private val userContextService: UserContextService,
    private val locationParser: LocationParser,
    private val userRepository: UserRepository
) {
    private val logger = org.slf4j.LoggerFactory.getLogger(LocationService::class.java)
    @MessageMapping("/private/location")
    @SendTo("/topic/private/update")
    fun updateUserLocation(updateUserLocationRequest: UpdateUserLocationRequest) {
        val userId: String = userContextService.getCurrentUserId().toString()
        val trackingEnabled = userRepository.getUserTrackingPreferences(UUID.fromString(userId))
        if (!trackingEnabled) {
            throw UserLocationDisabledException.forUser(userId)
        }
        val encryptedLocation = locationEncryptionService.encryptLocation(
            "POINT(${updateUserLocationRequest.location.latitude} ${updateUserLocationRequest.location.longitude})"
        )
        userLocationRepository.saveUserLocation(userId, encryptedLocation)
    }

    fun getUserLocation(userIds: UUID): Location {
        val extractedUserId: String = userContextService.getCurrentUserId().toString()
        if(extractedUserId != userIds.toString()) {
            throw UserMismatchException.forUser(extractedUserId)
        }

        val trackingEnabled = userRepository.getUserTrackingPreferences(userIds)
        if (!trackingEnabled) {
            throw UserLocationDisabledException.forUser(userIds.toString())
        }

        val encryptedLocation = userLocationRepository.findUserLocation(extractedUserId) ?:
            throw LocationNotAvailableException.forUser(extractedUserId)

        val decryptedLocation = locationEncryptionService.decryptLocation(encryptedLocation)
        val parsedLocation: Location = locationParser.parseLocation(decryptedLocation)
        return parsedLocation
    }
}