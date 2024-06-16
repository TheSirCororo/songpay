package ru.cororo.songpay.data.device.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.cororo.songpay.data.device.model.DeviceMetadata
import ru.cororo.songpay.data.user.model.User
import java.util.*

@Repository
interface DeviceMetadataRepo : JpaRepository<DeviceMetadata, Long> {
    fun findByUser(user: User): List<DeviceMetadata>

    fun findByUserAndDeviceDetailsAndLocation(
        user: User,
        deviceDetails: String,
        location: String,
    ): Optional<DeviceMetadata>
}
