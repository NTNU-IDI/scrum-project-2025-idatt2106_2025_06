package edu.ntnu.idatt2106.gr6.backend.service

import edu.ntnu.idatt2106.gr6.backend.repository.UserRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.crypto.password.PasswordEncoder
import edu.ntnu.idatt2106.gr6.backend.DTOs.UserDTOs.UserResponse
import edu.ntnu.idatt2106.gr6.backend.model.User
import edu.ntnu.idatt2106.gr6.backend.repository.RoleRepository
import org.springframework.stereotype.Service

@Service
class AuthenticationService(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val authenticationManager: AuthenticationManager,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService
) {
    fun signupUser(
        name: String,
        email: String,
        password: String,
    ): UserResponse {
        if (userRepository.findByEmail(email) != null) {
            throw IllegalArgumentException("User with email $email already exists")
        }

        val userToSave = User(
            name = name,
            email = email,
            passwordHashed = passwordEncoder.encode(password),
            role = roleRepository.findDefaultRole()
                ?: throw IllegalStateException("Default role not found"),
            verified = false,

        )
        return userRepository.save(userToSave).toResponse()
    }

    fun User.toResponse(): UserResponse {
        val token = jwtService.generateToken(this)
        return UserResponse(
            id = id.toString(),
            name = name,
            email = email,
            createdAt = createdAt,
            role = role.name,
            permissions = role.permissions.map { it.name }.toSet(),
            token = token,
            expiresIn = jwtService.getExpirationTime(),
        )
    }

}