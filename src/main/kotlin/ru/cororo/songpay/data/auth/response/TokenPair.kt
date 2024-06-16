package ru.cororo.songpay.data.auth.response

data class TokenPair(
    val accessToken: String,
    val refreshToken: String,
)
