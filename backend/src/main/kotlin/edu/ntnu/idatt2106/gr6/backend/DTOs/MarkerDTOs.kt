package edu.ntnu.idatt2106.gr6.backend.DTOs
import edu.ntnu.idatt2106.gr6.backend.model.ContactInfo
import edu.ntnu.idatt2106.gr6.backend.model.MarkerType
import edu.ntnu.idatt2106.gr6.backend.model.OpeningHours
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

class MarkerDTOs {
    data class MarkerDTO(
        val id: Int,
        val name: String,
        val location: Location,
        val description: String,
        val contactInfo: String,
        val imageId: String,
        val type: String
    )

    data class CreateMarkerRequest(
        @field:NotBlank(message = "Name is required")
        val name: String,
        val eventId: String? = null,
        val storageId: String? = null,
        @field:NotBlank(message = "Description is required")
        val description: String,
        @field:NotNull(message = "Location is required")
        @field:Valid
        val location: Location,
        val contactInfo: ContactInfo? = null,
        val openingHours: OpeningHours? = null,
        val imageId: String? = null,
        @field:NotBlank(message = "Type is required")
        val type: String
    )

    data class CreateMarkerResponse(
        val id: String,
        val name: String,
        val location: Location,
        val description: String,
        val contactInfo: ContactInfo?,
        val openingHours: OpeningHours?,
        val imageId: String?,
        val type: String
    )

    data class UpdateMarkerRequest(
        @field:NotBlank(message = "ID is required")
        val id: String,
        val name: String,
        val eventId: String?,
        val storageId: String?,
        val description: String,
        @field:NotNull(message = "Location is required")
        @Valid
        val location: Location,
        val contactInfo: ContactInfo?,
        val openingHours: OpeningHours?,
        val imageId: String?,
        val type: String
    )

    data class UpdateMarkerResponse(
        val id: String,
        val name: String,
        val location: Location,
        val description: String,
        val contactInfo: ContactInfo?,
        val openingHours: OpeningHours?,
        val imageId: String?,
        val type: String
    )

    data class MarkerTypeDTO(
        val id: Int,
        val name: String
    )

    data class ClosestMarkerRequest(
        val startLocation: Location,
        val type: MarkerType
    )

    data class ClosestMarkerResponse(
        val markerId: String
    )
}