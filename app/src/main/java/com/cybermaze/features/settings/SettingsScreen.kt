package com.cybermaze.features.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cybermaze.core.ui.components.NeonButton
import com.cybermaze.core.ui.theme.CyberColors

/**
 * Settings screen.
 */
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {}
) {
    val settings by viewModel.settings.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberColors.Background)
            .padding(24.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "SETTINGS",
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
        
        settings?.let { s ->
            // Sound setting
            SettingRow(
                label = "Sound",
                checked = s.soundEnabled,
                onCheckedChange = { viewModel.setSoundEnabled(it) }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Vibration setting
            SettingRow(
                label = "Vibration",
                checked = s.vibrationEnabled,
                onCheckedChange = { viewModel.setVibrationEnabled(it) }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Control type
            Text(
                text = "Control Type: ${s.controlType}",
                color = CyberColors.TextPrimary,
                fontSize = 18.sp
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                NeonButton(
                    text = "SWIPE",
                    onClick = { viewModel.setControlType("SWIPE") },
                    modifier = Modifier.width(150.dp),
                    color = if (s.controlType == "SWIPE") CyberColors.NeonGreen else CyberColors.TextSecondary
                )
                
                NeonButton(
                    text = "D-PAD",
                    onClick = { viewModel.setControlType("DPAD") },
                    modifier = Modifier.width(150.dp),
                    color = if (s.controlType == "DPAD") CyberColors.NeonGreen else CyberColors.TextSecondary
                )
            }
        }
    }
}

/**
 * Single setting row with toggle.
 */
@Composable
private fun SettingRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = CyberColors.TextPrimary,
            fontSize = 20.sp
        )
        
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = CyberColors.NeonGreen,
                checkedTrackColor = CyberColors.NeonGreen.copy(alpha = 0.5f),
                uncheckedThumbColor = CyberColors.TextSecondary,
                uncheckedTrackColor = CyberColors.TextSecondary.copy(alpha = 0.3f)
            )
        )
    }
}
