package ru.cororo.songpay.controller

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import ru.cororo.songpay.data.device.model.DeviceMetadata
import ru.cororo.songpay.data.response.StatusResponse
import ru.cororo.songpay.data.user.model.User
import ru.cororo.songpay.service.DeviceService

@RestController("/api/device")
class DeviceController(private val deviceService: DeviceService) {
    @GetMapping("/")
    fun getDevices(@AuthenticationPrincipal user: User): List<DeviceMetadata> {
        return deviceService.getUserActiveDevices(user)
    }

    @DeleteMapping("/{id}")
    fun deleteDevice(@AuthenticationPrincipal user: User, @PathVariable id: Long): StatusResponse {
        deviceService.invalidateDevice(id)
        return StatusResponse()
    }
}