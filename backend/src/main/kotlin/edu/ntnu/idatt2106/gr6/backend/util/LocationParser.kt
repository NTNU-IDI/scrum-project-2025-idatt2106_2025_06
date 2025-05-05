package edu.ntnu.idatt2106.gr6.backend.util

import edu.ntnu.idatt2106.gr6.backend.model.Location
import org.springframework.stereotype.Component

@Component
class LocationParser {
    fun parseLocation(location: String): Location {
        val point = location.substringAfter("POINT(").substringBefore(")")
        val coords = point.removePrefix("POINT(").removeSuffix(")").split(" ")
        val lat = coords[1].toDouble()
        val lon = coords[0].toDouble()
        return Location(latitude = lat, longitude = lon)
    }
}