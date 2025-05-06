package edu.ntnu.idatt2106.gr6.backend.exception

class DatabaseNoRowsAffectedException(message: String) : RuntimeException(message) {
    companion object {
        fun forQuery(query: String) = DatabaseNoRowsAffectedException("No rows affected for query, but was expected")
    }
}