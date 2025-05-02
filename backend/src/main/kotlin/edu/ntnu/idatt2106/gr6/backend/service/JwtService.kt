package edu.ntnu.idatt2106.gr6.backend.service

import edu.ntnu.idatt2106.gr6.backend.model.User
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import java.util.*
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec
import io.jsonwebtoken.SignatureAlgorithm

@Service
class JwtService {
    private val logger = org.slf4j.LoggerFactory.getLogger(JwtService::class.java)
    @Value("\${security.jwt.secret-key}")
    private lateinit var secret: String


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
        val emailClaim = userDetails.username

        val roleClaim = userDetails.authorities
            .filter { it.authority.startsWith("ROLE_") }
            .map { it.authority }
        val permissionsClaim = userDetails.authorities
            .filter { !it.authority.startsWith("ROLE_") }
            .map { it.authority }

        logger.info("Generating JWT token for user ID ${userDetails.authorities}")

        return Jwts
            .builder()
            .subject(userId.toString())
            .claim("email", emailClaim)
            .claim("role", roleClaim)
            .claim("permissions", permissionsClaim)
            .issuedAt(Date(now))
            .expiration(Date(expirationTime))
            .signWith(getSignInKey(), SignatureAlgorithm.HS256)
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
        logger.info("Secret key: $secret")
        val keyBytes = Decoders.BASE64.decode(secret)
        return Keys.hmacShaKeyFor(keyBytes)
    }
}