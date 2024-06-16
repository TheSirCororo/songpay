package ru.cororo.songpay.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import ru.cororo.songpay.entrypoint.AuthenticationEntryPointResolver
import ru.cororo.songpay.filter.JwtAuthenticationFilter

val SECURITY_WHITELISTED_URLS =
    arrayOf(
        "/api/auth/**",
    )

@Configuration
@EnableWebSecurity(debug = true)
class SecurityConfig(
    private val authenticationProvider: AuthenticationProvider,
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val authenticationEntryPointResolver: AuthenticationEntryPointResolver,
) {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain =
        http
            .csrf { it.disable() }
            .cors {
                it.configurationSource {
                    CorsConfiguration().applyPermitDefaultValues().apply {
                        allowedMethods = listOf("*")
                    }
                }
            }.authorizeHttpRequests {
                SECURITY_WHITELISTED_URLS.forEach { url -> it.requestMatchers(url).permitAll() }
                it.anyRequest().authenticated()
            }.exceptionHandling {
                it
                    .accessDeniedHandler { _, response, _ ->
                        response.sendError(
                            401,
                            "You must be authorized to access this endpoint",
                        )
                    }.authenticationEntryPoint(authenticationEntryPointResolver)
            }.sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .build()
}
