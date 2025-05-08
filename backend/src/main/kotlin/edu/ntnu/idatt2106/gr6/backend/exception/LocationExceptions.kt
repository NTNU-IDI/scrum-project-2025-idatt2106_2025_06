package edu.ntnu.idatt2106.gr6.backend.exception

/**
 * Custom exception class for a location not being available because it has been disabled.
 */
class LocationNotAvailableException(message: String) : RuntimeException(message) {
    companion object {
        fun forUser(userId: String) = LocationNotAvailableException("Location with id $userId has no" +
                " tracked location history or has disabled tracking.")
    }
}

/**
 * Custom exception class for when a user does not match the user in the request.
 */
class UserMismatchException(message: String) : RuntimeException(message) {
    companion object {
        fun forUser(userId: String) = UserMismatchException("User with id $userId does not match the" +
                " user in the request.")
    }
}
/**
 * Custom exception class for when a user has disabled location tracking.
 */
class UserLocationDisabledException(message: String) : RuntimeException(message) {
    companion object {
        fun forUser(userId: String) = UserLocationDisabledException("User with id $userId has disabled" +
                " location tracking.")
    }
}