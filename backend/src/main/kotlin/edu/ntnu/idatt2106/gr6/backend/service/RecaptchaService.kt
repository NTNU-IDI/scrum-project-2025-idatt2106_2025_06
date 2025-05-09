package edu.ntnu.idatt2106.gr6.backend.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import edu.ntnu.idatt2106.gr6.backend.DTOs.UserDTOs.RecaptchaResponse
import edu.ntnu.idatt2106.gr6.backend.exception.RecaptchaVerificationFailedException
import org.springframework.http.HttpStatus

/**
 * Service class for handling reCAPTCHA verification. this class used reCAPTCHA v2 technology
 * to verify if the user is a human or a bot. it uses a private and a site key to verify using google
 * api
 */


@Service
class RecaptchaService(
    @Value("\${recaptcha.site-key}") private val secret: String
) {

    private val verifyUrl = "https://www.google.com/recaptcha/api/siteverify"

    /**
     * Verifies the reCAPTCHA token by sending a request to the Google reCAPTCHA API. the
     * response from the API is returned as a success boolean value.
     *
     * @param token The reCAPTCHA token to verify.
     * @return True if the verification is successful, false otherwise.
     */
    fun verifyRecaptcha(token: String): Boolean {
        val restTemplate = RestTemplate()

        val headers = org.springframework.http.HttpHeaders()
        headers.contentType = org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED

        val params = LinkedMultiValueMap<String, String>()
        params.add("secret", secret)
        params.add("response", token)

        val request = HttpEntity<MultiValueMap<String, String>>(params, headers)

        val response = restTemplate.postForEntity(
            verifyUrl,
            request,
            RecaptchaResponse::class.java
        )



        return response.body?.success ?: throw RecaptchaVerificationFailedException.forRecaptcha()
    }
}