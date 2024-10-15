package ru.cororo.songpay.server.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.cors.routing.CORS

fun Application.configureCors() {
    install(CORS) {
        anyHost()
        anyMethod()
        allowHeaders { true }
        allowNonSimpleContentTypes = true
    }
}
