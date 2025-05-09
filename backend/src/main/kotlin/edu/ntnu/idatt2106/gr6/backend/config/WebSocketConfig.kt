package edu.ntnu.idatt2106.gr6.backend.config

import edu.ntnu.idatt2106.gr6.backend.repository.UserRepository
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
import java.util.UUID


@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
class WebsocketConfig(
    private val jwtService: JwtService,
    private val userDetailsService: UserDetailsServiceImpl,
    private val jwtHandshakeInterceptor: JwtHandshakeInterceptor,
    private val userRepository: UserRepository
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
                    throw IllegalArgumentException("Missing or invalid Authorization header")
                }


                //allows sending data from user if tracking enabled
                if (StompCommand.SEND == accessor.command && accessor.destination == "/app/private/location/update") {
                    val authorizationHeader = accessor.getFirstNativeHeader("Authorization")

                    if(authorizationHeader.isNullOrBlank() || !authorizationHeader.startsWith("Bearer ")) {
                        throw IllegalArgumentException("Missing or invalid Authorization header")
                    }
                    val token: String = authorizationHeader.substring(7)

                    val claims = jwtService.getAllClaimsFromToken(token)
                    val email = claims.get("email") as String
                    val userId = claims.subject
                    val userDetails = userDetailsService.loadUserByUsername(email)

                    if(jwtService.isTokenValid(token, userDetails)) {
                        val userDetails = userDetailsService.loadUserByUsername(email)
                        val authToken = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                        SecurityContextHolder.getContext().authentication = authToken
                        accessor.user = authToken
                    }

                    if (!userRepository.getUserTrackingPreferences(UUID.fromString(userId))) {
                        throw IllegalArgumentException("User has disabled tracking or tracking policy could not be fetched")
                    }
                        return message
                    } else {
                    logger.info("Destination not recognized: ${accessor.destination}")
                    // This error message was confusing as it's not about tracking being disabled
                    throw IllegalArgumentException("Invalid destination path")
                }
            }
        })
    }

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