package edu.ntnu.idatt2106.gr6.backend.service

import edu.ntnu.idatt2106.gr6.backend.DTOs.MarkerDTOs
import edu.ntnu.idatt2106.gr6.backend.repository.MarkerRepository
import edu.ntnu.idatt2106.gr6.backend.DTOs.MarkerDTOs.CreateMarkerRequest
import edu.ntnu.idatt2106.gr6.backend.DTOs.MarkerDTOs.CreateMarkerResponse
import edu.ntnu.idatt2106.gr6.backend.model.Marker
import edu.ntnu.idatt2106.gr6.backend.util.IdGenerator
import edu.ntnu.idatt2106.gr6.backend.model.MarkerType
import org.hibernate.sql.Update
import org.springframework.stereotype.Service

@Service
class MarkerService(
    private val markerRepository: MarkerRepository,
    private val idGenerator: IdGenerator,
    private val userContextService: UserContextService,
    ) {
    private val logger = org.slf4j.LoggerFactory.getLogger(MarkerService::class.java)

    fun createMarker(request: CreateMarkerRequest): CreateMarkerResponse {
        var markerId: String
        do {
            markerId = idGenerator.generateId(10)
        } while (markerRepository.findMarkerById(markerId) != null)
        val savedMarker = markerRepository.createMarker(request.toMarker(markerId))
        return CreateMarkerResponse(
            id = savedMarker.id,
            name = savedMarker.name,
            location = savedMarker.location,
            description = savedMarker.description,
            contactInfo = savedMarker.contactInfo,
            openingHours = savedMarker.openingHours,
            imageId = savedMarker.imageId,
            type = savedMarker.type.toString()
        )
    }

    fun deleteMarkerById(id: String): Boolean {
        return markerRepository.deleteMarkerById(id)
    }

    fun updateMarker(request: MarkerDTOs.UpdateMarkerRequest): CreateMarkerResponse {
        val existingMarker = markerRepository.findMarkerById(request.id)
            ?: throw IllegalArgumentException("Marker with ID ${request.id} not found")

        val updatedMarker = existingMarker.copy(
            name = request.name,
            eventId = request.eventId ?: existingMarker.eventId,
            storageId = request.storageId ?: existingMarker.storageId,
            description = request.description,
            location = request.location,
            contactInfo = request.contactInfo ?: existingMarker.contactInfo,
            openingHours = request.openingHours?: existingMarker.openingHours,
            imageId = request.imageId?: existingMarker.imageId,
            type = MarkerType.fromString(request.type)
        )

        logger.info(updatedMarker.toString())

        return markerRepository.updateMarker(updatedMarker).toResponse()
    }

    fun getMarkerById(id: String): CreateMarkerResponse? {
        val marker = markerRepository.findMarkerById(id)
        return if (marker != null) {
            CreateMarkerResponse(
                id = marker.id,
                name = marker.name,
                location = marker.location,
                description = marker.description,
                contactInfo = marker.contactInfo,
                openingHours = marker.openingHours,
                imageId = marker.imageId,
                type = marker.type.toString()
            )
        } else {
            null
        }
    }


    fun getAllMarkers(): List<CreateMarkerResponse> {
        return markerRepository.findAllMarkers().map { marker ->
            CreateMarkerResponse(
                id = marker.id,
                name = marker.name,
                location = marker.location,
                description = marker.description,
                contactInfo = marker.contactInfo,
                openingHours = marker.openingHours,
                imageId = marker.imageId,
                type = marker.type.toString()
            )
        }
    }

    internal fun CreateMarkerRequest.toMarker(id: String): Marker {
        requireNotNull(this.location) { "Location cannot be null" }
        return Marker(
            id = id,
            name = this.name,
            eventId = this.eventId,
            storageId = this.storageId,
            description = this.description,
            location = this.location,
            contactInfo = this.contactInfo,
            openingHours = this.openingHours,
            imageId = this.imageId,
            type = MarkerType.fromString(this.type),
        )
    }

    fun getStandardMarkersSet(): List<CreateMarkerResponse> {
        return markerRepository.findAllMarkers()
            .filter { marker -> marker.type != MarkerType.fromString("household") }
            .map { marker ->
                CreateMarkerResponse(
                    id = marker.id,
                    name = marker.name,
                    location = marker.location,
                    description = marker.description,
                    contactInfo = marker.contactInfo,
                    openingHours = marker.openingHours,
                    imageId = marker.imageId,
                    type = marker.type.toString()
                )
            }
    }


    internal fun Marker.toResponse(): CreateMarkerResponse {
        return CreateMarkerResponse(
            id = this.id,
            name = this.name,
            location = this.location,
            description = this.description,
            contactInfo = this.contactInfo,
            openingHours = this.openingHours,
            imageId = this.imageId,
            type = this.type.toString()
        )
    }
}