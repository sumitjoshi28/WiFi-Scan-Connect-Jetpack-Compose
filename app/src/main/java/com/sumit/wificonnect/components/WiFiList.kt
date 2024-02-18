package com.sumit.wificonnect.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun WiFiList(networks: SnapshotStateList<String>) {
    Column(modifier = Modifier
        .fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(
                    start = 16.dp,
                    end = 16.dp
                )
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Here are the nearby wifi list")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Select a wifi to connect")

            LazyColumn {
                items(networks) { ssid ->
                    WiFiCard(
                        onItemClick = {
                            // TODO Show prompt to enter wifi password.
                        },
                        wiFiSSID = ssid
                    )
                }
            }
        }
    }
}