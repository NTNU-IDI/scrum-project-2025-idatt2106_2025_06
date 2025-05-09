package edu.ntnu.idatt2106.gr6.backend.controller

import edu.ntnu.idatt2106.gr6.backend.DTOs.AssignCheckpointRequest
import edu.ntnu.idatt2106.gr6.backend.DTOs.CheckpointResponse
import edu.ntnu.idatt2106.gr6.backend.service.CheckpointService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/checkpoints")
class CheckpointController(private val checkpointService: CheckpointService) {

    @GetMapping
    @PreAuthorize("hasAuthority('CREATE_STORAGE')") // Optional permission
    fun getAllCheckpoints(): ResponseEntity<List<CheckpointResponse>> {
        val checkpoints = checkpointService.getAll()
        return ResponseEntity.ok(checkpoints)
    }

    @GetMapping("/my-checkpoints")
    @PreAuthorize("hasAuthority('CREATE_STORAGE')") // Optional permission
    fun getMyCheckpoints(): ResponseEntity<List<CheckpointResponse>> {
        val checkpoints = checkpointService.getCheckpointsForCurrentUser()
        return ResponseEntity.ok(checkpoints)
    }

    @PostMapping("/assign")
    @PreAuthorize("hasAuthority('CREATE_STORAGE')") // Optional permission check
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
