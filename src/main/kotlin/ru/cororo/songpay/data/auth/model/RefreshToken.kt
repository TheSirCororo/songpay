package ru.cororo.songpay.data.auth.model

import jakarta.persistence.Id
import org.springframework.data.redis.core.RedisHash
import java.time.Instant

@RedisHash("RefreshToken")
data class RefreshToken(
    @Id
    val token: String,
    val userId: Long,
    val expiredAt: Instant,
    val deviceId: Long
)