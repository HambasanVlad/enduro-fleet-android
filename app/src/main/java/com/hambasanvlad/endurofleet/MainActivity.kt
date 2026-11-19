package com.hambasanvlad.endurofleet

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.hambasanvlad.endurofleet.data.MotorcycleViewModel
import com.hambasanvlad.endurofleet.ui.AddMotorcycleScreen
import com.hambasanvlad.endurofleet.ui.EditMotorcycleScreen
import com.hambasanvlad.endurofleet.ui.MotorcycleListScreen

/** Punctul de pornire al aplicației. */
class MainActivity : ComponentActivity() {

    // Un singur ViewModel (datele), comun pentru toate ecranele.
    private val fleetViewModel: MotorcycleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EnduroFleetTheme {
                EnduroFleetNavigation(fleetViewModel)
            }
        }
    }
}

/** Navigarea între ecrane: list -> add, list -> edit/{id}. */
@Composable
fun EnduroFleetNavigation(viewModel: MotorcycleViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "list") {
        composable("list") {
            MotorcycleListScreen(
                viewModel = viewModel,
                onAddClick = { navController.navigate("add") },
                onEditClick = { id -> navController.navigate("edit/$id") },  // se trimite doar id-ul
            )
        }
        composable("add") {
            AddMotorcycleScreen(
                viewModel = viewModel,
                onDone = { navController.popBackStack() },
            )
        }
        composable(
            route = "edit/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType }),
        ) { entry ->
            EditMotorcycleScreen(
                viewModel = viewModel,
                motorcycleId = entry.arguments?.getInt("id") ?: -1,
                onDone = { navController.popBackStack() },
            )
        }
    }
}

/** Culorile aplicației; pe Android 12+ (Samsung One UI 4+) preia culorile din wallpaper. */
@Composable
fun EnduroFleetTheme(content: @Composable () -> Unit) {
    val dark = isSystemInDarkTheme()
    val colors = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val context = LocalContext.current
        if (dark) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    } else {
        if (dark) darkColorScheme() else lightColorScheme()
    }
    MaterialTheme(colorScheme = colors, content = content)
}
