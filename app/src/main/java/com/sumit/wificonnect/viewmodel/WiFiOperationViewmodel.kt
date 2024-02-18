package com.sumit.wificonnect.viewmodel

import android.content.Context
import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiManager
import android.net.wifi.WifiNetworkSpecifier
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class WiFiOperationViewModel(context: Context) : ViewModel() {
    /** State variable to update UI. */
    private val _isWiFiConnected: MutableStateFlow<Boolean> =
        MutableStateFlow(false)
    val isWiFiConnected by lazy { _isWiFiConnected.asStateFlow()}

    /** Initialise the connectivity manager. */
    private val connectivityManager = ContextCompat.getSystemService(context, ConnectivityManager::class.java)

    /** Connect to wifi, using SSID and password. */
    fun connectToWiFi(wifiManager: WifiManager, ssid: String, password: String, isProtectedNetwork:Boolean) {
        val wifiNetworkSpecifier = WifiNetworkSpecifier.Builder().apply {
            setSsid(ssid)
            if (isProtectedNetwork){
                setWpa2Passphrase(password)
            }
        }.build()

        val networkRequest = NetworkRequest.Builder().apply {
            addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            setNetworkSpecifier(wifiNetworkSpecifier)
        }.build()

        networkCallback().let {
            connectivityManager?.requestNetwork(networkRequest, it)
        }
    }

    /** Network Callbacks, use as per your requirement. */
    private fun networkCallback(): ConnectivityManager.NetworkCallback {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onLost(network: Network) {
                super.onLost(network)
                // Release the network request
                connectivityManager?.bindProcessToNetwork(null)
                Log.d("networkCallback", "networkCallback onLost: ")
            }

            override fun onLosing(network: Network, maxMsToLive: Int) {
                super.onLosing(network, maxMsToLive)
                Log.d("networkCallback", "networkCallback onLosing: ")
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                super.onCapabilitiesChanged(network, networkCapabilities)
                Log.d("networkCallback", "networkCallback onCapabilitiesChanged: ")
            }

            override fun onBlockedStatusChanged(network: Network, blocked: Boolean) {
                super.onBlockedStatusChanged(network, blocked)
                Log.d("networkCallback", "networkCallback onBlockedStatusChanged: ")
            }

            override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
                super.onLinkPropertiesChanged(network, linkProperties)
                Log.d("networkCallback", "networkCallback onLinkPropertiesChanged: ")

            }

            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                if (connectivityManager != null) {
                    createNetworkRoute(network,connectivityManager)
                }
                Log.d("networkCallback", "networkCallback onAvailable: ")
            }

            override fun onUnavailable() {
                super.onUnavailable()
                Log.d("networkCallback", "networkCallback onUnavailable: ")

            }

        }
        return callback
    }

    private fun createNetworkRoute(network: Network, connectivityManager: ConnectivityManager) {
        // Binding to a network as it is needed for some devices like One Plus etc.
        connectivityManager.bindProcessToNetwork(network)
        // WiFi connection is a success.
        _isWiFiConnected.value = true
    }

}