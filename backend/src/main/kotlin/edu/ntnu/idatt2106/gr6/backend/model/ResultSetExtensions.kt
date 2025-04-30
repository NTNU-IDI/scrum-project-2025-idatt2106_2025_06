package edu.ntnu.idatt2106.gr6.backend.model

import edu.ntnu.idatt2106.gr6.backend.DTOs.Location
import java.sql.ResultSet

fun ResultSet.getLocation(): Location? {
    val point = getString("location") ?: return null
    val coords = point.removePrefix("POINT(").removeSuffix(")").split(" ")
    val lat = coords[1].toDouble()
    val lon = coords[0].toDouble()
    return Location(latitude = lat, longitude = lon)
}

fun ResultSet.getEventType(): EventType {
    val type = getString("type")
    return EventType.fromString(type)
}

fun ResultSet.getSeverity(): Severity {
    val severity = getString("severity")
    return Severity.fromString(severity)
}

fun ResultSet.getStatus(): Status {
    val status = getString("status")
    return Status.fromString(status)
}