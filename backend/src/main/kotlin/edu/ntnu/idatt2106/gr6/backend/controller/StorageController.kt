package edu.ntnu.idatt2106.gr6.backend.controller

import edu.ntnu.idatt2106.gr6.backend.DTOs.CreateStorageRequest
import edu.ntnu.idatt2106.gr6.backend.DTOs.JoinStorageRequest
import edu.ntnu.idatt2106.gr6.backend.DTOs.RemoveUserFromStorageRequest
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

    /**
     * Finds a storage by its token.
     *
     * @param token The token of the storage to retrieve.
     * @return   ResponseEntity containing the storage if found, or 404 if not found.
     */
    @GetMapping("/{token}")
    @PreAuthorize("hasAuthority('CREATE_STORAGE')") // !!!Make a uniqe auth for this action, now it is set to 'CREATE_STORAGE'!!!!
    @Operation(
        summary = "Find storage by token",
        description = "Finds a storage linked to the authenticated user by its ID"
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Storage found"),
            ApiResponse(responseCode = "404", description = "Storage not found"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "500", description = "Internal server error"),
        ],
    )
    fun findStorageByToken(
        @PathVariable
        @Parameter(description = "ID of the storage to retrieve", required = true)
        token: String
    ): ResponseEntity<StorageResponse> {
        logger.info("Fetching storage with ID: $token")

        val storage = storageService.findStorageById(token)
        return if (storage != null) {
            ResponseEntity.ok(storage)
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    @PostMapping("/join")
    @PreAuthorize("hasAuthority('CREATE_STORAGE')") // !!!Make a uniqe auth for this action, now it is set to 'CREATE_STORAGE'!!!!
    @Operation(
        summary = "Join a storage",
        description = "Join a storage by sending a token in the JSON body"
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully joined storage"),
            ApiResponse(responseCode = "404", description = "Storage not found"),
            ApiResponse(responseCode = "400", description = "Bad request"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "500", description = "Internal server error"),
        ],
    )
    fun joinStorageByToken(
        @RequestBody @Valid
        @Parameter(description = "Request containing storage token", required = true)
        request: JoinStorageRequest
    ): ResponseEntity<Void> {
        logger.info("User attempting to join storage with token: ${request.token}")

        return try {
            storageService.joinStorageByToken(request.token)
            ResponseEntity.ok().build()
        } catch (e: IllegalArgumentException) {
            logger.warn("Join failed: ${e.message}")
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }


    @GetMapping("/{id}/members")
    @PreAuthorize("hasAuthority('CREATE_STORAGE')") // !!!Make a uniqe auth for this action, now it is set to 'CREATE_STORAGE'!!!!
    @Operation(
        summary = "Get member names of a storage",
        description = "Retrieves all user names who are members of the given storage"
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Members retrieved successfully"),
            ApiResponse(responseCode = "404", description = "Storage not found"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "500", description = "Internal server error"),
        ],
    )
    fun getMemberNamesOfStorage(
        @PathVariable
        @Parameter(description = "ID of the storage", required = true)
        id: String
    ): ResponseEntity<List<String>> {
        logger.info("Fetching member names for storage with ID: $id")

        val memberNames = storageService.getMemberNamesOfStorage(id)

        return ResponseEntity.ok(memberNames)
    }

    @PostMapping("/remove-member")
    @PreAuthorize("hasAuthority('CREATE_STORAGE')") // !!!Make a uniqe auth for this action, now it is set to 'CREATE_STORAGE'!!!!
    @Operation(
        summary = "Remove user from storage",
        description = "Removes a user from a storage using a JSON body with storageId and userId"
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "User removed successfully"),
            ApiResponse(responseCode = "404", description = "User or storage not found"),
            ApiResponse(responseCode = "400", description = "Invalid input"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "500", description = "Internal server error"),
        ],
    )
    fun removeUserFromStorage(
        @RequestBody @Valid
        @Parameter(description = "Request containing storageId and userId", required = true)
        request: RemoveUserFromStorageRequest
    ): ResponseEntity<Void> {
        logger.info("Attempting to remove user ${request.userId} from storage ${request.storageId}")

        val success = storageService.removeUserFromStorage(request.userId, request.storageId)

        return if (success) {
            ResponseEntity.ok().build()
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }
}
