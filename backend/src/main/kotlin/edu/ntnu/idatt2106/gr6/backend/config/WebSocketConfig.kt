package edu.ntnu.idatt2106.gr6.backend.config

import edu.ntnu.idatt2106.gr6.backend.service.JwtService
import edu.ntnu.idatt2106.gr6.backend.service.UserDetailsServiceImpl
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.config.ChannelRegistration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.messaging.support.MessageHeaderAccessor
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer
import org.springframework.messaging.Message



@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
class WebsocketConfig(
    private val jwtService: JwtService,
    private val userDetailsService: UserDetailsServiceImpl,
    private val jwtHandshakeInterceptor: JwtHandshakeInterceptor,
) : WebSocketMessageBrokerConfigurer {
    private val logger = org.slf4j.LoggerFactory.getLogger(WebsocketConfig::class.java)

    init {
        logger.info("WebSocketConfig initialized")
    }
    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/ws")
            .setAllowedOriginPatterns("*")
            .withSockJS()
            .setInterceptors(jwtHandshakeInterceptor)
        registry.addEndpoint("/ws")
            .setAllowedOriginPatterns("*")
            .addInterceptors(jwtHandshakeInterceptor)
    }

    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        registry.enableSimpleBroker("/topic")
        registry.setApplicationDestinationPrefixes("/app")
    }

    override fun configureClientInboundChannel(registration: ChannelRegistration) {
        logger.info("Configuring client inbound channel")
        registration.interceptors(object : ChannelInterceptor {
            override fun preSend(message: Message<*>, channel: MessageChannel): Message<*>? {
                val accessor: StompHeaderAccessor =
                    MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor::class.java) ?: return message
                logger.info("Headers: {}", accessor)

                if (StompCommand.CONNECT == accessor?.getCommand()) {
                    return message
                }

                if (StompCommand.SUBSCRIBE == accessor?.getCommand()) {
                    logger.info("User subscribed to topic: ${accessor?.destination}")
                }

                if (StompCommand.DISCONNECT == accessor?.getCommand()) {
                    logger.info("Disconnecting user")
                    SecurityContextHolder.clearContext()
                    return message
                }

                if (StompCommand.UNSUBSCRIBE == accessor?.getCommand()) {
                    logger.info("User unsubscribed from topic: ${accessor?.destination}")
                }

                if (StompCommand.SEND == accessor.command) {
                    val destination = accessor.destination
                    if (destination != null && destination.startsWith("/app/public")) {
                        logger.info("Allowing unauthenticated access to public destination: $destination")
                        return message
                    }
                }



                val destination = accessor.destination ?: ""
                logger.info("Destination: $destination")
                if (destination.startsWith("/topic/public")) {
                    logger.info("Allowing unauthenticated access to public destination: $destination")
                    return message
                }

                val authorizationHeader: String? = accessor.getFirstNativeHeader("Authorization")
                if (authorizationHeader.isNullOrBlank() || !authorizationHeader.startsWith("Bearer ")) {
                    logger.info("Authorization header: $authorizationHeader")
                    throw IllegalArgumentException("Missing or invalid Authorization header")
                }

                val token = authorizationHeader.substring(7)
                logger.info("Extracted token from websocket: $token")

                val username = jwtService.extractEmailFromToken(token)
                    ?: throw IllegalArgumentException("Invalid token")
                val userDetails = userDetailsService!!.loadUserByUsername(username)

                val authToken = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                SecurityContextHolder.getContext().authentication = authToken
                accessor.user = authToken

                return message
            }
        })
    }
}