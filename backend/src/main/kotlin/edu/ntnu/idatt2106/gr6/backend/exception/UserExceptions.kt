package edu.ntnu.idatt2106.gr6.backend.exception
/**
 * Custom exception class for when trying to create a user that already exists.
 */
class UserAlreadyExistsException(message: String) : RuntimeException(message) {
    companion object {
        fun forUserId(userId: String) = UserAlreadyExistsException("User with id $userId already exists.")
        fun forEmail(email: String) = UserAlreadyExistsException("User with email $email already exists.")
    }
}

/**
 * Custom exception class for when the provided user is not found in the database.
 */
class UserNotFoundException(message: String) : RuntimeException(message) {
    companion object {
        fun forUserId(userId: String) = UserNotFoundException("User with id $userId not found.")
        fun forEmail(email: String) = UserNotFoundException("User with email $email not found.")
    }
}

/**
 * Custom exception class for when the provided user is not authorized to perform an action.
 */
class UserNotAuthorizedException(message: String) : RuntimeException(message) {
    companion object {
        fun forUserId(userId: String) = UserNotAuthorizedException("User with id $userId is not authorized.")
    }
}

/**
 * Custom exception class for when a user is already logged in.
 */
class UserAlreadyLoggedInException(message: String) : RuntimeException(message) {
    companion object {
        fun forUserId(userId: String) = UserAlreadyLoggedInException("User with id $userId is already logged in.")
    }
}

/**
 * Custom exception class for when a user is not logged in.
 */
class UserNotLoggedInException(message: String) : RuntimeException(message) {
    companion object {
        fun forUserId(userId: String) = UserNotLoggedInException("User with id $userId is not logged in.")
    }
}

/**
 * Custom exception class for when a user couldn't update credentials .
 */
class UserFailedToUpdateCredentialsException(message: String) : RuntimeException(message) {
    companion object {
        fun forUserId(userId: String) = UserFailedToUpdateCredentialsException("User with id $userId failed to update credentials.")
    }
}