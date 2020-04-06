package me.dgahn

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.DefaultHeaders
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun Application.module() {
    install(DefaultHeaders)
    install(CallLogging)
    routing {
        get("/") {
          call.respondText("OK")
        }
    }
}

fun main() {
    embeddedServer(factory = Netty, port = 8080, module = Application::module).start(wait = true)
}
