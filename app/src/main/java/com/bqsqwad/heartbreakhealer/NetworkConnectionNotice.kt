package com.bqsqwad.heartbreakhealer

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.getSystemService

@Composable
fun NetworkConnectionNotice() {
    val context = LocalContext.current
    val connectivity = remember(context) { context.getSystemService<ConnectivityManager>() }
    var connected by remember { mutableStateOf(context.hasValidatedInternet(connectivity)) }
    DisposableEffect(connectivity) {
        if (connectivity == null) {
            connected = false
            onDispose { }
        } else {
            val callback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) { connected = context.hasValidatedInternet(connectivity) }
                override fun onCapabilitiesChanged(network: Network, capabilities: NetworkCapabilities) {
                    connected = capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                }
                override fun onLost(network: Network) { connected = context.hasValidatedInternet(connectivity) }
            }
            runCatching { connectivity.registerDefaultNetworkCallback(callback) }
            onDispose { runCatching { connectivity.unregisterNetworkCallback(callback) } }
        }
    }
    if (!connected) Surface(color = MaterialTheme.colorScheme.secondaryContainer, contentColor = MaterialTheme.colorScheme.onSecondaryContainer, modifier = Modifier.fillMaxWidth()) {
        Text("You’re offline. Turn on mobile data or connect to Wi-Fi for online features and ads. You can still use available offline features.", style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp))
    }
}

private fun Context.hasValidatedInternet(connectivity: ConnectivityManager?): Boolean {
    val manager = connectivity ?: return false
    val network = manager.activeNetwork ?: return false
    val capabilities = manager.getNetworkCapabilities(network) ?: return false
    return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
}
