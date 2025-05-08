package edu.ntnu.idatt2106.gr6.backend.exception

/**
 * Custom exception class for when a user has provided invalid credentials.
 */
class InvalidCredentialsException(message: String) : RuntimeException(message) {
    companion object {
        fun forEmail(email: String) = InvalidCredentialsException("Invalid credentials for email $email.")
        fun forUserId(userId: String) = InvalidCredentialsException("Invalid credentials for user ID $userId.")
    }
}

/**
 * Custom exception class for when a user could not be authenticated.
 */
class AuthenticationException(message: String) : RuntimeException(message) {
    companion object {
        fun forUserId(userId: String) = AuthenticationException("Authentication failed for user ID $userId.")
        fun forEmail(email: String) = AuthenticationException("Authentication failed for email $email.")
    }
}

/**
 * Custom exception class for when a custom user model is not an instance of springboot userDetails class.
 */
class InvalidUserDetailsException(message: String) : RuntimeException(message) {
    companion object {
        fun notCustomUserClass(): InvalidUserDetailsException {
            return InvalidUserDetailsException("UserDetails must be an instance of custom User class")
        }
    }
}

/**
 * Custom exception class for when a user tries to login but has not verified email.
 */
class UserEmailNotVerifiedException(message: String) : RuntimeException(message) {
    companion object {
        fun forEmail(email: String) = UserEmailNotVerifiedException("User with email $email has not verified their email.")
        fun forUserId(userId: String) = UserEmailNotVerifiedException("User with ID $userId has not verified their email.")
    }
}

/**
 * Custom exception class for when the internal token generation fails.
 */
class TokenIncorrectlyFormattedException(message: String) : RuntimeException(message) {
    companion object {
        fun forToken(token: String) = TokenIncorrectlyFormattedException("Token $token is incorrectly formatted.")
    }
}

/**
 * Custom exception class for when the token is invalid.
 */
class TokenInvalidException(message: String) : RuntimeException(message) {
    companion object {
        fun forToken(token: String) = TokenInvalidException("Token $token is invalid.")
    }
}