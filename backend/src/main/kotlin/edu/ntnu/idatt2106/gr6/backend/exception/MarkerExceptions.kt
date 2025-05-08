package edu.ntnu.idatt2106.gr6.backend.exception

/**
 * Custom exception class for when a marker doesn't exist.
 */

class MarkerNotFoundException(message: String) : RuntimeException(message) {
    companion object {
        fun forMarkerId(markerId: String) = MarkerNotFoundException("Marker with id $markerId does not exist.")
        fun forMarkerName(markerName: String) = MarkerNotFoundException("Marker with name $markerName does not exist.")
    }
}