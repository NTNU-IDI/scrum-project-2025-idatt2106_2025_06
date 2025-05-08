package edu.ntnu.idatt2106.gr6.backend.exception

/**
 * Custom exception class for when a query does not affect any rows in the database.
 */

class DatabaseNoRowsAffectedException(message: String) : RuntimeException(message) {
    companion object {
        fun forQuery(query: String) = DatabaseNoRowsAffectedException("No rows affected for query, but was expected")
    }
}