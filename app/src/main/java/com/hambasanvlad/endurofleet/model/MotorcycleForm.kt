package com.hambasanvlad.endurofleet.model

/**
 * Starea formularelor Add/Edit. Câmpurile sunt text, pentru că așa le scrie utilizatorul;
 * le validăm și le transformăm în [Motorcycle] abia la salvare.
 */
data class MotorcycleForm(
    val model: String = "",
    val vin: String = "",
    val engineHours: String = "",
    val oilHours: String = "",
    val pistonHours: String = "",
    val issues: String = "",
) {
    /** Câte un mesaj de eroare (sau null) pentru fiecare câmp. */
    data class Errors(
        val model: String? = null,
        val vin: String? = null,
        val engineHours: String? = null,
        val oilHours: String? = null,
        val pistonHours: String? = null,
    ) {
        val isEmpty: Boolean
            get() = model == null && vin == null && engineHours == null &&
                oilHours == null && pistonHours == null
    }

    fun validate(): Errors {
        val engine = parse(engineHours)
        val oil = parse(oilHours)
        val piston = parse(pistonHours)
        val vinValue = vin.trim()

        return Errors(
            model = if (model.isBlank()) "Model is required." else null,
            vin = if (vinValue.length != 17 || !vinValue.all { it.isLetterOrDigit() })
                "VIN must have exactly 17 letters/digits." else null,
            engineHours = if (engine == null || engine < 0) "Enter a number, 0 or more." else null,
            oilHours = hoursError(oil, engine),
            pistonHours = hoursError(piston, engine),
        )
    }

    /** Construiește motocicleta. Se apelează doar după ce validate() nu a găsit erori. */
    fun toMotorcycle(id: Int): Motorcycle = Motorcycle(
        id = id,
        model = model.trim(),
        vinNumber = vin.trim().uppercase(),
        engineHours = parse(engineHours) ?: 0.0,
        oilHours = parse(oilHours) ?: 0.0,
        pistonHours = parse(pistonHours) ?: 0.0,
        issues = issues.trim(),
    )

    companion object {
        /** Formular precompletat cu valorile existente (pentru Edit). */
        fun from(m: Motorcycle) = MotorcycleForm(
            model = m.model,
            vin = m.vinNumber,
            engineHours = format(m.engineHours),
            oilHours = format(m.oilHours),
            pistonHours = format(m.pistonHours),
            issues = m.issues,
        )

        /** Acceptă și "12,5" (virgulă) și "12.5". Întoarce null dacă nu e număr. */
        fun parse(text: String): Double? = text.trim().replace(',', '.').toDoubleOrNull()

        /** 145.0 -> "145", 145.5 -> "145.5" */
        fun format(value: Double): String =
            if (value % 1.0 == 0.0) value.toLong().toString() else value.toString()

        private fun hoursError(hours: Double?, engine: Double?): String? = when {
            hours == null || hours < 0 -> "Enter a number, 0 or more."
            engine != null && engine >= 0 && hours > engine -> "Cannot be more than the engine hours."
            else -> null
        }
    }
}
