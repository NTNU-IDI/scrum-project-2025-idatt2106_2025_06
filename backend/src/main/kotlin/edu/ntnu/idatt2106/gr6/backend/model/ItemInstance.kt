package edu.ntnu.idatt2106.gr6.backend.model

import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate

/**
 * Data class representing an instance of an item in the system. This class allows
 * users to store data about items that may vary from between each instance of an item
 * . It contains information such as expiration date and amount.
 */
data class ItemInstance(
    val id: String,
    val itemId: String,
    val storageId: String,
    val amount: BigDecimal,
    val expiryDate: LocalDate?,
    val createdAt: Instant,
    val updatedAt: Instant
)
