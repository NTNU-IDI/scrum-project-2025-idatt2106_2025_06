package edu.ntnu.idatt2106.gr6.backend.controller

import edu.ntnu.idatt2106.gr6.backend.DTOs.StorageDTOs
import edu.ntnu.idatt2106.gr6.backend.DTOs.StorageDTOs.CreateStorageRequest
import edu.ntnu.idatt2106.gr6.backend.DTOs.StorageDTOs.JoinStorageRequest
import edu.ntnu.idatt2106.gr6.backend.DTOs.StorageDTOs.RemoveUserFromStorageRequest
import edu.ntnu.idatt2106.gr6.backend.DTOs.StorageDTOs.StorageResponse
import edu.ntnu.idatt2106.gr6.backend.DTOs.StorageDTOs.StorageSummary
import edu.ntnu.idatt2106.gr6.backend.DTOs.StorageDTOs.UpdateStorageRequest
import edu.ntnu.idatt2106.gr6.backend.DTOs.UserDTOs.SimpleUserResponse
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

    @PostMapping("/join")
    @PreAuthorize("hasAuthority('JOIN_STORAGE')")
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
    @PreAuthorize("hasAuthority('GET_STORAGE_MEMBERS')")
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
    fun getStorageMembers(
        @PathVariable
        @Parameter(description = "ID of the storage", required = true)
        id: String
    ): ResponseEntity<List<SimpleUserResponse>> {
        logger.info("Fetching member IDs and names for storage with ID: $id")

        val members = storageService.getMemberDTOsOfStorage(id)

        return ResponseEntity.ok(members)
    }

    @PostMapping("/remove-member")
    @PreAuthorize("hasAuthority('REMOVE_STORAGE_MEMBERS')")
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

    @GetMapping("/my-storages")
    @PreAuthorize("hasAuthority('GET_STORAGE')")
    @Operation(summary = "Get all storages a user is connected to")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully fetched storages"),
            ApiResponse(responseCode = "404", description = "User not found or has no storages"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun getUserStorages(
    ): ResponseEntity<List<StorageSummary>> {
        val storages = storageService.getStoragesByUserId()
        return ResponseEntity.ok(storages)
    }

    @PutMapping("/update")
    @PreAuthorize("hasAuthority('UPDATE_STORAGE')")
    @Operation(
        summary = "Update storage name and location",
        description = "Updates the name and optionally the location of a storage. Only the owner of the storage can perform this operation."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Storage updated successfully"),
            ApiResponse(responseCode = "403", description = "User is not the owner of the storage"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "400", description = "Invalid input"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun updateStorage(
        @RequestBody @Valid
        @Parameter (description = "Request containing storage name and location", required = true)
        request: UpdateStorageRequest
    ): ResponseEntity<Void> {
        logger.info("Recived an update request for storage $request.id")

        val success = storageService.updateStorage(request)

        return if (success) {
            logger.info("Successfullt updated storage ${request.id} with name ${request.name} and location ${request.location}")
            ResponseEntity.ok().build()
        } else {
            logger.info("The update for storage ${request.id} was unsuccessful")
            ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE_STORAGE')")
    @Operation(
        summary = "Delete a storage by ID",
        description = "Deletes the specified storage only if the user is the storage owner"
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Storage successfully deleted"),
            ApiResponse(responseCode = "403", description = "User is not the storage owner"),
            ApiResponse(responseCode = "404", description = "Storage not found"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "500", description = "Internal server error"),
        ],
    )
    fun deleteStorage(
        @PathVariable
        @Parameter(description = "ID of the storage", required = true)
        id: String
    ): ResponseEntity<Void> {
        logger.info("Attempting to delete storage with ID: $id")

        val success = storageService.deleteStorage(id)

        return if (success) {
            logger.info("Successfully deleted storage with ID: $id")
            ResponseEntity.ok().build()
        } else {
            logger.warn("Storage $id could NOT be deleted!")
            ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        }
    }
}
