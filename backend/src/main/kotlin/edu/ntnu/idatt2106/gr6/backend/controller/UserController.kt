package edu.ntnu.idatt2106.gr6.backend.controller

import edu.ntnu.idatt2106.gr6.backend.DTOs.UserDTOs.EditUserNameEmailRequest
import edu.ntnu.idatt2106.gr6.backend.DTOs.UserDTOs.EditUserNameEmailResponse
import edu.ntnu.idatt2106.gr6.backend.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user")
class UserController(
    private val userService: UserService
) {

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
}
