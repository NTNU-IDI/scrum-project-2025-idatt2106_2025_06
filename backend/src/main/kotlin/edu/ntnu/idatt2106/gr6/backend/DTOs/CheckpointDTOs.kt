package edu.ntnu.idatt2106.gr6.backend.DTOs

data class CheckpointResponse(
    val id: String,
    val name: String,
    val description: String?
)

data class AssignCheckpointRequest(
    val checkpointIds: List<String>
)

data class CheckpointProgressResponse(
    val checkpoints: List<CheckpointResponse>,
    val completionPercentage: Double
)
