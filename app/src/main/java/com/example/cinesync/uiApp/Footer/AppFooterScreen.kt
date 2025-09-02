package com.example.cinesync.uiApp.Footer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.cinesync.ui.theme.CustomBlue
import com.example.cinesync.uiApp.Search.SearchScreenEvent

@Composable
fun AppFooter(
    modifier: Modifier = Modifier,
    onSearchClick: () -> Unit,
    onGlobalWatchlistClick: () -> Unit,
    onLocalWatchlistClick: () -> Unit
) {
    BottomAppBar(
        modifier = modifier.fillMaxWidth(),
        containerColor = Color.White,
        windowInsets = WindowInsets(0, 0, 0, 0)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = onSearchClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = CustomBlue,
                    contentColor = Color.White
                )
            ) {
                Icon(Icons.Default.Search, contentDescription = "Search")
                Text(text = "Search", modifier = Modifier.padding(start = 8.dp))
            }

            // Global Watchlist Button
            Button(
                onClick = onGlobalWatchlistClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = CustomBlue,
                    contentColor = Color.White
                )
            ) {
                Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Global Watchlist")
                Text(text = "Global", modifier = Modifier.padding(start = 8.dp))
            }

            // Local Watchlist Button
            Button(
                onClick = onLocalWatchlistClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = CustomBlue,
                    contentColor = Color.White
                )
            ) {
                Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Local Watchlist")
                Text(text = "Local", modifier = Modifier.padding(start = 8.dp))
            }
        }
    }
}
