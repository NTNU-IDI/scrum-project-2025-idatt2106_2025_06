package edu.ntnu.idatt2106.gr6.backend.DTOs

import edu.ntnu.idatt2106.gr6.backend.model.Item
import edu.ntnu.idatt2106.gr6.backend.model.ItemInstance
import java.math.BigDecimal
import java.time.LocalDate

data class ItemInstanceResponse (
    val id: String,
    val name: String,
    val itemId: String,
    val storageId: String,
    val amount: BigDecimal,
    val unitId: Int, // Should be refactored to use UUID
    val typeId: Int, // Should be refactored to use UUID
    val expiryDate: LocalDate?
){
    companion object {
        fun fromItemInstance(itemInstance: ItemInstance, item: Item) = ItemInstanceResponse(
            id = itemInstance.id,
            name = item.name,
            itemId = itemInstance.itemId,
            storageId = itemInstance.storageId,
            amount = itemInstance.amount,
            unitId = item.unitId,
            typeId = item.typeId,
            expiryDate = itemInstance.expiryDate
        )
    }
}

data class SimpleGetItemInstancesResponse(
    val id: String,
    val name: String,
    val amount: BigDecimal,
    val unitId: Int, // Should be refactored to use UUID
    val expiryDate: LocalDate?
){
    companion object {
        fun fromItemInstance(itemInstance: ItemInstance, item: Item) = SimpleGetItemInstancesResponse (
            id = itemInstance.id,
            name = item.name,
            amount = itemInstance.amount,
            unitId = item.unitId,
            expiryDate = itemInstance.expiryDate
        )
    }
}

data class CreateItemInstanceRequest (
    val name: String,
    val description: String?,
    val typeId: Int,
    val unitId: Int,
    val storageId: String,
    val amount: BigDecimal,
    val expiryDate: LocalDate?
)

data class DeleteItemInstancesRequest(
    val instances: List<String>
)

data class DeleteItemInstancesResponse (
    val itemInstancesDeleted: Int
)