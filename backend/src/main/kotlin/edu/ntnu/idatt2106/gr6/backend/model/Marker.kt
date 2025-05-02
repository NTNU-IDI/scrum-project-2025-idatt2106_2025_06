package edu.ntnu.idatt2106.gr6.backend.model

import edu.ntnu.idatt2106.gr6.backend.DTOs.Location
import jakarta.persistence.JoinColumn
import jakarta.persistence.MapsId
import jakarta.persistence.OneToOne

sealed class MarkerType {
    object FoodBank : MarkerType()
    object WaterDistribution : MarkerType()
    object Clinic : MarkerType()
    object Pharmacy : MarkerType()
    object Hospital : MarkerType()
    object BombShelter : MarkerType()
    object Household : MarkerType()
    object Other : MarkerType()

    override fun toString(): String {
        return when (this) {
            is FoodBank -> "food_bank"
            is Hospital -> "hospital"
            is Clinic -> "clinic"
            is Pharmacy -> "pharmacy"
            is WaterDistribution -> "water_distribution"
            is BombShelter -> "bomb_shelter"
            is Household -> "household"
            is Other -> "other"
        }
    }

    companion object {
        fun fromString(type: String): MarkerType {
            return when (type) {
                "food_bank" -> FoodBank
                "water_distribution" -> WaterDistribution
                "clinic" -> Clinic
                "pharmacy" -> Pharmacy
                "hospital" -> Hospital
                "bomb_shelter" -> BombShelter
                "household" -> Household
                else -> Other
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