package edu.ntnu.idatt2106.gr6.backend.DTOs

import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

data class CreateItemInstanceRequest (
    val name: String,
    val description: String?,
    val typeId: Int,
    val unitId: Int,
    val storageId: String,
    val amount: BigDecimal,
    val expiryDate: LocalDate?
)

data class StorageItemResponse(
    val name: String,
    val amount: BigDecimal,
    val unit: String,
    val expiryDate: LocalDate?
)