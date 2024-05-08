package ru.cororo.songpay.data.auth.request

data class SignInRequest(
    val loginOrEmail: String,
    val password: String
)