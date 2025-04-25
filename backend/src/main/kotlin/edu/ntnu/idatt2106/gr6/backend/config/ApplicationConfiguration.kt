package edu.ntnu.idatt2106.gr6.backend.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import edu.ntnu.idatt2106.gr6.backend.repository.UserRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority

@Configuration
class ApplicationConfiguration(
    private val userRepository: UserRepository
) {
    private val logger = org.slf4j.LoggerFactory.getLogger(ApplicationConfiguration::class.java)
    @Bean
    fun userDetailsService(): UserDetailsService =
        UserDetailsService { email ->
            val user = userRepository.findByEmail(email)
                ?: throw UsernameNotFoundException("User with email $email not found")

            val authorities = user.role.permissions.map { permission ->
                SimpleGrantedAuthority(permission.name)
            }

            logger.info("User with email $email found with roles: ${user.role.permissions.joinToString(", ")}")

            org.springframework.security.core.userdetails.User(
                user.email,
                user.password,
                authorities
            )
        }

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder = BCryptPasswordEncoder()

    @Bean
    @Throws(Exception::class)
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager = config.authenticationManager

    @Bean
    fun authenticationProvider(): AuthenticationProvider {
        val authProvider = DaoAuthenticationProvider()

        authProvider.setUserDetailsService(userDetailsService())
        authProvider.setPasswordEncoder(passwordEncoder())

        return authProvider
    }
}