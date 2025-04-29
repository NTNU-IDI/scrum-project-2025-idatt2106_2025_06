package edu.ntnu.idatt2106.gr6.backend.model

import edu.ntnu.idatt2106.gr6.backend.DTOs.Location
import java.sql.ResultSet

fun ResultSet.getLocation(): Location? {
    val latitude = getDouble("latitude")
    val longitude = getDouble("longitude")
    return if (wasNull()) null else Location(latitude, longitude)
}

fun ResultSet.getEventType(): EventType {
    val type = getString("type")
    return EventType.fromString(type)
}

fun ResultSet.getSeverity(): Severity {
    val severity = getString("severity")
    return Severity.fromString(severity)
}