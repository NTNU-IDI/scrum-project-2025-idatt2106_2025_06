package edu.ntnu.idatt2106.gr6.backend.exception

import org.springframework.http.ResponseEntity

/**
 * Custom exception class for when an item does not exist.
 */
class ItemNotFoundException(message: String) : RuntimeException(message) {
    companion object {
        fun forItemId(itemId: String) = ItemNotFoundException("Item with id $itemId does not exist.")
        fun forItemName(itemName: String) = ItemNotFoundException("Item with name $itemName does not exist.")
    }
}