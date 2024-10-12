package ru.cororo.songpay.common.user.service

import ru.cororo.songpay.common.user.model.User
import kotlin.uuid.Uuid

interface UserService {
    suspend fun getUserById(userId: Uuid): User?
}