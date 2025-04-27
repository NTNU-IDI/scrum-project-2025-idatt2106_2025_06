package edu.ntnu.idatt2106.gr6.backend.DTOs

import java.time.Instant

data class CreateStorageRequest(
    val name: String,
    val token: Int,
    val latitude: Double?,  // optional
    val longitude: Double?  // optional
)

data class StorageResponse(
    val id: String,
    val name: String,
    val storageOwner: String,
    val token: Int,
    val createdAt: Instant,
    val updatedAt: Instant
)
