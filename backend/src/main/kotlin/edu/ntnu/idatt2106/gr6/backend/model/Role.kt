package edu.ntnu.idatt2106.gr6.backend.model

import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany

data class Role(
    val id: Int = -1,
    val name: String,

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "role_permissions",
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