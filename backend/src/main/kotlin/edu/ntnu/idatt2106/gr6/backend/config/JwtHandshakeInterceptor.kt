package edu.ntnu.idatt2106.gr6.backend.config

import edu.ntnu.idatt2106.gr6.backend.service.JwtService
import org.slf4j.LoggerFactory
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.server.HandshakeInterceptor
import kotlin.collections.forEach

/**
 * Interceptor for WebSocket handshake requests that extracts the user ID from the JWT token in the Authorization header.
 *
 */

@Component
class JwtHandshakeInterceptor(
    private val jwtService: JwtService,
) : HandshakeInterceptor {
    private val logger = LoggerFactory.getLogger(JwtHandshakeInterceptor::class.java)

    override fun beforeHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        attributes: MutableMap<String, Any>
    ): Boolean {
        logger.info("Handshake request: {}", request)
        val headers = request.headers
        val authHeader = headers.getFirst("Authorization")

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            val token = authHeader.substring(7)
            val id = jwtService.extractUserIdFromToken(token)
            attributes["id"] = id!!
        }

        return true
    }

    override fun afterHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        ex: Exception?
    ) {}
}