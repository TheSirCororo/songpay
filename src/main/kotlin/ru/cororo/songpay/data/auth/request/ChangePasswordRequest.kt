package ru.cororo.songpay.data.auth.request

import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class ChangePasswordRequest(
    val oldPassword: String,
    @Size(min = 8, message = "Min length of password is 8")
    @Pattern(
        regexp = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])^[^ ]+\$",
        message = "Password must has uppercase, lowercase character and digit without spaces",
    )
    val newPassword: String,
)
