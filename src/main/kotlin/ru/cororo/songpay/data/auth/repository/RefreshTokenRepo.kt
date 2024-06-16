package ru.cororo.songpay.data.auth.repository

import org.springframework.data.keyvalue.repository.KeyValueRepository
import org.springframework.stereotype.Repository
import ru.cororo.songpay.data.auth.model.RefreshToken

@Repository
interface RefreshTokenRepo : KeyValueRepository<RefreshToken, String> {
    fun findByDeviceId(deviceId: Long): List<RefreshToken>

    fun findByUserId(userId: Long): List<RefreshToken>
}

fun RefreshTokenRepo.deleteAllByUserId(userId: Long) {
    findByUserId(userId).forEach {
        delete(it)
    }
}

fun RefreshTokenRepo.deleteAllByDeviceId(deviceId: Long) {
    findByDeviceId(deviceId).forEach {
        delete(it)
    }
}
