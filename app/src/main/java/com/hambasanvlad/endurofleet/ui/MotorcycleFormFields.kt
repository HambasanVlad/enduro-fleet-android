package com.hambasanvlad.endurofleet.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.hambasanvlad.endurofleet.model.MotorcycleForm

/**
 * Câmpurile formularului, refolosite de ecranul Add și de ecranul Edit.
 * Fiecare câmp are o etichetă vizibilă (label) și afișează eroarea sub el, cu roșu.
 */
@Composable
fun MotorcycleFormFields(
    form: MotorcycleForm,
    errors: MotorcycleForm.Errors,
    onFormChange: (MotorcycleForm) -> Unit,
) {
    val numberKeyboard = KeyboardOptions(keyboardType = KeyboardType.Decimal)

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        FormField("Model", form.model, errors.model, "e.g. KTM 300 EXC") {
            onFormChange(form.copy(model = it))
        }
        FormField(
            "VIN number", form.vin, errors.vin, "17 characters",
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Characters,
                autoCorrectEnabled = false,
            ),
        ) { onFormChange(form.copy(vin = it)) }
        FormField("Engine hours (total)", form.engineHours, errors.engineHours, "e.g. 145.5",
            keyboardOptions = numberKeyboard) { onFormChange(form.copy(engineHours = it)) }
        FormField("Hours since last oil change", form.oilHours, errors.oilHours, "e.g. 4",
            keyboardOptions = numberKeyboard) { onFormChange(form.copy(oilHours = it)) }
        FormField("Hours since last piston change", form.pistonHours, errors.pistonHours, "e.g. 32",
            keyboardOptions = numberKeyboard) { onFormChange(form.copy(pistonHours = it)) }
        FormField("Issues", form.issues, null, "Known problems (optional)",
            singleLine = false) { onFormChange(form.copy(issues = it)) }
    }
}

@Composable
private fun FormField(
    label: String,
    value: String,
    error: String?,
    placeholder: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    singleLine: Boolean = true,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },                 // eticheta vizibilă
        placeholder = { Text(placeholder) },     // exemplu gri
        isError = error != null,                 // conturul devine roșu
        supportingText = { if (error != null) Text(error) },  // mesajul de eroare
        keyboardOptions = keyboardOptions,
        singleLine = singleLine,
        minLines = if (singleLine) 1 else 3,
        modifier = Modifier.fillMaxWidth(),
    )
}
