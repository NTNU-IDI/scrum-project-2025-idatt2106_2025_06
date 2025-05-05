package edu.ntnu.idatt2106.gr6.backend.model

import edu.ntnu.idatt2106.gr6.backend.DTOs.StorageDTOs.StorageResponse

fun Storage.toResponse(): StorageResponse {
    return StorageResponse(
        id = this.id,
        name = this.name,
        storageOwner = this.storageOwner,
        token = this.token,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}