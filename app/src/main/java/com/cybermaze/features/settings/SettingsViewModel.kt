package com.cybermaze.features.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cybermaze.core.data.database.entity.SettingsEntity
import com.cybermaze.core.data.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for settings screen.
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    
    private val _settings = MutableStateFlow<SettingsEntity?>(null)
    val settings: StateFlow<SettingsEntity?> = _settings.asStateFlow()
    
    init {
        loadSettings()
    }
    
    /**
     * Loads settings from database.
     */
    private fun loadSettings() {
        viewModelScope.launch {
            // Initialize default settings if none exist
            settingsRepository.initializeSettings()
            
            settingsRepository.getSettings().collect { settings ->
                _settings.value = settings
            }
        }
    }
    
    /**
     * Toggles sound on/off.
     */
    fun setSoundEnabled(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setSoundEnabled(enabled)
        }
    }
    
    /**
     * Toggles vibration on/off.
     */
    fun setVibrationEnabled(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setVibrationEnabled(enabled)
        }
    }
    
    /**
     * Sets control type (SWIPE or DPAD).
     */
    fun setControlType(type: String) {
        viewModelScope.launch {
            settingsRepository.setControlType(type)
        }
    }
}
