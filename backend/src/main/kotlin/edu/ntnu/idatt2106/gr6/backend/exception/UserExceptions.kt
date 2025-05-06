package edu.ntnu.idatt2106.gr6.backend.exception

class UserAlreadyExistsException(message: String) : RuntimeException(message) {
    companion object {
        fun forUserId(userId: String) = UserAlreadyExistsException("User with id $userId already exists.")
        fun forEmail(email: String) = UserAlreadyExistsException("User with email $email already exists.")
    }
}

class UserNotFoundException(message: String) : RuntimeException(message) {
    companion object {
        fun forUserId(userId: String) = UserNotFoundException("User with id $userId not found.")
        fun forEmail(email: String) = UserNotFoundException("User with email $email not found.")
    }
}

class UserNotAuthorizedException(message: String) : RuntimeException(message) {
    companion object {
        fun forUserId(userId: String) = UserNotAuthorizedException("User with id $userId is not authorized.")
    }
}

class UserAlreadyLoggedInException(message: String) : RuntimeException(message) {
    companion object {
        fun forUserId(userId: String) = UserAlreadyLoggedInException("User with id $userId is already logged in.")
    }
}

class UserNotLoggedInException(message: String) : RuntimeException(message) {
    companion object {
        fun forUserId(userId: String) = UserNotLoggedInException("User with id $userId is not logged in.")
    }
}

class UserFailedToUpdateCredentialsException(message: String) : RuntimeException(message) {
    companion object {
        fun forUserId(userId: String) = UserFailedToUpdateCredentialsException("User with id $userId failed to update credentials.")
    }
}