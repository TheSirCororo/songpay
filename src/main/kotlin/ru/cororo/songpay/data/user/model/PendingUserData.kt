package ru.cororo.songpay.data.user.model

import java.time.Instant

data class PendingUserData(
    val login: String,
    val email: String,
    val hashedPassword: String,
    val expireDate: Instant,
)
