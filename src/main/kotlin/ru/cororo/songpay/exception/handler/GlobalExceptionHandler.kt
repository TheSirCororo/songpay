package ru.cororo.songpay.exception.handler

import jakarta.validation.ConstraintViolationException
import org.springframework.core.annotation.Order
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

@Order(1000)
@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(InsufficientAuthenticationException::class)
    fun incorrectCredentialsException(ex: InsufficientAuthenticationException) = ErrorResponses.AuthFailed.toResponseEntity()

    @ExceptionHandler(UsernameNotFoundException::class)
    fun usernameNotFoundException(ex: UsernameNotFoundException) = ErrorResponses.AuthLoginNotFound.toResponseEntity()

    @ExceptionHandler(CredentialsExpiredException::class)
    fun credentialsExpiredException(ex: CredentialsExpiredException) = ErrorResponses.CredentialsExpired.toResponseEntity()

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun httpMessageNotReadable(ex: HttpMessageNotReadableException) = ErrorResponses.HttpMessageNotReadable(ex).toResponseEntity()

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolationException(ex: ConstraintViolationException) =
        ErrorResponses.ConstraintViolation(ex.constraintViolations).toResponseEntity()

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleGlobalValidationExceptions(ex: MethodArgumentNotValidException) =
        ErrorResponses.MethodArgumentNotValid(ex.bindingResult.fieldErrors).toResponseEntity()

    @ExceptionHandler(StatusCodeException::class)
    fun handleStatusCodeException(e: StatusCodeException): ResponseEntity<ErrorResponse> = e.errorResponse.toResponseEntity()
}
