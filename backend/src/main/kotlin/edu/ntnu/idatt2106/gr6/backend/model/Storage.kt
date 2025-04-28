package edu.ntnu.idatt2106.gr6.backend.model

import java.time.Instant

data class Storage(
    val id: String,
    val name: String,
    val storageOwner: String,
    val token: String,
    val latitude: Double?,   // extracted from POINT (longitude, latitude)
    val longitude: Double?,  // extracted from POINT (longitude, latitude)
    val createdAt: Instant,
    val updatedAt: Instant
) {
    init {
        require(id.isNotBlank()) { "Storage id must not be blank." }
        require(name.isNotBlank()) { "Storage name must not be blank." }
        require(storageOwner.isNotBlank()) { "Storage owner id must not be blank." }
        require(token.isNotBlank()) { "Token must not be blank" }
        if (latitude != null) {
            require(latitude in -90.0..90.0) { "Latitude must be between -90 and 90." }
        }
        if (longitude != null) {
            require(longitude in -180.0..180.0) { "Longitude must be between -180 and 180." }
        }
    }
}