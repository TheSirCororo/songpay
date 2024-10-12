package ru.cororo.songpay.common.auth.request

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
data class ConfirmEmailRequest(
    @Contextual
    val verifyCode: Uuid,
)