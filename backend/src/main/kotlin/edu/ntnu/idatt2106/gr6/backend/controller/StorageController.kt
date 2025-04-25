package edu.ntnu.idatt2106.gr6.backend.controller

import edu.ntnu.idatt2106.gr6.backend.DTOs.CreateStorageRequest
import edu.ntnu.idatt2106.gr6.backend.DTOs.StorageResponse
import edu.ntnu.idatt2106.gr6.backend.model.Role.Companion.ROLE_ADMIN
import edu.ntnu.idatt2106.gr6.backend.service.StorageService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/storages")
@PreAuthorize("hasAuthority('CREATE_STORAGE')")
class StorageController(
    private val storageService: StorageService
) {

    @PostMapping
    fun createStorage(@RequestBody request: CreateStorageRequest): ResponseEntity<StorageResponse> {
        val response = storageService.createStorage(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }
}
