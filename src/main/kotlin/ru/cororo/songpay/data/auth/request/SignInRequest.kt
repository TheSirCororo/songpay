package ru.cororo.songpay.data.auth.request

data class SignInRequest(
    val login: String,
    val password: String,
)
