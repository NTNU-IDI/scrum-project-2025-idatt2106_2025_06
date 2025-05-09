package edu.ntnu.idatt2106.gr6.backend.service

import edu.ntnu.idatt2106.gr6.backend.DTOs.AssignCheckpointRequest
import edu.ntnu.idatt2106.gr6.backend.DTOs.CheckpointProgressResponse
import edu.ntnu.idatt2106.gr6.backend.DTOs.CheckpointResponse
import edu.ntnu.idatt2106.gr6.backend.repository.CheckpointRepository
import edu.ntnu.idatt2106.gr6.backend.util.IdGenerator
import org.springframework.stereotype.Service

/**
 * Service class responsible for checkpoint-related operations
 *
 * @property checkpointRepository The repository for performing checkpoint db operations
 * @property userContextService Responsible for retrieving the ID of the user doing the operation
 * @property idGenerator Used to generate an ID when a checkpoint is created
 */
@Service
class CheckpointService(
    private val checkpointRepository: CheckpointRepository,
    private val userContextService: UserContextService,
    private val idGenerator: IdGenerator
    ) {

    /**
     * Retrieves all available checkpoints
     *
     * @return [CheckpointResponse] a list of checkpoints
     */
    fun getAll(): List<CheckpointResponse> {
        return checkpointRepository.getAllCheckpoints().map {
            CheckpointResponse(it.id, it.name, it.description)
        }
    }

    /**
     * Retrieves all checkpoints linked with the current user
     *
     * @return CheckpointProgressResponse A list with linked checkpoints and its percentage of all checkpoints
     */
    fun getCheckpointsWithProgressForCurrentUser(): CheckpointProgressResponse {
        val userId = userContextService.getCurrentUserId().toString()

        val checkpoints = checkpointRepository.getCheckpointsByUserId(userId)
        val completion = checkpointRepository.getUserCheckpointCompletionPercentage(userId)

        return CheckpointProgressResponse(
            checkpoints = checkpoints.map { CheckpointResponse(it.id, it.name, it.description) },
            completionPercentage = completion
        )
    }

    /**
     * A list of checkpoints is linked with the current user
     *
     * @param request The list with checkpoints the current user is to be assigned
     * @return True if the operation was successful
     */
    fun assignCheckpointToCurrentUser(request: AssignCheckpointRequest): Boolean {
        val userId = userContextService.getCurrentUserId().toString()

        val entries = request.checkpointIds.map { checkpointId ->
            idGenerator.generateId(12) to checkpointId
        }

        return checkpointRepository.checkpointsForUser(userId, entries)
    }
}