/**
 * Created by Ravish Mishra on 30 November 2024
 * GitHub: https://github.com/ravishmishralko/punchintracker
 */
/**
 * Created by Ravish Mishra on 30 November 2025
 * GitHub: https://github.com/ravishmishralko/punchintracker
 */
package com.office.punchintracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.office.punchintracker.data.DataStoreManager
import com.office.punchintracker.ui.navigation.NavGraph
import com.office.punchintracker.ui.navigation.Screen
import com.office.punchintracker.ui.theme.PunchInTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PunchInTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val context = LocalContext.current
                    val navController = rememberNavController()

                    val isLoggedIn by DataStoreManager.isLoggedIn(context)
                        .collectAsState(initial = false)

                    val startDestination = if (isLoggedIn) {
                        Screen.Home.route
                    } else {
                        Screen.Login.route
                    }

                    NavGraph(
                        navController = navController,
                        startDestination = startDestination
                    )
                }
            }
        }
    }
}