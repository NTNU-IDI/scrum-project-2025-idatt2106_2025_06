package edu.ntnu.idatt2106.gr6.backend.service

import edu.ntnu.idatt2106.gr6.backend.DTOs.CreateStorageRequest
import edu.ntnu.idatt2106.gr6.backend.DTOs.StorageResponse
import edu.ntnu.idatt2106.gr6.backend.repository.StorageRepository
import org.springframework.stereotype.Service

@Service
class StorageService(
    private val storageRepository: StorageRepository
) {
    fun createStorage(request: CreateStorageRequest): StorageResponse {
        return storageRepository.saveStorage(
            name = request.name,
            token = request.token,
            latitude = request.latitude,
            longitude = request.longitude
        )
    }
}
