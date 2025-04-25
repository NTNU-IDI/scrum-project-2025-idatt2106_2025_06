package edu.ntnu.idatt2106.gr6.backend.controller

import edu.ntnu.idatt2106.gr6.backend.DTOs.ItemInstancesResponse
import edu.ntnu.idatt2106.gr6.backend.service.ItemInstanceService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/item-instances")
class ItemInstanceController(
    private val itemInstanceService: ItemInstanceService
) {

    @GetMapping
    fun getItemInstancesForCurrentUser(): List<ItemInstancesResponse> {
        return itemInstanceService.getItemInstancesForCurrentUser()
    }
}
