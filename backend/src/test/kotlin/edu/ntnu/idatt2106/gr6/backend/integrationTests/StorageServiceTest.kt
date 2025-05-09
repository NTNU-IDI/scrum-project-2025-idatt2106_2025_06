package edu.ntnu.idatt2106.gr6.backend.integrationTests

import edu.ntnu.idatt2106.gr6.backend.BaseIntegrationTest
import edu.ntnu.idatt2106.gr6.backend.DTOs.StorageDTOs.UpdateStorageRequest
import edu.ntnu.idatt2106.gr6.backend.DTOs.StorageDTOs.CreateStorageRequest
import edu.ntnu.idatt2106.gr6.backend.model.Location
import edu.ntnu.idatt2106.gr6.backend.service.StorageService
import edu.ntnu.idatt2106.gr6.backend.testconfig.TestUserContextService
import junit.framework.TestCase.assertEquals
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.testcontainers.junit.jupiter.Testcontainers
import java.util.UUID
import javax.sql.DataSource


@SpringBootTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StorageServiceTest : BaseIntegrationTest() {

    @Autowired
    lateinit var dataSource: DataSource

    @Autowired
    lateinit var storageService: StorageService

    @Autowired
    lateinit var userContextService: TestUserContextService


    lateinit var createRequest1: CreateStorageRequest

    @BeforeEach
    fun beforeAll() {
        cleanDatabase()
        insertTestUser()
        insertTestStorage()
        userContextService.setCurrentUserId(UUID.fromString("00000000-0000-0000-0000-000000000001"))
    }

    @BeforeEach
    fun setUp() {

        createRequest1 = CreateStorageRequest(
            name = "TestStorage1",
            location = Location(latitude = 60.0, longitude = 5.0)
        )
    }

    private fun cleanDatabase() {
        dataSource.connection.use { conn ->
            conn.createStatement().use { stmt ->
                stmt.execute("SET FOREIGN_KEY_CHECKS = 0")
                stmt.execute("TRUNCATE TABLE users")
                stmt.execute("TRUNCATE TABLE user_storages")
                stmt.execute("TRUNCATE TABLE storages")
                stmt.execute("SET FOREIGN_KEY_CHECKS = 1")
            }
        }
    }

    private fun insertTestUser() {
        dataSource.connection.use { conn ->
            conn.prepareStatement("""
            INSERT INTO users (
                id, email, name, password, created_at, verified, role_id
            ) VALUES (
                '00000000-0000-0000-0000-000000000001',
                'testuser@example.com',
                'Test User',
                'hashed-password-placeholder',
                NOW(),
                TRUE,
                1
            )
        """.trimIndent()).execute()
        }
    }

    private fun insertTestStorage() {
        dataSource.connection.use { conn ->
            conn.prepareStatement("""
            INSERT INTO storages (
                id, name, storage_owner, token, location, created_at, updated_at
            ) VALUES (
                '123',
                'test storage',
                '00000000-0000-0000-0000-000000000001',
                '123',
                ST_GeomFromText('POINT(10.4 63.0)', 4326),                    
                NOW(),
                NOW()
            )
        """.trimIndent()).execute()

            conn.prepareStatement("""
            INSERT INTO user_storages (
                user_id, storage_id
            ) VALUES (
                '00000000-0000-0000-0000-000000000001',
                '123'
            )
        """.trimIndent()).execute()
        }
    }

    private fun insertSecondTestUser() {
        dataSource.connection.use { conn ->
            conn.prepareStatement(
                """
            INSERT INTO users (
                id, email, name, password, created_at, verified, role_id
            ) VALUES (
                '00000000-0000-0000-0000-000000000002',
                'seconduser@example.com',
                'Second User',
                'hashed-password-placeholder',
                NOW(),
                TRUE,
                1
            )
        """.trimIndent()
            ).execute()
        }
    }


    @Test
    fun contextLoads() {
        // This test will pass if the application context loads successfully
    }

    private val logger = org.slf4j.LoggerFactory.getLogger(EventServiceTest::class.java)


    @DisplayName("Positive Tests")
    @Nested
    inner class PositiveTests {
        /**
         * Verifies that creating a new storage with valid input returns a storage with the expected name.
         */
        @Test
        fun `createStorage should create and return new storage`() {
            // Arrange
//            insertTestUser()

            // Act
            val storage1 = storageService.createStorage(createRequest1)

            // Assert
            assert(storage1.name == createRequest1.name)
        }

        /**
         * Verifies that the service returns the correct storage associated with the current user.
         */
        @Test
        fun `findStorageById should return correct storage if it exists`() {
            // Arrange
//            insertTestUser()
//            insertTestStorage()

            // Act
            val foundStorages = storageService.getStoragesByUserId()
            logger.info("Found storage $foundStorages")

            // Assert
            Assertions.assertEquals(1, foundStorages.size)
            val storage = foundStorages.first()
            Assertions.assertEquals("test storage", storage.name)
            Assertions.assertEquals("00000000-0000-0000-0000-000000000001", storage.storageOwner.toString())
            Assertions.assertEquals("123", storage.token)

        }

        /**
         * Verifies that a second user can join a storage using a valid token,
         * and that the member list updates correctly.
         */
        @Test
        fun `joinStorageByToken should add current user to storage`() {
            // Arrange
            insertSecondTestUser()
            val userId = "00000000-0000-0000-0000-000000000002"
            userContextService.setCurrentUserId(UUID.fromString(userId))

            // Act
            val joined = storageService.joinStorageByToken("123")

            // Assert

            val members = storageService.getMemberDTOsOfStorage("123")
            Assertions.assertEquals(2, members.size, "Storage should have 2 members after join")
            Assertions.assertTrue(members.any { it.name == "Second User" }, "Second user should appear in members")
        }

        /**
         * Verifies that getMemberDTOsOfStorage returns the correct list of users
         * currently in the specified storage.
         */
        @Test
        fun `getMemberDTOsOfStorage should return list of members`() {
            // Act
            val members = storageService.getMemberDTOsOfStorage("123")

            // Assert
            assertEquals(1, members.size)
            val member = members.first()
            assertEquals("00000000-0000-0000-0000-000000000001", member.id)
            assertEquals("Test User", member.name)
        }

        /**
         * Verifies that a user (0002) can be removed from the storage,
         * and that the resulting member list is correct.
         */
        @Test
        fun `removeUserFromStorage should remove user 0002 from storage`() {
            // Arrange
            insertSecondTestUser()
            userContextService.setCurrentUserId(UUID.fromString("00000000-0000-0000-0000-000000000002"))
            storageService.joinStorageByToken("123")

            // Act
            val removed = storageService.removeUserFromStorage("00000000-0000-0000-0000-000000000002", "123")

            // Assert
            Assertions.assertTrue(removed, "User 0002 should be successfully removed from the storage")

            val members = storageService.getMemberDTOsOfStorage("123")
            Assertions.assertEquals(1, members.size, "Only one user should remain in the storage")
            Assertions.assertEquals("Test User", members.first().name)
        }

        /**
         * Verifies that the current user gets all storages they are connected to.
         */
        @Test
        fun `getStoragesByUserId should return all user storages`() {
            // Act
            val storages = storageService.getStoragesByUserId()

            // Assert
            Assertions.assertEquals(1, storages.size, "User should be associated with 1 storage")
            val storage = storages.first()
            Assertions.assertEquals("123", storage.id)
            Assertions.assertEquals("test storage", storage.name)
            Assertions.assertEquals("00000000-0000-0000-0000-000000000001", storage.storageOwner.toString())
        }

        /**
         * Verifies that the owner of the storage can update its name and location.
         */
        @Test
        fun `updateStorage should update name and location if user is owner`() {
            // Arrange
            val newName = "Updated Storage Name"
            val newLocation = Location(latitude = 70.0, longitude = 8.0)

            val updateRequest = UpdateStorageRequest(
                id = "123",
                name = newName,
                location = newLocation
            )

            // Act
            val success = storageService.updateStorage(updateRequest)

            // Assert
            Assertions.assertTrue(success)

            val updatedStorage = storageService.getStoragesByUserId().firstOrNull { it.id == "123" }
            Assertions.assertNotNull(updatedStorage)
            Assertions.assertEquals(newName, updatedStorage!!.name)
            Assertions.assertEquals(newLocation.latitude, updatedStorage.location?.latitude)
            Assertions.assertEquals(newLocation.longitude, updatedStorage.location?.longitude)
        }

        /**
         * Verifies that the owner of the storage can delete it successfully,
         * and that it no longer appears in their storage list.
         */
        @Test
        fun `deleteStorage should delete if user is owner`() {
            // Act
            val result = storageService.deleteStorage("123")

            // Assert
            Assertions.assertTrue(result, "Storage should be successfully deleted")

            val storagesAfterDelete = storageService.getStoragesByUserId()
            Assertions.assertEquals(0, storagesAfterDelete.size, "No storages should remain for the user")
        }

    }

    @DisplayName("Negative Tests")
    @Nested
    inner class NegativeTests {
        /**
         * Verifies that joining a storage with an invalid token throws an exception.
         */
        @Test
        fun `joinStorageByToken should fail when token is invalid`() {
            val invalidToken = "notfound"

            val exception = Assertions.assertThrows(IllegalArgumentException::class.java) {
                storageService.joinStorageByToken(invalidToken)
            }

            Assertions.assertTrue(exception.message!!.contains("Storage with token"), "Expected failure on invalid token")
        }

        /**
         * Verifies that a non-owner user cannot update a storage.
         */
        @Test
        fun `updateStorage should fail when user is not the owner`() {
            // Arrange
            insertSecondTestUser()
            userContextService.setCurrentUserId(UUID.fromString("00000000-0000-0000-0000-000000000002"))  // Not the owner
            val updateRequest = UpdateStorageRequest(
                id = "123",
                name = "Hack Attempt",
                location = Location(0.0, 0.0)
            )

            // Act
            val result = storageService.updateStorage(updateRequest)

            // Assert
            Assertions.assertFalse(result, "Non-owner should not be allowed to update storage")
        }
    }
}
