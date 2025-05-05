package edu.ntnu.idatt2106.gr6.backend.DTOs

import java.time.Instant

data class CreateScenarioRequest(
    val title: String,
    val description: String,
    val content: String,
    val url: String
)

data class ScenarioResponse(
    val id: String,
    val title: String,
    val description: String,
    val content: String,
    val url: String,
    val updatedAt: Instant
)

data class UpdateScenarioRequest(
    val id: String,
    val title: String?,
    val description: String?,
    val content: String?,
    val url: String?
)