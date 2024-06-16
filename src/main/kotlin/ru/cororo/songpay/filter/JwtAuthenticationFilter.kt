package ru.cororo.songpay.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.CredentialsExpiredException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import ru.cororo.songpay.config.SECURITY_WHITELISTED_URLS
import ru.cororo.songpay.data.user.repository.UserRepo
import ru.cororo.songpay.service.JwtService
import ru.cororo.songpay.util.compareTo
import java.util.*
import kotlin.jvm.optionals.getOrElse

@Component
class JwtAuthenticationFilter(
    private val jwtService: JwtService,
    private val userRepo: UserRepo,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        runCatching {
            if (SECURITY_WHITELISTED_URLS.any { request.requestURI.startsWith(it.substringBefore("/**")) }) {
                return filterChain.doFilter(
                    request,
                    response,
                )
            }

            val authHeader = request.getHeader("Authorization")
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw CredentialsExpiredException("")
            }

            val jwt = authHeader.substring(7)

            val login: String?
            val issuedAt: Date?

            try {
                login = jwtService.extractUsername(jwt)
                issuedAt = jwtService.extractClaim(jwt) { claim -> claim.issuedAt }

                val isRefresh =
                    jwtService.extractClaim(jwt) { claim -> return@extractClaim claim.getOrElse("refresh") { false } as Boolean }!!
                if (isRefresh) throw CredentialsExpiredException("")
            } catch (e: Exception) {
                throw CredentialsExpiredException("")
            }

            if (login != null && issuedAt != null && SecurityContextHolder.getContext().authentication == null) {
                val user =
                    userRepo
                        .findByLoginIgnoreCaseOrEmailIgnoreCase(login, login)
                        .getOrElse { throw CredentialsExpiredException("") }

                val isTokenValid = issuedAt >= user.credentials.lastChanged && jwtService.isTokenValid(jwt, user)
                if (!isTokenValid) throw CredentialsExpiredException("")

                val authToken = UsernamePasswordAuthenticationToken(user, null, user.authorities)
                authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authToken
            }
        }

        filterChain.doFilter(request, response)
    }
}
