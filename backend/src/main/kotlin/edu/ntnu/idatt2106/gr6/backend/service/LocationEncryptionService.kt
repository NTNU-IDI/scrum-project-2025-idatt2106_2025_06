package edu.ntnu.idatt2106.gr6.backend.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec
import javax.security.auth.kerberos.EncryptionKey

@Service
class LocationEncryptionService(
    @Value("\${encryption.key}") private val encryptionKey: String,
) {
    private val cipher: Cipher = Cipher.getInstance("AES")
    private val key: SecretKey = SecretKeySpec(encryptionKey.toByteArray(), "AES")

    fun encryptLocation(location: String): String {
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val encryptedBytes = cipher.doFinal(location.toByteArray())
        return encryptedBytes.joinToString(",") { it.toString() }
    }

    fun decryptLocation(location: String): String {
        cipher.init(Cipher.DECRYPT_MODE, key)
        val encryptedBytes = location.split(",").map { it.toByte() }.toByteArray()
        val decryptedBytes = cipher.doFinal(encryptedBytes)
        return String(decryptedBytes)
    }
}