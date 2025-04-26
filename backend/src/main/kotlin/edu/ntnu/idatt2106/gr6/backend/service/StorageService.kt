package edu.ntnu.idatt2106.gr6.backend.service

import edu.ntnu.idatt2106.gr6.backend.DTOs.CreateStorageRequest
import edu.ntnu.idatt2106.gr6.backend.DTOs.StorageResponse
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

        // Save the storage itself
        val storage = storageRepository.saveStorage(
            name = request.name,
            storageOwner = userId.toString(),
            token = request.token,
            latitude = request.latitude,
            longitude = request.longitude
        )
        return storage
    }
}
