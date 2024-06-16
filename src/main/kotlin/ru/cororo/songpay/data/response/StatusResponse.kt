package ru.cororo.songpay.data.response

sealed class StatusResponse(
    val status: String,
) {
    class Ok : StatusResponse("ok")
}
