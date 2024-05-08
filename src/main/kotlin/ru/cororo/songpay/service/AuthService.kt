package ru.cororo.songpay.service

import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import ru.cororo.songpay.data.auth.model.RefreshToken
import ru.cororo.songpay.data.auth.model.UserCredentials
import ru.cororo.songpay.data.auth.repository.RefreshTokenRepo
import ru.cororo.songpay.data.auth.repository.UserCredentialsRepo
import ru.cororo.songpay.data.auth.response.TokenPair
import ru.cororo.songpay.data.device.model.DeviceMetadata
import ru.cororo.songpay.data.response.ErrorResponses
import ru.cororo.songpay.data.response.respondError
import ru.cororo.songpay.data.user.model.PendingUserData
import ru.cororo.songpay.data.user.model.User
import ru.cororo.songpay.data.user.repository.UserRepo
import ru.cororo.songpay.util.plus
import java.time.Instant
import java.util.UUID
import kotlin.time.Duration.Companion.seconds

@Service
class AuthService(
    private val refreshTokenRepo: RefreshTokenRepo,
    private val userRepo: UserRepo,
    private val authenticationManager: AuthenticationManager,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val deviceService: DeviceService,
    private val userCredentialsRepo: UserCredentialsRepo,
    private val emailService: EmailService,
    @Value("\${auth.pending_duration}")
    private val pendingUserDuration: Long
) {
    private val pendingRegistrations = mutableMapOf<UUID, PendingUserData>()

    fun signIn(loginOrEmail: String, password: String, request: HttpServletRequest): TokenPair {
        val auth = try {
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(loginOrEmail, password)
            )
        } catch (e: AuthenticationException) {
            respondError(ErrorResponses.AuthFailed)
        }

        val user = auth.principal as User
        val deviceMetadata = deviceService.getDeviceMetadata(user, request)
        deviceMetadata.lastLoggedIn = Instant.now()
        deviceService.saveDevice(deviceMetadata)

        return generateTokenPair(user, deviceMetadata)
    }

    fun register(pendingId: UUID): User {
        val pendingUser = pendingRegistrations[pendingId] ?: respondError(ErrorResponses.RegistrationNotFound)
        pendingRegistrations.remove(pendingId)
        if (userRepo.existsByLoginIgnoreCaseOrEmailIgnoreCase(pendingUser.login, pendingUser.email)) {
            respondError(ErrorResponses.UserAlreadyExists)
        }

        val user = User(
            0,
            pendingUser.login,
            pendingUser.email,
            pendingUser.login,
            UserCredentials(
                0,
                pendingUser.hashedPassword,
                Instant.now()
            )
        )

        return user
    }

    fun refreshToken(request: HttpServletRequest): TokenPair {
        val authHeader = request.getHeader("Authorization")
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            respondError(ErrorResponses.IncorrectRequest)
        }

        val jwt = authHeader.substring(7)
        val login = try {
            jwtService.extractUsername(jwt)!!
        } catch (e: Exception) {
            respondError(ErrorResponses.RefreshTokenFailed)
        }

        val user = userRepo.findByLoginIgnoreCaseOrEmailIgnoreCase(login, login).orElseGet {
            respondError(ErrorResponses.AuthUserNotFound)
        }

        refreshTokenRepo.removeByToken(jwt)

        val deviceMetadata = deviceService.getDeviceMetadata(user, request)
        return generateTokenPair(user, deviceMetadata)
    }

    fun changePassword(user: User, oldPassword: String, newPassword: String) {
        if (!passwordEncoder.matches(oldPassword, user.password)) {
            respondError(ErrorResponses.OldPasswordNotMatch)
        }

        refreshTokenRepo.removeByUserId(user.id)
        user.credentials.hashedPassword = passwordEncoder.encode(newPassword)
        user.credentials.lastChanged = Instant.now()
        userCredentialsRepo.save(user.credentials)
    }

    fun sendConfirmationLetter(login: String, email: String, password: String) {
        val userData = PendingUserData(login, email, passwordEncoder.encode(password), Instant.now() + pendingUserDuration.seconds)
        if (pendingRegistrations.containsValue(userData)) {
            respondError(ErrorResponses.IncorrectRequest)
        }

        val id = UUID.randomUUID()
        pendingRegistrations.entries.removeIf { (_, value) -> value.email == email }
        pendingRegistrations[id] = userData
        emailService.sendConfirmationMessage(email, id)
    }

    private fun generateTokenPair(user: User, deviceMetadata: DeviceMetadata): TokenPair {
        val accessToken = jwtService.generateAccessToken(user)
        val refreshToken = jwtService.generateRefreshToken(user)

        val expiry = jwtService.extractClaim(refreshToken) { it.expiration }!!

        refreshTokenRepo.removeByUserIdAndDeviceId(user.id, deviceMetadata.id)
        refreshTokenRepo.save(RefreshToken(refreshToken, user.id, expiry.toInstant(), deviceMetadata.id))

        return TokenPair(accessToken, refreshToken)
    }
}