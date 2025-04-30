package edu.ntnu.idatt2106.gr6.backend.service

import edu.ntnu.idatt2106.gr6.backend.DTOs.CreateItemInstanceRequest
import edu.ntnu.idatt2106.gr6.backend.DTOs.ItemInstanceResponse
import edu.ntnu.idatt2106.gr6.backend.DTOs.StorageItemResponse
import edu.ntnu.idatt2106.gr6.backend.model.Item
import edu.ntnu.idatt2106.gr6.backend.model.ItemInstance
import edu.ntnu.idatt2106.gr6.backend.repository.ItemRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.sql.SQLException
import java.time.Instant

@Service
class ItemService(
    private val itemRepository: ItemRepository
) {

    @Transactional
    fun createItemAndItemInstance(request: CreateItemInstanceRequest): ItemInstanceResponse {

        val item = itemRepository.createItem(
            name = request.name,
            typeId = request.typeId,
            unitId = request.unitId
        )

        val itemInstance =  itemRepository.saveItemInstance(
            itemId = item.id,
            storageId = request.storageId,
            amount = request.amount,
            expiryDate = request.expiryDate
        )

        return ItemInstanceResponse.fromItemInstance(itemInstance)
    }

    @Transactional
    fun deleteItemInstances(instanceIds: List<String>): Int {
        if (instanceIds.isNotEmpty()) {
            return itemRepository.deleteItemInstancesByIds(instanceIds)
        }
        return 0
    }

    fun getStorageItemsHumanReadable(storageId: String, typeId: String): List<StorageItemResponse> {
        val itemInstances = itemRepository.findStorageItemInstances(storageId, typeId)

        return itemInstances.map { instance ->
            // Fetch extra details if needed (name, unit, etc.)
            val item = itemRepository.findItemById(instance.itemId)
                ?: throw IllegalStateException("Item not found for id=${instance.itemId}")

            StorageItemResponse(
                id = instance.id,
                name = item.name,
                amount = instance.amount,
                unit = item.unitId,
                expiryDate = instance.expiryDate
            )
        }
    }




}
