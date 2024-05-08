package ru.cororo.songpay.config

import com.maxmind.geoip2.DatabaseReader
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import ru.cororo.songpay.data.user.repository.UserRepo
import ua_parser.Parser
import java.io.File
import kotlin.jvm.optionals.getOrElse

@Configuration
@PropertySource("classpath:application.properties")
class ApplicationConfig(
    private val userRepo: UserRepo,
    @Value("\${device.geolite2location}")
    private val geolite2Location: String
) {
    @Bean
    fun userDetailsService(): UserDetailsService {
        return UserDetailsService { login ->
            userRepo.findByLoginIgnoreCaseOrEmailIgnoreCase(login, login).getOrElse {
                throw UsernameNotFoundException("User not found")
            }
        }
    }

    @Bean
    fun authenticationProvider(): AuthenticationProvider {
        return DaoAuthenticationProvider().apply {
            setUserDetailsService(userDetailsService())
            setPasswordEncoder(passwordEncoder())
        }
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun authenticationManager(
        authenticationConfiguration: AuthenticationConfiguration
    ): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }

    @Bean
    fun databaseReader(): DatabaseReader {
        return DatabaseReader.Builder(File(geolite2Location)).build()
    }

    @Bean
    fun uapParser(): Parser {
        return Parser()
    }
}