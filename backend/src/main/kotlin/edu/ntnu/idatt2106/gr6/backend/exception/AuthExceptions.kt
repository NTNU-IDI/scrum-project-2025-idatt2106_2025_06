package edu.ntnu.idatt2106.gr6.backend.exception

class InvalidCredentialsException(message: String) : RuntimeException(message) {
    companion object {
        fun forEmail(email: String) = InvalidCredentialsException("Invalid credentials for email $email.")
        fun forUserId(userId: String) = InvalidCredentialsException("Invalid credentials for user ID $userId.")
    }
}

class AuthenticationException(message: String) : RuntimeException(message) {
    companion object {
        fun forUserId(userId: String) = AuthenticationException("Authentication failed for user ID $userId.")
        fun forEmail(email: String) = AuthenticationException("Authentication failed for email $email.")
    }
}

class InvalidUserDetailsException(message: String) : RuntimeException(message) {
    companion object {
        fun notCustomUserClass(): InvalidUserDetailsException {
            return InvalidUserDetailsException("UserDetails must be an instance of custom User class")
        }
    }
}