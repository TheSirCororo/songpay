package ru.cororo.songpay.controller

import jakarta.validation.Valid
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import ru.cororo.songpay.data.auth.request.ChangePasswordRequest
import ru.cororo.songpay.data.response.StatusResponse
import ru.cororo.songpay.data.user.model.User
import ru.cororo.songpay.data.user.request.UpdateMeRequest
import ru.cororo.songpay.service.AuthService
import ru.cororo.songpay.service.UserService

@RestController
@RequestMapping("/api/user")
class UserController(
    private val authService: AuthService,
    private val userService: UserService,
) {
    @PostMapping("/change-password")
    fun changePassword(
        @Valid @RequestBody requestBody: ChangePasswordRequest,
        @AuthenticationPrincipal user: User,
    ): StatusResponse {
        authService.changePassword(user, requestBody.oldPassword, requestBody.newPassword)
        return StatusResponse.Ok()
    }

    @GetMapping("/me")
    fun getMe(
        @AuthenticationPrincipal user: User,
    ): User = user

    @PatchMapping("/me")
    fun updateMe(
        @AuthenticationPrincipal user: User,
        @Valid @RequestBody request: UpdateMeRequest,
    ): User = userService.updateUser(user, request)
}
