package edu.ntnu.idatt2106.gr6.backend.service

import edu.ntnu.idatt2106.gr6.backend.DTOs.UserDTOs
import edu.ntnu.idatt2106.gr6.backend.DTOs.UserDTOs.EditUserNameEmailRequest
import edu.ntnu.idatt2106.gr6.backend.DTOs.UserDTOs.EditUserNameEmailResponse
import edu.ntnu.idatt2106.gr6.backend.DTOs.UserDTOs.ChangePasswordRequest
import edu.ntnu.idatt2106.gr6.backend.DTOs.UserDTOs.ChangeUserTrackingPreferenceRequest
import edu.ntnu.idatt2106.gr6.backend.exception.DatabaseNoRowsAffectedException
import edu.ntnu.idatt2106.gr6.backend.exception.InvalidCredentialsException
import edu.ntnu.idatt2106.gr6.backend.exception.UserAlreadyExistsException
import edu.ntnu.idatt2106.gr6.backend.exception.UserFailedToUpdateCredentialsException
import edu.ntnu.idatt2106.gr6.backend.exception.UserNotFoundException
import edu.ntnu.idatt2106.gr6.backend.repository.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Service
class UserService(
    private val userRepository: UserRepository,
    private val userContextService: UserContextService,
    private val jwtService: JwtService,
    private val passwordEncoder: BCryptPasswordEncoder
) {

    fun updateUserDetails(request: EditUserNameEmailRequest): EditUserNameEmailResponse {
        val userId = userContextService.getCurrentUserId()

        val existingUser = userRepository.findByEmail(request.email)
        if (existingUser != null && existingUser.id != userId) {
            throw UserAlreadyExistsException.forEmail(request.email)
        }

        val updated = userRepository.updateUser(userId, request.name, request.email, true)
        if (!updated) {
            throw DatabaseNoRowsAffectedException.forQuery(userId.toString())
        }

        val updatedUser = userRepository.findById(userId)
            ?: throw UserFailedToUpdateCredentialsException.forUserId(userId.toString())

        val newToken = jwtService.generateToken(updatedUser)

        return EditUserNameEmailResponse(
            id = updatedUser.id.toString(),
            name = updatedUser.name,
            email = updatedUser.email,
            token = newToken
        )
    }

    fun changePassword(request: ChangePasswordRequest): Boolean {
        val userId = userContextService.getCurrentUserId()

        val user = userRepository.findById(userId)
            ?: throw UserNotFoundException.forUserId(userId.toString())

        if (!passwordEncoder.matches(request.oldPassword, user.password)) {
            throw InvalidCredentialsException.forUserId(userId.toString())
        }

        val newHashedPassword = passwordEncoder.encode(request.newPassword)
        val updated = userRepository.updatePassword(userId, newHashedPassword)

        if (!updated) {
            throw DatabaseNoRowsAffectedException.forQuery(userId.toString())
        }

        return true
    }

    fun updateUserTrackingPreferences(request: ChangeUserTrackingPreferenceRequest): Boolean {
        val userId: UUID = userContextService.getCurrentUserId()
        userRepository.updateUserTrackingPreferences(userId, request.trackingEnabled)
        return true
    }

    fun deleteUserTrackingHistory(): Boolean {
        val userId: UUID = userContextService.getCurrentUserId()
        val updated = userRepository.deleteUserTrackingHistory(userId)
        if (!updated) {
            throw UserNotFoundException.forUserId(userId.toString())
        }

        return true
    }
}
