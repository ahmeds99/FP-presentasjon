package example.com.plugins

import example.com.stationService
import example.com.testEgenEither
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello world!!!")
        }

        get("/stations") {
            stationService.getStationsBetter().fold(
                { error -> call.respond(error.message) },
                { stations -> call.respond(stations) }
            )
        }

        get("/station/{id}") {
            val id = call.parameters["id"]
            if (id != null) {
                stationService.getStationEvenEvenBetter(id).fold(
                    { error -> call.respond(error.message) },
                    { station -> call.respond(station) }
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

/*
    stationService.getStationsBetter().fold(
                { error -> call.respond(error.message) },
                { stations -> call.respond(stations) }
            )
 */
