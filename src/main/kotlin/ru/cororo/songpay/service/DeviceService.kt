package ru.cororo.songpay.service

import com.maxmind.geoip2.DatabaseReader
import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Service
import ru.cororo.songpay.data.auth.repository.RefreshTokenRepo
import ru.cororo.songpay.data.device.model.DeviceMetadata
import ru.cororo.songpay.data.device.repository.DeviceMetadataRepo
import ru.cororo.songpay.data.user.model.User
import ua_parser.Parser
import java.net.InetAddress
import java.time.Instant
import kotlin.jvm.optionals.getOrNull

const val UNKNOWN = "UNKNOWN"

@Service
class DeviceService(
    private val databaseReader: DatabaseReader,
    private val uapParser: Parser,
    private val deviceMetadataRepo: DeviceMetadataRepo,
    private val refreshTokenRepo: RefreshTokenRepo
) {
    fun getDeviceMetadata(user: User, request: HttpServletRequest): DeviceMetadata {
        val location = request.extractDeviceLocation()
        val deviceDetails = request.extractDeviceDetails()
        return deviceMetadataRepo.findByUserAndDeviceDetailsAndLocation(user, deviceDetails, location).orElseGet {
            deviceMetadataRepo.save(
                DeviceMetadata(
                    0,
                    user,
                    deviceDetails,
                    location,
                    Instant.now()
                )
            )
        }
    }

    fun invalidateDevice(deviceId: Long) = deviceMetadataRepo.deleteById(deviceId)

    fun saveDevice(deviceMetadata: DeviceMetadata) = deviceMetadataRepo.save(deviceMetadata)

    fun getUserActiveDevices(user: User): List<DeviceMetadata> {
        val activeDevices = refreshTokenRepo.findByUserId(user.id)
        return activeDevices.mapNotNull { deviceMetadataRepo.findById(it.deviceId).getOrNull() }
    }

    private fun HttpServletRequest.extractDeviceLocation(): String {
        return (getHeader("X-Forwarded-For")?.substringBefore(",") ?: remoteAddr).locationFromIp()
    }

    private fun String.locationFromIp(): String {
        val inetAddress = InetAddress.getByName(this)
        val location = databaseReader.city(inetAddress)
        return if (location != null && location.city != null && location.country != null) {
            "${location.country.name} ${location.city.name}"
        } else {
            UNKNOWN
        }
    }

    private fun HttpServletRequest.extractDeviceDetails() = getHeader("User-Agent").extractDeviceDetails()

    private fun String.extractDeviceDetails(): String {
        val client = uapParser.parse(this)
        return if (client != null) {
            "${client.userAgent.family} ${client.userAgent.major}.${client.userAgent.minor} - ${client.os.family} ${client.os.major}.${client.os.minor}"
        } else {
            UNKNOWN
        }
    }
}


