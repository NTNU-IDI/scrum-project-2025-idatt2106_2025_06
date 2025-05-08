package edu.ntnu.idatt2106.gr6.backend.model

import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin

/**
 * Data class representing a geographical location with latitude and longitude.
 *
 * @property latitude The latitude of the location, must be between -90.0 and 90.0.
 * @property longitude The longitude of the location, must be between -180.0 and 180.0.
 */
data class Location(
    @field:DecimalMin("-90.0") @field:DecimalMax("90.0")
    val latitude: Double,
    @field:DecimalMin("-180.0") @field:DecimalMax("180.0")
    val longitude: Double,
)