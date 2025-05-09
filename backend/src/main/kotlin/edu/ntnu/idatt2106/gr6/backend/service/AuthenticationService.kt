package edu.ntnu.idatt2106.gr6.backend.service

import edu.ntnu.idatt2106.gr6.backend.DTOs.UserDTOs.LoginUserRequest
import edu.ntnu.idatt2106.gr6.backend.repository.UserRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.crypto.password.PasswordEncoder
import edu.ntnu.idatt2106.gr6.backend.DTOs.UserDTOs.UserResponse
import edu.ntnu.idatt2106.gr6.backend.exception.AuthenticationException
import edu.ntnu.idatt2106.gr6.backend.exception.IdGeneratorMaxAttemptsReachedException
import edu.ntnu.idatt2106.gr6.backend.exception.InvalidCredentialsException
import edu.ntnu.idatt2106.gr6.backend.exception.TokenIncorrectlyFormattedException
import edu.ntnu.idatt2106.gr6.backend.exception.TokenInvalidException
import edu.ntnu.idatt2106.gr6.backend.exception.UserAlreadyExistsException
import edu.ntnu.idatt2106.gr6.backend.exception.UserEmailNotVerifiedException
import edu.ntnu.idatt2106.gr6.backend.exception.UserNotFoundException
import edu.ntnu.idatt2106.gr6.backend.model.User
import edu.ntnu.idatt2106.gr6.backend.repository.RoleRepository
import edu.ntnu.idatt2106.gr6.backend.util.IdGenerator
import jakarta.transaction.Transactional
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service
import org.slf4j.LoggerFactory
import java.util.UUID

/**
 * Service class for handling user authentication and registration.
 * It contains methods for signing up users, logging in users, verifying users.
 *
 *
 * @param userRepository The repository for user data access.
 * @param roleRepository The repository for role data access.
 * @param authenticationManager The authentication manager for handling authentication.
 * @param passwordEncoder The password encoder for encoding passwords.
 * @param emailService The email service for sending verification emails.
 * @param jwtService The JWT service for generating and validating tokens.
 * @param idGenerator The ID generator for generating unique IDs.
 */

@Service
class AuthenticationService(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val authenticationManager: AuthenticationManager,
    private val passwordEncoder: PasswordEncoder,
    private val emailService: EmailService,
    private val jwtService: JwtService,
    private val idGenerator: IdGenerator
) {
    private val logger = LoggerFactory.getLogger(AuthenticationService::class.java)

    /**
     * Signs up a new user to the service. it checks if user already exits before proceeding.
     * Request is validated and mapped to a User object. User is saved to the database. a verification
     * code is then generated and sent to the user via email. the saved user object is then returned.
     *
     * @param name The name of the user.
     * @param email The email of the user.
     * @param password The password of the user.
     */

        @Transactional
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
        val savedUser =  userRepository.save(userToSave).toResponse()
        logger.info("User $name with email $email created successfully.")
            val token = jwtService.generateVerificationToken(
                UUID.fromString(savedUser.id), savedUser.email
            )
        val verificationToken: String = token
        userRepository.saveEmailVerificationToken(generateVerificationId(), UUID.fromString(savedUser.id), verificationToken)
        emailService.sendVerificationLink(email, verificationToken)

        return savedUser
    }

    /**
     * Logs in a user to the service. It checks if the user exists and if the credentials are valid.
     * If the credentials are valid and the user has verified their email, the user is authenticated
     * and a JWT token is generated.
     */

    fun loginUser(request: LoginUserRequest) : UserResponse {
        val email = request.email
        val password = request.password

        if (email.isBlank() || password.isBlank()) {
            throw InvalidCredentialsException.forEmail(email)
        }

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


        val user = userRepository.findByEmail(email)
            ?: throw UserNotFoundException.forEmail(email)
        if (!user.verified) {
            logger.warn("User $email has not verified their email.")
            throw UserEmailNotVerifiedException.forEmail(email)
        }

        return userRepository.findByEmail(email)?.toResponse()
            ?: throw UserNotFoundException.forEmail(email)
    }

    /**
     * Generates a unique verification ID for the user to verify using email. It checks if the ID already exists in the database
     * and generates a new one if it does. It will try to generate a new ID up to 10 times before throwing an exception.
     *
     * @return A unique verification ID.
     */

    private fun generateVerificationId(): String {
        var id: String = ""
        var attempts = 0
        val maxAttempts = 10
        do {
            if (++attempts >= maxAttempts) {
                throw IdGeneratorMaxAttemptsReachedException.forVerificationToken("")
            }
            id = idGenerator.generateId(12)
        } while (userRepository.findEmailVerificationToken(id) != null)
        return id
    }

    /**
     * Verifies the user using the verification code sent to their email. It checks if the token is valid and if the user exists.
     * If the token is valid, the user is updated in the database and their email is marked as verified.
     *
     * @param verificationCode The verification code sent to the user's email.
     */

    fun verifyUser(verificationCode: String): Boolean {
        // check if token already exists if not generate a new one
        logger.info("verify user with code: $verificationCode")

        val claims = jwtService.getAllClaimsFromToken(verificationCode)
        val userId = claims.subject
        val email = claims["email"] as String
        val purpose = claims["purpose"] as? String
        //MUST HAVE USE USERID NOT TOKEN
        var token: String? = userRepository.findEmailVerificationToken(userId)

        if (token == null) {
            throw TokenIncorrectlyFormattedException.forToken(verificationCode)
        }

        if (token == null || token.isBlank()) {
            token = generateVerificationId()
            userRepository.saveEmailVerificationToken(token, UUID.fromString(userId), email)
        }

        val user = userRepository.findById(UUID.fromString(userId))
            ?: throw UserNotFoundException.forUserId(userId)
        val fetchUserId = user.id?.toString()
            ?: throw UserNotFoundException.forUserId(userId)

        if(!jwtService.isVerificationTokenValid(verificationCode)) {
            throw TokenInvalidException.forToken(verificationCode)
        }

        if(user.email != email) {
            throw TokenInvalidException.forToken(verificationCode)
        }

        userRepository.updateUser(UUID.fromString(fetchUserId), user.name, email, true)
        return true
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
            verified = verified,
            trackingEnabled = trackingEnabled,
            expiresIn = jwtService.getExpirationTime(),
        )
    }
}