package ru.cororo.songpay.controller

import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*
import ru.cororo.songpay.data.auth.request.RegisterRequest
import ru.cororo.songpay.data.auth.request.SignInRequest
import ru.cororo.songpay.data.auth.response.TokenPair
import ru.cororo.songpay.data.response.ErrorResponses
import ru.cororo.songpay.data.response.StatusCodeException
import ru.cororo.songpay.data.response.StatusResponse
import ru.cororo.songpay.data.response.respondError
import ru.cororo.songpay.service.AuthService
import java.util.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService,
) {
    @PostMapping("/register")
    fun register(
        @Valid @RequestBody request: RegisterRequest,
    ): StatusResponse {
        authService.sendConfirmationLetter(request.login, request.email, request.password)
        return StatusResponse.Ok()
    }

    @PostMapping("/sign-in")
    fun signIn(
        @RequestBody signInRequest: SignInRequest,
        request: HttpServletRequest,
    ): TokenPair = authService.signIn(signInRequest.login, signInRequest.password, request)

    @GetMapping("/confirm-email")
    fun confirmEmail(
        @RequestParam code: String,
        request: HttpServletRequest,
    ): TokenPair =
        try {
            val user = authService.register(UUID.fromString(code))
            authService.generateTokenPair(user, request)
        } catch (ex: StatusCodeException) {
            throw ex
        } catch (ex: Exception) {
            respondError(ErrorResponses.InternalServerError(ex))
        }

    @PostMapping("/refresh")
    fun refreshToken(request: HttpServletRequest): TokenPair = authService.refreshToken(request)

    @PostMapping("/logout")
    fun logout(request: HttpServletRequest): StatusResponse {
        authService.logout(request)
        return StatusResponse.Ok()
    }
}
