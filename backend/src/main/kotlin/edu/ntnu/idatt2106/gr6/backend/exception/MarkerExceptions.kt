package edu.ntnu.idatt2106.gr6.backend.exception

class MarkerNotFoundException(message: String) : RuntimeException(message) {
    companion object {
        fun forMarkerId(markerId: String) = MarkerNotFoundException("Marker with id $markerId does not exist.")
        fun forMarkerName(markerName: String) = MarkerNotFoundException("Marker with name $markerName does not exist.")
    }
}