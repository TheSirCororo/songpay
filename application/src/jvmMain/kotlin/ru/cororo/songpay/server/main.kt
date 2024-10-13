package ru.cororo.songpay.server

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.netty.EngineMain
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import org.koin.dsl.module
import org.koin.ktor.plugin.koin
import org.koin.logger.slf4jLogger
import ru.cororo.songpay.common.user.service.UserService
import ru.cororo.songpay.server.persistence.configurePersistence
import ru.cororo.songpay.server.routing.configureRouting
import ru.cororo.songpay.server.user.service.ServerUserService
import ru.cororo.songpay.server.validation.configureValidation

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    configureNegotiation()
    configurePersistence()
    configureInjection()
    configureValidation()
    configureRouting()
}

private val appModule = module {
    single<UserService> { ServerUserService }
}

private fun Application.configureInjection() {
    koin {
        slf4jLogger()
        modules(appModule)
    }
}

private fun Application.configureNegotiation() {
    install(ContentNegotiation) {
        json()
    }
}
