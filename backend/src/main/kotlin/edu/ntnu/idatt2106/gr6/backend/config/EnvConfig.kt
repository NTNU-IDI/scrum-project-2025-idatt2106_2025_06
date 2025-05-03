package edu.ntnu.idatt2106.gr6.backend.config

import io.github.cdimascio.dotenv.Dotenv
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import javax.annotation.PostConstruct

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
class EnvConfig {
    private val logger = org.slf4j.LoggerFactory.getLogger(EnvConfig::class.java)
    @PostConstruct
    fun loadEnv() {
        try {
            val dotenv = Dotenv.configure()
                .directory(System.getProperty("user.dir"))
                .filename(".env")
                .ignoreIfMissing()
                .load()

            dotenv.entries().forEach { (key, value) ->
                if (System.getProperty(key) == null) {
                    System.setProperty(key, value)
                }
            }

        } catch (e: Exception) {
            logger.error("Error while loading environment", e)
        }
    }
}