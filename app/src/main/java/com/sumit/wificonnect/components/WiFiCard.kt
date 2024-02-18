package com.sumit.wificonnect.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sumit.wificonnect.ui.theme.Purple80

@Composable
fun WiFiCard(onItemClick: () -> Unit, wiFiSSID: String){
    Surface(modifier = Modifier
        .padding(top = 16.dp)
        .fillMaxWidth()
        .clickable(onClick = onItemClick)
        .height(60.dp),
        shape = RoundedCornerShape(10.dp),
        color = Purple80
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(24.dp))
            Text(text = wiFiSSID)
        }
    }
}