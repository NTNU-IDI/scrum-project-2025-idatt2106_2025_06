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

    @Transactional
    fun createItemAndItemInstance(request: CreateItemInstanceRequest): ItemInstance {
        val now = Instant.now()

        val item = itemRepository.createItem(
            name = request.name,
            typeId = request.typeId,
            unitId = request.unitId
        )

        return itemRepository.saveItemInstance(
            itemId = item.id,
            storageId = request.storageId,
            amount = request.amount,
            expiryDate = request.expiryDate
        )
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


    @Transactional
    fun deleteItemInstances(instanceIds: List<String>) {
        if (instanceIds.isNotEmpty()) {
            itemRepository.deleteItemInstancesByIds(instanceIds)
        }
    }

}
