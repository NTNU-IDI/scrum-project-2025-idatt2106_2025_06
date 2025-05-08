package edu.ntnu.idatt2106.gr6.backend.DTOs

import java.time.Instant

class UserDTOs {
    data class UserResponse(
        val id: String,
        val name: String,
        val email: String,
        val createdAt: Instant,
        val role: String,
        val verified: Boolean,
        val trackingEnabled: Boolean,
        val permissions: Set<String>,
        val token: String,
        val expiresIn: Long,
    )

    data class SimpleUserResponse (
        val id: String,
        val name: String
    )

    data class CreateUserRequest(
        val name: String,
        val email: String,
        val password: String,
    )

    data class LoginUserRequest(
        val email: String,
        val password: String,
    )

    data class EditUserNameEmailRequest(
        val name: String,
        val email: String
    )

    data class EditUserNameEmailResponse(
        val id: String,
        val name: String,
        val email: String,
        val token: String
    )

    data class ChangePasswordRequest(
        val oldPassword: String,
        val newPassword: String
    )

    data class ChangePasswordResponse(
        val message: String
    )

    data class UpdateUserLocationRequest(
        val location: Location
    )

    data class GetUserLocationRequest(
        val userId: String,
    )

    data class UserLocationResponse(
        val userId: String,
        val location: Location,
    )

    data class ChangeUserTrackingPreferenceRequest(
        val trackingEnabled: Boolean
    )
}