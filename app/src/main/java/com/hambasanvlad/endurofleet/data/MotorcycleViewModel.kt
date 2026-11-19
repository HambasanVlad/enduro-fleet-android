package com.hambasanvlad.endurofleet.data

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.hambasanvlad.endurofleet.model.Motorcycle

/**
 * Datele aplicației, ținute doar în memorie (date false), cum cere lab 2.
 * Este singurul loc unde lista se modifică: ecranele doar cer add / update / delete.
 * Mai târziu (baza de date locală) se schimbă doar acest fișier.
 */
class MotorcycleViewModel : ViewModel() {

    // mutableStateListOf = listă „observată” de Compose: la o modificare,
    // se redesenează doar ce depinde de elementul schimbat.
    private val _motorcycles = mutableStateListOf<Motorcycle>().apply { addAll(sampleData) }

    /** Din afară lista se poate doar citi. */
    val motorcycles: List<Motorcycle> get() = _motorcycles

    private var nextId = sampleData.maxOf { it.id } + 1

    /** Create: adaugă doar elementul nou, cu un id nou. */
    fun add(motorcycle: Motorcycle) {
        _motorcycles.add(motorcycle.copy(id = nextId++))
    }

    /** Update: înlocuiește pe loc elementul cu același id; id-ul rămâne neschimbat. */
    fun update(motorcycle: Motorcycle) {
        val index = _motorcycles.indexOfFirst { it.id == motorcycle.id }
        if (index >= 0) _motorcycles[index] = motorcycle
    }

    /** Delete: primește doar id-ul. */
    fun delete(id: Int) {
        _motorcycles.removeAll { it.id == id }
    }

    fun find(id: Int): Motorcycle? = _motorcycles.firstOrNull { it.id == id }

    companion object {
        val sampleData = listOf(
            Motorcycle(1, "KTM 300 EXC", "VBKEXC300N0000001", 145.5, 4.0, 32.0),
            Motorcycle(2, "KTM 250 EXC", "VBKEXC250N0000002", 210.0, 11.5, 85.0,
                issues = "Clutch slipping on steep climbs."),
            Motorcycle(3, "KTM 350 EXC-F", "VBKEXC350N0000003", 62.0, 9.0, 62.0),
            Motorcycle(4, "Husqvarna TE 300", "VBKTE300N00000004", 98.0, 2.0, 18.0,
                issues = "Front brake lever is loose."),
            Motorcycle(5, "Husqvarna TE 250", "VBKTE250N00000005", 30.0, 6.0, 30.0),
        )
    }
}
