package edu.ntnu.idatt2106.gr6.backend.service

import edu.ntnu.idatt2106.gr6.backend.DTOs.AssignCheckpointRequest
import edu.ntnu.idatt2106.gr6.backend.DTOs.CheckpointResponse
import edu.ntnu.idatt2106.gr6.backend.repository.CheckpointRepository
import edu.ntnu.idatt2106.gr6.backend.util.IdGenerator
import org.springframework.stereotype.Service

@Service
class CheckpointService(
    private val checkpointRepository: CheckpointRepository,
    private val userContextService: UserContextService,
    private val idGenerator: IdGenerator
    ) {

    fun getAll(): List<CheckpointResponse> {
        return checkpointRepository.getAllCheckpoints().map {
            CheckpointResponse(it.id, it.name, it.description)
        }
    }


    fun getCheckpointsForCurrentUser(): List<CheckpointResponse> {
        val userId = userContextService.getCurrentUserId().toString()
        val checkpoints = checkpointRepository.getCheckpointsByUserId(userId)
        return checkpoints.map { CheckpointResponse(it.id, it.name, it.description) }
    }

    fun assignCheckpointToCurrentUser(request: AssignCheckpointRequest): Boolean {
        val userId = userContextService.getCurrentUserId().toString()

        val entries = request.checkpointIds.map { checkpointId ->
            idGenerator.generateId(12) to checkpointId
        }

        return checkpointRepository.replaceCheckpointsForUser(userId, entries)
    }


}