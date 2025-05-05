package edu.ntnu.idatt2106.gr6.backend.exception

class EventDoesNotExistException(message: String) : RuntimeException(message) {
    companion object {
        fun forEventId(eventId: String) = EventDoesNotExistException("Event with id $eventId does not exist.")
        fun forEventName(eventName: String) = EventDoesNotExistException("Event with name $eventName does not exist.")
    }
}