package ru.cororo.songpay.controller

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ru.cororo.songpay.data.auth.request.ChangePasswordRequest
import ru.cororo.songpay.data.response.StatusResponse
import ru.cororo.songpay.data.user.model.User
import ru.cororo.songpay.data.user.request.UpdateMeRequest
import ru.cororo.songpay.service.AuthService
import ru.cororo.songpay.service.UserService

@RestController("/api/user")
class UserController(
    private val authService: AuthService,
    private val userService: UserService
) {
    @PostMapping("/change-password")
    fun changePassword(
        @RequestBody requestBody: ChangePasswordRequest,
        @AuthenticationPrincipal user: User,
    ): StatusResponse {
        authService.changePassword(user, requestBody.oldPassword, requestBody.newPassword)
        return StatusResponse()
    }

    @GetMapping("/me")
    fun getMe(@AuthenticationPrincipal user: User): User {
        return user
    }

    @PatchMapping("/me")
    fun updateMe(@AuthenticationPrincipal user: User, request: UpdateMeRequest): User {
        return userService.updateUser(user, request)
    }
}