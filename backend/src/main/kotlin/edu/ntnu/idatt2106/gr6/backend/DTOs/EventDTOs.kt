package edu.ntnu.idatt2106.gr6.backend.DTOs

import edu.ntnu.idatt2106.gr6.backend.model.Event
import edu.ntnu.idatt2106.gr6.backend.model.EventType
import edu.ntnu.idatt2106.gr6.backend.model.Severity
import java.time.Instant

data class CreateEventRequest(
    val name: String,
    val description: String,
    val impactAreaRadiusKm: Double?,
    val mandatoryEvacuationAreaRadiusKm: Double?,
    val recommendedEvacuationAreaRadiusKm: Double?,
    val location: Location?,
    val startTime: Instant?,
    val endTime: Instant?,
    val eta: Instant?,
    val type: String,
    val severity: String,
)

data class EventResponse(
    val id: String,
    val name: String?,
    val description: String?,
    val location: Location?,
    val startDate: Instant?,
    val endDate: Instant?,
    val type: String,
    val severity: String,
)

internal fun CreateEventRequest.toEvent(id: String): Event = Event(
    id = id, // Placeholder, will be replaced by the database
    name = this.name,
    description = this.description,
    location = this.location,
    startDate = this.startTime,
    endDate = this.endTime,
    eta = this.eta,
    type = this.type?.let { EventType.fromString(it) },
    severity = Severity.fromString(this.severity),
    impactAreaRadiusKm = this.impactAreaRadiusKm,
    mandatoryEvacuationAreaRadiusKm = this.mandatoryEvacuationAreaRadiusKm,
    recommendedEvacuationAreaRadiusKm = this.recommendedEvacuationAreaRadiusKm,
)