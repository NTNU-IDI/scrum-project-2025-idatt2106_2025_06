package edu.ntnu.idatt2106.gr6.backend.config

import edu.ntnu.idatt2106.gr6.backend.service.JwtService
import edu.ntnu.idatt2106.gr6.backend.service.NotificationService
import edu.ntnu.idatt2106.gr6.backend.service.UserDetailsServiceImpl
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Bean
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
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.web.socket.messaging.SessionConnectedEvent
import org.springframework.web.socket.messaging.SessionSubscribeEvent

/**
 * WebSocket configuration class that sets up STOMP messaging endpoints and JWT-based
 * authentication for WebSocket communication.
 *
 * This configuration:
 * - Registers `/ws` endpoints for WebSocket/STOMP communication.
 * - Enables a simple message broker with `/topic` prefix.
 * - Intercepts messages to validate JWT tokens from the `Authorization` header.
 * - Allows unauthenticated access to specific public topics like `/app/public` and `/topic/public`.
 * - Sends notifications on subscription to certain public topics.
 */
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

    /**
     * Registers STOMP endpoints with and without SockJS fallback support.
     * Adds JWT handshake interceptor for validating connections.
     *
     * @param registry The endpoint registry.
     */
    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/ws")
            .setAllowedOriginPatterns("*")
            .withSockJS()
            .setInterceptors(jwtHandshakeInterceptor)
        registry.addEndpoint("/ws")
            .setAllowedOriginPatterns("*")
            .addInterceptors(jwtHandshakeInterceptor)
    }

    /**
     * Configures the message broker used to route messages between clients and server.
     * Sets `/topic` for outgoing messages and `/app` for application-specific destinations.
     *
     * @param registry The message broker registry.
     */
    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        registry.enableSimpleBroker("/topic")
        registry.setApplicationDestinationPrefixes("/app")
    }

    /**
     * Intercepts all client-bound messages and performs JWT-based authentication.
     * Allows public access to specific endpoints and throws if token is missing or invalid.
     *
     * @param registration The channel registration.
     */
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

    /**
     * Bean that listens for WebSocket `SUBSCRIBE` events and sends the latest
     * data immediately upon subscription to predefined topics.
     *
     * - `/topic/public/newsAlerts`: Sends latest notifications.
     * - `/topic/public/events`: Sends latest event-related news.
     *
     * @param messagingTemplate Template used to send messages to clients.
     * @param notificationService Service that provides latest updates.
     * @return An application listener that reacts to subscriptions.
     */
    @Bean
    fun subscribeEventListener(messagingTemplate: SimpMessagingTemplate, notificationService: NotificationService): ApplicationListener<SessionSubscribeEvent> {
        return ApplicationListener { event ->
            val accessor = StompHeaderAccessor.wrap(event.message)
            val destination = accessor.destination

            // Only send notifications when client subscribes to the newsAlerts topic
            if (destination == "/topic/public/newsAlerts") {
                val latestNotifications = notificationService.getNewestNotifications()
                messagingTemplate.convertAndSend("/topic/public/newsAlerts", latestNotifications)
                logger.info("Sent notifications after client subscribed to $destination")
            }

            if (destination == "/topic/public/events") {
                val latestNews = notificationService.getNews()
                messagingTemplate.convertAndSend("/topic/public/events", latestNews)
                logger.info("Sent news after client subscribed to $destination")
            }
        }
    }
}