# WiFi-Scan-Connect-Jetpack-Compose
This repository is created to solve the problem of WiFi APIs implementation in Android 10 and above. There had been so many changes happened in Android 10 and google decided to kill the good old WiFiManager
https://developer.android.com/reference/android/net/wifi/WifiManager as so many APIs are deprecated in that. 

Scanning nearby wifi using WiFiManager still works as the user needs to ask for ACCESS_FINE_LOCATION but making connection to wifi is deprecated using the WifiManager and won't work.

So to solve the wifi problems two new APIs are introduced
- NetworkSpecifier https://developer.android.com/reference/android/net/NetworkSpecifier
- WifiNetworkSuggestion https://developer.android.com/reference/android/net/wifi/WifiNetworkSuggestion

However, NetworkSpecifier is used for P2P (Peer to Peer) connection only as it is used mostly to configure your IoT devices to connect to its local network. Creating a connection using this API does not provide an internet connection to the app or to the device.

The other WifiNetworkSuggestion can be used in your app to add network credentials for a device to auto-connect to a Wi-Fi access point. You can supply suggestions for which network to connect to using this API, but this API is not developer friendly, from the code we can provide suggestion to connect to a network but it doesn't work except for sometimes. A user declining the network suggestion notification removes the CHANGE_WIFI_STATE permission from the app.

So If you want to connect to a local network and then connect back to youe home network you can use a combination of NetworkSpecifier and Settings.Panel https://developer.android.com/reference/android/provider/Settings.Panel APIs.

## Permission Needed
- Runtime Permission: ACCESS_FINE_LOCATION
- Manifest Permission: 
  - ACCESS_WIFI_STATE 
  - CHANGE_WIFI_STATE
  - CHANGE_NETWORK_STATE
  - ACCESS_NETWORK_STATE
