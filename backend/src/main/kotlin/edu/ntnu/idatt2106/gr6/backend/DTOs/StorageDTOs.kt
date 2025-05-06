package edu.ntnu.idatt2106.gr6.backend.DTOs

import edu.ntnu.idatt2106.gr6.backend.model.Location
import java.time.Instant

class StorageDTOs {
    data class CreateStorageRequest(
        val name: String,
        val location: Location?,
    )

    data class StorageResponse(
        val id: String,
        val name: String,
        val storageOwner: String,
        val token: String,
        val location: Location?,
        val createdAt: Instant,
        val updatedAt: Instant
    )

    data class RemoveUserFromStorageRequest(
        val storageId: String,
        val userId: String
    )

    data class JoinStorageRequest(
        val token: String
    )

    data class StorageSummary(
        val id: String,
        val name: String,
        val token: String,
        val location: Location?
    )
}