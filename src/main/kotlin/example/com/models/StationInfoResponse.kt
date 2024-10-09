package example.com.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StationInfoResponse(
    @SerialName("last_updated") val lastUpdated: Long,
    val data: Data
)

@Serializable
data class Data(
    val stations: List<Station>
)