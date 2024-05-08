package ru.cororo.songpay.data.auth.repository

import org.springframework.data.keyvalue.repository.KeyValueRepository
import org.springframework.stereotype.Repository
import ru.cororo.songpay.data.auth.model.RefreshToken
import java.util.*

@Repository
interface RefreshTokenRepo : KeyValueRepository<RefreshToken, String> {
    fun removeByUserId(userId: Long)

    fun removeByUserIdAndDeviceId(userId: Long, deviceId: Long)

    fun findByUserIdAndDeviceId(userId: Long, deviceId: Long): Optional<RefreshToken>

    fun findByUserId(userId: Long): List<RefreshToken>

    fun removeByToken(token: String)
}