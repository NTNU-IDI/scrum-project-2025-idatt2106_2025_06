package edu.ntnu.idatt2106.gr6.backend.config

import edu.ntnu.idatt2106.gr6.backend.service.JwtService
import edu.ntnu.idatt2106.gr6.backend.service.UserContextService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.servlet.HandlerExceptionResolver

@Component
class JwtAuthenticationFilter(
    private val jwtService: JwtService,
    private val userDetailsService: UserDetailsService,
    private val handlerExceptionResolver: HandlerExceptionResolver,
    private val userContextService: UserContextService
    ) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }

        try {
            val jwt = authHeader.substring(7)
            val email: String? = jwtService.extractEmailFromToken(jwt)
            logger.info("Extracted email from token: $email")

            val authentication: Authentication? = SecurityContextHolder.getContext().authentication

            if (email != null && authentication == null) {
                val userDetails = userDetailsService.loadUserByUsername(email)
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    val userId =
                        jwtService.extractUserIdFromToken(jwt)
                            ?: throw IllegalStateException("UserID not found in valid token")

                    val userDetailss = userDetailsService.loadUserByUsername(email)
                        ?: throw IllegalStateException("User not found in repository")

                    userContextService.setCurrentUserId(userId)
                    val authToken =
                        UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetailss.authorities,
                        )
                    authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = authToken
                }
            }
            filterChain.doFilter(request, response)
        } catch (e: Exception) {
            logger.error("Jwt processing error has occurred: ${e.message}", e)
            SecurityContextHolder.clearContext()
            handlerExceptionResolver.resolveException(request, response, null, e)
        }
    }
}