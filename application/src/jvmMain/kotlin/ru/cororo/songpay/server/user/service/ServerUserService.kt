package ru.cororo.songpay.server.user.service

import ru.cororo.songpay.common.user.model.User
import ru.cororo.songpay.common.user.service.UserService
import ru.cororo.songpay.server.user.persistence.UserPersistence
import kotlin.uuid.Uuid

object ServerUserService : UserService {
    override suspend fun getUserById(userId: Uuid): User? = UserPersistence.getById(userId)
}