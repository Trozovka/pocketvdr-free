package com.trozovka.pocketvdr.core.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.text.font.FontWeight
import com.trozovka.pocketvdr.core.util.appDisplayName
import com.trozovka.pocketvdr.core.util.appVersionName
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.trozovka.pocketvdr.core.entitlement.EntitlementHost
import com.trozovka.pocketvdr.core.settings.AppPreferences

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val preferences = remember { AppPreferences(context) }
    var intervalSeconds by remember { mutableIntStateOf(preferences.logIntervalSeconds) }
    var statusMessage by remember { mutableStateOf("") }
    var showHowTo by remember { mutableStateOf(false) }
    if (showHowTo) {
        HowToScreen(onBack = { showHowTo = false })
        return
    }

    androidx.compose.runtime.LaunchedEffect(Unit) {
        statusMessage = EntitlementHost.current().statusMessage(System.currentTimeMillis())
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { padding ->
        Surface(modifier = Modifier.padding(padding).fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
                Column(modifier = Modifier.fillMaxWidth().clickable { showHowTo = true }.padding(vertical = 8.dp)) {
                    Text("How to use", fontWeight = FontWeight.Bold)
                    Text("Step by step, in simple English", style = MaterialTheme.typography.bodySmall)
                }
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                Text(statusMessage, style = MaterialTheme.typography.bodyMedium)

                Text(
                    "Log interval: ${intervalSeconds}s",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(top = 24.dp),
                )
                Slider(
                    value = intervalSeconds.toFloat(),
                    onValueChange = {
                        intervalSeconds = it.toInt()
                        preferences.logIntervalSeconds = intervalSeconds
                    },
                    valueRange = AppPreferences.MIN_LOG_INTERVAL_SECONDS.toFloat()..AppPreferences.MAX_LOG_INTERVAL_SECONDS.toFloat(),
                    modifier = Modifier.fillMaxWidth(),
                )
                Text(
                    "Takes effect on the next voyage you start.",
                    style = MaterialTheme.typography.bodySmall,
                )

                EntitlementHost.current().SettingsExtras()

                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
                Text("About", fontWeight = FontWeight.Bold)
                Text("${appDisplayName(context)} v${appVersionName(context)}", style = MaterialTheme.typography.bodySmall)
                Text(
                    "A personal voyage recorder: it logs your position, speed, heading and altitude offline, " +
                        "so you have your own record of the voyage. No account, no ads, nothing leaves your phone unless you export it.",
                    style = MaterialTheme.typography.bodySmall,
                )
                Text("Made by a Master Mariner for seafarers. Developer: John Winsome Munar (Trozovka).", style = MaterialTheme.typography.bodySmall)
                Text(
                    "Not a type-approved VDR or S-VDR under SOLAS. Personal record-keeping only.",
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HowToScreen(onBack: () -> Unit) {
    BackHandler(onBack = onBack)
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("How to use") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, contentDescription = "Back") } },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            HowToSection("What it is",
                "Your own voyage recorder in your pocket. It saves where the ship was, the speed, heading and time, " +
                    "all offline: no internet, no SIM, no account.")
            HowToSection("Start and stop",
                "1. Tap Start voyage. Allow Location and Notifications when asked.\n" +
                    "2. You can lock the screen. A notification shows it is recording.\n" +
                    "3. At the end, tap Stop voyage.")
            HowToSection("Mark an incident",
                "Something happened (near miss, damage, heavy weather)? Tap MARK INCIDENT. " +
                    "It saves the exact time and position. You can add a short note.")
            HowToSection("Look back and share",
                "View past voyages > pick a voyage to see the track and the marked incidents. " +
                    "Export to GPX (for OpenCPN or Google Earth), NMEA, or a plain text log.")
            HowToSection("Settings",
                "Log interval = how often a position is saved. Shorter gives more detail, longer saves battery. " +
                    "It applies to the next voyage.")
            HowToSection("Tips",
                "Keep the phone charged. Turn off battery saver for this app so Android does not stop it. " +
                    "GPS works best near a window or outside.")
            HowToSection("Free and Pro",
                "Free: recording always works. The first 30 days you can look back at everything; after that, " +
                    "you can look back at and export the last 24 hours or the current voyage.\n" +
                    "Pro (one payment): look back at and export all your voyages, forever.")
            HowToSection("Please note",
                "This is a personal record only. It is not a type-approved VDR or S-VDR under SOLAS and has no legal standing.")
        }
    }
}

@Composable
private fun HowToSection(title: String, body: String) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(title, fontWeight = FontWeight.Bold)
        Text(body, style = MaterialTheme.typography.bodyMedium)
    }
}
