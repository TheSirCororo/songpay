package ru.cororo.songpay.common.auth.model

import kotlinx.serialization.Serializable

@Serializable
data class TokenPair(val accessToken: String, val refreshToken: String)
