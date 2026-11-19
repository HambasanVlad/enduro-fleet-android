package com.hambasanvlad.endurofleet.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.hambasanvlad.endurofleet.data.MotorcycleViewModel
import com.hambasanvlad.endurofleet.model.MotorcycleForm

/** Ecranul Update: separat de listă, câmpurile sunt precompletate. */
@Composable
fun EditMotorcycleScreen(
    viewModel: MotorcycleViewModel,
    motorcycleId: Int,              // primim doar id-ul
    onDone: () -> Unit,
) {
    val original = remember(motorcycleId) { viewModel.find(motorcycleId) }
    if (original == null) {         // id inexistent: ne întoarcem la listă
        LaunchedEffect(Unit) { onDone() }
        return
    }

    var form by remember { mutableStateOf(MotorcycleForm.from(original)) }  // precompletat
    var errors by remember { mutableStateOf(MotorcycleForm.Errors()) }

    fun save() {
        errors = form.validate()
        if (!errors.isEmpty) return
        // același id, deci elementul este înlocuit pe loc, nu șters și re-adăugat
        viewModel.update(form.toMotorcycle(id = motorcycleId))
        onDone()
    }

    FormScreen(
        title = "Edit Motorcycle",
        buttonText = "Save",
        onBack = onDone,
        onSave = ::save,
    ) {
        MotorcycleFormFields(form, errors) { form = it }
    }
}
