package edu.ntnu.idatt2106.gr6.backend.service

import edu.ntnu.idatt2106.gr6.backend.DTOs.CreateItemInstanceRequest
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

    /**
     * Creates an item instance.
     * Reuses an existing item if one with the same name already exists, otherwise creates a new item first.
     */
    @Transactional
    fun createItemAndInstance(request: CreateItemInstanceRequest): ItemInstance {
        val now = Instant.now()

        // Try to find an existing item with the same name
        val existingItem = itemRepository.findItemByName(request.name)

        val item = existingItem ?: itemRepository.saveItem(
            name = request.name,
            typeId = request.typeId,
            unitId = request.unitId
        )

        if (item==null) {
            throw SQLException("Item with name ${request.name} not found")
        }

        // Create item instance linked to the item
        return itemRepository.saveItemInstance(
            itemId = item.id,
            storageId = request.storageId,
            amount = request.amount,
            expiryDate = request.expiryDate
        )
    }

    fun getStorageItemsHumanReadable(storageId: String): List<StorageItemResponse> {
        val itemInstances = itemRepository.findStorageItemInstances(storageId)

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


    @Transactional
    fun deleteItemInstance(instanceId: String): Boolean {
        return itemRepository.deleteItemInstanceById(instanceId)
    }

}
