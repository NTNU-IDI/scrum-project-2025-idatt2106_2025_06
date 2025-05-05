package edu.ntnu.idatt2106.gr6.backend.service

import edu.ntnu.idatt2106.gr6.backend.model.User
import edu.ntnu.idatt2106.gr6.backend.repository.RoleRepository
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
// import org.springframework.security.core.userdetails.UserDetailsService
// import org.springframework.security.core.userdetails.UsernameNotFoundException
import java.util.*
import javax.crypto.SecretKey

@Service
class JwtService(private val roleRepository: RoleRepository) {
    private val logger = org.slf4j.LoggerFactory.getLogger(JwtService::class.java)
    @Value("\${security.jwt.secret-key}")
    private val secret: String? = null

    @Value("\${security.jwt.expiration-time}")
    private var expiration: Long = 0

    //generate
    fun generateToken(user: User): String {
        val now = System.currentTimeMillis()
        val expirationTime = now + expiration

        val userId = user.id
        val emailClaim = user.email
        val roleClaim=  user.authorities.map { it.authority }


        val authoritiesClaim = roleRepository
            .findPermissionsByRole(user.role.id)
            .map { it.name }

        return Jwts
            .builder()
            .subject(userId.toString())
            .claim("email", emailClaim)
            .claim("role", roleClaim)
            .claim("authorities", authoritiesClaim)
            .issuedAt(Date(now))
            .expiration(Date(expirationTime))
            .signWith(getSignInKey())
            .compact()
    }

    fun extractUserIdFromToken(token: String): UUID? {
        val claims = getAllClaimsFromToken(token)
        return try {
            claims.subject?.let { UUID.fromString(it) }
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    fun extractEmailFromToken(token: String): String? {
        val claims = getAllClaimsFromToken(token)
        return claims["email"] as? String
    }

    fun isTokenValid(
        token: String,
        userDetails: UserDetails
    ): Boolean {
        val extractedUserId: UUID? = extractUserIdFromToken(token)
        val userIdFromUserDetails = if (userDetails is User) userDetails.id else null
        return extractedUserId != null && extractedUserId == userIdFromUserDetails && !isTokenExpired(token)
    }

    fun getAuthoritiesFromToken(token: String): Collection<GrantedAuthority> {
        val claims = getAllClaimsFromToken(token)
        val roles = claims["role"] as? String
        val permissions = claims["permissions"] as? List<String>

        val authorities = mutableListOf<GrantedAuthority>()
        authorities.addAll(
            roles?.split(",")?.map { it.trim() }?.map { SimpleGrantedAuthority(it) } ?: emptyList()
        )
        authorities.addAll(
            permissions?.map { SimpleGrantedAuthority(it) } ?: emptyList()
        )
        return authorities
    }

    fun getExpirationTime(): Long {
        return expiration
    }

    fun isTokenExpired(token: String): Boolean {
        val claims = getAllClaimsFromToken(token)
        return claims.expiration.before(Date())
    }

    private fun getAllClaimsFromToken(token: String): Claims =
        Jwts
            .parser()
            .verifyWith(getSignInKey())
            .build()
            .parseSignedClaims(token)
            .payload

    private fun getSignInKey(): SecretKey {
        val keyBytes = Decoders.BASE64.decode(secret)
        return Keys.hmacShaKeyFor(keyBytes)
    }
}