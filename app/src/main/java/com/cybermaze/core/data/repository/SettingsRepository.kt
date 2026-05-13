package com.cybermaze.core.data.repository

import com.cybermaze.core.data.database.dao.SettingsDao
import com.cybermaze.core.data.database.entity.SettingsEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Thin abstraction over [SettingsDao]. Stores a single SettingsEntity row.
 */
@Singleton
class SettingsRepository @Inject constructor(
    private val settingsDao: SettingsDao
) {

    fun getSettings(): Flow<SettingsEntity?> = settingsDao.getSettings()

    suspend fun saveSettings(settings: SettingsEntity) = settingsDao.saveSettings(settings)

    suspend fun setSoundEnabled(enabled: Boolean) = settingsDao.setSoundEnabled(enabled)

    suspend fun setVibrationEnabled(enabled: Boolean) = settingsDao.setVibrationEnabled(enabled)

    suspend fun setControlType(type: String) = settingsDao.setControlType(type)

    /**
     * Write the default settings row iff it doesn't exist. Safe to call from
     * multiple call sites — won't clobber user-tweaked values on app start.
     */
    suspend fun initializeSettings() {
        val current = settingsDao.getSettings().first()
        if (current == null) {
            settingsDao.saveSettings(SettingsEntity())
        }
    }
}
