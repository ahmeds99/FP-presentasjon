package example.com.plugins

import arrow.core.flatMap
import example.com.stationService
import example.com.testEgenEither
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
                val station = stationService.getStationEvenEvenBetter(id)
                station.fold(
                    { error -> call.respond(HttpStatusCode.BadRequest, error.message) },
                    { s -> call.respond(s) }
                )
            }
        }

        get("/testEither") {
            testEgenEither()
        }

        get("/new-stations") {
            call.respond(stationService.addNewStations())
        }
    }
}
