package example.com.fasit

/*
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
                station.id.value == stationId
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
                    station.id.value == stationId
                }
            }
        }
    }

    // Bruk som fasit hvis stuck på slutten
    suspend fun getStationEvenEvenBetter(stationId: String): Either<DomainError, Station> {
        if (stationId.isEmpty()) return Either.Left(EmptyStationInput())

        val stationsResponse = getStationsBetter()

        return stationsResponse.fold(
            { domainError -> domainError.left() },
            { allStations ->
                val station = allStations.find { station -> station.id.value == stationId }

                if (station == null) StationNotFound(stationId).left()
                else station.right()
            }
        )
    }

    // Merk: named arguments kan til dels hjelpe oss her, men garanterer ingen typesikkerhet
    fun addNewStations(): List<Station> {
        val stations = listOf(
            Station(
                id = StationId("900"),
                name = Name("Forskningsparken Nord"),
                address = Address("Gaustadaleen 21B"),
                capacity = Capacity(20)
            ),
            Station(
                id = StationId("901"),
                name = Name("Forskningsparken Sør"),
                address = Address("Gaustadaleen 21C"),
                capacity = Capacity(25)
            ),
            Station(
                id = StationId("902"),
                name = Name("Ved Sophus Lies"),
                address = Address("Problemveien 22"),
                capacity = Capacity(18)
            ),
            Station(
                id = StationId("903"),
                name = Name("Computas hovedkontor"),
                address = Address("Akersgata 35"),
                capacity = Capacity(30)
            ),
            Station(
                id = StationId("904"),
                name = Name("Åråsen Stadion"),
                address = Address("C.J. Hansens vei 3B"),
                capacity = Capacity(22)
            ),
        )

        println("Adding new stations: $stations")

        // ADD NEW STATIONS TO A DB ...

        return stations
    }
}

 */