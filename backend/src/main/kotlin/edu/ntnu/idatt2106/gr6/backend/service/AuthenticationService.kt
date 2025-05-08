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

        @Transactional
    fun signupUser(
        name: String,
        email: String,
        password: String,
    ): UserResponse {
        if (userRepository.findByEmail(email) != null) {
            throw UserAlreadyExistsException.forEmail(email)
        }

            logger.info("adasddasdas")

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
            logger.info("adasddasdas" + token)
        val verificationToken: String = token
        userRepository.saveEmailVerificationToken(generateVerificationId(), UUID.fromString(savedUser.id), verificationToken)
        emailService.sendVerificationLink(email, verificationToken)

        return savedUser
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

        val user = userRepository.findByEmail(email)
            ?: throw UserNotFoundException.forEmail(email)
        if (!user.verified) {
            logger.warn("User $email has not verified their email.")
            throw UserEmailNotVerifiedException.forEmail(email)
        }

        return userRepository.findByEmail(email)?.toResponse()
            ?: throw UserNotFoundException.forEmail(email)
    }


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
            logger.info("token is null")
            throw TokenIncorrectlyFormattedException.forToken(verificationCode)
        }
        logger.info("verified: $token")
        logger.info("verified:ssssss $verificationCode")

        if (token == null || token.isBlank()) {
            token = generateVerificationId()
            userRepository.saveEmailVerificationToken(token, UUID.fromString(userId), email)
        }

        logger.info("verified2: $verificationCode")

        val user = userRepository.findById(UUID.fromString(userId))
            ?: throw UserNotFoundException.forUserId(userId)
        val fetchUserId = user.id?.toString()
            ?: throw UserNotFoundException.forUserId(userId)

        logger.info("verified2.5: $fetchUserId")

        if(!jwtService.isVerificationTokenValid(verificationCode)) {
            throw TokenInvalidException.forToken(verificationCode)
        }

        logger.info("verified3: $verificationCode")

        if(user.email != email) {
            throw TokenInvalidException.forToken(verificationCode)
        }

        logger.info("verified4: $verificationCode")

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