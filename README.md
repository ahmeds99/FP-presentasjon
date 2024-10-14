# FP-presentasjon
Presentasjon om funksjonell programmering i Kotlin, med kodesnutter og diverse eksempler. 

## Funksjonell feilhåndtering

Det gjennomgående temaet i kodesnuttene (og presentasjonen) handler om feilhåndtering. Spesielt interessant er måten vi kan bruke typer som et verktøy for feilhåndtering ved å øke lesbarheten til en metode, samt avgrense handlingsrommet på en egendefinert måte ved å spesifisere feil som kan oppstå i domenet vårt. Spesielt _Either_ blir benyttet for vise hvordan kan vi bevege oss bort fra try-catch og Nullable typer. 

## Typesikkerhet

Et annet tema vi berører er bruken av value classes i Kotlin. Se for deg eksempelet med en dataklasse _Station_, likt det vi brukte i koden vår: 

```kotlin
data class Station(
    val id: String,
    val name: String,
    val address: String,
    val capacity: Int,
)
```

Denne klassen er helt fin og grei å bruke, og den har typer til alle variablene sine. Men, selv om vi benytter String som type for id, navn og addresse, så har vi egentlig ingen typesikkerhet. Vi kunne også brukt named arguments ved opprettelsen av slike Station-objekter: 


```kotlin
val uioStasjon = Station(
    id = "1",
    name = "UiO ved Bunnpris"
    address = "Problemveien 1"
    capcity = 20
)
```

Men også her er det ingenting i veien for å blande mellom navn og addresse f.eks. Kompilatoren ser kun på om det er en streng som blir sendt som typen. Så hva kan vi gjøre? 

### Value classes
Mye av informasjonen her er hentet og inspirert fra [dokumentasjonen](https://kotlinlang.org/docs/inline-classes.html) til Kotlin. 

En idiomatisk løsning som også sikrer typesikkerhet er å bruke _value classes_. Fremfor name som en String, så kunne vi benyttet dette: 

```kotlin
@JvmInline value class Name(val value: String)

data class Station(
    val id: String,
    val name: Name,
    val address: String,
    val capacity: Int,
)

val uioStasjon = Station(
    id = "1",
    name = Name("UiO ved Bunnpris")
    address = "Problemveien 1"
    capcity = 20
)
```

Det vi gjør her er å tilføye en type som wrapper rundt verdien vi ønsker. Du tenker kanskje at dette medfører en del overhead dersom _alle_ verdier trenger en value class (spesielt primitive typer), men fordelen med value classes er at de ikke inkluderer noen overhead, f.eks. knyttet til heap allokering. En annen fordel er at vi [modellerer domenet](https://arrow-kt.io/learn/design/domain-modeling/) vårt med større presisjon. De har ingen identitet, og er _kun_ ment for å holde på én verdi. Ved å forsøke å inkludere mer enn én parameter dukker følgende feilmelding opp

```
Inline class must have exactly one primary constructor parameter.
```

Under panseret i den genererte koden oppfører en value class som enten en wrapper, eller den underliggende typen. Grunnet økt effektivitet med tanke på kjøretid så prøver Kotlin-kompilatoren å bruke den underliggende typen så ofte som mulig for å optimalisere. 

### Er ikke dette det samme som typealias????

Nei. Typealias er aliaser, og kun det. Value classes oppfører seg som en ny type, som _wrapper_ den underliggende typen. For å aksessere den underliggende typen til Name i eksempelet over (Station), er vi nødt til å si: 

```kotlin
val uioStasjon = Station(
    id = "1",
    name = Name("UiO ved Bunnpris")
    address = "Problemveien 1"
    capcity = 20
)

val stationName = uioStasjon.name.value
```

der value kommer fra attributtet i vår inline value class _Name_. 





