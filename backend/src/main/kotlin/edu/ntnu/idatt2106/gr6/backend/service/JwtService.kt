package edu.ntnu.idatt2106.gr6.backend.service

import edu.ntnu.idatt2106.gr6.backend.model.User
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import java.util.*
import javax.crypto.SecretKey

@Service
class JwtService {
    @Value("\${security.jwt.secret-key}")
    private val secret: String? = null

    @Value("\${security.jwt.expiration-time}")
    private var expiration: Long = 0

    fun generateToken(userDetails: UserDetails): String {
        val now = System.currentTimeMillis()
        val expirationTime = now + expiration

        val userId =
            if (userDetails is User) {
                userDetails.id
            } else {
                throw IllegalArgumentException(
                    "UserDetails must be an instance of custom User class",
                )
            }
        val email = userDetails.username

        val emailClaim: Map<String, Any> = mapOf("email" to email)
        val roleClaim=  userDetails.authorities.map { it.authority }
        val permissionsClaim = userDetails.authorities.map { it.authority }

        return Jwts
            .builder()
            .subject(userId.toString())
            .claim("email", emailClaim)
            .claim("role", roleClaim)
            .claim("permissions", permissionsClaim)
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
        authorities.addAll(roles.map )
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