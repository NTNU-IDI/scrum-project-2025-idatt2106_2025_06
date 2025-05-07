package edu.ntnu.idatt2106.gr6.backend.controller

import edu.ntnu.idatt2106.gr6.backend.DTOs.UserDTOs.EditUserNameEmailRequest
import edu.ntnu.idatt2106.gr6.backend.DTOs.UserDTOs.EditUserNameEmailResponse
import edu.ntnu.idatt2106.gr6.backend.DTOs.UserDTOs.ChangePasswordRequest
import edu.ntnu.idatt2106.gr6.backend.DTOs.UserDTOs.ChangeUserTrackingPreferenceRequest
import edu.ntnu.idatt2106.gr6.backend.service.UserService
import edu.ntnu.idatt2106.gr6.backend.service.EmailService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.DeleteMapping

@RestController
@RequestMapping("/api/user")
class UserController(
    private val userService: UserService,
    private val emailService: EmailService
) {
    private val logger = org.slf4j.LoggerFactory.getLogger(UserController::class.java)

    @PutMapping("/edit/name-email")
    @PreAuthorize("hasAuthority('CREATE_STORAGE')") // !!!!!!!!
    @Operation(summary = "Edit current user's name and email")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "User updated"),
            ApiResponse(responseCode = "404", description = "User not found"),
            ApiResponse(responseCode = "409", description = "Email already in use")
        ]
    )
    fun editUser(@RequestBody request: EditUserNameEmailRequest): ResponseEntity<EditUserNameEmailResponse> {
        val updatedUser = userService.updateUserDetails(request)
        return ResponseEntity.ok(updatedUser)
    }

    @PutMapping("/edit/password")
    @PreAuthorize("hasAuthority('CREATE_STORAGE')") // Adjust as needed
    @Operation(summary = "Change current user's password")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Password changed successfully"),
            ApiResponse(responseCode = "400", description = "Current password is incorrect"),
            ApiResponse(responseCode = "404", description = "User not found")
        ]
    )
    fun changePassword(@RequestBody request: ChangePasswordRequest): ResponseEntity<Void> {
        userService.changePassword(request)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/privacy-policy/change-location-tracking")
    @PreAuthorize("hasAuthority('CREATE_STORAGE')") // Adjust as needed
    @Operation(summary = "Set location tracking preference")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Location tracking preference set successfully"),
            ApiResponse(responseCode = "400", description = "Invalid input"),
            ApiResponse(responseCode = "404", description = "User not found")
        ]
    )
    fun setLocationTrackingPreference(@RequestBody request: ChangeUserTrackingPreferenceRequest): ResponseEntity<Void> {
        logger.info("Received request to set location tracking preference: $request")
        userService.updateUserTrackingPreferences(request)
        logger.info("Location tracking preference set successfully")
        return ResponseEntity.ok().build()
    }

    @PostMapping("/privacy-policy/delete-location-history")
    @PreAuthorize("hasAuthority('CREATE_STORAGE')") // Adjust as needed
    @Operation(summary = "Delete location history")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Location history deleted successfully"),
            ApiResponse(responseCode = "404", description = "User not found")
        ]
    )
    fun deleteLocationHistory(): ResponseEntity<Void> {
        logger.info("Received request to delete location history")
        userService.deleteUserTrackingHistory()
        logger.info("Location history deleted successfully")
        return ResponseEntity.ok().build()
    }

    @GetMapping("/send-email")
    fun testEmail(): ResponseEntity<String> {
        try {
            logger.info("Received request to send verification email")
            val response = emailService.sendVerificationEmail()
            return ResponseEntity.ok("Email sent successfully: $response")
        } catch (e: Exception) {
            return ResponseEntity.internalServerError().body("Failed to send email: ${e.message}")
        }
    }
}
