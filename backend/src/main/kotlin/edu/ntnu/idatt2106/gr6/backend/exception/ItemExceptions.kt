package edu.ntnu.idatt2106.gr6.backend.exception

import org.springframework.http.ResponseEntity

class ItemNotFoundException(message: String) : RuntimeException(message) {
    companion object {
        fun forItemId(itemId: String) = ItemNotFoundException("Item with id $itemId does not exist.")
        fun forItemName(itemName: String) = ItemNotFoundException("Item with name $itemName does not exist.")
    }
}