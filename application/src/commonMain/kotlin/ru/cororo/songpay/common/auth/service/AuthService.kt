package ru.cororo.songpay.common.auth.service

import ru.cororo.songpay.common.auth.model.TokenPair

interface AuthService {
    suspend fun signIn(username: String, password: String): TokenPair

    suspend fun register(email: String, username: String, password: String): TokenPair
}