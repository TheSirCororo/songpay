package ru.cororo.songpay.common.core.response

import kotlinx.serialization.Serializable

@Serializable
sealed interface StatusResponse {
    val message: String
    val statusCode: Int

    @Serializable
    data class Ok(override val message: String) : StatusResponse {
        override val statusCode: Int = 0
    }

    @Serializable
    data class Error(override val message: String, override val statusCode: Int) : StatusResponse
}
