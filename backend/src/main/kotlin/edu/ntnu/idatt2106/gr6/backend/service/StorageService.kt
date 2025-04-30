package edu.ntnu.idatt2106.gr6.backend.service

import edu.ntnu.idatt2106.gr6.backend.DTOs.CreateStorageRequest
import edu.ntnu.idatt2106.gr6.backend.DTOs.StorageResponse
import edu.ntnu.idatt2106.gr6.backend.DTOs.StorageSummary
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

        // Save the storage entity
        val storage = storageRepository.saveStorage(
            name = request.name,
            storageOwner = userId.toString(),
            latitude = request.latitude,
            longitude = request.longitude
        )

        // Map model to DTO
        return storage.toResponse()
    }

    /**
     * Finds a storage by its ID.
     *
     * @param id The ID of the storage.
     * @return   The storage response, or null if not found.
     */
    @Transactional(readOnly = true)
    fun findStorageById(token: String): StorageResponse? {
        logger.info("Finding storage with id $token")

        return storageRepository.findStorageByToken(token)
            ?.toResponse()
    }

    @Transactional
    fun joinStorageByToken(token: String) {
        val userId = userContextService.getCurrentUserId().toString()

        val storage = storageRepository.findStorageByToken(token)
            ?: throw IllegalArgumentException("Storage with token $token not found.")

        storageRepository.addUserToStorage(userId, storage.id)
    }

    @Transactional(readOnly = true)
    fun getMemberNamesOfStorage(storageId: String): List<String> {
        return storageRepository.findUserNamesInStorage(storageId)
    }

    @Transactional
    fun removeUserFromStorage(userId: String, storageId: String): Boolean {
        return storageRepository.removeUserFromStorage(userId, storageId)
    }

    @Transactional
    fun getStoragesByUserId(userId: String): List<StorageSummary> {
        return storageRepository.findStoragesByUserId(userId)
    }
}
