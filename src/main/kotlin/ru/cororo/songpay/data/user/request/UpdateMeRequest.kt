package ru.cororo.songpay.data.user.request

import jakarta.validation.constraints.Size

data class UpdateMeRequest(
    @Size(min = 4, max = 64, message = "Display name must have at least 4 characters and maximum 64 characters.")
    val displayName: String
)