package edu.ntnu.idatt2106.gr6.backend.util

import org.springframework.stereotype.Component

@Component
class IdGenerator {
    fun generateId(idLength: Int): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        val random = StringBuilder()
        for (i in 0 until idLength) {
            val randomIndex = (Math.random() * chars.length).toInt()
            random.append(chars[randomIndex])
        }
        return random.toString()
    }
}