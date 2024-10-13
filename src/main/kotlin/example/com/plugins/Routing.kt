package example.com.plugins

import example.com.stationService
import io.ktor.http.HttpStatusCode
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

        get("/station/{id}") {
            val id = call.parameters["id"]
            if (id != null) {
                val station = stationService.getStationNullable(id)
                if (station != null) {
                    call.respond(station)
                } else {
                    call.respond(HttpStatusCode.NotFound, "Station not found!")
                }
            }
        }
    }
}
