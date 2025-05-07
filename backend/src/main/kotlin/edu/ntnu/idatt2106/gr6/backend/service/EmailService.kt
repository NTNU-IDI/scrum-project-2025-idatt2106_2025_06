package edu.ntnu.idatt2106.gr6.backend.service

import com.resend.Resend
import com.resend.core.exception.ResendException
import com.resend.services.emails.model.SendEmailRequest
import com.resend.services.emails.model.SendEmailResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class EmailService {
    private val logger = LoggerFactory.getLogger(EmailService::class.java)

    // Never hardcode API keys in production code
    private val apiKey = "re_MpFsfHP1_FeAp7TN6G1e4kv1inLspzQ4R"
    private val resend: Resend = Resend(apiKey)

    fun sendVerificationEmail(to: String = "mathole@stud.ntnu.no", token: String = "default-token"): SendEmailResponse {
        try {
            logger.info("Sending verification email to: $to")

            val sendEmailRequest = SendEmailRequest.builder()
                .from("onboarding@resend.dev")
                .to(to)
                .subject("Verify your email")
                .html("<p>Click <a href='http://localhost:5173/verify?token=$token'>here</a> to verify your email.</p>")
                .build()

            return resend.emails().send(sendEmailRequest)
        } catch (e: ResendException) {
            logger.error("Failed to send email through Resend API: ${e.message}")
            throw e
        } catch (e: Exception) {
            logger.error("Unexpected error sending email: ${e.javaClass.simpleName} - ${e.message}")
            throw e
        }
    }
}