package ru.cororo.songpay.data.auth.model

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.index.Indexed
import java.time.Instant

@RedisHash("RefreshToken")
data class RefreshToken(
    @Id
    val token: String,
    @Indexed
    val userId: Long,
    val expiredAt: Instant,
    @Indexed
    val deviceId: Long,
)
