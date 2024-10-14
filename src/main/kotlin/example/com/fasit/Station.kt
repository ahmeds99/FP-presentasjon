package example.com.fasit

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class StationId(val value: String)

@JvmInline
@Serializable
value class Name(val value: String)

@JvmInline
@Serializable
value class Address(val value: String)

@JvmInline
@Serializable
value class Capacity(val value: Int)

@Serializable
data class Station(
    @SerialName("station_id") val id: StationId,
    val name: Name,
    val address: Address,
    val capacity: Capacity,
)
