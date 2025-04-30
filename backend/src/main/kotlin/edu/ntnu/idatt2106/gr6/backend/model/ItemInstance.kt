package edu.ntnu.idatt2106.gr6.backend.model

import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate

data class ItemInstance(
    val id: String,
    val itemId: String,
    val storageId: String,
    val amount: BigDecimal,
    val expiryDate: LocalDate?,
    val createdAt: Instant,
    val updatedAt: Instant
)
