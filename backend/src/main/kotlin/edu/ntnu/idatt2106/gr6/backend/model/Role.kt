package edu.ntnu.idatt2106.gr6.backend.model

import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany

data class Role(
    val id: Int = -1,
    val name: String,
    val description: String,

    @ManyToMany
    @JoinTable(
        name = "user_roles",
        joinColumns = [JoinColumn(name = "role_id")],
        inverseJoinColumns = [JoinColumn(name = "permission_id")]
    )
    val permissions: Set<Permission> = emptySet()
) {
    companion object {
        const val ROLE_USER = "ROLE_USER"
        const val ROLE_MODERATOR = "ROLE_MODERATOR"
        const val ROLE_ADMIN = "ROLE_ADMIN"
    }
}