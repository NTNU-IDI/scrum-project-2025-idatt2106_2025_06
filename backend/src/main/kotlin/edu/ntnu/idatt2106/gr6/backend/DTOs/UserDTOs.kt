package edu.ntnu.idatt2106.gr6.backend.DTOs

import java.time.Instant

class UserDTOs {
    data class UserResponse(
        val id: String,
        val name: String,
        val email: String,
        val createdAt: Instant,
        val role: String,
        val permissions: Set<String>,
        val token: String,
        val expiresIn: Long,
    )

    data class CreateUserRequest(
        val name: String,
        val email: String,
        val password: String,
    )
}