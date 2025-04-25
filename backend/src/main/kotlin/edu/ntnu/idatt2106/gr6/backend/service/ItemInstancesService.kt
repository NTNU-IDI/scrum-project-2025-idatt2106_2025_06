package edu.ntnu.idatt2106.gr6.backend.service

import edu.ntnu.idatt2106.gr6.backend.DTOs.ItemInstancesResponse
import edu.ntnu.idatt2106.gr6.backend.repository.ItemInstanceRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class ItemInstanceService(
    private val itemInstanceRepository: ItemInstanceRepository,
    private val userContextService: UserContextService
) {

    fun getItemInstancesForCurrentUser(): List<ItemInstancesResponse> {
        val userId: UUID = userContextService.getCurrentUserId()
        return itemInstanceRepository.findByUserId(userId)
    }
}
