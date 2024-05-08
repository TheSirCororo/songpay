package ru.cororo.songpay.data.device.model

import jakarta.persistence.*
import ru.cororo.songpay.data.user.model.User
import java.time.Instant

@Entity
@Table(name = "user_devices_metadata")
data class DeviceMetadata(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "device_id")
    val user: User,

    val deviceDetails: String,

    val location: String,

    var lastLoggedIn: Instant
)