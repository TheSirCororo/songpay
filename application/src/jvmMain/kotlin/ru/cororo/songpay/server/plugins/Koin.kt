package ru.cororo.songpay.server.plugins

import io.ktor.server.application.Application
import org.koin.dsl.module
import org.koin.ktor.plugin.koin
import org.koin.logger.slf4jLogger
import ru.cororo.songpay.common.user.service.UserService
import ru.cororo.songpay.server.user.service.ServerUserService


private val appModule = module {
    single<UserService> { ServerUserService }
}

fun Application.configureKoin() {
    koin {
        slf4jLogger()
        modules(appModule)
    }
}