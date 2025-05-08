package edu.ntnu.idatt2106.gr6.backend.model

import edu.ntnu.idatt2106.gr6.backend.DTOs.Location
import java.time.Instant

/**
 * Represents event types. Used to categorize events based on their nature.
 */

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

/**
 * Represents the severity of an event. Used to indicate the threat level of an event.
 */
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

/**
 * Represents the status of an event. Used to indicate the current state of an event.
 */
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

/**
 * data class representing an emergency event. Used to store information about an event.
 * Events have various properties describing their content, location, impact area, time and status
 *
 * @property id The unique identifier of the event.
 * @property name The name of the event.
 * @property description A description of the event.
 * @property content The content of the event.
 * @property location The location of the event.
 * @property type The type of the event.
 * @property impactAreaRadiusKm The radius of the impact area in kilometers.
 * @property mandatoryEvacuationAreaRadiusKm The radius of the mandatory evacuation area in kilometers.
 * @property recommendedEvacuationAreaRadiusKm The radius of the recommended evacuation area in kilometers.
 * @property startDate The start date of the event.
 * @property endDate The end date of the event.
 * @property severity The severity of the event.
 * @property status The status of the event.
 * @property createdAt The date the event was created.
 * @property updatedAt The date the event was last updated.
 */
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