package edu.ntnu.idatt2106.gr6.backend.service

import edu.ntnu.idatt2106.gr6.backend.DTOs.CreateEventRequest
import edu.ntnu.idatt2106.gr6.backend.DTOs.EventResponse
import edu.ntnu.idatt2106.gr6.backend.DTOs.UpdateEventRequest
import edu.ntnu.idatt2106.gr6.backend.DTOs.toEvent
import edu.ntnu.idatt2106.gr6.backend.exception.EventDoesNotExistException
import edu.ntnu.idatt2106.gr6.backend.model.Event
import edu.ntnu.idatt2106.gr6.backend.model.EventType
import edu.ntnu.idatt2106.gr6.backend.model.Severity
import edu.ntnu.idatt2106.gr6.backend.model.Status
import edu.ntnu.idatt2106.gr6.backend.repository.EventRepository
import edu.ntnu.idatt2106.gr6.backend.util.IdGenerator
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class EventService(
    private val eventRepository: EventRepository,
    private val idGenerator: IdGenerator,
) {

    private val logger = org.slf4j.LoggerFactory.getLogger(EventService::class.java)

    fun getAllEvents(): List<EventResponse> {
        return eventRepository.findAllEvents().map { event ->
            event.toResponse()
        }
    }

    fun createEvent(request: CreateEventRequest): EventResponse {
        var eventId: String
        do {
            eventId = idGenerator.generateId(6)
        } while (eventRepository.findEventById(eventId) != null)
        val createdEvent = eventRepository.createEvent(request.toEvent(eventId))
        return createdEvent.toResponse()
    }

    fun getEventById(id: String): EventResponse? {
        val event = eventRepository.findEventById(id)
            ?: throw EventDoesNotExistException.forEventId(id)
        return event.toResponse()
    }

    fun deleteEventById(id: String): Boolean {
        eventRepository.findEventById(id)
            ?: throw EventDoesNotExistException.forEventId(id)
        return eventRepository.deleteEvent(id)
    }

    fun updateEvent(request: UpdateEventRequest): EventResponse? {
        val existingEvent = eventRepository.findEventById(request.id) ?:
            throw EventDoesNotExistException.forEventId(request.id)

        val updatedEvent = existingEvent.copy(
            id = request.id,
            name = request.name,
            description = request.description,
            content = request.content,
            location = request.location ?: existingEvent.location,
            startDate = request.startTime ?: existingEvent.startDate,
            endDate = request.endTime ?: existingEvent.endDate,
            type = EventType.fromString(request.type),
            status = Status.fromString(request.status),
            severity = Severity.fromString(request.severity),
            impactAreaRadiusKm = request.impactAreaRadiusKm ?: existingEvent.impactAreaRadiusKm,
            mandatoryEvacuationAreaRadiusKm = request.mandatoryEvacuationAreaRadiusKm ?: existingEvent.mandatoryEvacuationAreaRadiusKm,
            recommendedEvacuationAreaRadiusKm = request.recommendedEvacuationAreaRadiusKm ?: existingEvent.recommendedEvacuationAreaRadiusKm,
        )

        return eventRepository.updateEvent(updatedEvent).toResponse()
    }

    fun Event.toResponse(): EventResponse {
        return EventResponse(
            id = this.id,
            name = this.name,
            description = this.description,
            content = this.content,
            location = this.location,
            impactAreaRadiusKm = this.impactAreaRadiusKm,
            mandatoryEvacuationAreaRadiusKm = this.mandatoryEvacuationAreaRadiusKm,
            recommendedEvacuationAreaRadiusKm = this.recommendedEvacuationAreaRadiusKm,
            startDate = this.startDate,
            endDate = this.endDate,
            type = this.type.toString(),
            status = this.status.toString(),
            severity = this.severity.toString(),
            updatedAt = this.updatedAt,
            createdAt = this.createdAt,
        )
    }

    fun Event.toModel(event: CreateEventRequest): Event {
        return Event(
            id = this.id,
            name = event.name,
            description = event.description,
            content = event.content,
            location = event.location,
            startDate = startDate,
            endDate = endDate,
            type = EventType.fromString(event.type),
            severity = Severity.fromString(event.severity),
            impactAreaRadiusKm = event.impactAreaRadiusKm,
            mandatoryEvacuationAreaRadiusKm = event.mandatoryEvacuationAreaRadiusKm,
            recommendedEvacuationAreaRadiusKm = event.recommendedEvacuationAreaRadiusKm,
            status = this.status,
        )
    }

    internal fun locationToString(location: String): String {
        return location.replace("POINT(", "").replace(")", "")
    }
}