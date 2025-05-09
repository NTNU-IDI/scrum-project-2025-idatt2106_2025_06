package edu.ntnu.idatt2106.gr6.backend.controller

import edu.ntnu.idatt2106.gr6.backend.DTOs.AssignCheckpointRequest
import edu.ntnu.idatt2106.gr6.backend.DTOs.CheckpointProgressResponse
import edu.ntnu.idatt2106.gr6.backend.DTOs.CheckpointResponse
import edu.ntnu.idatt2106.gr6.backend.service.CheckpointService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

/**
 * REST controller for managing checkpoint-related endpoints
 * Provide access to retrieving all checkpoints, user-specific progress,
 * and linking new checkpoints the user
 */
@RestController
@RequestMapping("/api/checkpoints")
class CheckpointController(private val checkpointService: CheckpointService) {

    /**
     * Retrieves all checkpoints
     *
     * @return [CheckpointResponse] A list with all checkpoints
     */
    @GetMapping
    @PreAuthorize("hasAuthority('CREATE_STORAGE')") // Optional permission
    @Operation(summary = "Get all checkpoints", description = "Returns a list of all available checkpoints.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully retrieved all checkpoints"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun getAllCheckpoints(): ResponseEntity<List<CheckpointResponse>> {
        val checkpoints = checkpointService.getAll()
        return ResponseEntity.ok(checkpoints)
    }

    /**
     * Retrieves all checkpoints linked with to a specific user
     *
     * @return [CheckpointProgressResponse] Containing a list with linked checkpoints,
     *          and the percentage of the total cheks lists
     */
    @GetMapping("/my-checkpoints")
    @PreAuthorize("hasAuthority('CREATE_STORAGE')") // Optional permission
    @Operation(
        summary = "Get user's checkpoints with progress",
        description = "Returns all checkpoints assigned to the currently authenticated user along with completion percentage."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully retrieved user checkpoints and progress"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun getMyCheckpointsWithProgress(): ResponseEntity<CheckpointProgressResponse> {
        val result = checkpointService.getCheckpointsWithProgressForCurrentUser()
        return ResponseEntity.ok(result)
    }

    /**
     * Assigns a list of checkpoints to a user
     *
     * @param request A JSON body with the ID of the checkpoint that is to be linked
     * @return 200 OK if assignment succeeded, 500 if not
     */
    @PostMapping("/assign")
    @PreAuthorize("hasAuthority('CREATE_STORAGE')") // Optional permission check
    @Operation(
        summary = "Assign checkpoints to user",
        description = "Assigns a new list of checkpoints to the currently authenticated user. Previous assignments will be removed."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully assigned checkpoints"),
            ApiResponse(responseCode = "500", description = "Failed to assign checkpoints"),
            ApiResponse(responseCode = "401", description = "Unauthorized")
        ]
    )
    fun assignCheckpointToUser(
        @RequestBody request: AssignCheckpointRequest
    ): ResponseEntity<Void> {
        return if (checkpointService.assignCheckpointToCurrentUser(request)) {
            ResponseEntity.ok().build()
        } else {
            ResponseEntity.status(500).build()
        }
    }
}
