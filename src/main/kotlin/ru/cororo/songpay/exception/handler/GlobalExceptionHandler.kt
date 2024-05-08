package ru.cororo.songpay.exception.handler

import jakarta.annotation.Priority
import jakarta.validation.ConstraintViolationException
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.authentication.CredentialsExpiredException
import org.springframework.security.authentication.InsufficientAuthenticationException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import ru.cororo.songpay.data.response.ErrorResponse
import ru.cororo.songpay.data.response.ErrorResponses
import ru.cororo.songpay.data.response.StatusCodeException
import ru.cororo.songpay.data.response.toResponseEntity

@Priority(1000)
@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(InsufficientAuthenticationException::class)
    fun incorrectCredentialsException(ex: InsufficientAuthenticationException): ResponseEntity<*> {
        return ErrorResponses.AuthFailed.toResponseEntity()
    }

    @ExceptionHandler(UsernameNotFoundException::class)
    fun usernameNotFoundException(ex: UsernameNotFoundException): ResponseEntity<*> {
        return ErrorResponses.AuthLoginNotFound.toResponseEntity()
    }

    @ExceptionHandler(CredentialsExpiredException::class)
    fun credentialsExpiredException(ex: CredentialsExpiredException): ResponseEntity<*> {
        return ErrorResponses.CredentialsExpired.toResponseEntity()
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun httpMessageNotReadable(ex: HttpMessageNotReadableException): ResponseEntity<*> {
        return ErrorResponses.HttpMessageNotReadable.toResponseEntity()
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolationException(ex: ConstraintViolationException): Any {
        return ErrorResponses.ConstraintViolation(ex.constraintViolations)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleGlobalValidationExceptions(ex: MethodArgumentNotValidException): Any {
        return ErrorResponses.MethodArgumentNotValid(ex.bindingResult.fieldErrors)
    }

    @ExceptionHandler(StatusCodeException::class)
    fun handleStatusCodeException(e: StatusCodeException): ResponseEntity<ErrorResponse> {
        return e.errorResponse.toResponseEntity()
    }
}