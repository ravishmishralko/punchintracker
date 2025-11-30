package com.office.punchintracker.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.LocationServices
import com.office.punchintracker.data.local.PunchInDatabase
import com.office.punchintracker.data.local.PunchInEntity
import com.office.punchintracker.data.repository.PunchInRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PunchInScreen(
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val locationPermissions = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    var isTracking by remember { mutableStateOf(false) }
    var timeRemaining by remember { mutableStateOf(600) } // 10 minutes in seconds
    var showWarning by remember { mutableStateOf(false) }
    var latitude by remember { mutableStateOf(0.0) }
    var longitude by remember { mutableStateOf(0.0) }
    var punchInCount by remember { mutableStateOf(0) }

    val userId by context.dataStore.data
        .map { prefs -> prefs[stringPreferencesKey("user_id")] ?: "User" }
        .collectAsState(initial = "User")

    val database = remember { PunchInDatabase.getDatabase(context) }
    val repository = remember { PunchInRepository(database.punchInDao()) }

    // Location tracking effect
    LaunchedEffect(isTracking) {
        if (isTracking) {
            while (isTracking) {
                delay(1000)
                timeRemaining--

                // Show warning 1 minute before
                if (timeRemaining == 60) {
                    showWarning = true
                }

                // Punch in every 10 minutes
                if (timeRemaining <= 0) {
                    getCurrentLocation(context) { lat, lon ->
                        latitude = lat
                        longitude = lon
                        scope.launch {
                            repository.insertPunchIn(
                                PunchInEntity(
                                    latitude = lat,
                                    longitude = lon,
                                    timestamp = System.currentTimeMillis(),
                                    userId = userId
                                )
                            )
                            punchInCount++
                        }
                    }
                    timeRemaining = 600
                    showWarning = false
                }
            }
        }
    }

    // Pulsing animation
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Punch-In Tracking") },
                navigationIcon = {
                    IconButton(onClick = {
                        if (!isTracking) onNavigateBack()
                    }) {
                        Icon(Icons.Default.ArrowBack, "Back")
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
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                if (isTracking) {
                    Text(
                        text = "Tracking Active",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Punch-ins recorded: $punchInCount",
                        style = MaterialTheme.typography.bodyLarge
                    )
                } else {
                    Text(
                        text = "Ready to Track",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }

            // Center section with timer
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(200.dp)
                        .scale(if (isTracking) scale else 1f)
                        .background(
                            color = if (isTracking)
                                MaterialTheme.colorScheme.primaryContainer
                            else
                                MaterialTheme.colorScheme.surfaceVariant,
                            shape = CircleShape
                        )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Location",
                            modifier = Modifier.size(64.dp),
                            tint = if (isTracking)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        if (isTracking) {
                            val minutes = timeRemaining / 60
                            val seconds = timeRemaining % 60
                            Text(
                                text = String.format("%02d:%02d", minutes, seconds),
                                style = MaterialTheme.typography.headlineLarge,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }

                if (showWarning && isTracking) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Text(
                            text = "⚠️ Next punch-in in 1 minute",
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }

            // Bottom section with controls
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                if (latitude != 0.0 && longitude != 0.0) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Last Location",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Lat: %.6f".format(latitude),
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = "Lon: %.6f".format(longitude),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Button(
                    onClick = {
                        if (!locationPermissions.allPermissionsGranted) {
                            locationPermissions.launchMultiplePermissionRequest()
                        } else {
                            isTracking = !isTracking
                            if (isTracking) {
                                timeRemaining = 600
                                showWarning = false
                                // Get initial location
                                getCurrentLocation(context) { lat, lon ->
                                    latitude = lat
                                    longitude = lon
                                    scope.launch {
                                        repository.insertPunchIn(
                                            PunchInEntity(
                                                latitude = lat,
                                                longitude = lon,
                                                timestamp = System.currentTimeMillis(),
                                                userId = userId
                                            )
                                        )
                                        punchInCount++
                                    }
                                }
                            } else {
                                punchInCount = 0
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isTracking)
                            MaterialTheme.colorScheme.error
                        else
                            MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = if (isTracking) "Stop Tracking" else "Start Tracking",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                if (!locationPermissions.allPermissionsGranted) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Location permission required",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@SuppressLint("MissingPermission")
private fun getCurrentLocation(
    context: Context,
    onLocationReceived: (Double, Double) -> Unit
) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
        location?.let {
            onLocationReceived(it.latitude, it.longitude)
        }
    }
}