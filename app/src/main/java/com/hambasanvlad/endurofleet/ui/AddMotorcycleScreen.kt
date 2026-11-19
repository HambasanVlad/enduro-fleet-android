package com.hambasanvlad.endurofleet.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.hambasanvlad.endurofleet.data.MotorcycleViewModel
import com.hambasanvlad.endurofleet.model.MotorcycleForm

/** Ecranul Create: separat de listă, cu validare în același ecran. */
@Composable
fun AddMotorcycleScreen(
    viewModel: MotorcycleViewModel,
    onDone: () -> Unit,
) {
    var form by remember { mutableStateOf(MotorcycleForm()) }          // formular gol
    var errors by remember { mutableStateOf(MotorcycleForm.Errors()) } // nicio eroare la început

    fun save() {
        errors = form.validate()
        if (!errors.isEmpty) return                 // erorile apar în ecran, nu salvăm
        viewModel.add(form.toMotorcycle(id = 0))    // id-ul real îl dă ViewModel-ul; doar elementul nou e adăugat
        onDone()                                    // înapoi la listă
    }

    FormScreen(
        title = "Add Motorcycle",
        buttonText = "Add",
        onBack = onDone,
        onSave = ::save,
    ) {
        MotorcycleFormFields(form, errors) { form = it }
    }
}
