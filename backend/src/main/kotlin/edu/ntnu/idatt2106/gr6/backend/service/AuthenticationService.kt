package edu.ntnu.idatt2106.gr6.backend.service

import edu.ntnu.idatt2106.gr6.backend.DTOs.UserDTOs.LoginUserRequest
import edu.ntnu.idatt2106.gr6.backend.repository.UserRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.crypto.password.PasswordEncoder
import edu.ntnu.idatt2106.gr6.backend.DTOs.UserDTOs.UserResponse
import edu.ntnu.idatt2106.gr6.backend.exception.AuthenticationException
import edu.ntnu.idatt2106.gr6.backend.exception.InvalidCredentialsException
import edu.ntnu.idatt2106.gr6.backend.exception.UserAlreadyExistsException
import edu.ntnu.idatt2106.gr6.backend.exception.UserNotFoundException
import edu.ntnu.idatt2106.gr6.backend.model.User
import edu.ntnu.idatt2106.gr6.backend.repository.RoleRepository
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service
import org.slf4j.LoggerFactory

@Service
class AuthenticationService(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val authenticationManager: AuthenticationManager,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService
) {
    private val logger = LoggerFactory.getLogger(AuthenticationService::class.java)

    fun signupUser(
        name: String,
        email: String,
        password: String,
    ): UserResponse {
        if (userRepository.findByEmail(email) != null) {
            throw UserAlreadyExistsException.forEmail(email)
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

    fun loginUser(request: LoginUserRequest) : UserResponse {
        val email = request.email
        val password = request.password

        if (email.isBlank() || password.isBlank()) {
            throw InvalidCredentialsException.forEmail(email)
        }

        logger.info("Authenticating user with email: $email")
        try {
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    email,
                    password
                )
            )
        } catch (e: Exception) {
            logger.error("Authentication failed for user $email: ${e.message}")
            throw AuthenticationException.forEmail(email)
        }

        logger.info("User $email authenticated successfully.")

        return userRepository.findByEmail(email)?.toResponse()
            ?: throw UserNotFoundException.forEmail(email)
    }

    fun User.toResponse(): UserResponse {
        val token = jwtService.generateToken(this)
        logger.info("Authentication token: $token")
        return UserResponse(
            id = id.toString(),
            name = name,
            email = email,
            createdAt = createdAt,
            role = role.name,
            permissions = roleRepository.findPermissionsByRole(role.id).map { it.name }.toSet(),
            token = token,
            expiresIn = jwtService.getExpirationTime(),
        )
    }

}