package com.hambasanvlad.endurofleet.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hambasanvlad.endurofleet.data.MotorcycleViewModel
import com.hambasanvlad.endurofleet.model.Motorcycle
import com.hambasanvlad.endurofleet.model.MotorcycleForm

/** Ecranul principal (Read): lista flotei; Delete cu dialog de confirmare. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MotorcycleListScreen(
    viewModel: MotorcycleViewModel,
    onAddClick: () -> Unit,
    onEditClick: (Int) -> Unit,
) {
    // id-ul motocicletei pentru care e deschisă confirmarea de ștergere (null = niciuna)
    var pendingDeleteId by rememberSaveable { mutableStateOf<Int?>(null) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Fleet") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Filled.Add, contentDescription = "Add motorcycle")
            }
        },
    ) { padding ->
        if (viewModel.motorcycles.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("No motorcycles yet. Tap + to add one.")
            }
        } else {
            LazyColumn(contentPadding = padding) {
                // key = id: Compose recunoaște fiecare rând după id, deci la add/update/delete
                // atinge doar rândul afectat; lista nu e reconstruită.
                items(viewModel.motorcycles, key = { it.id }) { motorcycle ->
                    MotorcycleRow(
                        motorcycle = motorcycle,
                        onClick = { onEditClick(motorcycle.id) },
                        onDeleteClick = { pendingDeleteId = motorcycle.id },
                    )
                    HorizontalDivider()
                }
            }
        }
    }

    // Dialogul de confirmare pentru Delete
    pendingDeleteId?.let { id ->
        val name = viewModel.find(id)?.model ?: "This motorcycle"
        AlertDialog(
            onDismissRequest = { pendingDeleteId = null },
            title = { Text("Delete motorcycle?") },
            text = { Text("$name will be removed from the fleet.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.delete(id)          // se trimite doar id-ul
                    pendingDeleteId = null
                }) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { pendingDeleteId = null }) { Text("Cancel") }
            },
        )
    }
}

@Composable
private fun MotorcycleRow(
    motorcycle: Motorcycle,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    ListItem(
        modifier = Modifier.clickable(onClick = onClick),
        headlineContent = { Text(motorcycle.model, fontWeight = FontWeight.SemiBold) },
        supportingContent = {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text("VIN ${motorcycle.vinNumber}", style = MaterialTheme.typography.bodySmall)
                Text(
                    "Engine: ${MotorcycleForm.format(motorcycle.engineHours)} h  ·  " +
                        "Oil: ${MotorcycleForm.format(motorcycle.oilHours)} h  ·  " +
                        "Piston: ${MotorcycleForm.format(motorcycle.pistonHours)} h",
                    style = MaterialTheme.typography.bodySmall,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    if (motorcycle.needsOilChange) Badge("Oil due", Color(0xFFE67E00))
                    if (motorcycle.needsPistonChange) Badge("Piston due", Color(0xFFE67E00))
                    if (motorcycle.hasIssues) Badge("Issues", Color(0xFFD32F2F))
                }
            }
        },
        trailingContent = {
            IconButton(onClick = onDeleteClick) {
                Icon(Icons.Filled.Delete, contentDescription = "Delete ${motorcycle.model}")
            }
        },
    )
}

@Composable
private fun Badge(text: String, color: Color) {
    Surface(color = color.copy(alpha = 0.18f), contentColor = color, shape = CircleShape) {
        Text(
            text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
        )
    }
}
