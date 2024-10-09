package example.com.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Station(
    @SerialName("station_id") val id: String,
    val name: String,
    val address: String,
    val capacity: Int,
)
