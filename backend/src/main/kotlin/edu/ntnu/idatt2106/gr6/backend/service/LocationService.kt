package edu.ntnu.idatt2106.gr6.backend.service

import edu.ntnu.idatt2106.gr6.backend.repository.UserLocationRepository
import edu.ntnu.idatt2106.gr6.backend.DTOs.UserDTOs.UpdateUserLocationRequest
import edu.ntnu.idatt2106.gr6.backend.DTOs.UserDTOs.GetUserLocationRequest
import edu.ntnu.idatt2106.gr6.backend.model.Location
import edu.ntnu.idatt2106.gr6.backend.util.LocationParser
import org.springframework.stereotype.Service
import java.sql.ResultSet
import java.util.UUID

@Service
class LocationService(
    private val locationEncryptionService: LocationEncryptionService,
    private val userLocationRepository: UserLocationRepository,
    private val userContextService: UserContextService,
    private val locationParser: LocationParser
) {
    private val logger = org.slf4j.LoggerFactory.getLogger(LocationService::class.java)
    fun updateUserLocation(updateUserLocationRequest: UpdateUserLocationRequest) {
        val userId: String = userContextService.getCurrentUserId().toString()
        val encryptedLocation = locationEncryptionService.encryptLocation(
            "POINT(${updateUserLocationRequest.location.latitude} ${updateUserLocationRequest.location.longitude})"
        )
        userLocationRepository.saveUserLocation(userId, encryptedLocation)
    }

    fun getUserLocation(userIds: UUID): Location {
        val extractedUserId: String = userContextService.getCurrentUserId().toString()
        if(extractedUserId != userIds.toString()) {
            logger.error("extracted user id ${extractedUserId}, request id ${userIds}")
            throw IllegalArgumentException("User ID does not match the current user.")
        }

        val encryptedLocation = userLocationRepository.findUserLocation(extractedUserId) ?:
            throw IllegalArgumentException("User location has not been enabled")

        val decryptedLocation = locationEncryptionService.decryptLocation(encryptedLocation)
        val parsedLocation: Location = locationParser.parseLocation(decryptedLocation)
        return parsedLocation
    }
}