package ru.cororo.songpay.common.user.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
data class User(
    @Contextual
    var id: Uuid?,
    val email: String,
    val username: String,
    val isVerified: Boolean,
)