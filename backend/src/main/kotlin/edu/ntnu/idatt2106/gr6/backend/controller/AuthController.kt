package edu.ntnu.idatt2106.gr6.backend.controller

import edu.ntnu.idatt2106.gr6.backend.DTOs.UserDTOs.LoginUserRequest
import edu.ntnu.idatt2106.gr6.backend.DTOs.UserDTOs.UserResponse
import edu.ntnu.idatt2106.gr6.backend.DTOs.UserDTOs.CreateUserRequest

import edu.ntnu.idatt2106.gr6.backend.service.AuthenticationService
import edu.ntnu.idatt2106.gr6.backend.service.JwtService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authenticationService: AuthenticationService,
    private val jwtService: JwtService
) {
    private val logger = LoggerFactory.getLogger(AuthController::class.java)

    @PostMapping("/signup")
    @PreAuthorize("permitAll()")
    @Operation(
        summary = "User signup",
        description = "Creates a new user account with the provided details."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "User created successfully"),
            ApiResponse(responseCode = "400", description = "Invalid input or email already in use"),
            ApiResponse(responseCode = "409", description = "Conflict: User Already Exists"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun signup(
        @RequestBody request: CreateUserRequest
    ): ResponseEntity<UserResponse> {
        logger.info("Received signup request for user: ${request.email}")
        val userResponse = authenticationService.signupUser(request.name, request.email, request.password)
        logger.info("User ${request.email} signed up successfully with ID: ${userResponse.id}")
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse)
    }

    @PostMapping("/login")
    @PreAuthorize("permitAll()")
    @Operation(
        summary = "User login",
        description = "Authenticates a user with the provided credentials."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "User logged in successfully"),
            ApiResponse(responseCode = "400", description = "Invalid input"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun login(
        @RequestBody request: LoginUserRequest
    ) : ResponseEntity<UserResponse> {
        logger.info("Received login request for user: ${request.email}")
        val userResponse = authenticationService.loginUser(request)
        logger.info("User ${request.email} logged in successfully with ID: ${userResponse.id}")
        return ResponseEntity.ok(userResponse)
    }

    @GetMapping("/email-verification/{verificationCode}")
    @PreAuthorize("permitAll()")
    @Operation(
        summary = "Verify user",
        description = "Verifies a user with the provided verification code."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "User verified successfully"),
            ApiResponse(responseCode = "400", description = "Invalid verification code"),
            ApiResponse(responseCode = "404", description = "User not found"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun verifyUser(
        @PathVariable verificationCode: String
    ): ResponseEntity<String> {
        logger.info("Received verification request for code: $verificationCode")
        authenticationService.verifyUser(verificationCode)
        logger.info("User verified successfully with code: $verificationCode")
        return ResponseEntity.ok("User verified successfully")
    }
}