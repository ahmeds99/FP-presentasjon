package example.com.plugins

import example.com.stationService
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello world!!!")
        }

        get("/stations") {
            call.respond(stationService.getStations())
        }
    }
}
