package edu.ntnu.idatt2106.gr6.backend.model

import edu.ntnu.idatt2106.gr6.backend.DTOs.Location
import java.time.Instant


sealed class EventType {
    object NaturalDisaster : EventType()
    object NuclearAttack : EventType()
    object TerrorAttack: EventType()
    object Pandemic: EventType()
    object Other: EventType()

    override fun toString(): String {
        when(this) {
            is NaturalDisaster -> return "natural_disaster"
            is NuclearAttack -> return "nuclear_attack"
            is TerrorAttack -> return "terror_attack"
            is Pandemic -> return "pandemic"
            is Other -> return "other"
        }
    }

    companion object {
        fun fromString(value: String): EventType =
            when(value) {
                "natural_disaster" -> NaturalDisaster
                "nuclear_attack" -> NuclearAttack
                "terror_attack" -> TerrorAttack
                "pandemic" -> Pandemic
                "other" -> Other
                else -> throw IllegalArgumentException("Invalid event type: $value")
            }
    }
}

sealed class Severity {
    object Low : Severity()
    object Medium : Severity()
    object High : Severity()

    override fun toString(): String {
        when(this) {
            is Low -> return "Low"
            is Medium -> return "Medium"
            is High -> return "High"
        }
    }

    companion object {
        fun fromString(value: String): Severity =
            when(value) {
                "Low" -> Low
                "Medium" -> Medium
                "High" -> High
                else -> throw IllegalArgumentException("Invalid severity: $value")
            }
    }
}

data class Event (
    val id: String,
    val name: String? = null,
    val description: String? = null,
    val location: Location?,
    val type: EventType? = null,
    val impactAreaRadiusKm: Double?,
    val mandatoryEvacuationAreaRadiusKm: Double?,
    val recommendedEvacuationAreaRadiusKm: Double?,
    val startDate: Instant? = null,
    val endDate: Instant? = null,
    val eta: Instant? = null,
    val severity: Severity? = null,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null
    ) {
        init {
            require(id != null) { "ID must be -1 for unsaved events or non-negative for saved events" }
            require(description != null) { "Either name or description must be provided" }
            require(name != null) { "Name cannot be null" }
            require(location == null || (location.latitude in -90.0..90.0 && location.longitude in -180.0..180.0)) {
                require(type != null) { "Type cannot be null" }
                require(startDate != null) { "Start date cannot be null" }
                require(eta != null) { "ETA cannot be null" }
                require(severity != null) { "Severity cannot be null" }
            }
        }
}