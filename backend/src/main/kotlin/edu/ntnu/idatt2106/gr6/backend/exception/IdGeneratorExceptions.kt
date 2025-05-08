package edu.ntnu.idatt2106.gr6.backend.exception

/**
 * Custom exception class for when an event does not exist.
 */
class IdGeneratorMaxAttemptsReachedException(message: String, ) : RuntimeException(message) {
    companion object {
        fun forEventName(name: String) = IdGeneratorMaxAttemptsReachedException("Id generator has reached max attempts for event: $name.")
        fun forVerificationToken(name: String) = IdGeneratorMaxAttemptsReachedException("Id generator has reached max attempts for verification token: $name.")
    }
}