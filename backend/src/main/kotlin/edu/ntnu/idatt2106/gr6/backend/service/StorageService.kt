package edu.ntnu.idatt2106.gr6.backend.service

import edu.ntnu.idatt2106.gr6.backend.DTOs.StorageDTOs
import edu.ntnu.idatt2106.gr6.backend.DTOs.StorageDTOs.CreateStorageRequest
import edu.ntnu.idatt2106.gr6.backend.DTOs.StorageDTOs.StorageResponse
import edu.ntnu.idatt2106.gr6.backend.DTOs.StorageDTOs.StorageSummary
import edu.ntnu.idatt2106.gr6.backend.DTOs.StorageDTOs.UpdateStorageRequest
import edu.ntnu.idatt2106.gr6.backend.DTOs.UserDTOs.SimpleUserResponse
import edu.ntnu.idatt2106.gr6.backend.model.Location
import edu.ntnu.idatt2106.gr6.backend.model.toResponse
import edu.ntnu.idatt2106.gr6.backend.repository.StorageRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class StorageService(
    private val storageRepository: StorageRepository,
    private val userContextService: UserContextService,
) {

    private val logger = LoggerFactory.getLogger(StorageService::class.java)

    /**
     * Creates a new storage and links it to the current authenticated user.
     *
     * @param request The storage creation request.
     * @return        The created storage response.
     */
    @Transactional
    fun createStorage(request: CreateStorageRequest): StorageResponse {
        logger.info("Starting storage creation for ${request.name}")

        val userId: UUID = userContextService.getCurrentUserId()
        logger.info("Creating storage for user $userId")

        val location = request.location

        val storage = storageRepository.saveStorage(
            name = request.name,
            storageOwner = userId.toString(),
            location = request.location
        )

        return storage.toResponse()
    }

    @Transactional
    fun joinStorageByToken(token: String) {
        val userId = userContextService.getCurrentUserId().toString()

        val storage = storageRepository.findStorageByToken(token)
            ?: throw IllegalArgumentException("Storage with token $token not found.")

        storageRepository.addUserToStorage(userId, storage.id)
    }

    fun getMemberDTOsOfStorage(storageId: String): List<SimpleUserResponse> {
        return storageRepository.findSimpleUsersInStorage(storageId).map {
            SimpleUserResponse(
                id = it.id.toString(),
                name = it.name
            )
        }
    }


    @Transactional
    fun removeUserFromStorage(userId: String, storageId: String): Boolean {
        return storageRepository.removeUserFromStorage(userId, storageId)
    }

    @Transactional
    fun getStoragesByUserId(): List<StorageSummary> {
        val userId = userContextService.getCurrentUserId().toString()
        logger.info("Finding storages by user $userId")

        return storageRepository.findStoragesByUserId(userId)
    }

    /**
     * Optionally updates the name and location of a storage
     *
     * @param request The update request contains the id, new name and location
     * @return `true` if the storage was successfully updated, `false` if not
     */
    @Transactional
    fun updateStorage(request: UpdateStorageRequest): Boolean {
        val userId = userContextService.getCurrentUserId().toString()
        logger.info("User $userId is attempting to update the storage ${request.id}")

        return storageRepository.updateStorageIfOwner(
            storageId = request.id,
            userId = userId,
            newName = request.name,
            newLocation = request.location
        )
    }

    /**
     * Deletes the storage if the current user is the owner of the storage
     *
     * @param storageId The ID of the storage the user wants to delete
     * @return          ´true´ if the storage was deleted, ´false´ if not
     */
    @Transactional
    fun deleteStorage(storageId: String): Boolean {
        val currentUserId = userContextService.getCurrentUserId().toString()
        logger.info("User $currentUserId is attempting to delete storage $storageId")

        val deleted = storageRepository.deleteStorage(storageId, currentUserId)

        if (deleted) {
            logger.info("User $currentUserId deleted storage $storageId, succesfully")
        } else {
            logger.info("User $currentUserId did NOT deleted storage $storageId, succesfully!!!!")
        }

        return deleted
    }
}
