package example.com

import arrow.optics.Lens
import arrow.optics.*

@optics
data class Restaurant(
    val navn: String,
    val adresse: Adresse
) {
    companion object
}

@optics
data class Adresse(
    val navn: String,
    val by: By
) {
    companion object
}

@optics
data class By(
    val by: String,
    val land: String
) {
    companion object
}

val computasKantina =
    Restaurant("Akersgata Kantine", Adresse("Akersgata 4", By("Oslo", "Norge")))

val land = Restaurant.adresse.by.land.get(computasKantina)

fun main() {
    println(land)
}


fun old() {
    computasKantina.copy(
        navn = "Tiårets kantine"
    )

    computasKantina.copy(
        adresse = computasKantina.adresse.copy(
            by = By("Bergen", "Norge")
        )
    )

    val byLense: Lens<Restaurant, By> = Lens(
        get = { it.adresse.by },
        set = { restaurant: Restaurant, by: By ->
            restaurant.copy(
                adresse = restaurant.adresse.copy(
                    by = by
                )
            )
        }
    )

    byLense.get(computasKantina)

    byLense.modify(computasKantina) {
        By("Trondheim", "Norge")
    }
}