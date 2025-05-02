package edu.ntnu.idatt2106.gr6.backend.DTOs

import edu.ntnu.idatt2106.gr6.backend.model.Item

data class SimpleItemResponse (
    val id: String,
    val name: String,
    val typeId: Int,
    val unitId: Int
) {
    companion object {
        fun fromItem(item: Item) = SimpleItemResponse (
            id = item.id,
            name = item.name,
            typeId = item.typeId,
            unitId = item.unitId
        )
    }
}