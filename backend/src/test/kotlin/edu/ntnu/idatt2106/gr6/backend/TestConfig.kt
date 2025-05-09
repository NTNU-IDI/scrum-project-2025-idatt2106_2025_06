package edu.ntnu.idatt2106.gr6.backend

import edu.ntnu.idatt2106.gr6.backend.service.RecaptchaService
import org.mockito.Mockito.mock
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary

@TestConfiguration
class TestConfig {
    @Bean
    @Primary
    fun recaptchaService(): RecaptchaService {
        return mock(RecaptchaService::class.java)
    }
}