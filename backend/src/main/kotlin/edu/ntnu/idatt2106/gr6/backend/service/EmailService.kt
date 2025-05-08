package edu.ntnu.idatt2106.gr6.backend.service

import jakarta.mail.internet.MimeMessage
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service


@Service
class EmailService(private val mailSender: JavaMailSender) {

    @Value("\${email.verification.url.prefix}")
    private lateinit var frontendBaseUrl: String

    fun sendEmail(to: String, subject: String, text: String) {
        val message = SimpleMailMessage()
        message.setTo(to)
        message.setSubject(subject)
        message.setText(text)
        message.setFrom("krisefiksern@gmail.com")
        mailSender.send(message)
    }

    fun sendHtmlEmail(to: String, subject: String, htmlContent: String) {
        val message: MimeMessage = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, true, "UTF-8")

        helper.setTo(to)
        helper.setSubject(subject)
        helper.setText(htmlContent, true) // true indicates HTML content
        helper.setFrom("krisefiksern@gmail.com")

        mailSender.send(message)
    }

    fun sendVerificationEmail(to: String, verificationCode: String) {
        val subject = "Email Verification"
        val text = "Please verify your email using the following code: $verificationCode"
        sendEmail(to, subject, text)
    }

    fun sendVerificationLink(to: String, token: String) {

        val verificationLink = "$frontendBaseUrl/email-verification/$token"

        val subject = "Bekfreft din e-postadresse"
        val htmlContent = """
            <html>
                <body>
                    <h2>Email Verifisering</h2>
                    <p>Takk for at du registerer deg hos krisefikser. Vennligst klikk på linken nedenfor for å bekrefte at det er deg</p>
                    <p><a href="$verificationLink">Verifiser ved å trykke her</a></p>
                    <p>Den linken er gyldig i kun 10 minutter.</p>
                    <p>Hvis du ikke har laget en bruker, vennligst ignorer denne eposten.</p>
                </body>
            </html>
        """.trimIndent()

        sendHtmlEmail(to, subject, htmlContent)
    }
}