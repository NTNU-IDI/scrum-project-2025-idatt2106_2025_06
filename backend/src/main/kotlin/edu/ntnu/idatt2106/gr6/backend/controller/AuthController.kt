package edu.ntnu.idatt2106.gr6.backend.controller

import edu.ntnu.idatt2106.gr6.backend.DTOs.UserDTOs.LoginUserRequest
import edu.ntnu.idatt2106.gr6.backend.DTOs.UserDTOs.UserResponse
import edu.ntnu.idatt2106.gr6.backend.DTOs.UserDTOs.CreateUserRequest

import edu.ntnu.idatt2106.gr6.backend.service.AuthenticationService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authenticationService: AuthenticationService
) {
    private val logger = LoggerFactory.getLogger(AuthController::class.java)

    @PostMapping("/signup")
    @PreAuthorize("permitAll()")
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
    fun login(
        @RequestBody request: LoginUserRequest
    ) : ResponseEntity<UserResponse> {
        logger.info("Received login request for user: ${request.email}")
        val userResponse = authenticationService.loginUser(request)
        logger.info("User ${request.email} logged in successfully with ID: ${userResponse.id}")
        return ResponseEntity.ok(userResponse)
    }
}