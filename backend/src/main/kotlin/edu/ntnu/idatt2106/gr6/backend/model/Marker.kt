package edu.ntnu.idatt2106.gr6.backend.model

import edu.ntnu.idatt2106.gr6.backend.DTOs.Location
import jakarta.persistence.JoinColumn
import jakarta.persistence.MapsId
import jakarta.persistence.OneToOne

sealed class MarkerType {
    object Shelter: MarkerType()
    object Defibrillator: MarkerType()
    object EmergencyClinic: MarkerType()
    object DistributionPoint: MarkerType()
    object PoliceStation: MarkerType()
    object Pharmacy: MarkerType()
    object General: MarkerType()

    override fun toString(): String {
        return when (this) {
            is Shelter -> "Shelter"
            is Defibrillator -> "Defibrillator"
            is EmergencyClinic -> "EmergencyClinic"
            is DistributionPoint -> "DistributionPoint"
            is PoliceStation -> "PoliceStation"
            is Pharmacy -> "Pharmacy"
            is General -> "General"
        }
    }

    companion object {
        fun fromString(type: String): MarkerType {
            return when (type) {
                "Shelter" -> Shelter
                "Defibrillator" -> Defibrillator
                "EmergencyClinic" -> EmergencyClinic
                "DistributionPoint" -> DistributionPoint
                "PoliceStation" -> PoliceStation
                "Pharmacy" -> Pharmacy
                else -> General
            }
        }
    }
}

data class ContactInfo(
    val name: String?,
    val email: String?,
    val phone: String?
)

data class OpeningHours(
    val monday: String?,
    val tuesday: String?,
    val wednesday: String?,
    val thursday: String?,
    val friday: String?,
    val saturday: String?,
    val sunday: String?
)

data class Marker (
    val id: String,
    val storageId: String? = null,
    val eventId: String? = null,
    val imageId: String? = null,
    val name: String,
    val location: Location,
    val description: String,
    val contactInfo: ContactInfo? = null,
    val openingHours: OpeningHours? = null,
    val type: MarkerType,
) {
    init {
        require(name.isNotBlank()) { "Name must be specified" }
        require(description.isNotBlank()) { "Description must be specified" }
    }
}