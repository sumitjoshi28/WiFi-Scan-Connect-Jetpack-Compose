package com.sumit.wificonnect.screens

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.sumit.wificonnect.components.WiFiList

@Composable
fun WiFiScreen(){
    val context = LocalContext.current
    val wifiManager = remember {
        context.getSystemService(Context.WIFI_SERVICE) as WifiManager
    }
    val networks = remember { mutableStateListOf<String>() }
    val lifecycleOwner = LocalLifecycleOwner.current


    val requestLocationPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // Permission granted
                wifiManager.startScan()
            } else {
                // Permission denied, handle accordingly.
                Toast.makeText(context,"Location permission denied !!",Toast.LENGTH_SHORT).show()
            }
        }

    LaunchedEffect(key1 = Unit, block = {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ){
            requestLocationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    })

    DisposableEffect(lifecycleOwner) {
        // Create a callback lambda to handle Wi-Fi updates.

        val wifiListCallback: (List<ScanResult>) -> Unit = { scanResults ->
            networks.clear()
            scanResults.forEach { result ->
                val ssid = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    result.wifiSsid?.toString()?.removeSurrounding("\"") ?: ""
                } else {
                    result.SSID ?: ""
                }
                if (ssid.isNotBlank()){
                    networks.add(ssid)
                }
            }
        }

        // Register a broadcast receiver to listen for Wi-Fi scan results.
        val wifiReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent?) {
                intent?.let {
                    if (it.action == WifiManager.SCAN_RESULTS_AVAILABLE_ACTION) {
                        if (ActivityCompat.checkSelfPermission(
                                context,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ) != PackageManager.PERMISSION_GRANTED
                        ){
                            val scanResults = wifiManager.scanResults
                            wifiListCallback(scanResults)
                        }else{
                            requestLocationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                        }
                    }
                }
            }
        }

        // Register the receiver and start Wi-Fi scanning.
        context.registerReceiver(wifiReceiver, IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
        wifiManager.startScan()

        // Add a lifecycle observer to handle updates when the app is resumed or created.
        val wifiListObserver = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME || event == Lifecycle.Event.ON_CREATE) {
                // Fetch Wi-Fi list when the app is resumed or created
                wifiListCallback(wifiManager.scanResults)
            }
        }

        // Add the lifecycle observer
        lifecycleOwner.lifecycle.addObserver(wifiListObserver)

        // Dispose the receiver and remove the observer when the composable is disposed.
        onDispose {
            context.unregisterReceiver(wifiReceiver)
            lifecycleOwner.lifecycle.removeObserver(wifiListObserver)
        }
    }
    // Show wifi scan list here.
    WiFiList(networks)
}



