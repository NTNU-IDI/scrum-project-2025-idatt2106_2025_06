package edu.ntnu.idatt2106.gr6.backend.model

import java.time.Instant


data class Scenario(
    val id: String,
    val title: String,
    val description: String,
    val content: String,
    val url: String? = null,
    val createdAt: Instant,
    val updatedAt: Instant,
) {
    init {
        require(id.isNotBlank()) { "ID cannot be blank" }
        require(title.isNotBlank()) { "Name cannot be blank" }
        require(description.isNotBlank()) { "Description cannot be blank" }
    }
}