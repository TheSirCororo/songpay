package ru.cororo.songpay.data.response

import jakarta.validation.ConstraintViolation
import org.springframework.validation.FieldError

object ErrorResponses {
    val AuthFailed = ErrorResponse("Provided bad credentials or user not exists", 1, 401)
    val AuthLoginNotFound = ErrorResponse("Provided unexistent login", 2, 401)
    val CredentialsExpired = ErrorResponse("Provided credentials expired", 3, 401)
    val HttpMessageNotReadable = ErrorResponse("Http message is not readable", 4, 400)
    val ConstraintViolation: (Set<ConstraintViolation<*>>) -> ErrorResponse = {
        ErrorResponse(
            "Constraint violations: ${
                it.joinToString("; ") { violation -> violation.message }
            }", 5, 400
        )
    }
    val MethodArgumentNotValid: (List<FieldError>) -> ErrorResponse = {
        ErrorResponse(
            "Method arguments invalid: ${
                it.joinToString("; ") { error -> error.defaultMessage ?: "" }
            }",
            6,
            400
        )
    }
    val UserAlreadyExists = ErrorResponse("User already exists", 7, 409)
    val RefreshTokenFailed = ErrorResponse("Refresh token is incorrect", 8, 401)
    val IncorrectRequest = ErrorResponse("Passed incorrect request (inccorect body or headers)", 9, 400)
    val AuthUserNotFound = ErrorResponse("User not found", 10, 401)
    val OldPasswordNotMatch = ErrorResponse("Old password does not match", 10, 401)
    val RegistrationNotFound = ErrorResponse("Registration not found", 11, 400)
    val DeviceNotFound = ErrorResponse("Device not found", 12, 404)
}