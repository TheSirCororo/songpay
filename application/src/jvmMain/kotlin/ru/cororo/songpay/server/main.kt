package ru.cororo.songpay.server

import io.ktor.server.application.Application
import io.ktor.server.netty.EngineMain
import ru.cororo.songpay.server.persistence.configurePersistence
import ru.cororo.songpay.server.plugins.configureContentNegotiation
import ru.cororo.songpay.server.plugins.configureCors
import ru.cororo.songpay.server.plugins.configureKoin
import ru.cororo.songpay.server.plugins.configureRouting
import ru.cororo.songpay.server.plugins.configureStatusPages
import ru.cororo.songpay.server.plugins.configureValidation

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    configureContentNegotiation()
    configurePersistence()
    configureKoin()
    configureValidation()
    configureStatusPages()
    configureRouting()
    configureCors()
}
