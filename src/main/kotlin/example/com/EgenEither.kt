package example.com

import example.com.models.DomainError

// Kan vi gjenskape Either på egenhånd?
sealed class EgenEither<out A, out B> {
    fun isLeft(): Boolean = this is Left
    fun isRight(): Boolean = this is Right

    abstract fun fold(left: (A) -> Unit, right: (B) -> Unit)

    // TODO: fiks. Funker ikke helt ennå
    class Left<A>(val value: A) : EgenEither<A, Nothing>() {
        override fun fold(left: (A) -> Unit, right: (Nothing) -> Unit) = left(value)
    }

    class Right<B>(val value: B) : EgenEither<Nothing, B>() {
        override fun fold(left: (Nothing) -> Unit, right: (B) -> Unit) = right(value)
    }
}


fun testEgenEither() {
    val testString = EgenEither.Right("HALLO")

    val domainError = EgenEither.Left(DomainError.EmptyStationInput())

    if (domainError.isLeft())
        println("Vi fikk (korrekt) en feil.. ${domainError.value.message}")

    if (testString.isRight())
        println(testString.value)
}