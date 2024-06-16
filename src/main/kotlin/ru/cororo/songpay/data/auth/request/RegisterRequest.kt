package ru.cororo.songpay.data.auth.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class RegisterRequest(
    @Size(min = 4, max = 32, message = "Login length must be more than 4 and less than 32 characters.")
    val login: String,
    @Email
    val email: String,
    @Size(min = 8, max = 128, message = "Min length of password is 8")
    @Pattern(
        regexp = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])^[^ ]+\$",
        message = "Password must has uppercase, lowercase character and digit without spaces",
    )
    val password: String,
)
