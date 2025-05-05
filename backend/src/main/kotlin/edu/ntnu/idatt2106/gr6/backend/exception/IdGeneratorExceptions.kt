package edu.ntnu.idatt2106.gr6.backend.exception

class IdGeneratorMaxAttemptsReachedException(message: String, ) : RuntimeException(message) {
    companion object {
        fun forEventName(name: String) = IdGeneratorMaxAttemptsReachedException("Id generator has reached max attempts for event: $name.")
    }
}