package edu.ntnu.idatt2106.gr6.backend.service

import edu.ntnu.idatt2106.gr6.backend.DTOs.UserDTOs.EditUserNameEmailRequest
import edu.ntnu.idatt2106.gr6.backend.DTOs.UserDTOs.EditUserNameEmailResponse
import edu.ntnu.idatt2106.gr6.backend.repository.UserRepository
import edu.ntnu.idatt2106.gr6.backend.service.JwtService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Service
class UserService(
    private val userRepository: UserRepository,
    private val userContextService: UserContextService,
    private val jwtService: JwtService
) {
    fun updateUserDetails(request: EditUserNameEmailRequest): EditUserNameEmailResponse {
        val userId = userContextService.getCurrentUserId()

        val existingUser = userRepository.findByEmail(request.email)
        if (existingUser != null && existingUser.id != userId) {
            throw ResponseStatusException(HttpStatus.CONFLICT, "Email is already in use")
        }

        val updated = userRepository.updateUser(userId, request.name, request.email)
        if (!updated) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        }

        val updatedUser = userRepository.findById(userId)
            ?: throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to load updated user")

        val newToken = jwtService.generateToken(updatedUser) // ✅ Generate a new token

        return EditUserNameEmailResponse(
            id = updatedUser.id.toString(),
            name = updatedUser.name,
            email = updatedUser.email,
            token = newToken
        )
    }

}
