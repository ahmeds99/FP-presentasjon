package example.com

import example.com.models.Station
import example.com.models.StationInfoResponse
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.isSuccess

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
}