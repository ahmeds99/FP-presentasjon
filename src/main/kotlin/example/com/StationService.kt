package example.com

import example.com.models.Station
import example.com.models.StationInfoResponse
import io.ktor.client.call.body
import io.ktor.client.request.get

class StationService {
    val stationInfoURL = "https://gbfs.urbansharing.com/oslobysykkel.no/station_information.json"

    suspend fun getStations(): List<Station> {
        val response = client.get(stationInfoURL)

        val responseBody = response.body<StationInfoResponse>()
        return responseBody.data.stations
    }
}