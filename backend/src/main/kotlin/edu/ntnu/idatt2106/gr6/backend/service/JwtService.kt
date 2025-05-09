package edu.ntnu.idatt2106.gr6.backend.service

import edu.ntnu.idatt2106.gr6.backend.exception.InvalidUserDetailsException
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

/**
 * Service class responsible for generating, parsing, and validating JWT tokens.
 * Handles both authentication tokens and email verification tokens.
 *
 * Uses a symmetric secret key loaded from the application's configuration.
 */
@Service
class JwtService {
    private val logger = org.slf4j.LoggerFactory.getLogger(JwtService::class.java)

    /**
     * Secret key used to sign JWTs, injected from application properties.
     */
    @Value("\${security.jwt.secret-key}")
    private lateinit var secret: String

    /**
     * Token expiration time in milliseconds, configured in properties.
     */
    @Value("\${security.jwt.expiration-time}")
    private var expiration: Long = 0

    /**
     * Email verification token expiration time.
     */
    @Value("\${email.verification.expiration-time}")
    private var emailVerificationExpiration: Long = 0

    /**
     * Generates a signed JWT containing user ID, email, roles, and permissions.
     *
     * @param userDetails Spring Security's UserDetails implementation (must be of type `User`).
     * @return A signed JWT string.
     * @throws InvalidUserDetailsException if the userDetails is not of type `User`.
     */
    fun generateToken(userDetails: UserDetails): String {
        val now = System.currentTimeMillis()
        val expirationTime = now + expiration

        val userId =
            if (userDetails is User) {
                userDetails.id
            } else {
                throw InvalidUserDetailsException.notCustomUserClass()
            }
        val emailClaim = userDetails.username

        val roleClaim = userDetails.authorities
            .filter { it.authority.startsWith("ROLE_") }
            .map { it.authority }
        val permissionsClaim = userDetails.authorities
            .filter { !it.authority.startsWith("ROLE_") }
            .map { it.authority }

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

    /**
     * Extracts the user ID (subject) from the token.
     *
     * @param token The JWT string.
     * @return UUID of the user or null if invalid.
     */
    fun extractUserIdFromToken(token: String): UUID? {
        val claims = getAllClaimsFromToken(token)
        return try {
            claims.subject?.let { UUID.fromString(it) }
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    /**
     * Extracts the email claim from the token.
     *
     * @param token The JWT string.
     * @return Email address or null if not present.
     */
    fun extractEmailFromToken(token: String): String? {
        val claims = getAllClaimsFromToken(token)
        return claims["email"] as? String
    }

    /**
     * Extracts the purpose claim (used for email verification) from the token.
     *
     * @param token The JWT string.
     * @return Purpose string or null if not present.
     */
    fun extractPurposeFromToken(token: String): String? {
        val claims = getAllClaimsFromToken(token)
        return claims["purpose"] as? String
    }

    /**
     * Validates whether a token is valid and belongs to the given user.
     *
     * @param token The JWT string.
     * @param userDetails The user to validate against.
     * @return `true` if the token is valid and matches the user.
     */
    fun isTokenValid(
        token: String,
        userDetails: UserDetails
    ): Boolean {
        val extractedUserId: UUID? = extractUserIdFromToken(token)
        val userIdFromUserDetails = if (userDetails is User) userDetails.id else null
        return extractedUserId != null && extractedUserId == userIdFromUserDetails && !isTokenExpired(token)
    }

    /**
     * Validates an email verification token by checking its purpose and expiration.
     *
     * @param token The JWT string.
     * @return `true` if it's a valid email verification token.
     */
    fun isVerificationTokenValid(
        token: String
    ): Boolean {
        val claims = getAllClaimsFromToken(token)
        val purpose = claims["purpose"] as? String
        return purpose == "email_verification" && !isTokenExpired(token)
    }

    /**
     * Returns the configured expiration time for regular tokens.
     *
     * @return Expiration time in milliseconds.
     */
    fun getExpirationTime(): Long {
        return expiration
    }

    /**
     * Checks whether a token has expired.
     *
     * @param token The JWT string.
     * @return `true` if expired, `false` otherwise.
     */
    fun isTokenExpired(token: String): Boolean {
        val claims = getAllClaimsFromToken(token)
        return claims.expiration.before(Date())
    }

    /**
     * Generates a JWT specifically for email verification, with a shorter expiration time.
     *
     * @param userID The user's UUID.
     * @param email The user's email address.
     * @return A signed verification JWT.
     */
    fun generateVerificationToken(userID: UUID, email: String): String {
        val now = System.currentTimeMillis()
        val validationExpiration = now + (10 * 60 * 1000) // 10 minuttes

        return Jwts
            .builder()
            .subject(userID.toString())
            .claim("email", email)
            .claim("purpose", "email_verification")
            .issuedAt(Date(now))
            .expiration(Date(validationExpiration))
            .signWith(getSignInKey(), SignatureAlgorithm.HS256)
            .compact()
    }

    /**
     * Parses all claims from a given JWT token.
     *
     * @param token The JWT string.
     * @return Claims object containing the payload.
     */
    fun getAllClaimsFromToken(token: String): Claims =
        Jwts
            .parser()
            .verifyWith(getSignInKey())
            .build()
            .parseSignedClaims(token)
            .payload

    /**
     * Builds the signing key used for both signing and verifying JWTs.
     *
     * @return A `SecretKey` for HMAC SHA-based signing.
     */
    private fun getSignInKey(): SecretKey {
        val keyBytes = Decoders.BASE64.decode(secret)
        return Keys.hmacShaKeyFor(keyBytes)
    }
}