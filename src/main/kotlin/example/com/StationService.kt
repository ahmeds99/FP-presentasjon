package example.com

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import example.com.models.DomainError
import example.com.models.Station
import example.com.models.StationInfoResponse
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.isSuccess
import example.com.models.DomainError.*
import example.com.models.DomainError.EmptyStationList

class StationService {
    val stationInfoURL = "https://gbfs.urbansharing.com/oslobysykkel.no/station_information.json"

    suspend fun getStations(): List<Station> {
        val response = client.get(stationInfoURL)

        if (!response.status.isSuccess()) {
            throw Exception("Feil ved henting av data: ${response.status}")
        }

        val responseBody = response.body<StationInfoResponse>()
        val allStations = responseBody.data.stations

        check(allStations.isNotEmpty()) { "Fikk ut tom liste.." }

        return responseBody.data.stations
    }

    suspend fun getStationsBetter(): Either<DomainError, List<Station>> {
        val response = client.get(stationInfoURL)

        if (!response.status.isSuccess())
            return Either.Left(NetworkError(response.status.value))

        val responseBody = response.body<StationInfoResponse>()
        val allStations = responseBody.data.stations

        if (allStations.isEmpty())
            return EmptyStationList().left()

        return Either.Right(responseBody.data.stations)
    }

    suspend fun getStationNullable(stationId: String): Station? {
        if (stationId.isEmpty()) {
            return null
        }

        try {
            val allStations = getStations()
            val station = allStations.find { station ->
                station.id == stationId
            }

            if (station == null) {
                println("Could not find station...")
                return null
            }

            return station
        } catch (e: IllegalStateException) {
            println("Feil: ${e.message}")
            return null
        } catch (e: Exception) {
            println("Fikk feil grunnet nettverksfeil ${e.message}")
            return null
        }
    }

    // Eksempel på noe bedre enn try-catch, men fortsatt nullable
    suspend fun getStationBetter(stationId: String): Station? {
        if (stationId.isEmpty()) {
            return null
        }

        val allStations = getStationsBetter()
        return when (allStations) {
            is Either.Left -> {
                when (allStations.value) {
                    is NetworkError -> null // or retry
                    is EmptyStationList -> null // ...
                    else -> null // Exhaustive pattern matching
                }
            }

            is Either.Right -> {
                allStations.value.find { station ->
                    station.id == stationId
                }
            }
        }
    }

    suspend fun getStationEvenEvenBetter(stationId: String): Either<DomainError, Station> {
        if (stationId.isEmpty()) return EmptyStationInput().left()

        val stationsResponse = getStationsBetter()

        return stationsResponse.fold(
            { domainError -> domainError.left() },
            { allStations ->
                val station = allStations.find { station -> station.id == stationId }

                station?.right() ?: StationNotFound(stationId).left()
            }
        )
    }

    // Merk: named arguments kan til dels hjelpe oss her, men garanterer ingen typesikkerhet
    fun addNewStations(): List<Station> {
        val stations = listOf(
            Station(
                "900",
                "Forskningsparken Nord",
                "Gaustadaleen 21B",
                20
            ),
            Station(
                "901",
                "Gaustadaleen 21C",
                "Forskningsparken Sør",
                25
            ),
            Station(
                "902",
                "Ved Sophus Lies",
                "Problemveien 22",
                18
            ),
            Station(
                "903",
                "Akersgata 35",
                "Computas hovedkontor",
                30
            ),
            Station(
                "Åråsen Stadion",
                "904",
                "C.J. Hansens vei 3B",
                22
            ),
        )

        println("Adding new stations: $stations")

        // ADD NEW STATIONS TO A DB ...

        return stations
    }
}