package edu.ntnu.idatt2106.gr6.backend.DTOs

import edu.ntnu.idatt2106.gr6.backend.model.ItemInstance
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

data class ItemInstanceResponse (
    val id: String,
    val itemId: String,
    val storageId: String,
    val amount: BigDecimal,
    val expiryDate: LocalDate?
){
    companion object {
        fun fromItemInstance(item: ItemInstance) = ItemInstanceResponse(
            id = item.id,
            itemId = item.itemId,
            storageId = item.storageId,
            amount = item.amount,
            expiryDate = item.expiryDate
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

data class StorageItemResponse(
    val id: String,
    val name: String,
    val amount: BigDecimal,
    val unit: Int,
    val expiryDate: LocalDate?
)

data class DeleteItemInstanceRequest(
    val instances: List<String>
)