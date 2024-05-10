package ru.cororo.songpay.controller

import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.*
import ru.cororo.songpay.data.auth.request.RegisterRequest
import ru.cororo.songpay.data.auth.request.SignInRequest
import ru.cororo.songpay.data.auth.response.TokenPair
import ru.cororo.songpay.data.response.ErrorResponses
import ru.cororo.songpay.data.response.StatusResponse
import ru.cororo.songpay.data.response.respondError
import ru.cororo.songpay.data.user.model.User
import ru.cororo.songpay.service.AuthService
import java.util.*

@RestController("/api/auth")
class AuthController(private val authService: AuthService) {
    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): StatusResponse {
        authService.sendConfirmationLetter(request.login, request.email, request.password)
        return StatusResponse()
    }

    @PostMapping("/sign-in")
    fun signIn(@RequestBody signInRequest: SignInRequest, request: HttpServletRequest): TokenPair {
        return authService.signIn(signInRequest.loginOrEmail, signInRequest.password, request)
    }

    @GetMapping("/confirm-email")
    fun confirmEmail(@RequestParam code: String, request: HttpServletRequest): User {
        return try {
            authService.register(UUID.fromString(code))
        } catch (ex: Exception) {
            respondError(ErrorResponses.IncorrectRequest)
        }
    }

    @PostMapping("/refresh")
    fun refreshToken(request: HttpServletRequest): TokenPair {
        return authService.refreshToken(request)
    }

    @PostMapping("/logout")
    fun logout(request: HttpServletRequest): StatusResponse {
        authService.logout(request)
        return StatusResponse()
    }
}