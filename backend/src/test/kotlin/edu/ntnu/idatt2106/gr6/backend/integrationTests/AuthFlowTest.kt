package edu.ntnu.idatt2106.gr6.backend.integrationTests

import com.fasterxml.jackson.databind.ObjectMapper
import edu.ntnu.idatt2106.gr6.backend.BaseIntegrationTest
import edu.ntnu.idatt2106.gr6.backend.DTOs.UserDTOs
import edu.ntnu.idatt2106.gr6.backend.TestConfig
import edu.ntnu.idatt2106.gr6.backend.service.JwtService
import edu.ntnu.idatt2106.gr6.backend.service.RecaptchaService
import edu.ntnu.idatt2106.gr6.backend.repository.UserRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.whenever
import org.mockito.kotlin.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.UUID
import javax.sql.DataSource


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Import(TestConfig::class)
class AuthFlowTest: BaseIntegrationTest() {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var jwtService: JwtService

    @Autowired
    private lateinit var recaptchaService: RecaptchaService

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var dataSource: DataSource

    private val baseUrl = "/api/auth"
    private var userToken: String? = null
    private var adminToken: String? = null

    @BeforeEach
    fun setup() {

        val mockToken: String = "test-token";
        whenever(recaptchaService.verifyRecaptcha(any())).thenReturn(true)

        userRepository.findByEmail("user@test.com")?.let {
            userRepository.deleteUserByEmail(it.email)
        }
        userRepository.findByEmail("admin@test.com")?.let {
            userRepository.deleteUserByEmail(it.email)
        }


        fun cleanDB() {
            dataSource.connection.use { connection ->
                connection.createStatement().use { statement ->
                    statement.execute("SET FOREIGN_KEY_CHECKS = 0")
                    statement.execute("TRUNCATE TABLE users")
                    statement.execute("SET FOREIGN_KEY_CHECKS = 1")
                }
            }
        }
    }

    /**
     * Test the full authentication flow:
     * 1. User signs up
     * 2. User receives a verification email
     * 3. User verifies their email
     * 4. User logs in
     * 5. User accesses a protected resource
     * 6.
     * 7. User tries to access the protected resource again and fails
     */

    @Test
    fun `test full authentication flow - signup, verification, and login`() {
        // 1.
        val userRegisterRequest = UserDTOs.CreateUserRequest(
            name = "Test User",
            email = "user@test.com",
            password = "Password123!",
            recaptcha = "dummy-token"
        )

        mockMvc.perform(
            post("$baseUrl/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRegisterRequest))
        )
            .andExpect(status().isCreated)

        // 2.
        val user = userRepository.findByEmail("user@test.com")
        assertNotNull(user, "User should be created in database")
        assertFalse(user!!.verified, "User should not be verified yet")


        val verificationCode = getVerificationCodeForUser(user.email)

        // 3.
        mockMvc.perform(
            get("$baseUrl/email-verification/$verificationCode")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().string("User verified successfully"))

        // 4.
        val verifiedUser = userRepository.findByEmail("user@test.com")
        assertTrue(verifiedUser!!.verified, "User should be verified after email verification")

        // 5.
        val invalidLoginRequest = UserDTOs.LoginUserRequest(
            email = "user@test.com",
            password = "WrongPassword"
        )

        mockMvc.perform(
            post("$baseUrl/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidLoginRequest))
        )
            .andExpect(status().isUnauthorized)

        // 6.
        val validLoginRequest = UserDTOs.LoginUserRequest(
            email = "user@test.com",
            password = "Password123!"
        )

        val loginResult = mockMvc.perform(
            post("$baseUrl/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validLoginRequest))
        )
            .andExpect(status().isOk)
            .andReturn()

        // 7.
        val userResponse = objectMapper.readValue(
            loginResult.response.contentAsString,
            UserDTOs.UserResponse::class.java
        )

        assertNotNull(userResponse.token, "Login response should include a JWT token")
        userToken = userResponse.token

        // 8.
        mockMvc.perform(
            get("/api/storages/my-storages")
                .header("Authorization", "Bearer $userToken")
        )
            .andExpect(status().isOk)

        // 9.
        mockMvc.perform(
            get("/api/storages/my-storages")
        )
            .andExpect(status().isUnauthorized)
    }

    /**
     * Helper method to get the verification code for a user
     */
    private fun getVerificationCodeForUser(email: String): String {
        // Get the user from repository
        val user = userRepository.findByEmail(email)
            ?: throw IllegalStateException("User not found with email $email")

        // Generate real verification token using the JwtService
        return jwtService.generateVerificationToken(UUID.fromString(user.id.toString()), user.email)
    }

    /**
     * Helper methods for assertions
     */
    private fun assertNotNull(obj: Any?, message: String) {
        if (obj == null) {
            throw AssertionError(message)
        }
    }

    private fun assertTrue(condition: Boolean, message: String) {
        if (!condition) {
            throw AssertionError(message)
        }
    }

    private fun assertFalse(condition: Boolean, message: String) {
        if (condition) {
            throw AssertionError(message)
        }
    }
}