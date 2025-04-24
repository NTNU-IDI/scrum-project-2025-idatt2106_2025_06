package edu.ntnu.idatt2106.gr6.backend.controller

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController {
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
}