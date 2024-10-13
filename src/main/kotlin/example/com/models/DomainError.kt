package example.com.models

sealed class DomainError(val message: String) {
    class EmptyStationInput : DomainError("Empty input string")

    data class NetworkError(val statusCode: Int) :
        DomainError("Network error upon request, with status code $statusCode")

    class EmptyStationList : DomainError("Empty station list")

    data class StationNotFound(val id: String) : DomainError("Station $id not found")
}
