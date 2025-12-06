/**
 * Created by Ravish Mishra on 30 November 2025
 * GitHub: https://github.com/ravishmishralko/punchintracker
 */
package com.office.punchintracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.office.punchintracker.data.DataStoreManager
import com.office.punchintracker.data.local.PunchInDatabase
import com.office.punchintracker.data.repository.PunchInRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteScreen(
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val userId by DataStoreManager.getUserId(context)
        .collectAsState(initial = "User")

    val database = remember { PunchInDatabase.getDatabase(context) }
    val repository = remember { PunchInRepository(database.punchInDao()) }

    val punchIns by repository.getAllPunchIns(userId).collectAsState(initial = emptyList())

    var showDeleteDialog by remember { mutableStateOf(false) }

    val locations = remember(punchIns) {
        punchIns.map { LatLng(it.latitude, it.longitude) }
    }

    val cameraPositionState = rememberCameraPositionState {
        if (locations.isNotEmpty()) {
            position = CameraPosition.fromLatLngZoom(locations.first(), 12f)
        } else {
            position = CameraPosition.fromLatLngZoom(LatLng(0.0, 0.0), 2f)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Route Map") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    if (punchIns.isNotEmpty()) {
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(Icons.Default.Delete, "Clear History")
                        }
                    }
                }
            )
        }
    ) { padding ->
        if (punchIns.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "No punch-ins recorded yet",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Start tracking to see your route",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                // Map
                GoogleMap(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    cameraPositionState = cameraPositionState,
                    properties = MapProperties(
                        isMyLocationEnabled = false
                    ),
                    uiSettings = MapUiSettings(
                        zoomControlsEnabled = true,
                        myLocationButtonEnabled = false
                    )
                ) {
                    // Add markers for each punch-in
                    locations.forEachIndexed { index, location ->
                        Marker(
                            state = MarkerState(position = location),
                            title = "Punch-in ${index + 1}",
                            snippet = formatTime(punchIns[index].timestamp)
                        )
                    }

                    // Draw polyline connecting all points
                    if (locations.size > 1) {
                        Polyline(
                            points = locations,
                            color = androidx.compose.ui.graphics.Color.Blue,
                            width = 8f
                        )
                    }
                }

                // Info card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Route Summary",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "Total Points",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "${punchIns.size}",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "Last Updated",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = formatTime(punchIns.first().timestamp),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Delete confirmation dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Clear History") },
            text = { Text("Are you sure you want to delete all punch-in records?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        scope.launch {
                            repository.deleteAllPunchIns(userId)
                            showDeleteDialog = false
                        }
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

private fun formatTime(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}