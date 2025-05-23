package edu.ntnu.idatt2106.gr6.backend.controller

import edu.ntnu.idatt2106.gr6.backend.DTOs.CreateItemInstanceRequest
import edu.ntnu.idatt2106.gr6.backend.DTOs.DeleteItemInstancesRequest
import edu.ntnu.idatt2106.gr6.backend.DTOs.DeleteItemInstancesResponse
import edu.ntnu.idatt2106.gr6.backend.DTOs.EditItemInstanceRequest
import edu.ntnu.idatt2106.gr6.backend.DTOs.EditItemInstanceResponse
import edu.ntnu.idatt2106.gr6.backend.DTOs.ItemInstanceResponse
import edu.ntnu.idatt2106.gr6.backend.DTOs.SimpleGetItemInstancesResponse
import edu.ntnu.idatt2106.gr6.backend.DTOs.SimpleItemResponse
import edu.ntnu.idatt2106.gr6.backend.service.ItemService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/items")
@Tag(name = "Items", description = "Item management APIs")
class ItemController(
    private val itemService: ItemService
) {

    private val logger = LoggerFactory.getLogger(ItemController::class.java)


    @GetMapping
    @PreAuthorize("hasAuthority('CREATE_STORAGE')") // !!!!!!!!
    @Operation(summary = "Get all items")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Items retrieved")
        ]
    )
    fun getAllItems(): ResponseEntity<List<SimpleItemResponse>> {
        val items = itemService.getAllItems()
        return ResponseEntity.ok(items)
    }

    /**
     * Endpoint to create a new item instance (and item if necessary).
     */
    @PostMapping("/create-item-instance")
    @PreAuthorize("hasAuthority('CREATE_STORAGE')") // SWAP THIS!!!
    @Operation(summary = "Create Item Instance", description = "Creates a new item instance and reuses or creates the associated item.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Item instance created successfully"),
            ApiResponse(responseCode = "400", description = "Invalid input"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun createItemInstance(
        @RequestBody @Valid request: CreateItemInstanceRequest
    ): ResponseEntity<ItemInstanceResponse> {
        logger.info("Received request to add item instance: $request")

        val createdItemInstance = itemService.createItemAndItemInstance(request)

        logger.info("Created item instance with id: ${createdItemInstance.id}")

        return ResponseEntity.status(HttpStatus.CREATED).body(createdItemInstance)
    }


    @DeleteMapping("/item-instances")
    @PreAuthorize("hasAuthority('CREATE_STORAGE')")
    @Operation(summary = "Delete one or multiple item instances")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Item instances deleted"),
            ApiResponse(responseCode = "400", description = "Invalid input"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun deleteItemInstances(
        @RequestBody request: DeleteItemInstancesRequest
    ): ResponseEntity<DeleteItemInstancesResponse> {
        val deletedCount = itemService.deleteItemInstances(request.instances)
        return ResponseEntity.ok(DeleteItemInstancesResponse(itemInstancesDeleted = deletedCount))
    }


    @GetMapping("/storage/{storageId}/item-instances")
    @PreAuthorize("hasAuthority('CREATE_STORAGE')") // !!!!!!!!!!!!!!!!!
    @Operation(summary = "Get readable item list for a storage")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully retrieved readable item list"),
            ApiResponse(responseCode = "400", description = "Invalid storage ID"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun getItemInstancesByType(
        @PathVariable storageId: String,
        @RequestParam typeId: String
    ): ResponseEntity<List<SimpleGetItemInstancesResponse>> {
        logger.info("Fetching readable items for storage ID: $storageId and type ID: $typeId")
        val itemInstances = itemService.getItemInstancesByType(storageId, typeId)
        return ResponseEntity.ok(itemInstances)
    }


    @PostMapping("/item-instances/{itemInstanceId}/edit")
    @PreAuthorize("hasAuthority('CREATE_STORAGE')") // !!!!!!!
    @Operation(summary = "Edit an already existing item instance")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Item instance edited"),
            ApiResponse(responseCode = "404", description = "Item instance not found")
        ]
    )
    fun editItemInstance(
        @PathVariable itemInstanceId: String,
        @RequestBody request: EditItemInstanceRequest
    ): ResponseEntity<EditItemInstanceResponse> {
        logger.info("Received request to edit item instance: $request")

        val editedItemInstance = itemService.editItemInstance(itemInstanceId, request)

        val response = EditItemInstanceResponse(
            id = editedItemInstance.id,
            amount = editedItemInstance.amount,
            expiryDate = editedItemInstance.expiryDate
        )

        return ResponseEntity.ok(response)
    }
}
