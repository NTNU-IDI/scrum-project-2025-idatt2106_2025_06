package edu.ntnu.idatt2106.gr6.backend.controller

import edu.ntnu.idatt2106.gr6.backend.DTOs.CreateStorageRequest
import edu.ntnu.idatt2106.gr6.backend.DTOs.StorageResponse
import edu.ntnu.idatt2106.gr6.backend.service.StorageService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/storages")
@Tag(name = "Storage", description = "Storage management APIs")

class StorageController(
    private val storageService: StorageService
) {

    private val logger = LoggerFactory.getLogger(StorageController::class.java)

    /**
     * Creates a new storage and links it to the current user.
     *
     * @param request The storage creation request payload.
     * @return        ResponseEntity containing the created storage and HTTP 201 status.
     */
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('CREATE_STORAGE')")
    @Operation(
        summary = "Create new storage",
        description = "Creates a new storage and links it to the current authenticated user"
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Storage created"),
            ApiResponse(responseCode = "400", description = "Invalid input or token already in use"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "500", description = "Internal server error"),
        ],
    )
    fun createStorage(
        @RequestBody @Valid
        @Parameter(description = "Storage creation data", required = true)
        request: CreateStorageRequest
    ): ResponseEntity<StorageResponse> {
        logger.info("==== ENTERED CONTROLLER ====")
        logger.info("Received storage create request: $request")

        val response = storageService.createStorage(request)

        logger.info("Storage created with ID: ${response.id}")
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @GetMapping("/test")
    @Operation(
        summary = "Test endpoint",
        description = "test"
    )
    fun test(): ResponseEntity<String> {
        logger.info("==== ENTERED CONTROLLER ====")
        return ResponseEntity.ok("Hallo")
    }

}
