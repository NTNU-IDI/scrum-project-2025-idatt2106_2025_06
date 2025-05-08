package edu.ntnu.idatt2106.gr6.backend.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * Global exception handler for the application.
 *
 * This class handles custom exceptions thrown by the application and returns appropriate HTTP responses.
 */

@RestControllerAdvice
class GlobalExceptionHandler {

    //USERS

    @ExceptionHandler(UserAlreadyExistsException::class)
    fun handleUserAlreadyExistsException(ex: UserAlreadyExistsException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.message)
    }

    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFoundException(ex: UserNotFoundException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.message)
    }

    @ExceptionHandler(UserNotAuthorizedException::class)
    fun handleUserNotAuthorizedException(ex: UserNotAuthorizedException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.message)
    }

    //AUTH

    @ExceptionHandler(InvalidCredentialsException::class)
    fun handleInvalidCredentialsException(ex: InvalidCredentialsException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.message)
    }

    @ExceptionHandler(AuthenticationException::class)
    fun handleAuthenticationException(ex: AuthenticationException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.message)
    }

    @ExceptionHandler(InvalidUserDetailsException::class)
    fun handleInvalidUserDetailsException(ex: InvalidUserDetailsException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.message)
    }

    @ExceptionHandler(UserEmailNotVerifiedException::class)
    fun handleUserEmailNotVerifiedException(ex: UserEmailNotVerifiedException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.message)
    }

    @ExceptionHandler(TokenIncorrectlyFormattedException::class)
    fun handleTokenIncorrectlyFormattedException(ex: TokenIncorrectlyFormattedException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.message)
    }

    @ExceptionHandler(TokenInvalidException::class)
    fun handleTokenInvalidException(ex: TokenInvalidException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.message)
    }
    //EVENTS

    @ExceptionHandler(EventDoesNotExistException::class)
    fun handleEventDoesNotExistException(ex: EventDoesNotExistException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.message)
    }

    //ITEMS

    @ExceptionHandler(ItemNotFoundException::class)
    fun handleItemNotFoundException(ex: ItemNotFoundException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.message)
    }

    //LOCATIONS
    @ExceptionHandler(LocationNotAvailableException::class)
    fun handleLocationNotAvailableException(ex: LocationNotAvailableException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.message)
    }

    @ExceptionHandler(UserMismatchException::class)
    fun handleUserMismatchException(ex: UserMismatchException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.message)
    }

    @ExceptionHandler(UserLocationDisabledException::class)
    fun handleUserLocationDisabledException(ex: UserLocationDisabledException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.message)
    }

    //MARKERS
    @ExceptionHandler(MarkerNotFoundException::class)
    fun handleMarkerNotFoundException(ex: MarkerNotFoundException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.message)
    }

    //DATABASE
    @ExceptionHandler(DatabaseNoRowsAffectedException::class)
    fun handleDatabaseNoRowsAffectedException(ex: DatabaseNoRowsAffectedException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.message)
    }

    @ExceptionHandler(UserFailedToUpdateCredentialsException::class)
    fun handleUserFailedToUpdateCredentialsException(ex: UserFailedToUpdateCredentialsException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.message)
    }
}