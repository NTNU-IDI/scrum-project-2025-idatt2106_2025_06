package edu.ntnu.idatt2106.gr6.backend.service

import edu.ntnu.idatt2106.gr6.backend.DTOs.CreateEventRequest
import edu.ntnu.idatt2106.gr6.backend.DTOs.EventResponse
import edu.ntnu.idatt2106.gr6.backend.DTOs.toEvent
import edu.ntnu.idatt2106.gr6.backend.model.Event
import edu.ntnu.idatt2106.gr6.backend.model.EventType
import edu.ntnu.idatt2106.gr6.backend.model.Severity
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
        return eventRepository.findEventById(id)?.toResponse()
    }

    fun deleteEventById(id: String): Boolean {
        return eventRepository.deleteEvent(id)
    }

    fun updateEvent(id: String, request: CreateEventRequest): EventResponse? {
        val event = eventRepository.findEventById(id) ?: return null
        val updatedEvent = event.toModel(request)
        return eventRepository.updateEvent(updatedEvent).toResponse()
    }

    fun Event.toResponse(): EventResponse {
        return EventResponse(
            id = this.id,
            name = this.name,
            description = this.description,
            location = this.location,
            startDate = this.startDate,
            endDate = this.endDate,
            type = this.type.toString(),
            severity = this.severity.toString()
        )
    }

    fun Event.toModel(event: CreateEventRequest): Event {
        return Event(
            id = this.id,
            name = event.name,
            description = event.description,
            location = event.location,
            startDate = startDate,
            endDate = endDate,
            type = EventType.fromString(event.type),
            severity = Severity.fromString(event.severity),
            impactAreaRadiusKm = event.impactAreaRadiusKm,
            mandatoryEvacuationAreaRadiusKm = event.mandatoryEvacuationAreaRadiusKm,
            recommendedEvacuationAreaRadiusKm = event.recommendedEvacuationAreaRadiusKm,
            eta = event.eta,
        )
    }
}