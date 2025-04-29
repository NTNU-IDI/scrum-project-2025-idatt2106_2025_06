package edu.ntnu.idatt2106.gr6.backend.model

import java.time.Instant

data class Item(
    val id: String,
    val name: String,
    val typeId: Int,
    val unitId: Int,
    val createdAt: Instant,
    val updatedAt: Instant
)