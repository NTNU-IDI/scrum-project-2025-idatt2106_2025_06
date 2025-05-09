// TestUserContextService.kt
package edu.ntnu.idatt2106.gr6.backend.testconfig

import edu.ntnu.idatt2106.gr6.backend.service.UserContextService
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service
import java.util.*

@Service
@Primary
class TestUserContextService : UserContextService() {

    private var testUserId: UUID = UUID.fromString("00000000-0000-0000-0000-000000000001")

    override fun getCurrentUserId(): UUID = testUserId

    override fun setCurrentUserId(userId: UUID) {
        testUserId = userId
    }
}
