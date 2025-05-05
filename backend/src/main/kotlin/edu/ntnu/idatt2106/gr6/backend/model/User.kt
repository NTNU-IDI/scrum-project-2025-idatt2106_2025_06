package edu.ntnu.idatt2106.gr6.backend.model

import jakarta.persistence.ManyToOne
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.util.UUID
import org.springframework.security.core.userdetails.UserDetails
import java.time.Instant

data class User(
    var id: UUID? = UUID.fromString("00000000-0000-0000-0000-000000000000"),
    val name: String,
    val email: String,
    private val passwordHashed: String,
    val createdAt: Instant = Instant.now(),
    val verified: Boolean,
    @ManyToOne
    val role: Role
) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        val authorities = mutableListOf<GrantedAuthority>()
        authorities.add(SimpleGrantedAuthority(role.name))
        authorities.addAll(role.permissions.map { permission ->
            SimpleGrantedAuthority(permission.name)
        })
        return authorities
    }

    override fun getPassword(): String = passwordHashed

    override fun getUsername(): String = email

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}

data class SimpleUser (
    val id: UUID?,
    val name: String,
)