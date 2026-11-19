package com.hambasanvlad.endurofleet.model

/** O motocicletă din flotă (entitatea aplicației). */
data class Motorcycle(
    val id: Int,                 // identificator intern, utilizatorul nu îl vede; nu se schimbă niciodată
    val model: String,           // ex. KTM 300 EXC
    val vinNumber: String,       // seria de șasiu (VIN), 17 caractere
    val engineHours: Double,     // ore totale de funcționare ale motorului
    val oilHours: Double,        // ore de la ultimul schimb de ulei
    val pistonHours: Double,     // ore de la ultimul schimb de piston
    val issues: String = "",     // probleme cunoscute (text liber)
) {
    val needsOilChange: Boolean get() = oilHours >= OIL_INTERVAL
    val needsPistonChange: Boolean get() = pistonHours >= PISTON_INTERVAL
    val hasIssues: Boolean get() = issues.isNotBlank()

    companion object {
        // Intervale de service (le poți schimba aici).
        const val OIL_INTERVAL = 10.0
        const val PISTON_INTERVAL = 80.0
    }
}
