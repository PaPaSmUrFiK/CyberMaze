package com.cybermaze.core.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cybermaze.core.ui.components.NeonButton
import com.cybermaze.core.ui.theme.CyberColors

/**
 * Placeholder screen for levels that haven't been implemented yet.
 */
@Composable
fun PlaceholderLevelScreen(
    levelNumber: Int,
    onBack: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberColors.Background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "LEVEL $levelNumber",
                color = CyberColors.NeonCyan,
                fontSize = 32.sp
            )
            
            Text(
                text = "Coming Soon",
                color = CyberColors.TextSecondary,
                fontSize = 18.sp
            )
            
            Text(
                text = "This level will be implemented by a student",
                color = CyberColors.TextSecondary,
                fontSize = 14.sp
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            NeonButton(
                text = "BACK",
                onClick = onBack
            )
        }
    }
}

/**
 * About screen.
 */
@Composable
fun AboutScreen(
    onBackClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberColors.Background)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ABOUT",
                    color = CyberColors.NeonCyan,
                    fontSize = 32.sp
                )
                
                NeonButton(
                    text = "BACK",
                    onClick = onBackClick,
                    modifier = Modifier.width(120.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Text(
                text = "CYBER MAZE",
                color = CyberColors.NeonGreen,
                fontSize = 28.sp
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "A collaborative student project",
                color = CyberColors.TextPrimary,
                fontSize = 16.sp
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Built with Kotlin & Jetpack Compose",
                color = CyberColors.TextSecondary,
                fontSize = 14.sp
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "10 unique levels designed by 10 developers",
                color = CyberColors.TextPrimary,
                fontSize = 14.sp
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Version 1.0.0",
                color = CyberColors.TextSecondary,
                fontSize = 12.sp
            )
        }
    }
}
