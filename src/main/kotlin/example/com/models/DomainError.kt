package example.com.models

sealed class DomainError(val message: String) {
    class EmptyStationInput : DomainError("Empty input string")

    class NetworkError(val statusCode: Int) : DomainError("Network error upon request, with status code $statusCode")

    class EmptyStationList : DomainError("Empty station list")
}
