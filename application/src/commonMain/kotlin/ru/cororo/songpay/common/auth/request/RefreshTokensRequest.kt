package ru.cororo.songpay.common.auth.request

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokensRequest(
    val refreshToken: String
)