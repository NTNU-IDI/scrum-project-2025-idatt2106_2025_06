package edu.ntnu.idatt2106.gr6.backend.model

import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin

data class Location(
    @field:DecimalMin("-90.0") @field:DecimalMax("90.0")
    val latitude: Double,
    @field:DecimalMin("-180.0") @field:DecimalMax("180.0")
    val longitude: Double,
)