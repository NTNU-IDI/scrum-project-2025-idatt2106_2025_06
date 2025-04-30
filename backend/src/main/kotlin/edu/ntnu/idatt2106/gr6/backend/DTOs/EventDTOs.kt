package edu.ntnu.idatt2106.gr6.backend.DTOs

import edu.ntnu.idatt2106.gr6.backend.model.Event
import edu.ntnu.idatt2106.gr6.backend.model.EventType
import edu.ntnu.idatt2106.gr6.backend.model.Severity
import edu.ntnu.idatt2106.gr6.backend.model.Status
import java.time.Instant

data class CreateEventRequest(
    val name: String,
    val description: String,
    val content: String,
    val impactAreaRadiusKm: Double?,
    val mandatoryEvacuationAreaRadiusKm: Double?,
    val recommendedEvacuationAreaRadiusKm: Double?,
    val location: Location?,
    val startTime: Instant?,
    val endTime: Instant?,
    val status: String,
    val type: String,
    val severity: String,
)

data class EventResponse(
    val id: String,
    val name: String?,
    val description: String?,
    val content: String?,
    val location: Location?,
    val impactAreaRadiusKm: Double?,
    val mandatoryEvacuationAreaRadiusKm: Double?,
    val recommendedEvacuationAreaRadiusKm: Double?,
    val startDate: Instant?,
    val endDate: Instant?,
    val status: String,
    val type: String,
    val severity: String,
    val updatedAt: Instant?,
)

data class UpdateEventRequest(
    val id: String,
    val name: String,
    val description: String,
    val content: String,
    val impactAreaRadiusKm: Double?,
    val mandatoryEvacuationAreaRadiusKm: Double?,
    val recommendedEvacuationAreaRadiusKm: Double?,
    val location: Location?,
    val startTime: Instant?,
    val endTime: Instant?,
    val status: String,
    val type: String,
    val severity: String,
)

internal fun CreateEventRequest.toEvent(id: String): Event = Event(
    id = id, // Placeholder, will be replaced by the database
    name = this.name,
    description = this.description,
    content = this.content,
    location = this.location,
    startDate = this.startTime,
    endDate = this.endTime,
    status = this.status.let { Status.fromString(it) },
    type = this.type.let { EventType.fromString(it) },
    severity = Severity.fromString(this.severity),
    impactAreaRadiusKm = this.impactAreaRadiusKm,
    mandatoryEvacuationAreaRadiusKm = this.mandatoryEvacuationAreaRadiusKm,
    recommendedEvacuationAreaRadiusKm = this.recommendedEvacuationAreaRadiusKm,
)