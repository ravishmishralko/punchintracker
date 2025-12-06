/**
 * Created by Ravish Mishra on 30 November 2025
 * GitHub: https://github.com/ravishmishralko/punchintracker
 */
package com.office.punchintracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.office.punchintracker.data.DataStoreManager
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToPunchIn: () -> Unit,
    onNavigateToRoute: () -> Unit,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val userId by DataStoreManager.getUserId(context)
        .collectAsState(initial = "User")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Punch-In Tracker")
                        Text(
                            text = "Track your location every 10 minutes",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top welcome section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Welcome, $userId!",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Middle section with action cards
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Punch-In Card
                Card(
                    onClick = onNavigateToPunchIn,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Punch In",
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = "Start Punch-In",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = "Begin location tracking",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }

                // View Route Card
                Card(
                    onClick = onNavigateToRoute,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Place,
                            contentDescription = "View Route",
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = "View Route",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Text(
                                text = "See your tracked path",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }
            }

            // Bottom section with logout
            OutlinedButton(
                onClick = {
                    scope.launch {
                        DataStoreManager.logout(context)
                        onLogout()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.ExitToApp,
                    contentDescription = "Logout",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Logout")
            }
        }
    }
}