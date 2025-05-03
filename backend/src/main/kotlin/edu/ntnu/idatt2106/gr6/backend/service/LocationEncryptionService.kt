package edu.ntnu.idatt2106.gr6.backend.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec
import javax.security.auth.kerberos.EncryptionKey
import java.util.Base64
import javax.crypto.spec.IvParameterSpec

@Service
class LocationEncryptionService(
    @Value("\${encryption.key}") private val encryptionKey: String,
) {
    private val logger = org.slf4j.LoggerFactory.getLogger(LocationEncryptionService::class.java)
    private val key: SecretKey = SecretKeySpec(encryptionKey.toByteArray(), "AES")
    private val iv = IvParameterSpec(ByteArray(16)) // Replace with secure random in production

    fun encryptLocation(rawLocation: String): String {
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, key, iv)
        val encryptedBytes = cipher.doFinal(rawLocation.toByteArray())
        return Base64.getEncoder().encodeToString(encryptedBytes)
    }

    fun decryptLocation(encryptedLocation: String): String {
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, key, iv)
        val encryptedBytes = Base64.getDecoder().decode(encryptedLocation)
        val decryptedBytes = cipher.doFinal(encryptedBytes)
        return String(decryptedBytes)
    }
}