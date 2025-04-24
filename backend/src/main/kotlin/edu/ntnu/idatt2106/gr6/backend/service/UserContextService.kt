package edu.ntnu.idatt2106.gr6.backend.service

import org.springframework.stereotype.Service
import org.springframework.web.context.annotation.RequestScope
import java.util.UUID


@Service
@RequestScope
class UserContextService {
    private var userId: UUID? = null

    fun getCurrentUserId(): UUID = userId ?: UUID.fromString("00000000-0000-0000-0000-000000000000")

    fun setCurrentUserId(userId: UUID) {
        this.userId = userId
    }
}