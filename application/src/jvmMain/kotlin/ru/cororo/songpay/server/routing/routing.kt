package ru.cororo.songpay.server.routing

import io.ktor.server.application.Application
import io.ktor.server.request.receive
import io.ktor.server.response.respondText
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import ru.cororo.songpay.common.auth.request.RegisterRequest

fun Application.configureRouting() {
    routing {
        route("/api") {
            route("/auth") {
                post("/register") {
                    val body = call.receive<RegisterRequest>()
                    call.respondText("Got body $body!")
                }
            }
        }
    }
}