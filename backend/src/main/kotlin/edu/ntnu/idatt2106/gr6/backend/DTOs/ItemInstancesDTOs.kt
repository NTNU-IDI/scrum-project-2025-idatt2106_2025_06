package edu.ntnu.idatt2106.gr6.backend.DTOs

import java.time.Instant
import java.time.LocalDate

data class ItemInstancesResponse(
    val id: String,
    val itemId: String,
    val storageId: String,
    val expiryDate: LocalDate?,
    val amount: Double,
    val createdAt: Instant,
    val updatedAt: Instant
)