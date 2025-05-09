package edu.ntnu.idatt2106.gr6.backend.integrationTests

import com.fasterxml.jackson.databind.ObjectMapper
import edu.ntnu.idatt2106.gr6.backend.BaseIntegrationTest
import edu.ntnu.idatt2106.gr6.backend.DTOs.Location
import edu.ntnu.idatt2106.gr6.backend.DTOs.MarkerDTOs
import edu.ntnu.idatt2106.gr6.backend.DTOs.UserDTOs
import edu.ntnu.idatt2106.gr6.backend.TestConfig
import edu.ntnu.idatt2106.gr6.backend.model.ContactInfo
import edu.ntnu.idatt2106.gr6.backend.model.OpeningHours
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.description
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.http.RequestEntity.post
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import javax.sql.DataSource
import kotlin.text.get

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Import(TestConfig::class)
class MarkerFlowTest: BaseIntegrationTest() {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var dataSource: DataSource

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    var jwtToken: String = ""

    @BeforeEach
    fun setup() {
        dataSource.connection.use { connection ->
            connection.createStatement().execute("DELETE FROM marker")
        }

        val userid = "3e7aaed0-48dc-4ba1-b77a-944ec0540f24"
        val hashedPassword = "\$2a\$12\$FMs3ktuRwBgvH.gh3.Y0aueoVcFxsxp9W8qFB3ilHOurix1tfS35K"
        jdbcTemplate.execute("""
            INSERT INTO users (id, email, name, password, created_at, verified, role_id)
            VALUES ("3e7aaed0-48dc-4ba1-b77a-944ec0540f24", 'test@example.com', 'my name', '$hashedPassword' ,CURRENT_TIMESTAMP, true, 2)
        """)


        val user = UserDTOs.LoginUserRequest(
            email = "test@example.com",
            password = "wordpass",
        )


        val loginResult = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
            .andExpect(status().isOk)
            .andReturn()

        val responseBody = loginResult.response.contentAsString
        val responseMap = objectMapper.readValue(responseBody, Map::class.java)
        jwtToken = responseMap["token"] as String
    }

    @Test
    fun `test create marker`() {

        val request = MarkerDTOs.CreateMarkerRequest(
            name = "test",
            eventId = null,
            storageId = null,
            description = "Marker description",
            location = Location(
                latitude = 60.39299,
                longitude = 5.32415
            ),
            contactInfo = null,
            openingHours = null,
            imageId = null,
            type = "hospital"
        )

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/markers")
                .header("Authorization", "Bearer $jwtToken")

                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType("application/json"))
    }
}