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
            return NetworkError(response.status.value).left()


        val responseBody = response.body<StationInfoResponse>()
        val allStations = responseBody.data.stations

        if (allStations.isEmpty())
            return EmptyStationList().left()

        return responseBody.data.stations.right()
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

    // Bruk som fasit hvis stuck på slutten
    suspend fun getStationEvenEvenBetter(stationId: String): Either<DomainError, Station> {
        if (stationId.isEmpty()) return Either.Left(EmptyStationInput())

        val stationsResponse = getStationsBetter()

        return stationsResponse.fold(
            { error -> error.left() },
            { allStations ->
                val station = allStations.find { station -> station.id == stationId }

                if (station == null) StationNotFound(stationId).left()
                else station.right()
            }
        )
    }
}