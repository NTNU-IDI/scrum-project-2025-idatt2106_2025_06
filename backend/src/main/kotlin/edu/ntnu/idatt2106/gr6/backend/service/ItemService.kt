package edu.ntnu.idatt2106.gr6.backend.service

import edu.ntnu.idatt2106.gr6.backend.DTOs.CreateItemInstanceRequest
import edu.ntnu.idatt2106.gr6.backend.DTOs.EditItemInstanceRequest
import edu.ntnu.idatt2106.gr6.backend.DTOs.ItemInstanceResponse
import edu.ntnu.idatt2106.gr6.backend.DTOs.SimpleGetItemInstancesResponse
import edu.ntnu.idatt2106.gr6.backend.DTOs.SimpleItemResponse
import edu.ntnu.idatt2106.gr6.backend.model.ItemInstance
import edu.ntnu.idatt2106.gr6.backend.repository.ItemRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException

@Service
class ItemService(
    private val itemRepository: ItemRepository
) {

    fun getAllItems(): List<SimpleItemResponse> {
        return itemRepository.getAllItems().map { SimpleItemResponse.fromItem(it) }
    }


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

        return ItemInstanceResponse.fromItemInstance(itemInstance, item)
    }

    @Transactional
    fun deleteItemInstances(instanceIds: List<String>): Int {
        if (instanceIds.isNotEmpty()) {
            return itemRepository.deleteItemInstancesByIds(instanceIds)
        }
        return 0
    }

    @Transactional(readOnly = true)
    fun getItemInstancesByType(storageId: String, typeId: String): List<SimpleGetItemInstancesResponse> {
        val itemInstances = itemRepository.getItemInstancesByType(storageId, typeId)

        return itemInstances.map { instance ->
            val item = itemRepository.findItemById(instance.itemId)
                ?: throw IllegalStateException("Item not found for id=${instance.itemId}")

            SimpleGetItemInstancesResponse.fromItemInstance(instance, item)
        }
    }

    fun editItemInstance(itemInstanceId: String, request: EditItemInstanceRequest): ItemInstance {
        val updated = itemRepository.updateItemInstance(itemInstanceId, request.amount, request.expiryDate)
        if (!updated) throw ResponseStatusException(HttpStatus.NOT_FOUND, "ItemInstance is not found")

        return itemRepository.getItemInstanceById(itemInstanceId)
            ?: throw IllegalStateException("Could not retrieve updated item instance")
    }
}
