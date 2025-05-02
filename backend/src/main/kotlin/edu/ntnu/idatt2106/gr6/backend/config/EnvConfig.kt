package edu.ntnu.idatt2106.gr6.backend.config

import io.github.cdimascio.dotenv.Dotenv
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import javax.annotation.PostConstruct

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
class EnvConfig {

    @PostConstruct
    fun loadEnv() {
        try {
            // Load the .env file
            val dotenv = Dotenv.configure()
                .directory(System.getProperty("user.dir")) // Optional: specify the directory if needed
                .filename(".env")                          // Default is .env
                .ignoreIfMissing()                         // Don't throw an error if .env file is missing
                .load()

            // Set environment variables to system properties for Spring to pick them up
            dotenv.entries().forEach { (key, value) ->
                if (System.getProperty(key) == null) {
                    System.setProperty(key, value)
                }
            }

            // Optionally, print out the loaded environment variables
            println("Loaded JWT_SECRET_KEY: ${System.getProperty("JWT_SECRET_KEY")}")

        } catch (e: Exception) {
            println("Error loading .env file: ${e.message}")
        }
    }
}