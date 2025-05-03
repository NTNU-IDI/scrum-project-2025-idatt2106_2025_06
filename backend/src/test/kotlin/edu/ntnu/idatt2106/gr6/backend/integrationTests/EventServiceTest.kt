package edu.ntnu.idatt2106.gr6.backend.integrationTests

import edu.ntnu.idatt2106.gr6.backend.BaseIntegrationTest
import edu.ntnu.idatt2106.gr6.backend.DTOs.CreateEventRequest
import edu.ntnu.idatt2106.gr6.backend.DTOs.Location
import edu.ntnu.idatt2106.gr6.backend.DTOs.UpdateEventRequest
import edu.ntnu.idatt2106.gr6.backend.exception.EventDoesNotExistException
import edu.ntnu.idatt2106.gr6.backend.service.EventService
import org.flywaydb.core.internal.jdbc.JdbcTemplate
import org.junit.Before
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.junit.jupiter.Container

import org.testcontainers.containers.MySQLContainer
import java.time.Instant
import javax.sql.DataSource
import kotlin.enums.enumEntries

@SpringBootTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EventServiceTest: BaseIntegrationTest() {

    @Autowired
    lateinit var dataSource: DataSource

    @Autowired
    lateinit var eventService: EventService


    lateinit var createEventRequest1: CreateEventRequest
    lateinit var createEventRequest2: CreateEventRequest
    lateinit var createEventRequest3: CreateEventRequest
    lateinit var updateEventRequest: UpdateEventRequest


    @BeforeEach
    fun setupEach() {
        val location = Location(
            latitude = 60.39299,
            longitude = 5.32415
        )

        createEventRequest1 = CreateEventRequest(
            name = "Test Event",
            description = "This is a test event",
            content = "This is a test event",
            startTime = Instant.now(),
            endTime = Instant.now().plusSeconds(3600),
            location = location,
            impactAreaRadiusKm = 0.0,
            mandatoryEvacuationAreaRadiusKm = 0.0,
            recommendedEvacuationAreaRadiusKm = 0.0,
            status = "planned",
            severity = "low",
            type = "nuclear_attack"
        )

        createEventRequest2 = CreateEventRequest(
            name = "Test Event 2",
            description = "This is a test event 2",
            content = "This is a test event 2",
            startTime = Instant.now(),
            endTime = Instant.now().plusSeconds(7200),
            location = location,
            impactAreaRadiusKm = 1.0,
            mandatoryEvacuationAreaRadiusKm = 3.0,
            recommendedEvacuationAreaRadiusKm = 10.0,
            status = "planned",
            severity = "medium",
            type = "pandemic"
        )

        createEventRequest3 = CreateEventRequest(
            name = "Test Event 3",
            description = "This is a test event 3",
            content = "This is a test event 3",
            startTime = Instant.now(),
            endTime = Instant.now().plusSeconds(10800),
            location = location,
            impactAreaRadiusKm = 2.0,
            mandatoryEvacuationAreaRadiusKm = 5.0,
            recommendedEvacuationAreaRadiusKm = 15.0,
            status = "ongoing",
            severity = "high",
            type = "terror_attack"
        )
    }

    @BeforeEach
    fun cleanDB() {
        dataSource.connection.use { connection ->
            connection.createStatement().use { statement ->
                statement.execute("SET FOREIGN_KEY_CHECKS = 0")
                statement.execute("TRUNCATE TABLE event")
                statement.execute("SET FOREIGN_KEY_CHECKS = 1")
            }
        }
    }

    @Test
    fun contextLoads() {
        // This test will pass if the application context loads successfully
    }

    private val logger = org.slf4j.LoggerFactory.getLogger(EventServiceTest::class.java)

    @DisplayName("Positive tests")
    @Nested
    inner class PositiveTests {
        @Test
        @DisplayName("Test create event")
        fun testCreateEvent() {
            logger.info(eventService.getAllEvents().size.toString())
            eventService.createEvent(createEventRequest1)
            eventService.createEvent(createEventRequest2)
            eventService.createEvent(createEventRequest3)
            logger.info("Created events: ${eventService.getAllEvents().size}")
            assert(eventService.getAllEvents().size == 3)
        }

        @Test
        @DisplayName("Test get event by ID")
        fun testGetEventById() {
            val event = eventService.createEvent(createEventRequest1)
            val retrievedEvent = eventService.getEventById(event.id)
            assert(retrievedEvent != null)
            assert(retrievedEvent!!.id == event.id)
        }

        @Test
        @DisplayName("Test update event")
        fun testUpdateEvent() {
            val event = eventService.createEvent(createEventRequest1)
            val eventid = eventService.getAllEvents()[0].id
            updateEventRequest = UpdateEventRequest(
                id = eventid,
                name = "Updated Event",
                description = "This is an updated test event",
                content = "This is an updated test event",
                startTime = Instant.now(),
                endTime = Instant.now().plusSeconds(7200),
                impactAreaRadiusKm = 1.0,
                mandatoryEvacuationAreaRadiusKm = 3.0,
                recommendedEvacuationAreaRadiusKm = 10.0,
                status = "ongoing",
                severity = "medium",
                type = "nuclear_attack",
                location = Location(
                    latitude = 60.39299,
                    longitude = 5.32415
                )
            )
            eventService.updateEvent(updateEventRequest)

            val retrievedEvent = eventService.getEventById(event.id)
            assert(retrievedEvent != null)
            assert(retrievedEvent!!.name == "Updated Event")
        }

        @Test
        @DisplayName("Test delete event")
        fun testDeleteEvent() {
            val event = eventService.createEvent(createEventRequest1)
            val eventId = event.id
            eventService.deleteEventById(eventId)

            try {
                eventService.getEventById(eventId)
                fail("Expected EventDoesNotExistException but no exception was thrown")
            } catch (e: EventDoesNotExistException) {
                assert(e.message!!.contains("Event with id $eventId does not exist"))
            }
        }

        @Test
        @DisplayName("Test get all events")
        fun testGetAllEvents() {
            eventService.createEvent(createEventRequest1)
            eventService.createEvent(createEventRequest2)
            eventService.createEvent(createEventRequest3)
            val events = eventService.getAllEvents()
            assert(events.size == 3)
        }
    }

    @DisplayName("Negative tests")
    @Nested
    inner class NegativeTests {
        @Test
        @DisplayName("Test create event with invalid data")
        fun testCreateEventWithInvalidData() {
            val invalidRequest = CreateEventRequest(
                name = "",
                description = "This is a test event",
                content = "This is a test event",
                startTime = Instant.now(),
                endTime = Instant.now().plusSeconds(3600),
                location = null,
                impactAreaRadiusKm = 0.0,
                mandatoryEvacuationAreaRadiusKm = 0.0,
                recommendedEvacuationAreaRadiusKm = 0.0,
                status = "planned",
                severity = "low",
                type = "nuclear_attack"
            )
            try {
                eventService.createEvent(invalidRequest)
            } catch (e: Exception) {
                assert(e.message!!.contains("Name cannot be null"))
            }
        }

        @Test
        @DisplayName("Test get event by invalid ID")
        fun testGetEventByInvalidId() {
            val invalidId = ""
            try {
                eventService.getEventById(invalidId)
            } catch (e: Exception) {
                assert(e.message!!.contains("Event with id $invalidId does not exist."))
            }
        }

        @Test
        @DisplayName("Test update event with invalid data")
        fun testUpdateEventWithInvalidData() {
            val event = eventService.createEvent(createEventRequest1)
            val invalidUpdateRequest = UpdateEventRequest(
                id = event.id,
                name = "test123",
                description = "This is an updated test event",
                content = "This is an updated test event",
                startTime = Instant.now(),
                endTime = Instant.now().plusSeconds(7200),
                impactAreaRadiusKm = 1.0,
                mandatoryEvacuationAreaRadiusKm = 3.0,
                recommendedEvacuationAreaRadiusKm = 10.0,
                status = "ongoig",
                severity = "medium",
                type = "nuclear_attack",
                location = Location(
                    latitude = 60.39299,
                    longitude = 5.32415
                )
            )
            try {
                eventService.updateEvent(invalidUpdateRequest)
            } catch (e: Exception) {
                assert(e.message!!.contains("Invalid status: ongoig"))
            }
        }

        @Test
        @DisplayName("Test delete event with invalid ID")
        fun testDeleteEventWithInvalidId() {
            val invalidId = ""
            try {
                eventService.deleteEventById(invalidId)
            } catch (e: Exception) {
                assert(e.message!!.contains("Event with id $invalidId does not exist."))
            }
        }
    }
}