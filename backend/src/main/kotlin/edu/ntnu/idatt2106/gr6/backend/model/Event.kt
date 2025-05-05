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
            is Low -> return "low"
            is Medium -> return "medium"
            is High -> return "high"
        }
    }

    companion object {
        fun fromString(value: String): Severity =
            when(value) {
                "low" -> Low
                "medium" -> Medium
                "high" -> High
                else -> throw IllegalArgumentException("Invalid severity: $value")
            }
    }
}

sealed class Status {
    object Planned: Status()
    object Ongoing: Status()
    object Finished: Status()
    object Cancelled: Status()

    override fun toString(): String {
        when(this) {
            is Planned -> return "planned"
            is Ongoing -> return "ongoing"
            is Finished -> return "finished"
            is Cancelled -> return "cancelled"
        }
    }

    companion object {
        fun fromString(value: String): Status =
            when(value) {
                "planned" -> Planned
                "ongoing" -> Ongoing
                "finished" -> Finished
                "cancelled" -> Cancelled
                else -> throw IllegalArgumentException("Invalid status: $value")
            }
    }
}

data class Event (
    val id: String,
    val name: String? = null,
    val description: String? = null,
    val content: String? = null,
    val location: Location?,
    val type: EventType,
    val impactAreaRadiusKm: Double?,
    val mandatoryEvacuationAreaRadiusKm: Double?,
    val recommendedEvacuationAreaRadiusKm: Double?,
    val startDate: Instant? = null,
    val endDate: Instant? = null,
    val severity: Severity,
    val status: Status,
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
                require(status != null) { "Status cannot be null" }
                require(severity != null) { "Severity cannot be null" }
            }
        }
}